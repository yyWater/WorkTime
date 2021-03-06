package com.yy.worktime.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yy.base.utils.ColorUtils;
import com.yy.base.utils.ObjectUtils;
import com.yy.base.utils.TimeUtils;
import com.yy.base.utils.ToastCommon;
import com.yy.worktime.R;
import com.yy.worktime.db.ExtendTime;
import com.yy.worktime.domain.CalendarManager;
import com.yy.worktime.domain.WortTime;
import com.yy.worktime.model.WatchEvent;
import com.yy.worktime.view.calendar.ViewCalendarCellMonth;
import com.yy.worktime.view.calendar.ViewCalendarMonth;
import com.yy.worktime.view.calendar.ViewCalendarSelector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    @BindView(R.id.btn_today_extend_time)
    protected Button btn_today_extend_time;

    @BindView(R.id.tv_extend_time_label)
    protected TextView tv_extend_time_label;

    @BindView(R.id.ed_extend_time)
    protected EditText editText_extend_time;

    @BindView(R.id.btn_commit_extend_time)
    protected Button btn_commit_extend_time;

    private long mDefaultDate = System.currentTimeMillis();
    private long selectDate;
    private long currentUserId;
    private long currentKidsId;

//    private HashMap<Long, Long> dateExtendTimeMap = new HashMap<>(31);
    private LongSparseArray<Float> dateExtendTimeMap = new LongSparseArray<>(31);
    private float monthTotalHours;


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

        initView();

        initTitleBar();

        selectDate = CalendarManager.formatDateTo9Am(mDefaultDate);

        return mViewMain;
    }

    private void initView() {
        mViewSelector.setOnSelectListener(mSelectorListener);

        mViewCalendar.setOnSelectListener(mCalendarListener);

        btn_commit_extend_time.setOnClickListener(mOnCommitBtnClickedListener);

        editText_extend_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    String string = editText_extend_time.getText().toString();
                        editText_extend_time.setSelection(string.length());
                }
            }
        });

        tv_extend_time_label.setText(getString(R.string.extent_time_label,TimeUtils.formatTimeMonthDay(mDefaultDate)));
    }

    private void initTitleBar() {
        tv_title.setTextColor(getResources().getColor(R.color.color_orange_main));
        tv_title.setText(R.string.title_extend_time);
       /* view_left_action.setImageResource(R.drawable.icon_left);

        view_right_action.setImageResource(R.drawable.icon_add);
        view_right_action.setTag(R.drawable.icon_add);*/

    }


    @OnClick(R.id.main_toolbar_action1)
    public void onToolbarAction1() {

        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.main_toolbar_action2)
    public void onToolbarAction2() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null){
            mDefaultDate = getArguments().getLong(BUNDLE_KEY_DATE);
        }
        mViewSelector.setDate(mDefaultDate);
        mViewCalendar.setDate(mDefaultDate);
        monthTotalHours = 0;

        loadExtendTimeList(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());


    }

    private void loadExtendTimeList(long start, long end) {
//        Log.w("calendar", "start: " + TimeUtils.formatTimeYearMonthDay(start));
//        Log.w("calendar", "end  : " + TimeUtils.formatTimeYearMonthDay(end));

        mViewCalendar.delAllEvent();
        dateExtendTimeMap.clear();
        monthTotalHours = 0;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(end);

        List<ExtendTime> allExtendTime = WortTime.getAllExtendTime(start, end);
        if(!ObjectUtils.isListEmpty(allExtendTime)){
            List<WatchEvent> mEventList = new ArrayList<>(allExtendTime.size());

            for (ExtendTime extendTime :
                    allExtendTime) {

                float extendTimeHour = (float) extendTime.extendTimeMs / WortTime.hours;
                Log.w("calendar", "extendTimeHour  : " + extendTimeHour);

                //如果是同一个月才加入统计
                if(TimeUtils.isSameMonth(selectDate, extendTime.date)){

                    Log.w("calendar", "extendTime.date  : " + TimeUtils.formatTimeYearMonthDay(extendTime.date));
                    Log.w("calendar", "selectDate  : " + TimeUtils.formatTimeYearMonthDay(selectDate));
                    monthTotalHours += extendTimeHour;
                }

                dateExtendTimeMap.put(extendTime.date,extendTimeHour);

                //设置event
                WatchEvent watchEvent = new WatchEvent();
                watchEvent.mStartDate = extendTime.date;
                watchEvent.mEndDate = extendTime.date;
                watchEvent.mAlertTimeStamp = extendTime.date;
                watchEvent.mColor = ColorUtils.colorToString(WatchEvent.ColorList[3]);
                mEventList.add(watchEvent);
            }

            for (WatchEvent event : mEventList) {
                mViewCalendar.addEvent(event);
            }
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mViewSelector.getDate());

        tv_month_total_extend_time.setText(
                getString(R.string.month_total_extend_time_2,
                        calendar.get(Calendar.MONTH)+1,
                        WortTime.decimalFormat.format(monthTotalHours)));
/*
        tv_month_total_extend_time.setText(
                getString(R.string.month_total_extend_time,
                        calendar.get(Calendar.MONTH)+1,
                        monthTotalHours));
*/

    }

    private ViewCalendarSelector.OnSelectListener mSelectorListener = new ViewCalendarSelector.OnSelectListener() {
        @Override
        public void OnSelect(View view, long offset, long date) {
            Log.w("calendar", "OnSelect date: " + TimeUtils.formatTimeYearMonthDay(date));

            mViewCalendar.setDate(date);
            updateUIByMonthChange(date);
        }
    };

    private void updateUIByMonthChange(long date) {
        selectDate = CalendarManager.formatDateTo9Am(date);

        loadExtendTimeList(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());

        //选中date的时长
        showSelectDateExtendTime(CalendarManager.formatDateTo9Am(date));
        mDefaultDate = date;
    }

    //日历中点击具体某天的响应
    private ViewCalendarMonth.OnSelectListener mCalendarListener = new ViewCalendarMonth.OnSelectListener() {
        @Override
        public void onSelect(ViewCalendarMonth calendar, ViewCalendarCellMonth cell) {

//            tv_select_date.setText(TimeUtils.formatTimeYearMonthDay(cell.getDate()));
            tv_extend_time_label.setText(
                    getString(R.string.extent_time_label,
                            TimeUtils.formatTimeMonthDay(cell.getDate())));


            selectDate = CalendarManager.formatDateTo9Am(cell.getDate());
            showSelectDateExtendTime(selectDate);

            //如果非一个月
            handleMonthChange();

            //是否已经加班，如果提交过，则按钮显示update；如果未加，显示commit
            //周一到周五，默认是3H，周六，周日默认是7H

        }
    };

    private void handleMonthChange(){

        Log.w("calendar", "selectDate: " +
                TimeUtils.formatTimeYearMonthDay(mViewCalendar.getDateBegin()));

        if (!TimeUtils.isSameMonth(mViewSelector.getDate(), selectDate)) {
            mViewSelector.setDate(selectDate);

            //这里需要拿到即将显示的monthview的起始终止 date
            /*Log.w("calendar", "mViewCalendar.getDateBegin(): " +
                    TimeUtils.formatTimeYearMonthDay(mViewCalendar.getDateBegin()));
            Log.w("calendar", "mViewCalendar.getDateEnd(): " +
                    TimeUtils.formatTimeYearMonthDay(mViewCalendar.getDateEnd()));*/

            updateUIByMonthChange(selectDate);

        }

        /*int compareRes = TimeUtils.compareMonthInNextPrev(mViewSelector.getDate(), selectDate);
        if (compareRes > 0) {
            mViewSelector.prevMonth();
        }else if(compareRes < 0){
            mViewSelector.nextMonth();
        }*/
    }

    private void showSelectDateExtendTime(long selectDate) {
        Float extend = dateExtendTimeMap.get(selectDate);
        if(extend == null){
           editText_extend_time.setText("");
        }else {
            editText_extend_time.setText(String.valueOf(extend));
        }

        Editable text = editText_extend_time.getText();
        editText_extend_time.setSelection(text.length());
    }

    private Button.OnClickListener mOnCommitBtnClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
//            selectFragment(DashboardProgressFragment.class.getName(), null,true);

            String extendTimeHours = editText_extend_time.getText().toString().trim();

            if(!WortTime.isExtendTimeValid(extendTimeHours)){
                ToastCommon.makeText(getContext(),R.string.extend_time_invalid);
            }else {

//                Integer extendTimeHoursInt = Integer.valueOf(extendTimeHours);
                Float extendTimeHoursInt = Float.valueOf(extendTimeHours);


                ExtendTime extendTime = new ExtendTime();
                extendTime.date = selectDate;

                Log.w("WorkTime", "extendTime: " + extendTimeHoursInt * WortTime.hours);
                extendTime.extendTimeMs =  Math.round(extendTimeHoursInt * WortTime.hours);

                boolean operRes = false;
                //如果小于零，则认为是删除
                if(extendTimeHoursInt <= 0){
                    operRes = extendTime.delete();
                }else {
                    operRes = extendTime.save();
                }

                if(operRes){
                    ToastCommon.makeText(getContext(),R.string.extend_time_save_ok);
                    updateTotalTime();

                    hideInput();
                }else {
                    ToastCommon.makeText(getContext(),R.string.extend_time_save_fail);
                }
            }

        }
    };

    private void updateTotalTime() {
        /*monthTotalHours = WortTime.getTotalExtendTime(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());
        monthTotalHours = monthTotalHours/WortTime.hours;
        tv_month_total_extend_time.setText(getString(R.string.month_total_extend_time, monthTotalHours));*/

        loadExtendTimeList(mViewCalendar.getDateBegin(), mViewCalendar.getDateEnd());
        mViewCalendar.setDate(selectDate);
    }


}
