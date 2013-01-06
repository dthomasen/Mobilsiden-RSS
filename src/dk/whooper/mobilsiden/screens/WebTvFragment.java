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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class WebTvFragment extends SherlockListFragment {

    private static final String TAG = "WebTVFragment";
    private BroadcastReceiver updateReciever;
    private ArrayAdapter<String> adapter;
    private List<Item> webtvItems;
    private ArrayList<String> webtvHeadlines;
    private static ListView webtvList;
    ProgressDialog progressDialog;

    public WebTvFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        webtvList = new ListView(getActivity());
        webtvList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        webtvList.setId(android.R.id.list);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());

        webtvItems = dbConn.getAllItemsFromWebTv();
        webtvHeadlines = new ArrayList<String>();
        for (Item i : webtvItems) {
            webtvHeadlines.add(i.getTitle());
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, webtvHeadlines);
        webtvList.setAdapter(adapter);

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d(TAG, "WebTV broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                webtvItems = dbConn.getAllItemsFromWebTv();
                for (Item i : webtvItems) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (updateReciever != null) {
            getActivity().unregisterReceiver(updateReciever);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        progressDialog = ProgressDialog.show(getActivity(), "Vent venligst", "Henter artiklen...");
        super.onListItemClick(l, v, position, id);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());
        String link = dbConn.getLinkFromWebTv((String) webtvList.getItemAtPosition(position));

        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
}