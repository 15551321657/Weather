package cn.com.weather;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cn.com.weather.db.City;
import cn.com.weather.db.County;
import cn.com.weather.db.Province;
import cn.com.weather.util.HttpUtil;
import cn.com.weather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * 遍历省市区数据的碎片
 */
public class ChooseFragment extends Fragment {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private ArrayAdapter<String> adapter;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ProgressDialog progressDialog;
    private List<String> dataList = new ArrayList<>();    //缓存数据的集合
    private List<Province> provinceList;        //省列表
    private List<City> cityList;                //市列表
    private List<County> countyList;            //县区列表
    private int currentLevel;                   //当前选中的级别
    private Province selectedProvince;          //选中的省份
    private City selectedCity;                  //选中的城市
    private String TAG = "huangshun";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);//碎片的标题  如:中国
        backButton = (Button) view.findViewById(R.id.back_button);//返回按钮
        listView = (ListView) view.findViewById(R.id.listView);     //listView
        //初始化arrayAdapter
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, dataList);
        listView.setAdapter(adapter);       //将Adapter设置给listView
        return view;


    }

    /**
     * 当activity创建的时调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置listView的条目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {     //如果当前选中的级别为省的级别
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }


            }
        });

        //设置返回按钮的条目点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }

            }
        });
        queryProvinces();
    }


    /**
     * 查询全国的所以省 优先查询数据库 如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        Log.d(TAG, "queryProvinces: 查询省的方法走了 ");
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);//设置控件不可见
        provinceList = DataSupport.findAll(Province.class);//从数据库查询所有的省的数据
        if (provinceList.size() > 0) {//说明数据库有数据
            dataList.clear();//清空用于缓存省市区的集合缓存
            for (Province province : provinceList) {  //遍历所有省 拿出省的名字
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged(); //通知adapter刷新数据
            listView.setSelection(0);       //将光标移动到listView的第一条数据
            currentLevel = LEVEL_PROVINCE;    //当前选中的级别为省

        } else {
            //数据库没有查询到数据库  从服务器获取
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");


        }
    }

    /**
     * 查询所有的城市 优先从数据库查询  没有再从服务器查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());      //获得选中省份的名字
        backButton.setVisibility(View.VISIBLE);
        //查询选中的省下的城市
        cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();//通知listView刷新数据
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            //数据库为空  从服务器获取数据
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }

    }


    /**
     * 查询所有的县 先查询数据库 为空的话1再去服务器上查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            //从服务器获取数据
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }

    }


    /**
     * 根据传入的地址和类型从服务器你上查询数据
     *
     * @param addess url
     * @param type   类型
     */
    private void queryFromServer(String addess, final String type) {
        showProgressDialog();   //显示进度条
        Log.d(TAG, "queryFromServer: 进度条显示了==");
        //请求服务器
        HttpUtil.sendOkHttpRequest(addess, new Callback() {
            //链接失败时调用
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_LONG).show();
                    }
                });

            }

            //链接成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: 请求服务器成功了 =========");

                String responseText = response.body().string();
                Log.d(TAG, "onResponse: " + responseText);
                boolean result = false;
                if ("province".equals(type)) {
                    //处理服务器返回的省的数据json数据 存储到数据库
                    result = Utility.handleProvinceResponse(responseText);
                    Log.d(TAG, "onResponse: result =========" + result);

                } else if ("city".equals(type)) {

                    result = Utility.handleCityresponse(responseText, selectedProvince.getId());

                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    //如果从服务器获取数据成功存储到数据库
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //在子线程更新Ui
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });


    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            //若为空  重新创建
            //fragment里面可以通过getActivity获得context对象
            progressDialog = new ProgressDialog(getActivity());
            Log.d(TAG, "showProgressDialog: 进度条显示了");
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);//dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();   //关闭对话框
        }


    }


}
