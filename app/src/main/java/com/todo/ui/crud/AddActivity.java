package com.todo.ui.crud;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.R;
import com.todo.data.bean.CalendarBean;
import com.todo.data.database.Schedule;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.event.MsgEvent;
import com.todo.ui.main.MainActivity;
import com.todo.utils.DateFormatUtil;
import com.todo.utils.DateTimePickDialogUtil;
import com.todo.utils.ImageButtonText;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.Calendar;

/**
 * Created by tianyang on 2017/2/15.
 */
public class AddActivity extends BaseActivity implements ImageButtonText.OnImageButtonTextClickListener {
    private TextView stText, etText, xunhuanText;
    private ImageButtonText imageButtonText1, imageButtonText2, imageButtonText3, imageButtonText4;
    private int selectedIndex = 0; //重复类型
    private String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] types = {"只此一次", "每天", "每周", "自定义"};
    private boolean[] selectedWeekdays = new boolean[7];
    private EditText title;
    private String biaoqian;
    private SwitchCompat naozhong;
    private int alarmId = 0;  //存入数据库的id同样设置闹钟id
    private CalendarBean calendarBean;

    public Calendar startCalendar, endCalendar;

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
        naozhong = (SwitchCompat) findViewById(R.id.switchCompat);

        imageButtonText1 = (ImageButtonText) findViewById(R.id.imageText1);
        imageButtonText2 = (ImageButtonText) findViewById(R.id.imageText2);
        imageButtonText3 = (ImageButtonText) findViewById(R.id.imageText3);
        imageButtonText4 = (ImageButtonText) findViewById(R.id.imageText4);
        imageButtonText2.getTextView().setText("生活");
        imageButtonText2.getImgView().setImageResource(R.mipmap.shenghuo);
        imageButtonText3.getTextView().setText("学习");
        imageButtonText3.getImgView().setImageResource(R.mipmap.xuexi);
        imageButtonText4.getTextView().setText("其它");
        imageButtonText4.getImgView().setImageResource(R.mipmap.qita);
        imageButtonText1.setmOnImageButtonTextClickListener(this);
        imageButtonText2.setmOnImageButtonTextClickListener(this);
        imageButtonText3.setmOnImageButtonTextClickListener(this);
        imageButtonText4.setmOnImageButtonTextClickListener(this);

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
        calendarBean = new CalendarBean();
        DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(this, "");
        dateTimePickDialogUtil.dateTimePicKDialog(stText, calendarBean);

    }

    public void endTime(View view) {
        CalendarBean calendarBean = new CalendarBean();
        DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(this, "");
        dateTimePickDialogUtil.dateTimePicKDialog(etText, calendarBean);
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
            //在点击保存时再读取calendarBean中的值。
            startCalendar = calendarBean.getCalendar();
            //判断提醒时间是否正确
            if (isTimeRight()) {
                //存入数据库并设置闹钟
                addToDataBase();
                if (naozhong.isChecked()) addAlarm();
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new MsgEvent("AddActivity"));
                finish();

            } else {
                Toast.makeText(this, "提醒时间已过期", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请补全信息后添加日程", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isTimeRight() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(startCalendar);
        int minutes = Minutes.minutesBetween(now, dateTime).getMinutes();
        return minutes > 0;
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

    public void addAlarm() {
        if (selectedIndex == 0) {
            AlarmManagerUtil.setAlarm(this, 0, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), alarmId, 0, title.getText().toString(), 1);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 1) {
            AlarmManagerUtil.setAlarm(this, 1, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), alarmId, 0, title.getText().toString(), 1);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (selectedIndex == 2) {
            DateTime dateTime = new DateTime(startCalendar);
            AlarmManagerUtil.setAlarm(this, 2, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), alarmId, dateTime.getDayOfWeek(), title.getText().toString(), 1);
        } else if (selectedIndex == 3) {
            for (int i = 0; i < selectedWeekdays.length; i++) {
                if (selectedWeekdays[i]) {
                    AlarmManagerUtil.setAlarm(this, 2, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), alarmId, i + 1, title.getText().toString(), 1);
                }
            }
        }
    }

    private void addToDataBase() {
        DateTime dateTime = new DateTime(startCalendar);
        Schedule schedule = new Schedule();
        schedule.setTitle(title.getText().toString());
        schedule.setRemind(naozhong.isChecked());
        schedule.setStartTime(DateFormatUtil.format(dateTime));
        schedule.setCycleTime(xunhuanText.getText().toString());
        schedule.setBiaoqian(biaoqian);
        schedule.setType(selectedIndex);
        schedule.save();
        alarmId = schedule.getId();
        Log.d("qqq", "startime " + DateFormatUtil.format(dateTime));

//        List<Schedule> scheduls = DataSupport.findAll(Schedule.class);
//        for(Schedule s: scheduls){
//            Log.d("qqq","lala   "+DateFormatUtil.parse(s.getStartTime()).toString(DateFormatUtil.string));
//        }

    }


    @Override
    public void OnImageButtonTextClick(View view) {
        switch (view.getId()) {
            case R.id.imageText1:
                if (imageButtonText1.isChecked()) {
                    imageButtonText1.setChecked(false);
                    imageButtonText1.getImgView().setImageResource(R.mipmap.gongzuo);
                    imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText1.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText1.setChecked(true);
                    imageButtonText1.getImgView().setImageResource(R.mipmap.gongzuo1);
                    imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.b0));
                }
                break;
            case R.id.imageText2:
                if (imageButtonText2.isChecked()) {
                    imageButtonText2.setChecked(false);
                    imageButtonText2.getImgView().setImageResource(R.mipmap.shenghuo);
                    imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText2.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText2.setChecked(true);
                    imageButtonText2.getImgView().setImageResource(R.mipmap.shenghuo1);
                    imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.b0));
                }
                break;
            case R.id.imageText3:
                if (imageButtonText3.isChecked()) {
                    imageButtonText3.setChecked(false);
                    imageButtonText3.getImgView().setImageResource(R.mipmap.xuexi);
                    imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText3.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText3.setChecked(true);
                    imageButtonText3.getImgView().setImageResource(R.mipmap.xuexi1);
                    imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.b0));
                }
                break;
            case R.id.imageText4:
                if (imageButtonText4.isChecked()) {
                    imageButtonText4.setChecked(false);
                    imageButtonText4.getImgView().setImageResource(R.mipmap.qita);
                    imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.g0));
                } else {
                    biaoqian = imageButtonText4.getTextView().getText().toString();
                    resetAllImageBUttonText();
                    imageButtonText4.setChecked(true);
                    imageButtonText4.getImgView().setImageResource(R.mipmap.qita1);
                    imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.b0));
                }
                break;
            default:
        }
    }

    public void resetAllImageBUttonText() {
        imageButtonText1.setChecked(false);
        imageButtonText1.getImgView().setImageResource(R.mipmap.gongzuo);
        imageButtonText1.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText2.setChecked(false);
        imageButtonText2.getImgView().setImageResource(R.mipmap.shenghuo);
        imageButtonText2.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText3.setChecked(false);
        imageButtonText3.getImgView().setImageResource(R.mipmap.xuexi);
        imageButtonText3.getTextView().setTextColor(getResources().getColor(R.color.g0));
        imageButtonText4.setChecked(false);
        imageButtonText4.getImgView().setImageResource(R.mipmap.qita);
        imageButtonText4.getTextView().setTextColor(getResources().getColor(R.color.g0));
    }


}
