package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/7.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-07
 * Time: 09:46
 */
public class ProfileModel implements Serializable{

    private boolean platform;
    private String city;
    private String sign;
    private String pic;
    private String name;
    private String phone;
    private int jifen;
    private int lzb;
    private int grade;


    public ProfileModel(String sign, int jifen, int lzb, int grade) {
        this.sign = sign;
        this.jifen = jifen;
        this.lzb = lzb;
        this.grade = grade;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public int getLzb() {
        return lzb;
    }

    public void setLzb(int lzb) {
        this.lzb = lzb;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
