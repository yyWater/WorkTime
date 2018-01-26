package com.yy.worktime.view.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


import com.yy.worktime.model.WatchEvent;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class ViewCalendarCellUtils {
    public static void drawEvent(ViewCalendarCell viewCalendarCell, Canvas canvas,
                          Paint paint, Rect rect, List<WatchEvent> mEventList) {

        float size = viewCalendarCell.getTextSize();
        int radius = (int) Math.floor(size * 0.3 / 2);
        int gap = (int) Math.floor(size * 0.5 / 2);

        int baseLine = viewCalendarCell.getLayout().getLineBounds(0, rect);

        // find the rect of center dot
        /*mRect.top = mRect.bottom;
        mRect.bottom = getMeasuredHeight();
        mRect.left = getMeasuredWidth() / 2 - radius - gap;
        mRect.right = getMeasuredWidth() / 2 + radius + gap;*/

        viewCalendarCell.getMeasuredHeight();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);

        //一个点时，画中间；二个点时，还中间点两边；三个点时，
        int eventSize = mEventList.size();
        int left = rect.left;

        int mergeLeftRight = rect.right - rect.left;

        int step = mergeLeftRight/(eventSize + 1);

        for (int i = 0; i < eventSize; i++) {
            paint.setColor(WatchEvent.stringToColor(mEventList.get(i).mColor));

            //计算merge
            int merge = getMerge(eventSize, step, i);

//            canvas.drawCircle(left + merge, rect.bottom + size/2 + radius + gap,radius, paint);
            canvas.drawCircle(left + merge, rect.bottom + radius + gap,radius, paint);
        }


        /*if(size1 == 1){
            mRect.offset(0, 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(0).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

        }
        if(size1 == 2){
            int offsize = mRect.width();
            mRect.offset(-offsize + offsize/3, 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(0).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

            mRect.offset(0, 0);
            mRect.offset(offsize + offsize/3, 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(1).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

        }

        if(size1 == 3){
            int offsize = mRect.width();
            mRect.offset(-offsize, 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(0).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

            mRect.offset(0, 0);
            mRect.offset(-offsize + offsize/3, 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(1).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

            mRect.offset(0, 0);
            mRect.offset(offsize , 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(2).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

        }*/

        /*if (mEventList.size() > 0) {
            mRect.offset(-mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(0).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }*/

        /*if (mEventList.size() > 1) {
            mRect.offset(mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(1).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }

        if (mEventList.size() > 2) {
            mRect.offset(mRect.width(), 0);
            mPaint.setColor(WatchEvent.stringToColor(mEventList.get(2).mColor));
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);
        }*/
    }

    private static int getMerge(int dotSize, int step, int index) {
        int merge = step * (index + 1);

        //当有两个点时，两个点间的距离适当近些
        if (dotSize == 2) {
            if (index == 0) {
                merge += step / 4;
            }

            if (index == 1) {
                merge -= step / 4;
            }
        }
        return merge;
    }
}
