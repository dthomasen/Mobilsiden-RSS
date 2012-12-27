package dk.whooper.mobilsiden.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import dk.whooper.mobilsiden.business.Item;

import android.content.Context;
import android.util.Log;

public class XMLParser implements Runnable{

	Context context;
	private static final String TAG="XMLParser";
	private static final String ns = null;

	public XMLParser(Context context){
		this.context = context;
	}

	public void run(){
		Log.d(TAG, "Service started");
		try {
			InputStream fIn = context.openFileInput("rss.xml");
			parse(fIn);
		} catch (FileNotFoundException e) {
			Log.d(TAG,"Filenotfound exception");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	public List<Item> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(in, "UTF-8");

			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Item> items = new ArrayList<Item>();

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if(name.equalsIgnoreCase("channel")){
				Log.d(TAG, "Channel tag found");
				items = readChannel(parser, items);
			}
		}  
		return items;
	}

	private List<Item> readChannel(XmlPullParser parser, List<Item> items) throws XmlPullParserException, IOException {
		
		parser.require(XmlPullParser.START_TAG, ns, "channel");
		while(parser.next() != XmlPullParser.END_TAG){
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String name = parser.getName();

			if (name.equals("item")) {
				items.add(readItem(parser));
	        } else {
	            skip(parser);
	        }
		}
		return items;
	}
	
	private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "item");
	    Item currentItem = new Item();
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("title")) {
	            currentItem.setTitle(readTitle(parser));
	        } else if (name.equals("link")){
	        	currentItem.setLink(readLink(parser));
	        } else if (name.equals("comments")){
	        	currentItem.setComments(readComments(parser));
	        } else if (name.equals("description")){
	        	currentItem.setDescription(readDescription(parser));
	        } else if (name.equals("author")){
	        	currentItem.setAuthor(readAuthor(parser));
	        } else if (name.equals("pubDate")){
	        	currentItem.setPubDate(readPubDate(parser));
	        }else {
	        	skip(parser);
	        }
	    }
	    Log.d(TAG,currentItem.toString());
	    return currentItem;
	}
	
	private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "pubDate");
	    String pubDate = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "pubDate");
	    return pubDate;
	}
	
	private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "author");
	    String author = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "author");
	    return author;
	}
	
	private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "description");
	    String description = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "description");
	    return description;
	}
	
	private String readComments(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "comments");
	    String comments = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "comments");
	    return comments;
	}
	
	private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "title");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "title");
	    return title;
	}
	
	private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "link");
	    String link = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "link");
	    return link;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
