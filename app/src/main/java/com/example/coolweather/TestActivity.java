package com.example.coolweather;

import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_test);

//        初始化各控件
//        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
//        titleCity = (TextView) findViewById(R.id.title_city);
//        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
//        degreeText = (TextView) findViewById(R.id.degree_text);
//        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
//        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
//        aqiText = (TextView) findViewById(R.id.aqi_text);
//        pm25Text = (TextView) findViewById(R.id.pm25_text);
//        comfortText = (TextView) findViewById(R.id.comfort_text);
//        carWashText = (TextView) findViewById(R.id.car_wash_text);
//        sportText = (TextView) findViewById(R.id.sport_text);
//
//
////        有缓存使用缓存
//        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this);
//        String weatherString = perfs.getString("weather", null);
//
//
//
//        if (weatherString != null) {
//            Log.d(TAG, "onCreate: weatherString != null");
//            Weather weather = Utility.handleWeatherResponse(weatherString);
//            showWeatherInfo(weather);
//
//        } else {
//            Log.d(TAG, "onCreate: weatherString == null");
//            //无缓存时，去访问服务器查询天气
//            String weatherId = getIntent().getStringExtra("weather_id");
//            Log.d(TAG, "onCreate: weatherId = "+weatherId);
//
//            weatherLayout.setVisibility(ScrollView.INVISIBLE);
//            requestWeather(weatherId);
//
//        }
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=b07cc009dec64a30bf7de9c66a67f7d8 ";
        Log.d(TAG, "requestWeather: weatherUrl="+weatherId);

        HttpUtil.sendOkHttpRequest(weatherId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "requestWeather run: onFailure");
//                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                Log.d(TAG, "requestWeather  responseText="+responseText);

                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "requestWeather run: onFailure");

                        if (weather != null && "ok".equals(weather.status)) {
//                            SharedPreferences.Editor editor = PreferenceManager.
//                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("weather", responseText);
//
//                            editor.commit();

                            showWeatherInfo(weather);
                        } else {
//                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }


    /**
     *  处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        Log.d(TAG, "showWeatherInfo: ");

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();

        for(Forecast forecast: weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);

            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);

            forecastLayout.addView(view);

        }



        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);

        }

        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动指数："+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }
}
