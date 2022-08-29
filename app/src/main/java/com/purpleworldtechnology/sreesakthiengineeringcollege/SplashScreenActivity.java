package com.purpleworldtechnology.sreesakthiengineeringcollege;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class SplashScreenActivity  extends AppCompatActivity implements TaskDelegate,TaskStaticJSON {
    private boolean isFirstAnimation = false;


    public TextView ClgName,clgSubTitle;
    public ImageView icon;

    String path;
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    private ProgressDialog pd = null;
    String updated_at,banner_name;
    JSONObject data;
    JSONArray bannerDetails;
    int sub_header_visiblity;
    Bitmap decodedByte;
    Context context;
    Database db;

    String banner_data;
    ArrayList<String> banner_name_list  = new ArrayList<String>();
    ArrayList<String> sub_header_list   = new ArrayList<String>();
    ArrayList<Banners> bannerData = null ;

//    Banners bannerData  ;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_splash_screen);


        db = new Database(SplashScreenActivity.this);
        DatabaseManager.initializeInstance(new Database(SplashScreenActivity.this));

        db = new Database(this.getApplicationContext());
        db.checkDataBase();



        // get banner data from database  end

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        // path = Environment.getExternalStorageDirectory() + File.separator + "SSEC";

             //   path = getExternalFilesDir("/")+ "SSEC" + File.separator;
       // path = Environment.getExternalStorageDirectory() + File.separator + "SSEC" + File.separator;



//        if(isStoragePermissionGranted())
//        {
//            bannerData =  db.getBanner();
//            if (bannerData.size() > 0) {
//                Iterator itr=bannerData.iterator();
//                while(itr.hasNext()) {
//                    Banners st = (Banners) itr.next();
//                     banner_name = st.banner_name;
//                     banner_data = st.banner_data;
//                    banner_name = banner_name.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\s+","");
////                    File f = new File(path+banner_name);
//
//                    File f = new File(path);
//                    if(!f.exists()) {
//                        f.mkdirs();
//                    }
//                    try {
//
//                        File f_1 = new File(path, banner_name);
//                        if (!f_1.exists()) {
//                            f_1.createNewFile();
//
//                        }
//                        try {
//                            FileOutputStream out = new FileOutputStream(f_1);
//                            byte[] decodedString = Base64.decode(banner_data, Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                            out.flush();
//                            out.close();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

//        }
//        else
//        {
//            Toast.makeText(SplashScreenActivity.this,"Please allow permission to access this application",Toast.LENGTH_LONG).show();
//            return;
//        }
        isStoragePermissionGranted();

        icon = findViewById(R.id.icon);

        ClgName=findViewById(R.id.ClgName);
        clgSubTitle=findViewById(R.id.clgSubTitle);
         /*Simple hold animation to hold ImageView in the centre of the screen at a slightly larger
        scale than the ImageView's original one.*/
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);



        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);


        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    icon.clearAnimation();

                    ClgName.setVisibility(View.VISIBLE);
                    RunAnimation();
//                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                icon.clearAnimation();
                icon.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        icon.startAnimation(hold);
    }  // onCreate method end

    /*********************  Permission  ***************************/

    //https://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (SplashScreenActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("ManiTag","Permission is granted");
                return true;
            } else {

                Log.v("ManiTag","Permission is revoked");
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("ManiTag","Permission is granted");
            return true;
        }
    }


    private void RunAnimation()
    {

        Animation a = AnimationUtils.loadAnimation(this, R.anim.scaling);
        a.reset();
//        KingsDurbarText.setVisibility(View.VISIBLE);
//        KingsDurbarSubText.setVisibility(View.VISIBLE);
        ClgName.clearAnimation();
        ClgName.startAnimation(a);
//        nextActivityNavigation();
        ClgName.setVisibility(View.VISIBLE);
        clgSubTitle.setVisibility(View.VISIBLE);
        nextActivityNavigation();
    }

//  android:background="#7F000000"

    private void nextActivityNavigation() {

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3500);

                    isStoragePermissionGranted();
                    // Show the ProgressDialog on this thread


                    // After 5 seconds redirect to another intent
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    updated_at = dateFormat.format(cal.getTime());
                    System.out.println(dateFormat.format(cal.getTime()));

//                    if(isOnline())
//                    {
////                        String urlPara = "get_banner?api_key="+MyApplication.api_key+"&updated_at="+ URLEncoder.encode(updated_at);
////                        String urlPara = "get_banner?api_key="+MyApplication.api_key;
//                        new AsyncTaskOTPSync((TaskDelegate) SplashScreenActivity.this,"GetBanner").execute(urlPara);
//                    }
//                    else{
                        if(student_pref.contains("student_uuid") ){
                            Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, MobileNumberActivity.class);
                            startActivity(intent);
                            finish();
                        }

//                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // start thread
        background.start();
    }

    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) SplashScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "GetBanner")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            bannerDetails = data.getJSONArray("banner_data");
//                            JSONObject data1 = new JSONObject(data.getString("banner_data"));

                            SharedPreferences.Editor editor = student_pref.edit();
                            if(student_pref.contains(("banner_name_list")))
                            {
                                editor.remove("banner_name_list");
                                editor.remove("sub_header_list");
                                editor.commit();

                            }


                            File f = new File(path);
                            if(!f.exists()) {
                                f.mkdirs();
                            }

                            for (int i = 0; i < bannerDetails.length(); i++) {

                                JSONObject jsonobject = bannerDetails.getJSONObject(i);
                                sub_header_visiblity = jsonobject.getInt("sub_header_visiblity");
                                String banner_name = jsonobject.getString("banner_name");
                                banner_data = jsonobject.getString("banner_data");
                                String sub_header_text = jsonobject.getString("sub_header_text");

                                banner_name_list.add(banner_name);
                                sub_header_list.add(sub_header_text);

                                // test slide image
                                try {
                                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                                    File f_1 = new File(path, banner_name);
                                    if (!f_1.exists()) {
//                                        f_1.mkdirs();
                                        f_1.createNewFile();

                                    }
                                    try {
                                        FileOutputStream out = new FileOutputStream(f_1);
                                        byte[] decodedString = Base64.decode(banner_data, Base64.DEFAULT);
                                        // decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                        out.flush();
                                        out.close();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            editor = student_pref.edit();
                            editor.putString("banner_name_list", banner_name_list.toString());
                            editor.putString("sub_header_list", sub_header_list.toString());
                            editor.commit();
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(SplashScreenActivity.this,msg_1,Toast.LENGTH_LONG).show();
                        }

//                        if(pd.isShowing())
//                        {
//                            pd.dismiss();
//                        }
                        if(student_pref.contains("student_uuid") ){
                            Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, MobileNumberActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    } // taskResult end

    private void storeImage(Bitmap image,String file_name) {
        try {
            String mImageName=file_name;
            FileOutputStream fos = openFileOutput(mImageName, MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void taskStaticJSONResult(String completedAction, String json, String type) {

    }
}


