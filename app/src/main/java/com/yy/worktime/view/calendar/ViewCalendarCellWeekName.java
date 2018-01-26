package com.yy.worktime.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 */

public class ViewCalendarCellWeekName extends ViewCalendarCell {

    public ViewCalendarCellWeekName(Context context) {
        super(context);
        init(context, null);
    }

    public ViewCalendarCellWeekName(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewCalendarCellWeekName(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    static final String dayName[] = new String[]{"?", "S", "M", "T", "W", "T", "F", "S"};
    static final String dayName_ru[] = new String[]{"?", "В", "П", "В", "С", "Ч", "П", "С"};
    static final String dayName_es[] = new String[]{"?", "D", "L", "M", "X", "J", "V", "S"};
    static final String dayName_ja[] = new String[]{"?", "日", "月", "火", "水", "木", "金", "土"};

    /*static final String dayName[] = new String[]{"?", "M", "T", "W", "T", "F", "S", "S"};
    static final String dayName_ru[] = new String[]{"?", "П", "В", "С", "Ч", "П", "С", "В"};
    static final String dayName_es[] = new String[]{"?", "L", "M", "X", "J", "V", "S", "D"};*/

    @Override
    public void setDate(long date) {
        super.setDate(date);

        Calendar calc = ViewCalendar.getInstance();
        calc.setTimeInMillis(mDate);

        int weekDay = calc.get(Calendar.DAY_OF_WEEK);
        String lang = Locale.getDefault().getLanguage();

        Log.w("week","lang: " + lang);

        if (lang.equals("ru"))
            setText(dayName_ru[weekDay]);
        else if (lang.equals("es"))
            setText(dayName_es[weekDay]);
        else if (lang.equals("ja"))
            setText(dayName_ja[weekDay]);
        else
            setText(dayName[weekDay]);
    }
}
