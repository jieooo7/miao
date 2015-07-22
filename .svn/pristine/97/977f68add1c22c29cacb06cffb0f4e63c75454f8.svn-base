/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.liuzhuni.lzn.volley;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.liuzhuni.lzn.GetInfo;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.sec.aes.AESHelper;
import com.liuzhuni.lzn.utils.Md5Utils;
import com.liuzhuni.lzn.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GsonRequest<T> extends Request<T> {
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Listener<T> mListener;
    private final static String IMEI=((TelephonyManager)AppManager.getAppManager().currentActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    private final static String CLIENT="0";
    private final static String VERSION="1";
    private String mUrl="";
    private String customKey=new GetInfo().getKey(AppManager.getAppManager().currentActivity());
//    private static final Map<String, String> mMap=new HashMap<String, String>();
//    static{
////        mMap.put("content-type","application/json");
//        mMap.put("Custom-Auth-Name", PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.AUTH,"",PreferencesUtils.Keys.USERINFO));
//        mMap.put("Custom-Auth-Key", PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.TOKEN,"",PreferencesUtils.Keys.USERINFO));
//    }


	public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        mUrl=url;
    }

//	public GsonRequest(int method, String url, Class<T> clazz,Listener<T> listener, ErrorListener errorListener) {
//		super(method, url, errorListener);
//		this.mClazz = clazz;
//		this.mListener = listener;
//	}


	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> map=new HashMap<String, String>();
        Random random = new Random();
        long num=random.nextLong();
        String accessToken= PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.TOKEN, "", PreferencesUtils.Keys.USERINFO);
        String authName=PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.AUTH,"",PreferencesUtils.Keys.USERINFO);
        String md5Key="";
        String key="";
        if(TextUtils.isEmpty(accessToken)){
            md5Key= Md5Utils.md5(mUrl.toLowerCase() + IMEI + num).toLowerCase();//create md5
            key= AESHelper.encrypt(md5Key+" "+num, customKey);//aes encode

        }else{
            md5Key= Md5Utils.md5(mUrl.toLowerCase() + authName + num).toLowerCase();
            key=AESHelper.encrypt(md5Key+" "+num,AESHelper.decrypt(accessToken,customKey));
        }
//        System.out.println("authkey是:"+key);
//        System.out.println("md5是:"+md5Key.toLowerCase());
//        System.out.println("url是:"+mUrl.toLowerCase());
//        System.out.println("num是:"+num);

        map.put("Custom-Auth-Name", authName);
        map.put("Custom-Auth-Key",key);
        map.put("imei ",IMEI);
        map.put("client ",CLIENT);
        map.put("version ",VERSION);
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
			return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
