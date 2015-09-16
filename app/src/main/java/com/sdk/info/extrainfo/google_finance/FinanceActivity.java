package com.sdk.info.extrainfo.google_finance;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdk.info.extrainfo.R;

public class FinanceActivity extends ActionBarActivity {

    private TextView text_tv;
    private Button span_btn;
    private boolean isSpan = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        text_tv = (TextView) findViewById(R.id.my_test_tv);
        span_btn = (Button)findViewById(R.id.span_btn);
        context = this;
        span_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String str = "这是一段文字，可能会用特殊的效果," +
                       "这是一段文字，可能会用特殊的效果,这是一段文字，可能会用特殊的效果";
                if(isSpan){
                    text_tv.setText(str);
                    isSpan= false;
                }else {
                    ImageSpan imageSpan = new ImageSpan(context,R.drawable.gallery_icon);
                    SpannableString spanStr = new SpannableString("\u2020\u2020\u2020"+str);
                    spanStr.setSpan(imageSpan,0,2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    text_tv.setText(spanStr);
                    isSpan = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_finance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item1_menu) {

            return true;
        }
        if (id == R.id.item2_menu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
