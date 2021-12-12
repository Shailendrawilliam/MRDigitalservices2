package com.mrmulti.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mrmulti.R;
import com.mrmulti.Util.EnglishNumberToWords;
import com.mrmulti.Util.FragmentActivityMessage;
import com.mrmulti.Util.GlobalBus;
import com.mrmulti.Util.UtilMethods;
import com.mrmulti.Util.ui.BankDetailList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PaymentRequest  extends AppCompatActivity implements View.OnClickListener{

    String fundType="1";

    String base64_1="";
    String image1="",setPic="1";

    private static final int SELECT_PICTURE = 1;

    public Uri fileUri;

    public static Bitmap bitmap;

    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public ImageView cancel;


    String uploadImagePicturePath="";
    Uri uploadImageUri;
       TextView uploadImage;

    int RESULT_LOAD_IMAGE=1;




    EditText txtTransactionID, bankFund, number, bankRole,AmountInWords;
    Spinner paymentmode;
    EditText txttranferAmount,remark;
    String walletType;
    Button btnPaymentSubmit;
    Toolbar toolbar;
    ArrayAdapter aa;
    String RequestedTo = "",SelectedpaymentMode;
    RadioButton prepaid, utility;
    ProgressDialog loader= null;

    String[] bankNames={"--Select Payment Mode--","Cash deposit","Third Party Transfer","NEFT","IMPS","RTGS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_request);
        GetId();
    }


    private void GetId() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Request");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new ProgressDialog( PaymentRequest.this );
        txtTransactionID = (EditText) findViewById(R.id.txtTransactionID);
        txttranferAmount = (EditText) findViewById(R.id.txttranferAmount);
        remark = (EditText) findViewById(R.id.remark);
        number = (EditText) findViewById(R.id.number);


        bankFund = (EditText) findViewById(R.id.bankFund);
        bankRole = (EditText) findViewById(R.id.bankRole);
        paymentmode = (Spinner) findViewById(R.id.paymentmode);
        AmountInWords = (EditText) findViewById(R.id.AmountInWords);
        uploadImage=(TextView) findViewById(R.id.uploadImage);
        prepaid = (RadioButton) findViewById(R.id.prepaid);
        utility = (RadioButton) findViewById(R.id.utility);
        txttranferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null){
                    try{
                        Long Amount= Long.parseLong(s.toString());

                        String return_val_in_english =   EnglishNumberToWords.convert(Amount);
                        AmountInWords.setText(return_val_in_english);
                    }catch (Exception e){

                    }

                }


            }
        });

        btnPaymentSubmit = (Button) findViewById(R.id.btnPaymentSubmit);
        prepaid.setChecked(true);
        walletType = "1";
        SetListener();
        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        paymentmode.setAdapter(aa);
        paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id){
                SelectedpaymentMode=  arg0.getItemAtPosition(position).toString();

//                if (SelectedpaymentMode.equalsIgnoreCase("--Select Payment Mode--")){
//                    ((TextView)paymentmode.getSelectedView()).setError("Please Select Payment Mode");
//                }
                if (SelectedpaymentMode.equalsIgnoreCase("Cash deposit")){

                    txtTransactionID.setText(getRandomString(20));
                }else {
                    txtTransactionID.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                ((TextView)paymentmode.getSelectedView()).setError("Please Select Payment Mode");
            }
        });
    }

    private void SetListener() {

        bankRole.setOnClickListener((View.OnClickListener) this);
        uploadImage.setOnClickListener(this);
        bankFund.setOnClickListener((View.OnClickListener)this);
        prepaid.setOnClickListener((View.OnClickListener)this);
        utility.setOnClickListener((View.OnClickListener)this);
        btnPaymentSubmit.setOnClickListener((View.OnClickListener)this);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), bankNames[position], Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//// TODO Auto-generated method stub
//
//    }

    @Override
    public void onClick(View v) {
        if (v == uploadImage){
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("application/msword,application/pdf/jpg/png/jpeg");
            startActivityForResult(intent,RESULT_LOAD_IMAGE);

//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

        }
        if (v==bankRole){
            Intent bankRoleIntent = new Intent(PaymentRequest.this, BankDetailList.class);
            bankRoleIntent.putExtra("from", "role");
            startActivity(bankRoleIntent);
        }
        if (v==paymentmode){
            Intent bankRoleIntent = new Intent(PaymentRequest.this, BankDetailList.class);
            bankRoleIntent.putExtra("from", "role");
            startActivity(bankRoleIntent);
        }

        if (v==bankFund){
            Intent bankIntent = new Intent(PaymentRequest.this, BankDetailList.class);
            bankIntent.putExtra("from", "bank");
            startActivity(bankIntent);
        }
        if (v==prepaid){
            prepaid.setChecked(true);
            walletType = "1";
            utility.setChecked(false);
        }
        if (v==utility){
            prepaid.setChecked(false);
            walletType = "2";
            utility.setChecked(true);
        }
        if (v==btnPaymentSubmit){
            if (validationForm("")==0){


//            btnPaymentSubmit.setEnabled(false);
//            btnPaymentSubmit.setBackgroundColor(getResources().getColor(R.color.back_bg));
                //File file = new File(uploadImagePicturePath);

//                if (!file.isFile()){
//                    loader.dismiss();
//
//                    Toast.makeText(PaymentRequest.this,"Upload Image",Toast.LENGTH_SHORT).show();
//
//                }else {
                   // final ProgressDialog loader = new ProgressDialog(this );
//                    loader.setMessage("Preparing image...");
//                    loader.show();
                    btnPaymentSubmit.setEnabled(false);
                    btnPaymentSubmit.setBackgroundColor(getResources().getColor(R.color.back_bg));
                    if (UtilMethods.INSTANCE.isNetworkAvialable(PaymentRequest.this)) {


//                         UtilMethods.INSTANCE.afterLogintoPreviousWindowFund(PaymentRequest.this, "",null, number.getText().toString().trim().toString(),
//                                 txttranferAmount.getText().toString().trim(), "" + number.getText().toString().trim(), txtTransactionID.getText().toString().trim(), walletType,remark.getText().toString(),bankFund.getText().toString(),"","" ,null,null);
                        new Uploadtask().execute("Execute" );


                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                btnPaymentSubmit.setEnabled(true);
                                btnPaymentSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                        },7000);
                    } else {
                        UtilMethods.INSTANCE.dialogOk(this, getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message), 2);
                    }


               // }
            }

        }
    }
    private class Uploadtask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
loader.show();
loader.setMessage("Loading.. ");
loader.setIndeterminate(true);
loader.setCanceledOnTouchOutside(false);

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String myString = params[0];
            //loader.setProgressPercent(progress[0]);

            // Do something that takes a long time, for example:
//            for (int i = 0; i <= 100; i++) {
//
//                // Do things
//               // loader.setProgress(i);
//                // Call this to update your progress
//                 //publishProgress(i);
//            }
            UtilMethods.INSTANCE.PaymentRequest(PaymentRequest.this, txttranferAmount.getText().toString(), number.getText().toString(), txtTransactionID.getText().toString(), fundType,remark.getText().toString(),bankFund.getText().toString(),AmountInWords.getText().toString(),"",loader);

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //loader.dismiss();
            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }


    private int validationForm(String s) {
        int flag = 0;
        if(number.getText() !=null&&number.getText().toString().trim().length()>0){
        }else {
            number.setError("Please enter valid number!!");
            number.requestFocus();
            flag++;
        }
        if(txttranferAmount.getText() !=null&&txttranferAmount.getText().toString().trim().length()>0){
        }else {
            txttranferAmount.setError("Please enter valid amount!!");
            txttranferAmount.requestFocus();
            flag++;
        }
//        if(remark.getText() !=null&&remark.getText().toString().trim().length()>0){
//        }else {
//            remark.setError("Please enter Remark!!");
//            remark.requestFocus();
//            flag++;
//        }



        if(txtTransactionID.getText() !=null&&txtTransactionID.getText().toString().trim().length()>0){
        }else {
            txtTransactionID.setError("Please enter valid Txn Id!!");
            txtTransactionID.requestFocus();
            flag++;
        }
        return flag;
    }
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getFrom().equalsIgnoreCase("bankSelected")) {

            String[] detail = activityFragmentMessage.getMessage().split(",");
            bankFund.setText("" + detail[0]);
            number.setText("" + detail[1]);
        }else if (activityFragmentMessage.getFrom().equalsIgnoreCase("bankSelectedRole")) {

            String[] detail = activityFragmentMessage.getMessage().split(",");
            bankRole.setText("" + detail[0]);
            RequestedTo = detail[1];
        }else if (activityFragmentMessage.getFrom().equalsIgnoreCase("refreshvalue")) {
            bankRole.setText("");
            bankFund.setText("");
            number.setText("");
            txtTransactionID.setText("");
            txttranferAmount.setText("");
            remark.setText("");
            number.setText("");
            bankFund.setText("");
            paymentmode.setSelection(0);

        }
    }
//    public void usingSimpleImage(ImageView imageView) {
//        ImageAttacher mAttacher = new ImageAttacher(imageView);
//        ImageAttacher.MAX_ZOOM = 4.0f; // Double the current Size
//        ImageAttacher.MIN_ZOOM = 1.0f; // Half the current Size
//        MatrixChangeListener mMaListener = new MatrixChangeListener();
//        mAttacher.setOnMatrixChangeListener(mMaListener);
//        PhotoTapListener mPhotoTap = new PhotoTapListener();
//        mAttacher.setOnPhotoTapListener(mPhotoTap);
//    }
//
//    private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {
//
//        @Override
//        public void onPhotoTap(View view, float x, float y) {
//        }
//    }
//
//    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {
//
//        @Override
//        public void onMatrixChanged(RectF rect) {
//
//        }
//    }

    //===========================================End Pinch Zoom ==================================================

    //===========================================For Camera and Gallery ==================================================
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /* Creating file uri to store image/video*/
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {


        Matrix matrix = new Matrix();
        if (angle != 0f) {
            matrix.preRotate(angle);
        }
        //matrix.postRotate(angle);
        //retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        Bitmap adjustedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return adjustedBitmap;
    }

    public void setPictures(Bitmap b,String setPic,String base64) {
        if(setPic.equals("1"))
        {

            base64_1=base64;
            Log.e("base",base64_1);
            image1="0";
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            uploadImageUri= selectedImage;
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            uploadImagePicturePath= cursor.getString(columnIndex);
            Log.e("Picture Path :",""+ uploadImagePicturePath);
            cursor.close();
            uploadImage.setBackgroundColor(getResources().getColor(R.color.white));
            uploadImage.setText(""+uploadImagePicturePath);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }
}
