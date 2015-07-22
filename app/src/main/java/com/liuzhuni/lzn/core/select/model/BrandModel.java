package com.liuzhuni.lzn.core.select.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/4/28.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-28
 * Time: 09:33
 */
public class BrandModel implements Serializable{

    private String brand;
    private int id;
    private boolean isSelect=false;

    public BrandModel(String brand, boolean isSelect) {
        this.brand = brand;
        this.isSelect = isSelect;
    }

    public BrandModel(String brand) {
        this.brand = brand;
        this.isSelect = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
