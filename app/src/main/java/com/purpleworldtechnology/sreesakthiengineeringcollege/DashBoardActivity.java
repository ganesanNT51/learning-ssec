package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Base64;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

public class DashBoardActivity extends AppCompatActivity implements  BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener , ActionBar.TabListener, TaskDelegate,TaskStaticJSON {
private TextView notification;
private ImageView GatePassimg,Resultimg,profileimg,gatepasslistimg,logout,ShareFriends,Remarks,latecomers,questionbank;
private ImageView offcampusimg;
    SliderLayout sliderLayout;
    String path;

    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    int department_id = 0;
    JSONObject data ;
    ArrayList<String> banner_name_list  = new ArrayList<String>();
    ArrayList<String> sub_header_list   = new ArrayList<String>();
    String banner_name;
//    HashMap<String, File> hash_file_maps;
    String banner_data,sub_header_text,updated_at;
    JSONArray bannerDetails;
    int sub_header_visiblity;
    private ProgressDialog pd = null;
    Database db;
    HashMap<String, Integer> hash_file_maps;

    ArrayList<Banners> bannerData = null ;
    String sub_header_text_str = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        notification = findViewById(R.id.notification);
        notification.setEnabled(true);
        notification.setSelected(true);

        GatePassimg  = findViewById(R.id.ApplyGatepassimg); // Apply gatepass image
        Resultimg    = findViewById(R.id.Resultimg);
        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        profileimg = findViewById(R.id.profileimg);
        gatepasslistimg = findViewById(R.id.gatepasslistimg);
        logout = findViewById(R.id.logout);
        ShareFriends = findViewById(R.id.ShareFriends);
        Remarks      = findViewById(R.id.Remarks);
        latecomers   = findViewById(R.id.latecomers);
        questionbank = findViewById(R.id.questionbank);
        offcampusimg = findViewById(R.id.offcampusimg);



        db = new Database(DashBoardActivity.this);
        DatabaseManager.initializeInstance(new Database(DashBoardActivity.this));

        // get banner data from database
        bannerData =  db.getBanner();

      //   pd = ProgressDialog.show(DashBoardActivity.this, "", "Loading. Please Wait...", true, false);

//        hash_file_maps = new HashMap<String, File>();
       //  path = Environment.getExternalStorageDirectory() + File.separator + "SSEC" + File.separator;
        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        if(student_pref.contains("student_id")  )
        {
            // setSliderBanner();

        }
        else
        {
            Toast.makeText(DashBoardActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(DashBoardActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }


//        if (bannerData.size() > 0) {
//            Iterator itr=bannerData.iterator();
//            while(itr.hasNext()) {
//                Banners st = (Banners) itr.next();
//                String banner_name = st.banner_name;
//                String banner_data = st.banner_data;
//                sub_header_text = st.sub_header_text;
//
//                sub_header_text_str = sub_header_text_str + "  "+  sub_header_text + " || ";
//
//                banner_name = banner_name.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\s+","");
//                File f = new File(path+banner_name);
//
//                hash_file_maps.put(banner_name,f);
//            }
//
//        }

//        hash_file_maps.put(banner_name);
        try {
            hash_file_maps = new HashMap<String, Integer>();
            hash_file_maps.put("SSEC CBE",R.drawable.banner_1);
            hash_file_maps.put("SSEC KARAMADAI",R.drawable.banner_2);
            // slider image startr
            for (String name : hash_file_maps.keySet())
            {
                TextSliderView textSliderView =  new TextSliderView(DashBoardActivity.this);
                textSliderView.description(name)
                        .image(hash_file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra",name);
                sliderLayout.addSlider(textSliderView);

            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(this);
            sliderLayout.startAutoCycle();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // slider image end



//        if (bannerData.size() > 0) {
//            Iterator itr=bannerData.iterator();
//            while(itr.hasNext()) {
//                Banners st = (Banners) itr.next();
//                sub_header_text = st.sub_header_text;
//                sub_header_text_str = sub_header_text_str + "  "+  sub_header_text + " || ";
//
//
//            }
//
//        }
//        notification.setText(sub_header_text_str);
//
//        if (pd.isShowing())
//        {
//            pd.dismiss();
//        }


        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        gatepasslistimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,GatePassHistoryActivity.class);
                startActivity(intent);
            }
        });

        Remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,RemarksActivity.class);
                startActivity(intent);
            }
        });
        latecomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())
                {
                    Intent intent = new Intent(DashBoardActivity.this,LatecomersActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(DashBoardActivity.this, "You are offline.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });
        questionbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())
                {
                    // start
                    if(!isStoragePermissionGranted())
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoardActivity.this);
                        builder1.setMessage("If you want view question papers , please allow storage permission to question paper download into your device.Otherwise you can't view the question papers.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        isStoragePermissionGranted();
                                    }
                                });

                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();



                    }


                    // end

                    Intent intent = new Intent(DashBoardActivity.this,QuestionBankActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(DashBoardActivity.this, "You are offline.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });


        offcampusimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())
                {
                    Intent intent = new Intent(DashBoardActivity.this,OffCampusActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(DashBoardActivity.this, "You are offline.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });




        ShareFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SSEC");
                    String shareMessage= "\nLet me recommend you SSEC mobile application\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareMessage = shareMessage + "ssec-cbe.purpleworldtechnology.com/ssec-karamadai/core/static/android_apk/ssec_android_app.apk" +"\n\n";


                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashBoardActivity.this,"You have successfully logged out. " ,Toast.LENGTH_LONG).show();
                Intent ii = new Intent(DashBoardActivity.this, MobileNumberActivity.class);
                // shared preference three lines
                SharedPreferences.Editor student_editor = student_pref.edit();
                student_editor.clear();
                student_editor.commit();

                startActivity(ii);
            }
        });


        GatePassimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After 5 seconds redirect to another intent
                department_id = Integer.parseInt(student_pref.getString("department_id", ""));
                if (isOnline())
                {
                    pd = ProgressDialog.show(DashBoardActivity.this, "", "Loading. Please Wait...", true, false);
                    String urlPara = "reason_staff?api_key="+MyApplication.api_key+"&department_id="+department_id;
                    new AsyncTaskOTPSync((TaskDelegate) DashBoardActivity.this,"GetStaffReason").execute(urlPara);

                }
                else
                {
                    Toast.makeText(DashBoardActivity.this,"You are offline." ,Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
        Resultimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ExamMarkActivity.class);
                startActivity(intent);
            }
        });


    }   // onCreate method end


    //https://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (DashBoardActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("ManiTag","Permission is granted");
                return true;
            } else {

                Log.v("ManiTag","Permission is revoked");
                ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("ManiTag","Permission is granted");
            return true;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }


public void onBackPressed() {
    moveTaskToBack(true);
}


    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) DashBoardActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.dotnotification){

        }else if(id==R.id.dotProfile){
            Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
            startActivity(intent);

        }else if(id==R.id.dotlogout){

            Toast.makeText(DashBoardActivity.this,"You have successfully logged out. " ,Toast.LENGTH_LONG).show();
            Intent ii = new Intent(DashBoardActivity.this, MobileNumberActivity.class);
            // shared preference three lines
            SharedPreferences.Editor student_editor = student_pref.edit();
            student_editor.clear();
            student_editor.commit();

            startActivity(ii);



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void taskResult(String completedAction, String json, String call) {
        taskResultAuto(completedAction,json,call);
    }
    private void taskResultAuto(String completedAction, String json, String call) {

        if (completedAction == "GetStaffReason")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {

                            //JSONObject data1 = new JSONObject(data.getString("staff_data"));
                            JSONArray staff_data  = data.getJSONArray("staff_data");
                            JSONArray reason_data = data.getJSONArray("reason_data");

                            SharedPreferences.Editor editor = student_pref.edit();
                            editor.putString("staff_data", staff_data.toString());
                            editor.putString("reason_data", reason_data.toString());
                            editor.commit();

                            // mobile_number
                            Intent ii = new Intent(DashBoardActivity.this,GatePassActivity.class);
                            startActivity(ii);

                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(DashBoardActivity.this,msg_1,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(pd.isShowing())
                    {
                        pd.dismiss();
                        //startActivity(getIntent());
                    }

                }
            }
        }

        // getbanner from server start
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
                            startActivity(getIntent());
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(DashBoardActivity.this,msg_1,Toast.LENGTH_LONG).show();

                            startActivity(getIntent());
                        }

                        if(pd.isShowing())
                        {
                            pd.dismiss();
                            startActivity(getIntent());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        // getbanner from server end

    } // taskResult end

    @Override
    public void taskStaticJSONResult(String completedAction, String json, String type) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}