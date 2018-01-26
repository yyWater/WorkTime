package com.yy.worktime.domain;


import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yy.woktime.db.ExtendTime;
import com.yy.woktime.db.ExtendTime_Table;

import java.util.Calendar;
import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.Method.sum;

/**
 * <br>author: wzg@xdja.com <br/>
 * date:   2018/1/22 18:41 <br/>
 */

public class WortTime {

    public final static long hours_3 = 3 * 60 * 60;
    public final static long hours_7 = 7 * 60 * 60;

    public void testDBOper(){
        ExtendTime extendTime = new ExtendTime();

        extendTime.date = Calendar.getInstance().getTimeInMillis();
        extendTime.extendTimeMs = hours_3;

        extendTime.save();


        //query
        long startTime = 0;
        long endTime = 0;


        List<ExtendTime> extendTimes = new Select().from(ExtendTime.class)
                .where(ExtendTime_Table.date.greaterThan(startTime))
                .and(ExtendTime_Table.date.lessThan(endTime))
                .queryList();

        //get total time in some interval
        long totalExtendTime = SQLite.select(sum(ExtendTime_Table.extendTimeMs)).from(ExtendTime.class)
                .where(ExtendTime_Table.date.greaterThan(startTime))
                .and(ExtendTime_Table.date.lessThan(endTime))
                .query().getLongOrDefault(0, -1);



    }
}
