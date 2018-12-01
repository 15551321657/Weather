package cn.com.weather.gson;

/**
 * 作者    HuangShun
 * 时间    12/1/18 1:15 PM
 * 文件    Weather
 * 描述    空气质量以及pm2.5
 */
public class AQI {
    public AQICitty city;
    public class  AQICitty{
        public String aqi;
        public String pm25;
    }
}
