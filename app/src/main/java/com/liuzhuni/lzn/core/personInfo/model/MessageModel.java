package com.liuzhuni.lzn.core.personInfo.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/4/18.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-18
 * Time: 14:43
 */
public class MessageModel implements Serializable{
    private String year;
    private String month;
    private String message;
    private String date;
    private String url;
    private boolean isRead;


    public MessageModel(String mYear, String mMonthDay, String mMessage, boolean mIsReaded) {
        this.year = mYear;
        this.month = mMonthDay;
        this.message = mMessage;
        this.isRead = mIsReaded;
    }
    public MessageModel(String mYear, String mMonthDay, String mMessage) {
        this.year = mYear;
        this.month = mMonthDay;
        this.message = mMessage;
        this.isRead = true;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }
}
