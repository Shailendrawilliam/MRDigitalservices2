package com.mrmulti.FundTransferStat;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mrmulti.FundRecReport.dto.FundRecObject;
import com.mrmulti.FundRecReport.dto.FundRecResponse;
import com.mrmulti.R;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lalit on 15-04-2017.
 */

public class FundTransferStatement extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog mProgressDialog = null;
    private Toolbar toolbar;
    RecyclerView recycler_view;
    FundTransferAdapter mAdapter;
    String response = "";
    LinearLayoutManager mLayoutManager;
    ArrayList<FundRecObject> transactionsObjects = new ArrayList<>();
    FundRecResponse transactions = new FundRecResponse();

    LinearLayout llSearchContainer;
    RelativeLayout rlsearch;
    EditText MobileNo;

    boolean visibleFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledger_report);
        response = getIntent().getExtras().getString("response");

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressDialog = new ProgressDialog(FundTransferStatement.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setTitle("Fund Transfer Statement");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llSearchContainer=(LinearLayout)findViewById(R.id.searchContainer);
        rlsearch=(RelativeLayout)findViewById(R.id.searchLayout);
        MobileNo=(EditText) findViewById(R.id.numberSearch);
        rlsearch.setOnClickListener(this);
        MobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = MobileNo.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = MobileNo.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }
        });
        dataParse(response);
    }
    void filter(String text){
        ArrayList<FundRecObject> temp = new ArrayList();
//        Log.e("transactionsObjects",userListObjects.size()+"");
//        Log.e("Names :",userListObjects.get(0).getName());
//        Log.e("Names :",userListObjects.get(1).getName());
//        for(UserListRetailer d: transactionsObjects){
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if(d.getName().toLowerCase(Locale.getDefault()).contains(text) || d.getOutletName().toLowerCase(Locale.getDefault()).contains(text)){
//                temp.add(d);
//            }
//        }
        for(int i=0; i<transactionsObjects.size(); i++){
            if( text.toLowerCase(Locale.getDefault()).equalsIgnoreCase(transactionsObjects.get(i).getFrom().toLowerCase(Locale.getDefault()))
                    || transactionsObjects.get(i).getFrom().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))
                    ||text.toLowerCase(Locale.getDefault()).equalsIgnoreCase(transactionsObjects.get(i).getTo().toLowerCase(Locale.getDefault()))
                    || transactionsObjects.get(i).getTo().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))
            ){

                temp.add(transactionsObjects.get(i));
            }
        }
        //update recyclerview
        try{
            mAdapter.updateList(temp);
        }catch ( Exception e){}
    }

    public void dataParse(String response) {
        Gson gson = new Gson();
        transactions = gson.fromJson(response, FundRecResponse.class);
        transactionsObjects = transactions.getFundReceive();
       // transactionsObjects = transactions.getFundTransfer();

        mAdapter = new FundTransferAdapter(transactionsObjects, FundTransferStatement.this);
        mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v==rlsearch)
        {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.FundReceiveStatement(this, "","",MobileNo.getText().toString() , mProgressDialog,"specific");
            } else {
                UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dmr_report_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dmr_report_search) {

            if (visibleFlag) {
                visibleFlag = false;
                llSearchContainer.setVisibility(View.GONE);
            } else {
                visibleFlag = true;
                llSearchContainer.setVisibility(View.VISIBLE);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onActivityActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("fund_statement")) {
            dataParse(activityFragmentMessage.getFrom());
        }
    }
}