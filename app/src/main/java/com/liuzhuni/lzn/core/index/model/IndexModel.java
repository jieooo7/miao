package com.liuzhuni.lzn.core.index.model;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/4/22.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-22
 * Time: 10:15
 */
public class IndexModel implements Serializable{

    private String pic;
    private String name;
    private String key;
    private String time;

    public IndexModel(String img, String name, String red, String time) {
        this.pic = img;
        this.name = name;
        this.key = red;
        this.time = time;
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

    public SpannableString getKey() {
        if(TextUtils.isEmpty(key)){
            return null;
        }else{

            SpannableString ss = new SpannableString(key);
            ss.setSpan(new ForegroundColorSpan(Color.argb(0xff,0xf4,0x5f,0x60)),0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return ss;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
