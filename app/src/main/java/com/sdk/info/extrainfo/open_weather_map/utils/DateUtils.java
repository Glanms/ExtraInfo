package com.sdk.info.extrainfo.open_weather_map.utils;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/7/21.
 *  时间的工具类
 */
public class DateUtils {
    private int today_week;
    private String next_day1_week;
    private String next_day2_week;
    private String next_day3_week;

    public DateUtils() {
        this.today_week = getToday();
    }

    public static int getToday(){
        Calendar calendar = Calendar.getInstance();
        int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return todayOfWeek;
    }
    public void getWeek(int todayOfWeek){
        switch (todayOfWeek)
        {
            case Calendar.MONDAY:
                break;
            case Calendar.TUESDAY:
                break;
            case Calendar.WEDNESDAY:
                break;
            case Calendar.THURSDAY:
                break;
            case Calendar.FRIDAY:
                break;
            case Calendar.SATURDAY:
                break;
            case Calendar.SUNDAY:
                break;

        }

    }
}
