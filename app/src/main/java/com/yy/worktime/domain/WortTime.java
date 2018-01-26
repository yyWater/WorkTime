package com.yy.worktime.domain;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;
import com.yy.base.utils.ObjectUtils;
import com.yy.worktime.db.ExtendTime;
import com.yy.worktime.db.ExtendTime_Table;

import java.util.Calendar;
import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.Method.sum;

/**
 * <br>author: wzg@xdja.com <br/>
 * date:   2018/1/22 18:41 <br/>
 */

public class WortTime {

    public final static long hours = 60 * 60;
    public final static long hours_3 = 3 * 60 * 60;
    public final static long hours_7 = 7 * 60 * 60;

    public static boolean isExtendTimeValid(String extendTime){

        return  !TextUtils.isEmpty(extendTime) && ObjectUtils.isValidInt(extendTime);
    }

    public void saveExtendTime(@NonNull ExtendTime extendTime){
        extendTime.save();
    }

    public static List<ExtendTime> getAllExtendTime(long startTime, long endTime){
        List<ExtendTime> extendTimes = SQLite.select().from(ExtendTime.class)
                .where(ExtendTime_Table.date.greaterThanOrEq(startTime))
                .and(ExtendTime_Table.date.lessThanOrEq(endTime))
                .queryList();

        return extendTimes;
    }

    public static long getTotalExtendTime(long startTime, long endTime){
        FlowCursor query = SQLite.select(sum(ExtendTime_Table.extendTimeMs)).from(ExtendTime.class)
                .where(ExtendTime_Table.date.greaterThan(startTime))
                .and(ExtendTime_Table.date.lessThan(endTime))
                .query();

        if(query != null && query.getColumnCount() > 0){
            query.moveToFirst();
            long total = query.getLong(0);
            query.close();

            return total;
        }else{
            return 0;
        }

    }

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
