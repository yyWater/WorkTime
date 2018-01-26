package com.yy.worktime.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yy.base.BaseFragmentActivity;
import com.yy.worktime.R;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;


/**
 * 主界面
 *
 * @data 2017年4月15日19:33:10
 */
public class MainFrameActivity extends BaseFragmentActivity {

    protected FrameLayout view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frame_main);

        /*initValue();

        initView();
*/
        //初始化fragment对象
        initFragments();

    }

    private void initFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .add(R.id.fragment_container, new CalendarMonthFragment(), "");
        fragmentTransaction.commit();
    }

}
