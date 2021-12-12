package com.mrmulti.Dashboard.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrmulti.R;
import com.mrmulti.Util.ApplicationConstant;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OutletRegistrationActivity extends AppCompatActivity   implements LocationListener{
    Button submit;
    EditText otp;
    String latlong ;
    TextView FName, LName, DOB, sendermobile, Pincode, Address, Area, Pan, Aadhar, Panlink, Aadharlink, OTP, email;
    Button Submit, getotp;
    private Toolbar toolbar; Location locationNet;
    //CustomLoader loader;
    static final int TWO_MINUTES = 1000 * 60 * 2;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressDialog mProgressDialog = null;
    private Location currentBestLocation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_registration);
        // loader = new CustomLoader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mProgressDialog = new ProgressDialog(this);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Outlet Registration");
        setSupportActionBar(toolbar);
        locationListener = new MyLocationListener();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FName=(TextView)findViewById(R.id.FName);
        LName=(TextView)findViewById(R.id.LName);
        email=(EditText)findViewById(R.id.email);
        DOB=(TextView)findViewById(R.id.DOB);
        sendermobile=(TextView)findViewById(R.id.sendermobile);
        Pincode=(TextView)findViewById(R.id.Pincode);
        Address=(TextView)findViewById(R.id.Address);
        Area=(TextView)findViewById(R.id.Area);
        Pan=(TextView)findViewById(R.id.Pan);
        Aadhar=(TextView)findViewById(R.id.Aadhar);
        Panlink=(TextView)findViewById(R.id.Panlink);
        Aadharlink=(TextView)findViewById(R.id.Aadharlink);
        OTP=(TextView)findViewById(R.id.OTP);
        Submit=(Button)findViewById(R.id.Submit);
        getotp=(Button)findViewById(R.id.getotp);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latlong();
                if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {
                    mProgressDialog.setTitle("Loading...");
                    mProgressDialog.show();

                    UtilMethods.INSTANCE.OutletRegistration(OutletRegistrationActivity.this,
                            LName.getText().toString(),
                            email.getText().toString(),
                            sendermobile.getText().toString(),
                            Pincode.getText().toString(),
                            Address.getText().toString(),
                            Pan.getText().toString(),
                            OTP.getText().toString(),
                            latlong,
                            mProgressDialog);
                    // Area.getText().toString(),
                } else {
//                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.err_msg_network_title),
//                        getResources().getString(R.string.err_msg_network));
                }


            }
        });
        //getotp.setOnClickListener(this);
        SharedPreferences myPrefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
        String UMobile = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

        sendermobile.setText(UMobile);
        sendermobile.setEnabled(false);


        Getotp();

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
                DOB.setText(sdf.format(myCalendar.getTime()));
            }

        };


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(OutletRegistrationActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void Getotp() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

           mProgressDialog.setTitle("Loading...");
           mProgressDialog.show();
            SharedPreferences myPrefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNameLoginPref, MODE_PRIVATE);
            String mobileLogin = myPrefs.getString(ApplicationConstant.INSTANCE.UMobile, null);

           UtilMethods.INSTANCE.OutletRegistartionreOTP(OutletRegistrationActivity.this,mobileLogin,mProgressDialog);



        } else {
            UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.err_msg_network),
                    getResources().getString(R.string.err_msg_network), 2);
        }

    }

    private void latlong (){
try{ if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.

    ActivityCompat.requestPermissions(OutletRegistrationActivity.this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            1);
    locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    String lat= String.valueOf(locationNet.getLatitude());
    String longi= String.valueOf(locationNet.getLongitude());
    latlong=lat+","+longi;
    Log.e("loc",latlong);
}
else{
    locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    String lat= String.valueOf(locationNet.getLatitude());
    String longi= String.valueOf(locationNet.getLongitude());
    latlong=lat+","+longi;
    Log.e("loc",latlong);
}
}catch (Exception e){e.printStackTrace();}

         //   buildAlertMessageNoGps();



//
// for disable GPS
//            Intent intent1 = new Intent("android.location.GPS_ENABLED_CHANGE");
//            intent.putExtra("enabled", false);
//            sendBroadcast(intent1);
               }
  //  }

    //    @Override
//    public void onClick(View v) {
//
//
//     /*   if (v ==getotp)
//        {
//
//
//            Submit.setVisibility(View.VISIBLE);
//
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.otp, null);
//
//
//            final Dialog dialog = new Dialog(OutletRegistrationActivity.this);
//            dialog.setCancelable(true);
//            dialog.setContentView(view);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.show();
//            dialog.getWindow();
//            otp=(EditText)view.findViewById(R.id.otp);
//            submit=(Button)view.findViewById(R.id.submitotp);
//          Button cancelButton=(Button)view.findViewById(R.id.cancelButton);
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    loader.show();
//                    loader.setCancelable(false);
//                    loader.setCanceledOnTouchOutside(false);
//
//
//
//                  //  UtilMethods.INSTANCE.OutletRegistartionreOTP(OutletRegistrationActivity.this,otp.getText().toString(),loader);
//
//                    dialog.dismiss();
//                }
//            });
//
//
//
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    dialog.dismiss();
//                }
//            });
//
//
//
//
//        }*/
//
//        if (v ==Submit)
//        {
//
//
//
//        }
//
//    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

                    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.e("loc",locationNet+"   <- Net  GPS->  "+locationGPS);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(OutletRegistrationActivity.this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
//        editLocation.setText("");
//        pb.setVisibility(View.INVISIBLE);
        //Toast.makeText(getApplicationContext(), "Location changed: Lat: " + loc.getLatitude() + " Lng: "+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
       // Log.e(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
       // Log.e(TAG, latitude);
        latlong=null;
        latlong=loc.getLatitude()+","+loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getFrom().equalsIgnoreCase("outletRegistered")) {
           String outletregisteredresponse=activityFragmentMessage.getMessage();
           if (outletregisteredresponse!=null){
               LayoutInflater inflater = (LayoutInflater) OutletRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View view = inflater.inflate(R.layout.layoutpoop, null);
               ImageView imageView =(ImageView) view.findViewById(R.id.dialog_logo);
               TextView tc=(TextView)view.findViewById(R.id.dialog_content);
               final AppCompatButton okButton = (AppCompatButton) view.findViewById(R.id.okButton);
               TextView tt=(TextView)view.findViewById(R.id.dialog_title);
               final Dialog dialog = new Dialog(OutletRegistrationActivity.this);

               dialog.setCancelable(false);
               dialog.setContentView(view);
               imageView.setVisibility(View.GONE);
               tt.setVisibility(View.GONE);
               tc.setText(""+outletregisteredresponse);
               okButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                     finish();
                   }
               });
               dialog.show();
           }
 else {}       }
    }
        @Override
        public void onStart() {
            super.onStart();
            if (!EventBus.getDefault().isRegistered(this)) {
                GlobalBus.getBus().register(this);
            }
        }
    }
