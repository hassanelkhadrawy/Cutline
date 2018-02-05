package com.system.cutline.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.system.cutline.Core.CutlineApplication;

public class PrefsUtil {

    private static final String DEFAULT_APP_PREFS_NAME = "cutline-default-prefs";
    public static final String QUESTIONS_DIALOG_PREFS_NAME = "questions-dialog-default-prefs";

    private static SharedPreferences getPrefs(String prefsName) {
        if (TextUtils.isEmpty(prefsName)) {
            prefsName = DEFAULT_APP_PREFS_NAME;
        }
        return CutlineApplication.getInstance().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value) {
        saveString(null, key, value);
    }

    public static void saveString(String prefsName, String key, String value) {
        getPrefs(prefsName).edit().putString(key, value).apply();
    }

    @Nullable
    public static String getString(String key) {
        return getString(null, key);
    }

    @Nullable
    public static String getString(String prefsName, String key) {
        return getPrefs(prefsName).getString(key, null);
    }

    public static void saveInt(String key, int value) {
        saveInt(null, key, value);
    }

    public static void saveInt(String prefsName, String key, int value) {
        getPrefs(prefsName).edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getInt(null, key);
    }

    public static int getInt(String prefsName, String key) {
        return getPrefs(prefsName).getInt(key, -1);
    }

    public static void saveBoolean(String key, boolean value) {
        saveBoolean(null, key, value);
    }

    public static void saveBoolean(String prefsName, String key, boolean value) {
        getPrefs(prefsName).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(null, key);
    }

    public static boolean getBoolean(String prefsName, String key) {
        return getPrefs(prefsName).getBoolean(key, false);
    }

    public static void clear() {
        getInstance().edit().clear().commit();
    }

    public static void clear(String prefsName) {
        getInstance(prefsName).edit().clear().commit();
    }

    public static SharedPreferences getInstance() {
        return getPrefs(null);
    }

    public static SharedPreferences getInstance(String prefsName) {
        return getPrefs(prefsName);
    }

}
