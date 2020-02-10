package com.organicbharat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class AppPref {
    public static String cart_address="";
    public static  String SLOTS = "SLOTS";
    public static  String DELIVERYCHARGE = "DELIVERYCHARGE";
    public static  String MINAMOUNT = "MINAMOUNT";
    public static  String ADDRESSID = "ADDRESSID";
    public static String PHONE = "PHONE";
    public static String API_KEY="API_KEY";
    public static String FCM_TOKEN="FCM_TOKEN";
    public static String USER_ID="USER_ID";
    public static String NAME="NAME";
    public static String EMAIL="EMAIL";
    public static String MOBILE="MOBILE";
    public static String IS_LOGIN="IS_LOGIN";
    public static String OTP = "OTP";
    public static String ITEMINCART = "ITEMINCART";

    private static AppPref sInstance;
    private static SharedPreferences sPref;
    private static SharedPreferences.Editor sEditor;

    private AppPref(Context context) {
        sPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sEditor = sPref.edit();
    }

    public static AppPref getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPref(context);
        }
        return sInstance;
    }

    //set methods
    public void set(String key, String value) {
        sEditor.putString(key, value).apply();
    }

    public void set(String key, boolean value) {
        sEditor.putBoolean(key, value).apply();
    }

    public void set(String key, float value) {
        sEditor.putFloat(key, value).apply();
    }

    public void set(String key, int value) {
        sEditor.putInt(key, value).apply();
    }

    public void set(String key, long value) {
        sEditor.putLong(key, value).apply();
    }

    public void set(String key, Set<String> value) {
        sEditor.putStringSet(key, value).apply();
    }

    // get methods
    public int getInt(String key, int defaultVal) {
        return sPref.getInt(key, defaultVal);
    }
    public int getInt(String key) {
        return sPref.getInt(key,0);
    }

    public String getString(String key, String defaultVal) {
        return sPref.getString(key, defaultVal);
    }
    public String getString(String key) {
        return sPref.getString(key, "");
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return sPref.getBoolean(key, defaultVal);
    }
    public boolean getBoolean(String key) {
        return sPref.getBoolean(key,false);
    }


    public float getFloat(String key, float defaultVal) {
        return sPref.getFloat(key, defaultVal);
    }
    public float getFloat(String key) {
        return sPref.getFloat(key,0);
    }

    public long getLong(String key, long defaultVal) {
        return sPref.getLong(key, defaultVal);
    }
    public long getLong(String key) {
        return sPref.getLong(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return sPref.getStringSet(key, null);
    }

    public void clearData() {
        String token = getString(FCM_TOKEN);
        sEditor.clear().apply();
        set(FCM_TOKEN,token);
    }
    public int getUserId()
    {
        return sPref.getInt(USER_ID,0);
    }
    public boolean contains(String key) {
        return sPref.contains(key);
    }

    public void remove(String key) {
        sEditor.remove(key);
    }

    public Map<String, ?> getAll() {
        return sPref.getAll();
    }

    public boolean isLogin() {
        return sPref.getBoolean(IS_LOGIN,false);
    }
}