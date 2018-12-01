package cn.com.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:12 PM
 * 文件    Weather
 * 描述    基本信息 城市 城市ID 以及日期
 */
public class Basic {
    @SerializedName("city")
    public String ciityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;


    }




}
