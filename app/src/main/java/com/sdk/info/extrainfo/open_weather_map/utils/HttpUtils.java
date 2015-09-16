package com.sdk.info.extrainfo.open_weather_map.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/7/18.
 * 处理网络请求，得到JSON
 */
public class HttpUtils {

    //定义一个静态方法来发起请求
    public String getJsonContent(String path){

        try {
            //根据路径创建URL
            URL url = new URL(path);
            //通过url打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时
            conn.setConnectTimeout(10000);
            //设置请求方式
            conn.setRequestMethod("GET");
            //设置代理
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; " +
//                            "Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729;" +
//                            " .NET CLR 3.0.30729; Media Center PC 6.0; " +
//                            ".NET4.0C; .NET4.0E)");
            //设置该连接是否可输入
            //conn.setDoInput(true);
            int code = conn.getResponseCode();
            System.out.println(code+"****");
            if(code == 200)
            {
                return changeInputString(conn.getInputStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //自定义IO流获取字符串
    public static String changeInputString(InputStream is){
        String jsonString = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = is.read(data))!= -1) {
                baos.write(data,0,len);
            }
            jsonString = new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    //根据天气状况json获取icon
    public static Bitmap getHttpBitmap(String url) {
        URL icon_url;
        Bitmap bitmap = null;
        try {
            icon_url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)icon_url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

}
