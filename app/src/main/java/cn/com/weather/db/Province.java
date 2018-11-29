package cn.com.weather.db;

import org.litepal.crud.DataSupport;

/**
 * 作者    HuangShun
 * 时间    11/29/18 2:27 PM
 * 文件    Weather
 * 描述
 */
public class Province  extends DataSupport {
    private int id;
    private String provinceName;    //省的名字
    private int provinceCode;       //记录省的代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}




