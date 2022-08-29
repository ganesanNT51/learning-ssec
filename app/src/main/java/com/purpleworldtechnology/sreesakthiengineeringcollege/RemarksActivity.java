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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RemarksActivity extends AppCompatActivity   implements TaskDelegate, TaskStaticJSON {
        ListView remarkListView;
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String student_name = "",uid ="";
    public SharedPreferences pref;
    SharedPreferences.Editor editor;


    JSONObject data,jsonObject;
    ArrayList<Remarks> showRemarkData = null ;
    public static CustomRemarkListAdapter remarkAdapter;
    JSONArray remarkDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        pref = RemarksActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if(student_pref.contains("student_id")  )
        {
            student_name = student_pref.getString("student_name" , "");
            uid = student_pref.getString("student_uuid" , "");
        }
        else
        {
            Toast.makeText(RemarksActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(RemarksActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }

        remarkListView = (ListView) findViewById(R.id.remarkListView);
        if(isOnline())
        {
            // String url = MyApplication.url_new +"pages_table?last_sync_at="+ URLEncoder.encode(updated_at);
            String roll_number_base64 =  "MTljczA0";
            String urlPara = MyApplication.url_new +"remarks_by_sid?"+"&api_key="+MyApplication.api_key+"&uid="+ URLEncoder.encode(uid);
            new DataAsyncTask(pref,  RemarksActivity.this,"SyncRemarks").execute(urlPara);
        }
        else
        {
            Toast toast = Toast.makeText(RemarksActivity.this, "You are offline", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(RemarksActivity.this, DashBoardActivity.class);
            startActivity(intent);

        }

     } // onCreate method end


    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) RemarksActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "SyncRemarks")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            remarkDetails = data.getJSONArray("remarks_data");
                            showRemarkData = new Gson().fromJson(remarkDetails.toString(), new TypeToken<List<Remarks>>(){}.getType());

                            remarkAdapter = new CustomRemarkListAdapter(RemarksActivity.this, R.layout.remarks_list,showRemarkData);
                            remarkListView.setAdapter(remarkAdapter);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(RemarksActivity.this,msg_1,Toast.LENGTH_LONG).show();
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