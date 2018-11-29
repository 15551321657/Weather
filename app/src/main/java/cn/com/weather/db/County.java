package cn.com.weather.db;

import org.litepal.crud.DataSupport;

/**
 * 作者    HuangShun
 * 时间    11/29/18 3:15 PM
 * 文件    Weather
 * 描述
 */
public class County extends DataSupport {
    private int id;
    private String countyName;      //记录县的名字
    private String wetherId;        //记录县对应的天气
    private int cityId;             //记录当前所属市的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWetherId() {
        return wetherId;
    }

    public void setWetherId(String wetherId) {
        this.wetherId = wetherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
