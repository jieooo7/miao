package com.liuzhuni.lzn.core.model;



import java.io.Serializable;
import java.util.List;

/**
 * Created by Andrew Lee on 2015/5/12.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-12
 * Time: 15:51
 */
public class BaseListModel<T> implements Serializable{
    private int ret;
    private int pageIndex;
    private int totalpage;

    private int forward;
    private int back;

    private int l;
    private int id;
    private String mes;
    private List<T> data;

    private int review;
    private String url;


    public BaseListModel(int ret, int pageIndex, int totalpage, int forward, int back, int l, int id, String mes, List<T> data) {
        this.ret = ret;
        this.pageIndex = pageIndex;
        this.totalpage = totalpage;
        this.forward = forward;
        this.back = back;
        this.l = l;
        this.id = id;
        this.mes = mes;
        this.data = data;
    }

    public BaseListModel(int ret, int pageIndex, int totalpage, int l, String mes,int id, List<T> data) {
        this.ret = ret;
        this.pageIndex = pageIndex;
        this.totalpage = totalpage;
        this.l = l;
        this.id=id;
        this.mes = mes;
        this.data = data;
    }


    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getForward() {
        return forward;
    }
    public void setForward(int forward) {
        this.forward = forward;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
