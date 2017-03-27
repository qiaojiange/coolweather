package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qiaojiange on 2017/3/24.
 */

public class AQI {

    @SerializedName("city")
    public AQICity city;

    public class  AQICity{
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;

        @Override
        public String toString() {
            return "AQICity{" +
                    "aqi='" + aqi + '\'' +
                    ", pm25='" + pm25 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AQI{" +
                "city=" + city +
                '}';
    }
}
