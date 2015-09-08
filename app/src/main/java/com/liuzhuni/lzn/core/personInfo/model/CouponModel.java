package com.liuzhuni.lzn.core.personInfo.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/9/6.
 * E-mail:jieooo7@163.com
 * Date: 2015-09-06
 * Time: 17:55
 */
public class CouponModel implements Serializable{

    private String title;
    private String code;
    private String pwd;
    private String url;
    private String enddate;
    private boolean expired;


    public CouponModel(String title, String code, String pwd, String url, String enddate, boolean expired) {
        this.title = title;
        this.code = code;
        this.pwd = pwd;
        this.url = url;
        this.enddate = enddate;
        this.expired = expired;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
