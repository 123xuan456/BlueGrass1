package com.reeching.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PostInfoSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final int VERSION = 1;

	public static final String SQL_FISHPOSTINFO_NAME = "cachepostinfo.db";

	public String postinfo = "create table if not exists postinfo (_id integer primary key autoincrement,galleryId text,theme text,status text,dateBegin text,dateEnd text,picarraypath text,careLevel text,author text,authorIntroduction text,manager text,managerIntroduction text,userId text,exhibitionIntroduction text,remarks text)";

	public PostInfoSQLiteOpenHelper(Context context) {
		super(context, SQL_FISHPOSTINFO_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(postinfo);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
