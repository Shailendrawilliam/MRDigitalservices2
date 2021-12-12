package com.mrmulti.Util.ui;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mrmulti.R;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.dto.ContactObjects;

import java.util.ArrayList;

/**
 * Created by Lalit on 08-02-2017.
 */

public class ContactListScreen extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    private Toolbar toolbar;
    TextView noData;
    EditText searchText;
    ImageView clearSearch;
    RelativeLayout search_layout;
    ContactListAdapter mAdapter;
    ArrayList<ContactObjects> contacts = new ArrayList<>();

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_screen);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        noData = (TextView) findViewById(R.id.noData);
        searchText = (EditText) findViewById(R.id.searchText);
        clearSearch = (ImageView) findViewById(R.id.clearSearch);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);

        clearSearch.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Phone Book");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        contacts = UtilMethods.INSTANCE.getContact(ContactListScreen.this);

        if (contacts != null && contacts.size() > 0) {
            noData.setVisibility(View.GONE);

            mAdapter = new ContactListAdapter(contacts, ContactListScreen.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);

        } else {
            noData.setVisibility(View.VISIBLE);
        }

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ArrayList<ContactObjects> finalList = new ArrayList<ContactObjects>();

                for (ContactObjects resList : contacts) {
                    if (resList.getName().toLowerCase().contains(s.toString().toLowerCase()) ||
                            resList.getNumber().toLowerCase().contains(s.toString().toLowerCase())) {
                        finalList.add(resList);
                    }
                }

                mAdapter = new ContactListAdapter(finalList, ContactListScreen.this);
                recycler_view.setAdapter(mAdapter);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_list_menu, menu);

        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            search_layout.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ItemClick(String name, String number) {
        Intent clickIntent = new Intent();
        clickIntent.putExtra("selected", name);
        clickIntent.putExtra("selectedNumber", number);
        setResult(2, clickIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == clearSearch) {
            searchText.setText("");
        }
    }
}
