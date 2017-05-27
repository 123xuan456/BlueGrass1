package com.reeching.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 用户的持久化操作
 * 
 * @author Administrator
 * 
 */
public class UserDao {
	private static UserDao userDao;
	private DBHelper dbHelper;
	private SQLiteDatabase readDb, writeDb;

	private UserDao(Context context) {
		dbHelper = new DBHelper(context);
		// 通过dbHelper创建readDb,writeDb
		readDb = dbHelper.getReadableDatabase();
		writeDb = dbHelper.getWritableDatabase();
	}

	public static UserDao getInstance(Context context) {
		if (userDao == null) {
			userDao = new UserDao(context);
		}
		return userDao;
	}

	public long addList(String galleryId, String theme, String status,
			String dateBegin, String dateEnd, String careLevel, String author,
			String authorIntroduction, String manager,
			String managerIntroduction, String userId,
			String exhibitionIntroduction, String remarks, String path) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_author, author);

		values.put(DBHelper.COLUMN_authorIntroduction, authorIntroduction);
		values.put(DBHelper.COLUMN_careLevel, careLevel);
		values.put(DBHelper.COLUMN_dateBegin, dateBegin);
		values.put(DBHelper.COLUMN_manager, manager);
		values.put(DBHelper.COLUMN_managerIntroduction, managerIntroduction);
		values.put(DBHelper.COLUMN_status, status);
		values.put(DBHelper.COLUMN_exhibitionIntroduction,
				exhibitionIntroduction);
		values.put(DBHelper.COLUMN_galleryId, galleryId);
		values.put(DBHelper.COLUMN_remarks, remarks);
		values.put(DBHelper.COLUMN_theme, theme);
		values.put(DBHelper.COLUMN_userId, userId);
		values.put(DBHelper.COLUMN_dateEnd, dateEnd);
		values.put(DBHelper.COLUMN_path, path);

		return writeDb.insert(DBHelper.TABLE_NAME, null, values);
	}

	/**
	 * 查询所有用户 Cursor:类似ArrayList 数据集合 select * from userinfo
	 */
	public Cursor queryUserList() {
		Cursor cursor = readDb.query(DBHelper.TABLE_NAME, null, null, null,
				null, null, null);
		return cursor;
	}

	public int delete(String id) {
		return writeDb.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_ID + "=?",
				new String[] { id });
	}

}
