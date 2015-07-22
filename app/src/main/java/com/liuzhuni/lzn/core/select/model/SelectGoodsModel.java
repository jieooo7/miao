package com.liuzhuni.lzn.core.select.model;

import java.io.Serializable;

/**
 *
 * Created by Andrew Lee on 2015/5/20.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-20
 * Time: 10:16
 */
public class SelectGoodsModel implements Serializable{

    private String img;
    private String title;
    private String mall;
    private String url;
    private String price;
    private String code;


    public SelectGoodsModel(String img, String title, String mall, String url, String price,String code) {
        this.img = img;
        this.title = title;
        this.mall = mall;
        this.url = url;
        this.price = price;
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
