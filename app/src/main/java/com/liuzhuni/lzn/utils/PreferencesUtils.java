package com.liuzhuni.lzn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtils {

    //	ctrl+shift+x   转为大写     ctrl+shift+y   转为小写
    private static SharedPreferences preferences;

    /**
     * cn.icnt.dinners.utils.Keys
     *
     * @author Andrew J. Lee <br/>
     *         create at 2014年7月17日 下午7:39:32
     */
    public static class Keys {
        /**
         * 版本号
         */
        public static final String VERSION = "version";
        public static final String TEST = "test";


        /**
         * userconfig信息
         */
        public static final String IS_LOGIN = "is_login";
        public static final String IS_FIRST = "is_first";
        public static final String PUSH_SET = "push_set";
        public static final String PUSH_LOW_NOTIFY = "push_low_notify";
        public static final String PUSH_RECOM = "push_recom";
        public static final String PUSH_QUIET = "push_quiet";
        public static final String IS_WELCOME = "is_welcome";
        public static final String IS_FIRST_CODE = "is_first_code";
        public static final String IS_DIALOG = "is_dialog";

        public static final String FILTER = "filter";






        /*
        * userinfo信息
        *
        * */
        public static final String AUTH = "auth";
        public static final String TOKEN = "token";
        public static final String SEX = "sex";
        public static final String IS_SEND_SEX = "send_sex";


        public static final String UID = "uid";
        public static final String UN_READ = "un_read";
        public static final String TEL = "tel";
        public static final String EMAIL = "email";
        public static final String NICKNAME = "nickName";
        public static final String CLIENTID = "clientId";
        public static final String IS_SEND_CLIENTID = "isSendclientId";
        public static final String PASSWORD = "password";
        public static final String PWD_KEY = "pwd_key";
        public static final String BUSS_PWD = "buss_pwd";
        public static final String NAME = "name";
        public static final String HEAD_URL = "headUrl";
        public static final String LEVEL = "level";
        public static final String MAX_ID = "max_id";
        public static final String IS_THIRD = "is_third";


        /**
         * 用户名
         */
        /**
         * sharedPreference 文件名
         */
        public static final String USERCONFIG = "userConfig";
        public static final String USERINFO = "userInfo";


    }

    /**
     * 存储布尔值
     *
     * @param mContext
     * @param key
     * @param value
     */
    public static void putBooleanToSPMap(Context mContext, String key, boolean value) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void putBooleanToSPMap(Context mContext, String key, boolean value, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void putIntToSPMap(Context mContext, String key, int value) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putIntToSPMap(Context mContext, String key, int value, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }


    public static int getIntFromSPMap(Context mContext, String key) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        int value = preferences.getInt(key, 0);
        return value;
    }

    public static int getIntFromSPMap(Context mContext, String key, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int value = preferences.getInt(key, 0);
        return value;
    }

    /**
     * 获取布尔值
     *
     * @param mContext
     * @param key
     * @return
     */
    public static Boolean getBooleanFromSPMap(Context mContext, String key) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

    public static Boolean getBooleanFromSPMap(Context mContext, String key, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

    /**
     * 存储String
     *
     * @param mContext
     * @param key
     * @param value
     */
    public static void putValueToSPMap(Context mContext, String key, String value) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putValueToSPMap(Context mContext, String key, String value, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 获取String
     *
     * @param mContext
     * @param key
     * @return value
     */
    public static String getValueFromSPMap(Context mContext, String key) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
            String value = preferences.getString(key, "");
            return value;
        } else {
            return null;
        }
    }

    /**
     * 获取String
     *
     * @param mContext
     * @param key
     * @param defaults 无值时取defaults
     * @return
     */
    public static String getValueFromSPMap(Context mContext, String key, String defaults) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
            String value = preferences.getString(key, defaults);
            return value;
        } else {
            return null;
        }
    }

    public static String getValueFromSPMap(Context mContext, String key, String defaults, String fileName) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            String value = preferences.getString(key, defaults);
            return value;
        } else {
            return null;
        }
    }

    /**
     * 清除全部
     *
     * @param mContext
     */
    public static void clearSPMap(Context mContext) {
        preferences = mContext.getSharedPreferences(Keys.USERCONFIG, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
    }

    public static void clearSPMap(Context mContext, String fileName) {
        preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
    }


    /**
     * 指定key清除
     *
     * @param mContext
     * @param key
     */
    public static void clearSomeSpMap(Context mContext, String key) {
        putValueToSPMap(mContext, key, "");
    }

    public static void clearSomeSpMap(Context mContext, String key, String fileName) {
        putValueToSPMap(mContext, key, "", fileName);
    }

    /**
     * 用户注销 清除指定key
     *
     * @param mContext
     */
    public static void logout_del(Context mContext) {
        putValueToSPMap(mContext, Keys.UID, "-1");
    }
}
