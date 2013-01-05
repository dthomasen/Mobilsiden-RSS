package dk.whooper.mobilsiden.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import dk.whooper.mobilsiden.business.Item;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

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

        Cursor cursor = db.rawQuery("SELECT * FROM news WHERE " + KEY_PUBDATE + "= '" + item.getPubDate() + "'", null);
        if (cursor.getCount() == 0) {
            Log.d("DBTEST", "Cursor is null");
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, item.getTitle());
            values.put(KEY_LINK, item.getLink());
            values.put(KEY_DESCRIPTION, item.getDescription());
            values.put(KEY_COMMENTS, item.getComments());
            values.put(KEY_AUTHOR, item.getAuthor());
            values.put(KEY_PUBDATE, item.getPubDate());

            // Inserting Row
            db.insert("news", null, values);
        }

        db.close(); // Closing database connection
    }

    public void addItemToReviewsDB(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM reviews WHERE " + KEY_PUBDATE + "= '" + item.getPubDate() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, item.getTitle());
            values.put(KEY_LINK, item.getLink());
            values.put(KEY_DESCRIPTION, item.getDescription());
            values.put(KEY_COMMENTS, item.getComments());
            values.put(KEY_AUTHOR, item.getAuthor());
            values.put(KEY_PUBDATE, item.getPubDate());

            // Inserting Row
            db.insert("reviews", null, values);
        }
        db.close(); // Closing database connection
    }

    public void addItemToWebTVDB(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM webtv WHERE " + KEY_PUBDATE + "= '" + item.getPubDate() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, item.getTitle());
            values.put(KEY_LINK, item.getLink());
            values.put(KEY_DESCRIPTION, item.getDescription());
            values.put(KEY_COMMENTS, item.getComments());
            values.put(KEY_AUTHOR, item.getAuthor());
            values.put(KEY_PUBDATE, item.getPubDate());

            // Inserting Row
            db.insert("webtv", null, values);
        }
        db.close(); // Closing database connection
    }

    public List<Item> getAllItemsFromNews() {
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
        return itemList;
    }

    public List<Item> getAllItemsFromReviews() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "reviews";

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
        return itemList;
    }

    public List<Item> getAllItemsFromWebTv() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "webtv";

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
        return itemList;
    }

    public String getLinkFromWebTv(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_COMMENTS + " FROM webtv WHERE " + KEY_TITLE + "= '" + title + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String commentURL = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));
                commentURL = commentURL.replaceAll("#comment_box", "");
                db.close();
                return commentURL;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getLinkFromNews(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_COMMENTS + " FROM news WHERE " + KEY_TITLE + "= '" + title + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String commentURL = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));
                commentURL = commentURL.replaceAll("#comment_box", "");
                db.close();
                return commentURL;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getLinkFromReviews(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_COMMENTS + " FROM reviews WHERE " + KEY_TITLE + "= '" + title + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String commentURL = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));
                commentURL = commentURL.replaceAll("#comment_box", "");
                db.close();
                return commentURL;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // Getting Items Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + "news";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        String countQuery2 = "SELECT  * FROM " + "reviews";
        Cursor cursor2 = db.rawQuery(countQuery2, null);
        cursor2.close();

        String countQuery3 = "SELECT  * FROM " + "webtv";
        Cursor cursor3 = db.rawQuery(countQuery3, null);
        cursor2.close();
        // return count
        return cursor.getCount() + cursor2.getCount() + cursor3.getCount();
    }

}
