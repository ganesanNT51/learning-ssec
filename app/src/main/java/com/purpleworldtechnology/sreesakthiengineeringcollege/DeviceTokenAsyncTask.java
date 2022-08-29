package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.util.List;


public class DeviceTokenAsyncTask extends AsyncTask<String, String, String> {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private TaskDelegate delegate;
    private List<NameValuePair> val;

    DeviceTokenAsyncTask(SharedPreferences preference, TaskDelegate result, List<NameValuePair> _val) {
        pref = preference;
        editor  = pref.edit();
        delegate = result;
        val = _val;
    }

    @Override
    protected String doInBackground( String... params) {
        String result = new ServiceHandler().makeServiceCallNew(MyApplication.url_new + params[0], ServiceHandler.POST,val);
//        Log.i("LOG-AUTH", MyApplication.url + params[0] );
        return result;
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            try {

            }
            catch (Exception ex) {

            }
            finally {
                delegate.taskResult("PostData", result, "");
            }
        }
    }
}

