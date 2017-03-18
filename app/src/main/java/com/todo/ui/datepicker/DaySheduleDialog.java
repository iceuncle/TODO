package com.todo.ui.datepicker;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.github.markzhai.recyclerview.BaseViewAdapter;
import com.github.markzhai.recyclerview.BindingViewHolder;
import com.github.markzhai.recyclerview.SingleTypeAdapter;
import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.databinding.DaysheduleDialogBinding;
import com.todo.databinding.ItemAdapterDaytimeBinding;
import com.todo.utils.SchedulesUtil;
import com.todo.utils.Util;

import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by tianyang on 2017/3/11.
 */
public class DaySheduleDialog extends AppCompatDialog {
    private DatePicker picker;


    public DaySheduleDialog(Context context) {
        super(context);
    }

    public DaySheduleDialog(Context context, int theme, DatePicker picker) {
        super(context, theme);
        this.picker = picker;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        picker.getMonthView().setFocusableInTouchMode(false);
    }

    public static class Builder {
        private Context mContext;
        private DateTime dayTime;
        private DaySheduleDialog dialog;
        private DaysheduleDialogBinding mBinding;
        private SingleTypeAdapter<Schedule> mAdapter;
        private List<Schedule> scheduleList = new ArrayList<>();
        private DatePicker datePicker;

        public Builder(@NonNull Context context) {
            mContext = context;
        }

        public Builder setDayTime(DateTime dayTime) {
            this.dayTime = dayTime;
            return this;
        }

        public Builder setDatePicker(DatePicker datePicker) {
            this.datePicker = datePicker;
            return this;
        }

        public DaySheduleDialog create() {
            dialog = new DaySheduleDialog(mContext, R.style.ActionSheetDialog, datePicker);
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dayshedule_dialog, null, false);

            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new SingleTypeAdapter(mContext, R.layout.item_adapter_daytime);
            mAdapter.setPresenter(new ItemPresenter());
            mBinding.recyclerView.setAdapter(mAdapter);
            mAdapter.setDecorator(new DemoAdapterDecorator());
            mBinding.confirmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            initDatas();
            dialog.setContentView(mBinding.getRoot());
            return dialog;
        }

        private void initDatas() {
            List<Schedule> list = DataSupport.findAll(Schedule.class);
            for (Schedule schedule : list) {
                if (schedule.getType() == 0) {
                    Schedule s = SchedulesUtil.getSheduleInDay(schedule, dayTime, 0);
                    if (s != null)
                        scheduleList.add(s);
                } else if (schedule.getType() == 1) {
                    Schedule s = SchedulesUtil.getSheduleInDay(schedule, dayTime, 1);
                    if (s != null)
                        scheduleList.add(s);
                } else if (schedule.getType() == 2) {
                    Schedule s = SchedulesUtil.getSheduleInDay(schedule, dayTime, 2);
                    if (s != null)
                        scheduleList.add(s);
                } else if (schedule.getType() == 3) {
                    Schedule s = SchedulesUtil.getSheduleInDay(schedule, dayTime, 3);
                    if (s != null)
                        scheduleList.add(s);
                }
            }
            ListSortComparator comparator = new ListSortComparator();
            Collections.sort(scheduleList, comparator);
            mAdapter.set(scheduleList);
        }

        public DaySheduleDialog show() {
            dialog = create();
            dialog.show();
            double dialogwidth = Util.getScreenWidth(mContext) - Util.dip2px(mContext, 80);
            double dialogHeight = Util.getScreenHeight(mContext) - Util.dip2px(mContext, 120);
            dialog.getWindow().setLayout((int) dialogwidth, (int) dialogHeight);
            return dialog;
        }


        public class DemoAdapterDecorator implements BaseViewAdapter.Decorator {
            ItemAdapterDaytimeBinding binding;

            @Override
            public void decorator(BindingViewHolder holder, int position, int viewType) {
                binding = (ItemAdapterDaytimeBinding) holder.getBinding();
                if (scheduleList.get(position).isRemind()) {
                    binding.alarmImg.setImageResource(R.mipmap.alarmon);
                } else {
                    binding.alarmImg.setImageResource(R.mipmap.alarmoff);
                }
                if (scheduleList.get(position).getBiaoqian() != null)
                    switch (scheduleList.get(position).getBiaoqian()) {
                        case "工作":
                            binding.biaoqianImg.setImageResource(R.mipmap.gongzuo1);
                            break;
                        case "学习":
                            binding.biaoqianImg.setImageResource(R.mipmap.xuexi1);
                            break;
                        case "生活":
                            binding.biaoqianImg.setImageResource(R.mipmap.shenghuo1);
                            break;
                        case "其它":
                            binding.biaoqianImg.setImageResource(R.mipmap.qita1);
                            break;
                        default:
                            break;
                    }

            }
        }

        public class ItemPresenter implements BaseViewAdapter.Presenter {
            public void onItemClick(Schedule schedule) {
                DayShowActivity.actionStart(mContext, schedule);
                dialog.dismiss();
            }

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


}
