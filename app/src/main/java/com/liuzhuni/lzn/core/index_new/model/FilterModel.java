package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/7/14.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-14
 * Time: 16:00
 */
public class FilterModel implements Serializable {
    private String id;
    private String href;
    private String name;
    private boolean selected = true;

    public FilterModel(String id, String href, String name, boolean selected) {
        this.id = id;
        this.href = href;
        this.name = name;
        this.selected=true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTheName() {
        return name;
    }

    public void setTheName(String name) {
        this.name = name;
    }
}
