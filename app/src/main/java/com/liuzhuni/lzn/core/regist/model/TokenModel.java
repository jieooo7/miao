package com.liuzhuni.lzn.core.regist.model;

import java.io.Serializable;

/**
 * Created by Andrew Lee on 2015/5/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-13
 * Time: 21:08
 */
public class TokenModel implements Serializable{

    private String token;
    private String authName;

    public TokenModel(String token, String authname) {
        this.token = token;
        this.authName = authname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthname() {
        return authName;
    }

    public void setAuthname(String authname) {
        this.authName = authname;
    }
}
