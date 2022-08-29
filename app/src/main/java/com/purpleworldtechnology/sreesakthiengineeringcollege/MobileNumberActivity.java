package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MobileNumberActivity extends AppCompatActivity implements TaskDelegate,TaskStaticJSON {
    private Button sendmeotp;
    EditText number_tv;
    String mobile_number;
    JSONObject data;
    String student_uuid="";

    private ImageView webnaviimg;



    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private String mVerificationId;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        webnaviimg=findViewById(R.id.webnaviimg);

        firebaseAuth = FirebaseAuth.getInstance();

        pref = this.getSharedPreferences("MyApp", 0); // 0 - for private mode
        editor = pref.edit();

        sendmeotp=findViewById(R.id.sendmeotp);
        number_tv = (EditText) findViewById(R.id.number);

//        isStoragePermissionGranted();



        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        webnaviimg.startAnimation(animation); //to start animation
        webnaviimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MobileNumberActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });



       // sendVerificationCode("8637643225");

        sendmeotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_number = number_tv.getText().toString().trim();

                if (mobile_number.isEmpty()||mobile_number.length()<10||mobile_number.length()>10)
                {
                    number_tv.setError("Enter a Valid Number");
                    number_tv.requestFocus();
                    return;
                }
                if (isOnline())
                {
                    //Remote call
                    String urlPara = "send_otp?api_key="+MyApplication.api_key+"&mobile="+ mobile_number;
                    new AsyncTaskOTPSync((TaskDelegate) MobileNumberActivity.this,"SendOtp").execute(urlPara);
                    sendmeotp.setText("OTP sending .Please wait ...");
                    sendmeotp.setClickable(false);
                  }
                else {
                    Toast.makeText(MobileNumberActivity.this, "You are offline", Toast.LENGTH_LONG).show();
                  }
               
            }
        });


    }  // onCreate method end
    public void onBackPressed() {
        moveTaskToBack(true);
    }

//    /*********************  Permission  ***************************/
//
//    //https://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
//    public  boolean isStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (MobileNumberActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("ManiTag","Permission is granted");
//                return true;
//            } else {
//
//                Log.v("ManiTag","Permission is revoked");
//                ActivityCompat.requestPermissions(MobileNumberActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("ManiTag","Permission is granted");
//            return true;
//        }
//    }





    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) MobileNumberActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            MyApplication.isOnline = true;
            return true;
        }
        else {
            MyApplication.isOnline = false;
            return false;
        }
    }

    @Override
    public void taskResult(String completedAction, String json, String call) {
        taskResultAuto(completedAction,json,call);
    }

    private void taskResultAuto(String completedAction, String json, String call) {

        if (completedAction == "SendOtp")
        {

            sendmeotp.setClickable(true);
            sendmeotp.setText("Send OTP");
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {

                            // call firebase otp start

                            // end

                            student_uuid =  data.getString("student_uuid");
                            // mobile_number
                            // Toast.makeText(MobileNumberActivity.this,"OTP sent to your registered email-id",Toast.LENGTH_LONG).show();
                            Intent ii = new Intent(MobileNumberActivity.this,OTPVerificationActivity.class);
                            ii.putExtra("student_uuid",student_uuid);
                            ii.putExtra("mobile",mobile_number);
                            startActivity(ii);

                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(MobileNumberActivity.this,msg_1,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    } // taskResult end

    @Override
    public void taskStaticJSONResult(String completedAction, String json, String type) {

    }


}