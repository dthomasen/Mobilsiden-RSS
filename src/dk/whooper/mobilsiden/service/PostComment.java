package dk.whooper.mobilsiden.service;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PostComment extends AsyncTask<String, Void, String> {
    private static final String TAG = "PostComment";
    private String result;


    //Parameters:
    //1. Article ID
    //2. Access token
    //3. Title
    //4. Message
    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();

        String resURL = "";

        //Resolving redirect address
        try {
            URLConnection con = new URL(params[0]).openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            resURL = String.valueOf(con.getURL());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String id = resURL.split("lid.")[1].split("/")[0];

        // In a POST request, we don't pass the values in the URL.
        //Therefore we use only the web page URL as the parameter of the HttpPost argument
        HttpPost httpPost = new HttpPost("http://www.mobilsiden.dk/services/json/comments.php?key=5535106688a0a103926ff1e3841e8335&token=" + params[1] + "&action=create");

        // Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
        //uniquely separate by the other end.
        //To achieve that we use BasicNameValuePair
        //Things we need to pass with the POST request
        BasicNameValuePair articleIdBasicNameValuePair = new BasicNameValuePair("comment[articleId]", id);
        BasicNameValuePair commentTitleBasicNameValuePAir = new BasicNameValuePair("comment[title]", params[2]);
        BasicNameValuePair commentContentBasicNameValuePAir = new BasicNameValuePair("comment[content]", params[3]);

        // We add the content that we want to pass with the POST request to as name-value pairs
        //Now we put those sending details to an ArrayList with type safe of NameValuePair
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(articleIdBasicNameValuePair);
        nameValuePairList.add(commentTitleBasicNameValuePAir);
        nameValuePairList.add(commentContentBasicNameValuePAir);

        try {
            // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
            //This is typically useful while sending an HTTP POST request.
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

            // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
            httpPost.setEntity(urlEncodedFormEntity);

            // HttpResponse is an interface just like HttpPost.
            //Therefore we can't initialize them
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // According to the JAVA API, InputStream constructor do nothing.
            //So we can't initialize InputStream although it is not an interface
            InputStream inputStream = httpResponse.getEntity().getContent();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            String bufferedStrChunk = null;

            while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                stringBuilder.append(bufferedStrChunk);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;

    }

    protected void onPostExecute(String result) {

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
