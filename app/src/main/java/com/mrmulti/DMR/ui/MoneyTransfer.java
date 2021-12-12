package com.mrmulti.DMR.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrmulti.R;
import com.mrmulti.Util.ActivityActivityMessage;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.dto.BankListObject;
import com.mrmulti.Util.dto.BankListResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Lalit on 12-04-2017.
 */

public class MoneyTransfer extends AppCompatActivity implements View.OnClickListener {

    EditText transferAmount;
    TextView submitButton, closeButton;
    Toolbar toolbar;
    String name, bank, bankAccount, recipientId, channel, amount, senderNumber;

    RelativeLayout neftContainer, impsContainer;
    RadioButton neftRadio, impsRadio;

    private ProgressDialog mProgressDialog;
    ArrayList<BankListObject> operator = new ArrayList<>();
    BankListResponse operatorList = new BankListResponse();
    boolean NEFTFlag = false;
    boolean IMPSFlag = false;
    int selectedFlag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_transfer);

        name = getIntent().getExtras().getString("name");
        bank = getIntent().getExtras().getString("bank");
        bankAccount = getIntent().getExtras().getString("bankAccount");
        recipientId = getIntent().getExtras().getString("recipientId");

        mProgressDialog = new ProgressDialog(MoneyTransfer.this);
        transferType();
        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        senderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MR Transfer");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ///////////////////////////////////////////////////////////////
        neftContainer = (RelativeLayout) findViewById(R.id.neftContainer);
        impsContainer = (RelativeLayout) findViewById(R.id.impsContainer);
        neftRadio = (RadioButton) findViewById(R.id.neftRadio);
        impsRadio = (RadioButton) findViewById(R.id.impsRadio);

        transferAmount = (EditText) findViewById(R.id.transferAmount);

        submitButton = (TextView) findViewById(R.id.submitButton);
        closeButton = (TextView) findViewById(R.id.closeButton);

        neftRadio.setOnClickListener(this);
        impsRadio.setOnClickListener(this);
        neftContainer.setOnClickListener(this);
        impsContainer.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == neftContainer || v == neftRadio) {
            neftRadio.setChecked(true);
            impsRadio.setChecked(false);

            selectedFlag = 1;
            channel = "1";
        }

        if (v == impsContainer || v == impsRadio) {
            neftRadio.setChecked(false);
            impsRadio.setChecked(true);

            selectedFlag = 2;
            channel = "2";
        }

        if (v == closeButton) {
            finish();
        }

        if (v == submitButton) {
            if (validationForm() == 0) {
                if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.SendMoney(this, senderNumber, bankAccount, amount, recipientId, channel, mProgressDialog);

                } else {
                    UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message), 2);
                }
            }
        }

    }

    public int validationForm() {
        int flag = 0;

        if (selectedFlag != 0 && channel != null && channel.length() > 0) {

        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.attention_error_title), "Please specify transfer type", 2);
            flag++;
        }

        if (transferAmount.getText() != null && transferAmount.getText().toString().trim().length() > 0) {
            amount = transferAmount.getText().toString().trim();
        } else {
            transferAmount.setError(getResources().getString(R.string.amount_transfer_error));
            transferAmount.requestFocus();
            flag++;
        }

        return flag;
    }

    public void transferType() {
        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.bankListPref, null);

        Gson gson = new Gson();
        operatorList = gson.fromJson(response, BankListResponse.class);
        operator = operatorList.getBanks();

        for (BankListObject object : operator) {
            if (object.getBankName().equalsIgnoreCase(bank)) {
                if (object.getNEFT().equalsIgnoreCase("Yes")) {
                    NEFTFlag = true;
                }

                if (object.getIMPS().equalsIgnoreCase("Yes")) {
                    IMPSFlag = true;
                }
            } else {
                NEFTFlag = false;
                IMPSFlag = false;
            }
        }
    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("transferDone")) {

            ActivityActivityMessage activityActivityMessage =
                    new ActivityActivityMessage("transferDoneDialog");
            GlobalBus.getBus().post(activityActivityMessage);

            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }
}
