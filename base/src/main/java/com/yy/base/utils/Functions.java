package com.yy.base.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <br>author: wzg<br/>
 * date:   2017/4/26 18:57 <br/>
 */

public class Functions {

    /**
     * 验证emailAddr是否为有效地址
     */
   /* public static boolean isValidEmail(String emailAddr) {
        Matcher matcher = Regex.EMAIL_ADDRESS_PATTERN.matcher(emailAddr);

        return matcher.matches();
    }*/

    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean containTheDay(List<Date> dateList, Date theDate){
        if(dateList == null || dateList.size() <= 0){
            return false;
        }

        boolean isContain = false;

        for (int i = dateList.size()-1; i  >= 0; i--) {
            if (areSameDay(dateList.get(i), theDate)) {
                isContain = true;
                break;
            }
        }

        return isContain;

    }

    /**
     * 从字符串转换成long；如果转换失败，则返回-1
     * @param longStr
     * @return
     */
    public static long getLongSafely(String longStr){
        if(TextUtils.isEmpty(longStr)){
            return -1;
        }

        long numLong = -1;

        if(!TextUtils.isEmpty(longStr)){
            try{
                numLong = Long.valueOf(longStr);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return numLong;
    }

    /**
     * 从字符串转换成int；如果转换失败，则返回-1
     * @param intStr
     * @return
     */
    public static long getIntSafely(@NonNull String intStr){
        if(TextUtils.isEmpty(intStr)){
            return -1;
        }

        long numInt = -1;

        if(!TextUtils.isEmpty(intStr)){
            try{
                numInt = Integer.valueOf(intStr);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return numInt;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}