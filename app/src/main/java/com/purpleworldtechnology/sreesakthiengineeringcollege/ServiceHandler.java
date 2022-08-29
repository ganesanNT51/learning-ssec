package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.media.Image;
import android.util.Log;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by arulkumar on 19/08/15.
 */
public class ServiceHandler {

    String tag = "ServiceHandler";

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
//    public String makeServiceCall(String url, int method) {
//        return this.makeServiceCall(url, method);
//    }
    public String makeServiceCall(String url, int method) {
        return makeServiceCall(url,method,null);
    }

    public String makeServiceCall(String url, int method, String json) {
        Log.e(tag,"URL : " + url);
// HTTP Client that supports streaming uploads and downloads
        //DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());


        //https://stackoverflow.com/questions/2012497/accepting-https-connections-with-self-signed-certificates/3904473#3904473
        //https://stackoverflow.com/questions/10352383/android-scheme-http-not-registered-on-ics-4-0-4-w-proxy

        DefaultHttpClient client = new DefaultHttpClient();


        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        int timeoutConnection = 30000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);

        int timeoutSocket = 30000;
        HttpConnectionParams
                .setSoTimeout(httpParameters, timeoutSocket);


        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(httpParameters, registry);
        DefaultHttpClient httpclient = new DefaultHttpClient(mgr, httpParameters);

// Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);




        // Define that I want to use the POST method to grab data from
        // the provided URL
        HttpPost httppost = new HttpPost(url);




        //http://stackoverflow.com/questions/6218143/how-to-send-post-request-in-json-using-httpclient
        if (json!=null) {
            try {

                //passes the results to a string builder/entity
                StringEntity se = new StringEntity(json);
                //sets the post request as the resulting string
                httppost.setEntity(se);

            } catch (UnsupportedEncodingException ex) {

            }
        }

        // Web service used is defined
        httppost.setHeader("Content-type", "application/json");

        // Used to read data from the URL
        InputStream inputStream = null;

        // Will hold the whole all the data gathered from the URL
        String result = null;

        try {

            // Get a response if any from the web service
            HttpResponse response = httpclient.execute(httppost);

            // The content from the requested URL along with headers, etc.
            HttpEntity entity = response.getEntity();

            // Get the main content from the URL
            inputStream = entity.getContent();

            // JSON is UTF-8 by default
            // BufferedReader reads data from the InputStream until the Buffer is full
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            // Will store the data
            StringBuilder theStringBuilder = new StringBuilder();

            String line = null;

            // Read in the data from the Buffer untilnothing is left
            while ((line = reader.readLine()) != null) {

                // Add data from the buffer to the StringBuilder
                theStringBuilder.append(line + "\n");
            }

            // Store the complete data in result
            result = theStringBuilder.toString();
            MyApplication.isOnline = true;

        } catch (UnknownHostException e) {
            Log.i("LOG", "Offline");
            MyApplication.isOnline = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Close the InputStream when you're done with it
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    //http://stackoverflow.com/questions/14272254/how-to-post-key-value-pairs-instead-of-a-json-object-using-spring-for-android
    public String makeServiceCallNew(String url, int method, List<NameValuePair> val) {
        // HTTP Client that supports streaming uploads and downloads
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

        // Define that I want to use the POST method to grab data from
        // the provided URL
        HttpPost httppost = new HttpPost(url);

        // Used to read data from the URL
        InputStream inputStream = null;

        // Will hold the whole all the data gathered from the URL
        String result = null;

        //http://stackoverflow.com/questions/6218143/how-to-send-post-request-in-json-using-httpclient
        if (val!=null) {
            try {


//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("name", nameString));
//                nameValuePairs.add(new BasicNameValuePair("Country", "country name"));


                httppost.setEntity(new UrlEncodedFormEntity(val));

                // Get a response if any from the web service
                HttpResponse response = httpclient.execute(httppost);

                // The content from the requested URL along with headers, etc.
                HttpEntity entity = response.getEntity();

                // Get the main content from the URL
                inputStream = entity.getContent();

                // JSON is UTF-8 by default
                // BufferedReader reads data from the InputStream until the Buffer is full
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                // Will store the data
                StringBuilder theStringBuilder = new StringBuilder();

                String line = null;

                // Read in the data from the Buffer untilnothing is left
                while ((line = reader.readLine()) != null) {

                    // Add data from the buffer to the StringBuilder
                    theStringBuilder.append(line + "\n");
                }

                // Store the complete data in result
                result = theStringBuilder.toString();


            } catch (UnsupportedEncodingException ex) {

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Web service used is defined
//        httppost.setHeader("Content-type", "application/json");


        return result;
    }

    //http://stackoverflow.com/questions/14272254/how-to-post-key-value-pairs-instead-of-a-json-object-using-spring-for-android
    public String makeServiceCallNew2(String url, int method, MultipartEntity val) {
        // HTTP Client that supports streaming uploads and downloads
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

        // Define that I want to use the POST method to grab data from
        // the provided URL
        HttpPost httppost = new HttpPost(url);

        // Used to read data from the URL
        InputStream inputStream = null;

        // Will hold the whole all the data gathered from the URL
        String result = null;

        //http://stackoverflow.com/questions/6218143/how-to-send-post-request-in-json-using-httpclient
        if (val!=null) {
            try {


//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("name", nameString));
//                nameValuePairs.add(new BasicNameValuePair("Country", "country name"));




                httppost.setEntity(val);
                // httpclient.execute(httppost);

                // Get a response if any from the web service
                HttpResponse response = httpclient.execute(httppost);

                // The content from the requested URL along with headers, etc.
                HttpEntity entity = response.getEntity();

                // Get the main content from the URL
                inputStream = entity.getContent();

                // JSON is UTF-8 by default
                // BufferedReader reads data from the InputStream until the Buffer is full
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                // Will store the data
                StringBuilder theStringBuilder = new StringBuilder();

                String line = null;

                // Read in the data from the Buffer untilnothing is left
                while ((line = reader.readLine()) != null) {

                    // Add data from the buffer to the StringBuilder
                    theStringBuilder.append(line + "\n");
                }

                // Store the complete data in result
                result = theStringBuilder.toString();


            } catch (UnsupportedEncodingException ex) {

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Web service used is defined
//        httppost.setHeader("Content-type", "application/json");


        return result;
    }
}