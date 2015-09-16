package com.sdk.info.extrainfo.open_weather_map.plistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sdk.info.extrainfo.R;

/**
 * Created by Administrator on 2015/8/7.
 */
public class MyBladeView extends View {
    private OnItemClickListener mOnItemClickListener;
    String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z" };
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;
    private int mCharHeight = 15;

    public MyBladeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCharHeight = context.getResources().getDimensionPixelSize(R.dimen.blade_view_text_size);
    }

    public MyBladeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCharHeight = context.getResources().getDimensionPixelSize(R.dimen.blade_view_text_size);
    }

    public MyBladeView(Context context) {
        super(context);
        mCharHeight = context.getResources().getDimensionPixelSize(R.dimen.blade_view_text_size);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(getResources().getColor(R.color.nav_bar_bg_active));
        } else {
            canvas.drawColor(getResources().getColor(R.color.nav_bar_bg_normal));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.nav_text_normal));
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(mCharHeight);
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.nav_text_press));
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        dismissPopup();
        return super.onSaveInstanceState();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        // 字母总长度
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c > 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c > 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 点击侧栏字母弹窗显示
     * @param item 点击的是第（item+1）个字母 = section位置
     */
    private void showPopup(int item) {
        if (mPopupWindow == null) {
            mPopupText = new TextView(getContext());
            mPopupText
                    .setBackgroundResource(R.drawable.ic_contacts_index_backgroud_sprd);
            mPopupText.setTextColor(getResources().getColor(R.color.popup_text_select));
            mPopupText.setTextSize(getResources().getDimensionPixelSize(R.dimen.blade_text_size));
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
                    | Gravity.CENTER_VERTICAL);
//            int size = (int)getResources().getDimension(R.dimen.bladeview_popup_height);
            mPopupWindow = new PopupWindow(mPopupText,
                    LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        String text = "";
        if (item == 0) {
            text = "#";
        } else {
            text = Character.toString((char) ('A' + item - 1));
        }
        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    private void dismissPopup() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(b[item]);
            showPopup(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

}