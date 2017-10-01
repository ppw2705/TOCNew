package com.geecon.toc.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.geecon.toc.interfaces.ConferenceDetailsInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MI on 3/11/2017.
 */

public class ConferenceDetailsAsync extends AsyncTask<String, Void, String> {

    private Exception exception;
    private ConferenceDetailsInterface conferenceDetailsInterface;

    public ConferenceDetailsAsync(ConferenceDetailsInterface conferenceDetailsInterface){
        this.conferenceDetailsInterface = conferenceDetailsInterface;
    }

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected String doInBackground(String... urls) {
        //String email = emailText.getText().toString();
        // Do some validation here

        try {
            URL url = new URL("http://www.event24seven.com/rest_services/RestController.php?view=getConferenceDetail&confId="+urls[0]);
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
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        conferenceDetailsInterface.responseReceived(response);
        //progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
        //responseView.setText(response);
    }
}
