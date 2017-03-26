package com.example.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by qiaojiange on 2017/3/23.
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "--ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;


    private ProgressDialog progressDialog;

    private TextView titleView;
    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;


//    当前选中的省
    private Province selectProvince;
//    当前选中的市
    private City selectCity;
//    当前选中的县
    private County selectCounty;

//当前选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleView = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if(currentLevel == LEVEL_PROVINCE){
                  selectProvince = provinceList.get(position);
                  queryCities();
              }else if(currentLevel == LEVEL_CITY){
                  selectCity = cityList.get(position);
                  queryCounties();
              }else if(currentLevel == LEVEL_COUNTY){
//                  selectCounty = countyList.get(position);
                  String weatherId = countyList.get(position).getWeatherId();
                  Log.d(TAG, "onItemClick: weatherId = "+weatherId);

//                  Intent intent = new Intent(getActivity(),TestActivity.class);
//                  startActivity(intent);
//                  getActivity().finish();

                  Intent intent = new Intent(getActivity(),WeatherActivity.class);
                  intent.putExtra("weather_id",weatherId);
                  startActivity(intent);
                  getActivity().finish();

              }
          }

      });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });


        queryProvinces();
    }

    private void queryCounties(){
        titleView.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        countyList = DataSupport.where("cityId=?",String.valueOf(selectCity.getId())).find(County.class);
        if (countyList.size()> 0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            this.currentLevel = LEVEL_COUNTY;

        }else{
            int provinceCode = selectProvince.getProvinceCode();
            int  cityCode = selectCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,County.COUNTY);

        }
    }

    private void queryCities(){
        titleView.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        Log.d(TAG, "queryCities: provinceId="+selectProvince.getId());

        cityList =DataSupport.where("provinceId=?",String.valueOf(selectProvince.getId())).find(City.class);

        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            this.currentLevel = LEVEL_CITY;
        }else{

//            Log.d(TAG, "queryCities: provinceCode="+selectProvince.getProvinceCode());

            int provinceCode = selectProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;

//            Log.d(TAG, "queryCities: address="+address);
            queryFromServer(address,City.CITY);
        }

    }

    private void queryProvinces() {
        titleView.setText("中国");
//        隐藏
        backButton.setVisibility(View.GONE);

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size()>0){//从数据库加载
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
//            更新数据
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;

        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,Province.PROVINCE);

        }

    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县的数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }



            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if (Province.PROVINCE.equals(type)){
                    result = Utility.handleProvinceResponse(responseText);

                }else if(City.CITY.equals(type)){
                    result = Utility.handleCityResponse(responseText,selectProvince.getId());

                }else if(County.COUNTY.equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Province.PROVINCE.equals(type)){
                                queryProvinces();
                            }else if(City.CITY.equals(type)){
                                queryCities();
                            }else if (County.COUNTY.equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
                closeProgressDialog();
            }
        });

    }

    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
