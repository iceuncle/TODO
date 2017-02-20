package com.todo.ui.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.todo.R;
import com.todo.data.database.Schedule;
import com.todo.ui.base.BaseActivity;
import com.todo.utils.LogUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by tianyang on 2017/2/19.
 */
public class ShowActivity extends BaseActivity {
    private Schedule schedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        schedule = getIntent().getParcelableExtra("schedulaData");
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
}
