package dk.whooper.mobilsiden.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import java.io.IOException;
import java.net.MalformedURLException;

public class FacebookWrapper {
    public FacebookWrapper(String appID) {
        super();
        this.appID = appID;
    }


    private String appID;
    private static final String FB_ACCESS_TOKEN = "fb_access_token";
    private static final String FB_ACCESS_EXPIRY = "fb_access_expires";
    private Facebook facebook;


    public Boolean isLoggedIn() {
        if (facebook == null) facebook = new Facebook(appID);
        return (facebook.getAccessExpires() > 0);
    }

    public void discardCredentials(final Context context) {
        if (facebook == null) facebook = new Facebook(appID);

        new Thread(new Runnable() {
            public void run() {

                try {
                    facebook.logout(context);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void login(Context context) {

        if (facebook == null) facebook = new Facebook(appID);
        final SharedPreferences mPrefs = ((Activity) context).getPreferences(context.MODE_PRIVATE);
        String access_token = mPrefs.getString(FB_ACCESS_TOKEN, null);
        long expires = mPrefs.getLong(FB_ACCESS_EXPIRY, 0);
        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {

            facebook.authorize((Activity) context, new String[]{}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(FB_ACCESS_TOKEN, facebook.getAccessToken());
                    editor.putLong(FB_ACCESS_EXPIRY, facebook.getAccessExpires());
                    editor.commit();
                }

                @Override
                public void onFacebookError(FacebookError error) {
                }

                @Override
                public void onError(DialogError e) {
                }

                @Override
                public void onCancel() {
                }
            });
        }

    }

    public void share(final Context context, final String link, final String thumb, final String name, final String description) {

        if (facebook == null) facebook = new Facebook(appID);
        final SharedPreferences mPrefs = ((Activity) context).getPreferences(context.MODE_PRIVATE);
        String access_token = mPrefs.getString(FB_ACCESS_TOKEN, null);
        long expires = mPrefs.getLong(FB_ACCESS_EXPIRY, 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {

            facebook.authorize((Activity) context, new String[]{}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(FB_ACCESS_TOKEN, facebook.getAccessToken());
                    editor.putLong(FB_ACCESS_EXPIRY, facebook.getAccessExpires());
                    editor.commit();
                    doShare(context, link, thumb, name, description);
                }

                @Override
                public void onFacebookError(FacebookError error) {
                }

                @Override
                public void onError(DialogError e) {
                }

                @Override
                public void onCancel() {
                }
            });
        } else {
            doShare(context, link, thumb, name, description);
        }
    }

    private void doShare(Context context, String link, String thumb, String name, String description) {
        Bundle params = new Bundle();
        params.putString("link", link);
        params.putString("picture", thumb);
        params.putString("name", name);
        params.putString("description", description);


        facebook.dialog(context, "feed", params, new DialogListener() {

            @Override
            public void onFacebookError(FacebookError arg0) {
            }

            @Override
            public void onError(DialogError arg0) {
            }

            @Override
            public void onComplete(Bundle arg0) {
            }

            @Override
            public void onCancel() {
            }
        });

        context = null; // release for gc
    }


    public void onComplete(int requestCode, int resultCode, Intent data) {
        if (facebook != null) facebook.authorizeCallback(requestCode, resultCode, data);
    }

}