package com.yy.worktime.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;


import com.yy.worktime.model.WatchEvent;

import java.util.Calendar;

/**
 */

public class ViewCalendarWeek extends ViewCalendar implements View.OnClickListener {

    private ViewCalendarCellWeekName[] mViewNameList;
    private ViewCalendarCellWeek[] mViewCellList;

    private OnSelectListener mSelectListener = null;
    private ViewCalendarWeek mThis = this;

    public ViewCalendarWeek(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarWeek(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TableRow tableRow;

        // Week Name
        tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1));

        mViewNameList = new ViewCalendarCellWeekName[7];
        for (int idx = 0; idx < mViewNameList.length; idx++) {
            mViewNameList[idx] = new ViewCalendarCellWeekName(context);

            mViewNameList[idx].setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mViewNameList[idx].setTypeface(mViewNameList[idx].getTypeface(), mTextStyle);
//            mViewNameList[idx].setTextColor(mTextColor);
            mViewNameList[idx].setTextColor(mWeekNameTextColor);
            mViewNameList[idx].setGravity(Gravity.CENTER);
            mViewNameList[idx].setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));

            tableRow.addView(mViewNameList[idx]);
        }
        addView(tableRow);

        // Cells
        tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1));

        mViewCellList = new ViewCalendarCellWeek[7];
        for (int idx = 0; idx < mViewCellList.length; idx++) {
            mViewCellList[idx] = new ViewCalendarCellWeek(context);
            mViewCellList[idx].setViewCalendar(this);
            mViewCellList[idx].setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mViewCellList[idx].setTypeface(mViewCellList[idx].getTypeface(), mTextStyle);
            mViewCellList[idx].setTextColor(mTextColor);
            mViewCellList[idx].setGravity(Gravity.CENTER);
            mViewCellList[idx].setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));

            tableRow.addView(mViewCellList[idx]);
        }
        addView(tableRow);

        updateNameList(mViewNameList);
        updateCellList(mViewCellList);
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);
        updateCellList(mViewCellList);
    }

    @Override
    public long getDateBegin() {
        return stripTime(mViewCellList[0].getDate());
    }

    @Override
    public long getDateEnd() {
        long date = stripTime(mViewCellList[6].getDate());
        return date + 86400000 - 1;
    }

    public void updateNameList(ViewCalendarCellWeekName[] list) {
        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        while (calc.get(Calendar.DAY_OF_WEEK) != calc.getFirstDayOfWeek())
            calc.add(Calendar.DAY_OF_MONTH, -1);

        for (int idx = 0; idx < list.length; idx++) {
            list[idx].setDate(calc.getTimeInMillis());
            calc.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void updateCellList(ViewCalendarCell[] list) {
        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        while (calc.get(Calendar.DAY_OF_WEEK) != calc.getFirstDayOfWeek())
            calc.add(Calendar.DAY_OF_MONTH, -1);

        for (int idx = 0; idx < list.length; idx++) {
            list[idx].setDate(calc.getTimeInMillis());
            calc.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public ViewCalendarCellWeek addEvent(WatchEvent event) {
        for (ViewCalendarCellWeek cell : mViewCellList) {
            if (cell.isSameDay(event.mStartDate)) {
                cell.addEvent(event);
                return cell;
            }
        }

        return null;
    }

    public void delEvent(WatchEvent event) {
        delEvent(event.mId);
    }

    public void delEvent(long id) {
        for (ViewCalendarCellWeek cell : mViewCellList)
            cell.delEvent(id);
    }

    public void delAllEvent() {
        for (ViewCalendarCellWeek cell : mViewCellList)
            cell.delAllEvent();
    }

    @Override
    public void onClick(View view) {
        if (mSelectListener == null)
            return;

        ViewCalendarCellWeek cell = (ViewCalendarCellWeek) view;
        setDate(cell.getDate());
        mSelectListener.onSelect(mThis, cell);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;

        for (ViewCalendarCellWeek cell : mViewCellList)
            if (cell.getText().length() != 0)
                cell.setOnClickListener(this);
    }

    public interface OnSelectListener {
        void onSelect(ViewCalendarWeek calendar, ViewCalendarCellWeek cell);
    }
}
