package dk.whooper.mobilsiden.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
    private static final String KEY_UNREAD = "unread";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("NewsDatase", "OnCreate");
        String CREATE_NEWS_TABLE = "CREATE TABLE news ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_WEBTV_TABLE = "CREATE TABLE webtv ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_TIPS_TABLE = "CREATE TABLE tips ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_PUBDATE + " TEXT," + KEY_UNREAD + " BOOL)";

        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
        db.execSQL(CREATE_WEBTV_TABLE);
        db.execSQL(CREATE_TIPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS reviews");
        db.execSQL("DROP TABLE IF EXISTS webtv");
        db.execSQL("DROP TABLE IF EXISTS tips");
        // Create tables again
        onCreate(db);
    }

    public void addItemToNewsDB(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM news WHERE " + KEY_PUBDATE + "= '" + item.getPubDate() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, item.getTitle());
            values.put(KEY_LINK, item.getLink());
            values.put(KEY_DESCRIPTION, item.getDescription());
            values.put(KEY_COMMENTS, item.getComments());
            values.put(KEY_AUTHOR, item.getAuthor());
            values.put(KEY_PUBDATE, item.getPubDate());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("news", null, values);
        }

        db.close(); // Closing database connection
    }

    public void addItemToTipsDB(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tips WHERE " + KEY_PUBDATE + "= '" + item.getPubDate() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, item.getTitle());
            values.put(KEY_LINK, item.getLink());
            values.put(KEY_DESCRIPTION, item.getDescription());
            values.put(KEY_COMMENTS, item.getComments());
            values.put(KEY_AUTHOR, item.getAuthor());
            values.put(KEY_PUBDATE, item.getPubDate());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("tips", null, values);
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
            values.put(KEY_UNREAD, true);

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
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("webtv", null, values);
        }
        db.close(); // Closing database connection
    }

    public List<Item> getAllItemsFromNews() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "news ORDER BY " + KEY_PUBDATE + " asc";

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
                item.setUnread(cursor.getInt(7) != 0);

                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;
    }

    public List<Item> getAllItemsFromReviews() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "reviews ORDER BY " + KEY_PUBDATE + " asc";

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
                item.setUnread(cursor.getInt(7) != 0);

                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;
    }

    public List<Item> getAllItemsFromWebTv() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "webtv ORDER BY " + KEY_PUBDATE + " asc";

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
                item.setUnread(cursor.getInt(7) != 0);

                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;
    }

    public List<Item> getAllItemsFromTips() {
        List<Item> itemList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + "tips ORDER BY " + KEY_PUBDATE + " asc";

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
                item.setUnread(cursor.getInt(7) != 0);

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

    public void setNewsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_TITLE + "=?";
        String[] whereArgs = new String[]{title};

        db.update("news", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setTipsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_TITLE + "=?";
        String[] whereArgs = new String[]{title};

        db.update("tips", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setReviewsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_TITLE + "=?";
        String[] whereArgs = new String[]{title};

        db.update("reviews", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setWebTVArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_TITLE + "=?";
        String[] whereArgs = new String[]{title};

        db.update("webtv", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void markAllAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, false);

        db.update("webtv", dataToInsert, null, null);
        db.update("reviews", dataToInsert, null, null);
        db.update("tips", dataToInsert, null, null);
        db.update("news", dataToInsert, null, null);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }
}
