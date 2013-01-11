package dk.whooper.mobilsiden.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.gson.Gson;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.business.Comment;
import dk.whooper.mobilsiden.business.SuccessLogin;
import dk.whooper.mobilsiden.service.CommentsDownloader;
import dk.whooper.mobilsiden.service.LoginTokenCollector;

import java.util.concurrent.ExecutionException;

public class CommentsViewer extends SherlockActivity {

    private final Activity activity = this;
    private static final String TAG = "WebViewer";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature((int) Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_comments_viewer);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

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
        CommentsDownloader commentsDownloader = new CommentsDownloader();
        try {
            webView.setHorizontalScrollBarEnabled(false);
            webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            String start = "<html><head><meta http-equiv='Content-Type' content='text/html' charset='UTF-8' /></head><body>";
            String end = "</body></html>";
            String result = commentsDownloader.execute(link).get();

            Gson gson = new Gson();
            Comment[] comments = gson.fromJson(result, Comment[].class);

            if (comments.length == 0) { //If no comments to the article
                AlertDialog.Builder noCommentsPopup = new AlertDialog.Builder(this);

                noCommentsPopup.setMessage("Der er endnu ingen kommentarer til denne artikel - Du kan blive den første!");
                noCommentsPopup.setTitle("Ingen kommentarer");
                noCommentsPopup.setPositiveButton("OK", null);
                noCommentsPopup.setCancelable(true);
                noCommentsPopup.create().show();
            } else {
                String html = "<div style=\"font-size: 12px\">";
                for (Comment com : comments) {
                    String date = com.getCreated().split("T")[0] + " " + com.getCreated().split("T")[1].substring(1).split("\\+")[0];
                    html = html + "<p><strong>" + com.getUser() + " - " + com.getTitle() + "</strong><br />" + com.getContent() + "<div style=\"float: right; color: gray;\">" + date + "</div></p><br /><hr>";
                }
                html = html + "</div>";


                webView.loadData(start + html + end, "text/html; charset=UTF-8", null);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SharedPreferences settings = getSharedPreferences("mobilsiden", 0);
        String token = settings.getString("token", null);

        if (token == null) {
            menu.add(Menu.NONE, R.id.addComment, Menu.NONE, "Comment").setIcon(R.drawable.login)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.add(Menu.NONE, R.id.addComment, Menu.NONE, "Comment").setIcon(R.drawable.plus)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }


        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.activity_comments_viewer, menu);
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
            case R.id.addComment:

                SharedPreferences settings = getSharedPreferences("mobilsiden", 0);
                String token = settings.getString("token", null);

                if (token == null) {
                    // custom dialog
                    final Dialog dialog = new Dialog(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Dialog));
                    dialog.setContentView(R.layout.login_popup);
                    dialog.setTitle("Login til Mobilsiden.dk");
                    Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
                    // if button is clicked, close the custom dialog
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Button loginButton = (Button) dialog.findViewById(R.id.btn_login);

                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                LoginTokenCollector tokenCollector = new LoginTokenCollector();
                                EditText username = (EditText) dialog.findViewById(R.id.user_name);
                                EditText password = (EditText) dialog.findViewById(R.id.password);

                                String result = tokenCollector.execute(username.getText().toString(), password.getText().toString()).get();

                                if (result.equals("loginFailed")) {
                                    Toast.makeText(context, "Login forkert - Prøv igen...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Gson gson = new Gson();
                                    SuccessLogin sl = gson.fromJson(result, SuccessLogin.class);

                                    SharedPreferences settings = getSharedPreferences("mobilsiden", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("token", sl.getToken());
                                    editor.commit();
                                    invalidateOptionsMenu();
                                    dialog.dismiss();
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(this, "Allerede logget ind", Toast.LENGTH_SHORT).show();
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
    }
}
