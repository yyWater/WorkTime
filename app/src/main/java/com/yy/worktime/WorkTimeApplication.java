package com.yy.worktime;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * 主工程实例
 * <br>author: wzg@xdja.com <br/>
 * date:   2018/1/22 10:49 <br/>
 */

public class WorkTimeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);
    }
}
