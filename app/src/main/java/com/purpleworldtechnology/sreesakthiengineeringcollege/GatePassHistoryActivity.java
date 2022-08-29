package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GatePassHistoryActivity extends AppCompatActivity  implements TaskDelegate, TaskStaticJSON {
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    int student_id;

    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    JSONObject data;
    ListView gatepassLV;

    ArrayList<GatePass> showGatepassData = null ;
    public static CustomGatepassHistoryAdapter adapter;
    JSONArray gatepassDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass_history);

        gatepassLV     = (ListView)findViewById(R.id.gatepassListView);

        pref = GatePassHistoryActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();
        if(student_pref.contains("student_id") )
        {
            student_id = Integer.parseInt(student_pref.getString("student_id", ""));
            if(isOnline())
            {
                // String urlPara = MyApplication.url_new +"internal_mark_by_student_id/"+"&api_key="+MyApplication.api_key;
                String urlPara = MyApplication.url_new +"request_data_by_studid/"+student_id;
                new DataAsyncTask(pref,  GatePassHistoryActivity.this,"GatapassHistorySync").execute(urlPara);
            }
            else
            {
                Toast.makeText(GatePassHistoryActivity.this,"You are offline ." ,Toast.LENGTH_LONG).show();
                return;
            }
        }
        else
        {
            Toast.makeText(GatePassHistoryActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(GatePassHistoryActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }


    }
    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) GatePassHistoryActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "GatapassHistorySync")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            gatepassDetails = data.getJSONArray("std_pass_request_data");
                            showGatepassData = new Gson().fromJson(gatepassDetails.toString(), new TypeToken<List<GatePass>>(){}.getType());

                            adapter = new CustomGatepassHistoryAdapter(GatePassHistoryActivity.this, R.layout.gate_pass_list,showGatepassData);
                            gatepassLV.setAdapter(adapter);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(GatePassHistoryActivity.this,msg_1,Toast.LENGTH_LONG).show();
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