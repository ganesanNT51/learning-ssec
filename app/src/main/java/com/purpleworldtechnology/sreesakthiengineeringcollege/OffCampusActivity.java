package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OffCampusActivity extends AppCompatActivity implements TaskDelegate, TaskStaticJSON {

    ListView offcampusListView;
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String student_name = "",uid ="";
    public SharedPreferences pref;
    SharedPreferences.Editor editor;


    JSONObject data,jsonObject;
    ArrayList<Offcampus> showOffcampusData = null ;
    public static CustomOffcampusListAdapter offcampusAdapter;
    JSONArray offcampusDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_campus);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        pref = OffCampusActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if(student_pref.contains("student_id")  )
        {
            student_name = student_pref.getString("student_name" , "");
            uid = student_pref.getString("student_uuid" , "");
        }
        else
        {
            Toast.makeText(OffCampusActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(OffCampusActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }
        offcampusListView = (ListView) findViewById(R.id.offcampusListView);
        if(isOnline())
        {

            String urlPara = MyApplication.url_new +"offcampus_api?"+"&api_key="+MyApplication.api_key+"&uid="+ URLEncoder.encode(uid);
            new DataAsyncTask(pref,  OffCampusActivity.this,"GetOffcampusData").execute(urlPara);
        }
        else
        {
            Toast toast = Toast.makeText(OffCampusActivity.this, "You are offline", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(OffCampusActivity.this, DashBoardActivity.class);
            startActivity(intent);

        }

    } // onCreate method end
    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) OffCampusActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void taskResultAuto(String completedAction, String json, String call) {

        if (completedAction == "GetOffcampusData")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            offcampusDetails = data.getJSONArray("offcampus_data");
                            showOffcampusData = new Gson().fromJson(offcampusDetails.toString(), new TypeToken<List<Offcampus>>(){}.getType());

                            offcampusAdapter = new CustomOffcampusListAdapter(OffCampusActivity.this, R.layout.offcompu_list,showOffcampusData);
                            offcampusListView.setAdapter(offcampusAdapter);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(OffCampusActivity.this,msg_1,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    } // taskResult end

    @Override
    public void taskResult(String completedAction, String json, String call) {
        taskResultAuto(completedAction,json,call);

    }

    @Override
    public void taskStaticJSONResult(String completedAction, String json, String type) {

    }
}