package com.yy.worktime.view.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.yy.base.utils.ObjectUtils;
import com.yy.worktime.domain.CalendarManager;

import java.util.List;

/**
 * date:   2017/10/24 10:44 <br/>
 */

public class MultiEventDecorator implements DayViewDecorator {

    private int year;
    private int month;

    public MultiEventDecorator(int year, int month){
        this.year = year;
        this.month = month;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return (day.getYear() == year && (day.getMonth()+1) == month);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AnnulusSpan(year,month));
    }

    public class AnnulusSpan implements LineBackgroundSpan {
        float radius = 10;
        int mergeSize = 12;

        int year;
        int month;

        public AnnulusSpan(int year, int month){
            this.year = year;
            this.month = month;
        }

        @Override
        public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline,
                                   int bottom, CharSequence charSequence, int start, int end, int lnum) {

            int oldColor = paint.getColor();

            //查询当天的前四个event，主要获取其color
            CalendarDay calendarDay =
                    CalendarDay.from(year,month-1, Integer.valueOf(charSequence.toString()));
            List<String> dayFirst4EventColors = CalendarManager.getDayFirst4EventColors(calendarDay);


            //根据获取到的color list 绘制dot
            if(!ObjectUtils.isListEmpty(dayFirst4EventColors)){
                int size = dayFirst4EventColors.size();

                int mergeLeftRight = right - left;

                int step = mergeLeftRight/(size + 1);

                for (int i = 0; i < size; i++) {
                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(i)));

                    //计算merge
                    int merge = getMerge(size, step, i);

                    canvas.drawCircle(left + merge, bottom+radius,radius, paint);
                }

                /*if(size == 1){
                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(0)));
                    canvas.drawCircle((left+right)/2, bottom+radius,radius, paint);
                }else if(size == 2){
                    int step = (right - left)/3;

                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(0)));
                    canvas.drawCircle(left + step, bottom+radius,radius, paint);

                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(1)));
                    canvas.drawCircle(left + step * 2, bottom+radius,radius, paint);
                }else if (size == 3){
                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(0)));
                    canvas.drawCircle((left+right)/2 - mergeSize, bottom+radius,radius, paint);

                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(0)));
                    canvas.drawCircle((left+right)/2, bottom+radius,radius, paint);

                    paint.setColor(Color.parseColor(dayFirst4EventColors.get(1)));
                    canvas.drawCircle((left+right)/2 + mergeSize, bottom+radius,radius, paint);
                }else if(size == 4){

                }*/
            }


            /*if(color != 0){
                paint.setColor(color);
            }

            Log.w("TestCalendar","(left+right) " + (left+right));
            Log.w("TestCalendar","charSequence " + charSequence);

            canvas.drawCircle((left+right)/2 - offSize, bottom+radius,radius, paint);*/
            paint.setColor(oldColor);

        }

        private int getMerge(int dotSize, int step, int index) {
            int merge = step * (index + 1);

            //当有两个点时，两个点间的距离适当近些
            if(dotSize == 2){
                if(index == 0){
                    merge += mergeSize;
                }

                if (index == 1){
                    merge -= mergeSize;
                }
            }
            return merge;
        }

        private void drawMultiEventDot(int year, int month, String day){
            //查询当天的前四个event，主要获取其color


        }
    }
}
