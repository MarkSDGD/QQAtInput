package com.mark.customatfriends.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences存储数据类
 */
public class PreferenceUtils {
    private static SharedPreferences sp;

    private static SharedPreferences getPreferences(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getInt(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static Long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getLong(key, defValue);
    }

}

