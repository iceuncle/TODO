package com.todo.ui.crud;

import android.Manifest;
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
import com.todo.ui.base.BaseActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.preview.PreviewActivity;
import com.todo.utils.DateFormatUtil;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;
import com.todo.widget.DateTimePickDialog;
import com.todo.widget.ImageButtonText;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by tianyang on 2017/2/15.
 */
public class AddActivity extends BaseActivity implements ImageButtonText.OnImageButtonTextClickListener {
    private static final int CAMERA_PERMISSION = 1;
    private TextView stText, etText, xunhuanText;
    private ImageButtonText imageButtonText1, imageButtonText2, imageButtonText3, imageButtonText4;
    private int selectedIndex = 0; //重复类型
    private int soundOrVibrator = 1;//铃声或震动设置
    private String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] types = {"只此一次", "每天", "每周", "自定义"};
    private boolean[] selectedWeekdays = new boolean[7];
    private EditText title, detailEt;
    private String biaoqian;
    private SwitchCompat naozhongSc, zhengdongSc, ringSc;
    private CalendarBean calendarBean = new CalendarBean();
    private LinearLayout soundOrVibratorView;
    private View soundOrVibratorDivider;
    private RecyclerView mRecyclerView;
    private SingleTypeAdapter<Picture> mAdapter;

    public Calendar startCalendar, endCalendar;

    //照片url
    private List<Picture> photoUrls = new ArrayList<>();

    private ArrayList<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("添加");
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

        title = (EditText) findViewById(R.id.title);

        stText = (TextView) findViewById(R.id.stText);
        etText = (TextView) findViewById(R.id.etText);
        xunhuanText = (TextView) findViewById(R.id.xunhuanText);
        naozhongSc = (SwitchCompat) findViewById(R.id.switchCompat);
        zhengdongSc = (SwitchCompat) findViewById(R.id.zhendong_sc);
        ringSc = (SwitchCompat) findViewById(R.id.ring_sc);
        detailEt = (EditText) findViewById(R.id.detail_et);
        soundOrVibratorView = (LinearLayout) findViewById(R.id.soundOrVibrator_view);
        soundOrVibratorDivider = findViewById(R.id.soundOrVibrator_divider);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new SingleTypeAdapter<>(this, R.layout.item_add_pic);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.add(new Picture(R.mipmap.ic_addpics, true));
        mAdapter.setPresenter(new ItemPresenter());

        imageButtonText1 = (ImageButtonText) findViewById(R.id.imageText1);
        imageButtonText2 = (ImageButtonText) findViewById(R.id.imageText2);
        imageButtonText3 = (ImageButtonText) findViewById(R.id.imageText3);
        imageButtonText4 = (ImageButtonText) findViewById(R.id.imageText4);
        imageButtonText2.getTextView().setText("生活");
        imageButtonText2.getImgView().setImageResource(R.mipmap.life_default);
        imageButtonText3.getTextView().setText("学习");
        imageButtonText3.getImgView().setImageResource(R.mipmap.study_default);
        imageButtonText4.getTextView().setText("其它");
        imageButtonText4.getImgView().setImageResource(R.mipmap.other_default);
        imageButtonText1.setmOnImageButtonTextClickListener(this);
        imageButtonText2.setmOnImageButtonTextClickListener(this);
        imageButtonText3.setmOnImageButtonTextClickListener(this);
        imageButtonText4.setmOnImageButtonTextClickListener(this);

        naozhongSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                save();
//                Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }


    public void startTime(View view) {
        DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(this, "", calendarBean.getCalendar());
        dateTimePickDialog.dateTimePicKDialog(stText, calendarBean);
    }

    public void endTime(View view) {
        CalendarBean calendarBean = new CalendarBean();
        DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(this, "", null);
        dateTimePickDialog.dateTimePicKDialog(etText, calendarBean);
        endCalendar = calendarBean.getCalendar();
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
                        xunhuanText.setText(types[selectedIndex]);
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
                        xunhuanText.setText(getXunhuanText(selectedWeekdays));
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


    public void save() {
        if (canSave()) {
            //获取铃声或震动设置
            if (ringSc.isChecked() && !zhengdongSc.isChecked())
                soundOrVibrator = 1;
            else if (ringSc.isChecked() && zhengdongSc.isChecked())
                soundOrVibrator = 2;
            else if (!ringSc.isChecked() && zhengdongSc.isChecked())
                soundOrVibrator = 0;
            else
                soundOrVibrator = 1;
            //在点击保存时再读取calendarBean中的值。
            startCalendar = calendarBean.getCalendar();
            //判断提醒时间是否正确
            if (isTimeRight()) {
                Schedule schedule = new Schedule();
                //存入数据库并设置闹钟
                addToDataBase(schedule);
                if (naozhongSc.isChecked()) addAlarm(schedule);
//                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new MsgEvent("UpDate"));
                finish();

            } else {
                Toast.makeText(this, "开始时间已过期", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请补全信息后添加日程", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isTimeRight() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(startCalendar);
        int seconds = Seconds.secondsBetween(now, dateTime).getSeconds();
        return seconds > 0;
    }


    public boolean canSave() {
        if (!title.getText().toString().equals("") && !stText.getText().equals("")
//                && etText.getText() != ""
                && !xunhuanText.getText().equals("") && (imageButtonText1.isChecked() || imageButtonText2.isChecked()
                || imageButtonText3.isChecked() || imageButtonText4.isChecked())) {
            return true;
        } else {
            return false;
        }
    }

    public void addAlarm(Schedule schedule) {

//        Schedule mSchedule = DataSupport.find(Schedule.class, schedule.getId());
        if (selectedIndex == 0) {
            Alarm alarm = new Alarm();
            alarm.setSchedule(schedule);
            alarm.setName(schedule.getTitle());
            alarm.save();
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 0, startCalendar, alarm.getId(), 0, title.getText().toString(), soundOrVibrator);
            Log.d("alarmId", "addAlarmID: " + alarm.getId());
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 1) {
            Alarm alarm = new Alarm();
            alarm.setName(schedule.getTitle());
            alarm.setSchedule(schedule);
            alarm.save();
            Log.d("alarmId", "addAlarmID: " + alarm.getId());
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 1, startCalendar, alarm.getId(), 0, title.getText().toString(), soundOrVibrator);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 2) {
            Alarm alarm = new Alarm();
            alarm.setName(schedule.getTitle());
            alarm.setSchedule(schedule);
            alarm.save();
            Log.d("alarmId", "addAlarmID: " + alarm.getId());
            DateTime dateTime = new DateTime(startCalendar);
            AlarmManagerUtil.setAlarm(MyApplication.instance(), 2, startCalendar, alarm.getId(), dateTime.getDayOfWeek(), title.getText().toString(), soundOrVibrator);
        } else if (selectedIndex == 3) {
            for (int i = 0; i < selectedWeekdays.length; i++) {
                if (selectedWeekdays[i]) {
                    Alarm alarm = new Alarm();
                    alarm.setName(schedule.getTitle());
                    alarm.setSchedule(schedule);
                    alarm.save();
                    schedule.getAlarmList().add(alarm);
                    Log.d("alarmId", "addAlarmID: " + alarm.getId());
                    AlarmManagerUtil.setAlarm(MyApplication.instance(), 2, startCalendar, alarm.getId(), i + 1, title.getText().toString(), soundOrVibrator);
                }
            }
        }

    }

    private void addToDataBase(Schedule schedule) {
        DateTime dateTime = new DateTime(startCalendar);
        schedule.setTitle(title.getText().toString());
        schedule.setRemind(naozhongSc.isChecked());
        schedule.setStartTime(DateFormatUtil.format(dateTime));
        schedule.setCycleTime(xunhuanText.getText().toString());
        schedule.setBiaoqian(biaoqian);
        schedule.setType(selectedIndex);
        schedule.setDetail(detailEt.getText().toString());
        schedule.setSoundOrVibrator(soundOrVibrator);
        schedule.save();
        for (Picture picture : photoUrls) {
            picture.setSchedule(schedule);
            picture.save();
        }
    }


    @Override
    public void OnImageButtonTextClick(View view) {
        switch (view.getId()) {
            case R.id.imageText1:
                if (imageButtonText1.isChecked()) {
                    imageButtonText1.setChecked(false);
                    imageButtonText1.getImgView().setImageResource(R.mipmap.work_default);
                    imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText1.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText1.setChecked(true);
                    imageButtonText1.getImgView().setImageResource(R.mipmap.work);
                    imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.work_color));
                }
                break;
            case R.id.imageText2:
                if (imageButtonText2.isChecked()) {
                    imageButtonText2.setChecked(false);
                    imageButtonText2.getImgView().setImageResource(R.mipmap.life_default);
                    imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText2.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText2.setChecked(true);
                    imageButtonText2.getImgView().setImageResource(R.mipmap.life);
                    imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.life_color));
                }
                break;
            case R.id.imageText3:
                if (imageButtonText3.isChecked()) {
                    imageButtonText3.setChecked(false);
                    imageButtonText3.getImgView().setImageResource(R.mipmap.study_default);
                    imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText3.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText3.setChecked(true);
                    imageButtonText3.getImgView().setImageResource(R.mipmap.study);
                    imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.study_color));
                }
                break;
            case R.id.imageText4:
                if (imageButtonText4.isChecked()) {
                    imageButtonText4.setChecked(false);
                    imageButtonText4.getImgView().setImageResource(R.mipmap.other_default);
                    imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText4.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText4.setChecked(true);
                    imageButtonText4.getImgView().setImageResource(R.mipmap.other);
                    imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.other_color));
                }
                break;
            default:
        }
    }

    public void resetAllImageBUttonText() {
        imageButtonText1.setChecked(false);
        imageButtonText1.getImgView().setImageResource(R.mipmap.work_default);
        imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText2.setChecked(false);
        imageButtonText2.getImgView().setImageResource(R.mipmap.life_default);
        imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText3.setChecked(false);
        imageButtonText3.getImgView().setImageResource(R.mipmap.study_default);
        imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText4.setChecked(false);
        imageButtonText4.getImgView().setImageResource(R.mipmap.other_default);
        imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.g0));
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
            PreviewActivity.actionStart(AddActivity.this, stringList);
        }

    }

//    public class ItemDecorator implements BaseViewAdapter.Decorator {
//
//        @Override
//        public void decorator(BindingViewHolder holder, int position, int viewType) {
//            ItemPicBinding binding = (ItemPicBinding) holder.getBinding();
//            Picture picture = mAdapter.getItemByPos(position);
//            if (picture.isAdd())
//                binding.getRoot().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//        }
//    }


}
