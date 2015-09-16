package com.sdk.info.extrainfo.open_weather_map.plistview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * Created by Administrator on 2015/8/7.
 */
public class MyPinnedHeaderListView extends ListView {

    public interface MyPinnedHeaderAdapter {
        /**
         * Pinned header state: don't show the header.
         */
        public static final int PINNED_HEADER_GONE = 0;
        /**
         * Pinned header state: show the header at the top of the list.
         */
        public static final int PINNED_HEADER_VISIBLE = 1;
        /**
         * Pinned header state: show the header. If the header extends beyond
         * the bottom of the first shown element, push it up and clip.
         */
        public static final int PINNED_HEADER_PUSHED_UP = 2;
        /**
         * Computes the desired state of the pinned header for the given
         * position of the first visible list item. Allowed return values are
         * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
         * {@link #PINNED_HEADER_PUSHED_UP}.
         */
        int getMyPinnedHeaderState(int position);
        /**
         * Configures the pinned header view to match the first visible list item.
         *
         * @param header pinned header view.
         * @param position position of the first visible list item.
         * @param alpha fading of the header view, between 0 and 255.
         */
        void configurePinnedHeader(View header, int position, int alpha);
    }

    private static final int MAX_ALPHA = 255;

    private MyPinnedHeaderAdapter mAdapter;
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public MyPinnedHeaderListView(Context context) {
        super(context);
    }

    public MyPinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPinnedHeaderListView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (MyPinnedHeaderAdapter) adapter;
    }

    public void configureHeaderView(int position) {
        if (mHeaderView == null) {
            return;
        }
        int state = mAdapter.getMyPinnedHeaderState(position);
        switch (state) {
            case MyPinnedHeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case MyPinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case MyPinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                int alpha;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
                            + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}