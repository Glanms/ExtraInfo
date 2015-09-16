package com.sdk.info.extrainfo;

import android.app.NotificationManager;
import android.os.Environment;
import android.util.Log;

import com.sdk.info.extrainfo.open_weather_map.bean.City;
import com.sdk.info.extrainfo.open_weather_map.db.CityDB;
import com.sdk.info.extrainfo.open_weather_map.utils.T;
import com.sdk.info.extrainfo.yahoo_finance.utils.RequestManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/7.
 */
public class Application extends android.app.Application {
    public static final int CITY_LIST_SCUESS = 100;
    private static final String FORMAT = "^[a-z,A-Z].*$";
    private static Application mApplication;
    private CityDB mCityDB;
    private HashMap<String, Integer> mWeatherIcon;// 天气图标
    private HashMap<String, Integer> mWidgetWeatherIcon;// 插件天气图标
    private List<City> mCityList;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;

//    private LocationClient mLocationClient = null;
//    private SharePreferenceUtil mSpUtil;
//    private WeatherInfo mAllWeather;
    public static int mNetWorkState;
    private NotificationManager mNotificationManager;

    public static synchronized Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
      mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理,待优化
        initData();
    }

    @Override
    public void onTerminate() {
        Log.d("Application:", "Application onTerminate...");
        super.onTerminate();
        if (mCityDB != null && mCityDB.isOpen())
            mCityDB.close();
    }

    // 当程序在后台运行时，释放这部分最占内存的资源
    public void free() {
        mCityList = null;
        mSections = null;
        mMap = null;
        mPositions = null;
        mIndexer = null;
        mWeatherIcon = null;
//        mAllWeather = null;
        System.gc();
    }

    /**
     * 百度定位的配置
     */
//    private LocationClientOption getLocationClientOption() {
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);
//        option.setAddrType("all");
//        option.setServiceName(this.getPackageName());
//        option.setScanSpan(0);
//        option.disableCache(true);
//        return option;
//    }

    public void initData() {

        RequestManager.init(this);
//        mNetWorkState = NetUtil.getNetworkState(this);
        initCityList();
//        mLocationClient = new LocationClient(this, getLocationClientOption());
//        initWeatherIconMap();
//        initWidgetWeather();
//        mSpUtil = new SharePreferenceUtil(this,
//                SharePreferenceUtil.CITY_SHAREPRE_FILE);
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    public synchronized CityDB getCityDB() {
        if (mCityDB == null || !mCityDB.isOpen())
            mCityDB = openCityDB();
        return mCityDB;
    }

/*    public synchronized SharePreferenceUtil getSharePreferenceUtil() {
        if (mSpUtil == null)
            mSpUtil = new SharePreferenceUtil(this,
                    SharePreferenceUtil.CITY_SHAREPRE_FILE);
        return mSpUtil;
    }

    public synchronized LocationClient getLocationClient() {
        if (mLocationClient == null)
            mLocationClient = new LocationClient(this,
                    getLocationClientOption());
        return mLocationClient;
    }*/
    //先删除之前的City数据库文件,方法不对
    private void deleteCityDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sdk.info.extrainfo" + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if(db.exists()){
            deleteDatabase(path);
            openCityDB();
        }
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sdk.info.extrainfo" + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()/* || getSharePreferenceUtil().getVersion() < 0*/) {
            Log.d("Application:","db is not exists");
            try {
                InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
//                getSharePreferenceUtil().setVersion(1);// 用于管理数据库版本，如果数据库有重大更新时使用
            } catch (IOException e) {
                e.printStackTrace();
                T.showLong(mApplication, e.getMessage());
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public List<String> getSections() {
        return mSections;
    }

    public Map<String, List<City>> getMap() {
        return mMap;
    }

    public List<Integer> getPositions() {
        return mPositions;
    }

    public Map<String, Integer> getIndexer() {
        return mIndexer;
    }


/*
    public Map<String, Integer> getWeatherIconMap() {
        if (mWeatherIcon == null || mWeatherIcon.isEmpty())
            mWeatherIcon = initWeatherIconMap();
        return mWeatherIcon;
    }
*/

    public NotificationManager getNotificationManager() {
        return mNotificationManager;
    }



  /*  public int getWidgetWeatherIcon(String climate) {
        int weatherRes = R.drawable.na;
        if (TextUtils.isEmpty(climate))
            return weatherRes;
        String[] strs = { "晴", "晴" };
        if (climate.contains("转")) {// 天气带转字，取前面那部分
            strs = climate.split("转");
            climate = strs[0];
            if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
                strs = climate.split("到");
                climate = strs[1];
            }
        }
        if (mWidgetWeatherIcon == null || mWidgetWeatherIcon.isEmpty())
            mWidgetWeatherIcon = initWidgetWeather();
        if (mWidgetWeatherIcon.containsKey(climate)) {
            weatherRes = mWidgetWeatherIcon.get(climate);
        }
        return weatherRes;
    }*/

   /* public WeatherInfo getAllWeather() {
        return mAllWeather;
    }

    public void SetAllWeather(WeatherInfo allWeather) {
        mAllWeather = allWeather;
    }*/

//    private HashMap<String, Integer> initWeatherIconMap() {
//        if (mWeatherIcon != null && !mWeatherIcon.isEmpty())
//            return mWeatherIcon;
//        mWeatherIcon = new HashMap<String, Integer>();
//        mWeatherIcon.put("暴雪", R.drawable.biz_plugin_weather_baoxue);
//        mWeatherIcon.put("暴雨", R.drawable.biz_plugin_weather_baoyu);
//        mWeatherIcon.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);
//        mWeatherIcon.put("大雪", R.drawable.biz_plugin_weather_daxue);
//        mWeatherIcon.put("大雨", R.drawable.biz_plugin_weather_dayu);
//
//        mWeatherIcon.put("多云", R.drawable.biz_plugin_weather_duoyun);
//        mWeatherIcon.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);
//        mWeatherIcon.put("雷阵雨冰雹",
//                R.drawable.biz_plugin_weather_leizhenyubingbao);
//        mWeatherIcon.put("晴", R.drawable.biz_plugin_weather_qing);
//        mWeatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);
//
//        mWeatherIcon.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);
//        mWeatherIcon.put("雾", R.drawable.biz_plugin_weather_wu);
//        mWeatherIcon.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);
//        mWeatherIcon.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);
//        mWeatherIcon.put("阴", R.drawable.biz_plugin_weather_yin);
//
//        mWeatherIcon.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);
//        mWeatherIcon.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);
//        mWeatherIcon.put("阵雨", R.drawable.biz_plugin_weather_zhenyu);
//        mWeatherIcon.put("中雪", R.drawable.biz_plugin_weather_zhongxue);
//        mWeatherIcon.put("中雨", R.drawable.biz_plugin_weather_zhongyu);
//        return mWeatherIcon;
//    }

   /* private HashMap<String, Integer> initWidgetWeather() {
        if (mWidgetWeatherIcon != null && !mWidgetWeatherIcon.isEmpty())
            return mWidgetWeatherIcon;
        mWidgetWeatherIcon = new HashMap<String, Integer>();
        mWidgetWeatherIcon.put("暴雪", R.drawable.w17);
        mWidgetWeatherIcon.put("暴雨", R.drawable.w10);
        mWidgetWeatherIcon.put("大暴雨", R.drawable.w10);
        mWidgetWeatherIcon.put("大雪", R.drawable.w16);
        mWidgetWeatherIcon.put("大雨", R.drawable.w9);

        mWidgetWeatherIcon.put("多云", R.drawable.w1);
        mWidgetWeatherIcon.put("雷阵雨", R.drawable.w4);
        mWidgetWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
        mWidgetWeatherIcon.put("晴", R.drawable.w0);
        mWidgetWeatherIcon.put("沙尘暴", R.drawable.w20);

        mWidgetWeatherIcon.put("特大暴雨", R.drawable.w10);
        mWidgetWeatherIcon.put("雾", R.drawable.w18);
        mWidgetWeatherIcon.put("小雪", R.drawable.w14);
        mWidgetWeatherIcon.put("小雨", R.drawable.w7);
        mWidgetWeatherIcon.put("阴", R.drawable.w2);

        mWidgetWeatherIcon.put("雨夹雪", R.drawable.w6);
        mWidgetWeatherIcon.put("阵雪", R.drawable.w13);
        mWidgetWeatherIcon.put("阵雨", R.drawable.w3);
        mWidgetWeatherIcon.put("中雪", R.drawable.w15);
        mWidgetWeatherIcon.put("中雨", R.drawable.w8);
        return mWidgetWeatherIcon;
    }*/

    private void initCityList() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
        for (City city : mCityList) {
            String firstName = city.getFirstSelection().substring(0, 1).toUpperCase();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        return true;
    }

    /*public void showNotification() {

        int icon = R.drawable.ic_launcher;
       CharSequence tickerText = mAllWeather.getYujing();
        long when = System.currentTimeMillis();
        Notification mNotification = new Notification(icon, tickerText, when);

        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.contentView = null;

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 指定内容意图
        mNotification.setLatestEventInfo(mApplication, "简洁天气预警", tickerText,
                contentIntent);

        mNotificationManager.notify(0x001, mNotification);
    }*/
}