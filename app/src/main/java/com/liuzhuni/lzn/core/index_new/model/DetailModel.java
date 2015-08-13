package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/15.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-15
 * Time: 14:27
 */
public class DetailModel implements Serializable{

    private int id;
    private String pic;
    private String title;
    private String title1;
    private String mall;
    private String time;
    private int read;
    private int review;
    private boolean expired=false;
    private boolean shenjia=false;
    private boolean baicai=false;
    private boolean quanqiugou=false;
    private boolean shoumanwu=false;
    private boolean IsCollect=false;

    private String url;
    private String shareurl;
    private String name;

    private List<DetailContentModel> content;


    public DetailModel(int id, String pic, String title, String title1, String mall, String url, String name) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.title1 = title1;
        this.mall = mall;
        this.url = url;
        this.name = name;
    }


    public boolean isCollect() {
        return IsCollect;
    }

    public void setCollect(boolean isCollect) {
        IsCollect = isCollect;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isShenjia() {
        return shenjia;
    }

    public void setShenjia(boolean shenjia) {
        this.shenjia = shenjia;
    }

    public boolean isBaicai() {
        return baicai;
    }

    public void setBaicai(boolean baicai) {
        this.baicai = baicai;
    }

    public boolean isQuanqiugou() {
        return quanqiugou;
    }

    public void setQuanqiugou(boolean quanqiugou) {
        this.quanqiugou = quanqiugou;
    }

    public boolean isShoumanwu() {
        return shoumanwu;
    }

    public void setShoumanwu(boolean shoumanwu) {
        this.shoumanwu = shoumanwu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DetailContentModel> getContent() {
        return content;
    }

    public void setContent(List<DetailContentModel> content) {
        this.content = content;
    }
}
