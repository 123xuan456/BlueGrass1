package com.reeching.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	public final static String SP_KANGAROO = "SP_KANGAROO";

	public static SharedPreferences preferences;
	public static SharedPreferences.Editor editor;

	/**
	 * 记录用户名
	 * */
	public static void putUserSP(String name, Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		editor = preferences.edit();
		editor.putString("SAVE_USER_KEY", name);
		editor.commit();
	}

	/**
	 * 获取用户名
	 * */
	public static String getUserSP(Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		return preferences.getString("SAVE_USER_KEY", "");

	}

	public static void getUserId(String id, Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		editor.putString("userId", id);
		editor.commit();
	}

	/**
	 * 记录密码
	 * */
	public static void putPassSP(String name, Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		editor = preferences.edit();
		editor.putString("SAVE_PASS_ET_KEY", name);
		editor.commit();
	}

	public static String getUserIdSp(Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		return preferences.getString("userId", null);
	}

	/**
	 * 获取密码
	 * */
	public static String getPassSP(Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		return preferences.getString("SAVE_PASS_ET_KEY", "");

	}

	/**
	 * 设置 是否有缓存 状态
	 * */
	public static void putflag(Boolean flag, Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		editor = preferences.edit();
		editor.putBoolean("HAS_INFO", flag);
		editor.commit();
	}

	/**
	 * 获取 是否有缓存 状态
	 * */
	public static Boolean getflag(Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		return preferences.getBoolean("HAS_INFO", false);

	}

	/**
	 * 记录时间
	 * */
	public static void putversion(String ver, Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		editor = preferences.edit();
		editor.putString("ver", ver);
		editor.commit();
	}

	/**
	 * 获取 时间
	 * */
	public static String getversion(Context context) {
		preferences = context.getSharedPreferences(SP_KANGAROO, 0);
		return preferences.getString("ver", "");

	}
}
