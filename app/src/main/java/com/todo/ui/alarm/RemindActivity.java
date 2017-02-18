package com.todo.ui.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.todo.ui.base.BaseActivity;

/**
 * Created by tianyang on 2017/2/17.
 */
public class RemindActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AlertDialog.Builder(this).
                setTitle("闹钟").//设置标题
                setMessage("时间到了！").//设置内容
                setPositiveButton("知道了",
                new DialogInterface.OnClickListener() {//设置按钮
                    public void onClick(DialogInterface dialog, int which) {
                        finish();//关闭Activity
                    }
                }).create().show();
    }
}
