package com.liuzhuni.lzn.core.siri.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/4/28.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-28
 * Time: 11:51
 */
public class SiriModel<T> implements Serializable{
    private int type;
    private int id;
    private T body;
    private int delayTime=1200;
    private int flag=0;

    public SiriModel(int type, int id, T body, int delayTime) {
        this.type = type;
        this.id = id;
        this.body = body;
        this.delayTime = delayTime;
    }
    public SiriModel(int type, int id, T body, int delayTime,int flag) {
        this.type = type;
        this.id = id;
        this.body = body;
        this.delayTime = delayTime;
        this.flag=flag;
    }

    public SiriModel(int type, int id, T body) {
        this.type = type;
        this.id = id;
        this.body = body;
    }

    public SiriModel(int type, T body) {
        this.type = type;
        this.body = body;
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
