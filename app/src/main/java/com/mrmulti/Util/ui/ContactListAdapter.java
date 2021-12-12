package com.mrmulti.Util.ui;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mrmulti.R;
import com.mrmulti.Util.dto.ContactObjects;

import java.util.ArrayList;

/**
 * Created by Lalit on 08-02-2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private ArrayList<ContactObjects> contactsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public ImageView opImage;
        public RelativeLayout container;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            number = (TextView) view.findViewById(R.id.number);
            opImage = (ImageView) view.findViewById(R.id.opImage);
            container = (RelativeLayout) view.findViewById(R.id.container);

        }
    }

    public ContactListAdapter(ArrayList<ContactObjects> contactsList, Context mContext) {
        this.contactsList = contactsList;
        this.mContext = mContext;
    }

    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_adapter, parent, false);

        return new ContactListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.MyViewHolder holder, int position) {
        final ContactObjects operator = contactsList.get(position);
        holder.name.setText(operator.getName());
        holder.number.setText(operator.getNumber());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContactListScreen) mContext).ItemClick(operator.getName(), operator.getNumber());
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

}
