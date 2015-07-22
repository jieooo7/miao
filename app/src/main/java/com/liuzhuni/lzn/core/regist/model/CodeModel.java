package com.liuzhuni.lzn.core.regist.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/5/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-13
 * Time: 20:50
 */
public class CodeModel implements Serializable{
    private String codes;

    public CodeModel(String codes) {
        this.codes = codes;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }
}
