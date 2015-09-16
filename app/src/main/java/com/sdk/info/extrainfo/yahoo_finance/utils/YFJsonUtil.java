package com.sdk.info.extrainfo.yahoo_finance.utils;

import android.util.Log;

import com.sdk.info.extrainfo.yahoo_finance.bean.FinanceInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/8.
 * Parse Yohoo Finance Json Data
 *
 */
public class YFJsonUtil {

    private List<FinanceInfo>infoList=null;
    private FinanceInfo info = new FinanceInfo();
    public YFJsonUtil(ArrayList<FinanceInfo> list) {
        this.infoList = list;
    }

    /**
     * 汇率请求来源
     */
    public static final String BaseUrl1 = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22";
    public static final String BaseUrl2 = "%22)&env=store://datatables.org/alltableswithkeys&format=json";
    /**
     * 土耳其指数来源
     */
    public static final String sourceUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where" +
            "%20url%3D'http%3A%2F%2Fcn.investing.com%2Findices%2Fise-100'%20and%20xpath%3D'%2F%2F*%5B%40id%3D%22last_last%22%5D'" +
            "&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
    private static final String TurCur = "";
    public static final String paramsEuro = "EURTRY";
    public static final String paramsDolar = "USDTRY";
    private static final String AltinCur = "";

    /**
     * @param param 请求地址中的货币参数
     * @throws Exception
     */
    public void getRate(String param,String jsonStr) throws Exception {
        Log.d("YFUtil", "--通过YQL得到汇率 --");
         // Qurey Basic Structure
        JSONObject rootObj = new JSONObject(jsonStr);
        JSONObject queryObj = rootObj.optJSONObject("query");
        JSONObject resultObj = queryObj.optJSONObject("results");
        //parse results
        JSONObject rateObj = resultObj.optJSONObject("rate");
        String rate = rateObj.optString("Rate");
        Log.d("YFUtil", "-- 得到"+param+"汇率： --" +rate);
        switch (param)
        {
            case YFJsonUtil.paramsEuro:
                info.setEuRate(rate);
                break;
            case YFJsonUtil.paramsDolar:
                info.setUsRate(rate);
                break;
            default:
                throw new Exception("Null Params Error!");
        }

    }

    public void getTurIndex(String jsonStr) throws Exception {
        if (jsonStr != null) {
            try {
            JSONObject object = new JSONObject(jsonStr);
            JSONObject queryObj = object.optJSONObject("query");
            JSONObject resultObj = queryObj.optJSONObject("results");
            JSONObject spanObj = resultObj.optJSONObject("span");
            String indexStr = spanObj.optString("content");//土耳其指数
                float index = 0;
                //格式化数据,去掉千分号
                CharSequence sequence = ",";
                if(indexStr.contains(sequence)){
                    String newIndexStr = indexStr.replaceAll(",", "");
                    index = Float.parseFloat(newIndexStr);
                }
//                DecimalFormat df = new DecimalFormat();
//                df.applyPattern();
//            float index = Float.valueOf(indexStr);   //土耳其指数
            Log.d("YFUtil", "-- 得到指数： --" + indexStr);
                info.setBist(index);
            infoList.add(info);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                Log.e("YFJsonUtil:","Index json parse error!");
            }
        }
    }
}
