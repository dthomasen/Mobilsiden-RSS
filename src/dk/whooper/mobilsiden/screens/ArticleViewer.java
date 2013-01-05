package dk.whooper.mobilsiden.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.service.ArticleScraper;

import java.util.concurrent.ExecutionException;

public class ArticleViewer extends Activity implements OnClickListener {

    private final Activity activity = this;
    private static final String TAG = "WebViewer";
    private String link;
    private String youtubeLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_article_viewer);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);


        link = getIntent().getExtras().getString("link");
        WebView webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
            }
        });

        webView.setWebViewClient(new Callback());

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getString("user_agent", "Mobil-optimeret").equals("Mobil-optimeret")) {
            ArticleScraper articleScraper = new ArticleScraper();
            ImageButton commentsButton = (ImageButton) findViewById(R.id.commentsButton);
            ImageButton youtube = (ImageButton) findViewById(R.id.youtubeButton);
            commentsButton.setOnClickListener(this);
            youtube.setOnClickListener(this);
            try {
                webView.setHorizontalScrollBarEnabled(false);
                webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                String article = articleScraper.execute(link).get();

                if (article.contains("http://www.youtube.com")) {
                    String[] splitArticle = article.split(" ", 2);
                    youtubeLink = splitArticle[0];
                    article = splitArticle[1];

                    youtube.setVisibility(View.VISIBLE);
                } else {
                    youtube.setVisibility(View.INVISIBLE);
                }

                String start = "<html><head><meta http-equiv='Content-Type' content='text/html' charset='UTF-8' /></head><body>";
                String end = "</body></html>";
                webView.loadData(start + article + end, "text/html; charset=UTF-8", null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1");
            webView.loadUrl(link);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_web_viewer, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commentsButton:
                Intent i = new Intent(this, CommentsViewer.class);
                i.putExtra("link", link);
                startActivity(i);
                break;
            case R.id.youtubeButton:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)));
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


