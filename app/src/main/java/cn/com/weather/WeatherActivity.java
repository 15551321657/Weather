package cn.com.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.com.weather.gson.Forecast;
import cn.com.weather.gson.Weather;
import cn.com.weather.util.HttpUtil;
import cn.com.weather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    public  TextView comfortText;

    public TextView carWashText;

    public TextView sportText;

    private ImageView bingPicImg;

    private String mWeatherId;
    private String TAG="huangshun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各种控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);     //找到天气的布局
        titleCity = (TextView) findViewById(R.id.title_city);               //标题
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);  //天气的更新时间
        degreeText = (TextView) findViewById(R.id.degree_text);               //当前气温
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);    //当前天气状况
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);   //未来天气布局
        aqiText = (TextView) findViewById(R.id.aqi_text);                   //aqi
        pm25Text = (TextView) findViewById(R.id.pm25_text);                 //屏幕2.5
        comfortText = (TextView) findViewById(R.id.comfort_text);           //舒适度
        sportText = (TextView) findViewById(R.id.sport_text);               //运动指数
        carWashText=(TextView)findViewById(R.id.car_wash_text);         //洗车指数
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //没有缓存去服务器查询数据
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d(TAG, "onCreate: 没有缓存去服务器查询数据 weatherId为::"+weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
     * 根据天气id请求城市天气信息   wetherId:CN101010200
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=6bf51213d3704350bb9e75d45a7f64fb";
        //http://guolin.tech/api/weather?cityid=2CN101010200&key=bc0418b57b2d4918819d3974ac1285d9
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d(TAG, "onResponse: 传回来的天气数据信息为:"+responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d(TAG, "onResponse: 处理后的天气信息为weather="+weather);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            Log.d(TAG, "run: 调用showWeatherInfo方法之前走了");
                            showWeatherInfo(weather);
                            Log.d(TAG, "run: weather 天气的信息有:"+weather);
                        } else {
                            Log.d(TAG, "run: 获取天气信息失败requestWeather====");
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run   onFailure: 获取天气失败");
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    /**
     * 处理并展示Weather实体类中的数据。
     */
    public void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        Log.d(TAG, "showWeatherInfo: cityName"+cityName+"updateTime"+updateTime+"degree"+degree+"weatherInfo"+weatherInfo);     //ok
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        Log.d(TAG, "showWeatherInfo: weatherInfoText   ok");
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        Log.d(TAG, "showWeatherInfo: 循环结束OK===");
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        Log.d(TAG, "showWeatherInfo: comfort"+comfort+"carWash"+carWash+"sport"+sport);
        Log.d(TAG, "showWeatherInfo: comfortText"+comfortText+"carWashText"+carWashText+"sportText"+sportText);
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
