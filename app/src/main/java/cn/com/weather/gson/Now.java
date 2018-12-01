package cn.com.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:18 PM
 * 文件    Weather
 * 描述
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;      //现在的温度

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;         //现在的天气 如降雨
    }
}
