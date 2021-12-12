package com.mrmulti.Dashboard.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rampo.updatechecker.UpdateChecker;
import com.mrmulti.Activities.NoticeBoardActivity;
import com.mrmulti.Activities.PaymentRequest;
import com.mrmulti.BalanceCheck.dto.BalanceCheckResponse;
import com.mrmulti.BalanceCheck.dto.ChildBalance;
import com.mrmulti.GooglePlayStoreAppVersionNameLoader;
import com.mrmulti.R;
import com.mrmulti.Util.ActivityActivityMessage;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.locale.LocaleManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.content.pm.PackageManager.GET_META_DATA;

/**
 * Created by Lalit on 01-04-2017.
 */

public class Dashboard3 extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgressDialog = null;
    TextView balanceUtility, balancePrepaid,number;
    TextView iv_11, iv_12;
    TextView iv_13;
    LinearLayout balancePrepaidLayout, balanceUtilityLayout;
    TextView news, tv_cc;
    FragmentManager fm;
    String time;
    Spinner lang_Spinner;

    Locale myLocale;
    String currentLanguage = "en", currentLang;
    private String version = "";
    String versionName = "";
    int versionCode = -1;
    private static long back_pressed;
    public static int countBackstack = 0;
    public static FragmentManagerHelper fragmentManagerHelper;
    public static FragmentManagerHelper getFragmentManagerHelper;

    @Override
    public void onClick(View v) {
        if ((v == iv_11) || v == iv_12 || v == iv_13) {
            showCC();
        }
    }

//    @Retention(RetentionPolicy.SOURCE)
//    @StringDef({ENGLISH, HINDI, SPANISH})
//    public @interface LocaleDef {
//        String[] SUPPORTED_LOCALES = {ENGLISH, HINDI, SPANISH};
//    }

    static final String ENGLISH = "en";
    //static final String HINDI = "hi";
    static final String SPANISH = "es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        currentLanguage = getIntent().getStringExtra(currentLang);
      //  Locale myLocale = new Locale("hi");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
        mProgressDialog = new ProgressDialog(this);
        version = GooglePlayStoreAppVersionNameLoader.newVersion;
        checkNumberList();
        getVersionInfo();
        ShowPopup();
        ShowPopupNow();

//   ChangeFragment(   );
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_container, new HomeFragment(), "Home");
        transaction.addToBackStack(null);
        transaction.commit();
        if (version != null && version.length() > 0) {
            if (!version.equals(versionName)) {
                UpdateChecker checker = new UpdateChecker(this);// If you are in a Activity or a FragmentActivity
                checker.showDialog("Update");
                checker.start();
            }
        }
        resetTitles();
        if (UtilMethods.INSTANCE.getKeyId(Dashboard3.this) != null &&
                UtilMethods.INSTANCE.getKeyId(Dashboard3.this).length() > 0) {
            if (UtilMethods.INSTANCE.getRegKeyStatus(Dashboard3.this) != null &&
                    UtilMethods.INSTANCE.getRegKeyStatus(Dashboard3.this).length() > 0) {

            } else {
                UtilMethods.INSTANCE.KeyUpdate(Dashboard3.this, UtilMethods.INSTANCE.getKeyId(Dashboard3.this));
            }
        } else {

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //  mSectionsPagerAdapter = new Dashboard3.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //  mViewPager = (ViewPager) findViewById(R.id.containerpager);
        //  mViewPager.setAdapter(mSectionsPagerAdapter);

        // TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        balanceUtility = (TextView) findViewById(R.id.balanceUtility);
        balancePrepaid = (TextView) findViewById(R.id.balancePrepaid);
        number = (TextView) findViewById(R.id.number);
        iv_11 = (TextView) findViewById(R.id.iv_11);
        iv_12 = (TextView) findViewById(R.id.iv_12);
        iv_13 = (TextView) findViewById(R.id.iv_13);
        //tv_cc = (TextView) findViewById(R.id.tv_cc);
        balancePrepaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCC();
            }


        });
        iv_11.setOnClickListener(this);
        iv_12.setOnClickListener(this);
        iv_13.setOnClickListener(this);

        balancePrepaidLayout = (LinearLayout) findViewById(R.id.balancePrepaidLayout);
        balanceUtilityLayout = (LinearLayout) findViewById(R.id.balanceUtilityLayout);

        news = (TextView) findViewById(R.id.getnews);
        lang_Spinner = (Spinner) findViewById(R.id.lang_Spinner);
        news.setSelected(true);
        List<String> list = new ArrayList<String>();

        list.add("Select language");
        list.add("English");
        list.add("Español");
        list.add("Français");
       // list.add("Hindi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang_Spinner.setAdapter(adapter);

//        lang_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                switch (position) {
//                    case 0:
//                        break;
//                    //case 1:
//                        //setLocale("en");
//                        //  setNewLocale(this, LocaleManager.ENGLISH);
//                     //   break;
//                    case 2:
//                        setLocale("es");
//                        break;
//                    case 3:
//                        setLocale("fr");
//                        break;
//                  //  case 4:
//                       // setLocale("hi");
//                      //  break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });


//        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////Setting the ArrayAdapter data on the Spinner
//        lang_Spinner.setAdapter(aa);
//        lang_Spinner.setSelection(0);
//        lang_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int position, long id){
//                Selected_lang=  arg0.getItemAtPosition(position).toString();
//if (!Selected_lang.isEmpty()){
//    changlang(Selected_lang);
//}

//                if (Selected_lang.equalsIgnoreCase("Cash deposit")){
//
//                    ((TextView)lang_Spinner.getSelectedView()).setText(getRandomString(20));
//                }else {
//                    ((TextView)lang_Spinner.getSelectedView()).setText("");
//                }
        // }

//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                ((TextView)lang_Spinner.getSelectedView()).setError("Select language");
//            }
//        });
    /*    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        news.startAnimation(animation1);
*/

        //UtilMethods.INSTANCE.App_Services_Provided(this);

        BalanceRefresh();

    }

    private void showCC() {
        try {
            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String popupresponse = myPreferences.getString(ApplicationConstant.INSTANCE.popupPref, null);
            //Log.e("response",popupresponse);

            if (popupresponse != null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layoutcc, null);

                final ImageView recharge = (ImageView) view.findViewById(R.id.iv_1);
                final ImageView addmoney = (ImageView) view.findViewById(R.id.iv_2);
                final ImageView tapp = (ImageView) view.findViewById(R.id.iv_3);

                final Dialog dialog = new Dialog(this);

                dialog.setContentView(view);

                //   title.setText("" + title1);
                tapp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Performs action on click
//                            Intent sendIntent = new Intent();
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                            sendIntent.setType("text/plain");
//                            sendIntent.setPackage("com.whatsapp");
//                            startActivity(Intent.createChooser(sendIntent, ""));
//                            startActivity(sendIntent);

                        String url = "https://api.whatsapp.com/send?phone=" + "";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        //opens the portfolio details class
                    }
                });
               /* addmoney.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ""));
                        if (ActivityCompat.checkSelfPermission(Da.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                        }
                    });  recharge.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ""));
                            startActivity(intent);
                        }
                    });*/

                    dialog.show();

                }
                else {
                   // Toast.makeText(this, "show popup", Toast.LENGTH_SHORT).show();


                }
            }catch (Exception e){


            }

       }

    private void checkNumberList() {
        if (getNumberList()!=null && !getNumberList().isEmpty()   ){

        }else {
            UtilMethods.INSTANCE.GetNumberList(Dashboard3.this, null);
        }
    }
public  String getNumberList(){
    SharedPreferences prefs =  getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,  MODE_PRIVATE);

    return     prefs.getString(ApplicationConstant.INSTANCE.numberListPref, null);
}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
    protected void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
//    public void setLocale(String localeName) {
//        if (!localeName.equals(currentLanguage)) {
//            myLocale = new Locale(localeName);
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(this, Dashboard3.class);
//            refresh.putExtra(currentLang, localeName);
//            startActivity(refresh);
//        } else {
//            Toast.makeText(Dashboard3.this, "Language already selected!", Toast.LENGTH_SHORT).show();
//        }
//    }

    public static void ReplaceFragment(String fragment){
        if (fragment.equalsIgnoreCase("Fund_Transfer")){
            countBackstack = countBackstack + 1;
           // fragmentManagerHelper.StartFragment(R.id.fragment_container, new RecentRecharge(), "Fund Transfer");
        }if (fragment.equalsIgnoreCase("Transaction")){
            countBackstack = countBackstack + 1;
            //  fragmentManagerHelper.StartFragment(R.id.fragment_container, new LedgerFragment(), "Transaction History");
        }
        if (fragment.equalsIgnoreCase("Fund_Trans")){
            countBackstack = countBackstack + 1;
            //  fragmentManagerHelper.StartFragment(R.id.fragment_container, new FundTransFragment(), "Fund_Trans");
        }if (fragment.equalsIgnoreCase("Recharge_Rep")){
            countBackstack = countBackstack + 1;
            //  fragmentManagerHelper.StartFragment(R.id.fragment_container, new RechargeHistoryFragment(), "Recharge History");
        }if (fragment.equalsIgnoreCase("Dispute_Rep")){
            countBackstack = countBackstack + 1;
            //   fragmentManagerHelper.StartFragment(R.id.fragment_container, new DisputeHistoryFragment(), "Dispute Report");
        }if (fragment.equalsIgnoreCase("Payment")){
            countBackstack = countBackstack + 1;
            // fragmentManagerHelper.StartFragment(R.id.fragment_container, new PaymentRequestFragment(), "Payment Request");
        }
        if (fragment.equalsIgnoreCase("Fund_Rec")){
            countBackstack = countBackstack + 1;
            // fragmentManagerHelper.StartFragment(R.id.fragment_container, new FundRecFragment(), "Fund_Rec");
        }
        if (fragment.equalsIgnoreCase("Commision_report")){
            countBackstack = countBackstack + 1;
            // fragmentManagerHelper.StartFragment(R.id.fragment_container, new CommissionFragment(), "Commision_report");
        }
    }
    private void getVersionInfo() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            Log.e("vers",versionName);
         //   UtilMethods.INSTANCE.UserSaleReportNew(Dashboard3.this,  mProgressDialog);
            UtilMethods.INSTANCE.BankDetail(Dashboard3.this, null);
            UtilMethods.INSTANCE.BalanceCheck(this, null);
            //uncomment here
           //UtilMethods.INSTANCE.VideoGallery(this, null);
            UtilMethods.INSTANCE.Popup(this, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        try{

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//            System.exit(0);

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count!=1) {
                fm=    getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentTransaction transaction = fm.beginTransaction();
               int count1 = fm.getBackStackEntryCount();
                transaction.replace(R.id.fragment_container,new HomeFragment(),"Home");
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else  {
               /* new SweetAlertDialog(Dashboard3.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want Exit?")
                        .setCancelText("No,cancel!")
                        .setConfirmText("Yes,Exit!")

                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
//
                            }
                        })
                        .show();*/

                new AlertDialog.Builder(Dashboard3.this)
                        .setTitle("Do You want Exit?")
                        //.setMessage(message)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // "OK" button was clicked
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // "OK" button was clicked

                                dialogInterface.dismiss();

                            }
                        })
                        .show();

            }
//uncomment here
           // UtilMethods.INSTANCE.VideoGallery(this, mProgressDialog);
           UtilMethods.INSTANCE.GetNews(this);
            back_pressed = System.currentTimeMillis();

        }catch (Exception e){}

    }
    public static void ChangeFragment(FragmentManager fragmentManager, Fragment fragment) {

//
//        String backStateName =  fragment.getClass().getName();
//        String fragmentTag = backStateName;
//
//        FragmentManager manager = getSupportFragmentManager();
//        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
//
//        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment, fragmentTag);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//
//


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerpager, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return  ;
    }
    private void ShowPopup() {
   try {
       SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
       String popupresponse = myPreferences.getString(ApplicationConstant.INSTANCE.popupPref, null);
       //Log.e("response",popupresponse);

       if (popupresponse != null) {
           LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View view = inflater.inflate(R.layout.layoutpoop, null);

           final ImageView dialog_logo = (ImageView) view.findViewById(R.id.dialog_logo);
           final AppCompatTextView title = (AppCompatTextView) view.findViewById(R.id.dialog_title);
           final ImageView okButton = (ImageView) view.findViewById(R.id.okButton);
           final AppCompatTextView content = (AppCompatTextView) view.findViewById(R.id.dialog_content);

           final Dialog dialog = new Dialog(this);

           dialog.setContentView(view);

           BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(popupresponse, BalanceCheckResponse.class);
           Log.e("response", popupresponse.toString());


           ChildBalance childBalance = balanceCheckResponse.getDATA().get(0);
           String imageUrl = childBalance.getImageURl().toString();
           String title1 = childBalance.getHeadings().toString();
           String content1 = childBalance.getContent().toString();
            time = childBalance.getSetTime().toString();

//        Glide.with(getApplicationContext())
//                .load(ApplicationConstant.INSTANCE.baseUrl+imageUrl.get(position).getImageUrl().replace("~",""))
//                .into(imageView);

           Glide.with(getApplicationContext()).load((ApplicationConstant.INSTANCE.baseUrl+imageUrl)).into(dialog_logo);
           title.setText("" + title1);
           content.setText("" + content1);

           okButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });
           dialog.show();

       }
       else {
           Toast.makeText(this, "show popup", Toast.LENGTH_SHORT).show();


       }
   }catch (Exception e){


       }



//        BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(popupresponse, BalanceCheckResponse.class);
//        String prepaidWallet = balanceCheckResponse.getPrepaidWallet();
//        String utilityWallet = balanceCheckResponse.getUtilityWallet();
//
//        if (prepaidWallet != null && prepaidWallet.length() > 0) {
//            balancePrepaidLayout.setVisibility(View.VISIBLE);
//        } else {
//            balancePrepaidLayout.setVisibility(View.GONE);
//        }
//
//        if (utilityWallet != null && utilityWallet.length() > 0) {
//            balanceUtilityLayout.setVisibility(View.VISIBLE);
//        } else {
//            balanceUtilityLayout.setVisibility(View.GONE);
//        }
//
//        balancePrepaid.setText("" + prepaidWallet);
//        balanceUtility.setText("" + utilityWallet);
//
//        if (balanceCheckResponse.getIsLogin().equalsIgnoreCase("0")) {
//            UtilMethods.INSTANCE.logout(Dashboard3.this);
//        }


    }

    public void ShowPopupNow() {

        Thread timerThread = new Thread(){
            public void run(){
                try{

                        sleep(600000);



                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    ShowPopup();                }
            }
        };
        timerThread.start();
    }
    public void BalanceRefresh() {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            /*mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();*/
            //uncomment here
         //   UtilMethods.INSTANCE.VideoGallery(Dashboard3.this, null);
           UtilMethods.INSTANCE.BalanceCheck(Dashboard3.this, null);

            //UtilMethods.INSTANCE.UserSaleReportNew(Dashboard3.this, mProgressDialog);

        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message), 2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!UtilMethods.INSTANCE.getRoleId(this).equalsIgnoreCase("1")) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_menu_retailer, menu);
        }

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
            BalanceRefresh();
            return true;
        } else if (id == R.id.action_update_operator) {
            UpdateOperator();
            return true;
        }else if (id == R.id.action_downline_balance) {
            downlineBalance(Dashboard3.this);
            return true;
        }else if (id == R.id.action_support ) {
            fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.fragment_container, new SupportFragment(), "support");
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        else if (id == R.id.action_showpopup) {

             ShowPopup();
            return true;
        }else if (id == R.id.action_payment_req) {

            Intent createIntent = new Intent(getApplicationContext(), PaymentRequest.class);
            createIntent.putExtra("Type", "abc");
            startActivity(createIntent);
            return true;
        }  else if (id == R.id.action_privacy_policy) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApplicationConstant.INSTANCE.baseUrl+"/Policy.html"));
            startActivity(intent);

            return true;
        }  else if (id == R.id.action_refund_policy) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApplicationConstant.INSTANCE.baseUrl+"/Refund.html"));
            startActivity(intent);

            return true;
        }
else if (id == R.id.action_notice) {
            Intent transactionIntent = new Intent(this, NoticeBoardActivity.class);
 startActivity(transactionIntent);
            //ShowNotice();
            return true;
        }

            return true;



    }


    public void UpdateOperator() {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            UtilMethods.INSTANCE.startingOperatorService(Dashboard3.this, mProgressDialog);

        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message), 2);
        }
    }

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            if (position == 1) {
//                RecentRecharge fragment = new RecentRecharge();
//                return fragment;
//            } else if (position == 2) {
//                ReportFragment fragment = new ReportFragment();
//                return fragment;
//            } else if (position == 3) {
//                ProfileFragment fragment = new ProfileFragment();
//                return fragment;
//            } else if (position == 4) {
//                SupportFragment fragment = new SupportFragment();
//                return fragment;
//            }
//            else {
//                ServiceFragment fragment = new ServiceFragment();
//                return fragment;
//                //return Dashboard3.PlaceholderFragment.newInstance(position + 1);
//            }
//
//        }
//
//        @Override
//        public int getCount() {
////            if (!(UtilMethods.INSTANCE.getRoleId(Dashboard3.this).equalsIgnoreCase("4") ||
////                    UtilMethods.INSTANCE.getRoleId(Dashboard3.this).equalsIgnoreCase("8")||
////                    UtilMethods.INSTANCE.getRoleId(Dashboard3.this).equalsIgnoreCase("9"))) {
////                return 6;
////            } else{
//                return 5;
//           //  }
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Services";
//                case 1:
//                    return "Recent Recharges";
//                case 2:
//                    return "Report";
//                case 3:
//                    return "Profile";
//                case 4:
//                    return "Support";
//
//            }
//            return null;
//        }
//    }

    public void SetBalance() {
      try{
            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);

            BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, BalanceCheckResponse.class);

            if(balanceCheckResponse!=null){
                String prepaidWallet = balanceCheckResponse.getPrepaidWallet();
                String utilityWallet = balanceCheckResponse.getUtilityWallet();

                if (prepaidWallet != null && prepaidWallet.length() > 0) {
                    balancePrepaidLayout.setVisibility(View.VISIBLE);
                } else {
                    balancePrepaidLayout.setVisibility(View.GONE);
                }

                if (utilityWallet != null && utilityWallet.length() > 0) {
                    balanceUtilityLayout.setVisibility(View.GONE);
                } else {
                    balanceUtilityLayout.setVisibility(View.GONE);
                }

                balancePrepaid.setText("Your Wallet Amount is : \u20B9 " + prepaidWallet);

                balanceUtility.setText("" + utilityWallet);
                SharedPreferences myPrefs =  getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
                String emailPref = myPrefs.getString(ApplicationConstant.INSTANCE.supportEmail, null);
                String numberPref = myPrefs.getString(ApplicationConstant.INSTANCE.supportNumber, null);
                String icon = myPrefs.getString(ApplicationConstant.INSTANCE.icon, null);
                number.setText("CustomerCare No : " + numberPref);
                if (balanceCheckResponse.getIsLogin().equalsIgnoreCase("0")) {
                    UtilMethods.INSTANCE.logout(Dashboard3.this);
                }}
        }catch(Exception e){

         }

    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("balanceUpdate")) {
            SetBalance();
        }
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
          if (activityFragmentMessage.getFrom().equalsIgnoreCase("news")) {
            Log.e("news",activityFragmentMessage.getMessage().trim());
            news.setText("" + activityFragmentMessage.getMessage().trim());
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

    public void downlineBalance(Context context) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.downline_balance_check, null);

        final EditText downlineMobileNumber = (EditText) view.findViewById(R.id.downlineMobileNumber);
        final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
        final AppCompatButton cancelButton = (AppCompatButton) view.findViewById(R.id.cancelButton);

        final Dialog dialog = new Dialog(this);

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
                if (downlineMobileNumber.getText() != null && downlineMobileNumber.getText().length() > 0) {

                    if (UtilMethods.INSTANCE.isNetworkAvialable(Dashboard3.this)) {

                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        UtilMethods.INSTANCE.CheckBalanceDownline(Dashboard3.this, downlineMobileNumber.getText().toString().trim(), dialog, "", mProgressDialog);

                    } else {
                        UtilMethods.INSTANCE.dialogOk(Dashboard3.this, getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message), 2);
                    }
                } else {
                    downlineMobileNumber.setError("Please enter a valid mobile number !!");
                    downlineMobileNumber.requestFocus();
                }
            }
        });
        dialog.show();
    }

    public void CheckBalanceDownline(String response, Context context) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.downline_balance_check, null);

        final EditText downlineMobileNumber = (EditText) view.findViewById(R.id.downlineMobileNumber);
        final AppCompatTextView message = (AppCompatTextView) view.findViewById(R.id.message);
        final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
        final AppCompatButton cancelButton = (AppCompatButton) view.findViewById(R.id.cancelButton);

        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);

        BalanceCheckResponse balanceCheckResponse = new Gson().fromJson(response, BalanceCheckResponse.class);
        ChildBalance childBalance = balanceCheckResponse.getChildBalance().get(0);
        String msg = "Name : " + childBalance.getName() + "\n" + "Prepaid Balance : " + childBalance.getPrepaidWallet() + "\n" +
                "Utility Balance : " + childBalance.getUtilityWallet();
        cancelButton.setVisibility(View.GONE);
        downlineMobileNumber.setVisibility(View.GONE);
        message.setText("" + msg);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}