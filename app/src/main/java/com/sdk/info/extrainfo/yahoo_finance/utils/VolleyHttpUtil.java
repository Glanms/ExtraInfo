package com.sdk.info.extrainfo.yahoo_finance.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdk.info.extrainfo.yahoo_finance.bean.FinanceInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/10.
 * 使用Volley进行网络请求
 */
public class VolleyHttpUtil {

    private Context mcontext =null;
    private static RequestQueue queue = null;
    private static String response = null;
    private List<FinanceInfo> infoList = new ArrayList<>();

    public  VolleyHttpUtil(Context context,String url) {
        this.mcontext =context;
        // 只需新建一个队列对象即可
       queue = Volley.newRequestQueue(mcontext);
        System.out.print(queue);
        String name = getString(url);
        System.out.println(name);
    }

    public static RequestQueue getRequestQueue() {
        if ( queue!= null) {
            return queue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * 获取全部内容
     * @param url 请求url
     */
    public static String getString(String url){
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
              //返回结果
                response = s;
                Log.d("VolleyTAG:","reponse--"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               Log.e("VolleyTAG:","Error--"+volleyError.getMessage(),volleyError);
            }
        });
        queue.add(stringRequest);
        return response;
    }

    /**
     * 获取JsonObject
     * @param url
     */
    public String getJsonObject(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("VolleyTAG:", "reponse--" + jsonObject);
                response = jsonObject.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("VolleyTAG:","Error--" +volleyError);
            }
        });
        queue.add(jsonObjectRequest);
        return response;
    }

    /**
     * 获取JsonArray
     * @param url
     */
    public String getJsonArray(String url) {
        JsonArrayRequest jsonArraayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("VolleyTAG:", "reponse--" + jsonArray);
                response = jsonArray.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("VolleyTAG:", "Error--" + volleyError);
            }
        });
        queue.add(jsonArraayRequest);
        return response;
    }
}
