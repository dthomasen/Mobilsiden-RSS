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
                    //String oldHeader = element.html();
                    // String oldHeaderWithoutDoubleSpace = oldHeader.trim().replaceAll(" +", " ");
                    String newHTML = "<h1 style=\"font-size: 15px;\">" + element.text() + "</h1>";
                    element.html(newHTML);
                }

                if (element.className().equals("entry_body")) {
                    String newHTML = "<p style=\"font-weight: bold; font-size: 13px;\">" + element.text() + "</p>";
                    element.html(newHTML);
                }

                if (element.className().equals("article_author")) {
                    Elements elements = element.getAllElements();

                    for (Element e : elements) {
                        if (e.tagName().equals("a")) {
                            e.remove();
                        }
                    }

                    String newHTML = "<div style=\"text-decoration: underline; font-size: 13px;\">" + element.text().replace(",", " ") + "</div>";
                    element.html(newHTML);
                }

                if (element.tagName().equals("p")) {
                    if (!element.className().equals("entry_body")) {
                        String newHTML = "<div style=\"font-size: 13px;\">" + element.html() + "</div>";
                        element.html(newHTML);
                    }
                }

                if (element.className().equals("image_list")) {
                    for (Element e : element.getAllElements()) {
                        if (e.className().equals("caption")) {
                            String newHTML = "<div style=\"font-size: 13px; padding:3px; color:#888;\">" + element.text() + "</div>";
                            e.html(newHTML);
                        }
                    }
                    String newHTML = "<div style=\"border-style:solid; border-width:1px; border-color:#dcdcdc; padding:3px;\">" + element.html() + "</div>";
                    element.html(newHTML);
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
