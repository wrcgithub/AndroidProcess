package com.wrc.androidprocess.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePreTool {

	public static final String PACKAGE_NAME = "runPackageName";
	public static final String TEXT_COLOR = "text_color";
	/**
	 * 获取配置参数 String
	 * 
	 * @return
	 */
	public static String getStringSP(Context context, String name,
                                     String defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {

			return preferences.getString(name, defValues);
		}
	}

	/**
	 * String 设置配置参数
	 */
	public static void setStringSP(Context context, String name, String values) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(name, values);
		editor.commit();
	}

	/**
	 * int,获取配置参数
	 */
	public static int getIntSP(Context context, String name,
                               int defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getInt(name, defValues);
		}
	}

	/**
	 * int,设置参数
	 */
	public static void setIntSP(Context context, String name, int value) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putInt(name, value);
			editor.commit();
		}
	}

	/**
	 * boolean类型,获取配置参数
	 */
	public static boolean getBooleanSP(Context context, String name,
                                       boolean defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getBoolean(name, defValues);
		}
	}

	/**
	 * boolean类型,设置参数
	 */
	public static void setBooleanSP(Context context, String name,
                                    boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putBoolean(name, value);
			editor.commit();
		}
	}
}
