package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GatePassActivity extends AppCompatActivity {
private LinearLayout AppygatePass,history;
private TextView lsaUserName;
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String student_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass);
        AppygatePass=findViewById(R.id.AppygatePass);
        history=findViewById(R.id.history);

        lsaUserName = (TextView)findViewById(R.id.lsaUserName);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();
        if(student_pref.contains("student_id")  )
        {
             student_name = student_pref.getString("student_name" , "");

        }
        else
        {
            Toast.makeText(GatePassActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(GatePassActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }





        lsaUserName.setText("Hi  "  + student_name);
        AppygatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After 5 seconds redirect to another intent
                Intent intent = new Intent(GatePassActivity.this, ApplyGatePassActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After 5 seconds redirect to another intent
                Intent intent = new Intent(GatePassActivity.this, GatePassHistoryActivity.class);
                intent.putExtra("is_from_gatepass", 1);
//                intent.putExtra("request_data", requestDetails.toString());
                startActivity(intent);
            }
        });
    }
}