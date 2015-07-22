package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/7/15.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-15
 * Time: 14:34
 */
public class DetailContentModel implements Serializable{

    private String type;
    private String data;

    public DetailContentModel(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
