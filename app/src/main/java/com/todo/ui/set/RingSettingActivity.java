package com.todo.ui.set;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.todo.R;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.set.adpter.RingSettingAdapter;
import com.todo.utils.LogUtil;

/**
 * Created by tianyang on 2017/3/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RingSettingActivity extends BaseActivity {
    private static final int REQUEST_CODE_RECORD_AUDIO = 20000;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 30000;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RingSettingAdapter viewPagerAdapter;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtil.d("qqq", "size   " + grantResults.length);
        switch (requestCode) {
            case REQUEST_CODE_RECORD_AUDIO:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您拒绝了授予录音权限", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    requestPermission();
                }
                break;
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您拒绝了授予读取音乐文件的权限", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    requestPermission();
                }
                break;

        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_setting);
        LogUtil.d("RingSettingActivity  onCreate...");
        if (Build.VERSION.SDK_INT >= 23)
            requestPermission();
        else
            initView();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_RECORD_AUDIO);
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        else
            initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("铃声设置");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new RingSettingAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

    }


}
