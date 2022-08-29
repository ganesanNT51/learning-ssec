package com.purpleworldtechnology.sreesakthiengineeringcollege;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankActivity extends AppCompatActivity   implements TaskDelegate, TaskStaticJSON {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static String fileName = "file.pdf";
    DownloadManager manager;
    private static final int PERMISSION_CODE=1001;
    URL MY_URL;
    URL url;

    // Ganesan code start
    ListView questionbank_listview;
    SharedPreferences student_pref;
    SharedPreferences.Editor student_editor;
    String student_name = "",uid ="";
    public SharedPreferences pref;
    SharedPreferences.Editor editor;


    JSONObject data,jsonObject;
    ArrayList<Questions> showQuestionData = null ;
    public static CustomQuestionsAdapter questionsAdapter;
    JSONArray QuestionsDetails;
    String question_url= "";
    String file_name = "";String downloaded_file_path = "";
    // end



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);
//
//        downloadPdf();

        questionbank_listview = (ListView) findViewById(R.id.questionbank_listview);

        student_pref   = this.getSharedPreferences("StudentPref", 0); // 0 - for private mode
        student_editor = student_pref.edit();

        pref = QuestionBankActivity.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if(student_pref.contains("student_id")  )
        {
            student_name = student_pref.getString("student_name" , "");
            uid = student_pref.getString("student_uuid" , "");
        }
        else
        {
            Toast.makeText(QuestionBankActivity.this,"Invalid Login ." ,Toast.LENGTH_LONG).show();
            Intent ii  = new Intent(QuestionBankActivity.this,MobileNumberActivity.class);
            startActivity(ii);
        }

        if(isOnline())
        {
            // String url = MyApplication.url_new +"pages_table?last_sync_at="+ URLEncoder.encode(updated_at);
            String roll_number_base64 =  "MTljczA0";
            String urlPara = MyApplication.url_new +"questions_api?"+"&api_key="+MyApplication.api_key+"&uid="+ URLEncoder.encode(uid);
            new DataAsyncTask(pref,  QuestionBankActivity.this,"SyncQuestion").execute(urlPara);
        }
        else
        {
            Toast toast = Toast.makeText(QuestionBankActivity.this, "You are offline", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(QuestionBankActivity.this, DashBoardActivity.class);
            startActivity(intent);

        }

        questionbank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                question_url =  showQuestionData.get(position).question_url;
                file_name    =  showQuestionData.get(position).file_name;
                question_url= MyApplication.url_new+question_url;

                downloaded_file_path  = Environment.getExternalStorageDirectory().getPath() + "/SSEC/QB/"+file_name;
                File file = new File(downloaded_file_path);
                if (!file.exists())
                {
                    if(isOnline())
                    {
                        new DownloadFile().execute(question_url,file_name);
                    }
                    else
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionBankActivity.this);
                        builder1.setMessage("You are offline.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                else
                {
                    Intent ii = new Intent(QuestionBankActivity.this,PdfViewActivity.class);
                    ii.putExtra("file_path" ,downloaded_file_path);
                    startActivity(ii);
                }


            }
        });
    } // onCreate method end


    private void downloadPdf(String question_url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(question_url)));


    }



    private class DownloadFile extends AsyncTask<String, Integer, String> {
        ProgressDialog pdLoading = new ProgressDialog(QuestionBankActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tQuestion Downloading ...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... urlParams) {
            int count;
            try {
                URL url = new URL(question_url);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();
                //  write the document content
                  String directory_path = Environment.getExternalStorageDirectory().getPath() + "/SSEC/QB";
//                String directory_path =  getExternalFilesDir("/").getAbsolutePath()+ File.separator + "SSEC/QB";
                File file = new File(directory_path);
                if (!file.exists())
                {
                    file.mkdirs();
                }
                else
                {
                    // downlod the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream( directory_path+"/" + file_name);

                    byte data[] = new byte[1024];
                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        publishProgress((int) (total * 100 / lenghtOfFile));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }

            } catch (Exception e) {
                Toast.makeText(QuestionBankActivity.this,e.toString() ,Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            Intent i = new Intent(QuestionBankActivity.this,PdfViewActivity.class);
            i.putExtra("file_path" ,downloaded_file_path);
            startActivity(i);


        }
    }


    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) QuestionBankActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (completedAction == "SyncQuestion")
        {
            String test_string = "[]\n";
            if(json != null && !json.isEmpty()) {
                if (!json.equals(test_string)) {
                    try {
                        data = new JSONObject(json);
                        if (data.getString("msg").equals("success")) {
                            QuestionsDetails = data.getJSONArray("question_data");
                            showQuestionData = new Gson().fromJson(QuestionsDetails.toString(), new TypeToken<List<Questions>>(){}.getType());

                            questionsAdapter = new CustomQuestionsAdapter(QuestionBankActivity.this, R.layout.question_bank_list,showQuestionData);
                            questionbank_listview.setAdapter(questionsAdapter);
                        }
                        else
                        {
                            String msg_1 = data.getString("msg");
                            Toast.makeText(QuestionBankActivity.this,msg_1,Toast.LENGTH_LONG).show();
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