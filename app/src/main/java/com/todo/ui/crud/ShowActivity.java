package com.todo.ui.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.base.BaseActivity;
import com.todo.utils.ImageButtonText;
import com.todo.utils.LogUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by tianyang on 2017/2/19.
 */
public class ShowActivity extends BaseActivity {
    private int scheduleId;
    private WeekSchedule schedule;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ModifyActivity.class);
        intent.putExtra("ScheduleID", scheduleId);
        startActivity(intent);
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
        scheduleId = getIntent().getExtras().getInt("schedulaId");
        schedule = DataSupport.find(WeekSchedule.class, scheduleId);

    }

    private void initEvent() {
        imageText1.getTextView().setText(schedule.getBiaoqian());
        imageText1.getImgView().setImageResource(getBiaoqianImagId(schedule.getBiaoqian()));
        imageText2.getTextView().setText(getAlarmText(schedule.isRemind()));
        imageText2.getImgView().setImageResource(getAlarmImgId(schedule.isRemind()));
        tiltle.setText(schedule.getTitle());
        startTime.setText(schedule.getStartTime());
        xunhuan.setText(schedule.getCycleTime());

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

}
