package com.liuzhuni.lzn.core.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/5/12.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-12
 * Time: 15:39
 */
public class BaseModel<T> implements Serializable{
    private int ret;
    private String mes;
    private T data;
    private int l;



    public BaseModel(int ret, String mes, T data) {
        this.ret = ret;
        this.mes = mes;
        this.data = data;
    }


    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
