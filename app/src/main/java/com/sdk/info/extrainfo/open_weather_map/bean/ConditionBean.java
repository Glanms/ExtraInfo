package com.sdk.info.extrainfo.open_weather_map.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/7/18.
 * Bean of the weather infomation
 */
public class ConditionBean {

    /**
     * 城市名
     */
    private String cityName;
    /**
     * 当前天气状态
     */
    private String curState;
    /**
     * 当前温度
     */
    private float curTemp;
    /**
     * 今日最低温
     */
    private float curMinTemp;
    /**
     * 今日最高温
     */
    private float curMaxTemp;
    /**
     * 天气描述信息
     */
    private String desc;
    /**
     * 当前天气的icon
     */
    private Bitmap curIcon;

    public Bitmap getCurIcon() {
        return curIcon;
    }

    public void setCurIcon(Bitmap curIcon) {
        this.curIcon = curIcon;
    }

    public float getCurMinTemp() {
        return curMinTemp;
    }

    public void setCurMinTemp(double curMinTemp) {
        this.curMinTemp =(float)Math.abs(curMinTemp - 274.15);
    }

    public float getCurMaxTemp() {
        return curMaxTemp;
    }

    public void setCurMaxTemp(double curMaxTemp) {
        this.curMaxTemp =(float)Math.abs(curMaxTemp - 274.15);
    }

    public float getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(double curTemp) {
        this.curTemp =(float)Math.abs(curTemp - 274.15);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCurState() {
        return curState;
    }

    public void setCurState(String curState) {
        this.curState = curState;
    }
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
