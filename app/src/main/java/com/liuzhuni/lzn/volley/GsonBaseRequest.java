package com.liuzhuni.lzn.volley;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.liuzhuni.lzn.GetInfo;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.sec.aes.AESHelper;
import com.liuzhuni.lzn.utils.Md5Utils;
import com.liuzhuni.lzn.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew Lee on 2015/5/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-13
 * Time: 11:11
 */
public class GsonBaseRequest<T>  extends Request<T> {
    private final Gson mGson = new Gson();
    private final Type mType;
    private final Response.Listener<T> mListener;
    private final static String IMEI=((TelephonyManager)AppManager.getAppManager().currentActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    private final static String CLIENT="0";
    private final static String VERSION= UrlConfig.VERSION_CODE;
    private String mUrl="";
    private String customKey=new GetInfo().getKey(AppManager.getAppManager().currentActivity());

    public GsonBaseRequest(int method, String url, Type type, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mType = type;
        this.mListener = listener;
        mUrl=url;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map=new HashMap<String, String>();
        Date date=new Date();
        long timeToken=date.getTime();
        long timeTokenSec=timeToken/1000;
        String accessToken=PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.TOKEN, "", PreferencesUtils.Keys.USERINFO);
        String authName=PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.AUTH,"",PreferencesUtils.Keys.USERINFO);
        String md5Key="";
        String key="";
        if(TextUtils.isEmpty(accessToken)){
            md5Key= Md5Utils.md5(mUrl.toLowerCase() + IMEI +timeTokenSec).toLowerCase();//create md5
            key= AESHelper.encrypt(md5Key,customKey);//aes encode

        }else{
            md5Key= Md5Utils.md5(mUrl.toLowerCase() + authName + timeTokenSec).toLowerCase();
            key=AESHelper.encrypt(md5Key,AESHelper.decrypt(accessToken,customKey));
        }
//        System.out.println("authkey是:"+key);
//        System.out.println("md5是:"+md5Key.toLowerCase());
//        System.out.println("url是:"+mUrl.toLowerCase());
//        System.out.println("num是:"+num);
//        System.out.println("内容是:"+mUrl.toLowerCase()+authName+num);
        map.put("Custom-Auth-Name", authName);
        map.put("Custom-Auth-Key",key);
        map.put("imei ",IMEI);
        map.put("client ",CLIENT);
        map.put("version ",VERSION);
        map.put("timeToken ",""+timeTokenSec);
        if(mUrl.equals(UrlConfig.USER_SIGN)){
            map.put("Content-Length","0");
        }
        return  map;
    
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }




    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return  Response.success((T)mGson.fromJson(json, mType),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}