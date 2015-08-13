package com.liuzhuni.lzn.core.select.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/7.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-07
 * Time: 16:56
 */
public class PriceModel implements Serializable {

    private String price;
    private String priceshow;
    private boolean isSelect=false;

    public PriceModel(String price, String priceshow, boolean isSelect) {
        this.price = price;
        this.priceshow = priceshow;
        this.isSelect = isSelect;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceshow() {
        return priceshow;
    }

    public void setPriceshow(String priceshow) {
        this.priceshow = priceshow;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
