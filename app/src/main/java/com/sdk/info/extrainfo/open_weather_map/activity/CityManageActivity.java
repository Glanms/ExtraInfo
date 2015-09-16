package com.sdk.info.extrainfo.open_weather_map.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdk.info.extrainfo.R;
import com.sdk.info.extrainfo.open_weather_map.adapter.CityAdapter;
import com.sdk.info.extrainfo.open_weather_map.bean.City;
import com.sdk.info.extrainfo.open_weather_map.utils.LocationUtils;
import com.sdk.info.extrainfo.open_weather_map.utils.T;
import com.sdk.info.extrainfo.open_weather_map.view.BladeView;
import com.sdk.info.extrainfo.open_weather_map.view.ClearEditText;
import com.sdk.info.extrainfo.open_weather_map.view.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityManageActivity extends AppCompatActivity {

    private EditText search_edt;
    private ClearEditText search_bar;
    private TextView local_city;
    private ListView city_lv;
    private TextView test_tv;
    /**
     * 热门城市数据集
     */
    List<City> hot = new ArrayList<>();
    /**
     * 全部城市数据集
     */
    List<City> all = new ArrayList<>();
    /**
     * 总的数据集
     */
    List<City> plist = new ArrayList<>();
    private Button btn;
    //    ListAdapter allListAdapter;
    private List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> splitList = new ArrayList<Map<String, String>>();
    private LayoutInflater mInflater;
    private String[] city = {"伊斯坦布尔", "安卡拉", "伊兹密尔", "布尔萨", "阿达纳",
            "加济安泰普", "科尼亚", "安塔利亚", "迪亚巴克尔",
            "梅尔辛", "开塞利", "萨姆松", "埃斯基谢希尔"};
    private City cityBean = null;
    private String cityId;
    /**
     * 定位方式provider
     */
    private String provider = null;
    private static final String FORMAT = "^[a-z,A-Z].*$";
    private PinnedHeaderListView mListView;
    private BladeView mLetter;
    /**
     * 全部城市数组
     */
    private String[] all_cities;
    /**
     * 热门城市数组
     */
    private String[] hot_cities;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<String>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private CityAdapter mAdapter;
    private T toast = null;
    /**
     * 位置操作类Location
     */
    private LocationManager locationManager;
    private Location location;
    private LocationListener locationListener;
    /**
     * 由经纬度计算出的当前城市
     */
    private String curCity = null;
    private Context context;
    private LocationUtils locationUtils;

    private static final String TAG = "城市选择页:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        context = this;
        toast = new T(context);
        initData();
        initView();

        locationUtils = new LocationUtils(this);

  /*      curCity = locationUtils.getLocation();

        // 对于请求结果字符串的处理
        if (curCity != null) {
            allListAdapter.getItem(1).put("item", curCity);
        } else {
            allListAdapter.getItem(1).put("item", getString(R.string.no_localcity_tip));
        }
        allListAdapter.notifyDataSetChanged();*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
                curCity = locationUtils.getLocation();
                //allListAdapter.getItem(1).put("item", curCity);
                //allListAdapter.notifyDataSetChanged();
//                }catch (Exception e)
//                {
//                    Log.e(TAG,"获取不到位置！");
//
            }
        });

    }

    /**
     * 设置LitView中显示的数据
     */
    private void initData() {
        // 初始化数组数据
        all_cities = getResources().getStringArray(R.array.countries);
        hot_cities = new String[]{"北京", "上海", "成都", "广州"};

        for (int i = 0; i < hot_cities.length; i++) {
            cityBean = new City();
            cityBean.setName(hot_cities[i]);
            cityBean.setId("000" + i);
            hot.add(cityBean);
        }
        for (int i = 0; i < all_cities.length; i++) {
            cityBean = new City();
            cityBean.setName(all_cities[i]);
            cityBean.setId("000" + i);
            all.add(cityBean);
        }
        plist.addAll(hot);
        plist.addAll(all);

        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<String>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();

        for (int i = 0; i < plist.size(); i++) {
            String firstName = plist.get(i).getName().substring(0, 1);
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(plist.get(i).getName());
                } else {
                    mSections.add(firstName);
                    List<String> list = new ArrayList<String>();
                    list.add(plist.get(i).getName());//
                    mMap.put(firstName, list);
                }
            } else {              //顶部 当前城市 和热门城市显示
                if (mSections.contains("#")) {
                    mMap.get("#").add(plist.get(i).getName());
                } else {
                    mSections.add("#");
                    List<String> list = new ArrayList<String>();
                    list.add(plist.get(i).getName());
                    mMap.put("#", list);

                }

            }
        }

     /*   for(int i = 0;i<hot_cities.length;i++){
            if (mSections.contains("热门城市")) {
                mMap.get("#").add(hot_cities[i]);
            }  else {
                mSections.add("#");
                List<String> list = new ArrayList<String>();
                list.add(hot_cities[i]);
                mMap.put("#", list);
//                    mMap.put("#", list);
            }
        }*/


        Collections.sort(mSections);
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
    }

    /**
     * 初始化界面
     */
    public void initView() {
        mInflater = LayoutInflater.from(context);
         search_edt = (EditText) findViewById(R.id.search_edt);
       /* search_edt.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, autoStr));*/

        //  local_city = (TextView)findViewById(R.id.local_city);
        //  city_lv = (ListView) findViewById(R.id.city_lv);
        search_bar = (ClearEditText) findViewById(R.id.filter_edit);
        test_tv = (TextView) findViewById(R.id.test_tv);
        test_tv.setFocusableInTouchMode(true);
        test_tv.requestFocus();
        btn = (Button) findViewById(R.id.btn1);
        //  allListAdapter = new ListAdapter(this, mylist, splitList);
        mListView = (PinnedHeaderListView) findViewById(R.id.friends_display);
        mLetter = (BladeView) findViewById(R.id.friends_myletterlistview);
        mLetter.setOnItemClickListener(new BladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                if (mIndexer.get(s) != null) {
                    mListView.setSelection(mIndexer.get(s));
                }
            }
        });


       // mAdapter = new CityAdapter(this, plist, mSections, mPositions);

        mListView.setAdapter(mAdapter);
        // getListViewHeight(mListView); //  去掉ScrollView可以不用计算了
        mListView.setOnScrollListener(mAdapter);
//        mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
//                R.layout.listview_head, mListView, false));
        //mListView.configureHeaderView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View vie2 = inflater.inflate(R.layout.toast_view, null);
                TextView tv = (TextView) vie2.findViewById(R.id.toast_text);
                tv.setText("点击了" + mAdapter.getItem(position));
                Toast toast1 = new Toast(context);
                toast1.setView(vie2);
                toast1.setGravity(Gravity.TOP, 50, 0);
                // toast1.setText("点击了"+mAdapter.getItem(position));
                toast1.setDuration(Toast.LENGTH_SHORT);

                toast1.show();
                // T.makeText(context,"点击了"+mAdapter.getItem(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onPause() {
        locationUtils.removeListener();
        super.onPause();
    }

    @Override
    protected void onResume() {
//        locationUtils.requsetListener();
        super.onResume();
    }

    @Override
    protected void onStop() {
        locationUtils.removeListener();
        super.onStop();
    }

    /**
     * 计算得到ListView的高
     *
     * @param listView
     */
    public void getListViewHeight(ListView listView) {
        //获取listView的Adapter
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View itemView = listAdapter.getView(i, null, listView);
            // 计算子项的高度
            itemView.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += itemView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        listView.setLayoutParams(params);
    }

    /**
     * 封装Toast的显示
     *
     * @param str
     */
    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置item中数据
     * mylist:
     */
    private void setData() {
        Map<String, String> map_city = new HashMap<String, String>();
        map_city.put("item", getString(R.string.local_city_label));
        mylist.add(map_city);
        splitList.add(map_city);

        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("item", "Beijing");
            mylist.add(map);
        }

        map_city = new HashMap<String, String>();
        map_city.put("item", getString(R.string.hot_city_label));
        mylist.add(map_city);
        splitList.add(map_city);

        for (int i = 0; i < 4; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("item", "Item 1-" + i);
            mylist.add(map);
        }
        map_city = new HashMap<String, String>();
        map_city.put("item", getString(R.string.all_city_label));
        mylist.add(map_city);
        splitList.add(map_city);
        for (int i = 0; i < city.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("item", city[i]);
            mylist.add(map);
        }
    }

    // 设置返回时城市的值
    public void setBackValue() {
        Intent backIntent = getIntent();
        backIntent.putExtra("cityId", cityId);
        setResult(0x111, backIntent);
    }

    // 返回时
    @Override
    public void onBackPressed() {
        if (cityId != null) {
            setBackValue();
        }
        super.onBackPressed();
    }


    //判断当前网络状态
    public boolean checkNetWork() {
        ConnectivityManager manager = (ConnectivityManager) this.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return true;
    }

}
