package com.liuzhuni.lzn.core.index_new.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/9/1.
 * E-mail:jieooo7@163.com
 * Date: 2015-09-01
 * Time: 15:34
 */
public class ShareFirstModel implements Serializable{


    private boolean isshare;

    public ShareFirstModel(boolean isshare) {
        this.isshare = isshare;
    }

    public boolean isshare() {
        return isshare;
    }

    public void setIsshare(boolean isshare) {
        this.isshare = isshare;
    }
}
