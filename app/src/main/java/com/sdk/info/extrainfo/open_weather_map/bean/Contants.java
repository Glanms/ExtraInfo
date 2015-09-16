package com.sdk.info.extrainfo.open_weather_map.bean;

/**
 * Created by Administrator on 2015/7/18.
 * 存放请求API地址，
 */
public class Contants {

    //当前天气
     // public static final String URL_CUR_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    //未来五天预报 ，每隔3小时
    // public static final String URL_FORE_WEATHER = "http://api.openweathermap.org/data/2.5/forecast/city";
    //当前天气的城市ID
    public  static final String PARAM1 = "1816670"; //Beijing
    public  static final String PARAM2 = "745042"; //伊斯坦布尔
    public  static final String PARAM3 = "323776"; //安卡拉
    public  static final String PARAM4 = "750268"; //布尔萨
    public  static final String PARAM5 = "311046"; //伊兹密尔



    /** Base API URL */
    public static final String API_BASE_URL = "http://openweathermap.org/data/2.5";
    /** Current weather API URL */
    public static final String API_WEATHER_URL = API_BASE_URL + "/weather?id=";
    /** Forecasts API URL. "cnt" presents days of forecasts  */
    public static final String API_FORECAST_URL = API_BASE_URL + "/forecast/daily?cnt=3&id=";
    /** API key */
    public static final String API_KEY = "&APPID=1423e6ff5f87d27168226e67b26742b1";

}
