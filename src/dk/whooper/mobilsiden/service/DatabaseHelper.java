package dk.whooper.mobilsiden.service;

import java.util.ArrayList;
import java.util.List;

import dk.whooper.mobilsiden.business.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "mobilsiden";
	private static final String TABLE_NAME = "articles";

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_LINK = "link";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_COMMENTS = "comments";
	private static final String KEY_AUTHOR = "author";	
	private static final String KEY_PUBDATE = "pubDate";	

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MOBILSIDEN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT" + ")";
		db.execSQL(CREATE_MOBILSIDEN_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	// Adding new Item
	public void addItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, item.getTitle());
		values.put(KEY_LINK, item.getLink());
		values.put(KEY_DESCRIPTION, item.getDescription());
		values.put(KEY_COMMENTS, item.getComments());
		values.put(KEY_AUTHOR, item.getAuthor());
		values.put(KEY_PUBDATE, item.getPubDate());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}

	// Getting single Item
	public Item getItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
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
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

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

		// return item list
		return itemList;
	}

	// Getting Items Count
	public int getItemsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
}
