package dk.whooper.mobilsiden.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

	public class WebTvFragment extends ListFragment {

		public static final String ARG_SECTION_NUMBER = "section_number";
		private String result;
		private static final String TAG="WebTVFragment";
		private BroadcastReceiver updateReciever;
		private ArrayAdapter adapter;
		private List<Item> webtvItems;
		private ArrayList webtvHeadlines;
		
		public WebTvFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			ListView webtvList = new ListView(getActivity());
			webtvList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			webtvList.setId(R.id.list);
			DatabaseHelper dbConn = new DatabaseHelper(getActivity());
			
			webtvItems = dbConn.getAllItemsFromWebTv();
			webtvHeadlines = new ArrayList();
			for(Item i : webtvItems){
				webtvHeadlines.add(i.getTitle());
			}
			
			adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, webtvHeadlines);

			setListAdapter(adapter);

			updateReciever = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					Log.d(TAG,"WebTV broadcast recieved");
					DatabaseHelper dbConn = new DatabaseHelper(getActivity());
					webtvItems = dbConn.getAllItemsFromWebTv();
					for(Item i : webtvItems){
						webtvHeadlines.add(i.getTitle());
					}
					adapter.notifyDataSetChanged();
					dbConn.close();
				}
			};
			
			dbConn.close();
			
			getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));

			return webtvList;
		}
	}