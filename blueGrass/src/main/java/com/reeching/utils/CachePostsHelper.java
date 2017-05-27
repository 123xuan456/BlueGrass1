package com.reeching.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.reeching.bluegrass.PostInfo;

public class CachePostsHelper {

	public PostInfoSQLiteOpenHelper sQopenHelper;

	public CachePostsHelper(Context context) {
		sQopenHelper = new PostInfoSQLiteOpenHelper(context);
	}

	/**
	 * 保存帖子
	 * 
	 * @param list
	 */
	public void saveCachePostInfo(List<Object> list) {
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();
		int size = list.size() >= 50 ? 50 : list.size();
		for (int i = 0; i < size; i++) {
			ContentValues values = new ContentValues();

			if (!"null".equals(((PostInfo) list.get(i)).getAuthor())
					&& ((PostInfo) list.get(i)).getAuthor() != null) {
				values.put("author", ((PostInfo) list.get(i)).getAuthor());
			} else {
				values.put("author", "");
			}
			if (!"null"
					.equals(((PostInfo) list.get(i)).getAuthorIntroduction())
					&& ((PostInfo) list.get(i)).getAuthorIntroduction() != null) {
				values.put("authorIntroduction",
						((PostInfo) list.get(i)).getAuthorIntroduction());
			} else {
				values.put("authorIntroduction", "");
			}
			if (!"null".equals(((PostInfo) list.get(i)).getCareLevel())
					&& ((PostInfo) list.get(i)).getCareLevel() != null) {
				values.put("careLevel", ((PostInfo) list.get(i)).getCareLevel());
			} else {
				values.put("careLevel", "");
			}
			if (!"null".equals(((PostInfo) list.get(i)).getDateBegin())
					&& ((PostInfo) list.get(i)).getDateBegin() != null) {
				values.put("dateBegin", ((PostInfo) list.get(i)).getDateBegin());
			} else {
				values.put("dateBegin", "");
			}
			if (((PostInfo) list.get(i)).getPicArrayPath() != null) {
				StringBuffer paths = new StringBuffer();
				for (String path : ((PostInfo) list.get(i)).getPicArrayPath()) {
					if (paths.length() == 0) {
						paths.append(path);
					} else {
						paths.append(";").append(path);
					}
				}
				values.put("picarraypath", paths.toString());
			} else {
				values.put("picarraypath", "");
			}

			if (!"null".equals(((PostInfo) list.get(i)).getDateEnd())
					&& ((PostInfo) list.get(i)).getDateEnd() != null) {
				values.put("dateEnd", ((PostInfo) list.get(i)).getDateEnd());
			} else {
				values.put("dateEnd", "");
			}
			values.put("exhibitionIntroduction",
					((PostInfo) list.get(i)).getExhibitionIntroduction());

			values.put("galleryId", ((PostInfo) list.get(i)).getGalleryId());
			if (((PostInfo) list.get(i)).getManager() != null) {
				values.put("manager", ((PostInfo) list.get(i)).getManager());
			} else {
				values.put("manager", "");
			}
			values.put("managerIntroduction",
					((PostInfo) list.get(i)).getManagerIntroduction());
			if (((PostInfo) list.get(i)).getRemarks() != null
					&& !"".equals(((PostInfo) list.get(i)).getRemarks())) {
				values.put("remarks", ((PostInfo) list.get(i)).getRemarks());
			} else {
				values.put("remarks", "");
			}
			if (((PostInfo) list.get(i)).getStatus() != null
					&& !"".equals(((PostInfo) list.get(i)).getStatus())) {
				values.put("status", ((PostInfo) list.get(i)).getStatus());
			} else {
				values.put("status", "");
			}
			if (!"".equals(((PostInfo) list.get(i)).getTheme())) {
				values.put("theme", ((PostInfo) list.get(i)).getTheme());
			} else {
				values.put("theme", "");
			}
			if (!"null".equals(((PostInfo) list.get(i)).getUserId())
					&& ((PostInfo) list.get(i)).getUserId() != null) {
				values.put("userId", ((PostInfo) list.get(i)).getUserId());

			} else {
				values.put("userId", "");
			}

		}
		database.close();
	}

	// 从数据库中获取数�?
	public ArrayList<Object> getCachePostInfo() {
		ArrayList<Object> list = new ArrayList<Object>();
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from postinfo", null);
		int columnCount = cursor.getColumnCount();
		int count = cursor.getCount();
		if (count > 0) {
			while (cursor.moveToNext()) {
				PostInfo postInfo = new PostInfo();

				if (cursor.getColumnIndex("galleryId") != -1
						&& cursor.getString(cursor.getColumnIndex("galleryId")) != null) {
					postInfo.setGalleryId(cursor.getString(cursor
							.getColumnIndex("galleryId")));
				}
				if (cursor.getColumnIndex("theme") != -1) {
					postInfo.setTheme(cursor.getString(cursor
							.getColumnIndex("theme")));
				}
				if (cursor.getColumnIndex("status") != -1) {
					postInfo.setStatus(cursor.getString(cursor
							.getColumnIndex("status")));
				}
				if (cursor.getColumnIndex("dateBegin") != -1) {
					postInfo.setDateBegin(cursor.getString(cursor
							.getColumnIndex("dateBegin")));
				}
				if (cursor.getColumnIndex("dateEnd") != -1) {
					postInfo.setDateEnd(cursor.getString(cursor
							.getColumnIndex("edateEnd")));
				}
				if (cursor.getColumnIndex("picarraypath") != -1) {
					String str = cursor.getString(cursor
							.getColumnIndex("picarraypath"));
					if (!"".equals(str) && str != null && !"null".equals(str)) {
						String[] picPath = str.split(";");
						postInfo.setPicArrayPath(picPath);
					}
				}

				if (cursor.getColumnIndex("careLevel") != -1) {
					postInfo.setCareLevel(cursor.getString(cursor
							.getColumnIndex("careLevel")));
				}
				if (cursor.getColumnIndex("author") != -1) {
					postInfo.setAuthor(cursor.getString(cursor
							.getColumnIndex("author")));
				}
				if (cursor.getColumnIndex("authorIntroduction") != -1) {
					postInfo.setAuthorIntroduction(cursor.getString(cursor
							.getColumnIndex("authorIntroduction")));
				}
				if (cursor.getColumnIndex("manager") != -1) {
					postInfo.setManager(cursor.getString(cursor
							.getColumnIndex("manager")));
				}
				if (cursor.getColumnIndex("managerIntroduction") != -1
						&& cursor.getString(cursor
								.getColumnIndex("managerIntroduction")) != null) {
					postInfo.setManagerIntroduction(cursor.getString(cursor
							.getColumnIndex("managerIntroduction")));
				}
				if (cursor.getColumnIndex("userId") != -1
						&& cursor.getString(cursor.getColumnIndex("userId")) != null) {
					postInfo.setUserId(cursor.getString(cursor
							.getColumnIndex("userId")));
				}
				if (cursor.getColumnIndex("exhibitionIntroduction") != -1
						&& cursor.getString(cursor
								.getColumnIndex("exhibitionIntroduction")) != null) {
					postInfo.setExhibitionIntroduction(cursor.getString(cursor
							.getColumnIndex("exhibitionIntroduction")));
				}
				if (cursor.getColumnIndex("remarks") != -1
						&& cursor.getString(cursor.getColumnIndex("remarks")) != null) {
					postInfo.setRemarks(cursor.getString(cursor
							.getColumnIndex("remarks")));
				}

			}
		}
		cursor.close();
		database.close();
		return list;
	}

	public void deleteCachePostInfo() {
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();
		database.delete("postinfo", null, null);
		database.close();
	}

	// ɾ��ĳ������
	public void deleteOneCachePostInfo(String galleryId) {
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();
		database.delete("postinfo", "galleryId" + "=?",
				new String[] { galleryId });
		database.close();
	}

	// ���ĳ������
	public void insertOneCachePostInfo(String str) {
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();

		database.insert("postinfo", "str" + "=?", null);
		database.close();
	}

	public void insertCachePostInfo(String str, String strValue) {
		SQLiteDatabase database = sQopenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(str, strValue);
		database.insert("postinfo", "str" + "=?", values);
		database.close();
	}

}
