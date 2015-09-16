package com.sdk.info.extrainfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sdk.info.extrainfo.google_finance.FinanceActivity;
import com.sdk.info.extrainfo.open_weather_map.activity.WShowActivity;
import com.sdk.info.extrainfo.yahoo_finance.YahooFinanceActivity;
import com.sdk.info.extrainfo.yahoo_weather.YahooWeatherActivity;

;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button yahoo_btn;
    private Button google_btn;
    private Button yahoo_finance_btn;
    private Button google_finance_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yahoo_btn = (Button) findViewById(R.id.demo_yahoo_weather);
        google_btn = (Button) findViewById(R.id.demo_owm_weather);
        yahoo_finance_btn = (Button) findViewById(R.id.demo_yahoo_finance);
        google_finance_btn = (Button) findViewById(R.id.demo_google_finance);

        yahoo_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        yahoo_finance_btn.setOnClickListener(this);
        google_finance_btn.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demo_yahoo_weather:
                Intent intent1 = new Intent(this, YahooWeatherActivity.class);
                startActivity(intent1);
                break;
            case R.id.demo_owm_weather:
                Intent intent2 = new Intent(this, WShowActivity.class);
                startActivity(intent2);
                break;
            case R.id.demo_yahoo_finance:
                Intent intent3 = new Intent(this, YahooFinanceActivity.class);
                startActivity(intent3);
                break;
            case R.id.demo_google_finance:
                Intent intent4 = new Intent(this, FinanceActivity.class);
                startActivity(intent4);
                break;
        }

    }
}
