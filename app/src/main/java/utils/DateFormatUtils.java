package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DateFormatUtils {
    public final static String DEFAULT="yyyy-MM-dd HH:mm:ss";
    public final static String LIST_ITEM="yyyy-MM-dd";
    public static final String FORMATETYPE_DEFAULT="yyyy-MM-dd HH:mm:ss";//通用的时间显示
    public static final String FORMATETYPE_ITEM_MONTH="M月d日";//列表通用的时间显示

    /**
     * 把毫秒转化成日期
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    public static  String longToDate(String dateFormat,Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }

    public static String formatDateStr(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }


    /**格式化时间,如果格式错误则返回当前时间**/
    public static String formateDateToString(String time,String format,String oldFormat){
        SimpleDateFormat sdf=null;
        String date="";

        if("".equals(format)){
            return date;
        }

        try {
            Date tempDate=formateDate(time,oldFormat);
            sdf=new SimpleDateFormat(format);
            date=sdf.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if("".equals(date)){
            date=sdf.format(new Date());
        }
        return date;
    }

    /**返回指定格式的日期,如果日期格式错误则返回当前时间**/
    public static Date formateDate(String time,String format){
        SimpleDateFormat sdf=null;
        Date date=null;

        if("".equals(format)){
            return date;
        }
        try {
            sdf=new SimpleDateFormat(format);
            date=sdf.parse(time);
        } catch (Exception e) {
        }
        if(date==null){
            date=new Date();
        }
        return date;
    }
    public static String formateTimestampDate (String dateFormat,Long timeStr) {
        return formateTimestampDate(longToDate(dateFormat,timeStr));
    }


    /** 将时间戳转为代表"距现在多久之前"的字符串   时间戳  */
    public static String formateTimestampDate (String timeStr) {
        StringBuffer sb=null;
        long time=gettimestamp(timeStr);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        long month = (long) Math.ceil(time/30/24/60/60/1000.0f);// 月

        long year = (long) Math.ceil(time/12/30/24/60/60/1000.0f);// 年

        if(year>1){
            sb=setYear(year);
        }else  if(month>1){
            sb=setMonth(month, timeStr);
        } else  if (day - 1 > 0) {
            sb=setDay(day);
        } else if (hour - 1 > 0) {
            sb=setHour(hour);
        } else if (minute - 1 > 0) {
            sb=setMinute(minute);
        } else if (mill - 1 > 0) {
            sb=setMill(mill);
        } else {
            sb = new StringBuffer();
            sb.append("刚刚");
        }
        return sb.toString();
    }
    /** 将时间戳转为代表"距现在多久之前"的字符串   时间戳  */
    public static String formateTimestampDate (Long time) {
        StringBuffer sb=null;
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        long month = (long) Math.ceil(time/30/24/60/60/1000.0f);// 月

        long year = (long) Math.ceil(time/12/30/24/60/60/1000.0f);// 年

        if(year>1){
            sb=setYear(year);
        }else  if(month>1){
            //sb=setMonth(month, timeStr);
        } else  if (day - 1 > 0) {
            sb=setDay(day);
        } else if (hour - 1 > 0) {
            sb=setHour(hour);
        } else if (minute - 1 > 0) {
            sb=setMinute(minute);
        } else if (mill - 1 > 0) {
            sb=setMill(mill);
        } else {
            sb = new StringBuffer();
            sb.append("刚刚");
        }
        return sb.toString();
    }


    /**计算两个时间差**/
    public  static long gettimestamp(String date){
        Date curDate = formateDate(date,FORMATETYPE_DEFAULT);
        Date endDate = new Date(System.currentTimeMillis());
        long diff = endDate.getTime() - curDate.getTime();
        return diff;
    }


    /*设置年*/
    private static StringBuffer setYear(long year){
        StringBuffer sb = new StringBuffer();
        sb.append(year + "年");
        return sb;
    }

    /*设置月*/
    private static StringBuffer setMonth(long month,String timeStr){
        StringBuffer sb = new StringBuffer();
        sb.append(formateDateToString(timeStr,FORMATETYPE_ITEM_MONTH,FORMATETYPE_DEFAULT));
        return sb;
    }

    /*设置天*/
    private static StringBuffer setDay(long day){
        StringBuffer sb = new StringBuffer();
        sb.append(day-1 + "天前");
        return sb;
    }

    /*设置小时*/
    private static StringBuffer setHour(long hour){
        StringBuffer sb = new StringBuffer();
        if (hour >= 24) {
            sb.append("1天");
        } else {
            sb.append(hour + "小时前");
        }
        return sb;
    }
    /*设置分钟*/
    private static StringBuffer setMinute(long minute){
        StringBuffer sb = new StringBuffer();
        if (minute == 60) {
            sb.append("1小时");
        } else {
            sb.append(minute + "分钟前");
        }
        return sb;
    }
    /*设置秒*/
    private static StringBuffer setMill(long mill){
        StringBuffer sb = new StringBuffer();
        if (mill == 60) {
            sb.append("1分钟");
        } else {
            sb.append(mill + "秒前");
        }
        return sb;
    }
}
