package com.example.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.myweather.R;
import com.example.myweather.service.AutoUpdateService;
import com.example.myweather.util.HttpCallbackListener;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.LoadBackgroundUtil;
import com.example.myweather.util.Utility;

import net.qiujuer.genius.ui.compat.UiCompat;

/**
 * 显示天气情况的Activity
 */

public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;

    //切换城市按钮
    private ImageView switchCity;
    // 更新天气按钮
    private ImageView refreshWeather;
    // 用于退出程序
    private ImageView exitIm;
    //用于显示城市名
    private TextView cityNameText;
    // 用于显示当前日期
    private TextView currentDateText;
    // 用于显示天气描述信息
    private TextView weatherDespText;
    // 用于显示温度
    private TextView tempText;
    //用于显示穿衣建议
    private TextView dressAdvText;
    // 背景layout
    private RelativeLayout bgLayout;
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        switchCity = (ImageView) findViewById(R.id.switch_city);
        refreshWeather = (ImageView) findViewById(R.id.refresh_weather);
        exitIm = (ImageView) findViewById(R.id.im_exit);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        currentDateText = (TextView) findViewById(R.id.current_date);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        tempText = (TextView) findViewById(R.id.temp);
        dressAdvText = (TextView) findViewById(R.id.dressing_advice);
        bgLayout = (RelativeLayout) findViewById(R.id.lay_container);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        exitIm.setOnClickListener(this);
        String countyName = getIntent().getStringExtra("county_name");
        String province = getIntent().getStringExtra("province");
        if (TextUtils.isEmpty(province)) {
            province = prefs.getString("province", "");
        }
        int id = LoadBackgroundUtil.findDrawableId(province);
        Glide.with(this)
                .load(id)
                .centerCrop()
                .into(new ViewTarget<RelativeLayout, GlideDrawable>(bgLayout) {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();
                        //使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.white_alpha_144),
                                PorterDuff.Mode.SCREEN); //设置着色的效果和颜色，蒙版模式
                        // 设置给ImageView
                        this.view.setBackground(drawable);

                    }
                });

        if(!TextUtils.isEmpty(countyName)) {
            queryWeatherInfo(countyName);
        } else {
            showWeather();
        }
    }

    /**
     * 查询县名称所对应的天气
     */
    private void queryWeatherInfo(String countyName) {
        String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + countyName + "&key=b86e2ed0aefe63d27dce6141b9320be2";
        queryFromServer(address);
    }
    /**
     * 根据传入的县级名称去向服务器查询天气信息
     */
    private void queryFromServer(final String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //处理服务器返回的信息信息
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather() {
        cityNameText.setText(prefs.getString("city_name", ""));
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        tempText.setText(prefs.getString("temp", ""));
        dressAdvText.setText(prefs.getString("dressing_advice", ""));
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String countName = prefs.getString("county_name", "");
                if(!TextUtils.isEmpty(countName)) {
                    queryWeatherInfo(countName);
                    Toast.makeText(WeatherActivity.this, "刷新成功！", Toast.LENGTH_SHORT).show();
                } else {
                    return;
                }
                break;
            case R.id.im_exit:
                finish();
                break;
            default:
                break;
        }
    }
}
