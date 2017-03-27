package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qiaojiange on 2017/3/24.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("sport")
    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;

        @Override
        public String toString() {
            return "Comfort{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }


    public class CarWash{
        @SerializedName("txt")
        public String info;

        @Override
        public String toString() {
            return "CarWash{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }

    public class Sport{

        @SerializedName("txt")
        public String info;

        @Override
        public String toString() {
            return "Sport{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "comfort=" + comfort +
                ", carWash=" + carWash +
                ", sport=" + sport +
                '}';
    }
}
