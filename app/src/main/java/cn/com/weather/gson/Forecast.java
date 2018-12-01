package cn.com.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:24 PM
 * 文件    Weather
 * 描述    天气预测
 */
public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;     //温度

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;      //最高温度

        public String min;      //最低温度

    }

    public class More {

        @SerializedName("txt_d")
        public String info;     //天气  如 多云

    }
}

