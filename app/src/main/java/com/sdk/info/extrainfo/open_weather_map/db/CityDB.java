package com.sdk.info.extrainfo.open_weather_map.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.sdk.info.extrainfo.open_weather_map.bean.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/7.
 * Read city info from asserts/city.db.
 */
public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;

    public CityDB(Context context, String path) {
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
    }

    public boolean isOpen() {
        return db != null && db.isOpen();
    }

    public void close() {
        if (db != null && db.isOpen())
            db.close();
    }

    public List<City> getAllCity() {
        List<City> list = new ArrayList<City>();
        Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
        while (c.moveToNext()) {
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("name"));
            int id = c.getInt(c.getColumnIndex("weather_id"));
            String firstSelection = c.getString(c.getColumnIndex("selection")); //用来作为排序的依据
            City item = new City(province, city, id, firstSelection);
            list.add(item);
        }
        return list;
    }

    public City getCity(String city) {
        if (TextUtils.isEmpty(city))
            return null;
        City item = getCityInfo(city);//先全部搜索
        if (item == null) {
            item = getCityInfo(parseName(city));//处理一下之后再搜索
        }
        return item;
    }

    /**
     * 去掉市或县搜索
     *
     * @param city
     * @return
     */
    private String parseName(String city) {
//        city = city.replaceAll("市$", "").replaceAll("县$", "")
//                .replaceAll("区$", "");
        return city;
    }

    /**
     * 查询数据库city表内容
     * @param city
     * @return
     */
    private City getCityInfo(String city) {
        City item = null;
        Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
                + " where name=?", new String[] { city });
        if (c.moveToFirst()) {
            String province = c.getString(c.getColumnIndex("province"));
            String name = c.getString(c.getColumnIndex("name"));
            int id = c.getInt(c.getColumnIndex("weather_id"));
            String firstSelection = c.getString(c.getColumnIndex("selection"));
            item = new City(province, name, id, firstSelection);
        }
        return item;
    }

    private void delecteCityTable(){

    }

}
