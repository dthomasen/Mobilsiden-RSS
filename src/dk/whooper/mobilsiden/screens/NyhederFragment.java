package dk.whooper.mobilsiden.screens;


import java.util.ArrayList;
import java.util.List;

import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.DatabaseHelper;
import dk.whooper.mobilsiden.service.XMLDownloader;
import dk.whooper.mobilsiden.service.XMLParser;
import android.R;
import android.support.v4.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NyhederFragment extends ListFragment {
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final String TAG="NyhederFragment";
	private List<Item> newsItems;
	private ArrayList newsHeadlines;
	private ArrayAdapter adapter;
	private BroadcastReceiver updateReciever;
	private ListView newsList;

	public NyhederFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		newsList = new ListView(getActivity());
		newsList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		newsList.setId(R.id.list);
		DatabaseHelper dbConn = new DatabaseHelper(getActivity());
		
		newsItems = dbConn.getAllItemsFromNews();
		newsHeadlines = new ArrayList();
		for(Item i : newsItems){
			newsHeadlines.add(i.getTitle());
		}
		
		adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, newsHeadlines);
		
		newsList.setAdapter(adapter);

		newsList.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view,
		              int position, long id) {
		        Log.i("debug", "single click");
		    }
		});
		
		dbConn.close();
		
		getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));
		return newsList;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		DatabaseHelper dbConn = new DatabaseHelper(getActivity());
	    String link = dbConn.getLinkFromNews((String) newsList.getItemAtPosition(position));

		Intent intent = new Intent(getActivity(), WebViewer.class);
		intent.putExtra("link", link);
		startActivity(intent);
	}
}