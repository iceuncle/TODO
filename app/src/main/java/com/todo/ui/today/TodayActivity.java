package com.todo.ui.today;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.databinding.ActivityTodayBinding;
import com.todo.databinding.ItemListTodayBinding;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.crud.AddActivity;
import com.todo.ui.datepicker.DatePickerActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.main.MainActivity;
import com.todo.ui.set.RingSettingActivity;
import com.todo.ui.thisweek.ThisWeekActivity;
import com.todo.utils.LogUtil;
import com.todo.utils.SchedulesUtil;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.BindingViewHolder;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Created by tianyang on 2017/4/24.
 */

public class TodayActivity extends BaseActivity {
    private ActivityTodayBinding mBinding;
    private SingleTypeAdapter<Schedule> mAdapter;
    private List<Schedule> mScheduleList = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MsgEvent event) {
        if (event.getMsg().equals("UpDate")) {
            LogUtil.d("aaa", "UpDate");
            initDatas();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.today_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rili:
                Intent intent = new Intent(TodayActivity.this, DatePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.music:
                startActivity(new Intent(TodayActivity.this, RingSettingActivity.class));
                break;
            case R.id.recent:
                startActivity(new Intent(TodayActivity.this, MainActivity.class));
                break;
            case R.id.thisWeek:
                startActivity(new Intent(TodayActivity.this, ThisWeekActivity.class));
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_today);

        initView();
        initDatas();
        initEventHandler();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("今日行程");
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

        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SingleTypeAdapter<Schedule>(this, R.layout.item_list_today);
        mAdapter.setPresenter(new ItemPresenter());
        mAdapter.setDecorator(new ItemDecorator());
        mBinding.recyclerview.setAdapter(mAdapter);


//        mBinding.swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);

    }

    private void initEventHandler() {
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();
            }
        });

        final List<Schedule> schedules = new ArrayList<>();
        mBinding.fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_work:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("工作")) {
                                schedules.add(schedule);
                            }
                        }
                        mAdapter.set(schedules);
                        break;
                    case R.id.action_life:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("生活")) {
                                schedules.add(schedule);
                            }
                        }
                        mAdapter.set(schedules);
                        break;
                    case R.id.action_study:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("学习")) {
                                schedules.add(schedule);
                            }
                        }
                        mAdapter.set(schedules);
                        break;
                    case R.id.action_other:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("其它")) {
                                schedules.add(schedule);
                            }
                        }
                        mAdapter.set(schedules);
                        break;
                    case R.id.action_add:
                        Intent intent = new Intent(TodayActivity.this, AddActivity.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });
    }

    private void initDatas() {
        mScheduleList.clear();
        List<Schedule> list = DataSupport.findAll(Schedule.class);
        DateTime dateTime = new DateTime();
        DateTime today = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
        for (Schedule schedule : list) {
            Schedule s = SchedulesUtil.getSheduleInDay(schedule, today, schedule.getType());
            if (s != null)
                mScheduleList.add(s);

        }
        SortComparator comparator = new SortComparator();
        Collections.sort(mScheduleList, comparator);
        mAdapter.set(mScheduleList);
        mBinding.swipeRefreshLayout.setRefreshing(false);

    }


    public class ItemPresenter implements BaseViewAdapter.Presenter {
        public void onItemClick(Schedule schedule) {
            TodayShowActivity.actionStart(TodayActivity.this, schedule);
        }

    }

    private class ItemDecorator implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, int position, int viewType) {
            ItemListTodayBinding binding = (ItemListTodayBinding) holder.getBinding();
            Schedule schedule = mAdapter.getItemByPos(position);
            switch (schedule.getBiaoqian()) {
                case "工作":
                    binding.iconImg.setImageResource(R.mipmap.work);
                    break;
                case "学习":
                    binding.iconImg.setImageResource(R.mipmap.study);
                    break;
                case "生活":
                    binding.iconImg.setImageResource(R.mipmap.life);
                    break;
                default:
                    binding.iconImg.setImageResource(R.mipmap.other);
                    break;
            }
        }
    }


    //从小到大的排序
    private class SortComparator implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            Schedule s1 = (Schedule) o;
            Schedule s2 = (Schedule) t1;
            return s1.getStartTime().compareTo(s2.getStartTime());
        }
    }

}
