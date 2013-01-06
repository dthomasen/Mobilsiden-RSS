package dk.whooper.mobilsiden.screens;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dk.whooper.mobilsiden.business.Item;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AnmeldelserFragment extends ListFragment {

    private static final String TAG = "AnmeldelserFragment";
    private BroadcastReceiver updateReciever;
    private ArrayAdapter adapter;
    private List<Item> reviewsItems;
    private ArrayList reviewsHeadlines;
    private ListView reviewsList;
    ProgressDialog progressDialog;

    public AnmeldelserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reviewsList = new ListView(getActivity());
        reviewsList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        reviewsList.setId(android.R.id.list);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());

        reviewsItems = dbConn.getAllItemsFromReviews();
        reviewsHeadlines = new ArrayList();
        for (Item i : reviewsItems) {
            reviewsHeadlines.add(i.getTitle());
        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, reviewsHeadlines);

        setListAdapter(adapter);

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d(TAG, "Anmeldelser broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                reviewsItems = dbConn.getAllItemsFromReviews();
                for (Item i : reviewsItems) {
                    reviewsHeadlines.add(i.getTitle());
                }
                adapter.notifyDataSetChanged();
                dbConn.close();
            }
        };

        dbConn.close();

        getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));

        return reviewsList;
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
        String link = dbConn.getLinkFromReviews((String) reviewsList.getItemAtPosition(position));

        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
}