package com.yy.worktime.domain;

import android.util.SparseArray;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * date:   2017/10/24 11:06 <br/>
 */

public class CalendarManager {
    public final static String KEY_DATA_TYPE = "data_type";

    public final static String VALUE_DATA_TYPE_EVENT = "data_type_event";
    public final static String KEY_DATA_TYPE_EVENT = "event";
    public final static String KEY_SELECT_EVENT = "event_select";

    public final static String VALUE_DATA_TYPE_REPEAT = "data_type_repeat";
    public final static String KEY_DATA_TYPE_REPEAT_VALUE = "repeat";
    public final static String KEY_DATA_TYPE_REPEAT_STR = "repeat_str";

    public static SparseArray<String> MonthLabelMap = new SparseArray<>(12);
    static {
        MonthLabelMap.put(0,"Jan");
        MonthLabelMap.put(1,"Feb");
        MonthLabelMap.put(2,"Mar");
        MonthLabelMap.put(3,"Apr");
        MonthLabelMap.put(4,"May");
        MonthLabelMap.put(5,"Jun");
        MonthLabelMap.put(6,"Jul");
        MonthLabelMap.put(7,"Aug");
        MonthLabelMap.put(8,"Sep");
        MonthLabelMap.put(9,"Oct");
        MonthLabelMap.put(10,"Nov");
        MonthLabelMap.put(11,"Dec");
    }

    /**
     * 获取指定日期中前四个event的颜色列表
     * @param calendarDay
     * @return
     */
    public static List<String> getDayFirst4EventColors(CalendarDay calendarDay){
        // TODO: 2017/10/24

        List<String> colors = new ArrayList<>(4);
        if(calendarDay.getDay() == 21){
            colors.add("#F05D25");
        }

        if(calendarDay.getDay() == 22){
            colors.add("#F05D25");
            colors.add("#FF4081");
        }

        if(calendarDay.getDay() == 23){
            colors.add("#F05D25");
            colors.add("#FF4081");
            colors.add("#20FF20");
        }

        if(calendarDay.getDay() == 24){
            colors.add("#F05D25");
            colors.add("#FF4081");
            colors.add("#20FF20");
            colors.add("#8B4513");
        }


        return colors;
    }


    public static int getTimezoneOffset(){
        Calendar now = Calendar.getInstance();
        int offset = now.getTimeZone().getOffset(now.getTimeInMillis());
        return offset / 60 / 1000;
    }

    public  static boolean isToday(long date) {
        Calendar calcSet = Calendar.getInstance();
        calcSet.setTimeInMillis(date);

        Calendar calcNow = Calendar.getInstance();

        if (calcSet.get(Calendar.YEAR) != calcNow.get(Calendar.YEAR))
            return false;
        if (calcSet.get(Calendar.DAY_OF_YEAR) != calcNow.get(Calendar.DAY_OF_YEAR))
            return false;

        return true;
    }

    //把时间格式为当前的上午九点整
    public static long formatDateTo9Am(long dateMillisecond){
        Calendar calcThis = Calendar.getInstance();

        calcThis.setTimeInMillis(dateMillisecond);
        calcThis.set(Calendar.HOUR_OF_DAY,9);
        calcThis.set(Calendar.MINUTE,0);
        calcThis.set(Calendar.SECOND,0);
        calcThis.set(Calendar.MILLISECOND, 0);

        return calcThis.getTimeInMillis();
    }
}
