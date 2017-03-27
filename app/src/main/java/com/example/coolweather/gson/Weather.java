package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qiaojiange on 2017/3/24.
 */

public class Weather {

    public String status;

    public Basic basic;

    @SerializedName("aqi")
    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @Override
    public String toString() {
        return "Weather{" +
                "status='" + status + '\'' +
                ", basic=" + basic +
                ", aqi=" + aqi +
                ", now=" + now +
                ", suggestion=" + suggestion +
                ", forecastList=" + forecastList +
                '}';
    }
}
