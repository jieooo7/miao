package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/7.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-07
 * Time: 11:44
 */
public class CheapModel implements Serializable{

    private String title;
    private String price;
    private String oldprice;
    private String img;
    private String url;

    private int id;
    private int types;

    private int mallid;
    private String tbid;



    public CheapModel(String title, String price, String oldprice, String img, String url, int id, int types) {
        this.title = title;
        this.price = price;
        this.oldprice = oldprice;
        this.img = img;
        this.url = url;
        this.id = id;
        this.types = types;
    }


    public int getMallid() {
        return mallid;
    }

    public void setMallid(int mallid) {
        this.mallid = mallid;
    }

    public String getTbid() {
        return tbid;
    }

    public void setTbid(String tbid) {
        this.tbid = tbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }
}
