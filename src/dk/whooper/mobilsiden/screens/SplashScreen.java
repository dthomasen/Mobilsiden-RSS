package dk.whooper.mobilsiden.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import com.actionbarsherlock.app.SherlockActivity;
import dk.whooper.mobilsiden.R;


public class SplashScreen extends SherlockActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isOnline()) {
            AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog)).create();

            alertDialog.setTitle("Ingen Internet Adgang");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setMessage("Der kunne ikke forbindes til internettet.\n \nEn aktiv internetforbindelse kræves for at benytte denne app.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        } else {

            setContentView(R.layout.splash_screen);
            Handler handler = new Handler();

            // run a thread after 2 seconds to start the home screen
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    // make sure we close the splash screen so the user won't come back when it presses back key

                    finish();
                    // start the home screen

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(intent);
                }

            }, 1000); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}