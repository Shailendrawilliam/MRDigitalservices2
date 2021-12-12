package com.mrmulti.FundRecReport.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.mrmulti.FundRecReport.dto.FundRecObject;
import com.mrmulti.FundRecReport.dto.FundRecResponse;
import com.mrmulti.R;

import java.util.ArrayList;

/**
 * Created by Lalit on 15-04-2017.
 */

public class FundRecReport extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog mProgressDialog = null;
    private Toolbar toolbar;
    RecyclerView recycler_view;
    FundRecAdapter mAdapter;
    String response = "",from;
    LinearLayoutManager mLayoutManager;
    ArrayList<FundRecObject> transactionsObjects = new ArrayList<>();
    FundRecResponse transactions = new FundRecResponse();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledger_report);
        response = getIntent().getExtras().getString("response");
        from = getIntent().getExtras().getString("from");

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressDialog = new ProgressDialog(FundRecReport.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(from);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dataParse(response);

    }

    public void dataParse(String response) {
        Gson gson = new Gson();
        transactions = gson.fromJson(response, FundRecResponse.class);
        transactionsObjects = transactions.getFundReceive();

        mAdapter = new FundRecAdapter(transactionsObjects, FundRecReport.this,from);
        mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}