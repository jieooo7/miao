package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/8/17.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-17
 * Time: 09:31
 */
public class PushModel implements Serializable{

    private String title;
    private String text;
    private String content;

    public PushModel(String title, String text, String content) {
        this.title = title;
        this.text = text;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
