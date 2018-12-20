package com.liuguilin.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * FileName: SharedPreUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 10:19
 * Email: lgl@szokl.com.cn
 * Profile: SharedPreferences
 */
public class SharedPreUtils {

    private Context mContext;
    private static SharedPreUtils mInstance = null;
    private static final String SP_NAME = "im";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreUtils() {

    }

    public static SharedPreUtils getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreUtils();
                }
            }
        }
        return mInstance;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void setBool(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public boolean getBool(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }
}
