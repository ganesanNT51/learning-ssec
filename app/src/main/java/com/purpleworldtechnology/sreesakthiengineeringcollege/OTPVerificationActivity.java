package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity implements TaskDelegate,TaskStaticJSON {
    private Button RegisterNow;
    String mobile,student_uuid,reg_no;
    EditText otp;
    int otp_number,department_id,student_id;
    JSONObject data;
    String student_name,father_name,department,roll_no;

    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    private TextView mobile_number_tv;
    private FirebaseAuth firebaseAuth;
    private String mVerificationId;
    // We have sent an OTP on Your number
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        firebaseAuth = FirebaseAuth.getInstance();



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        student_uuid  = bundle.getString("student_uuid");
        mobile        = bundle.getString("mobile");

        RegisterNow      = findViewById(R.id.RegisterNow);
        otp              = (EditText) findViewById(R.id.otp);
        mobile_number_tv = (TextView)findViewById(R.id.lnumberShow);
        if(!mobile.isEmpty() && mobile.length() >0)
        {
            mobile_number_tv.setText("We have sent an OTP on Your number " + mobile);
        }

        sendVerificationCode(mobile);


        FirebaseApp.initializeApp(OTPVerificationActivity.this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();

        firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance());
//        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());



        RegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_number = Integer.valueOf(otp.getText().toString().trim()) ;
                int min_number = 999;
//                if (otp_number  >= min_number)
//                {
//                    Toast.makeText(OTPVerificationActivity.this,"Enter 4 digit OTP number",Toast.LENGTH_LONG).show();
//                    return;
//                }
//                else
//                {
                    if (isOnline())
                    {
                        // firebase code start

                            String code = otp.getText().toString().trim();
                            if (code.isEmpty() || code.length() < 6) {
                                otp.setError("Enter valid code");
                                otp.requestFocus();
                                return;
                            }

                            //verifying the code entered manually


                            verifyVerificationCode(code);

                        // firebase code end


//                        String urlPara = "verify_otp?api_key="+MyApplication.api_key+"&mobile="
//                                + mobile+"&student_uuid="+student_uuid+"&otp="+otp_number;
//                        new AsyncTaskOTPSync((TaskDelegate) OTPVerificationActivity.this,"VerifyOtp").execute(urlPara);
                    }
                    else {
                        Toast.makeText(OTPVerificationActivity.this, "You are offline", Toast.LENGTH_LONG).show();
                    }
//                }


//                Intent intent = new Intent(OTPVerificationActivity.this, DashBoardActivity.class);
//                startActivity(intent);
            }
        });
    } // oncreate end


    public void getStudentData()
    {
        // get_stud_data
//        String urlPara = "verify_otp?api_key="+MyApplication.api_key+"&mobile="
//                                + mobile+"&student_uuid="+student_uuid+"&otp="+otp_number;
        String urlPara = "get_stud_data?api_key="+MyApplication.api_key+"&mobile="
                + mobile+"&student_uuid="+student_uuid;
        new AsyncTaskOTPSync((TaskDelegate) OTPVerificationActivity.this,"VerifyOtp").execute(urlPara);
    }
    // Firebase code start
    private void sendVerificationCode(String phonenumber) {

        // Turn off phone auth app verification.
//        firebase.auth().settings.appVerificationDisabledForTesting = true;
        // set this to remove reCaptcha web

//        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91" + phonenumber)       // Phone number to verify

                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        Toast.makeText(OTPVerificationActivity.this,phonenumber, Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void authenticateUser(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OTPVerificationActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            if (isOnline()) {
                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    otp.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            } else {
                Toast.makeText(OTPVerificationActivity.this, "Oops.. Internet is not connected please Establish it First", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getStudentData();  // CALL API After firebase login successs
                            Toast.makeText(OTPVerificationActivity.this, "Login successful" ,Toast.LENGTH_LONG).show();


                            //   checkroll();
                            //verification successful we will start the profile activity

                        } else {

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";
                            Toast.makeText(OTPVerificationActivity.this, "something went wrong,we will fix it soon..  ", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPVerificationActivity.this, "Invalid code entered... ", Toast.LENGTH_SHORT).show();

                                message = "Invalid code entered...";
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            }
                            //jprog.setVisibility(View.GONE);


                        }
                    }
                });
    }
    
    
    
    
    
    
    // Firebase end 
    
    
    
    
    
    
    
    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) OTPVerificationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "VerifyOtp")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            JSONObject data1 = new JSONObject(data.getString("student_data"));
                            if (data1.getInt("student_id") > 0) {

                                student_name  = data1.getString("student_name");
                                father_name   = data1.getString("father_name");
                                department    = data1.getString("department");
                                department_id = data1.getInt("department_id");

                                student_id    = data1.getInt("student_id");
                                roll_no       = data1.getString("roll_no");
//                                reg_no       = data1.getString("reg_no");


                                SharedPreferences.Editor editor = student_pref.edit();
                                editor.putString("student_id", String.valueOf(student_id));
                                editor.putString("mobile", mobile);

                                editor.putString("student_name", student_name);
                                editor.putString("department_id", String.valueOf(department_id) );

                                editor.putString("department", department);
                                editor.putString("father_name", father_name);
                                editor.putString("student_uuid", student_uuid);
                                editor.putString("roll_no", roll_no);
//                                editor.putString("reg_no", reg_no);

                                editor.commit();

                            }


//                            student_uuid =  data.getString("student_uuid");
                            // mobile_number
                            Intent ii = new Intent(OTPVerificationActivity.this,DashBoardActivity.class);
                            ii.putExtra("student_uuid",student_uuid);
                            ii.putExtra("mobile",mobile);
                            startActivity(ii);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(OTPVerificationActivity.this,msg_1,Toast.LENGTH_LONG).show();
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