package dk.whooper.mobilsiden.service;

import android.os.AsyncTask;
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

public class ArticleDownloaderById extends AsyncTask<String, Void, Article> {

    private static final String TAG = "ArticleDownloader";
    private String result;
    private String articleId;
    private String articleJson;

    //Input 1: article id
    @Override
    protected Article doInBackground(String... params) {
        articleId = params[0];
        articleJson = downloadArticleJson(articleId);

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

        Article article = gson.fromJson(articleJson, Article.class);
        return article;
    }

    public String downloadArticleJson(String id) {

        HttpClient httpClient = new DefaultHttpClient();

        String url = "http://www.mobilsiden.dk/services/json/articles.php?key=5535106688a0a103926ff1e3841e8335&article[id]=" + id + "&action=read";

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
