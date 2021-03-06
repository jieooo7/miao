package com.liuzhuni.lzn.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew Lee on 2015/4/15.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-15
 * Time: 15:51
 */
public class TextModify {

    private TextModify(){

    }
    private static class InstanceHolder{
        private static final TextModify instance = new TextModify();
    }
    public static TextModify getInstance(){
            return InstanceHolder.instance;
    }


    public boolean isEmail(String email){

//        Pattern p = Pattern.compile("^(\\w+[-|\\.]?)+\\w@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Pattern p = Pattern.compile("^[\\.a-zA-Z0-9_-]+@([a-zA-Z0-9_-]+\\.)+[a-zA-Z]{2,3}$");
//        Pattern p = Pattern.compile("([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public boolean isTel(String tel){

        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(tel);

        return m.matches();
    }
    public boolean isTelInsert(StringBuffer buffer){//判断邮箱输入时的手机号码

//        Pattern p = Pattern.compile("^1\\d{2}[0-9]{0,8}$");
        Pattern p = Pattern.compile("^1\\d{2}(\\s?[0-9]{0,4}){0,2}$");
        Matcher m = p.matcher(buffer.toString());

        return m.matches();
    }



    public boolean isCode(String code){

        Pattern p = Pattern.compile("^\\d{6}$");
        Matcher m = p.matcher(code);

        return m.matches();
    }


    public boolean isNickName(String code){

        Pattern p = Pattern.compile("^(?<!_)\\w{2,10}$");
        Matcher m = p.matcher(code);

        return m.matches();
    }




    public String mobileAdjust(String bind_mobile) {
        if (!TextUtils.isEmpty(bind_mobile)) {
            Pattern p = Pattern.compile("(\\d{3})(\\d{4})(\\d{4})");
            Matcher m = p.matcher(bind_mobile);
            bind_mobile = m.replaceAll("$1****$3");
            return bind_mobile;
        } else {
            return "";
        }

    }
    public String emailAdjust(String email) {
        if (!TextUtils.isEmpty(email)) {
            Pattern p = Pattern.compile("(\\w{1,})@(\\w+)");
            Matcher m = p.matcher(email);
            String last = m.replaceAll("@$2");
            String first=m.replaceAll("$1");

            if(first.length()==1){
                return first+last;
            }
            if(first.length()==2){

                return TextUtils.substring(first,0,1)+last;

            }
            if(first.length()==3){
                return TextUtils.substring(first,0,1)+"*"+TextUtils.substring(first,2,3)+last;

            }
            if(first.length()==4){
                return TextUtils.substring(first,0,2)+"*"+TextUtils.substring(first,3,4)+last;
            }
            if(first.length()==5){
                return TextUtils.substring(first,0,3)+"*"+TextUtils.substring(first,4,5)+last;
            }

            return TextUtils.substring(first,0,3)+printStar(first.length()-4)+TextUtils.substring(first,first.length()-1,first.length())+last;
        } else {
            return "";
        }

    }

    public String printStar(int num){
        StringBuffer buffer=new StringBuffer();
        int no=num;
        while(no>0){
            buffer.append("*");
            no--;

        }
        return buffer.toString();
    }


    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }








}
