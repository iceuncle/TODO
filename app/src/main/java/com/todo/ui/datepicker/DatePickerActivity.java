package com.todo.ui.datepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.DateManageUtil;
import com.todo.utils.LogUtil;
import com.todo.utils.PickerUtil;
import com.todo.utils.SchedulesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
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
        if (event.getMsg().equals("MonthViewUpDate")) {
            initDatas(picker.getmYear(), picker.getmMonth());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initView() {
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
                if (DateManageUtil.isInDateTimeList(DateFormatUtil.parseDate(date), dateTimeList))
                    new DaySheduleDialog.Builder(DatePickerActivity.this)
                            .setDayTime(DateFormatUtil.parseDate(date))
                            .setDatePicker(picker)
                            .show();
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
}
