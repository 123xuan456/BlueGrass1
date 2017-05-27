package com.reeching.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "list1.db";
	public static final String TABLE_NAME = "listinfo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_galleryId = "galleryId"; // 画廊
	public static final String COLUMN_theme = "theme"; // 展览主题
	public static final String COLUMN_status = "status"; // 状态 1:已完成 0：进行中
	public static final String COLUMN_dateBegin = "dateBegin"; // 开始时间
	public static final String COLUMN_dateEnd = "dateEnd"; // 结束时间
	public static final String COLUMN_careLevel = "careLevel"; // 关注等级 0:低 1：中
	public static final String COLUMN_author = "author"; // 作者
	public static final String COLUMN_authorIntroduction = "authorIntroduction"; // 作者简介
	public static final String COLUMN_manager = "manager"; // 策展人
	public static final String COLUMN_managerIntroduction = "managerIntroduction"; // 策展人简介
	public static final String COLUMN_userId = "userId"; // 上报人id
	public static final String COLUMN_exhibitionIntroduction = "exhibitionIntroduction";// 展览概要
	public static final String COLUMN_remarks = "remarks";
	public static final String COLUMN_path = "path";

	private static final int VERSION = 1;

	/**
	 * 鍒涘缓鏁版嵁搴?
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	/**
	 * 创建数据库
	 * 
	 * @param context
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table " + TABLE_NAME + "(" + COLUMN_ID
				+ " integer primary key autoincrement," + COLUMN_galleryId
				+ " text," + COLUMN_theme + " text," + COLUMN_status + " text,"
				+ COLUMN_exhibitionIntroduction + " text," + COLUMN_remarks
				+ " text," + COLUMN_dateBegin + " text," + COLUMN_dateEnd
				+ " text," + COLUMN_careLevel + " text," + COLUMN_author
				+ " text," + COLUMN_authorIntroduction + " text," + COLUMN_path
				+ " text," + COLUMN_manager + " text,"
				+ COLUMN_managerIntroduction + " text," + COLUMN_userId
				+ " text)");

	}

	/**
	 * 数据库版本更新的时候，自动执行回调
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
