package com.purpleworldtechnology.sreesakthiengineeringcollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class ExamMarkActivity extends AppCompatActivity  implements TaskDelegate, TaskStaticJSON {
    private final int ORANGE = Color.rgb(253, 104, 40);
    private final int[] COLORS = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, ORANGE};
    /** the amount of variation in R, G, B components of a confetti burst */
    private final int BURST_COLOR_SPREAD = 200;
    KonfettiView konfettiView;
    int color;
    Spinner semester,internal;
    Button internal_result_btn;
    public  int semester_id =0,internal_id =0;
    public  int student_id = 0;
    JSONObject data,jsonObject;
    String student_name;

    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView  markLV;

    ArrayList<Subjects> showMarkData = null ;
    public static CustomMarkListAdapter adapter;
    JSONArray markDetails;

    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String uid;
    String student_id_str= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_mark);

        semester = (Spinner) findViewById(R.id.semester);
        internal = (Spinner) findViewById(R.id.internal);
        markLV   = (ListView) findViewById(R.id.MarkListView) ;



        internal_result_btn = (Button) findViewById(R.id.internal_result_btn);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();



        pref = ExamMarkActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


        if(student_pref.contains("student_id")  )
        {
            student_name = student_pref.getString("student_name" , "");
            uid = student_pref.getString("student_uuid" , "");
            student_id = Integer.parseInt(student_pref.getString("student_id", ""));
            student_id_str   = student_pref.getString("student_id" , "");
        }
        else
        {
            Toast.makeText(ExamMarkActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(ExamMarkActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }

         konfettiView = findViewById(R.id.viewKonfetti);
         color = COLORS[new Random().nextInt(COLORS.length)];

//        konfettiView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                startKonfetti();
//                konfettiView.removeOnLayoutChangeListener(this);
//            }
//        });
       // final KonfettiView konfettiView = findViewById(R.id.konfettiView);

        internal_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semester_id_str = semester.getSelectedItem().toString();
                String internal_id_str = internal.getSelectedItem().toString();

                if(!semester_id_str.isEmpty() || !semester_id_str.contains("null")  || semester_id_str != "NULL")
                {
                    if (semester_id_str.contains("First"))
                    {
                        semester_id = 1;
                    }
                    else if (semester_id_str.contains("Second"))
                    {
                        semester_id = 2;
                    }
                    else if (semester_id_str.contains("Third"))
                    {
                        semester_id = 3;
                    }
                    else if (semester_id_str.contains("Four"))
                    {
                        semester_id = 4;
                    }
                    else if (semester_id_str.contains("Fift"))
                    {
                        semester_id = 5;
                    }
                    else if (semester_id_str.contains("Six"))
                    {
                        semester_id = 6;
                    }
                    else if (semester_id_str.contains("Seven"))
                    {
                        semester_id = 7;
                    }
                    else if (semester_id_str.contains("Eight"))
                    {
                        semester_id = 8;
                    }
                    else
                    {
                        Toast.makeText(ExamMarkActivity.this, "Please select semester ", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(ExamMarkActivity.this, "Please select semester ", Toast.LENGTH_LONG).show();
                }




                // internal id
                if (!internal_id_str.isEmpty() || !internal_id_str.contains("null")  || internal_id_str != "NULL" )
                {
                    if (internal_id_str.contains("1"))
                    {
                        internal_id = 1;
                    }
                    else if (internal_id_str.contains("2"))
                    {
                        internal_id = 2;
                    }
                    else if (internal_id_str.contains("3"))

                    {
                        internal_id = 3;
                    }
                    else
                    {
                        Toast.makeText(ExamMarkActivity.this, "Please select internal ", Toast.LENGTH_LONG).show();
                    }


                }
                else
                {
                    Toast.makeText(ExamMarkActivity.this, "Please select internal ", Toast.LENGTH_LONG).show();
                }

                if(semester_id  >0 && internal_id > 0)
                {
                    // String url = MyApplication.url_new +"pages_table?last_sync_at="+ URLEncoder.encode(updated_at);
                    String urlPara = MyApplication.url_new +"internal_mark_by_student_id/"+student_id+"?semester_id="+semester_id+"&internal_id="+internal_id+"&api_key="+MyApplication.api_key;
                    new DataAsyncTask(pref,  ExamMarkActivity.this,"SyncInternalMark").execute(urlPara);
                }






            }
        });


        konfettiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startKonfetti();
                color = COLORS[new Random().nextInt(COLORS.length)];
                burstKonfetti(color);
                color = COLORS[new Random().nextInt(COLORS.length)];
                burstKonfetti(color);
                color = COLORS[new Random().nextInt(COLORS.length)];
                burstKonfetti(color);
                color = COLORS[new Random().nextInt(COLORS.length)];
                burstKonfetti(color);
            }
        });
    }
    private void startKonfetti() {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 3000L);

    }

    private void burstKonfetti(int color){
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        Random random = new Random();
        int[] variations = new int[10];
        int halfSpread = BURST_COLOR_SPREAD / 2;
        for (int i = 0; i < variations.length; i++) {
            variations[i] = Color.rgb(
                    colorComponentClamp(r - halfSpread + random.nextInt(BURST_COLOR_SPREAD)),
                    colorComponentClamp(g - halfSpread + random.nextInt(BURST_COLOR_SPREAD)),
                    colorComponentClamp(b - halfSpread + random.nextInt(BURST_COLOR_SPREAD))
            );
        }

        float posX = (float) (Math.random() * konfettiView.getWidth());
        float posY = (float) (Math.random() * konfettiView.getHeight());

        konfettiView.build()
                .addColors(variations)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5))
                .setPosition(posX, posY)
                .burst(100);
    }
    private static int colorComponentClamp(int value){
        return Math.min(255, Math.max(0, value));
    }


    @Override
    public void taskResult(String completedAction, String json, String call) {
        taskResultAuto(completedAction,json,call);
    }
    private void taskResultAuto(String completedAction, String json, String call) {

        if (completedAction == "SyncInternalMark")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            markDetails = data.getJSONArray("mark_data");
                            showMarkData = new Gson().fromJson(markDetails.toString(), new TypeToken<List<Subjects>>(){}.getType());

                            adapter = new CustomMarkListAdapter(ExamMarkActivity.this, R.layout.internal_marks,showMarkData);
                            markLV.setAdapter(adapter);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(ExamMarkActivity.this,msg_1,Toast.LENGTH_LONG).show();
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