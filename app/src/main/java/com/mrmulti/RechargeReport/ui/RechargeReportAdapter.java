package com.mrmulti.RechargeReport.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mrmulti.Activities.MyPrintDocumentAdapter;
import com.mrmulti.R;
import com.mrmulti.RechargeReport.dto.RechargeStatus;
import com.mrmulti.Util.UtilMethods;

import java.util.ArrayList;

/**
 * Created by Lalit on 10-04-2017.
 */

public class RechargeReportAdapter extends RecyclerView.Adapter<RechargeReportAdapter.MyViewHolder> {

    private ArrayList<RechargeStatus> transactionsList;
    private Context mContext;
    int resourceId = 0;
    String from = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView balance;
        public AppCompatTextView txn;
        public AppCompatTextView recharge;
        public AppCompatTextView amount;
        public AppCompatTextView date;
        public AppCompatImageView operatorImage;
        public AppCompatTextView operatorId;
        public AppCompatTextView message;
        public AppCompatTextView status;
        public AppCompatTextView disputeText;
        public AppCompatButton actionButton, disputeButton;
        public AppCompatImageView printButton;
        public ImageView statusImg;
        public RelativeLayout disputeTextContainer;

        public MyViewHolder(View view) {
            super(view);
            txn = (AppCompatTextView) view.findViewById(R.id.txn);
            recharge = (AppCompatTextView) view.findViewById(R.id.recharge);
            balance = (AppCompatTextView) view.findViewById(R.id.balance);
            amount = (AppCompatTextView) view.findViewById(R.id.amount);
            date = (AppCompatTextView) view.findViewById(R.id.date);
            message = (AppCompatTextView) view.findViewById(R.id.message);
            status = (AppCompatTextView) view.findViewById(R.id.status);
            disputeText = (AppCompatTextView) view.findViewById(R.id.disputeText);
            statusImg = (ImageView) view.findViewById(R.id.statusImg);
            printButton = (AppCompatImageView) view.findViewById(R.id.printButton);

            operatorImage = (AppCompatImageView) view.findViewById(R.id.operatorImage);
            operatorId = (AppCompatTextView) view.findViewById(R.id.operatorId);
            actionButton = (AppCompatButton) view.findViewById(R.id.actionButton);
            disputeButton = (AppCompatButton) view.findViewById(R.id.disputeButton);

            disputeTextContainer = (RelativeLayout) view.findViewById(R.id.disputeTextContainer);
        }
    }

    public RechargeReportAdapter(ArrayList<RechargeStatus> transactionsList, Context mContext,String from  ) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        this.from = from;
    }

    @Override
    public RechargeReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recharge_report_adapter, parent, false);

        return new RechargeReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RechargeReportAdapter.MyViewHolder holder, int position) {
        final RechargeStatus operator = transactionsList.get(position);
       try{
           holder.txn.setText("" + operator.getTID());
           holder.recharge.setText("" + operator.getRechargeNo());
           holder.balance.setText("" + mContext.getResources().getString(R.string.rupiya) + " " + operator.getBalanceAmt());
           holder.amount.setText(" " + mContext.getResources().getString(R.string.rupiya) + " " + operator.getAmount());
           holder.date.setText(operator.getRDate());
           holder.message.setText("Recharge of " + operator.getOpName() + " ,No. " + operator.getRechargeNo());

           String status = "";
           if(operator.getOpName().equals("APEPDCL") || operator.getOpName().equals("APSPDCL") || operator.getOpName().equals("TSSPDCL"))
           {
               status = "Success";
               holder.statusImg.setBackgroundResource(R.drawable.ic_status_success);
               if (operator.getIsDisputable().equalsIgnoreCase("1")) {
                   holder.disputeButton.setVisibility(View.GONE);
                   holder.disputeTextContainer.setVisibility(View.VISIBLE);
                   holder.printButton.setVisibility(View.VISIBLE);
               }
               else
               {
                   holder.disputeButton.setVisibility(View.GONE);
                   holder.disputeTextContainer.setVisibility(View.GONE);
                   holder.printButton.setVisibility(View.GONE);
               }

           }
           else
           {
               if (operator.getStatus().equalsIgnoreCase("SUCCESS")) {
                   status = "Success";
                   holder.statusImg.setBackgroundResource(R.drawable.ic_status_success);
               } else if (operator.getStatus().equalsIgnoreCase("FAILED")) {
                   status = "Failed";
                   holder.statusImg.setBackgroundResource(R.drawable.ic_status_failed);
               } else if (operator.getStatus().equalsIgnoreCase("REFUND")) {
                   status = "Refund";
                   holder.statusImg.setBackgroundResource(R.drawable.ic_status_refund);
               } else if (operator.getStatus().equalsIgnoreCase("PENDING") || operator.getStatus().equalsIgnoreCase("REQUEST SEND")
                       || operator.getStatus().equalsIgnoreCase("REQUEST SENT")) {
                   status = "Pending";
                   holder.statusImg.setBackgroundResource(R.drawable.ic_status_pending);
               } else {
                   status = "other";
                   holder.statusImg.setBackground(null);
               }

               if (operator.getIsDisputable().equalsIgnoreCase("1")) {
                   if (operator.getStatus().equalsIgnoreCase("SUCCESS")) {
                       holder.disputeButton.setSupportBackgroundTintList(ColorStateList.valueOf
                               (mContext.getResources().getColor(R.color.buttoncolor)));
                       holder.disputeTextContainer.setVisibility(View.GONE);
                       Log.e("getIsDisputable", from);
                       if (from.equalsIgnoreCase("specific")){
                           holder.disputeButton.setVisibility(View.VISIBLE);
                           holder.disputeButton.setClickable(true);
                       }else {
                           holder.disputeButton.setVisibility(View.GONE);

                       }

                   } else if (operator.getStatus().equalsIgnoreCase("Failed")) {
                       holder.disputeButton.setSupportBackgroundTintList(ColorStateList.valueOf
                               (mContext.getResources().getColor(R.color.bottommenu)));
                       holder.disputeTextContainer.setVisibility(View.GONE);
                       holder.disputeButton.setVisibility(View.GONE);
                   }
               } else if (operator.getIsDisputable().equalsIgnoreCase("0")) {
                   if (operator.getStatus().equalsIgnoreCase("SUCCESS")) {
                       holder.disputeButton.setSupportBackgroundTintList(ColorStateList.valueOf
                               (mContext.getResources().getColor(R.color.bottommenu)));
                       holder.disputeText.setText("" + operator.getIsDisputable());
                       holder.disputeTextContainer.setVisibility(View.VISIBLE);
                       holder.disputeButton.setVisibility(View.GONE);
                   } else {
                       holder.disputeButton.setSupportBackgroundTintList(ColorStateList.valueOf
                               (mContext.getResources().getColor(R.color.bottommenu)));
                       holder.disputeTextContainer.setVisibility(View.GONE);
                       holder.disputeButton.setVisibility(View.GONE);
                   }
               }
           }
           holder.status.setText("" + status);
           holder.operatorId.setText(operator.getOpID());

           //////////////////////////////////////////////////////////////////////////////


           holder.actionButton.setVisibility(View.GONE);

           if (operator.getOpName() != null && operator.getOpName().toString().length() > 0) {

               String imgName = operator.getOpName();
               String imgTemp = imgName.toLowerCase().replace(" ", "");
               String imgTemp1 = imgTemp.toLowerCase().replace("-", "");
               String imgTemp2 = imgTemp1.toLowerCase().replace("&", "");
               String imgTemp3 = imgTemp2.toLowerCase().replace("_", "");
               String imgTemp4 = imgTemp3.substring(0, imgTemp3.length());


               resourceId = mContext.getResources().getIdentifier(
                       imgTemp4, "drawable", mContext.getPackageName());

               Glide.with(mContext).asBitmap().load(resourceId)/*.centerCrop().error(R.drawable.ic_operator_default_icon)*/.into(new BitmapImageViewTarget(holder.operatorImage) {
                   @Override
                   protected void setResource(Bitmap resource) {
                       RoundedBitmapDrawable circularBitmapDrawable =
                               RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                       circularBitmapDrawable.setCircular(true);
                       holder.operatorImage.setImageDrawable(circularBitmapDrawable);
                   }
               });
           } else {
               holder.operatorImage.setImageResource(R.drawable.ic_operator_default_icon);
           }
           holder.printButton.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   String shareBody = "Mobile Number : " + operator.getRechargeNo() + "\n" +
                           "Operator Name :" + operator.getOpName() + "\n" +
                           "Amount :" + operator.getAmount() + "\n" +
                           "Date :" + operator.getRDate() + "\n" +
                           "Tx.ID :" + operator.getTID() + "\n" +
                           "Live.ID :" + operator.getMemberId() + "\n" +
                           "Recharge Status :" + operator.getStatus();

                   saveandprint(shareBody);
               }
           });
           holder.actionButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (UtilMethods.INSTANCE.isNetworkAvialable(mContext)) {
                       UtilMethods.INSTANCE.rechargeParams(mContext, operator.getRechargeNo(), operator.getAmount(), operator.getOpID(),
                               "", null,holder.actionButton,"");
                   } else {
                       UtilMethods.INSTANCE.dialogOk(mContext, mContext.getResources().getString(R.string.network_error_title),
                               mContext.getResources().getString(R.string.network_error_message), 2);
                   }

               }
           });

           holder.disputeButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   View view = inflater.inflate(R.layout.dispute_popup, null);

                   final TextInputLayout remarkLayout = (TextInputLayout) view.findViewById(R.id.remarkLayout);
                   final EditText remark = (EditText) view.findViewById(R.id.remark);

                   final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
                   final AppCompatButton cancelButton = (AppCompatButton) view.findViewById(R.id.cancelButton);

                   final Dialog dialog = new Dialog(mContext);

                   dialog.setTitle("Dispute Remark");
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

                           String remarkText;
                           if (remark.getText() != null && remark.getText().toString().trim().length() > 0) {
                               remarkText = remark.getText().toString().trim();
                           } else {
                               remarkText = "";
                           }

                           if (UtilMethods.INSTANCE.isNetworkAvialable(mContext)) {
                               dialog.dismiss();
                               UtilMethods.INSTANCE.Dispute(mContext, operator.getID() ,remarkText);
                           } else {
                               UtilMethods.INSTANCE.dialogOk(mContext, mContext.getResources().getString(R.string.network_error_title),
                                       mContext.getResources().getString(R.string.network_error_message), 2);
                           }

                       }
                   });
                   dialog.show();

               }
           });
       }catch (Exception e){}

    }
    private void saveandprint(String string) {
        MyPrintDocumentAdapter pd = new MyPrintDocumentAdapter(mContext, string);
        pd.printDocument(string);
    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}
