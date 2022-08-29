package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ApplyGatePassActivity extends AppCompatActivity implements TaskDelegate, TaskStaticJSON {
     Integer student_id = 1;
     String request_to,reason ,return_date;
     String reg_no = "";
     Spinner request_to_1,reason_1;
     EditText return_date_et;
     private TextView return_date_tv;
     Context context;
     int mYear,mMonth,mDay;
     DatePicker picker;
     Button apply_btn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    JSONObject jsonObject;
    JSONObject data,jsonobject,jsonObject_reason,jsonObject_staff;
    private DatePickerDialog datepic;

    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String staff_name;
    ArrayList<String> reason_arr = new ArrayList<String>();
    ArrayList<String> staff_name_arr = new ArrayList<String>();

    ArrayList<Integer> sender_id_list     = new ArrayList<Integer>();
    ArrayList<Integer> society_id_list_   = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_gate_pass);
        request_to_1   = (Spinner) findViewById(R.id.request_to);
        reason_1       = (Spinner) findViewById(R.id.reason);
        return_date_tv = (TextView) findViewById(R.id.return_date_tv);
        picker         = (DatePicker)findViewById(R.id.datePicker1);

        apply_btn = (Button) findViewById(R.id.apply_btn);

        pref = ApplyGatePassActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();
        if(student_pref.contains("student_id") )
        {
            student_id = Integer.parseInt(student_pref.getString("student_id", ""));
//            reg_no     =  student_pref.getString("reg_no", "");
        }
        else
        {
            Toast.makeText(ApplyGatePassActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(ApplyGatePassActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }

        String strJson_staff  = student_pref.getString("staff_data","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        String strJson_reason = student_pref.getString("reason_data","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        // society table data store in strJson

        if (strJson_staff != null) {
            try {

                JSONArray jsonArray = new JSONArray(strJson_staff);
                for(int ij=0; ij < jsonArray.length(); ij++) {

                    jsonObject_staff = jsonArray.getJSONObject(ij);
                    staff_name       = jsonObject_staff.getString("staff_name");
                    staff_name_arr.add(staff_name);
                }

                //society_list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Society>>(){}.getType());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (strJson_reason != null) {
            try {
                JSONArray jsonArray = new JSONArray(strJson_reason);
                for(int ij=0; ij < jsonArray.length(); ij++) {
                    jsonObject_reason = jsonArray.getJSONObject(ij);
                    reason            = jsonObject_reason.getString("reason");
                    reason_arr.add(reason);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, reason_arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        reason_1.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerArrayAdapterStaff = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, staff_name_arr);
        spinnerArrayAdapterStaff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        request_to_1.setAdapter(spinnerArrayAdapterStaff);


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        return_date_tv.startAnimation(anim);
// clicking event
        return_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datepic = new DatePickerDialog(ApplyGatePassActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        return_date_tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datepic.show();
            }
        });


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request_to  = request_to_1.getSelectedItem().toString();
                reason      = reason_1.getSelectedItem().toString();
                return_date =  return_date_tv.getText().toString();

                if(return_date.isEmpty() || return_date.length() <=0 )
                {
                    Toast.makeText(ApplyGatePassActivity.this, "Please select return date.",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(reason.isEmpty() || reason.length() <=0 )
                {
                    Toast.makeText(ApplyGatePassActivity.this, "Please select reason.",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(request_to.isEmpty() || request_to.length() <=0 )
                {
                    Toast.makeText(ApplyGatePassActivity.this, "Please select staff.",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    if(isOnline())
                    {
                        List<NameValuePair> insertParameter = new ArrayList<NameValuePair>();
                        insertParameter.add(new BasicNameValuePair("student_id", String.valueOf(student_id)));
                        insertParameter.add(new BasicNameValuePair("request_to", request_to));
                        insertParameter.add(new BasicNameValuePair("reason", reason));
                        insertParameter.add(new BasicNameValuePair("return_date", return_date));
//                        insertParameter.add(new BasicNameValuePair("reg_no", reg_no));



                        // "http://192.168.0.101:5001/gatepass_request"
                        String urlPara = "gatepass_request?api_key="+MyApplication.api_key;
                        new DeviceTokenAsyncTask(pref,  ApplyGatePassActivity.this, insertParameter).execute(urlPara, null);
                    }
                    else
                    {
                        Toast.makeText(ApplyGatePassActivity.this,"You are offline ." ,Toast.LENGTH_LONG).show();
                        return;
                    }
                }






            }
        });
    } // oncreate end




    private Boolean isOnline()	{
        ConnectivityManager cm = (ConnectivityManager) ApplyGatePassActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "PostData")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {

                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                           //  JSONObject data1 = new JSONObject(data.getString("data"));
                           // JSONArray requestDetails = data1.getJSONArray("std_pass_request_data");

                            JSONArray staff_data  = data.getJSONArray("std_pass_request_data");

                            Toast.makeText(ApplyGatePassActivity.this,"Your gate pss request successfully registered.Please wait until get permission ",Toast.LENGTH_LONG).show();

                            Intent i = new Intent(ApplyGatePassActivity.this, GatePassHistoryActivity.class);
                            i.putExtra("is_from_gatepass", 0);
                            i.putExtra("request_data", staff_data.toString());

                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(ApplyGatePassActivity.this,data.getString("msg"),Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
        @Override
    public void taskStaticJSONResult(String completedAction, String json, String type) {

    }

    public class Insert extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ApplyGatePassActivity.this);
            dialog.setMessage("Loading , please wait");
            dialog.setTitle("Connecting... ");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                List<NameValuePair> insert = new ArrayList<NameValuePair>();
                insert.add(new BasicNameValuePair("student_id", String.valueOf(student_id)));
                insert.add(new BasicNameValuePair("request_to", request_to));
                insert.add(new BasicNameValuePair("reason", reason));
                insert.add(new BasicNameValuePair("return_date", return_date));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://192.168.0.101:5001/gatepass_request"); // link to connect to database
                httpPost.setEntity(new UrlEncodedFormEntity(insert));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            android.app.AlertDialog.Builder ac = new android.app.AlertDialog.Builder(ApplyGatePassActivity.this);
            ac.setTitle("Result");
            ac.setMessage("Details Successfully saved");
            ac.setCancelable(true);
            ac.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            android.app.AlertDialog alert = ac.create();
            alert.show();
        }
    }  //insert close

}