package com.sdk.info.extrainfo.yahoo_finance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sdk.info.extrainfo.yahoo_finance.utils.RequestManager;

/**
 * Created by Administrator on 2015/8/10.
 */
public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
