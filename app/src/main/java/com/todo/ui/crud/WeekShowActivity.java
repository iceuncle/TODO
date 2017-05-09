package com.todo.ui.crud;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.MyApplication;
import com.todo.R;
import com.todo.data.bean.Picture;
import com.todo.data.database.Alarm;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.preview.PreviewActivity;
import com.todo.utils.IsEmpty;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;
import com.todo.widget.ImageButtonText;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/2/19.
 */
public class WeekShowActivity extends BaseActivity {
    private int mWeekScheduleId;
    private WeekSchedule mWeekSchedule;
    private ImageButtonText imageText1, imageText2;
    private TextView tiltle, startTime, xunhuan, detailTv;
    private LinearLayout detailView, soundOrVibratorView, pictureView;
    private View dividerView;
    private SwitchCompat zhengdongSc, ringSc;
    private RecyclerView mRecyclerView;
    private SingleTypeAdapter<Picture> mAdapter;
    private Schedule mSchedule;

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
        ModifyActivity.actionStart(this, mWeekSchedule.getScheduleId(), "WeekShowActivity");
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


    protected void initView() {
        imageText1 = (ImageButtonText) findViewById(R.id.imageText1);
        imageText2 = (ImageButtonText) findViewById(R.id.imageText2);
        tiltle = (TextView) findViewById(R.id.title_tv);
        startTime = (TextView) findViewById(R.id.starttime_tv);
        xunhuan = (TextView) findViewById(R.id.xunhuan_tv);
        detailTv = (TextView) findViewById(R.id.detail_tv);
        detailView = (LinearLayout) findViewById(R.id.detail_view);
        soundOrVibratorView = (LinearLayout) findViewById(R.id.soundOrVibrator_view);
        pictureView = (LinearLayout) findViewById(R.id.picture_ll);
        dividerView = findViewById(R.id.soundOrVibrator_divider);
        zhengdongSc = (SwitchCompat) findViewById(R.id.zhendong_sc);
        ringSc = (SwitchCompat) findViewById(R.id.ring_sc);
        imageText1.setImageButtonTextClickable(false);
        imageText2.setImageButtonTextClickable(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new SingleTypeAdapter<>(this, R.layout.item_weekshow_pic);
        mAdapter.setPresenter(new ItemPresenter());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData() {
        mWeekScheduleId = getIntent().getExtras().getInt("WeekScheduleId");
        mWeekSchedule = DataSupport.find(WeekSchedule.class, mWeekScheduleId, true);
        mSchedule = DataSupport.find(Schedule.class, mWeekSchedule.getScheduleId(), true);
    }

    private void initEvent() {
        imageText1.getTextView().setText(mWeekSchedule.getBiaoqian());
        imageText1.getImgView().setImageResource(getBiaoqianImagId(mWeekSchedule.getBiaoqian()));
        imageText2.getTextView().setText(getAlarmText(mWeekSchedule.isRemind()));
        imageText2.getImgView().setImageResource(getAlarmImgId(mWeekSchedule.isRemind()));
        tiltle.setText(mWeekSchedule.getTitle());
        startTime.setText(mWeekSchedule.getStartTime());
        xunhuan.setText(mWeekSchedule.getCycleTime());
        if (mWeekSchedule.getDetail() == null || mWeekSchedule.getDetail().equals("")) {
            detailView.setVisibility(View.GONE);
        } else {
            detailView.setVisibility(View.VISIBLE);
            detailTv.setText(mWeekSchedule.getDetail());
        }

        if (mWeekSchedule.isRemind()) {
            soundOrVibratorView.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.VISIBLE);
            if (mWeekSchedule.getSoundOrVibrator() == 1) {
                ringSc.setChecked(true);
            } else if (mWeekSchedule.getSoundOrVibrator() == 0) {
                zhengdongSc.setChecked(true);
            } else if (mWeekSchedule.getSoundOrVibrator() == 2) {
                ringSc.setChecked(true);
                zhengdongSc.setChecked(true);
            }
        }

        if (!IsEmpty.list(mSchedule.getPhotoList())) {
            mAdapter.addAll(mSchedule.getPhotoList());
        } else {
            pictureView.setVisibility(View.GONE);
        }


    }


    private String getAlarmText(boolean b) {
        if (b) return "闹钟开";
        else return "闹钟关";
    }

    private int getAlarmImgId(boolean b) {
        if (b) return R.mipmap.alarm_on;
        else return R.mipmap.alarm_off;
    }

    private int getBiaoqianImagId(String s) {
        switch (s) {
            case "工作":
                return R.mipmap.work;
            case "学习":
                return R.mipmap.study;
            case "生活":
                return R.mipmap.life;
            default:
                return R.mipmap.other;
        }
    }

    public void Delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("将删除所有该类行的重复闹钟！")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = mWeekSchedule.getScheduleId();

                        List<Integer> alarmIdList = new ArrayList<Integer>();
                        for (Alarm alarm : mSchedule.getAlarmList())
                            alarmIdList.add(alarm.getId());
                        if (mWeekSchedule.isRemind()) {
                            for (int alarmId : alarmIdList) {
                                AlarmManagerUtil.cancelAlarm(MyApplication.instance(), "com.loonggg.alarm.clock", alarmId);
                                Log.d("alarmId", "alarmID: " + alarmId);
                            }
                        }
                        DataSupport.delete(Schedule.class, id);
                        DataSupport.deleteAll(WeekSchedule.class, "scheduleId=?", id + "");
                        EventBus.getDefault().post(new MsgEvent("UpDate"));
                        finish();
                    }
                }).show();
    }


    public class ItemPresenter implements BaseViewAdapter.Presenter {

        private ArrayList<String> stringList = new ArrayList<>();

        public void previewClick() {
            stringList.clear();
            for (Picture picture : mSchedule.getPhotoList()) {
                stringList.add((String) picture.getImgRes());
            }
            PreviewActivity.actionStart(WeekShowActivity.this, stringList);
        }

    }
}
