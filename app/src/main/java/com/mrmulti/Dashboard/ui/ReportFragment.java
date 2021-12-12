package com.mrmulti.Dashboard.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrmulti.Dashboard.dto.HomeItem;
import com.mrmulti.R;
import com.mrmulti.RechargeReport.ui.RechargeReport;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.UtilMethods;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lalit on 06-04-2017.
 */

public class ReportFragment extends Fragment implements  View.OnClickListener {

    RecyclerView recycler_view0;
    private HomeFragmentAdapter adapter0;
    private List<HomeItem> itemList0;
    private ProgressDialog mProgressDialog = null;
    ImageView recharge_report,specific_report,ledger_report,dispute_report,fund_recieve_report,user_day_book_report,
            commission_report,fundtransReport,notice_view, UserSaleReportDetails,UserSaleAllDetailReport,
    FundReceiveStatement , GetSaleCoupan,OfferSheet,RechargeOffer;
LinearLayout fundtransferReportlayout,row2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.report_fragment, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        recycler_view0 = (RecyclerView) v.findViewById(R.id.recycler_view0);

        itemList0 = new ArrayList<>();
        recharge_report  =  v.findViewById(R.id.rechargeReport);
        specific_report  =  v.findViewById(R.id.specificReport);
        ledger_report =  v.findViewById(R.id.ledgerReport);
        dispute_report =  v.findViewById(R.id.disputeReport);
        fund_recieve_report =  v.findViewById(R.id.fundRecReport);

        fundtransReport =  v.findViewById(R.id.fundtransReport);
        user_day_book_report =  v.findViewById(R.id.userDayBook);
        commission_report =  v.findViewById(R.id.commisionSlab);
        UserSaleReportDetails =  v.findViewById(R.id.UserSaleReportDetails);
        UserSaleAllDetailReport =  v.findViewById(R.id.UserSaleAllDetailReport);
        FundReceiveStatement =  v.findViewById(R.id.FundReceiveStatement);
        GetSaleCoupan =  v.findViewById(R.id.GetSaleCoupan);
        OfferSheet =  v.findViewById(R.id.OfferSheet);
        RechargeOffer =  v.findViewById(R.id.notice_view);
        fundtransferReportlayout =  v.findViewById(R.id.fundtransferReportlayout);
        row2 =  v.findViewById(R.id.row2);

        if (!UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("3")) {

            fundtransferReportlayout.setVisibility(View.VISIBLE);
            row2.setVisibility(View.VISIBLE);

        }else {
            fundtransferReportlayout.setVisibility(View.INVISIBLE);
            row2.setVisibility(View.GONE);
        }
        recharge_report.setOnClickListener((View.OnClickListener) this);
        specific_report.setOnClickListener((View.OnClickListener) this);
        ledger_report.setOnClickListener((View.OnClickListener) this);
        dispute_report.setOnClickListener((View.OnClickListener) this);
        fund_recieve_report.setOnClickListener((View.OnClickListener) this);

        fundtransReport.setOnClickListener((View.OnClickListener) this);
        user_day_book_report.setOnClickListener((View.OnClickListener) this);
        commission_report.setOnClickListener((View.OnClickListener) this);

        UserSaleReportDetails.setOnClickListener((View.OnClickListener) this);
        UserSaleAllDetailReport.setOnClickListener((View.OnClickListener) this);
        FundReceiveStatement.setOnClickListener((View.OnClickListener) this);
        GetSaleCoupan.setOnClickListener((View.OnClickListener) this);
        OfferSheet.setOnClickListener((View.OnClickListener) this);
        RechargeOffer.setOnClickListener((View.OnClickListener) this);

        HomeItem item1 = new HomeItem("two", 0, R.drawable.specific_report, 0);
        HomeItem item2 = new HomeItem("three", 0, R.drawable.ledger_report, 0);
        HomeItem item3 = new HomeItem("four", 0, R.drawable.dispute_report, 0);

        HomeItem item4 = new HomeItem("four", 0, R.drawable.fundrec_report, 0);
        HomeItem item5 = new HomeItem("four", 0, R.drawable.daybook_report, 0);

        HomeItem item6 = new HomeItem("four", 0, R.drawable.commission_slab, 0);
        HomeItem item7 = new HomeItem("four", 0, R.drawable.fund_transfer_statement, 0);
        HomeItem item8 = new HomeItem("four", 0, R.drawable.dthbookingreport, 0);
        HomeItem item9 = new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item10= new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item11 = new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item12 = new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item13 = new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item14 = new HomeItem("four", 0, R.drawable.userdth, 0);
        HomeItem item15 = new HomeItem("four", 0, R.drawable.userdth, 0);

        itemList0.add(item1);
        itemList0.add(item2);
        itemList0.add(item3);
        itemList0.add(item4);
        itemList0.add(item5);
        itemList0.add(item6);
        itemList0.add(item7);
        itemList0.add(item8);
        itemList0.add(item9);

        itemList0.add(item10);
        itemList0.add(item11);
        itemList0.add(item12);
        itemList0.add(item13);
        itemList0.add(item14);
        itemList0.add(item15);
       /* if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("0"))
            itemList0.add(item7);*/

        adapter0 = new HomeFragmentAdapter(getActivity(), itemList0, "");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_view0.setLayoutManager(layoutManager);
        recycler_view0.setAdapter(adapter0);

        recycler_view0.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        if (position == 0) {

                            Intent transactionIntent = new Intent(getActivity(), RechargeReport.class);
                            transactionIntent.putExtra("response", "specific");
                            transactionIntent.putExtra("from", "");
                            getActivity().startActivity(transactionIntent);

                        } else if (position == 1) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.Ledger(getActivity(), "", "","", mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        } else if (position == 2) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.DisputeReport(getActivity(), mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        } else if (position == 3) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.FundReceiveStatus(getActivity(),"","", mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }else if (position == 4) {
                            ////////////////////////////////////////////////////////////////////////////////
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View viewTemp = inflater.inflate(R.layout.userdaybook_dateselection, null);

                            final TextView fromDate = (TextView) viewTemp.findViewById(R.id.fromDate);
                            final EditText childNumber = (EditText) viewTemp.findViewById(R.id.childNumber);
                            final AppCompatButton okButton = (AppCompatButton) viewTemp.findViewById(R.id.okButton);
                            final AppCompatButton cancelButton = (AppCompatButton) viewTemp.findViewById(R.id.cancelButton);

                            final Dialog dialog = new Dialog(getActivity());

                            dialog.setCancelable(false);
                            dialog.setContentView(viewTemp);

                            if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("1")) {
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
                                    new DatePickerDialog(getActivity(), date, myCalendar
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

                                        if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("1")) {

                                            mProgressDialog.setIndeterminate(true);
                                            mProgressDialog.setMessage("Loading...");
                                            mProgressDialog.show();

                                            SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                                            String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                                            UtilMethods.INSTANCE.GetUserDayBook(getActivity(), "" + mobileLogin,
                                                    "" + fromDate.getText().toString().trim(), mProgressDialog);

                                        } else {
                                            if (childNumber.getText() != null && childNumber.getText().toString().trim().length() > 0) {
                                                mProgressDialog.setIndeterminate(true);
                                                mProgressDialog.setMessage("Loading...");
                                                mProgressDialog.show();

                                                SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                                                String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                                                UtilMethods.INSTANCE.GetUserDayBook(getActivity(), "" + childNumber.getText().toString().trim(),
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
                            ////////////////////////////////////////////////////////////////////////////////
                        } else if (position == 5) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.CommisionSlab(getActivity(), mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }else if (position == 6) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.FundReceiveStatement(getActivity(),"","","", mProgressDialog,"all");

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }else if (position == 7) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.GetNotice(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 8) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 9) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 10) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 11) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 12) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 13) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                        else if (position == 14) {
                            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                UtilMethods.INSTANCE.UserSaleReport(getActivity(),  mProgressDialog);

                            } else {
                                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                                        getResources().getString(R.string.network_error_message), 2);
                            }
                        }
                    }
                }
                )
        );
        return v;
    }
    @Override
    public void onClick(View v) {

        if (v==recharge_report){


            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.LastRechargeStatus(  getActivity()  , "", "", mProgressDialog, "Recent");

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }


        }
        if (v==specific_report){
            Intent transactionIntent = new Intent(getActivity(), RechargeReport.class);
            transactionIntent.putExtra("response", "specific");
            transactionIntent.putExtra("from", "");
            getActivity().startActivity(transactionIntent);

        }
        if (v==ledger_report){

            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.Ledger(getActivity(), "", "","", mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v==dispute_report){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.DisputeReport(getActivity(), mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v==fund_recieve_report){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

             ///   mProgressDialog.setIndeterminate(true);
             //   mProgressDialog.setMessage("Loading...");
             //   mProgressDialog.show();

                UtilMethods.INSTANCE.FundReceiveStatus(getActivity(),"","", mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if(v==fundtransReport){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.FundReceiveStatement(getActivity(),"","","", mProgressDialog,"all");

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }


        }


        if (v==notice_view){



            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.GetNotice(getActivity(),  mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v==user_day_book_report){
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewTemp = inflater.inflate(R.layout.userdaybook_dateselection, null);

            final TextView fromDate = (TextView) viewTemp.findViewById(R.id.fromDate);
            final EditText childNumber = (EditText) viewTemp.findViewById(R.id.childNumber);
            final AppCompatButton okButton = (AppCompatButton) viewTemp.findViewById(R.id.okButton);
            final AppCompatButton cancelButton = (AppCompatButton) viewTemp.findViewById(R.id.cancelButton);

            final Dialog dialog = new Dialog(getActivity());

            dialog.setCancelable(false);
            dialog.setContentView(viewTemp);

            if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("1")) {
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
                    new DatePickerDialog(getActivity(), date, myCalendar
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

                        if (UtilMethods.INSTANCE.getRoleId(getActivity()).equalsIgnoreCase("1")) {

                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                            String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                            UtilMethods.INSTANCE.GetUserDayBook(getActivity(), "" + mobileLogin,
                                    "" + fromDate.getText().toString().trim(), mProgressDialog);

                        } else {
                            if (childNumber.getText() != null && childNumber.getText().toString().trim().length() > 0) {
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                SharedPreferences myPrefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, getActivity().MODE_PRIVATE);
                                String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

                                UtilMethods.INSTANCE.GetUserDayBook(getActivity(), "" + childNumber.getText().toString().trim(),
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
        if (v==commission_report){

            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.CommisionSlab(getActivity(), mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }

        if (v==GetSaleCoupan){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.GetSaleCoupan(getActivity(),  mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v==UserSaleReportDetails){
            Intent intent = new Intent(getActivity(), DisMemberList.class);
            intent.putExtra("from", "UserSaleReportDetailDateWise");
            startActivity(intent);
//            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {
//
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.setMessage("Loading...");
//                mProgressDialog.show();
//
//                UtilMethods.INSTANCE.UserSaleReportDetailDateWise(getActivity(),"",  mProgressDialog );
//
//            } else {
//                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
//                        getResources().getString(R.string.network_error_message), 2);
//            }
        }
        if (v==UserSaleAllDetailReport){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.UserSaleReportDetailAllDateWise(getActivity(),"",  mProgressDialog );

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }

        if (v==FundReceiveStatement){
            Intent intent = new Intent(getActivity(), DisMemberList.class);
            intent.putExtra("from", "FundReceiveStatementDateWise");
            startActivity(intent);
//
//            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {
//
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.setMessage("Loading...");
//                mProgressDialog.show();
//
//                UtilMethods.INSTANCE.FundReceiveStatementDateWise(getActivity(),"",  mProgressDialog,"");
//
//            } else {
//                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
//                        getResources().getString(R.string.network_error_message), 2);
//            }

        }

        if (v==OfferSheet){

            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.OfferSheet(getActivity(),  mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }
        if (v==RechargeOffer){
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.GetNotice(getActivity(),  mProgressDialog);

            } else {
                UtilMethods.INSTANCE.dialogOk(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 2);
            }
        }



    }
    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getFrom().equalsIgnoreCase("senderLogin")) {
            //  senderLogin(activityFragmentMessage.getMessage());
        }
    }
}