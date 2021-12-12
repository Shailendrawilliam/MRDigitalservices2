package com.mrmulti.Dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrmulti.R;
import com.mrmulti.RechargeReport.ui.RechargeReport;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.UtilMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReportLiActivity extends AppCompatActivity  implements View.OnClickListener {
LinearLayout rechargeReport,ledgerReport,userDayBook,fundRecReport,commisionSlab,specificReport,disputeReport,
        fundtransferReportlayout,ll_user_role;
ProgressDialog mProgressDialog;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_li);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            getSupportActionBar().setTitle("Report");

      //  Log.e("userId"," userId "+ userId);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rechargeReport=findViewById(R.id.rechargeReport);
        ledgerReport=findViewById(R.id.ledgerReport);
        userDayBook=findViewById(R.id.userDayBook);
        fundRecReport=findViewById(R.id.fundRecReport);
        commisionSlab=findViewById(R.id.commisionSlab);
        ll_user_role=findViewById(R.id.ll_user_role);
        specificReport=findViewById(R.id.specificReport);
        disputeReport=findViewById(R.id.disputeReport);
        fundtransferReportlayout=findViewById(R.id.fundtransferReportlayout);
        mProgressDialog= new ProgressDialog(this);
        rechargeReport.setOnClickListener(this);
        ledgerReport.setOnClickListener(this);
        userDayBook.setOnClickListener(this);
        fundRecReport.setOnClickListener(this);
        commisionSlab.setOnClickListener(this);
        specificReport.setOnClickListener(this);
        disputeReport.setOnClickListener(this);
        fundtransferReportlayout.setOnClickListener(this);
        ll_user_role.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {



        if (v == rechargeReport) {


            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.LastRechargeStatus(ReportLiActivity.this, "", "", mProgressDialog, "recent");

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }


        }
        if (v == specificReport) {
            Intent transactionIntent = new Intent(ReportLiActivity.this, RechargeReport.class);
            transactionIntent.putExtra("response", "specific");
            transactionIntent.putExtra("from", "");
           startActivity(transactionIntent);

        } if (v == ll_user_role) {
            Intent transactionIntent = new Intent(ReportLiActivity.this, UserRoleActivity.class);

       startActivity(transactionIntent);

        }
        if (v == ledgerReport) {

            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.Ledger(ReportLiActivity.this, "", "", "", mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v == disputeReport) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.DisputeReport(ReportLiActivity.this, mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v == fundRecReport) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.FundReceiveStatus(ReportLiActivity.this, "","",mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v == fundtransferReportlayout) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.FundReceiveStatement(ReportLiActivity.this,"","", "", mProgressDialog, "all");

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }


        }
 
        if (v == userDayBook) {
            LayoutInflater inflater = (LayoutInflater) ReportLiActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewTemp = inflater.inflate(R.layout.userdaybook_dateselection, null);

            final TextView fromDate = (TextView) viewTemp.findViewById(R.id.fromDate);
            final EditText childNumber = (EditText) viewTemp.findViewById(R.id.childNumber);
            final AppCompatButton okButton = (AppCompatButton) viewTemp.findViewById(R.id.okButton);
            final AppCompatButton cancelButton = (AppCompatButton) viewTemp.findViewById(R.id.cancelButton);

            final Dialog dialog = new Dialog(ReportLiActivity.this);

            dialog.setCancelable(false);
            dialog.setContentView(viewTemp);

            if (UtilMethods.INSTANCE.getRoleId(ReportLiActivity.this).equalsIgnoreCase("1")) {
                childNumber.setVisibility(View.GONE);
            } else {
                childNumber.setVisibility(View.VISIBLE);
            }

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                    String myFormat = "dd/MMM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    fromDate.setText(sdf.format(myCalendar.getTime()));
                }

            };
            fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(ReportLiActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromDate.getText() != null && fromDate.getText().toString().trim().length() > 0) {

                        if (UtilMethods.INSTANCE.getRoleId(ReportLiActivity.this).equalsIgnoreCase("1")) {

                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            SharedPreferences myPrefs = ReportLiActivity.this.getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, ReportLiActivity.this.MODE_PRIVATE);
                            String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                            UtilMethods.INSTANCE.GetUserDayBook(ReportLiActivity.this, "" + mobileLogin,
                                    "" + fromDate.getText().toString().trim(), mProgressDialog);

                        } else {
                            if (childNumber.getText() != null && childNumber.getText().toString().trim().length() > 0) {
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                SharedPreferences myPrefs = ReportLiActivity.this.getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, ReportLiActivity.this.MODE_PRIVATE);
                                String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                                UtilMethods.INSTANCE.GetUserDayBook(ReportLiActivity.this, "" + childNumber.getText().toString().trim(),
                                        "" + fromDate.getText().toString().trim(), mProgressDialog);
                            } else {
                                childNumber.setError("Please enter child mobile number !!");
                                childNumber.requestFocus();
                            }
                        }

                    } else {
                        fromDate.setError("Please select From date !!");
                        fromDate.requestFocus();
                    }
                }
            });
            dialog.show();
        }
        if (v == commisionSlab) {

            if (UtilMethods.INSTANCE.isNetworkAvialable(ReportLiActivity.this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.CommisionSlab(ReportLiActivity.this, mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(ReportLiActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
    }
}