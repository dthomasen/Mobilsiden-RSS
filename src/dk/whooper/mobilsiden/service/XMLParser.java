package dk.whooper.mobilsiden.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class XMLParser implements Runnable{
	
	Context context;
	
	public XMLParser(Context context){
		this.context = context;
	}
	
	public void run(){
		String xmlRss = AndroidFileFunctions.getFileValue("rss.xml", context);
		Log.d("XMLPARSER", "Service started");
	}
}
