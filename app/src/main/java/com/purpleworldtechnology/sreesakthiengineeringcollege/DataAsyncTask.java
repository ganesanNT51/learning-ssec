package com.purpleworldtechnology.sreesakthiengineeringcollege;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


public class DataAsyncTask extends AsyncTask<String, String, String> {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private TaskDelegate delegate;
    String completedAction;


    DataAsyncTask(SharedPreferences preference, TaskDelegate result, String _completedAction) {
        pref = preference;
        editor  = pref.edit();
        delegate = result;
        completedAction     = _completedAction;

    }

    @Override
    protected String doInBackground( String... params) {
        String result = new ServiceHandler().makeServiceCall( params[0], ServiceHandler.POST);

//        String result = new ServiceHandler().makeServiceCall(MyApplication.url_new + params[0], ServiceHandler.POST);
        return result;
    }

    protected void onPostExecute(String result) {
        delegate.taskResult(completedAction, result, null);
    }
}
