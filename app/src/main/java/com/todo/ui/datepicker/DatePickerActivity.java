package com.todo.ui.datepicker;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.DateManageUtil;
import com.todo.utils.PickerUtil;
import com.todo.utils.SchedulesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by tianyang on 2017/2/15.
 */
public class DatePickerActivity extends BaseActivity implements DatePicker.DateChangeListener {
    private DatePicker picker;

    //month list
    private List<DateTime> monthList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    //有日程的时间
    private List<DateTime> dateTimeList = new ArrayList<>();

    private DaySheduleDialog.Builder builder;
    private DaySheduleDialog daySheduleDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_datepicker);
        initView();

        initDatas(picker.getmYear(), picker.getmMonth());

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MsgEvent event) {
        if (event.getMsg().equals("UpDate")) {
            initDatas(picker.getmYear(), picker.getmMonth());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("日历");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        picker = (DatePicker) findViewById(R.id.main_dp);
        picker.setDateChangeListener(this);
        picker.setDate(DateTime.now().getYear(), DateTime.now().getMonthOfYear());
        picker.setFestivalDisplay(true);
        picker.setTodayDisplay(true);
        picker.setHolidayDisplay(false);
        picker.setDeferredDisplay(false);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                if (DateManageUtil.isInDateTimeList(DateFormatUtil.parseDate(date), dateTimeList)) {
                    if (builder == null)
                        builder = new DaySheduleDialog.Builder(DatePickerActivity.this);
                    if (daySheduleDialog == null) {
                        daySheduleDialog = builder.setDayTime(DateFormatUtil.parseDate(date))
                                .create();
                    } else {
                        builder.setDayTime(DateFormatUtil.parseDate(date))
                                .upDateDatas();
                    }
                    builder.show();

                }
            }
        });

    }

    private void initDatas(int year, int month) {
        if (year != 0 && month != 0) {
            dateTimeList.clear();
            monthList = DateManageUtil.getMonthDays(year, month);
            List<Schedule> list = DataSupport.findAll(Schedule.class);
            for (Schedule schedule : list) {
                switch (schedule.getType()) {
                    case 0:
                        if (SchedulesUtil.isInMonth(monthList, schedule)) {
                            DateTime dateTime = DateFormatUtil.parse(schedule.getStartTime());
                            dateTimeList.add(dateTime);
                        }
                        break;
                    case 1:
                        dateTimeList.addAll(SchedulesUtil.getDaysInMonth(monthList, schedule, 1));
                        break;
                    case 2:
                        dateTimeList.addAll(SchedulesUtil.getDaysInMonth(monthList, schedule, 2));
                        break;
                    case 3:
                        dateTimeList.addAll(SchedulesUtil.getDaysInMonth(monthList, schedule, 3));
                        break;
                    default:
                }
            }
            HashSet<DateTime> timeHashSet = new HashSet<>(dateTimeList);
            PickerUtil.setDPDecor(timeHashSet, picker);
        }
    }


    @Override
    public void DateChange(int year, int month) {
        initDatas(year, month);
    }


    public void dateSet(View view) {
        DatePickerDialog mDialog = new DatePickerDialog(this, R.style.PickerDialogCustom, mdateListener, DateTime.now().getYear(),
                DateTime.now().getMonthOfYear() - 1, DateTime.now().getDayOfMonth());
        mDialog.setTitle("");
        mDialog.show();
        android.widget.DatePicker dp = mDialog.getDatePicker();
//        dp.setSpinnersShown(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            dp.setLayoutMode(1);
//        }

        if (dp.getChildAt(0) != null
                && ((ViewGroup) dp.getChildAt(0)).getChildAt(0) != null
                && ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2) != null)
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }


    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            picker.setDate(year, monthOfYear + 1);
            initDatas(year, monthOfYear + 1);
        }

    };

}
