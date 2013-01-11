package dk.whooper.mobilsiden.service;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginTokenCollector extends AsyncTask<String, Void, String> {
    private static final String TAG = "LoginTokenCollector";
    private String result;


    //Parameters:
    //1. Article ID
    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();


        String url = "http://www.mobilsiden.dk/services/json/auth.php?key=5535106688a0a103926ff1e3841e8335&login=" + params[0] + "&pass=" + params[1];

        HttpGet request = new HttpGet(url);
        HttpResponse webServerResponse = null;

        try {
            webServerResponse = httpClient.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity httpEntity = webServerResponse.getEntity();

        if (httpEntity != null) {
            InputStream inStream;
            try {
                inStream = httpEntity.getContent();
                result = convertStreamToString(inStream);
                Log.d(TAG, "Dette er mit json!: " + result);
                inStream.close();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (webServerResponse.getStatusLine().getStatusCode() == 400) {
            return "loginFailed";
        }

        return result;
    }

    protected void onPostExecute(String result) {

    }

    public String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
