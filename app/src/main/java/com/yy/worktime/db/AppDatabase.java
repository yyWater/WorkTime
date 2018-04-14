package com.yy.worktime.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * <br>author: wzg@xdja.com <br/>
 * date:   2018/1/22 11:03 <br/>
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "workTime";
    public static final int VERSION = 1;
}
