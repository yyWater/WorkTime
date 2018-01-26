package com.yy.worktime.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.yy.worktime.model.WatchEvent;


/**
 */

public class ViewCalendarCellDaily extends ViewCalendarCell {
    private WatchEvent mEvent;

    private Rect mRect;
    private Paint mPaint;
    final private int mColor = Color.WHITE;

    public ViewCalendarCellDaily(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarCellDaily(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewCalendarCellDaily(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRect = new Rect();
        mPaint = new Paint();

        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics());

        setPadding(padding, 0, padding, 0);
        setSingleLine();
    }

    public void setEvent(WatchEvent event) {
        mEvent = event;
        setText(event.mName);
    }

    public WatchEvent getEvent() {
        return mEvent;
    }

    @Override
    public void setViewCalendar(ViewCalendar calendar) {
        mViewCalendar = calendar;

        setTextSize(TypedValue.COMPLEX_UNIT_PX, mViewCalendar.getTextSize() + 1);
        setTypeface(getTypeface(), Typeface.BOLD);
        setTextColor(mColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        boolean isEllipsize = !((getLayout().getText().toString()).equals(mEvent.mDescription));
        if (getText().length() == 0)
            return;

        getPaint().getTextBounds(getText().toString(), 0, getText().length(), mRect);

        Paint.FontMetrics fm = getPaint().getFontMetrics();
        float height = fm.descent - fm.ascent;

        if (height > getMeasuredHeight()) {
            drawEllipsize(canvas, getMeasuredHeight() / 2);
        } else if (mRect.width() > getMeasuredWidth()) {
            super.onDraw(canvas);
            drawEllipsize(canvas, mRect.height() + getMeasuredHeight() / 2);
        } else {
            super.onDraw(canvas);
        }
    }

    private void drawEllipsize(Canvas canvas, int y) {
        int gap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics());
        int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics());
        int centerY = y;
        int centerX = getPaddingStart() + radius;

        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(centerX, centerY, radius, mPaint);

        centerX += gap + radius * 2;
        canvas.drawCircle(centerX, centerY, radius, mPaint);

        centerX += gap + radius * 2;
        canvas.drawCircle(centerX, centerY, radius, mPaint);
    }
}
