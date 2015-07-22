package com.liuzhuni.lzn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Andrew Lee on 2015/4/22.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-22
 * Time: 11:31
 */
public class TimeUtil {

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
    private SimpleDateFormat mWeekFormat = new SimpleDateFormat("EEEE HH:mm");
    private SimpleDateFormat mDateFormatTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat mDateFormatDay = new SimpleDateFormat("dd");

    private TimeUtil(){

    }

    private static class InstanceHolder{
        private static final TimeUtil instance = new TimeUtil();
    }


    public static TimeUtil getInstance(){

        return InstanceHolder.instance;
    }

    public String timeFormat(String time){
        Date now=new Date();
        long currentTime=now.getTime();
        long oldTime=strToDate(time).getTime();
        long newTIme=(currentTime-oldTime)/1000;

        if(newTIme>0&&newTIme<60){
            return newTIme+"秒钟前";
        }
        if(newTIme>=60&newTIme<3600){
            return newTIme/60+"分钟前";
        }

        if (newTIme >= 3600 && newTIme < 3600 * 24) {
            return newTIme / 3600 + "小时前";
        }

        if(newTIme > 3600 * 24&&newTIme<3600*24*2){
            return  "昨天"+mDateFormatTime.format(oldTime);
        }

//        return time;
        return mDateFormat.format(oldTime);

    }



    public String timeFormat(long oldTime){
        Date now=new Date();
        long currentTime=now.getTime();
//        long oldTime=strToDate(time).getTime();
        long newTIme=(currentTime-oldTime)/1000;

        if(newTIme<600){
            return "";
        }

        if(newTIme<3600 * 24){
//            if (newTIme<=3){
//                newTIme=3;
//            }
//            return newTIme+"秒钟前";
            if(mDateFormatDay.format(oldTime).equals(mDateFormatDay.format(now))){
                return  mDateFormatTime.format(oldTime);
            }else{

                return  "昨天"+mDateFormatTime.format(oldTime);
            }
        }

        if(newTIme>=3600 * 24&&newTIme<3600 * 24*2){
            if(mDateFormatDay.format(oldTime).equals(mDateFormatDay.format(currentTime-3600 * 24))){
                return  "昨天"+mDateFormatTime.format(oldTime);
            }else{

                return  "前天"+mDateFormatTime.format(oldTime);
            }
        }

        if(newTIme>=3600 * 24*2&&newTIme<3600 * 24*7){
            if(mDateFormatDay.format(oldTime).equals(mDateFormatDay.format(currentTime-3600 * 24*2))){
                return  "前天"+mDateFormatTime.format(oldTime);
            }else{

                return  mWeekFormat.format(oldTime);
            }
        }

//        if(newTIme>=60&newTIme<3600){
//            return newTIme/60+"分钟前";
//        }
//
//        if (newTIme >= 3600 && newTIme < 3600 * 24) {
//            return newTIme / 3600 + "小时前";
//        }

//        if(newTIme > 3600 * 24&&newTIme<3600*24*2){
//            return  "昨天"+mDateFormatTime.format(oldTime);
//        }

//        return time;
        return mDateFormat.format(oldTime);

    }


    public Date strToDate(String str) {
        // sample：Tue May 31 17:46:55 +0800 2011
        // E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = format.parse(str);
        } catch (Exception e) {
        }
        return result;

    }


    public String second2Time(long ld){
        Date date = new Date(ld);
        //标准日历系统类
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        //java.text.SimpleDateFormat，设置时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //得到毫秒值转化的时间
        String time = format.format(gc.getTime());
        return time;
    }

    public String getCurrentTime(){

        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");

        return format.format(date);
    }
}
