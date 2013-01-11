package dk.whooper.mobilsiden.screens;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.ArticleBaseAdapter;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NyhederFragment extends SherlockListFragment {
    private static final String TAG = "NyhederFragment";
    private List<Item> newsItems;
    private ArrayList newsHeadlines;
    private ArticleBaseAdapter adapter;
    private BroadcastReceiver updateReciever;
    private ListView newsList;
    ProgressDialog progressDialog;

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

        adapter = new ArticleBaseAdapter(getActivity(), newsItems);

        newsList.setAdapter(adapter);

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Nyheder broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                newsItems = dbConn.getAllItemsFromNews();
                adapter.setItemArray(newsItems);
                adapter.notifyDataSetChanged();
                dbConn.close();
            }
        };


        dbConn.close();

        getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));
        return newsList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
        progressDialog = ProgressDialog.show(getActivity(), "Vent venligst", "Henter artiklen...");
        super.onListItemClick(l, v, position, id);
        Item item = (Item) newsList.getItemAtPosition(position);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());
        dbConn.setNewsArticleUnreadStatus(false, item.getTitle());
        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("item", item);

        startActivity(intent);
    }
}