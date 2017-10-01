package com.geecon.toc.async;

import android.os.AsyncTask;
import android.util.Log;

import com.geecon.toc.interfaces.PollingSubmitInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MI on 3/17/2017.
 */

public class PollingSubmitAsync extends AsyncTask<String, Void, String> {
    private Exception exception;
    private PollingSubmitInterface pollingSubmitInterface;

    public PollingSubmitAsync(PollingSubmitInterface pollingSubmitInterface){
        this.pollingSubmitInterface = pollingSubmitInterface;
    }

    protected void onPreExecute() {
    }

    protected String doInBackground(String... urls) {

        try {
            URL url = new URL("http://www.event24seven.com/rest_services/RestController.php?view=pollingResponse&sresponse="+urls[0]);
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
        pollingSubmitInterface.pollingSubmitResponse(response);
        //progressBar.setVisibility(View.GONE);
        //Log.i("INFO", response);
        //responseView.setText(response);
    }
}
