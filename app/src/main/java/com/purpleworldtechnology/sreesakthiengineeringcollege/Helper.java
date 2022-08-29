package com.purpleworldtechnology.sreesakthiengineeringcollege;



import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Struct;
import java.sql.Time;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created  on 24/02/2020.
 */
public class Helper {
    //http://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
    public Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    //http://stackoverflow.com/questions/5351483/calculate-date-time-difference-in-java
    public long getTimeDifferenceInHours(Date startDate, Date endDate) {
//        String dateStart = "11/03/14 09:29:58";
//        String dateStop = "11/03/14 09:33:43";
//
//        // Custom date format
//        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//
//        Date d1 = null;
//        Date d2 = null;
//        try {
//            d1 = format.parse(dateStart);
//            d2 = format.parse(dateStop);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

// Get msec from each, and subtract.
        long diff = endDate.getTime() - startDate.getTime();
//        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
        //long diffHours = diff / (60 * 60 * 1000);
        return diff / (60 * 60 * 1000) % 60;
    }

    public String formattedFromString(String dt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dt);
//            System.out.println(date);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String formattedFromDate(Date dt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        try {
            str = format.format(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    //Sridhar On 26 Sep 2016
    //https://coderanch.com/t/649319/Android/Android-Parsing-Local-JSON-Data
    public String readAssetsJSONFile(Context context, String filename)
    {
        String myjsonstring = "";
        // Reading json file from assets folder
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {

            br = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myjsonstring  = sb.toString();
        return  myjsonstring;
    }

    //Sridhar On 26 Sep 2016
    //https://coderanch.com/t/649319/Android/Android-Parsing-Local-JSON-Data
    public String readFolderJSONFile(Context context, String filename)
    {

        String path =   Environment.getExternalStorageDirectory() + File.separator + "SSEC" + File.separator;
        File f = new File(path + filename);

        String myjsonstring = "";
        // Reading json file from assets folder
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(f));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myjsonstring  = sb.toString();
        return  myjsonstring;
    }

    /*
     *  Sridhar On 13 Dec 2016 3:23 PM
     *  usage :datetime current format to change custom format
     *  url : http://stackoverflow.com/questions/14999506/convert-string-date-to-string-date-different-format
     * */
    public String stringToStringDate(String aDate, String inFormat,String outFormat) {

        try {

            if (aDate == null) return null;
            SimpleDateFormat formatin = new SimpleDateFormat(inFormat);
            SimpleDateFormat formatout = new SimpleDateFormat(outFormat);
            Date date = formatin.parse(aDate);
            return formatout.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }

    }


    /*
     *sridhar On 2015
     * http://stackoverflow.com/questions/6290531/check-if-edittext-is-empty
     */
    public final static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    /*
     *sridhar On 2015
     *http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
     */
    public final static boolean isValidEmail(EditText etText) {
        CharSequence target = etText.getText().toString().trim().length() == 0?null:etText.getText().toString().trim();
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /*
     *sridhar On 2015
     *http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
     */
    public final static boolean isValidEmail(String etText) {
        CharSequence target = etText.length() == 0?null:etText;
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    /*
     *sridhar On 2015
     *http://stackoverflow.com/questions/22505336/email-and-phone-number-validation-in-android
     */
    public final static boolean isValidMobile(EditText etText)
    {
        if (etText.getText().toString().trim().length() == 0) {
            return false;
        }
        else if(etText.getText().toString().trim().length() < 10)
        {
            return false;
        }
        else {
            return android.util.Patterns.PHONE.matcher(etText.getText().toString().trim()).matches();
        }

    }

    /*
     *sridhar On 2015
     *http://stackoverflow.com/questions/22505336/email-and-phone-number-validation-in-android
     */
    public final static boolean isValidMobile(String etText)
    {
        if (etText.toString().trim().length() == 0) {
            return false;
        }
        else if(etText.toString().trim().length() < 10)
        {
            return false;
        }
        else {
            return android.util.Patterns.PHONE.matcher(etText.toString().trim()).matches();
        }

    }

    /*
     *sridhar On 2015
     *http://stackoverflow.com/questions/22505336/email-and-phone-number-validation-in-android
     */
    public final static boolean isValidOTP(EditText etText)
    {
        if (etText.getText().toString().trim().length() == 0) {
            return false;
        }
        else if(etText.getText().toString().trim().length() < 4)
        {
            return false;
        }
        else {
            return android.util.Patterns.PHONE.matcher(etText.getText().toString().trim()).matches();
        }

    }


    /*
     *sridhar On 07 Dec 2016 5:47 PM
     *http://stackoverflow.com/questions/2149680/regex-date-format-validation-on-java
     */
    public final static boolean isValidDateTimeFromat(String datetime,String validFormat)
    {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat(validFormat);

        try {
            format.parse(datetime);
            return true;
        }
        catch(ParseException e){
            return false;
        }
    }




    /*
     *sridhar On 28.Nov.2016 2.04 PM
     *http://stackoverflow.com/questions/12558206/how-can-i-check-if-a-value-is-of-type-integer
     */
    public final static Integer stringToInt(String s)
    {
        try
        {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex)
        {
            return 0;
        }
    }


    /*
     *sridhar On 13.Jan.2017 4.08 PM
     *http://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android
     */
    public final static String getDateTime(String aFormat)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(aFormat);
            return sdf.format(new Date());
        } catch (NumberFormatException ex)
        {
            return "";
        }
    }



    public final static boolean IsDateOpen(Date start, Date end)
    {
        try
        {
            Date date = new Date();
            if(start.compareTo(date) <= 0  && end.compareTo(date) >=0)
            {
                return  true;
            }
            else
            {
                return  false;
            }

        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public final static boolean isNull(String str)
    {
        if (str == null || str.isEmpty() || str.equalsIgnoreCase("null")) {
            return  true;
        } else {
            return  false;
        }
    }

    //https://stackoverflow.com/questions/18918662/httpurlconnection-https-vs-http
    //sridhar on 2018 Dec 22 10:22
    private final static void trustAllHosts()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
            }
        } };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
