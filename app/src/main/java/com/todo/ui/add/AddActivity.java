package com.todo.ui.add;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.R;
import com.todo.ui.Receiver.OneShotAlarm;
import com.todo.ui.base.BaseActivity;
import com.todo.utils.DateTimePickDialogUtil;
import com.todo.utils.ImageButtonText;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by tianyang on 2017/2/15.
 */
public class AddActivity extends BaseActivity implements ImageButtonText.OnImageButtonTextClickListener {
    private TextView stText, etText, xunhuanText;
    private ImageButtonText imageButtonText1, imageButtonText2, imageButtonText3, imageButtonText4;
    private int selectedIndex = 0;
    private String[] weekdays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private String[] types = {"只此一次", "每天", "每周", "每月", "每年", "自定义"};
    private boolean[] selectedWeekdays = new boolean[7];
    private EditText title;

    private AlarmManager alarmManager = null;
    public static Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("添加");
        setSupportActionBar(toolbar);

        title = (EditText) findViewById(R.id.title);

        stText = (TextView) findViewById(R.id.stText);
        etText = (TextView) findViewById(R.id.etText);
        xunhuanText = (TextView) findViewById(R.id.xunhuanText);

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
        DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(this, "");
        dateTimePickDialogUtil.dateTimePicKDialog(stText);

    }

    public void endTime(View view) {
        DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(this, "");
        dateTimePickDialogUtil.dateTimePicKDialog(etText);
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
                        if (selectedIndex == 5) showWeekDialog();
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
            //存入数据库并设置闹钟
            addAlarm();
            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请补全信息后添加日程", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean canSave() {
        if (!title.getText().toString().equals("") && stText.getText() != ""
//                && etText.getText() != ""
                && xunhuanText.getText() != " " && (imageButtonText1.isChecked() || imageButtonText2.isChecked()
                || imageButtonText3.isChecked() || imageButtonText4.isChecked())) {
            return true;
        } else {
            return false;
        }
    }

    public void addAlarm() {
//        Intent intent = new Intent(this, OneShotAlarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        Log.d("qqq", "canlendar1   " + cal.getTime());
        Log.d("qqq", "hour: " + cal.get(Calendar.HOUR_OF_DAY) + "  minute:  " + cal.get(Calendar.MINUTE));
//        AlarmManagerUtil.setAlarm(this, 0, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0, 0, "闹钟响了", 1);
//        finish();
        DateTime dateTime = new DateTime(cal);
        String string_u = dateTime.toString("yyyy/MM/dd");
        Log.d("qqq", dateTime.getYear() + "  " + dateTime.getMonthOfYear() + dateTime.getDayOfWeek());
        Log.d("qqq", dateTime.getDayOfMonth() + "  " + dateTime.getDayOfWeek() + "  " + dateTime.getDayOfYear());
        Log.d("qqq", dateTime.getHourOfDay() + "  " + dateTime.getMinuteOfHour());

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


//    public static void main(String[] args)
//    {
//        int num = 19;
//        String binaryString = Integer.toBinaryString(num);
//        System.out.println(binaryString);
//        for (int i = 0; i < binaryString.getBytes().length; i++)
//        {
//            System.out.print(get(num, i) + "\t");
//        }
//    }
//
//    /**
//     * @param num:要获取二进制值的数
//     * @param index:倒数第一位为0，依次类推
//     */
//    public static int get(int num, int index)
//    {
//        return (num & (0x1 << index)) >> index;
//    }
}
