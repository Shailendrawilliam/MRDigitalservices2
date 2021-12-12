package com.mrmulti.Dashboard.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.mrmulti.BalanceCheck.dto.BalanceCheckResponse;
import com.mrmulti.BrowsePlan.ui.SelectPlanOption;
import com.mrmulti.DMR.dto.TABLE;
import com.mrmulti.DMR.ui.BeneficiaryListScreen;
import com.mrmulti.DthBook.ui.DthBookingActivity;
import com.mrmulti.Login.dto.GetOperatorResponse;
import com.mrmulti.R;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.dto.BankDetail;
import com.mrmulti.Util.dto.BankDetailResponse;
import com.mrmulti.Util.dto.Operator;
import com.mrmulti.Util.dto.OperatorList;
import com.mrmulti.Util.ui.BankDetailList;
import com.mrmulti.Util.ui.BankListScreen;
import com.mrmulti.Util.ui.ContactListScreen;
import com.mrmulti.Util.ui.ListScreen;
import com.mrmulti.Util.ui.PinPasswordMatcher;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Lalit on 08-04-2017.
 */

public class ServiceFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ServiceFragment.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static ServiceFragment mInstance;
    private static final int RESULT_LOAD_IMAGE = 5;
    ImageView thumbnail;
    RelativeLayout importContactLayout;
    ImageView importContact;
    EditText stdCode, number, operator, amount, bankFund, transactionId;
    AppCompatButton payButton;
    private ProgressDialog mProgressDialog = null;
    Bitmap resizedBitmap;
    RelativeLayout formContainer;
    RelativeLayout optionMenu;
    CardView card_view, card_view_dmr;
  //  private ImagePicker imagePicker;
    GetOperatorResponse circleob;
    View addBeneficiary, dmrDashboard;
String lookUpApi1="",op_by_service,StdCode1,amout1;
    //dmrdashboard_login/////////////////////////////////////
    RelativeLayout loginContainer, dashboardDMR, currentLogin;
    RadioButton loginSenderRadio, createSenderRadio;
    TextView loginLabel, loginButton;
    EditText senderLoginNumber, senderName,remark,AmountInWords;
    TextView beneficiaryDetail;
    ImageView senderLogout;
    TextView  payment_slip;
    RelativeLayout currentLogoutContainer;
    TextView currentMobile, currentName;
    //add_beneficiary/////////////////////////////////////
    EditText beneficiaryName, beneficiaryNumber, bank, accountNumber, ifscCode, ifsc;
    TextView create, accVerify, senderNumber;
    ///////////////////////////////////////
String encoded="";
    private File selectedImageFile;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    boolean categoryFlag = false;
    boolean fundSelected = false;
    int fundSubMenu = 0;


    private TextView messageText;

    private ImageView imageview;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagepath=null;
    RelativeLayout rechargeCategory, dmrCategory, fundCategory, dmrLayout, fundLayout, rechargesLayout;
    CardView card_view1, card_view2, card_view3, card_view4, card_view5, card_view6, card_view7, card_view8, card_view9,
            card_view10, card_view11, card_view12, card_view13, card_view113 ,card_view31, card_view131, card_view41,card_view19,card_view191;
    AlertDialog.Builder alertBuilder;

    RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8,
            radio9, radio10, radio11, radio12, radio13, radio31, radio131,radio113, radio41,radio19,radio191;
    ImageView rechargeCategorySelection, fundCategorySelection, dmrCategorySelection;

    LinearLayout rechargeType;
    RelativeLayout prepaidSelLayout, postpaidSelLayout;
    RadioButton prepaidRadio, postpaidRadio;
    TextView prepaidLabel, postpaidLabel;
    String flagSelectedService = "mobile";
    String mobileType = "prepaid",circle_selected;

    int operatorSelectedId;
    String operatorSelected;

    TABLE senderTableInfo;
    TextView headerNumber;
    TextView kycText;
    TextView name;
    TextView currency;
    TextView limitUsed;
    TextView remaining;

    String bankId;
    String bankName;
    String accVerification;
    String shortCode;
    String verified;

    /////////////////////////////////////
    RelativeLayout opImgContainer;
    ImageView opImg, opImgLeft;
    AppCompatTextView browsePlanButton,info_custa;
    AppCompatTextView ROfferButton;
TextView info;
    String part1 = "";
    String part2 = "";
    String part3 = "";

    String contactSelected, contactSelectedNumber;

    String operatorSelectedForPlan = "";
    String IReffOp = "";
    String IReffCircle = "";

    ArrayList<BankDetail> bankDetails = new ArrayList<>();

    boolean flagElectricity = false;
    String paramValue1 = "";
    String paramValue2 = "";
    String paramValue3 = "";
    String paramValue4 = "";
    String ROffer = "";
    ImageUtils imageUtils ;
    boolean flagOpFetched = true;
String base64String;
    TextView tvROffers,tvCircle;ImageView imageView;




    private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
     /////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_three, container, false);
        tvCircle  = (TextView)  rootView.findViewById(R.id.circle);
        mProgressDialog = new ProgressDialog(getActivity());
          imageView = (ImageView) rootView.findViewById(R.id.imgView);

        formContainer = (RelativeLayout) rootView.findViewById(R.id.formContainer);
        optionMenu = (RelativeLayout) rootView.findViewById(R.id.optionMenu);
        card_view = (CardView) rootView.findViewById(R.id.card_view);
        card_view_dmr = (CardView) rootView.findViewById(R.id.card_view_dmr);

        addBeneficiary = (View) rootView.findViewById(R.id.addBeneficiary);
        dmrDashboard = (View) rootView.findViewById(R.id.dmrDashboard);


        opImgContainer = (RelativeLayout) rootView.findViewById(R.id.opImgContainer);
        opImg = (ImageView) rootView.findViewById(R.id.opImg);
        opImgLeft = (ImageView) rootView.findViewById(R.id.opImgLeft);
        browsePlanButton = (AppCompatTextView) rootView.findViewById(R.id.browsePlanButton);
        info_custa = (AppCompatTextView) rootView.findViewById(R.id.info_custa);
        info = (TextView) rootView.findViewById(R.id.info);
        ROfferButton = (AppCompatTextView) rootView.findViewById(R.id.RofferButton);
        payment_slip =  rootView.findViewById(R.id.payment_slip);
        remark =   rootView.findViewById(R.id.remark);
        AmountInWords =   rootView.findViewById(R.id.AmountInWords);
        /////////////////////////////////////////////////////////////////////////////////////////
        loginContainer = (RelativeLayout) dmrDashboard.findViewById(R.id.loginContainer);
        dashboardDMR = (RelativeLayout) dmrDashboard.findViewById(R.id.dashboardDMR);
        currentLogin = (RelativeLayout) dmrDashboard.findViewById(R.id.currentLogin);
        loginSenderRadio = (RadioButton) dmrDashboard.findViewById(R.id.loginSenderRadio);
        createSenderRadio = (RadioButton) dmrDashboard.findViewById(R.id.createSenderRadio);
        loginLabel = (TextView) dmrDashboard.findViewById(R.id.loginLabel);
        loginButton = (TextView) dmrDashboard.findViewById(R.id.loginButton);
        senderLoginNumber = (EditText) dmrDashboard.findViewById(R.id.senderLoginNumber);
        senderName = (EditText) dmrDashboard.findViewById(R.id.senderName);
          imageUtils =new ImageUtils();
        headerNumber = (TextView) dmrDashboard.findViewById(R.id.headerNumber);
        kycText = (TextView) dmrDashboard.findViewById(R.id.kycText);
        name = (TextView) dmrDashboard.findViewById(R.id.name);
        currency = (TextView) dmrDashboard.findViewById(R.id.currency);
        limitUsed = (TextView) dmrDashboard.findViewById(R.id.limitUsed);
        remaining = (TextView) dmrDashboard.findViewById(R.id.remaining);
        beneficiaryDetail = (TextView) dmrDashboard.findViewById(R.id.beneficiaryDetail);
        senderLogout = (ImageView) dmrDashboard.findViewById(R.id.senderLogout);

        currentLogoutContainer = (RelativeLayout) dmrDashboard.findViewById(R.id.currentLogoutContainer);
        currentMobile = (TextView) dmrDashboard.findViewById(R.id.currentMobile);
        currentName = (TextView) dmrDashboard.findViewById(R.id.currentName);

        browsePlanButton.setOnClickListener(this);
        info_custa.setOnClickListener(this);
        ROfferButton.setOnClickListener(this);
        currentLogoutContainer.setOnClickListener(this);
        senderLogout.setOnClickListener(this);
        beneficiaryDetail.setOnClickListener(this);
        loginSenderRadio.setOnClickListener(this);
        createSenderRadio.setOnClickListener(this);
        loginButton.setOnClickListener(this);


        /////////////////////////////////////////////////////////////////////////////////////////
        beneficiaryName = (EditText) addBeneficiary.findViewById(R.id.beneficiaryName);
        beneficiaryNumber = (EditText) addBeneficiary.findViewById(R.id.beneficiaryNumber);
        bank = (EditText) addBeneficiary.findViewById(R.id.bank);
        accountNumber = (EditText) addBeneficiary.findViewById(R.id.accountNumber);
        ifscCode = (EditText) addBeneficiary.findViewById(R.id.ifscCode);
        ifsc = (EditText) addBeneficiary.findViewById(R.id.ifsc);

        senderNumber = (TextView) addBeneficiary.findViewById(R.id.senderNumber);
        accVerify = (TextView) addBeneficiary.findViewById(R.id.accVerify);
        create = (TextView) addBeneficiary.findViewById(R.id.create);

        accVerify.setOnClickListener(this);
        bank.setOnClickListener(this);
        create.setOnClickListener(this);
        payment_slip.setOnClickListener(this);
        /////////////////////////////////////////////////////////////////////////////////////////

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                height / 2 - 100);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        optionMenu.setLayoutParams(params);

        RelativeLayout.LayoutParams paramsForm = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                (height / 2));
        paramsForm.addRule(RelativeLayout.ABOVE, optionMenu.getId());
        paramsForm.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsForm.setMargins(0, 0, 0, 20);
        formContainer.setLayoutParams(paramsForm);

        importContactLayout = (RelativeLayout) rootView.findViewById(R.id.importContactLayout);
        importContact = (ImageView) rootView.findViewById(R.id.importContact);
        thumbnail = (ImageView) rootView.findViewById(R.id.thumbnail);
        stdCode = (EditText) rootView.findViewById(R.id.stdCode);
        number = (EditText) rootView.findViewById(R.id.number);
        tvROffers = (TextView) rootView.findViewById(R.id.tv_roffers);
        operator = (EditText) rootView.findViewById(R.id.operator);
        amount = (EditText) rootView.findViewById(R.id.amount);
        bankFund = (EditText) rootView.findViewById(R.id.bankFund);
        transactionId = (EditText) rootView.findViewById(R.id.transactionId);

        importContactLayout.setOnClickListener(this);
        thumbnail.setOnClickListener(this);
        operator.setOnClickListener(this);
        bankFund.setOnClickListener(this);

        payButton = (AppCompatButton) rootView.findViewById(R.id.payButton);
        payButton.setOnClickListener(this);

        rechargeCategory = (RelativeLayout) rootView.findViewById(R.id.rechargeCategory);
        fundCategory = (RelativeLayout) rootView.findViewById(R.id.fundCategory);
        dmrCategory = (RelativeLayout) rootView.findViewById(R.id.dmrCategory);
        fundLayout = (RelativeLayout) rootView.findViewById(R.id.fundLayout);
        dmrLayout = (RelativeLayout) rootView.findViewById(R.id.dmrLayout);
        rechargesLayout = (RelativeLayout) rootView.findViewById(R.id.rechargesLayout);



        card_view1 = (CardView) rootView.findViewById(R.id.card_view1);
        card_view2 = (CardView) rootView.findViewById(R.id.card_view2);
        card_view3 = (CardView) rootView.findViewById(R.id.card_view3);
        card_view4 = (CardView) rootView.findViewById(R.id.card_view4);

        card_view5 = (CardView) rootView.findViewById(R.id.card_view5);
        card_view6 = (CardView) rootView.findViewById(R.id.card_view6);
        card_view7 = (CardView) rootView.findViewById(R.id.card_view7);
        card_view8 = (CardView) rootView.findViewById(R.id.card_view8);

        card_view9 = (CardView) rootView.findViewById(R.id.card_view9);
        card_view10 = (CardView) rootView.findViewById(R.id.card_view10);
        card_view11 = (CardView) rootView.findViewById(R.id.card_view11);
        card_view12 = (CardView) rootView.findViewById(R.id.card_view12);
        card_view13 = (CardView) rootView.findViewById(R.id.card_view13);
        card_view113 = (CardView) rootView.findViewById(R.id.card_view113);
        card_view31 = (CardView) rootView.findViewById(R.id.card_view31);
        card_view131 = (CardView) rootView.findViewById(R.id.card_view131);
        card_view41 = (CardView) rootView.findViewById(R.id.card_view41);
        card_view19 = (CardView) rootView.findViewById(R.id.card_view19);
        card_view191 = (CardView) rootView.findViewById(R.id.card_view191);

        radio1 = (RadioButton) rootView.findViewById(R.id.radio1);
        radio2 = (RadioButton) rootView.findViewById(R.id.radio2);
        radio3 = (RadioButton) rootView.findViewById(R.id.radio3);
        radio4 = (RadioButton) rootView.findViewById(R.id.radio4);
        radio5 = (RadioButton) rootView.findViewById(R.id.radio5);
        radio6 = (RadioButton) rootView.findViewById(R.id.radio6);
        radio7 = (RadioButton) rootView.findViewById(R.id.radio7);
        radio8 = (RadioButton) rootView.findViewById(R.id.radio8);

        radio9 = (RadioButton) rootView.findViewById(R.id.radio9);
        radio10 = (RadioButton) rootView.findViewById(R.id.radio10);
        radio11 = (RadioButton) rootView.findViewById(R.id.radio11);
        radio12 = (RadioButton) rootView.findViewById(R.id.radio12);
        radio13 = (RadioButton) rootView.findViewById(R.id.radio13);
        radio113 = (RadioButton) rootView.findViewById(R.id.radio113);
        radio31 = (RadioButton) rootView.findViewById(R.id.radio31);
        radio131 = (RadioButton) rootView.findViewById(R.id.radio131);
        radio41 = (RadioButton) rootView.findViewById(R.id.radio41);
        radio19 = (RadioButton) rootView.findViewById(R.id.radio19);
        radio191 = (RadioButton) rootView.findViewById(R.id.radio191);
        card_view1.setOnClickListener(this);
        card_view2.setOnClickListener(this);
        card_view3.setOnClickListener(this);
        card_view4.setOnClickListener(this);
        card_view5.setOnClickListener(this);
        card_view6.setOnClickListener(this);
        card_view7.setOnClickListener(this);
        card_view8.setOnClickListener(this);
        card_view8.setVisibility(View.GONE);
        card_view9.setOnClickListener(this);
        card_view10.setOnClickListener(this);
        card_view11.setOnClickListener(this);
        card_view11.setVisibility(View.GONE);
        card_view12.setOnClickListener(this);
        card_view12.setVisibility(View.GONE);
        card_view13.setOnClickListener(this);
        card_view113.setOnClickListener(this);
        card_view31.setOnClickListener(this);
        card_view131.setOnClickListener(this);
        card_view41.setOnClickListener(this);
        card_view19.setOnClickListener(this);
        card_view191.setOnClickListener(this);

        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);
        radio4.setOnClickListener(this);
        radio5.setOnClickListener(this);
        radio6.setOnClickListener(this);
        radio7.setOnClickListener(this);
        radio8.setOnClickListener(this);
        radio9.setOnClickListener(this);
        radio10.setOnClickListener(this);
        radio11.setOnClickListener(this);
        radio12.setOnClickListener(this);
        radio13.setOnClickListener(this);
        radio113.setOnClickListener(this);
        radio31.setOnClickListener(this);
        radio131.setOnClickListener(this);
        radio41.setOnClickListener(this);
        radio19.setOnClickListener(this);
        radio191.setOnClickListener(this);
        alertBuilder = new AlertDialog.Builder(getActivity());
        rechargeCategorySelection = (ImageView) rootView.findViewById(R.id.rechargeCategorySelection);
        fundCategorySelection = (ImageView) rootView.findViewById(R.id.fundCategorySelection);
        dmrCategorySelection = (ImageView) rootView.findViewById(R.id.dmrCategorySelection);
        ////////////////////////////////////////
        rechargeType = (LinearLayout) rootView.findViewById(R.id.rechargeType);
        prepaidSelLayout = (RelativeLayout) rootView.findViewById(R.id.prepaidSelLayout);
        postpaidSelLayout = (RelativeLayout) rootView.findViewById(R.id.postpaidSelLayout);
        prepaidRadio = (RadioButton) rootView.findViewById(R.id.prepaidRadio);
        postpaidRadio = (RadioButton) rootView.findViewById(R.id.postpaidRadio);

        prepaidLabel = (TextView) rootView.findViewById(R.id.prepaidLabel);
        postpaidLabel = (TextView) rootView.findViewById(R.id.postpaidLabel);

        rechargeCategory.setOnClickListener(this);
        fundCategory.setOnClickListener(this);
        dmrCategory.setOnClickListener(this);
        prepaidSelLayout.setOnClickListener(this);
        postpaidSelLayout.setOnClickListener(this);
        prepaidRadio.setOnClickListener(this);
        postpaidRadio.setOnClickListener(this);
//
        /////////////////////////////////////////////////////////////////


        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                     if (fundSelected){
                         operator.setVisibility(View.GONE);
                         opImgLeft.setVisibility(View.GONE);
                     }
                     else {

                     }
                tvCircle.setVisibility(View.GONE);
                opImg.setVisibility(View.GONE);
                opImgLeft.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (fundSelected){
                    operator.setVisibility(View.GONE);
                    opImgLeft.setVisibility(View.GONE);
                }
                else {
                   // operator.setText("");
                    if (s.length() == 4) {
                        if (!prepaidRadio.isChecked()){
                            if (flagOpFetched) {
                                fetchSCode(s.toString());
                            } else if (s.length() < 4) {
                               //  operator.setText("");
                                opeImgMethod("");
                                part1 = "";
                                part2 = "";
                                part3 = "";

                                operatorSelected = "";
                                operatorSelectedId = 0;
                            }
                        }
                    }
                    if(number.getText().toString().length() == 10 && mobileType.equalsIgnoreCase("prepaid")) {
                        if (radio1.isChecked() && prepaidRadio.isChecked()) {
                            mProgressDialog.setTitle("Loading..");
                            mProgressDialog.show();
                            UtilMethods.INSTANCE.LookUpApi(getActivity(), number.getText().toString(), mProgressDialog, new UtilMethods.ApiCallBack() {
                                @Override
                                public void onSucess(Object object) {

                                }
                            });
                        }  }
                    else
                    {
                        //  tvROffers.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //show_hide_roleID//////////////////////////////////////////////////////////////////////////////////
        if (!UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("3")) {
            dmrCategory.setVisibility(View.GONE);
            card_view5.setVisibility(View.VISIBLE);
        } else {
            if (UtilMethods.INSTANCE.getDMRService(getActivity()) != null && UtilMethods.INSTANCE.getDMRService(getActivity()).equalsIgnoreCase("0"))
                dmrCategory.setVisibility(View.GONE);
            else
                dmrCategory.setVisibility(View.VISIBLE);
            card_view5.setVisibility(View.GONE);
        }

        if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("0")) {
            card_view6.setVisibility(View.GONE);
        } else {
            card_view6.setVisibility(View.VISIBLE);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////

        return rootView;
    }

    public void opeImgMethod(final String imgName) {

        String imgTemp = imgName.toLowerCase().replace(" ", "");
        String imgTemp1 = imgTemp.toLowerCase().replace("-", "");
        String imgTemp2 = imgTemp1.toLowerCase().replace("&", "");
        String imgTemp3 = imgTemp2.toLowerCase().replace("_", "");
        String imgTemp4 = imgTemp3.substring(0, imgTemp3.length());
    Log.e("img is ","imgeselected "+imgTemp4);
    if (imgTemp4!="jiospecial"){
        int resourceId = getResources().getIdentifier(
                imgTemp4 + "rec", "drawable", getActivity().getPackageName());
        if (resourceId != 0) {
            opImg.setVisibility(View.VISIBLE);
            opImgLeft.setVisibility(View.GONE);

            //un comment
         /*   Glide.with(this).load(resourceId)
                    .bitmapTransform(new RoundedCornersTransformation(getActivity(), 0, 0))
                    .into(opImg);*/

        } else {
            opImg.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.VISIBLE);
        }
    }else {
        int resourceId = getResources().getIdentifier(
                "reliancejio" + "rec", "drawable", getActivity().getPackageName());
        Log.e("resourceid  is ", String.valueOf(resourceId));
        if (resourceId != 0) {
            opImg.setVisibility(View.VISIBLE);
            opImgLeft.setVisibility(View.GONE);

           // Glide.with(this).load(R.drawable.reliancejio).into(opImg);
            opImg.setImageAlpha(R.drawable.reliancejio);

        } else {
            opImg.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.VISIBLE);
        }
    }

    }

    public void setFalse(LinearLayout rechargeType, EditText number) {
        radio1.setChecked(false);
        radio2.setChecked(false);
        radio3.setChecked(false);
        radio4.setChecked(false);
        radio5.setChecked(false);
        radio6.setChecked(false);
        radio7.setChecked(false);
        radio8.setChecked(false);
        info.setVisibility(View.GONE);
        radio9.setChecked(false);
        radio10.setChecked(false);
        radio11.setChecked(false);
        radio12.setChecked(false);
        radio13.setChecked(false);
        radio113.setChecked(false);
        radio31.setChecked(false);
        radio131.setChecked(false);
        radio41.setChecked(false);
        radio19.setChecked(false);
        operator.setVisibility(View.GONE);
       //  opImgLeft.setVisibility(View.GONE);
        radio191.setChecked(false);
        rechargeType.setVisibility(View.GONE);
        bankFund.setVisibility(View.GONE);
        transactionId.setVisibility(View.GONE);
        browsePlanButton.setVisibility(View.GONE);
        ROfferButton.setVisibility(View.GONE);
        stdCode.setVisibility(View.GONE);
        number.setHint("");
        operator.setText("");
        number.setText("");
        amount.setText("");
        bank.setText("");
        bankFund.setText("");

        remark.setText("");
        AmountInWords.setText("");

        transactionId.setText("");

        opImg.setVisibility(View.GONE);
        opImgLeft.setVisibility(View.VISIBLE);
        thumbnail.setVisibility(View.GONE);
        importContactLayout.setVisibility(View.GONE);
        flagOpFetched = false;
        fundSelected = false;

        flagElectricity = false;
        part1 = "";
        part2 = "";
        part3 = "";
        operatorSelected = "";
        operatorSelectedId = 0;


    }

    public void rechargePlan(String opId, String zoneId, String operator) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            UtilMethods.INSTANCE.RechargePlans(getActivity(), opId, zoneId, operator, mProgressDialog);
        } else {
            UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message), 2);
        }
    }

    public void BankDetailFund() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
        String response = myPrefs.getString(ApplicationConstant.INSTANCE.bankDetailListPref, null);

        BankDetailResponse bankDetailResponse = new Gson().fromJson(response, BankDetailResponse.class);
        bankDetails = bankDetailResponse.getBanks();
    }

    @Override
    public void onClick(View v) {
        if (v == payment_slip) {

           // imagePicker.choosePicture(true);

         Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
//*/         //   Intent intent = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, RESULT_LOAD_IMAGE);
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RESULT_LOAD_IMAGE);


        }


        if (v == payButton) {

            if (UtilMethods.INSTANCE.getRegKey(getActivity()) != null &&
                    UtilMethods.INSTANCE.getRegKey(getActivity()).length() > 0) {
                if (!fundSelected) {
                    if (validateForm() == 0) {
                        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                        boolean pinPass = myPreferences.getBoolean(ApplicationConstant.INSTANCE.PinPasscodeFlag, false);
                        if (pinPass) {
                            Intent passIntent = new Intent(getActivity(), PinPasswordMatcher.class);
                            startActivityForResult(passIntent, 3);
                        } else {
                            //Log.e("here", "operatorSelectedId : " + operatorSelectedId);
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.layout_confirmation, null);

                           TextView number1 = (TextView) view.findViewById(R.id.number);
                          TextView operator1 = (TextView) view.findViewById(R.id.operator);
                          TextView amount1 = (TextView) view.findViewById(R.id.amount);
                          TextView okButton =  view.findViewById(R.id.ok);
                          TextView cancelButton =   view.findViewById(R.id.cancel);
                        number1.setText("Recharge No : " + number.getText().toString());
                        operator1.setText("Operator : "+ operator.getText().toString());
                        amount1.setText(" Amount : "+amount.getText().toString());
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setCancelable(false);
                        dialog.setContentView(view);

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                                boolean pinPass = myPreferences.getBoolean(ApplicationConstant.INSTANCE.PinPasscodeFlag, false);
                                if (pinPass) {
                                    Intent passIntent = new Intent(getActivity(), PinPasswordMatcher.class);
                                    startActivityForResult(passIntent, 3);
                                } else {
                                    if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                        payButton.setEnabled(false);
                                        payButton.setBackgroundColor(getResources().getColor(R.color.bottommenu));
                                        mProgressDialog.setIndeterminate(true);
                                        mProgressDialog.setMessage("Loading...");
                                        mProgressDialog.show();
                                        mProgressDialog.setCancelable(false);

                                        if (flagSelectedService.equalsIgnoreCase("broadband")) {

                                            SharedPreferences myPref = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                                            StdCode1 = myPref.getString(ApplicationConstant.INSTANCE.reffCode, null);
                                            Log.e("reffCode",StdCode1);
                                            UtilMethods.INSTANCE.afterLogintoPreviousWindow(getActivity(), number.getText().toString().trim().toString(),
                                                    amount.getText().toString().trim(), "" + operatorSelectedId,StdCode1+","+stdCode.getText().toString().trim(),
                                                    flagElectricity, paramValue1, paramValue2, paramValue3, paramValue4, mProgressDialog,payButton);
                                            Log.e("reffCode ",stdCode.getText().toString().trim()+","+StdCode1);
                                        }else {
                                            UtilMethods.INSTANCE.afterLogintoPreviousWindow(getActivity(), number.getText().toString().trim().toString(),
                                                    amount.getText().toString().trim(), "" + operatorSelectedId, stdCode.getText().toString().trim(),
                                                    flagElectricity, paramValue1, paramValue2, paramValue3, paramValue4, mProgressDialog,payButton);

                                        }


                                    } else {
                                        UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                                getResources().getString(R.string.network_error_message), 2);
                                    }}
                                dialog.dismiss();
                            }
                        });
                        dialog.show();





                        }
                    }
                }
                else {
                    if (validateFormFund(fundSubMenu) == 0) {

                        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();
                            mProgressDialog.setCancelable(false);

                            if (fundSubMenu == 1) {
                                UtilMethods.INSTANCE.afterLogintoPreviousWindowFund(getActivity(), "fundTransfer",null, number.getText().toString().trim().toString(),
                                        amount.getText().toString().trim(), "", "", mobileType,"","","","" ,mProgressDialog,payButton);
                            } else if (fundSubMenu == 2) {

                                SendImage(encoded);
//                                UtilMethods.INSTANCE.afterLogintoPreviousWindowFund(getActivity(), "",selectedImageFile, number.getText().toString().trim().toString(),
//                                        amount.getText().toString().trim(), "" + number.getText().toString().trim(), transactionId.getText().toString().trim(), mobileType,remark.getText().toString(),bank.getText().toString(),AmountInWords.getText().toString(),encoded ,mProgressDialog,payButton);
                             }
                        } else {
                            UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title), getResources().getString(R.string.network_error_message), 2);
                        }
                    }
                }

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        payButton.setEnabled(true);
                        payButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                },7000);// set time as per your requirement

            } else {
                Toast.makeText(getActivity(), "Please try after some time", Toast.LENGTH_SHORT).show();
                UtilMethods.INSTANCE.KeyUpdate(getActivity(), UtilMethods.INSTANCE.getKeyId(getActivity()));
            }
        }

        if (v == operator) {
            Intent listIntent = new Intent(getActivity(), ListScreen.class);
            listIntent.putExtra("from", "" + flagSelectedService);
            listIntent.putExtra("type", "" + mobileType);
            startActivityForResult(listIntent, 1);
        }

        if(v==info_custa){

            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();


                UtilMethods.INSTANCE.DTHCustomerInfo(getActivity(),number.getText().toString().trim(),
                        ROffer, mProgressDialog );


            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }


        }



        if (v == browsePlanButton) {

            if (flagSelectedService.equalsIgnoreCase("electricity")){

                if (operatorSelectedId!=0){
                    if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        UtilMethods.INSTANCE.GetElectricityInfo(getActivity(),number.getText().toString().trim(),
                                "" + operatorSelectedId, mProgressDialog );

                    } else {
                        UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message), 2);
                    }
                }else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.attention_error_title),
                            "Please Select operator first", 2);
                }

            }else if (flagSelectedService.equalsIgnoreCase("broadband")){

                if (operatorSelectedId!=0){
                    if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        UtilMethods.INSTANCE.GetViewBill(getActivity(),number.getText().toString().trim(),
                                "" + operatorSelectedId, mProgressDialog );

                    } else {
                        UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message), 2);
                    }
                }else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.attention_error_title),
                            "Please Select operator first", 2);
                }

            }





            else if (flagSelectedService.equalsIgnoreCase("dth")){

                if (operatorSelectedId!=0){

                    String  SCode="";

                    if(operator.getText().toString().trim().equalsIgnoreCase("Dish Tv"))
                    {
                        SCode="DD";
                    }
                    else  if(operator.getText().toString().trim().equalsIgnoreCase("Videocon D2H"))
                    {
                        SCode="DV";
                    }
                    else  if(operator.getText().toString().trim().equalsIgnoreCase("Tata Sky"))
                    {
                        SCode="DT";
                    }
                    else
                    {
                        SCode="";
                    }

                    if(SCode.equals(""))
                    {
                        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            Log.e("SCode",SCode);



                       /* UtilMethods.INSTANCE.GetDTHInfo(getActivity(),number.getText().toString().trim(),
                                    "" + operatorSelectedId, mProgressDialog );*/

                        } else {
                            UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                    getResources().getString(R.string.network_error_message), 2);
                        }
                    }
                    else
                    {
                        if (number.getText() != null && number.getText().toString().trim().length() > 0) {

                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                Log.e("SCodee",SCode);

                                UtilMethods.INSTANCE.HTMLResponse(getActivity(),number.getText().toString().trim(),SCode,mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }

                        else
                        {
                            number.setError(getResources().getString(R.string.number_error));
                            number.requestFocus();
                        }
                    }

                }else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.attention_error_title),
                            "Please Select operator first", 2);
                }
            }  else if (flagSelectedService.equalsIgnoreCase("mobile")) {
                if ((part2 != null && part2.length() > 0) || (part3 != null && part3.length() > 0)) {
                    rechargePlan(part2, part3, part1);
                } else if (operatorSelected != null && operatorSelected.length() > 0) {

                    Intent planOptionIntent = new Intent(getActivity(), SelectPlanOption.class);
                    planOptionIntent.putExtra("from", "zoneOp");
                    planOptionIntent.putExtra("opList", 1);
                    planOptionIntent.putExtra("opListName", operatorSelected);
                    planOptionIntent.putExtra("operatorId", IReffOp);
                    startActivityForResult(planOptionIntent, 4);

                } else {
                    Intent planOptionIntent = new Intent(getActivity(), SelectPlanOption.class);
                    planOptionIntent.putExtra("from", "operator");
                    planOptionIntent.putExtra("opList", 0);
                    planOptionIntent.putExtra("operatorId", "");
                    startActivityForResult(planOptionIntent, 4);
                }
            }
        }
        if(v==ROfferButton)
        {
            if(number.getText().toString().length() == 10 && !ROffer.isEmpty()  )
            {
                tvROffers.setVisibility(View.VISIBLE);
                UtilMethods.INSTANCE.GetROffer(getActivity(),number.getText().toString(),""+operatorSelectedId,null,tvROffers);
            }
            else if(number.getText().toString().length() < 10)
            {
                Toast.makeText(getActivity(), "Please Enter the Valid Number", Toast.LENGTH_SHORT).show();
                tvROffers.setVisibility(View.GONE);
            }
            else if(ROffer.isEmpty())
            {
                Toast.makeText(getActivity(), "Please Select the Operator", Toast.LENGTH_SHORT).show();
                tvROffers.setVisibility(View.GONE);
            }
            else
            {
                tvROffers.setVisibility(View.GONE);
            }
        }

        if (v == bankFund) {
            Intent bankIntent = new Intent(getActivity(), BankDetailList.class);
            startActivity(bankIntent);
        }

        if (v == rechargeCategory) {
            fundLayout.setVisibility(View.GONE);
            rechargesLayout.setVisibility(View.VISIBLE);
            dmrLayout.setVisibility(View.GONE);

            rechargeCategorySelection.setBackgroundResource(R.drawable.leftselect);
            fundCategorySelection.setBackgroundResource(R.drawable.rightdeselect);
            dmrCategorySelection.setBackgroundResource(R.drawable.centerdeselect);

            card_view.setVisibility(View.VISIBLE);
            card_view_dmr.setVisibility(View.GONE);

            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            setFalse(rechargeType, number);
            radio1.setChecked(true);
            postpaidLabel.setText("Postpaid");

            rechargeType.setVisibility(View.VISIBLE);
            number.setHint("Mobile Number");
            operator.setVisibility(View.VISIBLE);
            browsePlanButton.setVisibility(View.GONE);
            ROfferButton.setVisibility(View.VISIBLE);
            flagSelectedService = "mobile";

            importContactLayout.setVisibility(View.VISIBLE);
            flagOpFetched = true;
        }

        if (v == dmrCategory) {
            fundLayout.setVisibility(View.GONE);
            rechargesLayout.setVisibility(View.GONE);
            dmrLayout.setVisibility(View.VISIBLE);
            info_custa.setVisibility(View.GONE);
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            rechargeCategorySelection.setBackgroundResource(R.drawable.leftdeselect);
            fundCategorySelection.setBackgroundResource(R.drawable.rightdeselect);
            dmrCategorySelection.setBackgroundResource(R.drawable.centerselect);

            card_view.setVisibility(View.GONE);
            card_view_dmr.setVisibility(View.VISIBLE);

            setFalse(rechargeType, number);
            radio9.setChecked(true);

            if (IsSenderLogin()) {
                currentLogin.setVisibility(View.VISIBLE);
                loginContainer.setVisibility(View.GONE);
                dashboardDMR.setVisibility(View.GONE);

                setCurrentDetail();
            } else {
                currentLogin.setVisibility(View.GONE);
                loginContainer.setVisibility(View.VISIBLE);
                dashboardDMR.setVisibility(View.GONE);
            }

            loginSenderRadio.setChecked(true);
            createSenderRadio.setChecked(false);

            loginLabel.setText("Sender Login : ");
            loginButton.setText("Login");
            senderName.setVisibility(View.GONE);

        }

        if (v == fundCategory) {
            fundLayout.setVisibility(View.VISIBLE);
            rechargesLayout.setVisibility(View.GONE);
            dmrLayout.setVisibility(View.GONE);
            info_custa.setVisibility(View.GONE);
            flagOpFetched = false;
            rechargeCategorySelection.setBackgroundResource(R.drawable.leftdeselect);
            fundCategorySelection.setBackgroundResource(R.drawable.rightselect);
            dmrCategorySelection.setBackgroundResource(R.drawable.centerdeselect);

            card_view.setVisibility(View.VISIBLE);
            card_view_dmr.setVisibility(View.GONE);
            operator.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.GONE);
            setFalse(rechargeType, number);
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("1")) {
                radio6.setChecked(true);
                number.setHint("Account Number");
                remark.setVisibility(View.VISIBLE);
                AmountInWords.setVisibility(View.VISIBLE);
                payment_slip.setVisibility(View.VISIBLE);
                fundSelected = true;
                fundSubMenu = 2;
                bankFund.setVisibility(View.VISIBLE);
                transactionId.setVisibility(View.VISIBLE);
                operator.setVisibility(View.GONE);

                opImg.setVisibility(View.GONE);
                opImgLeft.setVisibility(View.GONE);
                browsePlanButton.setVisibility(View.GONE);
                ROfferButton.setVisibility(View.GONE);

                BankDetailFund();

                SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);
                BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceCheckResponse.class);
                String utilityWallet = balanceCheckResponse.getUtilityWallet();
                if (utilityWallet != null && utilityWallet.length() > 0) {
                    rechargeType.setVisibility(View.VISIBLE);
                    prepaidRadio.setChecked(true);
                    postpaidRadio.setChecked(false);

                    postpaidLabel.setText("Utility");
                    mobileType = "utility";
                }
            } else {
                radio5.setChecked(true);
                number.setHint("Receiver Mobile No.");

                fundSelected = true;
                fundSubMenu = 1;
                operator.setVisibility(View.GONE);

                opImg.setVisibility(View.GONE);
                opImgLeft.setVisibility(View.GONE);
                browsePlanButton.setVisibility(View.GONE);
                ROfferButton.setVisibility(View.GONE);

                thumbnail.setVisibility(View.VISIBLE);

                SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);
                BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceCheckResponse.class);
                String utilityWallet = balanceCheckResponse.getUtilityWallet();

                if (utilityWallet != null && utilityWallet.length() > 0) {
                    rechargeType.setVisibility(View.VISIBLE);
                    prepaidRadio.setChecked(true);
                    postpaidRadio.setChecked(false);

                    postpaidLabel.setText("Utility");
                    mobileType = "prepaid";
                }
            }
        }

        if (v == prepaidSelLayout || v == prepaidRadio) {
            prepaidRadio.setChecked(true);
            postpaidRadio.setChecked(false);

            mobileType = "prepaid";
            flagOpFetched = false;
        }

        if (v == postpaidSelLayout || v == postpaidRadio) {
            prepaidRadio.setChecked(false);
            postpaidRadio.setChecked(true);
            info_custa.setVisibility(View.GONE);

            mobileType = "postpaid";
            flagOpFetched = true;
        }

        if (v == card_view1 || v == radio1) {
            setFalse(rechargeType, number);
            radio1.setChecked(true);

            rechargeType.setVisibility(View.VISIBLE);
            number.setHint("Mobile Number");
            number.setInputType(InputType.TYPE_CLASS_NUMBER );

            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            operator.setVisibility(View.VISIBLE);
            browsePlanButton.setVisibility(View.GONE);
            browsePlanButton.setText("Browse Plan");
            ROfferButton.setVisibility(View.VISIBLE);
            flagSelectedService = "mobile";
            flagOpFetched = false;
            info_custa.setVisibility(View.GONE);
            importContactLayout.setVisibility(View.VISIBLE);
        }
        if (v == card_view2 || v == radio2) {
            setFalse(rechargeType, number);
            radio2.setChecked(true);
            number.setInputType(InputType.TYPE_CLASS_NUMBER );
            number.setHint("DTH Number");
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            info_custa.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            browsePlanButton.setVisibility(View.GONE);
            browsePlanButton.setText("Customer Info");
            operator.setVisibility(View.VISIBLE);
            flagSelectedService = "dth";
        } if (v == card_view13 || v == radio13) {

            Intent beneIntent = new Intent(getActivity(), DthBookingActivity.class);
            startActivity(beneIntent);

        }
        if (v == card_view113 || v == radio113) {
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            setFalse(rechargeType, number);
            number.setHint("Broadband Number");
            radio113.setChecked(true);
            operator.setVisibility(View.VISIBLE);
            info_custa.setVisibility(View.GONE);
            ROfferButton.setVisibility(View.GONE);
            flagSelectedService = "broadband";
            stdCode.setVisibility(View.GONE);
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            browsePlanButton.setVisibility(View.VISIBLE);
            browsePlanButton.setText("View Bill");
//            Intent beneIntent = new Intent(getActivity(), DthBookingActivity.class);
//            startActivity(beneIntent);

        }
        if (v == card_view3 || v == radio3) {
            setFalse(rechargeType, number);
            radio3.setChecked(true);
            number.setHint("Landline Number");
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            operator.setVisibility(View.VISIBLE);
            info_custa.setVisibility(View.GONE);
            flagSelectedService = "landline";
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            stdCode.setVisibility(View.VISIBLE);
        }
        if (v == card_view4 || v == radio4) {
            setFalse(rechargeType, number);
            radio4.setChecked(true);
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            number.setHint("Electricity Account Number");
            browsePlanButton.setVisibility(View.VISIBLE);
            info_custa.setVisibility(View.GONE);
            browsePlanButton.setText("Customer Info");
            operator.setVisibility(View.VISIBLE);
            flagSelectedService = "electricity";

            flagElectricity = true;
        }
        if (v == card_view5 || v == radio5) {


            Intent intent = new Intent(getActivity(), DisMemberList.class);
            startActivity(intent);

            number.setInputType(InputType.TYPE_CLASS_TEXT );
            setFalse(rechargeType, number);
            radio5.setChecked(true);
            info_custa.setVisibility(View.GONE);
            number.setHint("Receiver Mobile No.");

            fundSelected = true;
            fundSubMenu = 1;
            operator.setVisibility(View.GONE);

            opImg.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.GONE);
            browsePlanButton.setVisibility(View.GONE);
            ROfferButton.setVisibility(View.GONE);

            thumbnail.setVisibility(View.VISIBLE);

            SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
            String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);
            BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceCheckResponse.class);
            String utilityWallet = balanceCheckResponse.getUtilityWallet();
            if (utilityWallet != null && utilityWallet.length() > 0) {
                rechargeType.setVisibility(View.VISIBLE);
                prepaidRadio.setChecked(true);
                postpaidRadio.setChecked(false);

                postpaidLabel.setText("Utility");
                mobileType = "prepaid";
            }
        }
        if (v == card_view6 || v == radio6) {
            setFalse(rechargeType, number);
            radio6.setChecked(true);
            info_custa.setVisibility(View.GONE);
            number.setHint("Account Number");
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            fundSelected = true;

            fundSubMenu = 2;
            bankFund.setVisibility(View.VISIBLE);
            transactionId.setVisibility(View.VISIBLE);
            operator.setVisibility(View.GONE);

            opImg.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.GONE);
            browsePlanButton.setVisibility(View.GONE);
            ROfferButton.setVisibility(View.GONE);
            remark.setVisibility(View.VISIBLE);
            AmountInWords.setVisibility(View.VISIBLE);
            payment_slip.setVisibility(View.VISIBLE);

            BankDetailFund();

            SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
            String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);
            BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceCheckResponse.class);
            String utilityWallet = balanceCheckResponse.getUtilityWallet();
            if (utilityWallet != null && utilityWallet.length() > 0) {
                rechargeType.setVisibility(View.VISIBLE);
                prepaidRadio.setChecked(true);
                postpaidRadio.setChecked(false);

                postpaidLabel.setText("Utility");
                mobileType = "utility";
            }
        }

        if (v == card_view7 || v == radio7) {
            setFalse(rechargeType, number);
            radio7.setChecked(true);
            number.setHint("Account Number");
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            fundSelected = true;
            fundSubMenu = 3;
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            operator.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.GONE);
        }
        if (v == card_view8 || v == radio8) {
            setFalse(rechargeType, number);
            radio8.setChecked(true);
            number.setHint("Account Number");
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            fundSelected = true;
            fundSubMenu = 3;
            remark.setVisibility(View.GONE);
            AmountInWords.setVisibility(View.GONE);
            payment_slip.setVisibility(View.GONE);
            operator.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.GONE);
        }

        if (v == card_view9 || v == radio9) {
            setFalse(rechargeType, number);
            radio9.setChecked(true);

            addBeneficiary.setVisibility(View.GONE);
            dmrDashboard.setVisibility(View.VISIBLE);

            if (IsSenderLogin()) {
                loginContainer.setVisibility(View.GONE);
                dashboardDMR.setVisibility(View.GONE);
                currentLogin.setVisibility(View.VISIBLE);

                setCurrentDetail();
            } else {
                loginContainer.setVisibility(View.VISIBLE);
                dashboardDMR.setVisibility(View.GONE);
                currentLogin.setVisibility(View.GONE);
            }
        }

        if (v == card_view10 || v == radio10) {
            setFalse(rechargeType, number);
            radio10.setChecked(true);

            if (IsSenderLogin()) {

                SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String senderLoginNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);
                senderNumber.setText("" + senderLoginNumber);

                addBeneficiary.setVisibility(View.VISIBLE);
                dmrDashboard.setVisibility(View.GONE);
                currentLogin.setVisibility(View.GONE);

            } else {
                addBeneficiary.setVisibility(View.GONE);
                dmrDashboard.setVisibility(View.VISIBLE);

                loginContainer.setVisibility(View.VISIBLE);
                dashboardDMR.setVisibility(View.GONE);
                currentLogin.setVisibility(View.GONE);
            }
        }

        if (v == card_view11 || v == radio11) {
            setFalse(rechargeType, number);
            radio11.setChecked(true);

            if (IsSenderLogin()) {

                SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String senderLoginNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);
                senderLogin(senderLoginNumber);

                addBeneficiary.setVisibility(View.GONE);
                dmrDashboard.setVisibility(View.VISIBLE);

                loginContainer.setVisibility(View.GONE);
                dashboardDMR.setVisibility(View.VISIBLE);
                currentLogin.setVisibility(View.GONE);

            } else {
                addBeneficiary.setVisibility(View.GONE);
                dmrDashboard.setVisibility(View.VISIBLE);

                loginContainer.setVisibility(View.VISIBLE);
                dashboardDMR.setVisibility(View.GONE);
                currentLogin.setVisibility(View.GONE);
            }
        }

        if (v == loginSenderRadio) {
            loginSenderRadio.setChecked(true);
            createSenderRadio.setChecked(false);

            loginLabel.setText("Sender Login : ");
            loginButton.setText("Login");
            senderName.setVisibility(View.GONE);
        }
        if (v == card_view31 || v == radio31) {
            setFalse(rechargeType, number);
            radio31.setChecked(true);
            number.setHint("Account Number");
//            view_bill.setVisibility(View.GONE);
//            view_plane.setVisibility(View.GONE);
            tvROffers.setVisibility(View.GONE);
            info_custa.setVisibility(View.GONE);
            number.setInputType(InputType.TYPE_CLASS_TEXT );
            operator.setVisibility(View.VISIBLE);
            flagSelectedService = "gas";

           // flagElectricity = true;
           // hide_EditText();
        }
        if (v == card_view131 || v == radio131) {
            setFalse(rechargeType, number);
            radio131.setChecked(true);
            number.setHint("Account Number");
//            view_bill.setVisibility(View.GONE);
//            view_plane.setVisibility(View.GONE);
            tvROffers.setVisibility(View.GONE);
            info_custa.setVisibility(View.GONE);
            operator.setVisibility(View.VISIBLE);
            flagSelectedService = "insurance";
            number.setInputType(InputType.TYPE_CLASS_TEXT );
          //  flagElectricity = true;
          //  hide_EditText();
        }
        if (v == card_view41 || v == radio41) {
            setFalse(rechargeType, number);
            radio41.setChecked(true);
            number.setHint("Account Number");
//            view_bill.setVisibility(View.GONE);
//            view_plane.setVisibility(View.GONE);
            tvROffers.setVisibility(View.GONE);
            info_custa.setVisibility(View.GONE);
            operator.setVisibility(View.VISIBLE);
            flagSelectedService = "water";
            number.setInputType(InputType.TYPE_CLASS_TEXT );
          //  flagElectricity = true;
            //  hide_EditText();
        }

        if (v == card_view191 || v == radio191) {



//            loginSenderRadio.setChecked(false);
//            createSenderRadio.setChecked(true);
//
//            loginLabel.setText("Create Sender : ");
//            loginButton.setText("Create");
//            senderName.setVisibility(View.VISIBLE);
        }
        if (v == card_view19 || v == radio19) {
//            setFalse(rechargeType, number);
//            radio19.setChecked(true);


            Intent intent = new Intent(getActivity(), OutletRegistrationActivity.class);
            startActivity(intent);

//
//            SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
//            String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);
//
//
//            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {
//
//                loader.show();
//                loader.setCancelable(false);
//                loader.setCanceledOnTouchOutside(false);
//
//                UtilMethods.INSTANCE.GetOutlet(getActivity(), mobileLogin, loader);
//
//
//            } else {
//                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.err_msg_network),
//                        getResources().getString(R.string.err_msg_network), 2);
//            }
        }




        if (v == createSenderRadio) {
            loginSenderRadio.setChecked(false);
            createSenderRadio.setChecked(true);

            loginLabel.setText("Create Sender : ");
            loginButton.setText("Create");
            senderName.setVisibility(View.VISIBLE);
        }

        if (v == loginButton) {
            if (loginSenderRadio.isChecked()) {
                if (validationForm("login") == 0) {
                    if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        UtilMethods.INSTANCE.GetSender(getActivity(), senderLoginNumber.getText().toString().trim(), mProgressDialog);

                    } else {
                        UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message), 2);
                    }
                }
            } else {
                if (validationForm("create") == 0) {
                    UtilMethods.INSTANCE.CreateSender(getActivity(), senderLoginNumber.getText().toString().trim(), senderName.getText().toString());
                }
            }
        }

        if (v == beneficiaryDetail) {
            Intent beneIntent = new Intent(getActivity(), BeneficiaryListScreen.class);
            startActivity(beneIntent);
        }

        if (v == senderLogout || v == currentLogoutContainer) {
            UtilMethods.INSTANCE.setSenderNumber(getActivity(), "");
            UtilMethods.INSTANCE.setSenderInfo(getActivity(), "", "", false, null);
            UtilMethods.INSTANCE.setBeneficiaryList(getActivity(), "");

            setFalse(rechargeType, number);
            radio9.setChecked(true);

            addBeneficiary.setVisibility(View.GONE);
            dmrDashboard.setVisibility(View.VISIBLE);

            loginContainer.setVisibility(View.VISIBLE);
            dashboardDMR.setVisibility(View.GONE);
            currentLogin.setVisibility(View.GONE);
        }

        if (v == bank) {
            Intent bankIntent = new Intent(getActivity(), BankListScreen.class);
            startActivityForResult(bankIntent, 4);
        }

        if (v == create) {
            if (validationAddBeneficiary("") == 0) {
                SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String currentSenderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.AddBeneficiary(getActivity(), currentSenderNumber, beneficiaryName.getText().toString().trim(),
                            beneficiaryNumber.getText().toString().trim(), accountNumber.getText().toString().trim(),
                            ifscCode.getText().toString().trim() + ifsc.getText().toString().trim(), verified, bankId, mProgressDialog);

                } else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message), 2);
                }
            }
        }

        if (v == accVerify) {
            if (validationAddBeneficiary("accVerif") == 0) {
                SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
                String currentSenderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.VerifyBeneficiary(getActivity(), currentSenderNumber,
                            accountNumber.getText().toString().trim(), bankId, mProgressDialog);

                } else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message), 2);
                }
            }
        }

        if (v == thumbnail) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                if (number.getText() != null && number.getText().toString().trim().length() > 0) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.CheckBalanceDownline(getActivity(), number.getText().toString().trim(), null, "info", mProgressDialog);

                } else {
                    number.setError(getResources().getString(R.string.mobilenumber_error));
                    number.requestFocus();
                }

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v == importContactLayout) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant

                    return;
                }
                else
                {
                    Intent contactIntent = new Intent(getActivity(), ContactListScreen.class);
                    startActivityForResult(contactIntent, 2);
                }
            }
            else
            {
                Intent contactIntent = new Intent(getActivity(), ContactListScreen.class);
                startActivityForResult(contactIntent, 2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent contactIntent = new Intent(getActivity(), ContactListScreen.class);
                    startActivityForResult(contactIntent, 2);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void setCurrentDetail() {
        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.senderInfoPref, null);
        String currentSenderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

        Gson gson = new Gson();
        senderTableInfo = gson.fromJson(response, TABLE.class);

        currentMobile.setText("" + currentSenderNumber);
        currentName.setText("" + senderTableInfo.getNAME());
    }

    public int validationAddBeneficiary(String from) {
        int flag = 0;

        if (!from.equalsIgnoreCase("accVerif")) {

            if (accVerification.equalsIgnoreCase("Yes")) {
                if (verified != null && (verified.equalsIgnoreCase("1") || verified.equalsIgnoreCase("0"))) {

                } else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.attention_error_title),
                            "Please verify account first !!", 2);
                    flag++;
                }
            }
        }

        if (ifsc.getText() != null && ifsc.getText().toString().trim().length() > 0
                && !(ifsc.getText().toString().trim().length() < 7)) {
        } else {
            ifsc.setError(getResources().getString(R.string.bene_ifsc_error));
            ifsc.requestFocus();
            flag++;
        }

        if (accountNumber.getText() != null && accountNumber.getText().toString().trim().length() > 0
                && !(accountNumber.getText().toString().trim().length() < 15)) {
        } else {
            accountNumber.setError(getResources().getString(R.string.bene_acc_error));
            accountNumber.requestFocus();
            flag++;
        }

        if (bank.getText() != null && bank.getText().toString().trim().length() > 0) {
        } else {
            bank.setError(getResources().getString(R.string.bene_bank_error));
            bank.requestFocus();
            flag++;
        }

        if (beneficiaryName.getText() != null && beneficiaryName.getText().toString().trim().length() > 0) {
        } else {
            beneficiaryName.setError(getResources().getString(R.string.bene_name_error));
            beneficiaryName.requestFocus();
            flag++;
        }

        if (beneficiaryNumber.getText() != null && beneficiaryNumber.getText().toString().trim().length() > 0
        ) {
        } else {
            beneficiaryNumber.setError(getResources().getString(R.string.mobilenumber_error));
            beneficiaryNumber.requestFocus();
            flag++;
        }

        return flag;
    }

    public int validationForm(String type) {
        int flag = 0;

        if (type.equalsIgnoreCase("create")) {
            if (senderName.getText() != null && senderName.getText().toString().trim().length() > 0) {
            } else {
                senderName.setError(getResources().getString(R.string.name_error));
                senderName.requestFocus();
                flag++;
            }
        }

        if (senderLoginNumber.getText() != null && senderLoginNumber.getText().toString().trim().length() > 0)
        {
        } else {
            senderLoginNumber.setError(getResources().getString(R.string.mobilenumber_error));
            senderLoginNumber.requestFocus();
            flag++;
        }

        return flag;
    }

    public int validateFormFund(int subMenu) {
        int flag = 0;

        if (subMenu == 2) {
            if (bankFund.getText() != null && bankFund.getText().toString().trim().length() > 0) {
            } else {
                bankFund.setError(getResources().getString(R.string.bank_error));
                bankFund.requestFocus();
                flag++;
            }

            if (transactionId.getText() != null && transactionId.getText().toString().trim().length() > 0) {
            } else {
                transactionId.setError(getResources().getString(R.string.transactionId_error));
                transactionId.requestFocus();
                flag++;
            }

            if (remark.getText() != null && remark.getText().toString().trim().length() > 0) {
            } else {
                remark.setError(getResources().getString(R.string.remark_error));
                remark.requestFocus();
                flag++;
            }
            if (AmountInWords.getText() != null && AmountInWords.getText().toString().trim().length() > 0) {
            } else {
                AmountInWords.setError(getResources().getString(R.string.AmountInWords_error));
                AmountInWords.requestFocus();
                flag++;
            }

        }

        if (amount.getText() != null && amount.getText().toString().trim().length() > 0 && !amount.getText().toString().contains(".")) {
        } else {
            amount.setError(getResources().getString(R.string.amount_error));
            amount.requestFocus();
            flag++;
        }

        if (number.getText() != null && number.getText().toString().trim().length() > 0) {
        } else {
            number.setError(getResources().getString(R.string.mobilenumber_error));
            number.requestFocus();
            flag++;
        }

        return flag;
    }

    public int validateForm() {
        int flag = 0;

        if (amount.getText() != null && amount.getText().toString().trim().length() > 0) {
        } else {
            amount.setError(getResources().getString(R.string.amount_error));
            amount.requestFocus();
            flag++;
        }

        if (operator.getText() != null && operator.getText().toString().trim().length() > 0) {
        } else {
            operator.setError(getResources().getString(R.string.operator_error));
            operator.requestFocus();
            flag++;
        }

        if (flagSelectedService.equalsIgnoreCase("mobile")) {
            if (number.getText() != null && number.getText().toString().trim().length() > 0
            ) {
            } else {
                number.setError(getResources().getString(R.string.mobilenumber_error));
                number.requestFocus();
                flag++;
            }
        } else {
            if (number.getText() != null && number.getText().toString().trim().length() > 0) {
            } else {
                number.setError(getResources().getString(R.string.number_error));
                number.requestFocus();
                flag++;
            }
        }
        return flag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                operatorSelected = data.getExtras().getString("selected");
                operatorSelectedId = data.getExtras().getInt("selectedId");
               operator.setText(operatorSelected);
                paramValue1 = data.getExtras().getString("param1");
                paramValue2 = data.getExtras().getString("param2");
                paramValue3 = data.getExtras().getString("param3");
                paramValue4 = data.getExtras().getString("param4");
                ROffer = data.getExtras().getString("ROffer");

                part2 = "";
                part3 = "";
             //   opeImgMethod(operatorSelected);

            }
        }
        else if (requestCode == 2) {
            if (resultCode == 2) {
                contactSelected = data.getExtras().getString("selected");
                contactSelectedNumber = data.getExtras().getString("selectedNumber");

                if (contactSelectedNumber.contains("+91")) {
                    contactSelectedNumber = contactSelectedNumber.replace("+91", "");
                }
                if (contactSelectedNumber.contains(" ")) {
                    contactSelectedNumber = contactSelectedNumber.replace(" ", "");
                }
                if (contactSelectedNumber.contains("-")) {
                    contactSelectedNumber = contactSelectedNumber.replace("-", "");
                }
                if (contactSelectedNumber.contains("(")) {
                    contactSelectedNumber = contactSelectedNumber.replace("(", "");
                }
                if (contactSelectedNumber.contains(")")) {
                    contactSelectedNumber = contactSelectedNumber.replace(")", "");
                }

                number.setText("");
                number.setText(contactSelectedNumber);

                String s= number.getText().toString().substring(0,4);
                fetchSCode(s.toString());

                if(number.getText().toString().length() == 10)
                {
                    tvROffers.setVisibility(View.VISIBLE);
                    UtilMethods.INSTANCE.GetROffer(getActivity(),number.getText().toString(),part2,null,tvROffers);
                }
                else
                {
                    tvROffers.setVisibility(View.GONE);
                }
            }
        }
        else if (requestCode == 3) {
            if (resultCode == 3) {
                boolean flag = data.getExtras().getBoolean("flag");
                if (flag) {

                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                payButton.setEnabled(false);
                                payButton.setBackgroundColor(getResources().getColor(R.color.bottommenu));

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.afterLogintoPreviousWindow(getActivity(), number.getText().toString().trim().toString(),
                                        amount.getText().toString().trim(), "" + operatorSelectedId, stdCode.getText().toString().trim(),
                                        flagElectricity, paramValue1, paramValue2, paramValue3, paramValue4, mProgressDialog,payButton);



                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }

                } else {
                    UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.attention_error_title),
                            getResources().getString(R.string.pinpass_error), 2);
                }

            }
        }
        else if (resultCode == 4) {
            if (requestCode == 4) {
                bankId = data.getExtras().getString("bankId");
                bankName = data.getExtras().getString("bankName");
                accVerification = data.getExtras().getString("accVerification");
                shortCode = data.getExtras().getString("shortCode");

                bank.setText("" + bankName);
                if (shortCode != null && shortCode.length() > 0) {
                    ifscCode.setText("" + shortCode);
                    ifscCode.setVisibility(View.VISIBLE);
                } else {
                    ifscCode.setText("");
                    ifscCode.setVisibility(View.GONE);
                }

                if (accVerification.equalsIgnoreCase("Yes"))
                    accVerify.setVisibility(View.VISIBLE);
                else
                    accVerify.setVisibility(View.GONE);
            }
        }
        else if (requestCode == 4) {
            if (resultCode == 1) {
                operatorSelectedForPlan = data.getExtras().getString("selected");
                IReffOp = data.getExtras().getString("selectedId");

                Intent planOptionIntent = new Intent(getActivity(), SelectPlanOption.class);
                planOptionIntent.putExtra("from", "zone");
                planOptionIntent.putExtra("opList", 0);
                planOptionIntent.putExtra("operatorId", IReffOp);
                startActivityForResult(planOptionIntent, 4);
            }
            else if (resultCode == 2) {
                String operatorSelected = data.getExtras().getString("selected");
                IReffCircle = data.getExtras().getString("selectedId");

                rechargePlan(IReffOp, IReffCircle, operatorSelectedForPlan);
                operator.setText("" + operatorSelectedForPlan);
                opeImgMethod(operatorSelectedForPlan);
            }
            else if (resultCode == 3) {
                String operatorSelected = data.getExtras().getString("selected");
                IReffCircle = data.getExtras().getString("selectedId");
                String opListName = data.getExtras().getString("opListName");
                String opListNameCode = data.getExtras().getString("opListNameCode");

                operator.setText("" + opListName);
                opeImgMethod(opListName);
                rechargePlan(opListNameCode, IReffCircle, opListName);
            }
        }
        else if (requestCode==RESULT_LOAD_IMAGE){
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Bitmap bitmap;

                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                      resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    encoded = ConvertBitmapToString(resizedBitmap);
                    imageUtils =new ImageUtils();
                    Log.e("EncodedImage",encoded);


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Log.e("FileNotFoundException","");
                    e.printStackTrace();
                }

             Uri selectedImageUri = data.getData();
                 imagepath = getPath(getActivity(),selectedImageUri);
                selectedImageFile = new File(imagepath);

//                if (!imagepath.equalsIgnoreCase("")){
//                    Intent i = new Intent(getActivity(),ViewActivity.class);
//                    startActivity(i);
//                }
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.imag, null);

                final ImageView cancelButton =   view.findViewById(R.id.image);
                final TextView cancel =   view.findViewById(R.id.cancel);
                final TextView ok =   view.findViewById(R.id.ok);

                final Dialog dialog = new Dialog(getActivity());

                dialog.setTitle("Confirm ");
                dialog.setCancelable(false);
                dialog.setContentView(view);
                final File file =new File(imagepath);

                byte[] decodedBytes = Base64.decode(encoded.substring(encoded.indexOf(",")  + 1),
                        Base64.DEFAULT);
                Bitmap bmp=  BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            //    bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);

                cancelButton.setImageBitmap(resizedBitmap);
             //   cancelButton.setImageBitmap(bmp);
  ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filename=file.getName();
                        payment_slip.setText("" + filename );
//upload();

                        dialog.dismiss();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        payment_slip.setText("Select slip" );
                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        }
//        else {
//            imagePicker.handleActivityResult(resultCode, requestCode, data);
//        }
    }


//        private void uploadFile(Uri fileUri, String desc) {
//
//            //creating a file
//            File file = new File(getRealPathFromURI(fileUri));
//
//            //creating request body for file
//            RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);
//            RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
//
//            //The gson builder
//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();
//
//
//            //creating retrofit object
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Api.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build();
//
//            //creating our api
//            Api api = retrofit.create(Api.class);
//
//            //creating a call and calling the upload image method
//            Call<MyResponse> call = api.uploadImage(requestFile, descBody);
//
//            //finally performing the call
//            call.enqueue(new Callback<MyResponse>() {
//                @Override
//                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                    if (!response.body().error) {
//                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MyResponse> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//



private void SendImage( final String image) {



        boolean result = false;
        HttpClient hc = new DefaultHttpClient();
        String message;

        HttpPost p = new HttpPost(ApplicationConstant.INSTANCE.baseUrl);
        JSONObject object = new JSONObject();
        try {

//            object.put("updates", updates);
//            object.put("mobile", mobile);
//            object.put("last_name", lastname);
//            object.put("first_name", firstname);
//            object.put("email", email);

        } catch (Exception ex) {

        }

        try {
            message = object.toString();


            p.setEntity(new StringEntity(message, "UTF8"));
            p.setHeader("Content-type", "application/json");
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                if (resp.getStatusLine().getStatusCode() == 204)
                    result = true;
            }

            Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();

        }



}
    public static String ConvertBitmapToString(Bitmap bitmap){
        String encodedImage = "";
        String encodedImage2 = "";
        String imageUtil  = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        try {
            encodedImage2=  Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT) ;
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");

            Log.e("encodedImage2 ",encodedImage2.length()+"  UTF-8   "+encodedImage.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }


    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getFrom().equalsIgnoreCase("senderLogin")) {
            senderLogin(activityFragmentMessage.getMessage());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("createSender")) {
            String[] data = activityFragmentMessage.getMessage().split(",");
            otpVerification(data[0], data[1]);
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("verifySender")) {
            senderLogin(activityFragmentMessage.getMessage());
        }
        else if (activityFragmentMessage.getFrom().equalsIgnoreCase("fetchBillReffId")) {
            String[] data = activityFragmentMessage.getMessage().split(",");
             StdCode1=   data[0] ;
             amout1 = data[1];
            amount.setText(amout1);
            Log.e("reffId",activityFragmentMessage.getMessage().toString());
        }
        else if (activityFragmentMessage.getFrom().equalsIgnoreCase("LookUpApi")) {
            setoperator(activityFragmentMessage.getMessage());
            //  lookUpApi1=activityFragmentMessage.getMessage();
            Log.e("lookupAPI",activityFragmentMessage.getMessage().toString());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("beneAdded")) {
            beneficiaryName.setText("");
            beneficiaryNumber.setText("");
            bank.setText("");
            accountNumber.setText("");
            ifsc.setText("");
            ifscCode.setText("");
            ifscCode.setVisibility(View.GONE);
            bankId = "";
            bankName = "";
            senderLogin(activityFragmentMessage.getMessage());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("verifyBene")) {

            if (activityFragmentMessage.getMessage() != null && activityFragmentMessage.getMessage().length() > 0) {
                beneficiaryName.setText("" + activityFragmentMessage.getMessage());
                verified = "1";
            } else {
                beneficiaryName.setText("" + activityFragmentMessage.getMessage());
                verified = "0";
            }

        } else if (activityFragmentMessage.getMessage().equalsIgnoreCase("BillAmount")) {
            Log.e("billamt",activityFragmentMessage.getFrom().trim());
            amount.setText("" + activityFragmentMessage.getFrom().trim());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("planSelected")) {
            amount.setText("" + activityFragmentMessage.getMessage());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("rOffer_Amount")) {
            amount.setText("" + activityFragmentMessage.getMessage());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("dthCustomerPlan")) {
            amount.setText("");
            amount.setText("" + activityFragmentMessage.getMessage());
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("bankSelected")) {
            String[] detail = activityFragmentMessage.getMessage().split(",");
            bankFund.setText("" + detail[0]);
            number.setText("" + detail[1]);
            opImgLeft.setVisibility(View.GONE);
        } else if (activityFragmentMessage.getFrom().equalsIgnoreCase("refreshvalue")) {

            number.setText("");
            operator.setText("");
            amount.setText("");
            bank.setText("");
            transactionId.setText("");

            opImg.setVisibility(View.GONE);
            opImgLeft.setVisibility(View.VISIBLE);
            thumbnail.setVisibility(View.GONE);
            importContactLayout.setVisibility(View.GONE);
            flagOpFetched = false;

            flagElectricity = false;
            part1 = "";
            part2 = "";
            part3 = "";
            operatorSelected = "";
            operatorSelectedId = 0;
            if (fundSelected){
                operator.setVisibility(View.GONE);

            }
        }
        else if(activityFragmentMessage.getMessage().equals("amount"))
        {
            amount.setText(activityFragmentMessage.getFrom());
        }
    }
//  private void latlong (){
//try{ if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//    // TODO: Consider calling
//    //    ActivityCompat#requestPermissions
//    // here to request the missing permissions, and then overriding
//    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//    //                                          int[] grantResults)
//    // to handle the case where the user grants the permission. See the documentation
//    // for ActivityCompat#requestPermissions for more details.
//
//    ActivityCompat.requestPermissions(OutletRegistrationActivity.this,
//            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//            1);
//    locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//    String lat= String.valueOf(locationNet.getLatitude());
//    String longi= String.valueOf(locationNet.getLongitude());
//    latlong=lat+","+longi;
//    Log.e("loc",latlong);
//}
//else{
//    locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//    String lat= String.valueOf(locationNet.getLatitude());
//    String longi= String.valueOf(locationNet.getLongitude());
//    latlong=lat+","+longi;
//    Log.e("loc",latlong);
//}
//}catch (Exception e){e.printStackTrace();}
//
//         //   buildAlertMessageNoGps();
//
//
//
////
//// for disable GPS
////            Intent intent1 = new Intent("android.location.GPS_ENABLED_CHANGE");
////            intent.putExtra("enabled", false);
////            sendBroadcast(intent1);
//               }
    private void setoperator(String message) {
Log.e("lookupAPI",message.toString());

        Gson gson = new Gson();
        circleob = gson.fromJson(message, GetOperatorResponse.class);
        circle_selected = circleob.getCircle();
        op_by_service = circleob.getOperator();

            operator.setText(circleob.getOperator());
            opeImgMethod(circleob.getOperator());
           // opeIdMethod(circleob.getOperator());
           operatorSelectedId=circleob.getOPID();
    }

//    private void opeIdMethod(String operator) {
//
//        String imgTemp = operator.toLowerCase().replace(" ", "");
//        String imgTemp1 = imgTemp.toLowerCase().replace("-", "");
//        String imgTemp2 = imgTemp1.toLowerCase().replace("&", "");
//        String imgTemp3 = imgTemp2.toLowerCase().replace("_", "");
//        String imgTemp4 = imgTemp3.substring(0, imgTemp3.length());
//        ArrayList<Operator> operatorArray = new ArrayList<>();
//        OperatorList operatorList = new OperatorList();
//        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
//        String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);
//        Gson gson = new Gson();
//        operatorList = gson.fromJson(response, OperatorList.class);
//        operatorArray = operatorList.getPrepaidOperator();
//
//
//        for (Operator op : operatorArray) {
//            if (imgTemp4.equalsIgnoreCase(op.getOPNAME())) {
//                operatorSelected = op.getOPNAME();
//                operatorSelectedId = op.getOPID();
//                Log.e("opis", String.valueOf(operatorSelectedId));
//            }
//        }
////        int resourceId = getResources().getIdentifier(
////                imgTemp4 + "rec", "drawable", getActivity().getPackageName());
////        if (resourceId != 0) {
////            opImg.setVisibility(View.VISIBLE);
////            opImgLeft.setVisibility(View.GONE);
////
////            Glide.with(this).load(resourceId)
////                    .bitmapTransform(new RoundedCornersTransformation(getActivity(), 0, 0))
////                    .into(opImg);
////
////        } else {
////            opImg.setVisibility(View.GONE);
////            opImgLeft.setVisibility(View.VISIBLE);
////        }
////
//
//    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    public boolean IsSenderLogin() {

        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
        String isLogin = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

        if (isLogin != null && isLogin.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void senderLogin(String senderMobile) {
        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.senderInfoPref, null);

        Gson gson = new Gson();
        senderTableInfo = gson.fromJson(response, TABLE.class);

        headerNumber.setText("" + senderMobile);
        kycText.setText("" + senderTableInfo.getKYC());
        name.setText("" + senderTableInfo.getNAME());
        currency.setText("" + senderTableInfo.getCURRENCY());
        limitUsed.setText("" + getActivity().getResources().getString(R.string.rupiya) + senderTableInfo.getUSED());
        remaining.setText("" + getActivity().getResources().getString(R.string.rupiya) + senderTableInfo.getREMAINING());

        setFalse(rechargeType, number);
        radio11.setChecked(true);

        addBeneficiary.setVisibility(View.GONE);
        dmrDashboard.setVisibility(View.VISIBLE);

        loginContainer.setVisibility(View.GONE);
        dashboardDMR.setVisibility(View.VISIBLE);
        currentLogin.setVisibility(View.GONE);

    }

    public void otpVerification(final String senderNumber, final String senderName) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.otp_layout, null);

        final TextInputLayout otpTextLayout = (TextInputLayout) view.findViewById(R.id.otpTextLayout);
        final EditText otp = (EditText) view.findViewById(R.id.otp);
        final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
        final AppCompatButton cancelButton = (AppCompatButton) view.findViewById(R.id.cancelButton);

        final Dialog dialog = new Dialog(getActivity());

        dialog.setCancelable(false);
        dialog.setContentView(view);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText() != null && otp.getText().length() > 0) {
                    otpTextLayout.setErrorEnabled(false);

                    UtilMethods.INSTANCE.VerifySender(getActivity(), senderNumber, senderName, otp.getText().toString().trim());
                } else {
                    otp.setError("Please enter a valid OTP !!");
                    otp.requestFocus();
                }
            }
        });
        dialog.show();
    }

    public void fetchSCode(String s)
    {
        String param = UtilMethods.INSTANCE.fetchOperator(getActivity(), s);

        String[] parts = param.split(",");
        if (parts.length == 3) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
        } else {
            part1 = "";
            part2 = "";
            part3 = "";

            operatorSelected = "";
            operatorSelectedId = 0;
        }

        ////////////////////////////////////////////////////////
        ArrayList<Operator> operatorArray = new ArrayList<>();
        OperatorList operatorList = new OperatorList();
        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, getActivity().MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);
        Gson gson = new Gson();
        operatorList = gson.fromJson(response, OperatorList.class);
        operatorArray = operatorList.getPrepaidOperator();

        for (Operator op : operatorArray) {
            if (op.getOPNAME().equalsIgnoreCase(op.getOPNAME())) {
                operatorSelected = op.getOPNAME();
                operatorSelectedId = op.getOPID();
                ROffer= op.getRoffercode();
            }
        }
        ////////////////////////////////////////////////////////
      operator.setText("" + part1);
       opeImgMethod(part1);
    }


    public String getPath(Context context,Uri uri) {
        String res = null;
        String imagePath = "";
        String data = "";
try{

    String[] proj = {MediaStore.Images.Media.DATA};
    Cursor cursor = getActivity().managedQuery(uri, proj, null, null,  null); // Order-by clause (ascending by name)

    if (cursor != null) {
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            imagePath = cursor.getString(column_index);

        }
    }

}catch (NullPointerException e){
    Log.e("getContentResolver",e.getMessage());
}
       return imagePath ;
    }

    @Override
    public void onResume() {
        browsePlanButton.setVisibility(View.GONE);
        super.onResume();
    }
}
