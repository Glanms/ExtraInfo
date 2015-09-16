package com.sdk.info.extrainfo.open_weather_map.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sdk.info.extrainfo.R;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/8/1.
 * 定位工具类
 */
public class LocationUtils implements LocationListener{
    /**
     * 定位方式的provider
     */
    private String provider;
    /**
     * 定位到的当前城市
     */
    private String localCityStr = null;
    /**
     * 位置管理类
     */
    private LocationManager locationManager = null;
    private Location location = null;
    /**
     * 当前位置纬度
     */
    private double latitude = 0.0;
    /**
     * 当前位置经度
     */
    private double longitude = 0.0;
    private Context mcontext;
    private final String TAG = "LocationUtils" ;
    //Location Listener
    private final int minTime = 1000;//ms
    private final int minDist = 0;//meter
    //通过network获取location
    private String networkProvider = LocationManager.NETWORK_PROVIDER;
    //通过gps获取location
    private String GpsProvider = LocationManager.GPS_PROVIDER;
    LocationListener locationListener = null;
    private String cityInfo = null;
    /**
     * 定位失败时文字
     */
    private String errorStr= null;

    public LocationUtils(Activity activity) {
        this.mcontext = activity;
        //locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public String getLocation(){

        initLocation(mcontext);
        return localCityStr;
    }

    /**
     *  获取location对象
     *  实际通过检测网络状态设置定位provider
     */
    public void initLocation(Context mContext){
        //获得系统及服务的  LocationManager 对象  这个代码就这么写 不用考虑
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        errorStr = mContext.getResources().getString(R.string.no_city_found);

        //首先检测 通过network 能否获得location对象
        //如果获得了location对象 则更新tv
        if (startLocation(networkProvider,mContext)) {
//            location = locationManager.getLastKnownLocation(networkProvider);
            Log.d(TAG,"NetworkProvider");
            updateLocation(location, mContext);
        }else
            //通过gps 能否获得location对象
            //如果获得了location对象 则更新tv
            if(startLocation(GpsProvider,mContext)){
                Log.d(TAG,"GpsProvider");
//                location = locationManager.getLastKnownLocation(GpsProvider);
                updateLocation(location,mContext);
            }else{
                //如果上面两种方法都不能获得location对象 则显示下列信息
                showToast(mcontext.getResources().getString(R.string.no_provider_label));
            }
    }

    /**
     * 通过参数 获取Location对象
     * 如果Location对象为空 则返回 true 并且赋值给全局变量 location
     *   如果为空 返回false 不赋值给全局变量location
     *
     * @param provider
     * @param mContext
     * @return
     */
    private boolean startLocation(String provider,final Context mContext){
         location = locationManager.getLastKnownLocation(provider);
         Log.d(TAG,"Provider:"+provider+", Location:"+location);

        // 位置监听器
        locationListener = new LocationListener() {
            // 当位置改变时触发
            @Override
            public void onLocationChanged(Location location) {
                System.out.println(location.toString());
                updateLocation(location,mContext);
            }

            // Provider失效时触发
            @Override
            public void onProviderDisabled(String arg0) {
                System.out.println(arg0);
            }

            // Provider可用时触发
            @Override
            public void onProviderEnabled(String arg0) {
                System.out.println(arg0);
            }

            // Provider状态改变时触发
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                Log.d(TAG,"onStatusChanged");
            }
        };

        // 1.5秒更新一次，忽略位置变化
        locationManager.requestLocationUpdates(provider, minTime, minDist, locationListener);

//		如果Location对象为空 则返回 true 并且赋值给全局变量 location
//		如果为空 返回false 不赋值给全局变量location
        if (location!= null) {
            this.location=location;
            return true;
        }
        return false;

    }

    // 更新位置信息 展示到tv中
    private void updateLocation(Location location,Context mContext) {
        if (location != null) {
            Log.d("Location","定位对象信息如下：" + location.toString() + "\n\t其中经度："
                    + location.getLongitude() + "\n\t其中纬度："
                    + location.getLatitude());
//            localCityStr = getAddressByGeoPoint(location);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            location = locationManager.getLastKnownLocation(provider);

                localCityStr = ConvertUtil.getAddress(longitude, latitude);
                cityInfo = "定位对象信息如下：" + location.toString() + "\n\t其中经度："
                        + location.getLongitude() + "\n\t其中纬度："
                        + location.getLatitude();

            //如果已经获取到location信息 则在这里注销location的监听
            //gps会在一定时间内自动关闭
            locationManager.removeUpdates(locationListener);
        } else {
            System.out.println("没有获取到定位对象Location");
        }
    }

    /**
     * 初始化Provider
     * @return
     */
 /*   private boolean initLocationProvider() {
        //  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //1.选择最优提供器
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            Log.d(TAG, "--最优定位方式--：" + provider);
            showToast("--最优定位方式--" + provider);
            return true;
        }

        //2.选用GPS提供器
   *//*     if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            Log.d(TAG, "--Gps定位方式--：" + provider);
            showToast("--Gps定位方式--" + provider);
            return true;
        }

        //3、选用NetWork提供器
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, "--Net定位方式--：" + provider);
            showToast("--Net定位方式--" + provider);
            return true;
        }*//*
        return false;
    }*/


    /**
     * 获取经纬度
     */
  /*  public void getGeocode(LocationManager locationMgr) {

            //取得上次已知的位置
            Location location = locationManager.getLastKnownLocation(provider);
            // updateWithNewLocation(location);

            //GPS Listener
            locationManager.addGpsStatusListener(gpsListener);

            locationManager.requestLocationUpdates(provider, minTime, minDist, this);

        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        List<String> lp = locationMgr.getAllProviders();
        for (String item : lp) {
            Log.d(TAG, "可用位置服务："+item);
        }

//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
//        location = locationManager.getLastKnownLocation(provider);

        //设定double精度，可以不用
        DecimalFormat dcmFmt = new DecimalFormat("0.0000");
        if (location != null) {
            latitude = Double.parseDouble(dcmFmt.format(location.getLatitude()));
            longitude = Double.parseDouble(dcmFmt.format(location.getLongitude()));
//            longitude = location.getLongitude();
            Log.d(TAG, "定位方式： " +
                    provider + "  纬度：" + latitude + "  经度：" + longitude);
//            test_tv.setText("定位方式： " +
//                    provider + "  纬度：" + latitude + "  经度：" + longitude);

        }
        //定义一个存放经纬度的数组
        double geocode[] = new double[]{latitude, longitude};
        //localCityStr = getAddressByGeoPoint(geocode);

    }*/


    /**
     * 由经纬度得到当前地址名
     * @param location
     * @return address.locality
     */
    public String getAddressByGeoPoint(Location location) {
        Geocoder geocoder = new Geocoder(mcontext, Locale.CHINA);
        if (location != null) {
            try {
                List<Address> list = geocoder.
                        getFromLocation(location.getLatitude(), location.getLongitude(), 3);
                for (Address address : list) {
                    String cityName = address.getLocality().toString();
                  //  test_tv.append("地址：" + address.getLocality().toString() + "\n ");
                    Log.d(TAG, "当前城市："+cityName);
                    return cityName;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"位置信息为空！");
            }
        }
            return mcontext.getResources().getString(R.string.no_city_found);
    }

    /**
     * 开启位置请求
     */
    public void requsetListener(){
      /*  if(location ==null) {
            locationManager.requestLocationUpdates(provider, minTime, minDist, this);
        }*/
    }

    /**
     * 停止一直请求位置
     */
    public void removeListener(){
      /*  if(location!=null){
            locationManager.removeUpdates(this);
        }*/
    }

    //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "Location changed : ChLat: "
                    + location.getLatitude() + " ChLng: "
                    + location.getLongitude());

//            test_tv.append("定位方式： " + provider + "  Ch纬度：" + latitude + "  Ch经度：" + longitude);
//                    initLocationProvider();
        }else {
            //whereAmI();
            Log.d(TAG,"位置不变");
        }
    }

    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.d(TAG, "Status Changed: OUT_OF_SERVICE");
                showToast("Status Changed: OUT_OF_SERVICE");
                break;
            case LocationProvider.AVAILABLE:
                Log.d(TAG, "Status Changed: AVAILABLE");
                showToast("Status Changed: AVAILABLE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d(TAG, "Status Changed: TEMPORARILY_UNAVAILABLE");
                showToast("Status Changed: TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    // Provider被enable时触发此函数，比如GPS被打开
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, provider+"打开");
    }

    // Provider被disable时触发此函数，比如GPS被关闭
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, provider+"关闭");
    }

    //show Toast消息
    public void showToast(String str){
        Toast.makeText(mcontext,str,Toast.LENGTH_SHORT).show();
    }


    private void whereAmI(){
        //取得上次已知的位置
        location = locationManager.getLastKnownLocation(provider);
       // updateWithNewLocation(location);

        //GPS Listener
        locationManager.addGpsStatusListener(gpsListener);

        //Location Listener
        int minTime = 5000;//ms
        int minDist = 5;//meter
        locationManager.requestLocationUpdates(provider, minTime, minDist, this);
    }
        // GPS监听器

    GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
        switch (event)
        {
            case GpsStatus.GPS_EVENT_STARTED:
                //定位启动
                Log.d(TAG, "GPS_EVENT_STARTED");
                showToast("定位启动");
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                  //定位结束
                Log.d(TAG, "GPS_EVENT_STOPPED");
                showToast("定位结束");
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                //第一次定位
                Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                showToast("第一次定位");
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //卫星状态改变
                Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
              //  showToast("卫星状态改变");
                GpsStatus gpsStatus= locationManager.getGpsStatus(null); // 取当前状态
                int maxSatellites = gpsStatus.getMaxSatellites(); //获取卫星颗数的默认最大值
                //获取卫星
                Iterable<GpsSatellite> iterable=gpsStatus.getSatellites();
                //一般再次转换成Iterator
                Iterator<GpsSatellite> it=iterable.iterator();
                int count = 0;
                while (it.hasNext() && count <= maxSatellites) {
                    count++;
                    GpsSatellite s = it.next();
                }
                showToast("搜索到："+count+"颗卫星");

            break;
        }
        }
    };

}
