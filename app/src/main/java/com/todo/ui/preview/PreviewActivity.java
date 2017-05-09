package com.todo.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.todo.R;
import com.todo.data.bean.Picture;
import com.todo.databinding.ActivityPreviewBinding;
import com.todo.ui.base.BaseActivity;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/5/9.
 */

public class PreviewActivity extends BaseActivity {
    private static final String PREVIEW_EXTRA = "PREVIEW_EXTRA";
    private List<String> stringList = new ArrayList<>();
    private ActivityPreviewBinding mBinding;
    private SingleTypeAdapter<Picture> mAdapter;

    private List<Picture> pictureList = new ArrayList<>();

    public static void actionStart(Context context, ArrayList<String> stringList) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putStringArrayListExtra(PREVIEW_EXTRA, stringList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview);
        initToorBar();
        stringList = getIntent().getStringArrayListExtra(PREVIEW_EXTRA);
        mBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new SingleTypeAdapter<Picture>(this, R.layout.item_preview_pic);
        mBinding.recyclerView.setAdapter(mAdapter);

        for (String s : stringList) {
            File file = new File(s);
            if (file.exists()) {
                pictureList.add(new Picture(s, false));
            }
        }
        mAdapter.addAll(pictureList);

    }


    private void initToorBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("预览");
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
