package com.liuzhuni.lzn.config;

import android.content.Context;

import com.liuzhuni.lzn.utils.PreferencesUtils;

/**
 * Created by Andrew Lee on 2015/4/30.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-30
 * Time: 15:23
 */
public class Check {

    public static boolean isLogin(Context context) {
        return PreferencesUtils.getBooleanFromSPMap(context, PreferencesUtils.Keys.IS_LOGIN);
    }
    public static boolean isNotFirst(Context context) {
        return PreferencesUtils.getBooleanFromSPMap(context, PreferencesUtils.Keys.IS_FIRST);
    }
    public static boolean isGuideFirst(Context context) {
        return PreferencesUtils.getBooleanFromSPMap(context, PreferencesUtils.Keys.IS_GUIDE_F);
    }
    public static boolean isSendClientId(Context context) {
        return !PreferencesUtils.getBooleanFromSPMap(context, PreferencesUtils.Keys.IS_SEND_CLIENTID,PreferencesUtils.Keys.USERINFO);
    }
    public static boolean hashead(Context context) {
        return  "".equals(PreferencesUtils.getValueFromSPMap(context, PreferencesUtils.Keys.HEAD_URL,"",PreferencesUtils.Keys.USERINFO))?false:true;
    }
    public static boolean hasNickname(Context context) {
        return  "".equals(PreferencesUtils.getValueFromSPMap(context, PreferencesUtils.Keys.NICKNAME,"",PreferencesUtils.Keys.USERINFO))?false:true;
    }
    public static boolean hasTel(Context context) {
        return  "".equals(PreferencesUtils.getValueFromSPMap(context, PreferencesUtils.Keys.TEL,"",PreferencesUtils.Keys.USERINFO))?false:true;
    }


}
