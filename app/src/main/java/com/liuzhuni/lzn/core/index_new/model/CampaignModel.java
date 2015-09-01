package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/27.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-27
 * Time: 14:47
 */
public class CampaignModel implements Serializable{

    private int OrderNum;
    private String ImgUrl;
    private String ZhuanTiUrl;


    public CampaignModel(int orderNum, String imgUrl, String zhuanTiUrl) {
        OrderNum = orderNum;
        ImgUrl = imgUrl;
        ZhuanTiUrl = zhuanTiUrl;
    }

    public int getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(int orderNum) {
        OrderNum = orderNum;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getZhuanTiUrl() {
        return ZhuanTiUrl;
    }

    public void setZhuanTiUrl(String zhuanTiUrl) {
        ZhuanTiUrl = zhuanTiUrl;
    }
}
