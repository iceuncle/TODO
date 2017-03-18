package com.todo.ui.datepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.R;
import com.todo.data.database.Alarm;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.crud.ModifyActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.widget.ImageButtonText;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/3/11.
 */
public class DayShowActivity extends BaseActivity {
    private Schedule mSchedule;
    private ImageButtonText imageText1, imageText2;
    private TextView tiltle, startTime, xunhuan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initToorBar();
        initView();
        initData();
        initEvent();

    }

    public static void actionStart(Context context, Schedule schedule) {
        Intent intent = new Intent(context, DayShowActivity.class);
        intent.putExtra("DaySchedule", schedule);
        context.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ModifyActivity.actionStart(this, mSchedule.getId(), "DayShowActivity");
        return true;
    }

    private void initToorBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("显示");
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
    }

    private void initView() {
        imageText1 = (ImageButtonText) findViewById(R.id.imageText1);
        imageText2 = (ImageButtonText) findViewById(R.id.imageText2);
        tiltle = (TextView) findViewById(R.id.title_tv);
        startTime = (TextView) findViewById(R.id.starttime_tv);
        xunhuan = (TextView) findViewById(R.id.xunhuan_tv);
        imageText1.setImageButtonTextClickable(false);
        imageText2.setImageButtonTextClickable(false);
    }

    private void initData() {
        mSchedule = (Schedule) getIntent().getSerializableExtra("DaySchedule");

    }

    private void initEvent() {
        imageText1.getTextView().setText(mSchedule.getBiaoqian());
        imageText1.getImgView().setImageResource(getBiaoqianImagId(mSchedule.getBiaoqian()));
        imageText2.getTextView().setText(getAlarmText(mSchedule.isRemind()));
        imageText2.getImgView().setImageResource(getAlarmImgId(mSchedule.isRemind()));
        tiltle.setText(mSchedule.getTitle());
        startTime.setText(mSchedule.getStartTime());
        xunhuan.setText(mSchedule.getCycleTime());

    }


    private String getAlarmText(boolean b) {
        if (b) return "闹钟开";
        else return "闹钟关";
    }

    private int getAlarmImgId(boolean b) {
        if (b) return R.mipmap.alarmon;
        else return R.mipmap.alarmoff;
    }

    private int getBiaoqianImagId(String s) {
        switch (s) {
            case "工作":
                return R.mipmap.gongzuo1;
            case "学习":
                return R.mipmap.xuexi1;
            case "生活":
                return R.mipmap.shenghuo1;
            default:
                return R.mipmap.qita1;
        }
    }

    public void Delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("将删除所有该类行的重复闹钟！")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = mSchedule.getId();
                        List<Integer> alarmIdList = new ArrayList<Integer>();
                        for (Alarm alarm : mSchedule.getAlarmList())
                            alarmIdList.add(alarm.getId());
                        if (mSchedule.isRemind()) {
                            for (int alarmId : alarmIdList)
                                AlarmManagerUtil.cancelAlarm(DayShowActivity.this, "com.loonggg.alarm.clock", alarmId);
                        }
                        DataSupport.delete(Schedule.class, id);
                        DataSupport.deleteAll(WeekSchedule.class, "scheduleId=?", id + "");
                        EventBus.getDefault().post(new MsgEvent("UpDate"));
                        EventBus.getDefault().post(new MsgEvent("MonthViewUpDate"));
                        finish();
                    }
                }).show();
    }
}
