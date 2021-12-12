package com.mrmulti.Util.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.mrmulti.Activities.RechargeActivity;
import com.mrmulti.Login.dto.GetOperatorResponse;
import com.mrmulti.R;
import com.mrmulti.Util.ActivityActivityMessage;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.dto.Operator;
import com.mrmulti.Util.dto.OperatorList;

import java.util.ArrayList;

/**
 * Created by Lalit on 18-01-2017.
 */

public class ListScreen extends AppCompatActivity {

    RecyclerView recycler_view;
    TextView noData,use_mnp;
    ListScreenAdapter mAdapter;
    ArrayList<Operator> operator = new ArrayList<>();
    OperatorList operatorList = new OperatorList();
    Toolbar toolbar; Integer operatorSelectedId; int resourceId = 0;
  String operatorSelected, OpCircleName,RofferCode,operatorSelectedIcon;
    String type, from; EditText et_number;
TextView title;
    ImageView opImage; private ProgressDialog mProgressDialog=null;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        from = getIntent().getExtras().getString("from");
        type = getIntent().getExtras().getString("type");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Operator's");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        use_mnp = findViewById(R.id.use_mnp);
        noData = (TextView) findViewById(R.id.noData);

        et_number =   findViewById(R.id.et_number);
        title =   findViewById(R.id.title);
        opImage =   findViewById(R.id.opImage);
        mProgressDialog= new ProgressDialog(this);
        getOperatorList();

        sendFrom(from);
        if (type.equalsIgnoreCase("prepaid")){
            use_mnp.setVisibility(View.GONE);
        }else {
            use_mnp.setVisibility(View.GONE);
        }
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                if (type.equals("prepaid")){
                    if(et_number.getText().toString().length() == 10  ) {

                        mProgressDialog.setTitle("Loading..");
                        mProgressDialog.show();
                        UtilMethods.INSTANCE.LookUpApi(ListScreen.this, et_number.getText().toString(),
                                mProgressDialog, new UtilMethods.ApiCallBack() {
                            @Override
                            public void onSucess(Object object) {


                                GetOperatorResponse getOperatorResponse=(GetOperatorResponse) object;
                                if(getOperatorResponse!=null){
                                    operatorSelected =   getOperatorResponse.getOperator();
                                    title.setText(operatorSelected);
                                    operatorSelectedId =   getOperatorResponse.getOPID();
                                    OpCircleName = getOperatorResponse.getCircle();



                                }
                                ArrayList<Operator> operatorArray = new ArrayList<>();
                                OperatorList operatorList = new OperatorList();
                                SharedPreferences prefs =  getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,  MODE_PRIVATE);
                                String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);
                                Gson gson = new Gson();
                                operatorList = gson.fromJson(response, OperatorList.class);
                                operatorArray = operatorList.getPrepaidOperator();

                                for (Operator op : operatorArray) {
                                    if (operatorSelected.equalsIgnoreCase(op.getOPNAME())) {
                                        operatorSelected = op.getOPNAME();
                                        operatorSelectedId = op.getOPID();
                                        RofferCode= op.getRoffercode();
                                        operatorSelectedIcon= op.getColumn1();
                                    }
                                }
                                ////////////////////////////////////////////////////////

                                if (operatorSelectedIcon!=null&& operatorSelectedIcon.toString().length() > 0 ){
                                    String imgName = operatorSelectedIcon;
                                    String imgTemp = imgName.toLowerCase().replace(" ", "");
                                    String imgTemp1 = imgTemp.toLowerCase().replace("-", "");
                                    String imgTemp2 = imgTemp1.toLowerCase().replace("&", "");
                                    String imgTemp3 = imgTemp2.toLowerCase().replace("_", "");
                                    String imgTemp4 = imgTemp3.substring(0, imgTemp3.length());
                                    // Log.e("imagepathis",ApplicationConstant.INSTANCE.baseUrl+operator.getColumn1().toString());


                                    Glide.with(ListScreen.this).asBitmap().load(ApplicationConstant.INSTANCE.baseUrl+
                                            operatorSelectedIcon)./*centerCrop().error(R.drawable.ic_operator_default_new).*/into(new BitmapImageViewTarget(opImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create( getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            opImage.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }
                                else   if (operatorSelected != null && operatorSelected.toString().length() > 0) {

                                    String imgName = operatorSelected;
                                    String imgTemp = imgName.toLowerCase().replace(" ", "");
                                    String imgTemp1 = imgTemp.toLowerCase().replace("-", "");
                                    String imgTemp2 = imgTemp1.toLowerCase().replace("&", "");
                                    String imgTemp3 = imgTemp2.toLowerCase().replace("_", "");
                                    String imgTemp4 = imgTemp3.substring(0, imgTemp3.length());

                                    //for eg operator name Airtel Digital tv
                                    //above code convert it to airteldigitaltv
                                    resourceId = getResources().getIdentifier(
                                            imgTemp4, "drawable", getPackageName());

                                    // Log.e("here", "name : " + imgTemp4 + " , and : " + resourceId);

                                    Glide.with(ListScreen.this).asBitmap().load(resourceId)./*centerCrop().error(R.drawable.ic_operator_default_new).*/
                                            into(new BitmapImageViewTarget(opImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            opImage.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                } else {
                                    opImage.setImageResource(R.drawable.ic_operator_default_new);
                                }

                                /* intent.putExtra("selected", operator.getOPNAME());
                                 intent.putExtra("Roffer", operator.getPLANCODE());
                                 intent.putExtra("selectedicon", operator.getOPIMG());
                                 intent.putExtra("selectedId", operator.getOPID());
                                 intent.putExtra("selectedOpcode", operator.getRoffercode());
                                 intent.putExtra("Roffercode", operator.getRoffercode());*/

                            }
                        });
                    }



                }}
        });
        use_mnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // card_mnp.setVisibility(View.VISIBLE);
               // recycler_view.setVisibility(View.GONE);


                Intent transactionIntent = new Intent( ListScreen.this, RechargeActivity.class);
                transactionIntent.putExtra("type", "prepaid");
                transactionIntent.putExtra("hlr", "yes");
                transactionIntent.putExtra("from", "mobile");
                startActivity(transactionIntent);
//                intent.putExtra("selected", operator.getOPNAME());
//                intent.putExtra("Roffer", operator.getPLANCODE());
//                intent.putExtra("selectedicon", operator.getOPIMG());
//                intent.putExtra("selectedId", operator.getOPID());
//                intent.putExtra("selectedOpcode", operator.getRoffercode());
//                intent.putExtra("displayvalue1", operator.getDisplayalue1());
//                intent.putExtra("displayvalue2", operator.getDisplayalue2());
//                intent.putExtra("displayvalue3", operator.getDisplayalue3());
//                intent.putExtra("displayvalue4", operator.getDisplayalue4());
//                intent.putExtra("Roffercode", operator.getRoffercode());
                //callapi();

                // intent.putExtra("flaglandline", operator.getDisplayvalue4());
finish();
            }
        });
        if (operator != null && operator.size() > 0) {
            noData.setVisibility(View.GONE);
            mAdapter = new ListScreenAdapter(operator, ListScreen.this,from);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);

        } else {
            noData.setVisibility(View.VISIBLE);
        }


    }
    private void sendFrom(String from) {
        ActivityActivityMessage activityActivityMessage =
                new ActivityActivityMessage( "recharge");
        GlobalBus.getBus().post(activityActivityMessage);
        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,   MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.fromPref, type);
        editor.commit();
        Log.e("sendFrom","  ActivityActivityMessage recharge  "+from);
    }
    public void getOperatorList() {

        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);

        Gson gson = new Gson();
        operatorList = gson.fromJson(response, OperatorList.class);
         if (from.equalsIgnoreCase("mobile")) {
            if (type.equalsIgnoreCase("postpaid")) {
                operator = operatorList.getPostpaidOperator();
            } else {
                operator = operatorList.getPrepaidOperator();
            }
        } else if (from.equalsIgnoreCase("dth")) {
            operator = operatorList.getDthOperator();
        } else if (from.equalsIgnoreCase("dthmob")) {
            operator = operatorList.getDthOperator();
        } else if (from.equalsIgnoreCase("water")) {
            operator = operatorList.getWaterOperator();
        }
        else if (from.equalsIgnoreCase("insurance")) {
            operator = operatorList.getInsuranceOperator();
        }
        else if (from.equalsIgnoreCase("broadband")) {
            operator = operatorList.getBroadBandOperator();
        }else if (from.equalsIgnoreCase("landline")) {
            operator = operatorList.getLandlineOperator();
        } else if (from.equalsIgnoreCase("electricity")) {
            operator = operatorList.getElectricityOperator();
        } else if (from.equalsIgnoreCase("gas")) {
            operator = operatorList.getGasOperator();
        } else {

        }

    }

    public void ItemClick(String name, int id, String planCode, String Column1, String param1, String param2, String param3, String param4,String ROffer) {
        Intent clickIntent = new Intent();
        clickIntent.putExtra("selected", name);
        clickIntent.putExtra("selectedId", id);
        clickIntent.putExtra("Column1", Column1);
        clickIntent.putExtra("param1", param1);
        clickIntent.putExtra("param2", param2);
        clickIntent.putExtra("param3", param3);
        clickIntent.putExtra("param4", param4);
        clickIntent.putExtra("ROffer", ROffer);
        if (from.equalsIgnoreCase("DTHFragment"))
            clickIntent.putExtra("planCode", planCode);
        setResult(1, clickIntent);
        finish();
    }

}
