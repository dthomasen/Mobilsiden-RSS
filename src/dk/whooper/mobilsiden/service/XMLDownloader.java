package dk.whooper.mobilsiden.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class XMLDownloader extends AsyncTask<Intent, Void, Intent> {

	private String result;
	private static final String TAG="XMLDownloader";

	@Override
	protected Intent doInBackground(Intent... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://www.mobilsiden.dk/xml/rssfeed.php");
		HttpResponse webServerResponse = null;

		try {
			webServerResponse = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpEntity httpEntity = webServerResponse.getEntity();

		if(httpEntity != null){
			InputStream inStream;
			try {
				inStream = httpEntity.getContent();
				result = convertStreamToString(inStream);
				inStream.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d("XMLDOWNLOADER", result);
		return params[0];
	}      

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line = null;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "ISO-8859-1"));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	@Override
	protected void onPostExecute(Intent params) {
		Log.d(TAG,"On post execute");

		Context context = (Context) params.getExtras().getSerializable("Activity");
		AndroidFileFunctions.writeToFile("rss.xml", result, context, Context.MODE_WORLD_READABLE);

		XMLParser xmlParser = new XMLParser(context);
		xmlParser.run();
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}
}   