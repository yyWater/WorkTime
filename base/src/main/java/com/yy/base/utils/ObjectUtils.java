package com.yy.base.utils;

import android.text.TextUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtils {
    public static boolean isListEmpty(Collection collection){
        return collection == null || collection.size() ==0;
    }

    /**
     * 判断Map是否为空
     *
     * @param map 待判断map对象
     * @return
     */
    public static boolean isMapEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    /**
     *判断Object是否为空
     * @param object
     * @return
     */
    public static boolean isObjectEmpty(Object object){
        if(object == null) return true;
        return false;
    }

    /**
     *判断String是否为空
     */
    public static boolean isStringEmpty(String obj){
        if(TextUtils.isEmpty(obj) || obj.equals("null")
                ||"".equals(obj) ||obj.length() < 0) return true;
        return false;
    }

    /**
     * 判断字符串list中是否重复元素
     * @param stringList
     * @return
     */
    public static boolean isStringListMemDuplicate(List<String> stringList){
        boolean isDuplicate = false;
        HashMap<String,String> tmpMap = new HashMap<>(stringList.size());

        for (String str : stringList) {
            if (tmpMap.containsKey(str)){
                isDuplicate = true;
                break;
            }else {
                tmpMap.put(str,str);
            }
        }

        return isDuplicate;

    }

    public static <T>boolean isListMemDuplicate (List<T> list){
        boolean isDuplicate = false;
        HashMap<T,T> tmpMap = new HashMap<>(list.size());

        for (T t : list) {
            if (tmpMap.containsKey(t)){
                isDuplicate = true;
                break;
            }else {
                tmpMap.put(t,t);
            }
        }

        return isDuplicate;

    }

    public static boolean isValidInt(String intStr){
       boolean isValid = false;

       try {
           Integer.valueOf(intStr);
           isValid = true;
       }catch (Exception e){
           e.printStackTrace();
       }

       return isValid;
    }
}
