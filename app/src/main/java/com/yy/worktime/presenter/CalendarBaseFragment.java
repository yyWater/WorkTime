package com.yy.worktime.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.worktime.R;
import com.yy.worktime.base.BaseFragment;

import butterknife.BindView;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Administrator on 2017/11/1.
 */

public class CalendarBaseFragment extends BaseFragment {
    public final static String signal_show_sync_layout_new = "sync_note_new";


    @BindView(R.id.main_toolbar_title)
    protected TextView tv_title;
    @BindView(R.id.main_toolbar_action1)
    protected ImageView view_left_action;
    @BindView(R.id.main_toolbar_action2)
    protected ImageView view_right_action;

    protected MainFrameActivity mainFrameActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFrameActivity = (MainFrameActivity) getActivity();
    }

  /*  public void selectFragment(String className, Bundle args, boolean isAddToBackStack) {
        Fragment fragment = Fragment.instantiate(getContext(), className, args);

        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction()
                .replace(R.id.calender_fragment_container, fragment, className);

        if(isAddToBackStack){
            fragmentTransaction
                    .addToBackStack(null);
        }

        fragmentTransaction .commit();

    }*/
}
