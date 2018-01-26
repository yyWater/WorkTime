package com.yy.woktime.db;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * <br>author: wzg@xdja.com <br/>
 * date:   2018/1/22 11:13 <br/>
 */

@Table(database = AppDatabase.class)
public class ExtendTime extends BaseModel {
    @PrimaryKey(autoincrement = true)//主键 autoincrement 开启自增
    public int id;

    @Column
    public long date;//日期--毫秒值

    @Column
    public long extendTimeMs; //时长，单位毫秒

}
