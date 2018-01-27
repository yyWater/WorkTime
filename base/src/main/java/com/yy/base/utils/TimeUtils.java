package com.yy.base.utils;

import android.util.Log;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <br>author: wzg<br/>
 * date:   2017/5/6 12:06 <br/>
 */

public class TimeUtils {
    /**
     * 准备第一个模板，从字符串中提取出日期数字
     */
    private static String pat1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 准备第二个模板，将提取后的日期数字变为指定的格式
     */
    private static String pat2 = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * “yyyy-MM-dd”时间格式
     */
    private static String pat3 = "yyyy-MM-dd";

    /**
     * “MM-dd”时间格式
     */
    private static String pat4 = "MM-dd";
    /**
     * 实例化模板对象
     */
    private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
    private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);
    private static SimpleDateFormat sdf3 = new SimpleDateFormat(pat3);
    private static SimpleDateFormat sdf4 = new SimpleDateFormat(pat4);

    public static String getTime(long commitDateLong) {
        String commitDate = sdf1.format(commitDateLong);

        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(commitDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int nowDate = Integer.valueOf(currDate.substring(8, 10));
        int commit = Integer.valueOf(commitDate.substring(8, 10));
        String monthDay = sdf2.format(date).substring(5, 12);
        String yearMonthDay = sdf2.format(date).substring(0, 12);
        int month = Integer.valueOf(monthDay.substring(0, 2));
        int day = Integer.valueOf(monthDay.substring(3, 5));
        if (month < 10 && day < 10) {
            monthDay = monthDay.substring(1, 3) + monthDay.substring(4);
        } else if (month < 10) {
            monthDay = monthDay.substring(1);
        } else if (day < 10) {
            monthDay = monthDay.substring(0, 3) + monthDay.substring(4);
        }
        int yearMonth = Integer.valueOf(yearMonthDay.substring(5, 7));
        int yearDay = Integer.valueOf(yearMonthDay.substring(8, 10));
        if (yearMonth < 10 && yearDay < 10) {
            yearMonthDay = yearMonthDay.substring(0, 5) + yearMonthDay.substring(6, 8) + yearMonthDay.substring(9);
        } else if (yearMonth < 10) {
            yearMonthDay = yearMonthDay.substring(0, 5) + yearMonthDay.substring(6);
        } else if (yearDay < 10) {
            yearMonthDay = yearMonthDay.substring(0, 8) + yearMonthDay.substring(9);
        }
        String str = " 00:00:00";
        float currDay = farmatTime(currDate.substring(0, 10) + str);
        float commitDay = farmatTime(commitDate.substring(0, 10) + str);
        int currYear = Integer.valueOf(currDate.substring(0, 4));
        int commitYear = Integer.valueOf(commitDate.substring(0, 4));
        int flag = (int) (farmatTime(currDate) / 1000 - farmatTime(commitDate) / 1000);
        String des = null;
        String hourMin = commitDate.substring(11, 16);
        int temp = flag;
        if (temp < 60) {
            System.out.println("A");
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            } else {
                des = "刚刚";
            }
        } else if (temp < 60 * 60) {
            System.out.println("B");
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            } else {
                des = temp / 60 + "分钟以前";
            }
        } else if (temp < 60 * 60 * 24) {
            System.out.println("C");
            int hour = temp / (60 * 60);
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            } else {
                if (hour < 6) {
                    des = hour + "小时前";
                } else {
                    des = hourMin;
                }
            }
        } else if (temp < (60 * 60 * 24 * 2)) {
            System.out.println("D");
            if (nowDate - commit == 1) {
                des = "昨天  " + hourMin;
            } else {
                des = "前天  " + hourMin;
            }
        } else if (temp < 60 * 60 * 60 * 3) {
            System.out.println("E");
            if (nowDate - commit == 2) {
                des = "前天  " + hourMin;
            } else {
                if (commitYear < currYear) {
                    des = yearMonthDay + hourMin;
                } else {
                    des = monthDay + hourMin;
                }
            }
        } else {
            System.out.println("F");
            if (commitYear < currYear) {
                des = yearMonthDay + hourMin;
            } else {
                des = monthDay + hourMin;
            }
        }
        if (des == null) {
            des = commitDate;
        }
        return des;
    }

    public static Long farmatTime(String string) {
        Date date = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = Date(sf.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date Date(Date date) {
        Date datetimeDate;
        datetimeDate = new Date(date.getTime());
        return datetimeDate;
    }

    public static String formatTimeYearMonthDay(Date date) {
        return sdf3.format(date);
    }

    public static String formatTimeYearMonthDay(long date) {
        return sdf3.format(date);
    }

    public static String formatTimeMonthDay(long date) {
        return sdf4.format(date);
    }

    public static String getTodayDateString() {

        long ms = System.currentTimeMillis();
        Date date = new Date(ms);

        String format = "yyyy/MM/dd-HH";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        Log.w("time", "format: " + simpleDateFormat.format(date));

        return simpleDateFormat.format(date);
    }

    public static boolean isSameMonth(long data0, long date) {
        Calendar calcThis = Calendar.getInstance();
        calcThis.setTimeInMillis(data0);

        Calendar calcDate = Calendar.getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR))
            return false;
        if (calcThis.get(Calendar.MONTH) != calcDate.get(Calendar.MONTH))
            return false;

        return true;

    }

    public static int compareMonthInNextPrev(long data0, long date){
        Calendar calcThis = Calendar.getInstance();
        calcThis.setTimeInMillis(data0);

        Calendar calcDate = Calendar.getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR)){
            throw new InvalidParameterException();
        }

        return calcThis.get(Calendar.MONTH)-calcDate.get(Calendar.MONTH);
    }

}
