package com.mrmulti.SplashScreen.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mrmulti.Dashboard.ui.Dashboard3;
import com.mrmulti.GooglePlayStoreAppVersionNameLoader;
import com.mrmulti.Login.ui.LoginScreen;
import com.mrmulti.Notification.app.Config;
import com.mrmulti.Notification.util.NotificationUtils;
import com.mrmulti.R;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.UtilMethods;


/**
 * Created by Lalit on 08-03-2017.
 */

public class SplashScreen extends AppCompatActivity {

    private ProgressDialog mProgressDialog = null;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION_STATE = 1;
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,    Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int READ_EXTRENAL_MEDIA_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        /*if (!TextUtils.isEmpty(regId))
            Toast.makeText(getApplicationContext(), "Reg.Id: " + regId, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new GooglePlayStoreAppVersionNameLoader().execute();
        UtilMethods.INSTANCE.setRegKey(getApplicationContext(), "7d7d8f5f-7412-49dc-bc55-aeb56ee7713c");

        //////////////////////////////////////////
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };
        //////////////////////////////////////////

        mProgressDialog = new ProgressDialog(this);
        ReadPhoneStatePermission();
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                return;
            }
            else
            {
                HitApi();
            }
        }
        else
        {
            Log.e("check","a");
            HitApi();
        }*/
    }

    public void HitApi()
    {
        SharedPreferences myPrefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
        String UMobile = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);
        String apicheck = myPrefs.getString(ApplicationConstant.INSTANCE.prefapi, null);

        if (UMobile != null && UMobile.length() > 0)
        {
            Log.e("check","1");
            UtilMethods.INSTANCE.getDeviceId(this);
            dashboardpage();
        }

        else if (apicheck != null && apicheck.equals("1")&& UMobile.equals("") )
        {
            Log.e("check","2");
            loginpage();
        }
        else
        {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this))
            {

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                UtilMethods.INSTANCE.startingOperatorService(SplashScreen.this, mProgressDialog);
            }
            else
            {
                UtilMethods.INSTANCE.dialogOk(SplashScreen.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message), 4);
            }
        }
    }

    public void startDashboard() {
        Intent intent = new Intent(SplashScreen.this, Dashboard3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void startLogin() {
        Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    public void dashboardpage() {

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    startDashboard();
                }
            }
        };
        timerThread.start();
    }

    public void loginpage() {

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    startLogin();
                }
            }
        };
        timerThread.start();
    }
    public void ReadPhoneStatePermission() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(SplashScreen.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 6 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                HitApi();
                     /* if (LoginDataResponse != null && LoginDataResponse.getData() != null && LoginDataResponse.getData().getSessionID() != null && LoginDataResponse.getData().getSessionID().length() > 0) {
                        dashboardpage();
                    } else {

                        loginpage();

                    }
                } else {
                    if (UtilMethods.INSTANCE.isNetworkAvialable(Splash.this)) {

                        UtilMethods.INSTANCE.NumberList(Splash.this, null, 0);
                    } else {
                        UtilMethods.INSTANCE.NetworkError(Splash.this, getResources().getString(R.string.err_msg_network_title),
                                getResources().getString(R.string.err_msg_network));
                    }
                }
            }*/
            }  } else if (requestCode == READ_EXTRENAL_MEDIA_PERMISSIONS_REQUEST) {
                if (grantResults.length == 6 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SplashScreen.this, "EXTRENAL_MEDIA Contacts permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplashScreen.this, "EXTRENAL_MEDIA Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    HitApi();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }*/
    }