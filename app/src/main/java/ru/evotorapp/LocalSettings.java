package ru.evotorapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sergey-rush on 23.11.2017.
 */

public class LocalSettings {

    private static final String APP_PREFERENCES = "app_settings";
    private static final String API_KEY = "api_key";
    private static final String DEVICE_ID = "device_id";

    public static void setApiKey(Context context, String value) {
        SharedPreferences appSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(API_KEY, value);
        editor.apply();
        editor.commit();
    }

    public static String getApiKey(Context context) {
        String result = "";
        SharedPreferences appSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (appSettings.contains(API_KEY)) {
            result = appSettings.getString(API_KEY, "");
        }
        return result;
    }

    public static void setDeviceId(Context context, String value) {
        SharedPreferences appSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(DEVICE_ID, value);
        editor.apply();
    }

    public static String getDeviceId(Context context) {
        String result = "";
        SharedPreferences appSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (appSettings.contains(DEVICE_ID)) {
            result = appSettings.getString(DEVICE_ID, "");
        }
        return result;
    }
}
