package com.yy.worktime.view.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TableLayout;


import com.yy.worktime.R;

import java.util.Calendar;
import java.util.Locale;


// Calendar物件的基类, 所有ViewCalendarXXX均是继承自ViewCalendar
public class ViewCalendar extends TableLayout {

    protected int mTextSize = 20;
    protected int mTextStyle = Typeface.BOLD;
    protected int mTextColor = Color.BLACK;
    protected int mTodayColor = Color.WHITE;
    protected int mFocusColor = Color.TRANSPARENT;
    protected int mFocusBackgroundColor = Color.TRANSPARENT;
    protected int mTodayBackgroundColor = Color.TRANSPARENT;
    protected int mExceedColor = Color.LTGRAY;

    protected int mWeekNameTextColor = Color.BLACK;


    protected long mDate = System.currentTimeMillis();

    public ViewCalendar(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(
                    attrs, R.styleable.ViewCalendar);

            final int count = typedArray.getIndexCount();
            for (int idx = 0; idx < count; idx++) {
                final int attr = typedArray.getIndex(idx);

                if (attr == R.styleable.ViewCalendar_android_textSize) {
                    mTextSize = typedArray.getDimensionPixelOffset(attr, mTextSize);
                } else if (attr == R.styleable.ViewCalendar_android_textStyle) {
                    mTextStyle = typedArray.getInt(attr, mTextStyle);
                } else if (attr == R.styleable.ViewCalendar_android_textColor) {
                    mTextColor = typedArray.getColor(attr, mTextColor);
                } else if (attr == R.styleable.ViewCalendar_todayColor) {
                    mTodayColor = typedArray.getColor(attr, mTodayColor);
                } else if (attr == R.styleable.ViewCalendar_focusColor) {
                    mFocusColor = typedArray.getColor(attr, mFocusColor);
                } else if (attr == R.styleable.ViewCalendar_focusBackgroundColor) {
                    mFocusBackgroundColor = typedArray.getColor(attr, mFocusBackgroundColor);
                } else if (attr == R.styleable.ViewCalendar_exceedColor) {
                    mExceedColor = typedArray.getColor(attr, mExceedColor);
                }else if(attr == R.styleable.ViewCalendar_weekNameColor){
                    mWeekNameTextColor = typedArray.getColor(attr, mWeekNameTextColor);
                }else if(attr == R.styleable.ViewCalendar_todayBackgroundColor){
                    mTodayBackgroundColor = typedArray.getColor(attr, mTodayBackgroundColor);
                }
            }

            typedArray.recycle();
        }
    }

    public void setTextSize(int pixel) {
        mTextSize = pixel;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTodayColor(int color) {
        mTodayColor = color;
    }

    public int getTodayColor() {
        return mTodayColor;
    }

    public void setFocusColor(int color) {
        mFocusColor = color;
    }

    public int getFocusColor() {
        return mFocusColor;
    }

    public void setFocusBackgroundColor(int color) {
        mFocusBackgroundColor = color;
    }

    public int getFocusBackgroundColor() {
        return mFocusBackgroundColor;
    }

    public void setExceedColor(int color) {
        mExceedColor = color;
    }

    public int getExceedColor() {
        return mExceedColor;
    }

    public int getTodayBackgroundColor() {
        return mTodayBackgroundColor;
    }

    public void setTodayBackgroundColor(int mTodayBackgroundColor) {
        this.mTodayBackgroundColor = mTodayBackgroundColor;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public long getDate() {
        return mDate;
    }

    public boolean isSameDay(long date) {
        Calendar calcThis = getInstance();
        calcThis.setTimeInMillis(mDate);

        Calendar calcDate = getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR))
            return false;
        if (calcThis.get(Calendar.DAY_OF_YEAR) != calcDate.get(Calendar.DAY_OF_YEAR))
            return false;

        return true;
    }

    public boolean isSameWeek(long date) {
        Calendar calcThis = getInstance();
        calcThis.setTimeInMillis(mDate);

        Calendar calcDate = getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR))
            return false;
        if (calcThis.get(Calendar.WEEK_OF_YEAR) != calcDate.get(Calendar.WEEK_OF_YEAR))
            return false;

        return true;
    }

    public boolean isSameMonth(long date) {
        Calendar calcThis = getInstance();
        calcThis.setTimeInMillis(mDate);

        Calendar calcDate = getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR))
            return false;
        if (calcThis.get(Calendar.MONTH) != calcDate.get(Calendar.MONTH))
            return false;

        return true;
    }

    public boolean isSameYear(long date) {
        Calendar calcThis = getInstance();
        calcThis.setTimeInMillis(mDate);

        Calendar calcDate = getInstance();
        calcDate.setTimeInMillis(date);

        if (calcThis.get(Calendar.YEAR) != calcDate.get(Calendar.YEAR))
            return false;

        return true;
    }

    public long getDateBegin() {
        return mDate;
    }

    public long getDateEnd() {
        return mDate;
    }

    static Calendar getInstance() {
//        return Calendar.getInstance();
//        return Calendar.getInstance(Locale.US);       // Force to use Sunday to be the first day of week
        return Calendar.getInstance(Locale.GERMANY);  // Force to use Monday to be the first day of week
    }

    static boolean isToday(long date) {
        Calendar calcSet = getInstance();
        calcSet.setTimeInMillis(date);

        Calendar calcNow = getInstance();

        if (calcSet.get(Calendar.YEAR) != calcNow.get(Calendar.YEAR))
            return false;
        if (calcSet.get(Calendar.DAY_OF_YEAR) != calcNow.get(Calendar.DAY_OF_YEAR))
            return false;

        return true;
    }

    static boolean isMonth(long date) {
        Calendar calcSet = getInstance();
        calcSet.setTimeInMillis(date);

        Calendar calcNow = getInstance();

        if (calcSet.get(Calendar.YEAR) != calcNow.get(Calendar.YEAR))
            return false;
        if (calcSet.get(Calendar.MONTH) != calcNow.get(Calendar.MONTH))
            return false;

        return true;
    }

    static public long stripTime(long date) {
        Calendar calc = getInstance();

        calc.setTimeInMillis(date);
        calc.set(Calendar.HOUR_OF_DAY, 0);
        calc.clear(Calendar.MINUTE);
        calc.clear(Calendar.SECOND);
        calc.clear(Calendar.MILLISECOND);

        return calc.getTimeInMillis();
    }
}
