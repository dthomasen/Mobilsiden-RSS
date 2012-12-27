package dk.whooper.mobilsiden.service;

import java.util.ArrayList;
import java.util.List;

import dk.whooper.mobilsiden.business.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	SQLiteDatabase db;
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "mobilsiden";

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_LINK = "link";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_COMMENTS = "comments";
	private static final String KEY_AUTHOR = "author";	
	private static final String KEY_PUBDATE = "pubDate";	

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("NewsDatase", "OnCreate");
		String CREATE_NEWS_TABLE = "CREATE TABLE news ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT" + ")";
		String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT" + ")";
		String CREATE_WEBTV_TABLE = "CREATE TABLE webtv ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT" + ")";
		
		db.execSQL(CREATE_NEWS_TABLE);
		db.execSQL(CREATE_REVIEWS_TABLE);
		db.execSQL(CREATE_WEBTV_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS reviews");
		db.execSQL("DROP TABLE IF EXISTS webtv");
		// Create tables again
		onCreate(db);
	}

	public void addItemToNewsDB(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, item.getTitle());
		values.put(KEY_LINK, item.getLink());
		values.put(KEY_DESCRIPTION, item.getDescription());
		values.put(KEY_COMMENTS, item.getComments());
		values.put(KEY_AUTHOR, item.getAuthor());
		values.put(KEY_PUBDATE, item.getPubDate());

		// Inserting Row
		db.insert("news", null, values);
		db.close(); // Closing database connection
	}
	
		public void addItemToReviewsDB(Item item) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_TITLE, item.getTitle());
			values.put(KEY_LINK, item.getLink());
			values.put(KEY_DESCRIPTION, item.getDescription());
			values.put(KEY_COMMENTS, item.getComments());
			values.put(KEY_AUTHOR, item.getAuthor());
			values.put(KEY_PUBDATE, item.getPubDate());

			// Inserting Row
			db.insert("reviews", null, values);
			db.close(); // Closing database connection
		}
		
		public void addItemToWebTVDB(Item item) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_TITLE, item.getTitle());
			values.put(KEY_LINK, item.getLink());
			values.put(KEY_DESCRIPTION, item.getDescription());
			values.put(KEY_COMMENTS, item.getComments());
			values.put(KEY_AUTHOR, item.getAuthor());
			values.put(KEY_PUBDATE, item.getPubDate());

			// Inserting Row
			db.insert("webtv", null, values);
			db.close(); // Closing database connection
		}

	public Item getItemFromNewsDb(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("news", new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_DESCRIPTION, KEY_COMMENTS, KEY_AUTHOR, KEY_PUBDATE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		return item;
	}
	
	public Item getItemFromReviewsDb(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("reviews", new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_DESCRIPTION, KEY_COMMENTS, KEY_AUTHOR, KEY_PUBDATE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		return item;
	}
	
	public Item getItemFromWebTVDb(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("webtv", new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_DESCRIPTION, KEY_COMMENTS, KEY_AUTHOR, KEY_PUBDATE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		return item;
	}

	// Getting All Items
	public List<Item> getAllItems() {
		List<Item> itemList = new ArrayList<Item>();
		String selectQuery = "SELECT  * FROM " + "news";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item item = new Item();
				item.setTitle(cursor.getString(1));
				item.setLink(cursor.getString(2));
				item.setDescription(cursor.getString(3));
				item.setComments(cursor.getString(4));
				item.setAuthor(cursor.getString(5));
				item.setPubDate(cursor.getString(6));

				// Adding item to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		
		selectQuery = "SELECT  * FROM " + "reviews";

		cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item item = new Item();
				item.setTitle(cursor.getString(1));
				item.setLink(cursor.getString(2));
				item.setDescription(cursor.getString(3));
				item.setComments(cursor.getString(4));
				item.setAuthor(cursor.getString(5));
				item.setPubDate(cursor.getString(6));

				// Adding item to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		
		selectQuery = "SELECT  * FROM " + "webtv";

		cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item item = new Item();
				item.setTitle(cursor.getString(1));
				item.setLink(cursor.getString(2));
				item.setDescription(cursor.getString(3));
				item.setComments(cursor.getString(4));
				item.setAuthor(cursor.getString(5));
				item.setPubDate(cursor.getString(6));

				// Adding item to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}

		// return item list
		return itemList;
	}

	// Getting Items Count
	public int getItemsCount() {
		String countQuery = "SELECT  * FROM " + "news";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		String countQuery2 = "SELECT  * FROM " + "reviews";
		Cursor cursor2 = db.rawQuery(countQuery, null);
		cursor2.close();

		String countQuery3 = "SELECT  * FROM " + "webtv";
		Cursor cursor3 = db.rawQuery(countQuery, null);
		cursor2.close();
		// return count
		return cursor.getCount()+cursor2.getCount()+cursor3.getCount();
	}
}
