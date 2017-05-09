package com.todo.ui.crud;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.MyApplication;
import com.todo.R;
import com.todo.data.bean.CalendarBean;
import com.todo.data.bean.Picture;
import com.todo.data.database.Alarm;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.datepicker.DatePickerActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.main.MainActivity;
import com.todo.ui.preview.PreviewActivity;
import com.todo.ui.today.TodayActivity;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.IsEmpty;
import com.todo.utils.LogUtil;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;
import com.todo.widget.DateTimePickDialog;
import com.todo.widget.ImageButtonText;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by tianyang on 2017/2/20.
 */
public class ModifyActivity extends BaseActivity implements ImageButtonText.OnImageButtonTextClickListener {
    private static final int CAMERA_PERMISSION = 1;
    private Schedule mSchedule;
    private EditText titleEt, detailEt;
    private TextView startTimeTv, xunhuanTv;
    private SwitchCompat switchCompat, zhengdongSc, ringSc;
    private View soundOrVibratorDivider;
    private LinearLayout soundOrVibratorView;
    private ImageButtonText ibt1, ibt2, ibt3, ibt4;
    private String title, startTime, xunhuan, tag;
    private CalendarBean calendarBean = new CalendarBean();
    public Calendar startCalendar;
    private int mScheduleId;
    private int soundOrVibrator = 1;//铃声或震动设置
    private int alarmId;  //存入数据库的id同样设置闹钟id
    //记录是否点击过时间选择器
    private boolean isClicked = false;

    private int selectedIndex = 0; //重复类型
    private String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] types = {"只此一次", "每天", "每周", "自定义"};
    private boolean[] selectedWeekdays = new boolean[7];
    private String type;

    private RecyclerView mRecyclerView;
    private SingleTypeAdapter<Picture> mAdapter;

    //照片url
    private List<Picture> photoUrls = new ArrayList<>();

    private ArrayList<String> stringList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initToorBar();
        initView();
        initDatas();
        initEvents();
    }

    public static void actionStart(Context context, int scheduleId, String type) {
        Intent intent = new Intent(context, ModifyActivity.class);
        intent.putExtra("ScheduleID", scheduleId);
        intent.putExtra("ActivityType", type);
        context.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        save();
        return true;
    }

    private void initToorBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("编辑");
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
        titleEt = (EditText) findViewById(R.id.title_et);
        startTimeTv = (TextView) findViewById(R.id.starttime_content_tv);
        xunhuanTv = (TextView) findViewById(R.id.xunhuan_content_tv);
        switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
        detailEt = (EditText) findViewById(R.id.detail_et);
        ibt1 = (ImageButtonText) findViewById(R.id.imageText1);
        ibt2 = (ImageButtonText) findViewById(R.id.imageText2);
        ibt3 = (ImageButtonText) findViewById(R.id.imageText3);
        ibt4 = (ImageButtonText) findViewById(R.id.imageText4);
        ibt1.getTextView().setText("工作");
        ibt2.getTextView().setText("生活");
        ibt3.getTextView().setText("学习");
        ibt4.getTextView().setText("其它");
        ibt1.getImgView().setImageResource(R.mipmap.work_default);
        ibt2.getImgView().setImageResource(R.mipmap.life_default);
        ibt3.getImgView().setImageResource(R.mipmap.study_default);
        ibt4.getImgView().setImageResource(R.mipmap.other_default);
        ibt1.setmOnImageButtonTextClickListener(this);
        ibt2.setmOnImageButtonTextClickListener(this);
        ibt3.setmOnImageButtonTextClickListener(this);
        ibt4.setmOnImageButtonTextClickListener(this);

        soundOrVibratorView = (LinearLayout) findViewById(R.id.soundOrVibrator_view);
        soundOrVibratorDivider = findViewById(R.id.soundOrVibrator_divider);
        zhengdongSc = (SwitchCompat) findViewById(R.id.zhendong_sc);
        ringSc = (SwitchCompat) findViewById(R.id.ring_sc);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new SingleTypeAdapter<>(this, R.layout.item_modify_pic);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setPresenter(new ItemPresenter());


    }

    protected void initDatas() {
        mScheduleId = getIntent().getExtras().getInt("ScheduleID");
        type = getIntent().getStringExtra("ActivityType");
        mSchedule = DataSupport.find(Schedule.class, mScheduleId, true);
        selectedIndex = mSchedule.getType();
        soundOrVibrator = mSchedule.getSoundOrVibrator();

        if (!IsEmpty.list(mSchedule.getPhotoList())) {
            mAdapter.addAll(mSchedule.getPhotoList());
        }
        mAdapter.add(new Picture(R.mipmap.ic_addpics, true));

    }

    private void initEvents() {

        titleEt.setText(mSchedule.getTitle());
        titleEt.setSelection(mSchedule.getTitle().length());
        switchCompat.setChecked(mSchedule.isRemind());
        startTimeTv.setText(mSchedule.getStartTime());
        xunhuanTv.setText(mSchedule.getCycleTime());
        detailEt.setText(mSchedule.getDetail());
        switch (mSchedule.getBiaoqian()) {
            case "工作":
                ibt1.getImgView().setImageResource(R.mipmap.work);
                ibt1.getTextView().setTextColor(getResources().getColor(R.color.work_color));
                tag = "工作";
                break;
            case "学习":
                ibt3.getImgView().setImageResource(R.mipmap.study);
                ibt3.getTextView().setTextColor(getResources().getColor(R.color.study_color));
                tag = "学习";
                break;
            case "生活":
                ibt2.getImgView().setImageResource(R.mipmap.life);
                ibt2.getTextView().setTextColor(getResources().getColor(R.color.life_color));
                tag = "生活";
                break;
            default:
                ibt4.getImgView().setImageResource(R.mipmap.other);
                ibt4.getTextView().setTextColor(getResources().getColor(R.color.other_color));
                tag = "其它";
        }

        if (mSchedule.isRemind()) {
            soundOrVibratorView.setVisibility(View.VISIBLE);
            soundOrVibratorDivider.setVisibility(View.VISIBLE);
            if (mSchedule.getSoundOrVibrator() == 1) {
                ringSc.setChecked(true);
            } else if (mSchedule.getSoundOrVibrator() == 0) {
                zhengdongSc.setChecked(true);
            } else if (mSchedule.getSoundOrVibrator() == 2) {
                ringSc.setChecked(true);
                zhengdongSc.setChecked(true);
            }
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    soundOrVibratorView.setVisibility(View.VISIBLE);
                    soundOrVibratorDivider.setVisibility(View.VISIBLE);
                } else {
                    soundOrVibratorView.setVisibility(View.GONE);
                    soundOrVibratorDivider.setVisibility(View.GONE);
                }
            }
        });

        ringSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b && !zhengdongSc.isChecked())
                    zhengdongSc.setChecked(true);
            }
        });

        zhengdongSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b && !ringSc.isChecked())
                    ringSc.setChecked(true);
            }
        });
    }

    @Override
    public void OnImageButtonTextClick(View v) {
        switch (v.getId()) {
            case R.id.imageText1:
                if (ibt1.isChecked()) {
                    ibt1.setChecked(false);
                    ibt1.getImgView().setImageResource(R.mipmap.work_default);
                    ibt1.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    tag = ibt1.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    ibt1.setChecked(true);
                    ibt1.getImgView().setImageResource(R.mipmap.work);
                    ibt1.getTextView().setTextColor(getResources().getColor(R.color.work_color));
                }
                break;
            case R.id.imageText2:
                if (ibt2.isChecked()) {
                    ibt2.setChecked(false);
                    ibt2.getImgView().setImageResource(R.mipmap.life_default);
                    ibt2.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    tag = ibt2.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    ibt2.setChecked(true);
                    ibt2.getImgView().setImageResource(R.mipmap.life);
                    ibt2.getTextView().setTextColor(getResources().getColor(R.color.life_color));
                }
                break;
            case R.id.imageText3:
                if (ibt3.isChecked()) {
                    ibt3.setChecked(false);
                    ibt3.getImgView().setImageResource(R.mipmap.study_default);
                    ibt3.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    tag = ibt3.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    ibt3.setChecked(true);
                    ibt3.getImgView().setImageResource(R.mipmap.study);
                    ibt3.getTextView().setTextColor(getResources().getColor(R.color.study_color));
                }
                break;
            case R.id.imageText4:
                if (ibt4.isChecked()) {
                    ibt4.setChecked(false);
                    ibt4.getImgView().setImageResource(R.mipmap.other_default);
                    ibt4.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    tag = ibt4.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    ibt4.setChecked(true);
                    ibt4.getImgView().setImageResource(R.mipmap.other);
                    ibt4.getTextView().setTextColor(getResources().getColor(R.color.other_color));
                }
                break;
            default:
        }
    }


    public void resetAllImageBUttonText() {
        ibt1.setChecked(false);
        ibt1.getImgView().setImageResource(R.mipmap.work_default);
        ibt1.getTextView().setTextColor(getResources().getColor(R.color.g0));
        ibt2.setChecked(false);
        ibt2.getImgView().setImageResource(R.mipmap.life_default);
        ibt2.getTextView().setTextColor(getResources().getColor(R.color.g0));
        ibt3.setChecked(false);
        ibt3.getImgView().setImageResource(R.mipmap.study_default);
        ibt3.getTextView().setTextColor(getResources().getColor(R.color.g0));
        ibt4.setChecked(false);
        ibt4.getImgView().setImageResource(R.mipmap.other_default);
        ibt4.getTextView().setTextColor(getResources().getColor(R.color.g0));
    }

    //选择提醒时间点击事件
    public void startTime(View view) {
        isClicked = true;
        DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(this, "", calendarBean.getCalendar());
        dateTimePickDialog.dateTimePicKDialog(startTimeTv, calendarBean);
    }

    public void xunHuan(View view) {
        new AlertDialog.Builder(this)
                .setTitle("请选择重复类型")
                .setSingleChoiceItems(types, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedIndex = i;
                            }
                        })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("qqq", "selectedIndex   " + selectedIndex);
                        if (selectedIndex == 3) showWeekDialog();
                        xunhuanTv.setText(types[selectedIndex]);
                    }
                }).show();
    }

    public void showWeekDialog() {
        selectedWeekdays = new boolean[7];
        new AlertDialog.Builder(this)
                .setTitle("请选择重复类型")
                .setMultiChoiceItems(weekdays, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                selectedWeekdays[i] = b;
                            }
                        })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        xunhuanTv.setText(getXunhuanText(selectedWeekdays));
                    }
                }).show();
    }


    public String getXunhuanText(boolean[] b) {
        String a = "每周（";
        for (int i = 0; i < b.length; i++) {
            if (b[i]) a += weekdays[i];
            if (b.length > 1 && b[i]) a += "，";
        }
        a = a.substring(0, a.length() - 1);
        return a + ")";
    }


    private void save() {
        //获取铃声或震动设置的值
        if (ringSc.isChecked() && !zhengdongSc.isChecked())
            soundOrVibrator = 1;
        else if (ringSc.isChecked() && zhengdongSc.isChecked())
            soundOrVibrator = 2;
        else if (!ringSc.isChecked() && zhengdongSc.isChecked())
            soundOrVibrator = 0;
        else
            soundOrVibrator = 1;
        //在点击保存时再读取calendarBean中的值。
        if (calendarBean.getCalendar() == null) {
            startCalendar = DateFormatUtil.parse(mSchedule.getStartTime()).toCalendar(Locale.CHINA);
            LogUtil.d("null....");
        } else startCalendar = calendarBean.getCalendar();
        if (isTimeRight()) {
            //有闹钟删除闹钟
            List<Integer> alarmIdList = new ArrayList<Integer>();
            for (Alarm alarm : mSchedule.getAlarmList())
                alarmIdList.add(alarm.getId());
            if (mSchedule.isRemind()) {
                for (int alarmId : alarmIdList) {
                    Log.d("alarmId", "addAlarmID: " + alarmId);
                    AlarmManagerUtil.cancelAlarm(MyApplication.instance(), "com.loonggg.alarm.clock", alarmId);
                }

            }

            //删除数据库中数据
            DataSupport.delete(Schedule.class, mSchedule.getId());
            DataSupport.deleteAll(WeekSchedule.class, "scheduleId=?", mSchedule.getId() + "");
            //添加数据
            Schedule schedule = new Schedule();
            addToDataBase(schedule);
            if (switchCompat.isChecked()) addAlarm(schedule);
//            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
            if (type.equals("WeekShowActivity")) {
                startActivity(new Intent(this, MainActivity.class));
                EventBus.getDefault().post(new MsgEvent("UpDate"));
            } else if (type.equals("DayShowActivity")) {
                startActivity(new Intent(this, DatePickerActivity.class));
                EventBus.getDefault().post(new MsgEvent("UpDate"));
            } else if (type.equals("TodayShowActivity")) {
                startActivity(new Intent(this, TodayActivity.class));
                EventBus.getDefault().post(new MsgEvent("UpDate"));
            }

        } else {
            Toast.makeText(this, "提醒时间已过期", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTimeRight() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(startCalendar);
        int seconds = Seconds.secondsBetween(now, dateTime).getSeconds();
        return seconds > 0;
    }


    private void addToDataBase(Schedule schedule) {
        DateTime dateTime = new DateTime(startCalendar);
        schedule.setTitle(titleEt.getText().toString());
        schedule.setRemind(switchCompat.isChecked());
        schedule.setStartTime(DateFormatUtil.format(dateTime));
        schedule.setCycleTime(xunhuanTv.getText().toString());
        schedule.setBiaoqian(tag);
        schedule.setType(selectedIndex);
        schedule.setDetail(detailEt.getText().toString());
        schedule.setSoundOrVibrator(soundOrVibrator);
        schedule.save();
        alarmId = schedule.getId();
        for (Picture picture : photoUrls) {
            picture.setSchedule(schedule);
            picture.save();
        }
    }

    public void addAlarm(Schedule schedule) {
        if (selectedIndex == 0) {
            Alarm alarm = new Alarm();
            alarm.setSchedule(schedule);
            alarm.setName(schedule.getTitle());
            alarm.save();
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 0, startCalendar, alarm.getId(), 0, titleEt.getText().toString(), soundOrVibrator);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 1) {
            Alarm alarm = new Alarm();
            alarm.setSchedule(schedule);
            alarm.setName(schedule.getTitle());
            alarm.save();
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 1, startCalendar, alarm.getId(), 0, titleEt.getText().toString(), soundOrVibrator);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 2) {
            Alarm alarm = new Alarm();
            alarm.setSchedule(schedule);
            alarm.setName(schedule.getTitle());
            alarm.save();
            DateTime dateTime = new DateTime(startCalendar);
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 2, startCalendar, alarm.getId(), dateTime.getDayOfWeek(), titleEt.getText().toString(), soundOrVibrator);
        } else if (selectedIndex == 3) {
            for (int i = 0; i < selectedWeekdays.length; i++) {
                if (selectedWeekdays[i]) {
                    Alarm alarm = new Alarm();
                    alarm.setSchedule(schedule);
                    alarm.setName(schedule.getTitle());
                    alarm.save();
                    AlarmManagerUtil.setAlarm(MyApplication.instance(), 2, startCalendar, alarm.getId(), i + 1, titleEt.getText().toString(), soundOrVibrator);
                }
            }
        }
    }

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            List<String> permissionsNeeded = new ArrayList<String>();
            permissionsNeeded.add(Manifest.permission.CAMERA);
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]), CAMERA_PERMISSION);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            List<String> permissionsNeeded = new ArrayList<String>();
            permissionsNeeded.add(Manifest.permission.CAMERA);
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]), CAMERA_PERMISSION);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            List<String> permissionsNeeded = new ArrayList<String>();
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]), CAMERA_PERMISSION);
        } else {
            PhotoPicker.builder().setPhotoCount(4).setShowCamera(true).setShowGif(true)
                    .setPreviewEnabled(false).start(this, PhotoPicker.REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            PhotoPicker.builder().setPhotoCount(4).setShowCamera(true).setShowGif(true)
                    .setPreviewEnabled(false).start(this, PhotoPicker.REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请允许手机拍照权限", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请允许手机读写外部存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hello", resultCode + "");
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mAdapter.clear();
                photoUrls.clear();
                data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.d("hello", photoUrls.toString() + "");

                for (String s : data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)) {
                    Picture picture = new Picture(s, false);
                    photoUrls.add(picture);
                }

                mAdapter.addAll(photoUrls);
                mAdapter.add(new Picture(R.mipmap.ic_addpics, true));

            }
        }
    }

    public class ItemPresenter implements BaseViewAdapter.Presenter {
        public void addClick() {
            getPermissions();
        }


        public void previewClick() {
            stringList.clear();
            for (Picture picture : photoUrls) {
                stringList.add((String) picture.getImgRes());
            }
            PreviewActivity.actionStart(ModifyActivity.this, stringList);
        }

    }
}
