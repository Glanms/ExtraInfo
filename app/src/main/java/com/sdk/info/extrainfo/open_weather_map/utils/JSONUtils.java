package com.sdk.info.extrainfo.open_weather_map.utils;

import com.sdk.info.extrainfo.open_weather_map.bean.ConditionBean;
import com.sdk.info.extrainfo.open_weather_map.bean.ForcastsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/18.
 * 解析JSON获取数据工具类
 */
public class JSONUtils {

    private static final String baseUrl = "http://openweathermap.org/img/w/";
    /**
     *  解析实时天气
     * @param key
     * @param jsonStr
     * @return
     */
    public ConditionBean parseCurWeather(String key,String jsonStr){
        ArrayList<ConditionBean> list = new ArrayList<ConditionBean>();
        try {
            // 天气Bean
            ConditionBean bean = new ConditionBean();
            //获取json对象：city，（最外层，获取json来源字符串）
            JSONObject jsonObject = new JSONObject(jsonStr);
            //由json对象获得JSONArray：weather
            JSONArray array = jsonObject.optJSONArray("weather");
            //遍历获取每一个jsonObject
            JSONObject weatherObj = array.optJSONObject(0);
            //设置当天状态
            bean.setCurState(weatherObj.optString("main"));
            //获取当前天气图标
            String iconStr = weatherObj.optString("icon");
            String url = baseUrl + iconStr +".png";
            bean.setCurIcon(HttpUtils.getHttpBitmap(url));
            //设置当前天气情况
            bean.setCityName(jsonObject.optString("name"));
            //获取json对象：main
            JSONObject mainObj = jsonObject.optJSONObject("main");
            //设置当前温度、最低温、最高温
            bean.setCurTemp(mainObj.optDouble("temp"));
            bean.setCurMinTemp(mainObj.optDouble("temp_min"));
            bean.setCurMaxTemp(mainObj.optDouble("temp_max"));
            System.out.print(""+bean);
           list.add(bean);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list.get(0);
    }

    /**
     * 解析天气预报结果
     * @param key
     * @param jsonStr Url
     * @return
     */
    public ArrayList<ForcastsBean> parseFurWeather(String key,String jsonStr) {
        ArrayList<ForcastsBean> furList = new ArrayList<ForcastsBean>();
        try{
            //将jsonStr字符串转化为JsonObject
            JSONObject object = new JSONObject(jsonStr);
            //获取子标签list
            JSONArray listArray = object.optJSONArray("list");
            //从list中解析出最低温度、最高温度、天气状况
            for(int i = 0;i<listArray.length();i++)
            {
                // 天气Bean
                ForcastsBean bean = new ForcastsBean();
                JSONObject itemObj = listArray.getJSONObject(i);
                Double wind = itemObj.optDouble("speed");
                bean.setFurWind(wind);
                //解析list子标签temp下的数据
                JSONObject tempObj = itemObj.optJSONObject("temp");
                Double minTemp = tempObj.optDouble("min");
                bean.setFurMinTemp(minTemp);
                Double maxTemp = tempObj.optDouble("max");
                bean.setFurMaxTemp(maxTemp);
                //解析list子标签weather数组下的数据
                JSONArray array = itemObj.optJSONArray("weather");
                JSONObject stateObj = array.optJSONObject(0);
                String state = stateObj.optString("main");
                bean.setFurState(state);
                //获取预报天气图标
                String iconStr = stateObj.optString("icon");
                String url = baseUrl + iconStr +".png";
                bean.setFurIcon(HttpUtils.getHttpBitmap(url));
                furList.add(bean);
            }


        }catch (Exception e){
            e.printStackTrace();
        }



       return furList;
    }
}
