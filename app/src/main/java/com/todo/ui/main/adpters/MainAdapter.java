package com.todo.ui.main.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.R;
import com.todo.data.database.WeekSchedule;
import com.todo.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/2/19.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    public List<WeekSchedule> scheduleList = new ArrayList<>();
    private MyOnItemClickLitener mOnItemClickLitener;
    private Boolean showCheckBox;
    private Context mContext;


    public MainAdapter(Context context, List<WeekSchedule> list, Boolean showCheckBox) {
        this.scheduleList.addAll(list);
        this.showCheckBox = showCheckBox;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, title;
        ImageView tagImg, alarImg;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            tagImg = (ImageView) itemView.findViewById(R.id.biaoqianImg);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            alarImg = (ImageView) itemView.findViewById(R.id.alarmImg);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.d("yyy", "onCreateViewHolder    " );
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_main, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                mOnItemClickLitener.onClick(view, position);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                mOnItemClickLitener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LogUtil.d("yyy", "onBindViewHolder    " +position);
        final WeekSchedule schedule = scheduleList.get(position);
        holder.time.setText(schedule.getStartTime());
        holder.title.setText(schedule.getTitle());
        holder.checkBox.setChecked(schedule.isFinished());
        if (showCheckBox)
            holder.checkBox.setVisibility(View.VISIBLE);
        else
            holder.checkBox.setVisibility(View.GONE);
        switch (schedule.getBiaoqian()) {
            case "工作":
                holder.tagImg.setImageResource(R.mipmap.work);
                break;
            case "学习":
                holder.tagImg.setImageResource(R.mipmap.study);
                break;
            case "生活":
                holder.tagImg.setImageResource(R.mipmap.life);
                break;
            case "其它":
                holder.tagImg.setImageResource(R.mipmap.other);
                break;
        }

        if (schedule.isRemind()) holder.alarImg.setImageResource(R.mipmap.alarm_on);
        else holder.alarImg.setImageResource(R.mipmap.alarm_off);


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

    public void clear(){
        this.scheduleList.clear();
        notifyDataSetChanged();
    }

    public void update(List<WeekSchedule> list) {
        this.scheduleList.clear();
        this.scheduleList.addAll(list);
        notifyDataSetChanged();
    }


}
