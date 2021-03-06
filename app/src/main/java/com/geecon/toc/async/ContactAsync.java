package com.geecon.toc.async;

import android.os.AsyncTask;
import android.util.Log;

import com.geecon.toc.interfaces.ContactInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by MI on 3/18/2017.
 */

public class ContactAsync extends AsyncTask<String, Void, String> {
    private Exception exception;
    private ContactInterface contactInterface;

    public ContactAsync(ContactInterface contactInterface){
        this.contactInterface = contactInterface;
    }

    protected void onPreExecute() {
    }

    protected String doInBackground(String... urls) {

        try {
            URL url = new URL("http://www.event24seven.com/rest_services/RestController.php?view=contactUs&name="+URLEncoder.encode(urls[0], "UTF-8")+"&company="+URLEncoder.encode(urls[1], "UTF-8")+"&job="+URLEncoder.encode(urls[2], "UTF-8")+"&email="+URLEncoder.encode(urls[3], "UTF-8")+"&telephone="+URLEncoder.encode(urls[4], "UTF-8")+"&comments="+URLEncoder.encode(urls[5], "UTF-8"));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        /*if(response == null) {
            response = "THERE WAS AN ERROR";
        }*/
        contactInterface.contactResponse(response);
    }
}
