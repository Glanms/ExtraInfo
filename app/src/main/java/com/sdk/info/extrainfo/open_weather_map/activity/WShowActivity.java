package com.sdk.info.extrainfo.open_weather_map.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdk.info.extrainfo.R;
import com.sdk.info.extrainfo.open_weather_map.bean.ConditionBean;
import com.sdk.info.extrainfo.open_weather_map.bean.Contants;
import com.sdk.info.extrainfo.open_weather_map.bean.ForcastsBean;
import com.sdk.info.extrainfo.open_weather_map.utils.DateUtils;
import com.sdk.info.extrainfo.open_weather_map.utils.HttpUtils;
import com.sdk.info.extrainfo.open_weather_map.utils.JSONUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * OpenWeatherMap获取天气
 */
public class WShowActivity extends ActionBarActivity implements View.OnClickListener {
    private ConditionBean bean;
    private ForcastsBean foreBean;
    ArrayList<ForcastsBean> list = new ArrayList<>();
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private TextView city_name;
    private TextView cur_temp;
    private TextView cur_range_temp;
    private TextView cur_state;
    private ImageView cur_icon;
    private Handler myHandler = null;
    private String cur_url; //请求的api地址
    private String fur_url; //天气渊博请求的地址
    /**
     * 预报的天气信息
     */
    private ImageView day1_icon;
    private ImageView day2_icon;
    private ImageView day3_icon;
    private TextView day1_state;
    private TextView day2_state;
    private TextView day3_state;
    private TextView day1_temp;
    private TextView day2_temp;
    private TextView day3_temp;
    private TextView day1_wind;
    private TextView day2_wind;
    private TextView day3_wind;
    private TextView day1_date;
    private TextView day2_date;
    private TextView day3_date;
    private List<String> cityList;
    private  ProgressDialog dialog;
    private boolean isShowing = false;

    private int todayOfWeek;
    public static final String UPDATE_WIDGET_WEATHER_ACTION = "com.way.action.update_weather";
    private static final int LOACTION_OK = 0;
    private static final int UPDATE_EXISTS_CITY = 2;
    public static final int GET_WEATHER_SUCCESS = 3;
    public static final int GET_WEATHER_FAIL = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wshow);

        initView();
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);

    }

    /**
     * 初始化界面
     */
    public void initView()
    {
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        city_name = (TextView)findViewById(R.id.city_name);
        cur_temp = (TextView)findViewById(R.id.cur_temp);
        cur_range_temp = (TextView)findViewById(R.id.cur_range_temp);
        cur_state = (TextView)findViewById(R.id.cur_state);
        cur_icon = (ImageView)findViewById(R.id.cur_state_icon);
        bean = new ConditionBean();
        foreBean = new ForcastsBean();
        day1_icon = (ImageView)findViewById(R.id.forecast_day1_icon);
        day2_icon = (ImageView)findViewById(R.id.forecast_day2_icon);
        day3_icon = (ImageView)findViewById(R.id.forecast_day3_icon);
        day1_state = (TextView)findViewById(R.id.forecast_day1_state);
        day2_state = (TextView)findViewById(R.id.forecast_day2_state);
        day3_state = (TextView)findViewById(R.id.forecast_day3_state);
        day1_temp = (TextView)findViewById(R.id.forecast_day1_temp);
        day2_temp = (TextView)findViewById(R.id.forecast_day2_temp);
        day3_temp = (TextView)findViewById(R.id.forecast_day3_temp);
        day1_wind = (TextView)findViewById(R.id.forecast_day1_wind);
        day2_wind = (TextView)findViewById(R.id.forecast_day2_wind);
        day3_wind = (TextView)findViewById(R.id.forecast_day3_wind);
        day1_date = (TextView)findViewById(R.id.forecast_day1_date);
        day2_date = (TextView)findViewById(R.id.forecast_day2_date);
        day3_date = (TextView)findViewById(R.id.forecast_day3_date);
        todayOfWeek = DateUtils.getToday();
        String cities[] = new String[]{"北京市",Contants.PARAM2,
                        Contants.PARAM3,Contants.PARAM4, Contants.PARAM5};
        cityList = new ArrayList<String>();
        for (int i=0; i<5;i++){
            cityList.add(i,cities[i]);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn1:
                cur_url = Contants.API_WEATHER_URL + Contants.PARAM1;
                fur_url = Contants.API_FORECAST_URL + Contants.PARAM1;
                getData();
                break;
            case R.id.btn2:
                cur_url = Contants.API_WEATHER_URL + Contants.PARAM2;
                fur_url = Contants.API_FORECAST_URL + Contants.PARAM2;
                getData();
                break;
            case R.id.btn3:
                cur_url = Contants.API_WEATHER_URL + Contants.PARAM3;
                fur_url = Contants.API_FORECAST_URL + Contants.PARAM3;
                getData();
                break;
            case R.id.btn4:
                cur_url = Contants.API_WEATHER_URL + Contants.PARAM4;
                fur_url = Contants.API_FORECAST_URL + Contants.PARAM4;
                getData();
                break;
            case R.id.btn5:
                cur_url = Contants.API_WEATHER_URL + Contants.PARAM5;
                fur_url = Contants.API_FORECAST_URL + Contants.PARAM5;
                getData();
                break;
            case R.id.btn6:
                Intent _intent = new Intent(WShowActivity.this,Select2Activity.class);
                startActivityForResult(_intent, 0x112);
                break;
        }
    }

    public void showSpinner(){
        dialog = new ProgressDialog(this,R.style.myDialog);
        LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());
        RelativeLayout dialog_view = (RelativeLayout)inflater.inflate(R.layout.dialog_view, null);
        dialog.show();
        dialog.getWindow().setContentView(dialog_view);
        isShowing = true;
    }

    public void dismissSpinner(){
        if(isShowing){
            dialog.dismiss();
        }
    }


    //开始获取
    public void onRefreshStart(){
        //判断网络条件
       /* if(){
           getData();
        }*/
        showSpinner();
    }
    //正在刷新
    public void onRefreshing(){
      showSpinner();

    }

    //加载成功
    public void onRefreshSuccess(){
        showToast("获取成功！");
        dismissSpinner();
    }
    //加载失败
    public void onRefreshFail(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(requestCode == 0x112 && resultCode ==0x111)
      {
          String cityName = data.getStringExtra("cityId");
          Toast.makeText(this,"城市名："+cityName,Toast.LENGTH_SHORT).show();
          //通过返回值查找相应的cityNo，进行刷新
          if(cityList.indexOf(cityName)==0)  //测试下北京市
          {
              cur_url = Contants.API_WEATHER_URL + Contants.PARAM1;
              fur_url = Contants.API_FORECAST_URL + Contants.PARAM1;
              getData();
          }

      }
    }

    /**
     * 点击后的异步处理数据
     */
    public void getData(){
        onRefreshStart();
        new MyThread().start();
        myHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 0x123:  //点击按钮一
                        //获取Message中的Bundle
                        Bundle b = msg.getData();
                        if(b.getString("city") != null) {
                            onRefreshSuccess();
                            city_name.setText(b.getString("city").toString());
                            cur_temp.setText("" + b.getFloat("curTemp") + " ℃");
                            cur_range_temp.setText("" + b.getFloat("curMinTemp")
                                    + "℃/" + b.getFloat("curMaxTemp") + "℃");
                            cur_state.setText(b.getString("curState").toString());
//                                    Log.d("TAG:Bundle-","No Value");
                            String iconStr = b.getString("curIcon");
                            cur_icon.setImageBitmap(convertStringToIcon(iconStr));
                         //   showToast(""+iconStr);
                            list = b.getParcelableArrayList("furList");
                            // day1_icon.
                            day1_state.setText(list.get(0).getFurState());
                            day2_state.setText(list.get(1).getFurState());
                            day3_state.setText(list.get(2).getFurState());
                            day1_temp.setText("" + list.get(0).getFurMinTemp()
                                    + "℃/" + list.get(0).getFurMaxTemp() + "℃");
                            day2_temp.setText("" + list.get(1).getFurMinTemp()
                                    + "℃/" + list.get(1).getFurMaxTemp() + "℃");
                            day3_temp.setText("" + list.get(2).getFurMinTemp()
                                    + "℃/" + list.get(2).getFurMaxTemp() + "℃");

                            day1_wind.setText("Wind:" + list.get(0).getFurWind());
                            day2_wind.setText("Wind:" + list.get(1).getFurWind());
                            day3_wind.setText("Wind:" + list.get(2).getFurWind());
                            day1_icon.setImageBitmap(list.get(0).getFurIcon());
                            day2_icon.setImageBitmap(list.get(1).getFurIcon());
                            day3_icon.setImageBitmap(list.get(2).getFurIcon());
                            getWeek(todayOfWeek);
                            day1_date.setText(list.get(0).getFurDate());
                            day2_date.setText(list.get(1).getFurDate());
                            day3_date.setText(list.get(2).getFurDate());
                        }else {
                            Toast.makeText(getApplicationContext(),"无数据了",Toast.LENGTH_SHORT).show();
                        }
                }
            }
        };


    }

    public void getWeek(int todayOfWeek){
        switch (todayOfWeek)
        {
            case Calendar.MONDAY:
                list.get(0).setFurDate(getString(R.string.week_Thes));
                list.get(1).setFurDate(getString(R.string.week_Wed));
                list.get(2).setFurDate(getString(R.string.week_Thur));
                break;
            case Calendar.TUESDAY:
                list.get(0).setFurDate(getString(R.string.week_Wed));
                list.get(1).setFurDate(getString(R.string.week_Thur));
                list.get(2).setFurDate(getString(R.string.week_Fri));
                break;
            case Calendar.WEDNESDAY:
                list.get(0).setFurDate(getString(R.string.week_Thur));
                list.get(1).setFurDate(getString(R.string.week_Fri));
                list.get(2).setFurDate(getString(R.string.week_Sat));
                break;
            case Calendar.THURSDAY:
                list.get(0).setFurDate(getString(R.string.week_Fri));
                list.get(1).setFurDate(getString(R.string.week_Sat));
                list.get(2).setFurDate(getString(R.string.week_Sun));
                break;
            case Calendar.FRIDAY:
                list.get(0).setFurDate(getString(R.string.week_Sat));
                list.get(1).setFurDate(getString(R.string.week_Sun));
                list.get(2).setFurDate(getString(R.string.week_Mon));
                break;
            case Calendar.SATURDAY:
                list.get(0).setFurDate(getString(R.string.week_Sun));
                list.get(1).setFurDate(getString(R.string.week_Mon));
                list.get(2).setFurDate(getString(R.string.week_Thes));
                break;
            case Calendar.SUNDAY:
                list.get(0).setFurDate(getString(R.string.week_Mon));
                list.get(1).setFurDate(getString(R.string.week_Thes));
                list.get(2).setFurDate(getString(R.string.week_Wed));
                break;

        }

    }

   public class MyThread extends Thread{
        JSONUtils jsonUtils = new JSONUtils();
        HttpUtils httpUtils = new HttpUtils();
       ArrayList<ForcastsBean>arrayList = new ArrayList<>();

        @Override
        public void run() {
            super.run();
            // 拼接成完整URL
            try {
                //网络请求获取json字符串
                String jsonString1 = httpUtils.getJsonContent(cur_url);
                bean = jsonUtils.parseCurWeather(null, jsonString1);
                //请求天气预报数据
                String forecastStr = httpUtils.getJsonContent(fur_url);
                arrayList = jsonUtils.parseFurWeather(null,forecastStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 0x123;
            // Bundle存放数据
            Bundle bundle = new Bundle();
//            if(!TextUtils.isEmpty(bean.getCityName())) {
                bundle.putString("city", bean.getCityName());
                bundle.putFloat("curTemp", bean.getCurTemp());
                bundle.putFloat("curMinTemp", bean.getCurMinTemp());
                bundle.putFloat("curMaxTemp", bean.getCurMaxTemp());
                bundle.putString("curState", bean.getCurState());
            //Bitmap 转为String
            String iconStr = convertIconToString(bean.getCurIcon());
                bundle.putString("curIcon", iconStr);
//            }else {
//                Toast.makeText(getApplicationContext(),"无数据了",Toast.LENGTH_SHORT).show();
//            }
            if(arrayList!=null) {
                bundle.putParcelableArrayList("furList", arrayList);
            }else{
                Log.d("FurList:","Null");
            }
            msg.setData(bundle);
//            msg.obj =
            //指定当前Activity的Handler的sendMessage发送
            WShowActivity.this.myHandler.sendMessage(msg);
        }
    }

    /**
     * Bitmap 转为 String
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void showToast(String str)
    {
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }
}
