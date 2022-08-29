package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;

    String mobile,student_uuid,roll_no;
    int otp_number,department_id,student_id;
    JSONObject data;
    String student_name,father_name,department;
    TextView student_name_tv,father_name_tv,mobile_tv,rollno_tv,department_tv;
//    String student_name,father_name,mobile,rollno,department;
    Button back_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        student_pref = ProfileActivity.this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();


        if(student_pref.contains("student_name") ){
            student_name = student_pref.getString("student_name", "");
            father_name  = student_pref.getString("father_name", "");

            department   = student_pref.getString("department", "");
            mobile       = student_pref.getString("mobile", "");
            roll_no      = student_pref.getString("roll_no", "");
            student_uuid = student_pref.getString("student_uuid", "");


        }
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        father_name_tv  = (TextView) findViewById(R.id.father_name_tv);

        mobile_tv       = (TextView) findViewById(R.id.mobile_tv);
        rollno_tv       = (TextView) findViewById(R.id.rollno_tv);
        department_tv   = (TextView) findViewById(R.id.department_tv);
        back_btn        = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,DashBoardActivity.class);
                startActivity(i);
            }
        });

        if(student_name.length() > 0 )
        {
            student_name_tv.setText("Student Name  " + student_name);
        }

        if(father_name.length() > 0 )
        {
            father_name_tv.setText("Father Name  " + father_name);
        }

        if(department.length() > 0 )
        {
            department_tv.setText("Department Name  " + department);
        }


        if(mobile.length() > 0 )
        {
            mobile_tv.setText("Mobile Number  " + mobile);
        }

        if(roll_no.length() > 0 )
        {
            rollno_tv.setText("Roll Number  " + roll_no);
        }

    } // onCreate method end
}