package dk.whooper.mobilsiden.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.service.CommentsScraper;

import java.util.concurrent.ExecutionException;

public class CommentsViewer extends SherlockActivity {

    private final Activity activity = this;
    private static final String TAG = "WebViewer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature((int) Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_comments_viewer);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_viewer);


        String link = getIntent().getExtras().getString("link");
        WebView webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
            }
        });

        webView.setWebViewClient(new Callback());
        CommentsScraper commentsScraper = new CommentsScraper();
        try {
            webView.setHorizontalScrollBarEnabled(false);
            webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            String start = "<html><head><meta http-equiv='Content-Type' content='text/html' charset='UTF-8' /></head><body>";
            String end = "</body></html>";
            String html = commentsScraper.execute(link).get();
            if (html.equals("")) { //If no comments to the article
                AlertDialog.Builder noCommentsPopup = new AlertDialog.Builder(this);

                noCommentsPopup.setMessage("Der er endnu ingen kommentarer til denne artikel - Du kan blive den første!");
                noCommentsPopup.setTitle("Ingen kommentarer");
                noCommentsPopup.setPositiveButton("OK", null);
                noCommentsPopup.setCancelable(true);
                noCommentsPopup.create().show();
            }

            webView.loadData(start + html + end, "text/html; charset=UTF-8", null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.activity_web_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "URL IS: " + url);
            if (url.contains("mobilsiden.dk")) {
                Log.d(TAG, "Mobilsiden link found");
                Intent i = new Intent(activity, ArticleViewer.class);
                i.putExtra("link", url);
                activity.startActivity(i);
                return true;
            } else {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                return true;
            }
        }
    }
}
