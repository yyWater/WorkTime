package com.yy.worktime.view.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;


import com.yy.worktime.R;
import com.yy.worktime.model.WatchEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 */

public class ViewCalendarDaily extends ViewCalendar {

    private final int heightBase = 1560;//26 * 60
    private final int hourViewCount = (26 + (int)Math.round(0.2*26)) * 2;//26 * 60

    private int mDesiredWidth;
    private int mDesiredHeight;

    private int mHourPadding;
    private int mHourMargin;

    private Paint mPaint;
    private Rect mRect;

    private OnSelectListener mSelectListener;

    public ViewCalendarDaily(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarDaily(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);

        mDesiredWidth = getResources().getDisplayMetrics().widthPixels;
//        mDesiredHeight = mTextSize * 48;
//        mDesiredHeight = mTextSize * 52;//modify 2017年11月21日17:30:26 only 时间线新增0AM,12PM
        mDesiredHeight = mTextSize * hourViewCount;//modify 2017年11月21日17:30:26 only 时间线新增0AM,12PM

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        makePaintHour();
        String patten = "12 AM";
        mHourPadding = (int) mPaint.measureText(patten, 0, patten.length());
        mHourMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        mRect = new Rect();
    }

    public ViewCalendarCellDaily addEvent(WatchEvent event) {

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        Calendar cale = Calendar.getInstance();

        cale.setTimeInMillis(event.mStartDate);
        layoutParams.start_hour = cale.get(Calendar.HOUR_OF_DAY) + 1;
        layoutParams.start_minute = cale.get(Calendar.MINUTE);

        long endDate = event.mEndDate;
        if (Math.abs(endDate - event.mStartDate) < 3600000) // 60*60*1000 = 60M, Make event height is at least 60 minutes in calendar
            endDate = event.mStartDate + 3600000;
        endDate = Math.min(endDate, getDateEnd());

        cale.setTimeInMillis(endDate);
        layoutParams.end_hour = cale.get(Calendar.HOUR_OF_DAY) + 1;
        layoutParams.end_minute = cale.get(Calendar.MINUTE);

        ViewCalendarCellDaily cell = new ViewCalendarCellDaily(getContext());
        cell.setViewCalendar(this);
        cell.setEvent(event);
        cell.setOnClickListener(mEventListener);
        cell.setBackgroundColor(WatchEvent.stringToColor(event.mColor));
        cell.setLayoutParams(layoutParams);

        addView(cell);

        return cell;
    }

    public void delEvent(WatchEvent event) {
        delEvent(event.mId);
    }

    public void delEvent(long id) {
        while (true) {
            int idx, count = getChildCount();
            for (idx = 0; idx < count; idx++) {
                ViewCalendarCellDaily cell = (ViewCalendarCellDaily) getChildAt(idx);
                if (cell.getEvent().mId == id)
                    break;
            }
            if (idx >= count)
                break;
            removeViewAt(idx);
        }
    }

    public void delAllEvent() {
        removeAllViews();
    }

    public interface OnSelectListener {
        void OnSelect(View view, WatchEvent event);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

    private View.OnClickListener mEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mSelectListener == null)
                return;

            ViewCalendarCellDaily cell = (ViewCalendarCellDaily) view;
            mSelectListener.OnSelect(cell, cell.getEvent());
        }
    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        buildLayoutMap();

        for (int idx = 0; idx < count; idx++) {
            View child = getChildAt(idx);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE)
                return;
            if (!checkLayoutParams(layoutParams))
                return;

            Rect rect = layoutParams.rect;

            child.measure(View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.AT_MOST));
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void buildLayoutMap() {
        List<List<LayoutParams>> layoutMap = new ArrayList<>();
        int count;

        /**
         * Assign children to a column, each child can't overlap in a column.
         * If child can't find a free column, create one.
         */
        count = getChildCount();
        for (int idx = 0; idx < count; idx++) {
            View child = getChildAt(idx);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE)
                continue;
            if (!checkLayoutParams(layoutParams))
                continue;

            boolean added = false;
            for (List<LayoutParams> list : layoutMap) {
                if (!layoutParams.overLapping(list)) {
                    added = list.add(layoutParams);
                    break;
                }
            }

            if (!added) {
                List<LayoutParams> list = new ArrayList<>();
                list.add(layoutParams);
                layoutMap.add(list);
            }
        }

        /**
         * According map, vertical stretch all children in the column.
         */
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd() - mHourPadding - mHourMargin;
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int column_count = layoutMap.size();
        int column_width = column_count == 0 ? width : (width / column_count);

        count = getChildCount();
        for (int idx = 0; idx < count; idx++) {
            View child = getChildAt(idx);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE)
                continue;
            if (!checkLayoutParams(layoutParams))
                continue;

            for (int idy = 0; idy < column_count; idy++) {
                List<LayoutParams> list = layoutMap.get(idy);
                if (!list.contains(layoutParams))
                    continue;

                layoutParams.rect.left = getPaddingStart() + mHourPadding + mHourMargin + (column_width * idy);
                layoutParams.rect.right = layoutParams.rect.left + column_width;
            }

            layoutParams.rect.top = layoutParams.getStartPos() * height / heightBase;
            layoutParams.rect.bottom = layoutParams.getEndPos() * height / heightBase;
        }

        /**
         * According map, horizontal stretch all children.
         */
        count = layoutMap.size();
        for (int idx = 0; idx < count; idx++) {
            List<LayoutParams> list = layoutMap.get(idx);

            for (LayoutParams layoutParams : list) {
                int idy = idx + 1;
                while (idy < count) {
                    if (layoutParams.overLapping(layoutMap.get(idy)))
                        break;
                    idy++;
                }

                layoutParams.rect.right = layoutParams.rect.left + (column_width * (idy - idx));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(mDesiredWidth, widthSize);
        } else {
            width = mDesiredWidth;
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(mDesiredHeight, heightSize);
        } else {
            height = mDesiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        for (int idx = 1; idx < 24; idx++) {
        for (int idx = 0; idx <= 24; idx++) {
            drawHour(canvas, idx);
            drawSeparator(canvas, idx);
        }

        Calendar cale = Calendar.getInstance();
        if (mTodayColor != 0 && isSameDay(cale.getTimeInMillis())) {
            drawNow(canvas, cale.get(Calendar.HOUR_OF_DAY));
        }
    }

    private void makePaintHour() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, mTextStyle));
        mPaint.setTextSize(mTextSize);
    }

    private void makePaintSeparator() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.STROKE);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(width);
    }

    private void makePaintNow() {
        mPaint.reset();

        mPaint.setAntiAlias(true);
        mPaint.setColor(mTodayColor);
        mPaint.setStyle(Paint.Style.STROKE);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(width);
    }

    private void drawHour(Canvas canvas, int hour) {
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        String text;
        if (hour <= 12)
            text = String.format(Locale.US, "%d AM", hour);
        else
            text = String.format(Locale.US, "%d PM", hour - 12);

        makePaintHour();
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        int x = mHourPadding - (int) mPaint.measureText(text, 0, text.length());
        int y = ((hour+1) * 60 * height / heightBase) + (mRect.height() / 2);

        canvas.drawText(text, x + getPaddingStart(), y + getPaddingTop(), mPaint);
    }

    private void drawSeparator(Canvas canvas, int hour) {
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        makePaintSeparator();

        int x = mHourPadding + mHourMargin;
        int y = (hour+1) * 60 * height / heightBase;

        canvas.drawLine(x, y, getMeasuredWidth(), y, mPaint);
    }

    private void drawNow(Canvas canvas, int hour) {
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        makePaintNow();

        int x = mHourPadding + mHourMargin;
        int y = (hour+1) * 60 * height / heightBase;

        canvas.drawLine(x, y, getMeasuredWidth(), y, mPaint);
    }

    @Override
    public long getDateBegin() {
        return stripTime(mDate);
    }

    @Override
    public long getDateEnd() {
        long date = stripTime(mDate);
        return date + 86400000 - 1;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public static class LayoutParams extends TableLayout.LayoutParams {

        public int gravity = Gravity.TOP | Gravity.START;
        public int start_hour = 0;
        public int start_minute = 0;
        public int end_hour = 0;
        public int end_minute = 0;
        public Rect rect = new Rect();

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);

            if (attrs != null) {
                TypedArray typedArray = context.obtainStyledAttributes(
                        attrs, R.styleable.ViewCalendarDaily);

                final int count = typedArray.getIndexCount();
                for (int idx = 0; idx < count; idx++) {
                    final int attr = typedArray.getIndex(idx);

                    if (attr == R.styleable.ViewCalendarDaily_android_layout_gravity) {
                        gravity = typedArray.getInteger(attr, gravity);
                    } else if (attr == R.styleable.ViewCalendarDaily_start_hour) {
                        start_hour = typedArray.getInteger(attr, start_hour);
                    } else if (attr == R.styleable.ViewCalendarDaily_start_minute) {
                        start_minute = typedArray.getInteger(attr, start_minute);
                    } else if (attr == R.styleable.ViewCalendarDaily_end_hour) {
                        end_hour = typedArray.getInteger(attr, end_hour);
                    } else if (attr == R.styleable.ViewCalendarDaily_end_minute) {
                        end_minute = typedArray.getInteger(attr, end_minute);
                    }
                }

                typedArray.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public boolean overLapping(LayoutParams other) {
            int start1 = this.start_hour * 60 + this.start_minute;
            int end1 = this.end_hour * 60 + this.end_minute;
            int start2 = other.start_hour * 60 + other.start_minute;
            int end2 = other.end_hour * 60 + other.end_minute;

            return !(end1 <= start2 || end2 <= start1);
        }

        public boolean overLapping(List<LayoutParams> list) {
            for (LayoutParams other : list) {
                if (overLapping(other))
                    return true;
            }
            return false;
        }

        public int getPos(int hour, int minute) {
            return hour * 60 + minute;
        }

        public int getStartPos() {
            return getPos(start_hour, start_minute);
        }

        public int getEndPos() {
            return getPos(end_hour, end_minute);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("{start_hour:").append(start_hour)
                    .append(" start_minute:").append(start_minute)
                    .append(" end_hour:").append(end_hour)
                    .append(" end_minute:").append(end_minute)
                    .append(" rect:").append(rect.toString())
                    .append("}").toString();
        }
    }
}
