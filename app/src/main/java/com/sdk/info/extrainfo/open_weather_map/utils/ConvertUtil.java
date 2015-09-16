package com.sdk.info.extrainfo.open_weather_map.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2015/8/5.
 * 经纬度到地址的反向解析
 */
public class ConvertUtil {
    private static final String baseUrl =
            "http://maps.google.com/maps/api/geocode/json?latlng=";
    public static String getAddress(final double longtitude,final double latitude)
    {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
            @Override
            public String call() throws Exception {
                //定义一个HttpClient，用于向指定地址发送请求
                HttpClient client = new DefaultHttpClient();
                String url= baseUrl + latitude + "," + longtitude + "&sensor=false";
                //向指定地址发送GET请求
                HttpGet httpGet = new HttpGet(url);
                //模拟请求来自的区域，现在是中文
//                httpGet.addHeader("Accept-Charset","utf-8;q=0.7,*;q=0.3");
                httpGet.addHeader("Accept-Charset","GBK;q=0.7,*;q=0.3");
//                httpGet.addHeader("Accept-Language","tr,tr;q=0.8");
                httpGet.addHeader("Accept-Language","zh_CN,zh;q=0.8");
                StringBuilder sb = new StringBuilder();
                //执行请求
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                //获取服务器响应的字符串
                InputStreamReader br = new InputStreamReader(
                        entity.getContent() , "utf-8"
                );
                int b;
                while ((b = br.read()) != -1)
                {
                    sb.append((char)b);
                }
                //把服务器请求的结果转换为JSONObject
                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray array = jsonObject.optJSONArray("results");
                JSONObject obj = array.optJSONObject(0);
                //当前城市
                String city = obj.optString("formatted_address");
                return city;
            }
        });
        new Thread(task).start();
        try {
            return task.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
