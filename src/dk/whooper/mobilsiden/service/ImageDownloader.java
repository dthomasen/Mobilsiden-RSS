package dk.whooper.mobilsiden.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

    private static final String TAG = "ImageDownloader";
    private Bitmap image;

    @Override
    protected void onPreExecute() {
        Log.i("ImageLoadTask", "Loading image...");
    }

    // param[0] is img url
    protected Bitmap doInBackground(String... params) {
        Log.i("ImageLoadTask", "Attempting to load image URL: " + params[0]);
        try {
            Bitmap b = getBitmapFromURL(params[0]);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onProgressUpdate(String... progress) {
        // NO OP
    }

    protected void onPostExecute(Bitmap ret) {

    }
}