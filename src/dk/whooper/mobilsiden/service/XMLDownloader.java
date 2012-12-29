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

	private static final String TAG="XMLDownloader";
	private Context context;

	@Override
	protected Intent doInBackground(Intent... params) {
		
		String newsXml = downloadXML("http://feeds.mobilsiden.dk/MobilsidendkNyhedsoversigt?format=xml");
		String webTvXml = downloadXML("http://feeds.mobilsiden.dk/MobilsidendkAnmeldelser?format=xml");
		String reviewsXml = downloadXML("http://feeds.mobilsiden.dk/MobilsidendkWebvideo?format=xml");
		
		context = (Context) params[0].getExtras().getSerializable("Activity");
		AndroidFileFunctions.writeToFile("newsRSS.xml", newsXml, context, Context.MODE_WORLD_READABLE);
		AndroidFileFunctions.writeToFile("webTvRSS.xml", webTvXml, context, Context.MODE_WORLD_READABLE);
		AndroidFileFunctions.writeToFile("reviewsRSS.xml", reviewsXml, context, Context.MODE_WORLD_READABLE);
		
		return params[0];
	}      

	private String convertStreamToString(InputStream is) throws IOException {
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

	private String downloadXML(String page){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(page);
		HttpResponse webServerResponse = null;

		try {
			webServerResponse = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpEntity httpEntity = webServerResponse.getEntity();
		String result = "";
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
		return result;
	}
	
	@Override
	protected void onPostExecute(Intent params) {
		Log.d(TAG,"On post execute");
		
		XMLParser xmlParser = new XMLParser(context);
		xmlParser.execute();
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}
}   