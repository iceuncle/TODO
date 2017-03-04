package com.todo.ui.adpters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;

import java.util.List;

/**
 * Created by tianyang on 2017/2/19.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    public List<WeekSchedule> scheduleList;
    private MyOnItemClickLitener mOnItemClickLitener;
    private View mView;


    

    public MainAdapter(List<WeekSchedule> list) {
        this.scheduleList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, title;
        ImageView tagImg, alarImg;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            tagImg =
                    (ImageView) itemView.findViewById(R.id.biaoqianImg);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            alarImg = (ImageView) itemView.findViewById(R.id.alarmImg);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_main, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        mView = view;
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WeekSchedule schedule = scheduleList.get(position);
        holder.time.setText(schedule.getStartTime());
        holder.title.setText(schedule.getTitle());
        holder.checkBox.setChecked(schedule.isFinished());

        switch (schedule.getBiaoqian()) {
            case "工作":
                holder.tagImg.setImageResource(R.mipmap.gongzuo1);
                break;
            case "学习":
                holder.tagImg.setImageResource(R.mipmap.xuexi1);
                break;
            case "生活":
                holder.tagImg.setImageResource(R.mipmap.shenghuo1);
                break;
            case "其它":
                holder.tagImg.setImageResource(R.mipmap.qita1);
                break;
        }

        if (schedule.isRemind()) holder.alarImg.setImageResource(R.mipmap.alarmon);
        else holder.alarImg.setImageResource(R.mipmap.alarmoff);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLitener.onItemClick(view, position);
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLitener.onClick(view, position);
            }
        });

        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickLitener.onLongClick(view, position);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public interface MyOnItemClickLitener {
        void onItemClick(View view, int position);

        void onClick(View view, int poisition);

        void onLongClick(View view, int position);
    }

    public void setOnItemClickLitener(MyOnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
