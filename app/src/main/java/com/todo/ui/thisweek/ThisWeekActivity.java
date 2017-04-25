package com.todo.ui.thisweek;

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
import com.todo.databinding.ActivityThisweekBinding;
import com.todo.databinding.ItemListThisweekBinding;
import com.todo.databinding.ItemListThisweekPastBinding;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.crud.AddActivity;
import com.todo.ui.datepicker.DatePickerActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.main.MainActivity;
import com.todo.ui.set.RingSettingActivity;
import com.todo.ui.today.TodayActivity;
import com.todo.ui.today.TodayShowActivity;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.LogUtil;
import com.todo.utils.SchedulesUtil;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.BindingViewHolder;
import com.todo.vendor.recyleradapter.MultiTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Created by tianyang on 2017/4/24.
 */

public class ThisWeekActivity extends BaseActivity {
    private static final int DAIBAN_TYPE = 1111;
    private static final int GUOQI_TYPE_HEADER = 2222;
    private static final int GUOQI_TYPE = 3333;
    private ActivityThisweekBinding mBinding;
    private MultiTypeAdapter mAdapter;
    private List<Schedule> mScheduleList = new ArrayList<>();
    //待办list
    private List<Schedule> daibanList = new ArrayList<>();
    //过期list
    private List<Schedule> guoqiList = new ArrayList<>();

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
        getMenuInflater().inflate(R.menu.thisweek_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rili:
                Intent intent = new Intent(ThisWeekActivity.this, DatePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.music:
                startActivity(new Intent(ThisWeekActivity.this, RingSettingActivity.class));
                break;
            case R.id.recent:
                startActivity(new Intent(ThisWeekActivity.this, MainActivity.class));
                break;
            case R.id.today:
                startActivity(new Intent(ThisWeekActivity.this, TodayActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_thisweek);

        initView();
        initDatas();
        initEventHandler();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("本周行程");
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
        mAdapter = new MultiTypeAdapter(this);
        mAdapter.addViewTypeToLayoutMap(DAIBAN_TYPE, R.layout.item_list_thisweek);
        mAdapter.addViewTypeToLayoutMap(GUOQI_TYPE_HEADER, R.layout.item_past_header);
        mAdapter.addViewTypeToLayoutMap(GUOQI_TYPE, R.layout.item_list_thisweek_past);
        mAdapter.setPresenter(new ItemPresenter());
        mAdapter.setDecorator(new ItemDecorator());
        mBinding.recyclerview.setAdapter(mAdapter);

//        mBinding.swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
    }


    private void initDatas() {
        mScheduleList.clear();
        daibanList.clear();
        guoqiList.clear();
        mAdapter.clear();
        List<Schedule> list = DataSupport.findAll(Schedule.class);
        for (Schedule schedule : list) {
            if (schedule.getType() == 0) {
                if (SchedulesUtil.isInThisWeek(schedule))
                    mScheduleList.add(schedule);
            } else if (schedule.getType() == 1) {
                mScheduleList.addAll(SchedulesUtil.getThisWeek(schedule));
            } else if (schedule.getType() == 2) {
                if (SchedulesUtil.getScheduleOfThisWeek(schedule) != null)
                    mScheduleList.add(SchedulesUtil.getScheduleOfThisWeek(schedule));
            } else if (schedule.getType() == 3) {
                mScheduleList.addAll(SchedulesUtil.getSchedulesOfWeek(schedule));
            }
        }

        for (Schedule schedule : mScheduleList) {
            String date = schedule.getStartTime();
            DateTime now = DateTime.now();
            DateTime dateTime = DateFormatUtil.parse(date);
            int seconds = Seconds.secondsBetween(now, dateTime).getSeconds();
            if (seconds < 0) guoqiList.add(schedule);
            else daibanList.add(schedule);
        }

        SortComparator comparator = new SortComparator();
        Collections.sort(daibanList, comparator);

        ListSortComparator comparator1 = new ListSortComparator();
        Collections.sort(guoqiList, comparator1);

        mAdapter.addAll(daibanList, DAIBAN_TYPE);
        if (guoqiList.size() > 0) {
            mAdapter.add(null, GUOQI_TYPE_HEADER);
            mAdapter.addAll(guoqiList, GUOQI_TYPE);
        }
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }


    private void updateAfterFilter(List<Schedule> schedules) {
        List<Schedule> daibanSchedules = new ArrayList<>();
        List<Schedule> guoqiSchedules = new ArrayList<>();
        mAdapter.clear();

        for (Schedule schedule : schedules) {
            String date = schedule.getStartTime();
            DateTime now = DateTime.now();
            DateTime dateTime = DateFormatUtil.parse(date);
            int seconds = Seconds.secondsBetween(now, dateTime).getSeconds();
            if (seconds < 0) guoqiSchedules.add(schedule);
            else daibanSchedules.add(schedule);
        }

        SortComparator comparator = new SortComparator();
        Collections.sort(daibanSchedules, comparator);

        ListSortComparator comparator1 = new ListSortComparator();
        Collections.sort(guoqiSchedules, comparator1);

        mAdapter.addAll(daibanSchedules, DAIBAN_TYPE);
        if (guoqiSchedules.size() > 0) {
            mAdapter.add(null, GUOQI_TYPE_HEADER);
            mAdapter.addAll(guoqiSchedules, GUOQI_TYPE);
        }
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
                        updateAfterFilter(schedules);
                        break;
                    case R.id.action_life:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("生活")) {
                                schedules.add(schedule);
                            }
                        }
                        updateAfterFilter(schedules);
                        break;
                    case R.id.action_study:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("学习")) {
                                schedules.add(schedule);
                            }
                        }
                        updateAfterFilter(schedules);
                        break;
                    case R.id.action_other:
                        schedules.clear();
                        for (Schedule schedule : mScheduleList) {
                            if (schedule.getBiaoqian().equals("其它")) {
                                schedules.add(schedule);
                            }
                        }
                        updateAfterFilter(schedules);
                        break;
                    case R.id.action_add:
                        Intent intent = new Intent(ThisWeekActivity.this, AddActivity.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });
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

    //从大到小的排序
    private class ListSortComparator implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            Schedule s1 = (Schedule) o;
            Schedule s2 = (Schedule) t1;
            return s2.getStartTime().compareTo(s1.getStartTime());
        }
    }

    public class ItemPresenter implements BaseViewAdapter.Presenter {
        public void onItemClick(Schedule schedule) {
            TodayShowActivity.actionStart(ThisWeekActivity.this, schedule);
        }

    }

    private class ItemDecorator implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, int position, int viewType) {
            if (viewType == DAIBAN_TYPE) {
                ItemListThisweekBinding binding = (ItemListThisweekBinding) holder.getBinding();
                Schedule schedule = (Schedule) mAdapter.getItemByPos(position);
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
            } else if (viewType == GUOQI_TYPE) {
                ItemListThisweekPastBinding binding = (ItemListThisweekPastBinding) holder.getBinding();
                Schedule schedule = (Schedule) mAdapter.getItemByPos(position);
                switch (schedule.getBiaoqian()) {
                    case "工作":
                        binding.iconImg.setImageResource(R.mipmap.work_grey);
                        break;
                    case "学习":
                        binding.iconImg.setImageResource(R.mipmap.study_grey);
                        break;
                    case "生活":
                        binding.iconImg.setImageResource(R.mipmap.life_grey);
                        break;
                    default:
                        binding.iconImg.setImageResource(R.mipmap.other_grey);
                        break;
                }
            }
        }

    }


}
