package dk.whooper.mobilsiden.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class WebScraper extends AsyncTask<String, Void, String>{

	private String result = "";
	private static final String TAG = "WebScraper";
	Elements info;

	@Override
	protected String doInBackground(String... params) {
		Document eventInfo;
		try {
			eventInfo = Jsoup.connect(params[0]).get();
			info = eventInfo.select("div.article");
			Log.d(TAG,"SIIIIZE OMG: "+info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return info.html();
	}

	protected void onPostExecute(String result){
		
		
	}

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line = null;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "ISO-8859-1"));
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
