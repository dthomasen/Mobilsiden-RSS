package dk.whooper.mobilsiden.service;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class ArticleScraper extends AsyncTask<String, Void, String> {

    private static final String TAG = "ArticleScraper";
    private String html;
    private String youtubeLink;

    @Override
    protected String doInBackground(String... params) {
        try {
            Document document = Jsoup.parse(new URL(params[0]).openStream(), "ISO-8859-1", params[0]);
            for (Element element : document.select("*")) {
                if (element.className().equals("paragraph_banner")) {
                    element.remove();
                }
                if (element.className().equals("controls")) {
                    element.remove();
                }
                if (element.className().equals("category_list")) {
                    element.remove();
                }
                if (element.className().equals("category_list_title")) {
                    element.remove();
                }
                if (element.className().equals("category_list debat")) {
                    element.remove();
                }
                if (element.className().equals("addthis_toolbox addthis_default_style")) {
                    element.remove();
                }
                if (element.className().equals("headline")) {
                    element.remove();
                }
                if (element.className().equals("far_top")) {
                    element.remove();
                }

                if (element.tagName().equals("h1")) {
                    String oldHeader = element.html();
                    String oldHeaderWithoutDoubleSpace = oldHeader.trim().replaceAll(" +", " ");
                    element.html(oldHeaderWithoutDoubleSpace);
                }
                if (element.tagName().equals("iframe")) {
                    Boolean youtubeFound = false;
                    if (element.attr("src").contains("youtube")) {
                        youtubeLink = element.attr("src");
                        youtubeFound = true;
                    }
                    if (youtubeFound) {
                        element.remove();
                    }
                }
            }
            Elements articleContainer = document.select("div.article");
            if (youtubeLink != null) {
                articleContainer.prepend(youtubeLink);
            }
            articleContainer.append("<br /><br /><br />");
            Log.d(TAG, articleContainer.html());
            html = articleContainer.html();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html;
    }

    protected void onPostExecute(String result) {


    }


}
