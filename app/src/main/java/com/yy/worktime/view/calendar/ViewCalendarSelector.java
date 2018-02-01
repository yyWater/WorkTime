package com.yy.worktime.view.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yy.worktime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 */

public class ViewCalendarSelector extends ViewCalendar implements View.OnClickListener {
    static final int SELECT_PREV = -1;
    static final int SELECT_NEXT = 1;
    static final int SELECT_DATE = 0;

    static final int MODE_INVALID = -1;
    static final int MODE_YEAR = 0;
    static final int MODE_MONTH = 1;
    static final int MODE_WEEK = 2;
    static final int MODE_DAY = 3;
    protected int mMode = MODE_INVALID;

    private ViewCalendar mThis = this;
    private TextView mViewDate;
    private TextView mViewPrev;
    private TextView mViewNext;

    private OnSelectListener mSelectListener = null;

    public ViewCalendarSelector(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(
                    attrs, R.styleable.ViewCalendarSelector);

            final int count = typedArray.getIndexCount();
            for (int idx = 0; idx < count; idx++) {
                final int attr = typedArray.getIndex(idx);

                if (attr == R.styleable.ViewCalendarSelector_calendarMode) {
                    mMode = typedArray.getInt(attr, mMode);
                }
            }

            typedArray.recycle();
        }

        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1));

        mViewPrev = new TextView(context);
        mViewPrev.setText("◀"); // U+25C0 &#9664;
        mViewPrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mViewPrev.setTextColor(mTextColor);
        mViewPrev.setGravity(Gravity.CENTER);
        mViewPrev.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));
        tableRow.addView(mViewPrev);

        mViewDate = new TextView(context);
        mViewDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mViewDate.setTypeface(mViewDate.getTypeface(), mTextStyle);
        mViewDate.setTextColor(mTodayColor);
        mViewDate.setGravity(Gravity.CENTER);
        mViewDate.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 6));
        updateText();
        tableRow.addView(mViewDate);

        mViewNext = new TextView(context);
        mViewNext.setText("▶"); // U+25B6 &#9654;
        mViewNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mViewNext.setTextColor(mTextColor);
        mViewNext.setGravity(Gravity.CENTER);
        mViewNext.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));
        tableRow.addView(mViewNext);

        addView(tableRow);

        mViewPrev.setOnClickListener(this);
        mViewDate.setOnClickListener(this);
        mViewNext.setOnClickListener(this);

        updateText();
    }

    @Override
    public void onClick(View view) {
        if (mSelectListener == null)
            return;

        int sign;

        if (view == mViewPrev)
            sign = SELECT_PREV;
        else if (view == mViewNext)
            sign = SELECT_NEXT;
        else
            sign = 0;

        Calendar date = ViewCalendar.getInstance();
        date.setTimeInMillis(mDate);

        if (sign == 0) {
            date.setTimeInMillis(System.currentTimeMillis());
        } else if (mMode == MODE_YEAR) {
            date.add(Calendar.YEAR, sign);
        } else if (mMode == MODE_MONTH) {
            date.add(Calendar.MONTH, sign);
        } else if (mMode == MODE_WEEK) {
            date.add(Calendar.DAY_OF_MONTH, sign * 7);
        } else {
            date.add(Calendar.DAY_OF_MONTH, sign);
        }

        long offset = date.getTimeInMillis() - mDate;

        setDate(mDate + offset);
        mSelectListener.OnSelect(mThis, offset, date.getTimeInMillis());
    }

    public interface OnSelectListener {
        void OnSelect(View view, long offset, long date);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        updateText();
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);
        updateText();
    }

    @Override
    public long getDateBegin() {
        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        if (mMode == MODE_YEAR) {
            calc.set(Calendar.DAY_OF_YEAR, 1);
        } else if (mMode == MODE_MONTH) {
            calc.set(Calendar.DAY_OF_MONTH, 1);
        } else if (mMode == MODE_WEEK) {
            while (calc.get(Calendar.DAY_OF_WEEK) != calc.getFirstDayOfWeek())
                calc.add(Calendar.DAY_OF_MONTH, -1);
        }

        calc.set(Calendar.HOUR_OF_DAY, 0);
        calc.set(Calendar.MINUTE, 0);
        calc.set(Calendar.SECOND, 0);
        calc.set(Calendar.MILLISECOND, 0);

        return calc.getTimeInMillis();
    }

    @Override
    public long getDateEnd() {
        Calendar calc = ViewCalendar.getInstance();

        if(mMode == MODE_YEAR) {
            calc.setTimeInMillis(getDateBegin());
            calc.add(Calendar.YEAR, 1);
            return calc.getTimeInMillis() - 1;
        } else if( mMode == MODE_MONTH) {
            calc.setTimeInMillis(getDateBegin());
            calc.add(Calendar.MONTH, 1);
            return calc.getTimeInMillis() - 1;
        } else if( mMode == MODE_WEEK) {
            return getDateBegin() + 604800000 - 1; // 7 days
        } else if( mMode == MODE_DAY) {
            return getDateBegin() + 86400000 - 1;
        }

        return getDateBegin();
    }

    private String makeDateString(int mode, long ms) {
        Date date = new Date(ms);

        String format = "yyyy/MM/dd";

        if (mode == MODE_YEAR)
            format = "yyyy";
        else if (mode == MODE_MONTH)
            format = "MMMM yyyy";
        else if (mode == MODE_DAY || mode == MODE_WEEK)
//            format = "MMM dd, yyyy";
            format = getWeekFormat();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        return simpleDateFormat.format(date);
    }

    private String getWeekFormat(){
        String format = "MMM dd";
        format += getContext().getString(R.string.calendar_day_word) + ", yyyy";

        return format;
    }

    public void updateText() {
        Calendar dateNow = ViewCalendar.getInstance();
        int yearNow = dateNow.get(Calendar.YEAR);
        int monthNow = dateNow.get(Calendar.MONTH);
        int dayNow = dateNow.get(Calendar.DAY_OF_MONTH);

        Calendar dateSet = ViewCalendar.getInstance();
        dateSet.setTimeInMillis(mDate);
        int yearSet = dateSet.get(Calendar.YEAR);
        int monthSet = dateSet.get(Calendar.MONTH);
        int daySet = dateSet.get(Calendar.DAY_OF_MONTH);

        //同一个月即显示todaycolor
        if (yearNow == yearSet && monthNow == monthSet /*&& dayNow == daySet*/)
            mViewDate.setTextColor(mTodayColor);
        else
            mViewDate.setTextColor(mExceedColor);

        mViewDate.setText(makeDateString(mMode, mDate));
    }
}
