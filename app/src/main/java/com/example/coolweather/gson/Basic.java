package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qiaojiange on 2017/3/24.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;


    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;

        @Override
        public String toString() {
            return "Update{" +
                    "updateTime='" + updateTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Basic{" +
                "cityName='" + cityName + '\'' +
                ", weatherId='" + weatherId + '\'' +
                ", update=" + update +
                '}';
    }
}
