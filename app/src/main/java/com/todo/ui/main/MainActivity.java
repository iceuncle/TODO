package com.todo.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;
import com.todo.ui.main.adpters.ViewPagerAdapter;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.crud.AddActivity;
import com.todo.ui.datepicker.DatePickerActivity;
import com.todo.ui.set.RingSettingActivity;
import com.todo.ui.thisweek.ThisWeekActivity;
import com.todo.ui.today.TodayActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DataSupport.deleteAll(Schedule.class);
//        DataSupport.deleteAll(WeekSchedule.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("近期行程");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
//        tabLayout.addTab(tabLayout.newTab().setText("待办"));
//        tabLayout.addTab(tabLayout.newTab().setText("完成"));
//        tabLayout.addTab(tabLayout.newTab().setText("过期"));

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(null);
        fragmentList.add(null);
        fragmentList.add(null);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rili:
                Intent intent = new Intent(MainActivity.this, DatePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.music:
                startActivity(new Intent(MainActivity.this, RingSettingActivity.class));
                break;
            case R.id.today:
                startActivity(new Intent(MainActivity.this, TodayActivity.class));
                break;
            case R.id.thisWeek:
                startActivity(new Intent(MainActivity.this, ThisWeekActivity.class));
                break;
        }
        return true;
    }

    public void Add(View view) {

//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(AlarmManagerUtil.ALARM_ACTION);
//
//        long intervalMillis = 60000;
//        intent.putExtra("msg", "测试");
//        intent.putExtra("intervalMillis", intervalMillis);
//        intent.putExtra("id", 1111);
//        intent.putExtra("soundOrVibrator", 1);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 1111, intent, PendingIntent
//                .FLAG_CANCEL_CURRENT);
//
////        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, sender);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000,
//                    sender);

    }


}
