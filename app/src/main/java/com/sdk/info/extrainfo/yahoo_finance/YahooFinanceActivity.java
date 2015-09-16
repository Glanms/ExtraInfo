package com.sdk.info.extrainfo.yahoo_finance;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.sdk.info.extrainfo.R;
import com.sdk.info.extrainfo.yahoo_finance.activity.BaseActivity;
import com.sdk.info.extrainfo.yahoo_finance.bean.FinanceInfo;
import com.sdk.info.extrainfo.yahoo_finance.utils.VolleyHttpUtil;
import com.sdk.info.extrainfo.yahoo_finance.utils.YFJsonUtil;

import java.util.ArrayList;

public class YahooFinanceActivity extends BaseActivity implements View.OnClickListener{

    private TextView rate_tv1;  //欧元兑里拉价格
    private TextView rate_tv2; //美元兑里拉价格
    private TextView altin_tv; //黄金价格
    private TextView bist_tv; //土耳其指数

    private Button rateBtn ;
    private Button altinBtn ;
    private Button bistBtn ;
    private   VolleyHttpUtil volleyHttpUtil;
    private  YFJsonUtil jsonUtils;
    private Context context;
    private ArrayList<FinanceInfo> infoList = new ArrayList<>();

    private Handler myHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yahoo_finance);
        context = this;

      volleyHttpUtil = new VolleyHttpUtil(context,YFJsonUtil.sourceUrl);

        jsonUtils = new YFJsonUtil(infoList);
        initView();
//        getBist();
    }

    private void initView(){
        rate_tv1 = (TextView)findViewById(R.id.widget_tv1);
        rate_tv2 = (TextView)findViewById(R.id.widget_tv2);
        altin_tv = (TextView)findViewById(R.id.widget_tv3);
        bist_tv = (TextView)findViewById(R.id.widget_tv4);
        rateBtn = (Button)findViewById(R.id.rate_btn);
        altinBtn = (Button)findViewById(R.id.altin_btn);
        bistBtn = (Button)findViewById(R.id.bist_btn);

        rateBtn.setOnClickListener(this);
        altinBtn.setOnClickListener(this);
        bistBtn.setOnClickListener(this);
    }

    private void getBist(){
        executeRequest(new StringRequest(Request.Method.GET, YFJsonUtil.sourceUrl, responseListener,
                errorListener()));
    }

    private void getRate(){
        String url1 = YFJsonUtil.BaseUrl1 + YFJsonUtil.paramsEuro+ YFJsonUtil.BaseUrl2;
        String url2 = YFJsonUtil.BaseUrl1 + YFJsonUtil.paramsDolar+ YFJsonUtil.BaseUrl2;
        executeRequest(new StringRequest(Request.Method.GET,url1,euRateListener,errorListener()));
        executeRequest(new StringRequest(Request.Method.GET,url2,usRateListener,errorListener()));
    }

    /**
     * 得到欧元兑里拉汇率
     */
    private Response.Listener euRateListener = new Response.Listener() {
        @Override
        public void onResponse(Object o) {
            try {
                jsonUtils.getRate(YFJsonUtil.paramsEuro,o.toString());
                String euRate = infoList.get(0).getEuRate();
                rate_tv2.setText(euRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 得到美元兑里拉汇率
     */
    private Response.Listener usRateListener = new Response.Listener() {
        @Override
        public void onResponse(Object o) {
            try {
                jsonUtils.getRate(YFJsonUtil.paramsDolar,o.toString());
                String usRate = infoList.get(0).getUsRate();
                rate_tv1.setText(usRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //土耳其指数的responseListener
    private Response.Listener responseListener = new Response.Listener() {
        @Override
        public void onResponse(Object o) {
            try {
                jsonUtils.getTurIndex(o.toString());
                float bist = infoList.get(0).getBist();
                bist_tv.setText(""+bist);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void doIndex(String response){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rate_btn:
                getRate();
                break;
            case R.id.altin_btn:
                break;
            case R.id.bist_btn:
                getBist();
                break;
            default:
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yahoo_finance, menu);
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
