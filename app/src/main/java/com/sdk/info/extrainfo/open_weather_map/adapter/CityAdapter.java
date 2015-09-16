package com.sdk.info.extrainfo.open_weather_map.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sdk.info.extrainfo.R;
import com.sdk.info.extrainfo.open_weather_map.bean.City;
import com.sdk.info.extrainfo.open_weather_map.plistview.MyPinnedHeaderListView;
import com.sdk.info.extrainfo.open_weather_map.utils.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/3.
 */
public class CityAdapter extends BaseAdapter implements SectionIndexer,
        MyPinnedHeaderListView.MyPinnedHeaderAdapter, AbsListView.OnScrollListener {

    // 首字母集
    private List<City> mCities;
    private Map<String, List<City>> mMap;
    private List<String> mSections;
    private List<Integer> mPositions;
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> cities,
                       Map<String, List<City>> map, List<String> sections,
                       List<Integer> positions) {
        // TODO Auto-generated constructor stub
        inflater = LayoutInflater.from(context);
        mCities = cities;
        mMap = map;
        mSections = sections;
        mPositions = positions;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCities.size();
    }

    @Override
    public City getItem(int position) {
        // TODO Auto-generated method stub
        int section = getSectionForPosition(position);
        return mMap.get(mSections.get(section)).get(
                position - getPositionForSection(section));
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int section = getSectionForPosition(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.select_city_item, null);
        }
        TextView group = (TextView) convertView.findViewById(R.id.group_title);
        TextView city = (TextView) convertView.findViewById(R.id.column_title);
        if (getPositionForSection(section) == position) {
            group.setVisibility(View.VISIBLE);
            group.setText(mSections.get(section));
        } else {
            group.setVisibility(View.GONE);
        }
        City item = mMap.get(mSections.get(section)).get(
                position - getPositionForSection(section));
        city.setText(item.getName());
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        Log.d("CityAdapter:","SrcollState Change.");

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        if (view instanceof MyPinnedHeaderListView) {
            ((MyPinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
            Log.d("CityAdapter:", "PinnedHeader TOP.");

        }

    }

    @Override
    public int getMyPinnedHeaderState(int position) {
        int realPosition = position;
        if (realPosition < 0 || position >= getCount()) {
            return PINNED_HEADER_GONE;
        }
        int section = getSectionForPosition(realPosition);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1
                && realPosition == nextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }
        return PINNED_HEADER_VISIBLE;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        // TODO Auto-generated method stub
        int realPosition = position;
        int section = getSectionForPosition(realPosition);
        String title = (String) getSections()[section];
        ((TextView) header.findViewById(R.id.group_title)).setText(title);
    }

    @Override
    public Object[] getSections() {
        // TODO Auto-generated method stub
        Log.d(Tag.TAG,"getSections"+mSections.toArray());
        return mSections.toArray();
    }

    /**
     * 返回每个section的第一个元素位置
     * @param section
     * @return
     */
    @Override
    public int getPositionForSection(int section) {
        // TODO Auto-generated method stub
        if (section < 0 || section >= mPositions.size()) {
            return -1;
        }
        Log.d(Tag.TAG,"position-section:"+mPositions.get(section));
        return mPositions.get(section);
    }

    /**
     * 返回该处postion的section的位置
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= getCount()) {
            return -1;
        }
        //注意这个方法的返回值，它就是index<0时，返回-index-2的原因
        //解释Arrays.binarySearch，如果搜索结果在数组中，则返回它在数组中的索引，
        // 如果不在，刚返回第一个比它大的索引的负数-1
        int index = Arrays.binarySearch(mPositions.toArray(), position);
        return index >= 0 ? index : -index - 2;
    }
}
