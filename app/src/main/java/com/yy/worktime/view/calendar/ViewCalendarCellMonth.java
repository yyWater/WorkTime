package com.yy.worktime.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;


import com.yy.worktime.model.WatchEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */

public class ViewCalendarCellMonth extends ViewCalendarCell {

    private List<WatchEvent> mEventList;

    private Paint mPaint;
    private Rect mRect;

    public ViewCalendarCellMonth(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarCellMonth(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewCalendarCellMonth(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mEventList = new ArrayList<>();
        mPaint = new Paint();
        mRect = new Rect();
    }

    public void addEvent(WatchEvent event) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.US);
//        Log.d("xxx", "addEvent(" + simpleDateFormat.format(mDate) + "):" + simpleDateFormat.format(event.mStartDate));

        mEventList.add(event);
    }

    public void delEvent(WatchEvent event) {
        delEvent(event.mId);
    }

    public void delEvent(long id) {
        while (true) {
            int idx, count = mEventList.size();
            for (idx = 0; idx < count; idx++) {
                if (mEventList.get(idx).mId == id)
                    break;
            }
            if (idx >= count)
                break;
            mEventList.remove(idx);
        }
    }

    public void delAllEvent() {
        mEventList.clear();
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);

        ViewCalendar calendar = getViewCalendar();

        if (calendar != null) {
            if (calendar.isSameDay(mDate) && calendar.getFocusColor() != Color.TRANSPARENT)
                setTextColor(calendar.getFocusColor());
            else if (ViewCalendar.isToday(mDate))
                setTextColor(calendar.getTodayColor());
            else if (calendar.isSameMonth(mDate))
                setTextColor(calendar.getTextColor());
            else
                setTextColor(calendar.getExceedColor());
        }

        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        int day = calc.get(Calendar.DAY_OF_MONTH);
        setText(String.valueOf(day));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ViewCalendar calendar = getViewCalendar();
        /*if (calendar != null && calendar.getFocusBackgroundColor()!= Color.TRANSPARENT && calendar.isSameDay(mDate))
            drawFocus(canvas, calendar.getFocusBackgroundColor());*/

        if (calendar != null && calendar.getFocusBackgroundColor() != Color.TRANSPARENT){

            if(ViewCalendar.isToday(mDate)){//如果是今天
                drawFocus(canvas, calendar.getTodayBackgroundColor());
            }else if(calendar.isSameDay(mDate)){ //如果时选中的date
                drawFocus(canvas, calendar.getFocusBackgroundColor());
            }

        }

        if (mEventList.size() > 0){
//            drawEvent(canvas);
            ViewCalendarCellUtils.drawEvent(this,canvas,mPaint,mRect,mEventList);
        }


        super.onDraw(canvas);
    }

    private void drawFocus(Canvas canvas, int color) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float size = getTextSize() * 1.64f; // golden ratio.

        size = Math.min(size, width);
        size = Math.min(size, height);

        RectF rect = new RectF((width - size) / 2, (height - size) / 2, (width + size) / 2, (height + size) / 2);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(rect.centerX(), rect.centerY(), size / 2, mPaint);
    }

    private void drawEvent(Canvas canvas) {

        float size = getTextSize();
        int radius = (int) Math.floor(size * 0.6 / 2);
        int gap = (int) Math.floor(size * 0.15 / 2);

        getLayout().getLineBounds(0, mRect);

        // find the rect of center dot
        mRect.top = mRect.bottom;
        mRect.bottom = getMeasuredHeight();
        mRect.left = getMeasuredWidth() / 2 - radius - gap;
        mRect.right = getMeasuredWidth() / 2 + radius + gap;

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);

        if (mEventList.size() > 0) {
            mRect.offset(-mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(0).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }

        if (mEventList.size() > 1) {
            mRect.offset(mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(1).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }

        if (mEventList.size() > 2) {
            mRect.offset(mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(2).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }
    }
}
