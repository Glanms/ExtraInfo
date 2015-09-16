package com.sdk.info.extrainfo.open_weather_map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sdk.info.extrainfo.R;
import com.sdk.info.extrainfo.open_weather_map.adapter.CityAdapter;
import com.sdk.info.extrainfo.open_weather_map.adapter.SearchCityAdapter;
import com.sdk.info.extrainfo.Application;
import com.sdk.info.extrainfo.open_weather_map.bean.City;
import com.sdk.info.extrainfo.open_weather_map.plistview.MyBladeView;
import com.sdk.info.extrainfo.open_weather_map.plistview.MyPinnedHeaderListView;

import java.util.List;
import java.util.Map;

public class Select2Activity extends AppCompatActivity implements TextWatcher,
        OnClickListener{

    private EditText mSearchEditText;
    private ImageButton mClearSearchBtn;
    private View mCityContainer;
    private View mSearchContainer;
    private MyPinnedHeaderListView mCityListView;
    private MyBladeView mLetter;
    private ListView mSearchListView;
    private List<City> mCities;
    private SearchCityAdapter mSearchCityAdapter;
    private CityAdapter mCityAdapter;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private Application mApplication;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select2);
      //  NetBroadcastReceiver.mListeners.add(this);

        initView();
        initData();

    }

    private void initView() {

  /*      mTitleTextView.setText(Application.getInstance()
                .getSharePreferenceUtil().getCity());*/

        mSearchEditText = (EditText) findViewById(R.id.search_edit);
        mSearchEditText.addTextChangedListener(this);
        mClearSearchBtn = (ImageButton) findViewById(R.id.ib_clear_text);
        mClearSearchBtn.setOnClickListener(this);

        mCityContainer = findViewById(R.id.city_content_container);
        mSearchContainer = findViewById(R.id.search_content_container);
        mCityListView = (MyPinnedHeaderListView) findViewById(R.id.citys_list);
//        mCityListView.setEmptyView(findViewById(R.id.citys_list_empty));
        mLetter = (MyBladeView) findViewById(R.id.citys_bladeview);
        mLetter.setOnItemClickListener(new MyBladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                if (mIndexer.get(s) != null) {
                    mCityListView.setSelection(mIndexer.get(s));
                }
            }
        });
        mLetter.setVisibility(View.GONE);
        mSearchListView = (ListView) findViewById(R.id.search_list);
        mSearchListView.setEmptyView(findViewById(R.id.search_empty));
        mSearchContainer.setVisibility(View.GONE);
        mSearchListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mInputMethodManager.hideSoftInputFromWindow(
                        mSearchEditText.getWindowToken(), 0);
                return false;
            }
        });
        mCityListView
                .setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d("搜索页：",mCityAdapter.getItem(position).toString());
                        //startActivity(mCityAdapter.getItem(position));
                    }
                });

        mSearchListView
                .setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d("搜索页：", mSearchCityAdapter.getItem(position).toString());
                       // startActivity(mSearchCityAdapter.getItem(position));
                    }
                });
    }
    private void startActivity(City city) {
        Intent i = new Intent();
      //  i.putExtra("city", city);
        setResult(RESULT_OK, i);
        finish();
    }

    private void initData() {
        mApplication = Application.getInstance();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mCities = mApplication.getCityList();
        mSections = mApplication.getSections();
        mMap = mApplication.getMap();
        mPositions = mApplication.getPositions();
        mIndexer = mApplication.getIndexer();

        mCityAdapter = new CityAdapter(Select2Activity.this, mCities, mMap,
                mSections, mPositions);
        mCityListView.setAdapter(mCityAdapter);
        mCityListView.setOnScrollListener(mCityAdapter);
        mCityListView.setPinnedHeaderView(LayoutInflater.from(
                Select2Activity.this).inflate(
                R.layout.owm_weather_list_group_item, mCityListView,
                false));
        mLetter.setVisibility(View.VISIBLE);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSearchCityAdapter = new SearchCityAdapter(Select2Activity.this,
                mCities);
        mSearchListView.setAdapter(mSearchCityAdapter);
        mSearchListView.setTextFilterEnabled(true);
        if (mCities.size() < 1 || TextUtils.isEmpty(s)) {
            mCityContainer.setVisibility(View.VISIBLE);
            mSearchContainer.setVisibility(View.INVISIBLE);
            mClearSearchBtn.setVisibility(View.GONE);
        } else {
            mClearSearchBtn.setVisibility(View.VISIBLE);
            mCityContainer.setVisibility(View.INVISIBLE);
            mSearchContainer.setVisibility(View.VISIBLE);
            mSearchCityAdapter.getFilter().filter(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // 如何搜索字符串长度为0，是否隐藏输入法
        // if(TextUtils.isEmpty(s)){
        // mInputMethodManager.hideSoftInputFromWindow(
        // mSearchEditText.getWindowToken(), 0);
        // }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_clear_text:
                if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                    mSearchEditText.setText("");
                    mInputMethodManager.hideSoftInputFromWindow(
                            mSearchEditText.getWindowToken(), 0);
                }
                break;
          /*  case R.id.title_back:
                finish();
                break;*/
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    NetBroadcastReceiver.mListeners.remove(this);
    }

/*    @Override
    public void onNetChange() {
//		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE)
//			T.showLong(this, R.string.net_err);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
