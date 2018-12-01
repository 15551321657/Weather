package cn.com.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:26 PM
 * 文件    Weather
 * 描述
 */
public class Weather {
    public String status;           //返回状态 成功则ok   失败则返回失败原因
    public Basic basic;             //基本信息
    public AQI aqi;                 //空气质量
    public Now now;                 //现在的天气
    public Suggestion suggestion;   //建议
    @SerializedName("daily_forecast")//天气预测的集合类
    public List<Forecast> forecastList;
}
