package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.SharedPreferences;
import android.os.AsyncTask;


public class AsyncTaskOTPSync extends AsyncTask<String, String, String> {

    private String resultType;
    private TaskDelegate delegate;

    AsyncTaskOTPSync(TaskDelegate result,String _resultType) {
        delegate = result;
        resultType = _resultType;
    }

    @Override
    protected String doInBackground( String... params) {
        String result = new ServiceHandler().makeServiceCall(MyApplication.url_new + params[0], ServiceHandler.POST);

        return result;
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            try {

            }
            catch (Exception ex) {


            }
            finally {
                delegate.taskResult(resultType, result, null);
            }
        }
    }
}
