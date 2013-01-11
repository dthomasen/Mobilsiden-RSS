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

import java.util.List;

public class TipsFragment extends SherlockListFragment {

    private static final String TAG = "TipsFragment";
    private BroadcastReceiver updateReciever;
    private ArticleBaseAdapter adapter;
    private List<Item> tipsItems;
    private static ListView tipsList;
    ProgressDialog progressDialog;

    public TipsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tipsList = new ListView(getActivity());
        tipsList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tipsList.setId(android.R.id.list);
        DatabaseHelper dbConn = new DatabaseHelper(getActivity());

        tipsItems = dbConn.getAllItemsFromTips();

        adapter = new ArticleBaseAdapter(getActivity(), tipsItems);
        tipsList.setAdapter(adapter);

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d(TAG, "tips broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                tipsItems = dbConn.getAllItemsFromTips();
                adapter.setItemArray(tipsItems);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                dbConn.close();
            }
        };

        dbConn.close();

        getActivity().registerReceiver(updateReciever, new IntentFilter("ArticlesUpdated"));

        return tipsList;
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
        Item item = (Item) tipsList.getItemAtPosition(position);

        DatabaseHelper dbConn = new DatabaseHelper(getActivity());
        dbConn.setTipsArticleUnreadStatus(false, item.getTitle());
        dbConn.close();
        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("item", item);

        startActivity(intent);
    }
}