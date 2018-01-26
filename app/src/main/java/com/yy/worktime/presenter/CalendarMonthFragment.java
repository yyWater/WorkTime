package com.yy.worktime.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.yy.base.utils.TimeUtils;
import com.yy.worktime.R;
import com.yy.worktime.domain.CalendarManager;
import com.yy.worktime.view.calendar.ViewCalendarCellMonth;
import com.yy.worktime.view.calendar.ViewCalendarMonth;
import com.yy.worktime.view.calendar.ViewCalendarSelector;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CalendarMonthFragment
 */

public class CalendarMonthFragment extends CalendarBaseFragment {
    private View mViewMain;

    @BindView(R.id.calendar_month_selector)
    protected ViewCalendarSelector mViewSelector;
    @BindView(R.id.calendar_month_calendar)
    protected ViewCalendarMonth mViewCalendar;

    @BindView(R.id.tv_month_total_extend_time)
    protected TextView tv_month_total_extend_time;

    @BindView(R.id.tv_date_str)
    protected TextView tv_select_date;

    @BindView(R.id.ed_extend_time)
    protected EditText editText_extend_time;

    @BindView(R.id.btn_commit_extend_time)
    protected Button btn_commit_extend_time;

    private long mDefaultDate = System.currentTimeMillis();
    private long currentUserId;
    private long currentKidsId;

//    private HashMap<Long, Long> dateExtendTimeMap = new HashMap<>(31);
    private LongSparseArray<Long> dateExtendTimeMap = new LongSparseArray<>(31);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewMain = inflater.inflate(R.layout.fragment_calendar_month, container, false);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mViewMain.getWindowToken(), 0);

        ButterKnife.bind(this,mViewMain);

        mViewSelector.setOnSelectListener(mSelectorListener);

        mViewCalendar.setOnSelectListener(mCalendarListener);

        btn_commit_extend_time.setOnClickListener(mOnCommitBtnClickedListener);
//        showSyncDialog();

//        ViewUtils.setBtnTypeFace(getContext(),mSyncButton);

        initTitleBar();

        return mViewMain;
    }

    private void initTitleBar() {
        /*tv_title.setTextColor(getResources().getColor(R.color.colorAccent));
        tv_title.setText(R.string.title_calendar);
        view_left_action.setImageResource(R.drawable.icon_left);

        view_right_action.setImageResource(R.drawable.icon_add);
        view_right_action.setTag(R.drawable.icon_add);*/

    }


    @OnClick(R.id.main_toolbar_action1)
    public void onToolbarAction1() {
        /*Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_KEY_DATE, mViewCalendar.getDate());

        mActivityMain.selectFragment(FragmentCalendarMain.class.getName(), bundle);*/

        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.main_toolbar_action2)
    public void onToolbarAction2() {
//        WatchEvent event = new WatchEvent(mViewCalendar.getDate());

        /*WatchEvent event = EventManager.getWatchEventForAdd(mViewCalendar.getDate());
        event.mUserId = currentUserId;

        mainFrameActivity.mEventStack.push(event);
        selectFragment(CalendarAddEventFragment.class.getName(), null,true);*/
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null){
            mDefaultDate = getArguments().getLong(BUNDLE_KEY_DATE);
        }
        mViewSelector.setDate(mDefaultDate);
        mViewCalendar.setDate(mDefaultDate);

        //从cache中读取当前userid
        /*currentUserId = LoginManager.getCurrentLoginUserId(getContext());
        currentKidsId = DeviceManager.getFocusKidsId();*/

        loadEventList(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());
    }

    private void loadEventList(long start, long end) {
        mViewCalendar.delAllEvent();
        dateExtendTimeMap.clear();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(end);
        Log.d("CalendarMonthFragment", "start:" + startCalendar.getTime().toString());
        Log.d("CalendarMonthFragment", "end:  " + endCalendar.getTime().toString());

        //2017/11/8
//        List<WatchEvent> list = CalendarManager.getEventList(start, end);
        /*List<WatchEvent> mEventList = EventManager.getEventList(currentUserId,currentKidsId, start, end);

        for (WatchEvent event : mEventList) {
*//*
            Calendar cale = Calendar.getInstance();
            cale.setTimeInMillis(event.mStartDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
            String s = sdf.format(cale.getTimeInMillis());
            cale.setTimeInMillis(event.mEndDate);
            String e = sdf.format(cale.getTimeInMillis());
            Log.d("xxx", "Event:" + event.mName + "," + s + "--" + e );
*//*
            mViewCalendar.addEvent(event);
        }*/
    }

    private ViewCalendarSelector.OnSelectListener mSelectorListener = new ViewCalendarSelector.OnSelectListener() {
        @Override
        public void OnSelect(View view, long offset, long date) {
            mViewCalendar.setDate(date);
            loadEventList(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());
        }
    };

    //日历中点击具体某天的响应
    private ViewCalendarMonth.OnSelectListener mCalendarListener = new ViewCalendarMonth.OnSelectListener() {
        @Override
        public void onSelect(ViewCalendarMonth calendar, ViewCalendarCellMonth cell) {

            tv_select_date.setText(TimeUtils.formatTimeYearMonthDay(cell.getDate()));


            long formatDateTo9Am = CalendarManager.formatDateTo9Am(cell.getDate());
            Long extend = dateExtendTimeMap.get(formatDateTo9Am);
            if(extend == null){
               editText_extend_time.setText("0");
            }else {
                editText_extend_time.setText(String.valueOf(extend));
            }

            //是否已经加班，如果提交过，则按钮显示update；如果未加，显示commit
            //周一到周五，默认是3H，周六，周日默认是7H





//            selectFragment(CalendarDailyFragment.class.getName(), bundle,true);
        }
    };

    private Button.OnClickListener mOnCommitBtnClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
//            selectFragment(DashboardProgressFragment.class.getName(), null,true);

        }
    };

}
