package com.mrmulti.DMR.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrmulti.DMR.dto.BENEFICIARY;
import com.mrmulti.DMR.dto.TABLE;
import com.mrmulti.R;
import com.mrmulti.Util.ActivityActivityMessage;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Lalit on 12-04-2017.
 */

public class BeneficiaryListScreen extends AppCompatActivity {

    RecyclerView recycler_view;
    TextView noData;
    BeneficiaryAdapter mAdapter;
    com.mrmulti.DMR.dto.TABLE beneResponse;
    Toolbar toolbar;
    TABLE TABLE;
    ArrayList<BENEFICIARY> operator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beneficiary_list_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Beneficiary List");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        noData = (TextView) findViewById(R.id.noData);

        getBeneficiaryList("");

        if (operator != null && operator.size() > 0) {
            noData.setVisibility(View.GONE);

            mAdapter = new BeneficiaryAdapter(operator, BeneficiaryListScreen.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);

        } else {
            noData.setVisibility(View.VISIBLE);
        }
    }

    public void getBeneficiaryList(String update) {

        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.beneficiaryListPref, null);

        Log.e("here", "log : " + response);
        Gson gson = new Gson();
        beneResponse = gson.fromJson(response, TABLE.class);

        if (!update.equalsIgnoreCase("update")) {
            if (beneResponse.getRESPONSESTATUS() != null && beneResponse.getRESPONSESTATUS().equalsIgnoreCase("1"))
                operator = beneResponse.getBENEFICIARY();
        } else {
            operator = beneResponse.getBENEFICIARY();
        }
    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("beneUpdate")) {
            Log.e("coming", "here");

            mAdapter.deleteDone();
            getBeneficiaryList("update");

            if (operator != null && operator.size() > 0) {
                noData.setVisibility(View.GONE);

                mAdapter = new BeneficiaryAdapter(operator, BeneficiaryListScreen.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recycler_view.setLayoutManager(mLayoutManager);
                recycler_view.setItemAnimator(new DefaultItemAnimator());
                recycler_view.setAdapter(mAdapter);

            } else {
                noData.setVisibility(View.VISIBLE);
            }
        } else if (activityFragmentMessage.getMessage().equalsIgnoreCase("transferDoneDialog")) {
            SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String senderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                UtilMethods.INSTANCE.GetSender(this, senderNumber, null);

            } else {
                UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
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
