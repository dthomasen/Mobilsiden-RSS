package dk.whooper.mobilsiden.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.whooper.mobilsiden.business.Article;
import dk.whooper.mobilsiden.business.Tags;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ArticleDownloader extends AsyncTask<Intent, Void, Intent> {

    private static final String TAG = "ArticleDownloader";
    private String result;
    private Activity parentActivity;

    //Input 1: category ID
    //Input 2: Date from
    @Override
    protected Intent doInBackground(Intent... params) {

        parentActivity = (Activity) params[0].getExtras().getSerializable("Activity");

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = dateFormatter.format(today);

        c.setTime(today);
        c.add(Calendar.DAY_OF_YEAR, -7);
        Date dateFiveDaysAgo = c.getTime();
        String dateFiveDaysAgoF = dateFormatter.format(dateFiveDaysAgo);

        SharedPreferences settings = parentActivity.getSharedPreferences("mobilsiden", 0);
        String fromDate = settings.getString("lastUpdatedDate", dateFiveDaysAgoF);

        String newsJson = downloadArticleJson("1", fromDate);
        String reviewsJson = downloadArticleJson("19", fromDate);
        String webTvJson = downloadArticleJson("35", fromDate);
        String tipsJson = downloadArticleJson("7", fromDate);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("news", newsJson);
        returnIntent.putExtra("reviews", reviewsJson);
        returnIntent.putExtra("webtv", webTvJson);
        returnIntent.putExtra("tips", tipsJson);


        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastUpdatedDate", formattedDate);
        editor.commit();

        return returnIntent;
    }

    public String downloadArticleJson(String category, String fromDate) {

        HttpClient httpClient = new DefaultHttpClient();

        Log.d(TAG, "FromDate: " + fromDate);
        String url = "http://www.mobilsiden.dk/services/json/articles.php?key=5535106688a0a103926ff1e3841e8335&category=" + category + "&from=" + fromDate;

        HttpGet request = new HttpGet(url);
        HttpResponse webServerResponse = null;

        try {
            webServerResponse = httpClient.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity httpEntity = webServerResponse.getEntity();

        if (httpEntity != null) {
            InputStream inStream;
            try {
                inStream = httpEntity.getContent();
                result = convertStreamToString(inStream);
                inStream.close();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Intent intent) {
        super.onPostExecute(intent);
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                if (field.getName().equals("tags")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz.equals(Tags.class);
            }
        }).create();

        Article[] news = gson.fromJson(intent.getExtras().getString("news"), Article[].class);
        Article[] reviews = gson.fromJson(intent.getExtras().getString("reviews"), Article[].class);
        Article[] webtv = gson.fromJson(intent.getExtras().getString("webtv"), Article[].class);
        Article[] tips = gson.fromJson(intent.getExtras().getString("tips"), Article[].class);

        DatabaseHelper dbConn = new DatabaseHelper(parentActivity);
        for (Article i : news) {
            dbConn.addItemToNewsDB(i);
        }

        for (Article i : reviews) {
            dbConn.addItemToReviewsDB(i);
        }

        for (Article i : webtv) {
            dbConn.addItemToWebTVDB(i);
        }

        for (Article i : tips) {
            dbConn.addItemToTipsDB(i);
        }

        dbConn.close();

        Intent broad = new Intent("ArticlesUpdated");
        parentActivity.sendBroadcast(broad);
    }

    public String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }


}
