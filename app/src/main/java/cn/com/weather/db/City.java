package cn.com.weather.db;

import org.litepal.crud.DataSupport;

/**
 * 作者    HuangShun
 * 时间    11/29/18 2:29 PM
 * 文件    Weather
 * 描述
 */
public class City extends DataSupport {
    private int id;
    private String cityName;    //记录市的名字
    private int cityCode;       //记录市的代号
    private int provinceId;   //记录当前市所属于省的id值


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
