package com.sdk.info.extrainfo.yahoo_finance.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/9/9.
 */
public class SharePreferenceUtil {

    public static final String CITY_SHAREPREF_FILE = "city";
    private static final String CITY_NAME = "_city";
    private static final String SIMPLE_CLIMATE = "simple_climate";
    private static final String TIME = "time";
    private static final String TIMESAMP = "timesamp";
    private static final String VERSION = "version";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setCityName(String name) {
        editor.putString(CITY_NAME, name);
        editor.commit();
    }


    public String getCityName() {
        return sp.getString(CITY_NAME, "");
    }

    public String getSimpleClimate() {
        return SIMPLE_CLIMATE;
    }

    public void setTime(long timesamp){
        editor.putLong(TIME,timesamp);
        editor.commit();
    }
    public String getTime() {
        return TIME;
    }

    public String getTIMESAMP() {
        return TIMESAMP;
    }

    public String getVERSION() {
        return VERSION;
    }
}
