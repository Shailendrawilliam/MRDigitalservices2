package com.mrmulti.Util.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrmulti.R;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.dto.BankListObject;
import com.mrmulti.Util.dto.BankListResponse;

import java.util.ArrayList;

/**
 * Created by Lalit on 14-04-2017.
 */

public class BankListScreen extends AppCompatActivity {

    RecyclerView recycler_view;
    TextView noData;
    BankListScreenAdapter mAdapter;
    ArrayList<BankListObject> operator = new ArrayList<>();
    BankListResponse operatorList = new BankListResponse();
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bank List");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        noData = (TextView) findViewById(R.id.noData);

        getOperatorList();

        if (operator != null && operator.size() > 0) {
            noData.setVisibility(View.GONE);

            mAdapter = new BankListScreenAdapter(operator, BankListScreen.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);

        } else {
            noData.setVisibility(View.VISIBLE);
        }
    }

    public void getOperatorList() {

        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String response = prefs.getString(ApplicationConstant.INSTANCE.bankListPref, null);

        Gson gson = new Gson();
        operatorList = gson.fromJson(response, BankListResponse.class);
        operator = operatorList.getBanks();

    }

    public void ItemClick(String id, String name, String accVerification, String shortCode) {
        Intent clickIntent = new Intent();
        clickIntent.putExtra("bankName", name);
        clickIntent.putExtra("bankId", id);
        clickIntent.putExtra("accVerification", accVerification);
        clickIntent.putExtra("shortCode", shortCode);
        setResult(4, clickIntent);
        finish();
    }
}
