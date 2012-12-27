package dk.whooper.mobilsiden.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "mobilsiden";
    private static final String TABLE_NAME = "articles";
    private static final String TABLE_CREATE = "create table "+TABLE_NAME+" (article_id integer primary key autoincrement, "
            + "feed_id int not null, title text not null, url text not null);";
	
    public DatabaseOpenHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
