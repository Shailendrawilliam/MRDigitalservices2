package com.mrmulti.RechargeReport.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mrmulti.R;
import com.mrmulti.RechargeReport.dto.RechargeReportResponse;
import com.mrmulti.RechargeReport.dto.RechargeStatus;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Lalit on 10-04-2017.
 */

public class RechargeReport extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog mProgressDialog = null;
    private Toolbar toolbar;
    RecyclerView recycler_view;
    RechargeReportAdapter mAdapter;
    String response = "";
    String from = "";
    LinearLayoutManager mLayoutManager;
    ArrayList<RechargeStatus> transactionsObjects = new ArrayList<>();
    RechargeReportResponse transactions = new RechargeReportResponse();

    RelativeLayout searchLayout;
    EditText number;
    CardView cardContainer;
    EditText childNumber;
    String rechargeMobileNumber = "";
    String childMobileNumber = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_report);
        response = getIntent().getExtras().getString("response");
        from=response;
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressDialog = new ProgressDialog(RechargeReport.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Recharge History");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GetUpdate();

        number = (EditText) findViewById(R.id.number);
        cardContainer = (CardView) findViewById(R.id.cardContainer);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        cardContainer.setOnClickListener(this);

        childNumber = (EditText) findViewById(R.id.childNumber);

        if (response.equalsIgnoreCase("specific")) {
            searchLayout.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);

            if (UtilMethods.INSTANCE.getRoleId(this).equalsIgnoreCase("3")) {
                SharedPreferences myPrefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
                String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                childNumber.setVisibility(View.GONE);
                childMobileNumber = mobileLogin;
            } else {
                childNumber.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.VISIBLE);
                dataParse(response);
            }

        } else {
            searchLayout.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.VISIBLE);
            dataParse(response);
        }
    }

    public void dataParse(String response) {
        Gson gson = new Gson();
        try{
            transactions = gson.fromJson(response, RechargeReportResponse.class);
            transactionsObjects = transactions.getRechargeStatus();

            mAdapter = new RechargeReportAdapter(transactionsObjects, RechargeReport.this,from);
            mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);
        }catch (Exception e){}

    }

    @Override
    public void onClick(View v) {
        if (v == cardContainer) {

            if (validateForm() == 0) {
                if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.LastRechargeStatus(this, rechargeMobileNumber, childMobileNumber, mProgressDialog, "specific");

                } else {
                    UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message), 2);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.menu_refresh, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
          Refresh();
            return true;
        }

        return true;



    }

    private void Refresh() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
//specific
            UtilMethods.INSTANCE.LastRechargeStatus(this, rechargeMobileNumber, childMobileNumber, mProgressDialog, "recentnew");

        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message), 2);
        }
    }

    public int validateForm() {

        int flag = 0;

        if (number.getText() != null && number.getText().toString().trim().length() > 1 )
        {
            rechargeMobileNumber = number.getText().toString().trim();
        } else {
            number.setError(getResources().getString(R.string.mobilenumber_error));
            number.requestFocus();
            flag++;
        }
        if (!UtilMethods.INSTANCE.getRoleId(this).equalsIgnoreCase("3")) {
            if (childNumber.getText() != null && childNumber.getText().toString().trim().length() > 1 &&
                    !(childNumber.getText().toString().trim().length() < 10)) {
                childMobileNumber = childNumber.getText().toString().trim();
            } else {
                childNumber.setError(getResources().getString(R.string.mobilenumber_error));
                childNumber.requestFocus();
                flag++;
            }
        }
        return flag;
    }


    public void specificReportList(String response,String from) {

       if (from.equalsIgnoreCase("specific")){
           searchLayout.setVisibility(View.VISIBLE);
           recycler_view.setVisibility(View.VISIBLE);
       }

        dataParse(response);
    }
    public void GetUpdate() {
        if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            UtilMethods.INSTANCE.RecentRecharges(this, "", "", mProgressDialog, "all");

        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message), 2);
        }
    }
    @Override
    public void onResume() {
        // browsePlanButton.setVisibility(View.GONE);
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getFrom().equalsIgnoreCase("recent2")) {

            dataParse(activityFragmentMessage.getMessage());
        }
    }
}
