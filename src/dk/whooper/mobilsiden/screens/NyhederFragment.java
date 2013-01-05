package dk.whooper.mobilsiden.screens;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NyhederFragment extends ListFragment {
    private static final String TAG = "NyhederFragment";
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
        newsList.setId(android.R.id.list);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());

        newsItems = dbConn.getAllItemsFromNews();
        newsHeadlines = new ArrayList();
        for (Item i : newsItems) {
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

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d(TAG, "WebTV broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                newsItems = dbConn.getAllItemsFromWebTv();
                for (Item i : newsItems) {
                    newsHeadlines.add(i.getTitle());
                }
                adapter.notifyDataSetChanged();
                dbConn.close();
            }
        };


        dbConn.close();

        getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));
        return newsList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (updateReciever != null) {
            getActivity().unregisterReceiver(updateReciever);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());
        String link = dbConn.getLinkFromNews((String) newsList.getItemAtPosition(position));
        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
}