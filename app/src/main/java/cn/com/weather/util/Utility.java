package cn.com.weather.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.weather.db.City;
import cn.com.weather.db.County;
import cn.com.weather.db.Province;
import cn.com.weather.gson.Weather;

/**
 * 作者    HuangShun
 * 时间    11/29/18 3:35 PM
 * 文件    Weather
 * 描述    解析和处理服务器返回的json数据 保存到数据库
 */
public class Utility {
    /**
     * @param response 服务器返回的省级数据
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();//litePal保存数据到数据库
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * @param response 处理服务器返回的城市的数据
     * @param provinceId 所属于的省的id
     * @return
     */
    public static boolean handleCityresponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCitys=new JSONArray(response);
                for(int i=0;i<allCitys.length();i++){
                    JSONObject cityobject = allCitys.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityobject.getString("name"));
                    city.setCityCode(cityobject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @param response 处理服务器返回的county数据
     * @param cityId 所属于的城市的id
     * @return
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWetherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * @param response 将返回的天气json数据  解析成Weather实体类
     * @return
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }




}
