package com.liuzhuni.lzn.db;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/5/30.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-30
 * Time: 17:42
 */
public class DbModel implements Serializable{

    private int message_id;
    private int body_id;
    private String body;
    private long date;
    private int type;

    public DbModel(int type, int body_id, long date, String body) {
        this.type = type;
        this.date = date;
        this.body = body;
        this.body_id = body_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getBody_id() {
        return body_id;
    }

    public void setBody_id(int body_id) {
        this.body_id = body_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
