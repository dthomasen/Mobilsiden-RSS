package dk.whooper.mobilsiden.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.business.Article;
import dk.whooper.mobilsiden.service.FacebookWrapper;

public class ArticleViewer extends SherlockActivity {

    private final Activity activity = this;
    private static final String TAG = "WebViewer";
    private Article article;
    private String description;
    private String youtubeLink = "";
    private String title;
    private String link;
    private String content;
    private FacebookWrapper fbWrapper = new FacebookWrapper("487027081336289");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature((int) Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_article_viewer);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);

        article = (Article) getIntent().getExtras().getSerializable("article");

        if (article != null) {
            description = "";
            title = article.getHeader();
            content = article.getBodytext();
            link = article.getUrl();
        } else {
            link = getIntent().getStringExtra("link");
        }

        WebView webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
            }
        });

        webView.setWebViewClient(new Callback());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        String start = "<html><head><meta http-equiv='Content-Type' content='text/html' charset='UTF-8' /></head><body>";
        String end = "</body></html>";
        webView.loadData(start + content + end, "text/html; charset=UTF-8", null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbWrapper.onComplete(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (youtubeLink != "") {
            menu.add(Menu.NONE, R.id.youtubeButtonBar, Menu.NONE, "Youtube").setIcon(R.drawable.youtubeicon)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        menu.add(Menu.NONE, R.id.commentsButtonBar, Menu.NONE, "Comments").setIcon(R.drawable.commentsicon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(Menu.NONE, R.id.facebookButtonBar, Menu.NONE, "Facebook").setIcon(R.drawable.facebook_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
            case R.id.youtubeButtonBar:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)));
                return true;
            case R.id.commentsButtonBar:
                Intent intent = new Intent(this, CommentsViewer.class);
                intent.putExtra("link", link);
                startActivity(intent);
                return true;
            case R.id.facebookButtonBar:
                Log.d(TAG, "Facebook button presed");
                if (title != null && description != null) {
                    fbWrapper.share(this, link, "", title, description);
                } else {
                    fbWrapper.share(this, link, "", "", "");
                }
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

        // here you execute an action when the URL you want is about to load
        @Override
        public void onLoadResource(WebView view, String url) {

        }
    }
}


