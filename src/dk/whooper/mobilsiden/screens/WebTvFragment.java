package dk.whooper.mobilsiden.screens;

import android.R;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import dk.whooper.mobilsiden.business.Article;
import dk.whooper.mobilsiden.service.ArticleBaseAdapter;
import dk.whooper.mobilsiden.service.DatabaseHelper;

import java.util.List;

public class WebTvFragment extends SherlockListFragment {

    private static final String TAG = "WebTVFragment";
    private BroadcastReceiver updateReciever;
    private ArticleBaseAdapter adapter;
    private List<Article> webtvItems;
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

        adapter = new ArticleBaseAdapter(getActivity(), webtvItems);
        webtvList.setAdapter(adapter);

        updateReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d(TAG, "WebTV broadcast recieved");
                DatabaseHelper dbConn = new DatabaseHelper(getActivity());
                webtvItems = dbConn.getAllItemsFromWebTv();
                adapter.setItemArray(webtvItems);
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
        progressDialog = ProgressDialog.show(new ContextThemeWrapper(getActivity(), R.style.Theme_Holo_Dialog), "Vent venligst", "Henter artiklen...");
        super.onListItemClick(l, v, position, id);
        Article article = (Article) webtvList.getItemAtPosition(position);

        DatabaseHelper dbConn = new DatabaseHelper(getActivity());
        dbConn.setWebTVArticleUnreadStatus(false, article.getHeader());
        dbConn.close();
        Intent intent = new Intent(getActivity(), ArticleViewer.class);
        intent.putExtra("article", article);

        startActivity(intent);
    }
}