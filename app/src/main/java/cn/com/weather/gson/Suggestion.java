package cn.com.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:21 PM
 * 文件    Weather
 * 描述    建议
 */
public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;

    }

    public class CarWash {

        @SerializedName("txt")
        public String info;

    }

    public class Sport {

        @SerializedName("txt")
        public String info;

    }
}
