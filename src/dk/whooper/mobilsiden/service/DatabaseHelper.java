package dk.whooper.mobilsiden.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import dk.whooper.mobilsiden.business.Article;
import dk.whooper.mobilsiden.business.Images;
import dk.whooper.mobilsiden.business.Videos;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "mobilsiden";

    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_VIEWS = "views";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_PUBLISHED = "published";
    private static final String KEY_HEADER = "header";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_BODYTEXT = "bodytext";
    private static final String KEY_URL = "url";
    private static final String KEY_UNREAD = "unread";

    private static final String KEY_IMGID = "imgId";
    private static final String KEY_IMGTHUMBURL = "imgThumburl";
    private static final String KEY_IMGURL = "imgUrl";
    private static final String KEY_IMGTITLE = "imgTitle";

    private static final String KEY_VIDID = "vidId";
    private static final String KEY_VIDURLIMAGE = "vidUrlImage";
    private static final String KEY_VIDURL = "vidUrl";
    private static final String KEY_VIDTITLE = "vidTitle";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("NewsDatase", "OnCreate");
        String CREATE_NEWS_TABLE = "CREATE TABLE news ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT,"
                + KEY_VIEWS + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_PUBLISHED + " TEXT," + KEY_HEADER + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_BODYTEXT + " TEXT," + KEY_URL + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT,"
                + KEY_VIEWS + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_PUBLISHED + " TEXT," + KEY_HEADER + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_BODYTEXT + " TEXT," + KEY_URL + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_WEBTV_TABLE = "CREATE TABLE webtv ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT,"
                + KEY_VIEWS + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_PUBLISHED + " TEXT," + KEY_HEADER + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_BODYTEXT + " TEXT," + KEY_URL + " TEXT," + KEY_UNREAD + " BOOL)";
        String CREATE_TIPS_TABLE = "CREATE TABLE tips ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT,"
                + KEY_VIEWS + " TEXT," + KEY_COMMENTS + " TEXT," + KEY_PUBLISHED + " TEXT," + KEY_HEADER + " TEXT," + KEY_AUTHOR + " TEXT," + KEY_BODYTEXT + " TEXT," + KEY_URL + " TEXT," + KEY_UNREAD + " BOOL)";

        String CREATE_IMAGES_TABLE = "CREATE TABLE images ("
                + KEY_IMGID + " INTEGER," + KEY_IMGTITLE + " TEXT,"
                + KEY_IMGTHUMBURL + " TEXT," + KEY_IMGURL + " TEXT)";

        String CREATE_VIDEOS_TABLE = "CREATE TABLE videos ("
                + KEY_VIDID + " INTEGER," + KEY_VIDTITLE + " TEXT,"
                + KEY_VIDURLIMAGE + " TEXT," + KEY_VIDURL + " TEXT)";

        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
        db.execSQL(CREATE_WEBTV_TABLE);
        db.execSQL(CREATE_TIPS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS reviews");
        db.execSQL("DROP TABLE IF EXISTS webtv");
        db.execSQL("DROP TABLE IF EXISTS tips");
        db.execSQL("DROP TABLE IF EXISTS images");
        db.execSQL("DROP TABLE IF EXISTS videos");
        // Create tables again
        onCreate(db);
    }

    public void addItemToVideosDB(Videos video, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VIDID, id);
        values.put(KEY_VIDTITLE, video.getTitle());
        values.put(KEY_VIDURLIMAGE, video.getUrlImage());
        values.put(KEY_VIDURL, video.getUrl());

        // Inserting Row
        db.insert("videos", null, values);

        db.close(); // Closing database connection
    }


    public void addItemToImagesDB(Images image, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMGID, id);
        values.put(KEY_IMGTITLE, image.getTitle());
        values.put(KEY_IMGTHUMBURL, image.getThumbUrl());
        values.put(KEY_IMGURL, image.getUrl());

        // Inserting Row
        db.insert("images", null, values);

        db.close(); // Closing database connection
    }

    public void addItemToNewsDB(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM news WHERE " + KEY_ID + "= '" + article.getId() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, article.getId());
            values.put(KEY_CATEGORY, article.getCategory());
            values.put(KEY_VIEWS, article.getViews().toString());
            values.put(KEY_COMMENTS, article.getComments());
            values.put(KEY_PUBLISHED, article.getPublished());
            values.put(KEY_HEADER, article.getHeader());
            values.put(KEY_AUTHOR, article.getAuthor());
            values.put(KEY_BODYTEXT, article.getBodytext());
            values.put(KEY_URL, article.getUrl());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("news", null, values);
            for (int i = 0; i < article.getImages().size(); i++) {
                addItemToImagesDB(article.getImages().get(i), article.getId());
            }
            for (int i = 0; i < article.getVideos().size(); i++) {
                addItemToVideosDB(article.getVideos().get(i), article.getId());
            }
        }

        db.close(); // Closing database connection
    }

    public void addItemToTipsDB(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tips WHERE " + KEY_ID + "= '" + article.getId() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, article.getId());
            values.put(KEY_CATEGORY, article.getCategory());
            values.put(KEY_VIEWS, article.getViews().toString());
            values.put(KEY_COMMENTS, article.getComments());
            values.put(KEY_PUBLISHED, article.getPublished());
            values.put(KEY_HEADER, article.getHeader());
            values.put(KEY_AUTHOR, article.getAuthor());
            values.put(KEY_BODYTEXT, article.getBodytext());
            values.put(KEY_URL, article.getUrl());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("tips", null, values);
            for (int i = 0; i < article.getImages().size(); i++) {
                addItemToImagesDB((Images) article.getImages().get(i), article.getId());
            }
            for (int i = 0; i < article.getVideos().size(); i++) {
                addItemToVideosDB(article.getVideos().get(i), article.getId());
            }
        }
        db.close(); // Closing database connection
    }

    public void addItemToReviewsDB(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM reviews WHERE " + KEY_ID + "= '" + article.getId() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, article.getId());
            values.put(KEY_CATEGORY, article.getCategory());
            values.put(KEY_VIEWS, article.getViews().toString());
            values.put(KEY_COMMENTS, article.getComments());
            values.put(KEY_PUBLISHED, article.getPublished());
            values.put(KEY_HEADER, article.getHeader());
            values.put(KEY_AUTHOR, article.getAuthor());
            values.put(KEY_BODYTEXT, article.getBodytext());
            values.put(KEY_URL, article.getUrl());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("reviews", null, values);
            for (int i = 0; i < article.getImages().size(); i++) {
                addItemToImagesDB((Images) article.getImages().get(i), article.getId());
            }
            for (int i = 0; i < article.getVideos().size(); i++) {
                addItemToVideosDB(article.getVideos().get(i), article.getId());
            }
        }

        db.close(); // Closing database connection
    }

    public void addItemToWebTVDB(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM webtv WHERE " + KEY_ID + "= '" + article.getId() + "'", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, article.getId());
            values.put(KEY_CATEGORY, article.getCategory());
            values.put(KEY_VIEWS, article.getViews().toString());
            values.put(KEY_COMMENTS, article.getComments());
            values.put(KEY_PUBLISHED, article.getPublished());
            values.put(KEY_HEADER, article.getHeader());
            values.put(KEY_AUTHOR, article.getAuthor());
            values.put(KEY_BODYTEXT, article.getBodytext());
            values.put(KEY_URL, article.getUrl());
            values.put(KEY_UNREAD, true);

            // Inserting Row
            db.insert("webtv", null, values);
            for (int i = 0; i < article.getImages().size(); i++) {
                addItemToImagesDB((Images) article.getImages().get(i), article.getId());
            }
            for (int i = 0; i < article.getVideos().size(); i++) {
                addItemToVideosDB(article.getVideos().get(i), article.getId());
            }
        }

        db.close(); // Closing database connection
    }

    public List<Images> getImagesForArticle(String id) {
        List<Images> imageList = new ArrayList<Images>();
        String selectQuery = "SELECT  * FROM " + "images WHERE " + KEY_IMGID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Images image = new Images();
                image.setTitle(cursor.getString(1));
                image.setThumbUrl(cursor.getString(2));
                image.setUrl(cursor.getString(3));
                // Adding item to list
                imageList.add(image);
            } while (cursor.moveToNext());
        }
        db.close();
        return imageList;
    }

    public List<Videos> getVideosForArticle(String id) {
        List<Videos> videoList = new ArrayList<Videos>();
        String selectQuery = "SELECT  * FROM " + "videos WHERE " + KEY_VIDID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Videos video = new Videos();
                video.setTitle(cursor.getString(1));
                video.setUrlImage(cursor.getString(2));
                video.setUrl(cursor.getString(3));
                // Adding item to list
                videoList.add(video);
            } while (cursor.moveToNext());
        }
        db.close();
        return videoList;
    }

    public List<Article> getAllItemsFromNews() {
        List<Article> articleList = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + "news ORDER BY " + KEY_ID + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getString(0));
                article.setCategory(cursor.getString(1));
                article.setViews(new Integer(cursor.getString(2)));
                article.setComments(cursor.getString(3));
                article.setPublished(cursor.getString(4));
                article.setHeader(cursor.getString(5));
                article.setAuthor(cursor.getString(6));
                article.setBodytext(cursor.getString(7));
                article.setUrl(cursor.getString(8));
                article.setUnread(cursor.getInt(9) != 0);
                article.setImages(getImagesForArticle(article.getId()));
                article.setVideos(getVideosForArticle(article.getId()));
                // Adding item to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    public List<Article> getAllItemsFromReviews() {
        List<Article> articleList = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + "reviews ORDER BY " + KEY_ID + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getString(0));
                article.setCategory(cursor.getString(1));
                article.setViews(new Integer(cursor.getString(2)));
                article.setComments(cursor.getString(3));
                article.setPublished(cursor.getString(4));
                article.setHeader(cursor.getString(5));
                article.setAuthor(cursor.getString(6));
                article.setBodytext(cursor.getString(7));
                article.setUrl(cursor.getString(8));
                article.setUnread(cursor.getInt(9) != 0);

                // Adding item to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    public List<Article> getAllItemsFromWebTv() {
        List<Article> articleList = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + "webtv ORDER BY " + KEY_ID + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getString(0));
                article.setCategory(cursor.getString(1));
                article.setViews(new Integer(cursor.getString(2)));
                article.setComments(cursor.getString(3));
                article.setPublished(cursor.getString(4));
                article.setHeader(cursor.getString(5));
                article.setAuthor(cursor.getString(6));
                article.setBodytext(cursor.getString(7));
                article.setUrl(cursor.getString(8));
                article.setUnread(cursor.getInt(9) != 0);

                // Adding item to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    public List<Article> getAllItemsFromTips() {
        List<Article> articleList = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + "tips ORDER BY " + KEY_ID + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getString(0));
                article.setCategory(cursor.getString(1));
                article.setViews(new Integer(cursor.getString(2)));
                article.setComments(cursor.getString(3));
                article.setPublished(cursor.getString(4));
                article.setHeader(cursor.getString(5));
                article.setAuthor(cursor.getString(6));
                article.setBodytext(cursor.getString(7));
                article.setUrl(cursor.getString(8));
                article.setUnread(cursor.getInt(9) != 0);

                // Adding item to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    public void setNewsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_HEADER + "=?";
        String[] whereArgs = new String[]{title};

        db.update("news", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setTipsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_HEADER + "=?";
        String[] whereArgs = new String[]{title};

        db.update("tips", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setReviewsArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_HEADER + "=?";
        String[] whereArgs = new String[]{title};

        db.update("reviews", dataToInsert, where, whereArgs);

        Intent i = new Intent("ArticlesUpdated");
        context.sendBroadcast(i);
    }

    public void setWebTVArticleUnreadStatus(Boolean unread, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(KEY_UNREAD, unread);
        String where = KEY_HEADER + "=?";
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
