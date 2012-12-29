package dk.whooper.mobilsiden.screens;

import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.R.layout;
import dk.whooper.mobilsiden.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.NavUtils;

public class WebViewer extends Activity {

	private WebView webView;
	private Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_web_viewer);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_viewer);


		String link = getIntent().getExtras().getString("link");
		webView = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				// The progress meter will automatically disappear when we reach 100%
				activity.setProgress(progress * 100);
			}
		});

		webView.setWebViewClient(new Callback());
		webView.loadUrl(link);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web_viewer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}

	}

}
