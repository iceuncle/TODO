package com.todo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.base.BaseFragment;
import com.todo.ui.crud.AddActivity;
import com.todo.ui.crud.WeekShowActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.main.adpters.MainAdapter;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.LogUtil;
import com.todo.utils.SchedulesUtil;
import com.todo.widget.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by tianyang on 2017/2/14.
 */
public class MainFragment extends BaseFragment implements MainAdapter.MyOnItemClickLitener {
    private static final String ARG_POSITION = "position";
    private int mPosition;
    private RecyclerView recyclerView;
    private List<WeekSchedule> scheduleList = new ArrayList<>();
    private List<Schedule> weekList = new ArrayList<>();
    private List<WeekSchedule> weekScheduleList = new ArrayList<>();
    private List<WeekSchedule> daibanList = new ArrayList<>();
    private List<WeekSchedule> guoqiList = new ArrayList<>();
    private List<WeekSchedule> wanchengList = new ArrayList<>();
    private MainAdapter daibanAdapter;
    private MainAdapter guoqiAdapter;
    private MainAdapter wanchengAdapter;

    private SwipeRefreshLayout refreshLayout;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    public static MainFragment newInstance(int position) {
        MainFragment mainFragment = new MainFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mainFragment.setArguments(b);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        EventBus.getDefault().register(this);

    }

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        LogUtil.d("www", "onCreateView");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (mPosition == 0) {
            daibanAdapter = new MainAdapter(getActivity(), scheduleList, false);
            recyclerView.setAdapter(daibanAdapter);
            daibanAdapter.setOnItemClickLitener(this);
        } else if (mPosition == 1) {
            guoqiAdapter = new MainAdapter(getActivity(), scheduleList, true);
            recyclerView.setAdapter(guoqiAdapter);
            guoqiAdapter.setOnItemClickLitener(this);
        } else {
            wanchengAdapter = new MainAdapter(getActivity(), scheduleList, true);
            recyclerView.setAdapter(wanchengAdapter);
            wanchengAdapter.setOnItemClickLitener(this);
        }


        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        refreshLayout.setProgressViewOffset(false, 0, 100);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();
            }
        });
        isPrepared = true;

        final FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_work:
                        break;
                    case R.id.action_life:
                        break;
                    case R.id.action_study:
                        break;
                    case R.id.action_other:
                        break;
                    case R.id.action_add:
                        LogUtil.d("add...");
                        Intent intent = new Intent(getActivity(), AddActivity.class);
                        startActivity(intent);
                        fabSpeedDial.closeMenu();
                        break;

                }
                return false;
            }
        });

        return view;

    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initDatas();
    }


    private void initDatas() {
        if (!refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(true);
        LogUtil.d("www", "initDatas");

        weekList.clear();
        scheduleList.clear();
        wanchengList.clear();
        daibanList.clear();
        guoqiList.clear();
        weekScheduleList.clear();

        Action1<List<WeekSchedule>> onNextAction = new Action1<List<WeekSchedule>>() {
            @Override
            public void call(List<WeekSchedule> list) {
                refreshLayout.setRefreshing(false);
                LogUtil.d("www", "size    " + list.size());
                switch (mPosition) {
                    case 0:
                        daibanAdapter.update(list);
                        break;
                    case 1:
                        guoqiAdapter.update(list);
                        break;
                    default:
                        wanchengAdapter.update(list);
                        break;
                }


            }
        };


        Observable.create(new Observable.OnSubscribe<List<WeekSchedule>>() {
            @Override
            public void call(Subscriber<? super List<WeekSchedule>> subscriber) {
                setWeekList();
                setWeekScheduleList();
                subscriber.onNext(setScheduleList());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextAction);


    }


    /**
     * 解析一周内所有的Schedule
     */
    private void setWeekList() {
        List<Schedule> list = DataSupport.findAll(Schedule.class);
        for (Schedule schedule : list) {
            if (schedule.getType() == 0) {
                if (SchedulesUtil.isInWeek(schedule))
                    weekList.add(schedule);
            } else if (schedule.getType() == 1) {
                weekList.addAll(SchedulesUtil.getWeek(schedule));
            } else if (schedule.getType() == 2) {
                if (SchedulesUtil.getDayOfWeek(schedule) != null)
                    weekList.add(SchedulesUtil.getDayOfWeek(schedule));
            } else if (schedule.getType() == 3) {
                weekList.addAll(SchedulesUtil.getDaysOfWeek(schedule));
            }
        }
    }

    /**
     * 将所有的schedule转换成weekSchedule并保存，删除三天之前的数据
     */
    private void setWeekScheduleList() {
        SchedulesUtil.deleteThreeDayBeforeDatas();
        try {
            for (Schedule s : weekList) {
                if (!isExist(s)) {
                    WeekSchedule weekSchedule = new WeekSchedule();
                    weekSchedule.setSchedule(s);
                    weekSchedule.setFinished(false);
                    weekSchedule.save();
                }
            }
        } catch (ConcurrentModificationException exception) {
            LogUtil.d("exception..");
        }
        weekScheduleList = DataSupport.findAll(WeekSchedule.class);

    }


    /**
     * 将所有的weekSchedule转换成三种状态
     *
     * @return
     */
    private List<WeekSchedule> setScheduleList() {
        for (WeekSchedule schedule : weekScheduleList) {
            if (schedule.isFinished()) wanchengList.add(schedule);
            else {
                String date = schedule.getStartTime();
                DateTime now = DateTime.now();
                DateTime dateTime = DateFormatUtil.parse(date);
                int seconds = Seconds.secondsBetween(now, dateTime).getSeconds();
                if (seconds < 0) guoqiList.add(schedule);
                else daibanList.add(schedule);
            }
        }
        if (mPosition == 0) {
            scheduleList.addAll(daibanList);
            ListSortComparator2 comparator = new ListSortComparator2();
            Collections.sort(scheduleList, comparator);
        } else if (mPosition == 1) {
            scheduleList.addAll(guoqiList);
            ListSortComparator comparator = new ListSortComparator();
            Collections.sort(scheduleList, comparator);
        } else if (mPosition == 2) {
            scheduleList.addAll(wanchengList);
            ListSortComparator comparator = new ListSortComparator();
            Collections.sort(scheduleList, comparator);
        }
        return scheduleList;
    }

    /**
     * 判断是否存在weekSchedule
     */
    public boolean isExist(Schedule schedule) {
        List<WeekSchedule> weekScheduleList = DataSupport.findAll(WeekSchedule.class);
        for (WeekSchedule weekSchedule : weekScheduleList) {
            if (weekSchedule.getScheduleId() == schedule.getId()
                    && schedule.getStartTime().equals(weekSchedule.getStartTime()))
                return true;
        }
        return false;
    }


    @Override
    public void onItemClick(View view, int position) {
        if (scheduleList.get(position).isFinished()) {
            scheduleList.get(position).setFinished(false);
            scheduleList.get(position).save();
            scheduleList.remove(position);

            switch (mPosition) {
                case 0:
                    daibanAdapter.update(scheduleList);
                    break;
                case 1:
                    guoqiAdapter.update(scheduleList);
                    break;
                default:
                    wanchengAdapter.update(scheduleList);
                    break;
            }


        } else {
            scheduleList.get(position).setFinished(true);
            scheduleList.get(position).save();
            scheduleList.remove(position);
            switch (mPosition) {
                case 0:
                    daibanAdapter.update(scheduleList);
                    break;
                case 1:
                    guoqiAdapter.update(scheduleList);
                    break;
                default:
                    wanchengAdapter.update(scheduleList);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view, int poisition) {
        Intent intent = new Intent(getActivity(), WeekShowActivity.class);
        intent.putExtra("WeekScheduleId", scheduleList.get(poisition).getId());
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, final int position) {

    }


    //从大到小的排序
    class ListSortComparator implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            WeekSchedule s1 = (WeekSchedule) o;
            WeekSchedule s2 = (WeekSchedule) t1;
            return s2.getStartTime().compareTo(s1.getStartTime());
        }
    }

    //从小到大的排序
    class ListSortComparator2 implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            WeekSchedule s1 = (WeekSchedule) o;
            WeekSchedule s2 = (WeekSchedule) t1;
            return s1.getStartTime().compareTo(s2.getStartTime());
        }
    }


}
