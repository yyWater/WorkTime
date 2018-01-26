package com.yy.worktime.model;

import android.util.Log;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 */

public class WatchEvent implements Serializable {
    public final static String REPEAT_NEVER = "";
    public final static String REPEAT_DAILY = "DAILY";
    public final static String REPEAT_WEEKLY = "WEEKLY";
    public final static String REPEAT_MONTHLY = "MONTHLY";
    public final static String REPEAT_WEEKDAY = "WEEKDAY";//add 2018年1月22日15:54:21

    public long mId;
    public long mUserId;
    public List<Long> mKids;
    public String mName;
    public long mStartDate;
    public long mEndDate;
    public String mColor;
    public String mStatus;
    public String mDescription;
    public int mAlert;
    public String mRepeat;
    public int mTimezoneOffset;
    public long mDateCreated;
    public long mLastUpdated;
//    public List<WatchTodo> mTodoList;
    public long mAlertTimeStamp;

    public WatchEvent() {
        Calendar calc = Calendar.getInstance();
        long now = calc.getTimeInMillis();

        calc.set(Calendar.MINUTE, 0);
        calc.set(Calendar.SECOND, 0);
        calc.set(Calendar.MILLISECOND, 0);

        long start = calc.getTimeInMillis();

        calc.add(Calendar.HOUR_OF_DAY, 1);
        long end = calc.getTimeInMillis();

        int color = ColorList[4];

        init(0, 0, new ArrayList<Long>(), "", start, end, colorToString(color), "", "", ALARM_INVALID, REPEAT_NEVER, 0, now, now);
    }

    public WatchEvent(long date) {
        Calendar calc = Calendar.getInstance();
        long now = calc.getTimeInMillis();
        int hour = calc.get(Calendar.HOUR_OF_DAY);
        int minute = calc.get(Calendar.MINUTE);

        calc.setTimeInMillis(date);
        calc.set(Calendar.HOUR_OF_DAY, hour);
        calc.set(Calendar.MINUTE, minute);
        calc.set(Calendar.SECOND, 0);
        calc.set(Calendar.MILLISECOND, 0);

        long start = calc.getTimeInMillis();

        //根据要求，event的start，end之间默认间隔1分钟
//        calc.add(Calendar.HOUR_OF_DAY, 1);
        calc.add(Calendar.MINUTE, 1);

        long end = calc.getTimeInMillis();

        int color = ColorList[4];

        init(0, 0, new ArrayList<Long>(), "", start, end, colorToString(color), "", "", ALARM_INVALID, REPEAT_NEVER, 0, now, now);
    }

    public WatchEvent(long startDate, long endDate) {
        long now = System.currentTimeMillis();

        int color = ColorList[4];

        init(0, 0, new ArrayList<Long>(), "", startDate, endDate, colorToString(color), "", "", ALARM_INVALID, REPEAT_NEVER, 0, now, now);
    }

    public WatchEvent(long id, long userId, String name,
                      int startYear, int startMonth, int startDay, int startHour, int startMinute,
                      int endYear, int endMonth, int endDay, int endHour, int endMinute,
                      int color, String description, int alert, String repeat, int... kids) {
        Calendar cale = Calendar.getInstance();
        long now = cale.getTimeInMillis();

        cale.set(Calendar.YEAR, startYear);
        cale.set(Calendar.MONTH, startMonth);
        cale.set(Calendar.DAY_OF_MONTH, startDay);
        cale.set(Calendar.HOUR_OF_DAY, startHour);
        cale.set(Calendar.MINUTE, startMinute);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        long start = cale.getTimeInMillis();

        cale.set(Calendar.YEAR, endYear);
        cale.set(Calendar.MONTH, endMonth);
        cale.set(Calendar.DAY_OF_MONTH, endDay);
        cale.set(Calendar.HOUR_OF_DAY, endHour);
        cale.set(Calendar.MINUTE, endMinute);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        long end = cale.getTimeInMillis();

        List<Long> list = new ArrayList<>();
        for (long kid : kids)
            list.add(kid);

        init(id, userId, list, name, start, end, colorToString(color), "",
                description, alert, repeat, 0, now, now);
    }

    public WatchEvent(int id, int userId, List<Long> kids, String name, long startDate,
                      long endDate, String color, String status, String description,
                      int alert, String repeat, int timezoneOffset, long dateCreated, long lastUpdated) {
        init(id, userId, kids, name, startDate, endDate, color, status, description,
                alert, repeat, timezoneOffset, dateCreated, lastUpdated);
    }

    public WatchEvent(WatchEvent src) {
        init(src.mId, src.mUserId, src.mKids, src.mName, src.mStartDate, src.mEndDate, src.mColor, src.mStatus, src.mDescription,
                src.mAlert, src.mRepeat, src.mTimezoneOffset, src.mDateCreated, src.mLastUpdated);

        mAlertTimeStamp = src.mAlertTimeStamp;
        /*for (WatchTodo todo : src.mTodoList)
            mTodoList.add(new WatchTodo(todo));*/
    }

    private void init(long id, long userId, List<Long> kids, String name, long startDate,
                      long endDate, String color, String status, String description,
                      int alert, String repeat, int timezoneOffset, long dateCreated, long lastUpdated) {
        mId = id;
        mUserId = userId;
        mKids = kids;
        mName = name;
        mStartDate = startDate;
        mEndDate = endDate;
        mColor = color;
        mStatus = status;
        mDescription = description;
        mAlert = alert;
        mRepeat = repeat;
        mTimezoneOffset = timezoneOffset;
        mDateCreated = dateCreated;
        mLastUpdated = lastUpdated;
        mAlertTimeStamp = startDate;

        sortDate();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH/mm/ss", Locale.US);

        return new StringBuilder()
                .append("{mId:").append(mId)
                .append(" mUserId:").append(mUserId)
                .append(" mKids:").append(mKids.toString())
                .append(" mName:").append(mName)
                .append(" mStartDate:").append(mStartDate)
//                .append(" mStartDate:").append(sdf.format(mStartDate))
                .append(" mEndDate:").append(mEndDate)
//                .append(" mEndDate:").append(sdf.format(mEndDate))
                .append(" mColor:").append(mColor)
                .append(" mStatus:").append(mStatus)
                .append(" mDescription:").append(mDescription)
                .append(" mAlert:").append(mAlert)
                .append(" mRepeat:").append(mRepeat)
                .append(" mTimezoneOffset:").append(mTimezoneOffset)
                .append(" mDateCreated:").append(sdf.format(mDateCreated))
                .append(" mLastUpdated:").append(sdf.format(mLastUpdated))
//                .append(" mTodoList:").append(mTodoList.toString())
                .append(" mAlertTimeStamp:").append(mAlertTimeStamp)
                .append("}").toString();
    }

    private void sortDate() {
        if (mStartDate <= mEndDate)
            return;

        long tmp = mStartDate;
        mStartDate = mEndDate;
        mEndDate = tmp;
    }

    public boolean overLapping(WatchEvent event) {
        return !(mEndDate <= event.mStartDate || event.mEndDate <= mStartDate);
    }

    public boolean overLapping(List<WatchEvent> list) {
        for (WatchEvent event : list)
            if (overLapping(event))
                return true;
        return false;
    }

    public boolean containsKid(long id) {
        for (Long kid : mKids)
            if (kid == id)
                return true;
        return false;
    }

    public void removeKid(long id) {
        int count = mKids.size();
        for (int idx = 0; idx < count; idx++) {
            if (mKids.get(idx) != id)
                continue;

            mKids.remove(idx);
            count--;
        }
    }

    public void insertKid(long id, int position) {
        if (!containsKid(id))
            mKids.add(position, id);
    }

    static public WatchEvent earliestInDay(long after, List<WatchEvent> list) {
        if (list.size() == 0)
            return null;

        WatchEvent earliest = null;
        for (WatchEvent event : list) {
            if (event.mStartDate < after)
                continue;

            if (earliest == null || earliest.mStartDate > event.mStartDate)
                earliest = event;
        }

        return earliest;
    }

    static public WatchEvent earliestInDay(List<WatchEvent> list) {
        if (list.size() == 0)
            return null;

        WatchEvent earliest = list.get(0);
        for (WatchEvent event : list) {
            if (event.mStartDate < earliest.mStartDate)
                earliest = event;
        }

        return earliest;
    }

    static public WatchEvent lastInDay(List<WatchEvent> list) {
        if (list.size() == 0)
            return null;

        WatchEvent last = list.get(0);
        for (WatchEvent event : list) {
            if (event.mStartDate > last.mStartDate)
                last = event;
        }

        return last;
    }

    static String colorToString(int color) {
        String string = "#000000";

        color &= 0x00FFFFFF; // Format is argb, remove alpha value.

        try {
            string = String.format("#%06X", color);
        } catch (NumberFormatException e) {
            Log.d("Swing", e.getMessage());
        }

        return string;
    }

    public static int stringToColor(String string) {
        int color = 0;

        if (string.length() != 7 || string.charAt(0) != '#')
            return 0;

        string = string.substring(1);
        try {
            color = Integer.parseInt(string, 16);
            color |= 0xFF000000;
        } catch (NumberFormatException e) {
            Log.d("Swing", e.getMessage());
        }

        return color;
    }

    public final static int[] ColorList = new int[]{//FF7230
            0xFFFAD13E, 0xFF7572C1, 0xFF00C4B3, 0xFFF54A7E, 0xFFFF7230, 0xFF9A989A};

    static public class Alarm {
        public int mId;
        public int mName;
        public int mResource;

        Alarm(int id, int name, int resource) {
            mId = id;
            mName = name;
            mResource = resource;
        }
    }

    public final static int ALARM_INVALID = -1;

}
