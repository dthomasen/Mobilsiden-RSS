package dk.whooper.mobilsiden.service;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class CommentsScraper extends AsyncTask<String, Void, String>{
	private static final String TAG = "CommentsScraper";
	private String html;

	@Override
	protected String doInBackground(String... params) {
		Document eventInfo;
		Boolean color = false;
		try {
			Document document = Jsoup.parse(new URL(params[0]).openStream(), "ISO-8859-1", params[0]);
			for (Element element : document.select("*")) {
				if (!element.hasText() && element.isBlock()) {
					element.remove();
				}
				if(element.className().equals("far_left")){
					element.prepend("<br />");
				}
				if(element.className().equals("paragraph_banner")){
					element.remove();
				}
				if(element.id().equals("cmt_titles")){
					element.remove();
				}
				if(element.id().equals("editbox")){
					element.remove();
				}
				if(element.attr("style").equals("float: left; width: 49%; text-align: right;")){
					element.html("<br /><br />");
				}
				
			}
			Elements comments = document.select("div#tabs2");
			html = comments.html();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return html;
	}

	protected void onPostExecute(String result){


	}
}
