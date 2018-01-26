package com.yy.woktime;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
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
