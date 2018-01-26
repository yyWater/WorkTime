package com.yy.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.yy.base.utils.LanguageSpUtils;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置语言要放到  所有控件相关操作之前
        LanguageSpUtils.changeLanguage(this);

        super.onCreate(savedInstanceState);

        ActivityController.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.getInstance().removeActivity(this);
    }
}
