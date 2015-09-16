package com.sdk.info.extrainfo.open_weather_map.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/7/20.
 * 天气预报的Bean
 */
public class ForcastsBean implements Parcelable {
    /**
     * 未来天气状态
     */
    private String furState;
    /**
     * 未来天气最低温度
     */
    private float furMinTemp;
    /**
     * 未来天气最高温度
     */
    private float furMaxTemp;
    /**
     * 未来天气风力
     */
    private float furWind;
    /**
     * 预报对应的星期几
     */
    private String furDate;
    /**
     * 预报的对应的icon
     */
    private Bitmap furIcon;


    public ForcastsBean(Parcel in) {
        furState = in.readString();
        furMinTemp = in.readFloat();
        furMaxTemp = in.readFloat();
        furWind = in.readFloat();
        furDate = in.readString();
        furIcon = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ForcastsBean> CREATOR = new Creator<ForcastsBean>() {
        @Override
        public ForcastsBean createFromParcel(Parcel in) {
            return new ForcastsBean(in);
        }

        @Override
        public ForcastsBean[] newArray(int size) {
            return new ForcastsBean[size];
        }
    };

    public Bitmap getFurIcon() {
        return furIcon;
    }

    public void setFurIcon(Bitmap furIcon) {
        this.furIcon = furIcon;
    }

    public ForcastsBean() {

    }

    public String getFurState() {
        return furState;
    }

    public void setFurState(String furState) {
        this.furState = furState;
    }

    public float getFurMinTemp() {
        return furMinTemp;
    }

    public void setFurMinTemp(double furMinTemp) {
        this.furMinTemp =(float)Math.abs(furMinTemp - 274.15);
    }

    public float getFurMaxTemp() {
        return furMaxTemp;
    }

    public void setFurMaxTemp(double furMaxTemp) {
        this.furMaxTemp =(float)Math.abs(furMaxTemp - 274.15);
    }

    public float getFurWind() {
        return furWind;
    }

    public void setFurWind(double furWind) {
        this.furWind = (float)Math.abs(furWind-0);
    }

    public String getFurDate() {
        return furDate;
    }

    public void setFurDate(String furDate) {
        this.furDate = furDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(furState);
        dest.writeFloat(furMinTemp);
        dest.writeFloat(furMaxTemp);
        dest.writeFloat(furWind);
        dest.writeString(furDate);
        dest.writeParcelable(furIcon, flags);
    }

}
