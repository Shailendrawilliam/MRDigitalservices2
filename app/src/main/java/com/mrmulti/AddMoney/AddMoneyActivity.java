package com.mrmulti.AddMoney;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.BuildConfig;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.Payment;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.mrmulti.Api.Object.BalanceType;
import com.mrmulti.Api.Object.PG;
import com.mrmulti.Api.Object.PGModelForApp;
import com.mrmulti.Api.Object.RequestPTM;
import com.mrmulti.Api.Request.ChoosePaymentGatewayRequest;
import com.mrmulti.Api.Request.GatewayTransactionRequest;
import com.mrmulti.Api.Request.PayTMTransactionUpdateRequest;
import com.mrmulti.Api.Response.BalanceResponse;
import com.mrmulti.Api.Response.BasicResponse;
import com.mrmulti.Api.Response.PaymentChooseResponse;
import com.mrmulti.Api.Response.PaymentTransactionResponsedto.PaymentChooseResponse.PaymentTransactionResponse;
import com.mrmulti.Fragments.Adapter.AddMoneyTypeAdapter;
import com.mrmulti.Fragments.Adapter.GatewayTypeAdapter;
import com.mrmulti.Login.dto.LoginResponse;
import com.mrmulti.R;
import com.mrmulti.Util.ApiClient;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.CustomLoader;
import com.mrmulti.Util.EndPointInterface;
import com.mrmulti.Util.ListPopupWindowAdapter;
import com.mrmulti.Util.RazorpayTransactionUpdateRequest;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.dto.Operator;
import com.mrmulti.Util.dto.OperatorList;
import com.mrmulti.Util.dto.WalletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.VISIBLE;

//import com.mrmulti.Api.Object.WalletType;
//import com.mrmulti.Api.Response.NumberListResponse;
//import com.mrmulti.Api.Response.WalletTypeResponse;
//import com.mrmulti.BuildConfig;
//import com.mrmulti.Util.AlertDialogForConfirmation;
//import com.mrmulti.usefull.CustomLoader;


public class AddMoneyActivity extends AppCompatActivity  implements PaymentResultWithDataListener {
    private static final String TAG = AddMoneyActivity.class.getSimpleName();
    View walletView;
    TextView walletTv, walletAmountTv;
    ImageView arrowIv;
    EditText amountEt;
    RecyclerView recyclerView;
    CustomLoader loader;
    BalanceResponse balanceCheckResponse;
    RazorpayClient razorpay;
    // private boolean isBankWalletActive;
    private WalletResponse mWalletTypeResponse;
    HashMap<String, Integer> walletIdMap = new HashMap<>();
    ArrayList<BalanceType> mBalanceTypes = new ArrayList<>();
    private int selectedWalletId = 1;
    ArrayList<ArrayList<Operator>> operatorArray = new ArrayList<ArrayList<Operator>>();
    ArrayList<PG> pgList = new ArrayList<>();
    private Dialog gatewayDialog;
    private String selectedMethod;
    int selectedOPId = 0;
    LoginResponse mLoginDataResponse;
    boolean isActivityPause;
    private boolean isDialogShowBackground;
    private String dialogMsg;
    private boolean isSucessDialog;
    private PaymentCapture paymentCapture;
    ArrayList<Operator> operator = new ArrayList<>();
    OperatorList operatorList = new OperatorList();
    // String from, type;
    String paymentid;
    String SessionID;
    String UMobile;
    String userId;
    String umail;
    Checkout checkout;

    String PaymentStatus;
    String PaymentOrderId;
    String PaymentId;
    String Entity;
    String CreatedAt;
    String Amount;
    String Method;
    String Amount_Refund;
    String Refund_Status;
    String CardId;
    String Bank;
    String Wallet;
    String ContactNo;
    String payment_method;
    String r_key;
    String r_secret_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        isActivityPause = false;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String mLoginResponse = UtilMethods.INSTANCE.getRecentLogin(this);
        //   mLoginDataResponse = new Gson().fromJson(mLoginResponse, LoginResponse.class);

        //  razorpay = new RazorpayClient("rzp_live_0966TRE2PCqgbk", "fwaPUCwDKn8kqANP1Zf3sO4T");
        checkout = new Checkout();
        SharedPreferences myPrefs = this.getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, this.MODE_PRIVATE);

        SessionID = myPrefs.getString(ApplicationConstant.INSTANCE.SessionID, null);
        UMobile = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);
        userId = myPrefs.getString(ApplicationConstant.INSTANCE.UserID, null);
        umail = myPrefs.getString(ApplicationConstant.INSTANCE.UMail, null);
        // String UName = myPrefs.getString(ApplicationConstant.INSTANCE.UName, null);
        Checkout.preload(getApplicationContext());
        //
        // checkout.setKeyID("rzp_test_ijDuHl0VYH0QAW");


     /*   from = getIntent().getExtras().getString("from");
        type = getIntent().getExtras().getString("type");*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Add Money");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
        walletView = findViewById(R.id.walletView);
        walletTv = findViewById(R.id.walletTv);
        walletAmountTv = findViewById(R.id.walletAmountTv);
        arrowIv = findViewById(R.id.arrowIv);
        amountEt = findViewById(R.id.amountEt);
        amountEt.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(this, R.drawable.ic_rupee_indian),
                null, null, null);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentCapture=new PaymentCapture();
        walletView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoupWindow(v);
            }
        });
        //   showWalletListPopupWindow();
        //getOperator("20");

        Button pay= findViewById(R.id.pay);
        pay.setVisibility(View.GONE);

        getOperatorList();
    }

    private void showWalletListPopupWindow() {

        if (balanceCheckResponse != null) {
            mBalanceTypes.clear();
            if (balanceCheckResponse.getBalanceData().getIsBalance() && balanceCheckResponse.getBalanceData().getIsBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Prepaid Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getBalance() + "")));
            }
            if (balanceCheckResponse.getBalanceData().getIsUBalance() && balanceCheckResponse.getBalanceData().getIsUBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Utility Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getuBalance() + "")));

            }
            if (balanceCheckResponse.getBalanceData().getIsBBalance() && balanceCheckResponse.getBalanceData().getIsBBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Bank Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getbBalance() + "")));
                //  isBankWalletActive = true;
            }
            if (balanceCheckResponse.getBalanceData().getIsCBalance() && balanceCheckResponse.getBalanceData().getIsCBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Card Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getcBalance() + "")));
            }
            if (balanceCheckResponse.getBalanceData().getIsIDBalance() && balanceCheckResponse.getBalanceData().getIsIDBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Registration Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getIdBalnace() + "")));
            }
            if (balanceCheckResponse.getBalanceData().getIsAEPSBalance() && balanceCheckResponse.getBalanceData().getIsAEPSBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Aeps Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getAepsBalnace() + "")));
            }
            if (balanceCheckResponse.getBalanceData().getIsPacakgeBalance() && balanceCheckResponse.getBalanceData().getIsPacakgeBalanceFund()) {
                mBalanceTypes.add(new BalanceType("Package Wallet", UtilMethods.INSTANCE.formatedAmount(balanceCheckResponse.getBalanceData().getPackageBalnace() + "")));
            }
            if (mBalanceTypes != null && mBalanceTypes.size() > 0) {
                if (mBalanceTypes.size() == 1) {
                    arrowIv.setVisibility(View.GONE);
                    walletView.setClickable(false);
                } else {
                    arrowIv.setVisibility(VISIBLE);
                    walletView.setClickable(true);
                }
                //setWalletIds();
            }
        } else {
            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, "");
            balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceResponse.class);
            if (balanceCheckResponse != null) {
                showWalletListPopupWindow();
            }
            return;
        }

    }


    private void CapturOrder(){
        JSONObject captureRequest = new JSONObject();
        try {
            captureRequest.put("amount", "100");

            captureRequest.put("currency", "INR");

            Payment payment = razorpay.Payments.capture("pay_FkVA0Krvfz8PHa", captureRequest);
            Toast.makeText(this, "Payment Successful: " + "pay_FjYzvawOjhxO7C "+"   payment "+payment.toString(), Toast.LENGTH_SHORT).show();

        } catch (JSONException | RazorpayException e) {
            Toast.makeText(this, "Paymenterr: " + "pay_FjYzvawOjhxO7C "+"   payment "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }


    }
    private void fetchall(){
        try {


       /* try {
            Payment fetchedpaymente = razorpay.Payments.fetch(paymentid);
            Toast.makeText(this,"  payment status :   "+fetchedpaymente,Toast.LENGTH_LONG).show();
        } catch (RazorpayException e) {
            e.printStackTrace();
        }*/
            JSONObject paymentRequest = new JSONObject();

            //supported option filters (from, to, count, skip)
            paymentRequest.put("count", 20);
            paymentRequest.put("skip", 1);

            List<Payment> payments = razorpay.Payments.fetchAll(paymentRequest);
            Toast.makeText(this,""+payments,Toast.LENGTH_LONG).show();
            Log.e("ee",""+payments);
        } catch (RazorpayException | JSONException e ) {
            // Handle Exception
            System.out.println(e.getMessage());
            Toast.makeText(this," e.getMessage()  "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void showPoupWindow(View anchor) {
        if (mBalanceTypes != null && mBalanceTypes.size() > 0) {
            final ListPopupWindow listPopupWindow =
                    createListPopupWindow(anchor, ViewGroup.LayoutParams.WRAP_CONTENT, mBalanceTypes);
            listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect));
            listPopupWindow.show();
        } else {
            showWalletListPopupWindow();
        }
    }



    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")

    private ListPopupWindow createListPopupWindow(View anchor, int width, ArrayList<BalanceType> items) {
        final ListPopupWindow popup = new ListPopupWindow(this);

        ListAdapter adapter = new ListPopupWindowAdapter(items, this, false, R.layout.wallet_list_popup, new ListPopupWindowAdapter.ClickView() {
            @Override
            public void onClickView(String walletName, String amount) {
                walletTv.setText(walletName);
                walletAmountTv.setText(amount);
                selectedWalletId = walletIdMap.get(walletName);
                popup.dismiss();
            }
        });
        // popup.setWidth((int) getResources().getDimension(R.dimen._200sdp));
        popup.setAnchorView(anchor);
        popup.setAdapter(adapter);
        return popup;
    }


  /*  void setWalletIds() {

        if (mWalletTypeResponse != null && mWalletTypeResponse.getWalletTypes() != null && mWalletTypeResponse.getWalletTypes().size() > 0) {
            int count = 0;
            for (WalletType object : mWalletTypeResponse.getWalletTypes()) {
                if (object.getInFundProcess()) {
                    for (BalanceType objectWallet : mBalanceTypes) {
                        if (objectWallet.getName().contains(object.getName())) {
                            walletIdMap.put(objectWallet.getName(), object.getId());
                            if (count == 0) {
                                walletTv.setText(objectWallet.getName());
                                walletAmountTv.setText("\u20B9 " + objectWallet.getAmount());
                                selectedWalletId = object.getId();
                            }
                            count++;
                        }
                    }
                }
            }

        } else {
            mWalletTypeResponse = new Gson().fromJson(UtilMethods.INSTANCE.getWalletType(this), WalletTypeResponse.class);
            if (mWalletTypeResponse != null && mWalletTypeResponse.getWalletTypes() != null && mWalletTypeResponse.getWalletTypes().size() > 0) {
                setWalletIds();
            } else {
                UtilMethods.INSTANCE.WalletType(AddMoneyActivity.this, loader,new UtilMethods.ApiCallBack() {
                    @Override
                    public void onSucess(Object object) {
                        mWalletTypeResponse = (WalletTypeResponse) object;
                        if (mWalletTypeResponse != null && mWalletTypeResponse.getWalletTypes() != null && mWalletTypeResponse.getWalletTypes().size() > 0) {
                            setWalletIds();
                        }
                    }
                });
            }
        }
    }*/


    /*
        public void getOperator(String op_Type) {

            SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);

            Gson gson = new Gson();


            GetOperatorResponse operatorResponse = gson.fromJson(response, GetOperatorResponse.class);
            OperatorList operatorList = gson.fromJson(response, OperatorList.class);

           //here uncomment
            for (OperatorList op : operatorResponse.getPaymentOperator()) {
                if (op.getOpType().equals(op_Type)) {
                   operatorArray.add(op);
               }

           }

            AddMoneyTypeAdapter addMoneyTypeAdapter = new AddMoneyTypeAdapter(operatorArray, this);
            recyclerView.setAdapter(addMoneyTypeAdapter);
        }
    */
    public void getOperatorList() {



        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.operatorListPref, null);

        Gson gson = new Gson();
        operatorList = gson.fromJson(response, OperatorList.class);

        if (operatorList != null) {
            if (operatorList.getPaymentOperator() != null && operatorList.getPaymentOperator().size()>0){
                operator = operatorList.getPaymentOperator();
                operatorArray.add(operator);

                AddMoneyTypeAdapter addMoneyTypeAdapter = new AddMoneyTypeAdapter(operator, this);
                recyclerView.setAdapter(addMoneyTypeAdapter);

            }}


    }


    public void paymentTypeClick(Operator operator) {
        if (amountEt.getText().toString().isEmpty()) {
            amountEt.setError("Please Enter Amount");
            amountEt.requestFocus();
            return;
        }
        selectedMethod = operator.getOPNAME();
        selectedOPId = operator.getOPID();
        //here uncomment
        //selectedOPId =

        if (pgList != null && pgList.size() > 0) {
            if (pgList.size() == 1) {
                startGateway(pgList.get(0));
                //Toast.makeText(this, "GatewayTransaction", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(this, "222222", Toast.LENGTH_SHORT).show();
                showPopupGateWay();
            }
        } else {
            ChoosePaymentGateway();
            //Toast.makeText(this, "ChoosePaymentGateway", Toast.LENGTH_SHORT).show();
        }
    }


    public void ChoosePaymentGateway() {
        try {
            loader.show();
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);


            Call<PaymentChooseResponse> call = git.ChoosePaymentGateway(new ChoosePaymentGatewayRequest(
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    /*mLoginDataResponse.getData().get(0).getSessionID()*/SessionID,
                    /*mLoginDataResponse.getData().get(0).getUserID()*/userId,
                    /* mLoginDataResponse.getData().get(0).getUMobile()*/UMobile,
                    BuildConfig.VERSION_NAME
            ));
            /* mLoginDataResponse.getData().get(0).getUserID(),
             *//*mLoginDataResponse.getData().getLoginTypeID()*//*"1",
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    "", BuildConfig.VERSION_NAME,
                    UtilMethods.INSTANCE.getSerialNo(AddMoneyActivity.this),
                    mLoginDataResponse.getData().get(0).getSessionID(),
                    mLoginDataResponse.getData().get(0).getSessionID()*/

            call.enqueue(new Callback<PaymentChooseResponse>() {
                @Override
                public void onResponse(Call<PaymentChooseResponse> call, final retrofit2.Response<PaymentChooseResponse> response) {

                    try {
                        if (loader.isShowing())
                            loader.dismiss();
                        if (response.body() != null && response.body().getStatuscode().equals("1")) {

                            if (response.body().getPGs() != null && response.body().getPGs().size() > 0) {
                                pgList = response.body().getPGs();
                                if (response.body().getPGs().size() == 1) {
                                    startGateway(pgList.get(0));
                                } else {
                                    showPopupGateWay();
                                }

                            } else {
                                UtilMethods.INSTANCE.Processing(AddMoneyActivity.this, "Service is currently down.");
                            }

                        }
                    } catch (Exception e) {
                        if (loader.isShowing())
                            loader.dismiss();

                        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString(R.string.attention_error_title),e.getMessage(),2);

                     //   UtilMethods.INSTANCE.Error(AddMoneyActivity.this, e.getMessage() + "");
                    }

                }

                @Override
                public void onFailure(Call<PaymentChooseResponse> call, Throwable t) {
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {

                            if (t.getMessage().contains("No address associated with hostname")) {
                                Toast.makeText(AddMoneyActivity.this, "hehehehehe", Toast.LENGTH_SHORT).show();
                               // UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.err_msg_network));
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,
                                        getResources().getString(R.string.attention_error_title),getString(R.string.err_msg_network),2);


                            } else {
                                Toast.makeText(AddMoneyActivity.this, "ohoohoh", Toast.LENGTH_SHORT).show();

                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                        (R.string.attention_error_title),t.getMessage(),2);


                            }

                        } else {


                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                    (R.string.attention_error_title),getResources().getString
                                    (R.string.failed),2);

                        }
                    } catch (IllegalStateException ise) {
                        loader.dismiss();

                        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                (R.string.attention_error_title),ise.getMessage(),2);


                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (loader.isShowing())
                loader.dismiss();
            Toast.makeText(AddMoneyActivity.this, "uiofdshuhfjsdds", Toast.LENGTH_SHORT).show();


        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,  getResources().getString
                (R.string.attention_error_title),e.getMessage(),2);
        }
    }


    private void showPopupGateWay() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewMyLayout = inflater.inflate(R.layout.dialog_select_gateway, null);
        RecyclerView recyclerView = viewMyLayout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        View closeBtn = viewMyLayout.findViewById(R.id.closeBtn);

        GatewayTypeAdapter gatewayTypeAdapter = new GatewayTypeAdapter(pgList, AddMoneyActivity.this);
        recyclerView.setAdapter(gatewayTypeAdapter);
        gatewayDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog_Alert);
        gatewayDialog.setCancelable(false);
        gatewayDialog.setContentView(viewMyLayout);
        /* dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));*/
        gatewayDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatewayDialog.dismiss();
            }
        });

        gatewayDialog.show();
        // Window window = dialog.getWindow();
        //window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);


    }

    public void startGateway(PG paymentGatewayType) {

        if (gatewayDialog != null && gatewayDialog.isShowing()) {
            gatewayDialog.dismiss();
        }
        GatewayTransaction(paymentGatewayType);

    }

    void initPaytmSdk(final RequestPTM requestPTM) {
        PaytmPGService mService = /*BuildConfig.DEBUG ? PaytmPGService.getStagingService() :*/ PaytmPGService.getProductionService();
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", requestPTM.getMID() + "");
        paramMap.put("ORDER_ID", requestPTM.getORDERID() + "");
        paramMap.put("CUST_ID", requestPTM.getCUSTID() + "");
        paramMap.put("MOBILE_NO", requestPTM.getMOBILENO() + "");
        paramMap.put("EMAIL", requestPTM.getEMAIL() + "");
        paramMap.put("CHANNEL_ID", requestPTM.getCHANNELID() + "");
        paramMap.put("TXN_AMOUNT", requestPTM.getTXNAMOUNT() + "");
        paramMap.put("WEBSITE", requestPTM.getWEBSITE() + "");
        paramMap.put("INDUSTRY_TYPE_ID", requestPTM.getINDUSTRYTYPEID() + "");
        //BuildConfig.DEBUG ? "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + data.getNewOrderNo() : "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + data.getNewOrderNo()
        paramMap.put("CALLBACK_URL", requestPTM.getCALLBACKURL() + "");
        paramMap.put("CHECKSUMHASH", requestPTM.getCHECKSUMHASH() + "");
        PaytmOrder Order = new PaytmOrder(paramMap);
        mService.initialize(Order, null);

        mService.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*  Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                paytmCallBackApi(paytmFailedData(requestPTM, 0, "TXN_CANCEL", inErrorMessage));
                // Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onTransactionResponse(Bundle inResponse) {
                paytmCallBackApi(inResponse);
            }

            public void networkNotAvailable() {
                paytmCallBackApi(paytmFailedData(requestPTM, 0, "TXN_CANCEL", "Network not available"));
                // UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.err_msg_network));
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                paytmCallBackApi(paytmFailedData(requestPTM, 0, "TXN_CANCEL", inErrorMessage));
                // Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                paytmCallBackApi(paytmFailedData(requestPTM, iniErrorCode, "TXN_CANCEL", inErrorMessage));
                // Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onBackPressedCancelTransaction() {
                paytmCallBackApi(paytmFailedData(requestPTM, 0, "TXN_CANCEL", "Transaction canceled by user"));
                //Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                paytmCallBackApi(inResponse);
            }
        });
    }


    void paytmCallBackApi(Bundle inResponse) {
        JsonObject json = new JsonObject();
        if (inResponse != null) {
            Set<String> keys = inResponse.keySet();
            for (String key : keys) {
                json.addProperty(key, (String) inResponse.get(key));
            }
        }
        PayTMTransactionUpdate(json);
    }

    private Bundle paytmFailedData(RequestPTM requestPTM, int errorCode, String status, String errorMsg) {
        Bundle inResponse = new Bundle();
        inResponse.putString("STATUS", status);
        inResponse.putString("CHECKSUMHASH", requestPTM.getCHECKSUMHASH());
        inResponse.putString("BANKNAME", "");
        inResponse.putString("ORDERID", requestPTM.getORDERID());
        inResponse.putString("TXNAMOUNT", requestPTM.getTXNAMOUNT());
        inResponse.putString("MID", requestPTM.getMID());
        inResponse.putString("TXNID", "");
        inResponse.putString("RESPCODE", errorCode + "");
        inResponse.putString("PAYMENTMODE", "");
        inResponse.putString("BANKTXNID", "");
        inResponse.putString("CURRENCY", "INR");
        inResponse.putString("GATEWAYNAME", "");
        inResponse.putString("RESPMSG", errorMsg);

        return inResponse;
    }

    void initRazorPaySdk(PGModelForApp pGModelForApp) {
        r_key=pGModelForApp.getRPayRequest().getKey_id();
        r_secret_key=pGModelForApp.getRPayRequest().getKey_Secret();
        Checkout checkout = new Checkout();
        checkout.setKeyID(pGModelForApp.getRPayRequest().getKey_id());
        checkout.setImage(R.drawable.rnd_logo);

        try {
            JSONObject options = new JSONObject();
            options.put("name", pGModelForApp.getRPayRequest().getPrefill_name());
            options.put("theme.color", "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimary) & 0x00ffffff));
            options.put("prefill.contact", pGModelForApp.getRPayRequest().getPrefill_contact());
            options.put("prefill.email", pGModelForApp.getRPayRequest().getPrefill_email());

            if (selectedMethod != null) {
                if (selectedMethod.toLowerCase().contains("card")) {
                    options.put("prefill.method", "card");
                }
                if (selectedMethod.toLowerCase().contains("net banking") || selectedMethod.toLowerCase().contains("netbanking")) {
                    options.put("prefill.method", "netbanking");
                }
                if (selectedMethod.toLowerCase().contains("upi")) {
                    options.put("prefill.method", "upi");
                }
                if (selectedMethod.toLowerCase().contains("wallet")) {
                    options.put("prefill.method", "wallet");
                }
                if (selectedMethod.toLowerCase().contains("emi")) {
                    options.put("prefill.method", "emi");
                }
            }
            options.put("description", pGModelForApp.getRPayRequest().getDescription());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", pGModelForApp.getRPayRequest().getOrder_id());
            options.put("currency", "INR");

            options.put("amount",(  pGModelForApp.getRPayRequest().getAmount())*100);

            checkout.open(AddMoneyActivity.this, options);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error in starting Razorpay Checkout", e);
        } //key_id":"rzp_live_0ImY1iPcwt5Mgd","key_Secret":"
        // uzyvZPc4wdTU1Wv5RYzxdqHw","order_id":"order_GBpVuK9Q9Rl1A5"
    }

  @Override
  public void onPaymentSuccess(String s, PaymentData data) {
        /*amountEt.setText("");

        UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Sucessfully Added");*/
         /*UtilMethods.INSTANCE.BalanceCheck(AddMoneyActivity.this, null, new UtilMethods.ApiCallBack() {
            @Override
            public void onSucess(Object object) {
                balanceCheckResponse = (BalanceResponse) object;
                if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                    showWalletListPopupWindow();
                }
            }
        }  );*/

      try {
          //    Toast.makeText(this, "Payment Successful: " + data.getPaymentId(), Toast.LENGTH_SHORT).show();
          final String paymentId = data.getPaymentId();
          String signature = data.getSignature();
          String orderId = data.getOrderId();
          String contact = data.getUserContact();
          String email = data.getUserEmail();


          // SuccessPopup();

          /*Thread thread = new Thread(new Runnable() {

              @Override
              public void run() {*/
                  try {

                      //Get all details
                      fetchPayment(paymentId,orderId);
                      //Your code goes here
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
            //  }
         // });

         // thread.start();

      } catch (Exception e) {
          android.util.Log.e("com.merchant", e.getMessage(), e);
      }
  }


    public void finishMethod() {
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(getApplicationContext(),""+s ,Toast.LENGTH_LONG).show();
    }

    public void fetchPayment(String payment_id , String orderId) throws RazorpayException {
//fwaPUCwDKn8kqANP1Zf3sO4T
        RazorpayClient razorpay = new RazorpayClient(/*"rzp_live_0966TRE2PCqgbk"*/r_key, r_secret_key /*"fwaPUCwDKn8kqANP1Zf3sO4T"*//*"fwaPUCwDKn8kqANP1Zf3sO4T"*/);

        try {
            Payment payment = razorpay.Payments.fetch(payment_id);
            android.util.Log.e("payyyy", "" + payment);

            try {
                JSONObject object = new JSONObject("" + payment);
                PaymentStatus = object.getString("status");
                 PaymentOrderId = object.getString("order_id");
                PaymentId = object.getString("id");
                Entity = object.getString("entity");
                CreatedAt = getDate(Long.parseLong(object.getString("created_at")));
                Amount = object.getString("amount");
                Method = object.getString("method");
                Amount_Refund = object.getString("amount_refunded");
                Refund_Status = object.getString("refund_status");
                CardId = object.getString("card_id");
                Bank = object.getString("bank");
                Wallet = object.getString("wallet");
                ContactNo = object.getString("contact");
                payment_method = object.getString("method");

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        if (UtilMethods.INSTANCE.isNetworkAvialable(AddMoneyActivity.this)) {


                            loader.show();
                            loader.setCancelable(false);
                            loader.setCanceledOnTouchOutside(true);
                           /* HitPayment payAPI = new HitPayment();
                            payAPI.execute();*/
                            UtilMethods.INSTANCE.RazorpayUpdate(AddMoneyActivity.this, /*new RazorpayresponseUpdate(*/PaymentStatus, PaymentOrderId  ,
                                    PaymentId ,
                                    Entity ,
                                    CreatedAt ,
                                    Amount ,
                                    Method,
                                    Amount_Refund,
                                    Refund_Status,
                                    CardId,
                                    Bank ,
                                    Wallet ,
                                    ContactNo/*)*/, loader);
                        } else {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getString(R.string.network_error_title),
                                    getString(R.string.network_error_message), 2);
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }
    }


    public void RazorPayTransactionUpdate(JsonObject response) {
        try {
            if (!isActivityPause) {
                loader.show();
            }
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<BasicResponse> call = git.RazorPaySuccess(new RazorpayTransactionUpdateRequest(response,
                    /*mLoginDataResponse.getData().getUserID()*/userId,
                    /*mLoginDataResponse.getData().getLoginTypeID()*/"1",
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    "", BuildConfig.VERSION_NAME,
                    UtilMethods.INSTANCE.getSerialNo(AddMoneyActivity.this),
                    /*mLoginDataResponse.getData().getSessionID()*/SessionID,
                    /*  mLoginDataResponse.getData().getSession()*/SessionID,
                    UMobile
            ));
            Log.e("tage","req razorpayupdate"+new RazorpayTransactionUpdateRequest(response,
                    /*mLoginDataResponse.getData().getUserID()*/userId,
                    /*mLoginDataResponse.getData().getLoginTypeID()*/"1",
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    "", BuildConfig.VERSION_NAME,
                    UtilMethods.INSTANCE.getSerialNo(AddMoneyActivity.this),
                    /*mLoginDataResponse.getData().getSessionID()*/SessionID,
                    /*  mLoginDataResponse.getData().getSession()*/SessionID,
                    UMobile
            ).toString());
            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, final retrofit2.Response<BasicResponse> response) {
                    Log.e("onResponse","req razorpayupdate");
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (response.body() != null) {
                            if (response.body().getStatuscode() == 1) {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(getApplicationContext(), null);
                              //  Toast.makeText(AddMoneyActivity.this, "FUND TRANSFER SUCCESSFULL", Toast.LENGTH_LONG).show();
                             /*   UtilMethods.INSTANCE.BalanceCheck(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });*/

                                if (!isActivityPause) {
                                    //  AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //  forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }
                            else if(response.body().getStatuscode() == 2)
                            {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(getApplicationContext(), null);
                             /*   UtilMethods.INSTANCE.BalancecheckNew(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });*/

                                if (!isActivityPause) {
                                    // AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }




                            else {

                                if (!isActivityPause) {

                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),response.body().getMsg() + "",2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = response.body().getMsg() + "";
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                        (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }

                    } catch (Exception e) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {

                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                    (R.string.attention_error_title),e.getMessage() + "",2);
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = e.getMessage() + "";
                            isSucessDialog = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    Log.e("onFailure","req razorpayupdate");
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {

                            if (t.getMessage().contains("No address associated with hostname")) {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),getString(R.string.err_msg_network),2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = getString(R.string.err_msg_network);
                                    isSucessDialog = false;
                                }
                            } else {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),t.getMessage(),2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = t.getMessage();
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {

                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                        (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }
                    } catch (IllegalStateException ise) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                    (R.string.attention_error_title), getString(R.string.some_thing_error),2);
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = getString(R.string.some_thing_error);
                            isSucessDialog = false;
                        }
                    }
                }
            });

        } catch (Exception e) {
            Log.e("error" ,"updaterazor"+e.getMessage());
            e.printStackTrace();
            if (loader.isShowing()) {
                loader.dismiss();
            }

            if (!isActivityPause) {
                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                        (R.string.attention_error_title),e.getMessage() + "",2);
            } else {
                isDialogShowBackground = true;
                dialogMsg = e.getMessage() + "";
                isSucessDialog = false;
            }
        }
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }
  public void GatewayTransaction(final PG paymentGatewayType) {

        try {
            loader.show();
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);

            Call<PaymentTransactionResponse> call = git.GatewayTransaction(new GatewayTransactionRequest(
                            ApplicationConstant.INSTANCE.APP_ID,
                            UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                            SessionID,
                            userId,
                            UMobile,
                            amountEt.getText().toString(),
                            paymentGatewayType.getID() + "",
                            selectedOPId,
                            BuildConfig.VERSION_NAME
                    )
            );

            call.enqueue(new Callback<PaymentTransactionResponse>() {
                @Override
                public void onResponse(Call<PaymentTransactionResponse> call, final retrofit2.Response<PaymentTransactionResponse> response) {

                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (response.body() != null) {
                            if (response.body().getStatuscode().equals("1")) {
                                if (response.body().getPGModelForApp() != null) {
                                    if (response.body().getPGModelForApp().getStatuscode().equals("1")) {

                                        if (response.body().getPGModelForApp().getPGID().equals("1") || paymentGatewayType.getPGType() == 1) {
                                            if (response.body().getPGModelForApp().getRequestPTM() != null) {
                                                initPaytmSdk(response.body().getPGModelForApp().getRequestPTM());
                                            } else {
                                                UtilMethods.INSTANCE.Processing(AddMoneyActivity.this, response.body().getPGModelForApp().getMsg() + "");
                                            }
                                        }
                                        if (response.body().getPGModelForApp().getPGID().equals("2") || paymentGatewayType.getPGType() == 2) {
                                            if (response.body().getPGModelForApp().getRPayRequest() != null) {
                                                initRazorPaySdk(response.body().getPGModelForApp());
                                            } else {
                                                UtilMethods.INSTANCE.Processing(AddMoneyActivity.this, response.body().getPGModelForApp().getMsg() + "");
                                            }
                                        }
                                    } else {
                                        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                                (R.string.attention_error_title),
                                                response.body().getPGModelForApp().getMsg() + "",2);
                                    }
                                } else {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),response.body().getMsg() + " " +
                                            getString(R.string.some_thing_error),2);
                                }
                            } else {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                        (R.string.attention_error_title), response.body().getMsg() + "",2);
                            }

                        } else {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                    (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                        }

                    } catch (Exception e) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                (R.string.attention_error_title),e.getMessage() + "",2);
                    }
                }

                @Override
                public void onFailure(Call<PaymentTransactionResponse> call, Throwable t) {
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {

                            if (t.getMessage().contains("No address associated with hostname")) {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                        (R.string.attention_error_title), getString(R.string.err_msg_network),2);
                            } else {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                        (R.string.attention_error_title),t.getMessage(),2);
                            }

                        } else {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                    (R.string.attention_error_title), getString(R.string.some_thing_error),2);
                        }
                    } catch (IllegalStateException ise) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                                (R.string.attention_error_title), getString(R.string.some_thing_error),2);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (loader.isShowing()) {
                loader.dismiss();
            }
            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                    (R.string.attention_error_title),e.getMessage() + "",2);
        }
    }


    public void PayTMTransactionUpdate(JsonObject response) {
        try {
            if (!isActivityPause) {
                loader.show();
            }
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<BasicResponse> call = git.PayTMTransactionUpdate(new PayTMTransactionUpdateRequest(response,
                    /*mLoginDataResponse.getData().getUserID()*/userId,
                    /*mLoginDataResponse.getData().getLoginTypeID()*/"1",
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    "", BuildConfig.VERSION_NAME,
                    UtilMethods.INSTANCE.getSerialNo(AddMoneyActivity.this),
                    /*mLoginDataResponse.getData().getSessionID()*/SessionID,
                    /*  mLoginDataResponse.getData().getSession()*/SessionID,
                    UMobile
            ));

            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, final retrofit2.Response<BasicResponse> response) {

                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (response.body() != null) {
                            if (response.body().getStatuscode() == 1) {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(getApplicationContext(), null);
                                Toast.makeText(AddMoneyActivity.this, "FUND TRANSFER SUCCESSFULL", Toast.LENGTH_LONG).show();
                             /*   UtilMethods.INSTANCE.BalanceCheck(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });*/

                                if (!isActivityPause) {
                                    //  AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //  forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }
                            else if(response.body().getStatuscode() == 2)
                            {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(getApplicationContext(), null);
                             /*   UtilMethods.INSTANCE.BalancecheckNew(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });*/

                                if (!isActivityPause) {
                                    // AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }




                            else {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),response.body().getMsg() + "",2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = response.body().getMsg() + "";
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                        (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }

                    } catch (Exception e) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                    (R.string.attention_error_title),e.getMessage() + "",2);
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = e.getMessage() + "";
                            isSucessDialog = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {

                            if (t.getMessage().contains("No address associated with hostname")) {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),getString(R.string.err_msg_network),2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = getString(R.string.err_msg_network);
                                    isSucessDialog = false;
                                }
                            } else {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                            (R.string.attention_error_title),t.getMessage(),2);
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = t.getMessage();
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {
                                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                        (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }
                    } catch (IllegalStateException ise) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {
                            UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                                    (R.string.attention_error_title),getString(R.string.some_thing_error),2);
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = getString(R.string.some_thing_error);
                            isSucessDialog = false;
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (loader.isShowing()) {
                loader.dismiss();
            }

            if (!isActivityPause) {
                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this,getResources().getString
                        (R.string.attention_error_title), e.getMessage() + "",2);
            } else {
                isDialogShowBackground = true;
                dialogMsg = e.getMessage() + "";
                isSucessDialog = false;
            }
        }
    }



   /* public void PayTMTransactionUpdate(JsonObject response) {
        try {
            if (!isActivityPause) {
                loader.show();
            }
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<BasicResponse> call = git.PayTMTransactionUpdate(new PayTMTransactionUpdateRequest(response,
                   "26",
                    *//*mLoginDataResponse.getData().get(0).getLoginTypeID()*//*"",
                    ApplicationConstant.INSTANCE.APP_ID,
                    UtilMethods.INSTANCE.getIMEI(AddMoneyActivity.this),
                    "",
                    BuildConfig.VERSION_NAME,
                    UtilMethods.INSTANCE.getSerialNo(AddMoneyActivity.this),
                    *//*mLoginDataResponse.getData().getSessionID()*//*SessionID,
     *//*mLoginDataResponse.getData().getSession()*//*SessionID));

            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, final retrofit2.Response<BasicResponse> response) {

                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (response.body() != null) {
                            if (response.body().getStatuscode() == 1) {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });

                                if (!isActivityPause) {
                                    //  AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //  forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }
                            else if(response.body().getStatuscode() == 2)
                            {
                                amountEt.setText("");
                                UtilMethods.INSTANCE.BalanceCheck(AddMoneyActivity.this, loader,null,null, new UtilMethods.ApiCallBack() {
                                    @Override
                                    public void onSucess(Object object) {
                                        balanceCheckResponse = (BalanceResponse) object;
                                        if (balanceCheckResponse != null && balanceCheckResponse.getBalanceData() != null) {
                                            showWalletListPopupWindow();
                                        }

                                    }
                                });

                                if (!isActivityPause) {
                                    // AlertDialogForConfirmation forConfirmation = new AlertDialogForConfirmation(AddMoneyActivity.this,true);

                                    //forConfirmation.showMessage("Success", response.body().getMsg() +"",R.drawable.ic_check_circle_black_24dp,0);

                                    UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, "Money Added Sucessfully");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = "Money Added Sucessfully";
                                    isSucessDialog = true;
                                }
                            }




                            else {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.Error(AddMoneyActivity.this, response.body().getMsg() + "");
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = response.body().getMsg() + "";
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {
                                UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.some_thing_error));
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }

                    } catch (Exception e) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {
                            UtilMethods.INSTANCE.Error(AddMoneyActivity.this, e.getMessage() + "");
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = e.getMessage() + "";
                            isSucessDialog = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    try {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }
                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {

                            if (t.getMessage().contains("No address associated with hostname")) {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.err_msg_network));
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = getString(R.string.err_msg_network);
                                    isSucessDialog = false;
                                }
                            } else {

                                if (!isActivityPause) {
                                    UtilMethods.INSTANCE.Error(AddMoneyActivity.this, t.getMessage());
                                } else {
                                    isDialogShowBackground = true;
                                    dialogMsg = t.getMessage();
                                    isSucessDialog = false;
                                }
                            }

                        } else {

                            if (!isActivityPause) {
                                UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.some_thing_error));
                            } else {
                                isDialogShowBackground = true;
                                dialogMsg = getString(R.string.some_thing_error);
                                isSucessDialog = false;
                            }
                        }
                    } catch (IllegalStateException ise) {
                        if (loader.isShowing()) {
                            loader.dismiss();
                        }

                        if (!isActivityPause) {
                            UtilMethods.INSTANCE.Error(AddMoneyActivity.this, getString(R.string.some_thing_error));
                        } else {
                            isDialogShowBackground = true;
                            dialogMsg = getString(R.string.some_thing_error);
                            isSucessDialog = false;
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (loader.isShowing()) {
                loader.dismiss();
            }

            if (!isActivityPause) {
                UtilMethods.INSTANCE.Error(AddMoneyActivity.this, e.getMessage() + "");
            } else {
                isDialogShowBackground = true;
                dialogMsg = e.getMessage() + "";
                isSucessDialog = false;
            }
        }
    }*/


    @Override
    protected void onResume() {
        isActivityPause = false;
        super.onResume();


        if (isDialogShowBackground) {
            isDialogShowBackground = false;

            if (isSucessDialog) {
                UtilMethods.INSTANCE.Successful(AddMoneyActivity.this, dialogMsg);
            } else {
                UtilMethods.INSTANCE.dialogOk(AddMoneyActivity.this, getResources().getString
                        (R.string.attention_error_title),dialogMsg,2);
            }
        }
    }

    @Override
    protected void onPause() {
        isActivityPause = true;
        super.onPause();
    }

    @Override
    protected void onStart() {
        isActivityPause = false;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isActivityPause = true;
        super.onStop();
    }
}
