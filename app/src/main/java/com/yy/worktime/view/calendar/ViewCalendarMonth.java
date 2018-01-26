package com.yy.worktime.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;


import com.yy.worktime.model.WatchEvent;

import java.util.Calendar;

/**
 */

public class ViewCalendarMonth extends ViewCalendar implements View.OnClickListener {

    private ViewCalendarCellWeekName[] mViewNameList;
    private ViewCalendarCellMonth[] mViewCellList;

    private OnSelectListener mSelectListener = null;
    private ViewCalendarMonth mThis = this;

    public ViewCalendarMonth(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarMonth(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TableRow tableRow;

        // Week Name
        tableRow = new TableRow(context);
        tableRow.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1));

        mViewNameList = new ViewCalendarCellWeekName[7];
        for (int idx = 0; idx < mViewNameList.length; idx++) {
            mViewNameList[idx] = new ViewCalendarCellWeekName(context);

            mViewNameList[idx].setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mViewNameList[idx].setTypeface(mViewNameList[idx].getTypeface(), mTextStyle);
            mViewNameList[idx].setTextColor(mWeekNameTextColor);
            mViewNameList[idx].setGravity(Gravity.CENTER);
            mViewNameList[idx].setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));

            tableRow.addView(mViewNameList[idx]);
        }
        addView(tableRow);

        // Cells
        mViewCellList = new ViewCalendarCellMonth[42];  // 6 weeks
        int cidx = 0;
        for (int widx = 0; widx < 6; widx++) {
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1));

            for (int idx = 0; idx < 7; idx++) {
                mViewCellList[cidx] = new ViewCalendarCellMonth(context);
                mViewCellList[cidx].setViewCalendar(this);
                mViewCellList[cidx].setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                mViewCellList[cidx].setTypeface(mViewCellList[idx].getTypeface(), mTextStyle);
                mViewCellList[cidx].setTextColor(mTextColor);
                mViewCellList[cidx].setGravity(Gravity.CENTER);
                mViewCellList[cidx].setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2));

                tableRow.addView(mViewCellList[cidx++]);
            }
            addView(tableRow);
        }

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
        long date = stripTime(mViewCellList[41].getDate());
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

    public void updateCellList(ViewCalendarCellMonth[] list) {
        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        calc.set(Calendar.DAY_OF_MONTH, 1);
        while (calc.get(Calendar.DAY_OF_WEEK) != calc.getFirstDayOfWeek())
            calc.add(Calendar.DAY_OF_MONTH, -1);

        for (int idx = 0; idx < list.length; idx++) {
            list[idx].setDate(calc.getTimeInMillis());
            calc.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public ViewCalendarCellMonth addEvent(WatchEvent event) {
        for (ViewCalendarCellMonth cell : mViewCellList) {
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
        for (ViewCalendarCellMonth cell : mViewCellList)
            cell.delEvent(id);
    }

    public void delAllEvent() {
        for (ViewCalendarCellMonth cell : mViewCellList)
            cell.delAllEvent();
    }

    @Override
    public void onClick(View view) {
        if (mSelectListener == null)
            return;

        ViewCalendarCellMonth cell = (ViewCalendarCellMonth) view;
        mSelectListener.onSelect(mThis, cell);
        setDate(cell.getDate());
    }

    public void setOnSelectListener(ViewCalendarMonth.OnSelectListener listener) {
        mSelectListener = listener;

        for (ViewCalendarCellMonth cell : mViewCellList)
            if (cell.getText().length() != 0)
                cell.setOnClickListener(this);
    }

    public interface OnSelectListener {
        void onSelect(ViewCalendarMonth calendar, ViewCalendarCellMonth cell);
    }

}
