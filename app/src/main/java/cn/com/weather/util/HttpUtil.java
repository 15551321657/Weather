package cn.com.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 作者    HuangShun
 * 时间    11/29/18 3:29 PM
 * 文件    Weather
 * 描述    进行网络访问的工具类
 */
public class HttpUtil {

    /**
     * @param address 传进来的url地址
     * @param callback 回调函数
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
