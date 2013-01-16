package dk.whooper.mobilsiden.screens;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.service.ArticleDownloader;
import dk.whooper.mobilsiden.service.DatabaseHelper;
import dk.whooper.mobilsiden.service.SectionsPagerAdapter;

import java.io.Serializable;

public class MainActivity extends SherlockFragmentActivity implements
        ActionBar.TabListener, Serializable {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final String TAG = "MainActivity";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.        /*
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        Intent downloadIntent = new Intent();
        downloadIntent.putExtra("Activity", this);
        ArticleDownloader articleDownloader = new ArticleDownloader();
        articleDownloader.execute(downloadIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_refresh:
                Toast.makeText(this, "Opdaterer artikler...", Toast.LENGTH_LONG).show();
                Intent downloadIntent = new Intent();
                downloadIntent.putExtra("Activity", this);
                ArticleDownloader articleDownloader = new ArticleDownloader();
                articleDownloader.execute(downloadIntent);
                return true;
            case R.id.menu_markasread:
                new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Markér alle som læst")
                        .setMessage("Vil du markere alle artikler som læst?")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper dbConn = new DatabaseHelper(context);
                                dbConn.markAllAsRead();
                                dbConn.close();
                            }

                        })
                        .setNegativeButton("Nej", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

}
