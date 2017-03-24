package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by qiaojiange on 2017/3/23.
 */

public class Province extends DataSupport {
    public static final String PROVINCE="Province";

    private int id;
    private String provinceName;
    //从服务器上请求到的
    private int provinceCode;

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
