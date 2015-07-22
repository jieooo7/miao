package com.liuzhuni.lzn.core.comment.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/7/21.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-21
 * Time: 10:18
 */
public class ReplyModel implements Serializable{

    private String userNick;
    private String text;


    public ReplyModel(String userNick, String text) {
        this.userNick = userNick;
        this.text = text;
    }


    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
