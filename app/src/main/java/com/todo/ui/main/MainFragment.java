package com.todo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.ui.adpters.MainAdapter;
import com.todo.ui.crud.ShowActivity;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.DividerItemDecoration;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tianyang on 2017/2/14.
 */
public class MainFragment extends Fragment implements MainAdapter.MyOnItemClickLitener {
    private static final String ARG_POSITION = "position";
    private int position;
    private RecyclerView recyclerView;
    private List<Schedule> scheduleList = new ArrayList<>();
    private List<Schedule> daibanList = new ArrayList<>();
    private List<Schedule> guoqiList = new ArrayList<>();
    private List<Schedule> wanchengList = new ArrayList<>();
    private MainAdapter adapter;

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
        position = getArguments().getInt(ARG_POSITION);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(scheduleList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickLitener(this);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initDatas() {
        scheduleList.clear();
        wanchengList.clear();
        daibanList.clear();
        guoqiList.clear();

        for (Schedule schedule : DataSupport.findAll(Schedule.class)) {
            if (schedule.isFinished()) wanchengList.add(schedule);
            else {
                String date = schedule.getStartTime();
                DateTime now = DateTime.now();
                DateTime dateTime = DateFormatUtil.parse(date);
                int minutes = Minutes.minutesBetween(now, dateTime).getMinutes();
                if (minutes < 0) guoqiList.add(schedule);
                else daibanList.add(schedule);
            }
        }
        if (position == 0)
            scheduleList.addAll(daibanList);
        else if (position == 1)
            scheduleList.addAll(guoqiList);
        else if (position == 2)
            scheduleList.addAll(wanchengList);

        ListSortComparator comparator = new ListSortComparator();
        Collections.sort(scheduleList, comparator);
    }


    @Override
    public void onItemClick(View view, int position) {
        if (scheduleList.get(position).isFinished()) {
            scheduleList.get(position).setFinished(false);
            scheduleList.get(position).save();
            scheduleList.remove(position);
            adapter.notifyDataSetChanged();

        } else {
            scheduleList.get(position).setFinished(true);
            scheduleList.get(position).save();
            scheduleList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view, int poisition) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        intent.putExtra("schedulaData", scheduleList.get(poisition).getId());
        startActivity(intent);
    }


    class ListSortComparator implements Comparator {

        @Override
        public int compare(Object o, Object t1) {
            Schedule s1 = (Schedule) o;
            Schedule s2 = (Schedule) t1;
            return s1.getStartTime().compareTo(s2.getStartTime());
        }
    }


}
