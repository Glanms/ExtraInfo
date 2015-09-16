package com.sdk.info.extrainfo.open_weather_map.bean;

/**
 * Created by Administrator on 2015/8/4.
 */
public class City {

    private static final long serialVersionUID = 1L;
    private String province;
    private String name;
    /**
     * 天气的请求ID
     */
    private int weatherId;
    /**
     * 城市的排序依据（首字母）
     */
    private String firstSelection;

    public void setProvince(String province) {
        this.province = province;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public City() {
    }

    public City(String province, String city, int id,
                String selection) {
        super();
        this.province = province;
        this.name = city;
        this.weatherId = id;
        this.firstSelection = selection;
    }

    public String getProvince() {
        return province;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getFirstSelection() {
        return firstSelection;
    }

    public void setFirstSelection(String selection) {
        this.firstSelection = selection;
    }

    @Override
    public String toString() {
        return "City [province=" + province + ", name=" + name + ", weatherId="
                + weatherId + ", firstSelection=" + firstSelection + "]";
    }



}
