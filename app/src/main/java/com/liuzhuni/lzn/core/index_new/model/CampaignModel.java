package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/27.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-27
 * Time: 14:47
 */
public class CampaignModel implements Serializable{

    private int orderNum;
    private String imgUrl;
    private String zhuanTiUrl;


    public CampaignModel(int orderNum, String imgUrl, String zhuanTiUrl) {
        this.orderNum = orderNum;
        this.imgUrl = imgUrl;
        this.zhuanTiUrl = zhuanTiUrl;
    }

    public int getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getZhuanTiUrl() {
        return this.zhuanTiUrl;
    }

    public void setZhuanTiUrl(String zhuanTiUrl) {
        this.zhuanTiUrl = zhuanTiUrl;
    }
}
