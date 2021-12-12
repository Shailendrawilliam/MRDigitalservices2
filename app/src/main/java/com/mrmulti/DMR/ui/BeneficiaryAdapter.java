package com.mrmulti.DMR.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrmulti.DMR.dto.BENEFICIARY;
import com.mrmulti.R;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.UtilMethods;

import java.util.ArrayList;

/**
 * Created by Lalit on 12-04-2017.
 */

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.MyViewHolder> {

    private ArrayList<BENEFICIARY> operatorList;
    private Context mContext;
    int resourceId = 0;
    private ProgressDialog mProgressDialog;
    Dialog dialog = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView beneName;
        public TextView beneAccountNumber;
        public TextView beneBank;
        public TextView beneIFSC;

        public RelativeLayout transferLayout;
        public RelativeLayout deleteLayout;

        public MyViewHolder(View view) {
            super(view);
            beneName = (TextView) view.findViewById(R.id.beneName);
            beneAccountNumber = (TextView) view.findViewById(R.id.beneAccountNumber);
            beneBank = (TextView) view.findViewById(R.id.beneBank);
            beneIFSC = (TextView) view.findViewById(R.id.beneIFSC);

            transferLayout = (RelativeLayout) view.findViewById(R.id.transferLayout);
            deleteLayout = (RelativeLayout) view.findViewById(R.id.deleteLayout);

        }
    }

    public BeneficiaryAdapter(ArrayList<BENEFICIARY> operatorList, Context mContext) {
        this.operatorList = operatorList;
        this.mContext = mContext;
    }

    @Override
    public BeneficiaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beneficiary_list_adapter, parent, false);

        mProgressDialog = new ProgressDialog(mContext);

        return new BeneficiaryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BeneficiaryAdapter.MyViewHolder holder, final int position) {
        final BENEFICIARY operator = operatorList.get(position);
        holder.beneName.setText(operator.getNAME());
        holder.beneAccountNumber.setText(operator.getACCOUNT());
        holder.beneBank.setText(operator.getBANK());
        holder.beneIFSC.setText(operator.getIFSC());

        holder.transferLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferIntent = new Intent(mContext, MoneyTransfer.class);
                transferIntent.putExtra("name", operatorList.get(position).getNAME());
                transferIntent.putExtra("bankAccount", operatorList.get(position).getACCOUNT());
                transferIntent.putExtra("bank", operatorList.get(position).getBANK());
                transferIntent.putExtra("recipientId", operatorList.get(position).getRECIPIENTID());
                mContext.startActivity(transferIntent);
            }
        });

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog(operatorList.get(position).getRECIPIENTID(), operatorList.get(position).getNAME(),
                        operatorList.get(position).getMOBILENO());
            }
        });
    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

    public void confirmationDialog(final String id, final String beneName, final String beneNumber) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.confirm_dialog, null);

        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView number = (TextView) view.findViewById(R.id.number);
        final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
        final AppCompatButton cancelButton = (AppCompatButton) view.findViewById(R.id.cancelButton);

        name.setText("" + beneName);
        number.setText("" + beneNumber);

        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setContentView(view);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = mContext.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, mContext.MODE_PRIVATE);
                String currentSenderNumber = prefs.getString(ApplicationConstant.INSTANCE.senderNumberPref, null);

                if (UtilMethods.INSTANCE.isNetworkAvialable(mContext)) {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.DeleteBeneficiary(mContext, currentSenderNumber, id, mProgressDialog);

                } else {
                    UtilMethods.INSTANCE.dialogOk(mContext, mContext.getResources().getString(R.string.network_error_title),
                            mContext.getResources().getString(R.string.network_error_message), 2);
                }
            }
        });
        dialog.show();
    }

    public void deleteDone() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
