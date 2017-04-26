package com.todo.ui.base;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.todo.ui.base.EmptyState.EMPTY_DAIBAN_SCHEDULE;
import static com.todo.ui.base.EmptyState.EMPTY_DATA;
import static com.todo.ui.base.EmptyState.EMPTY_GUOQI_SCHEDULE;
import static com.todo.ui.base.EmptyState.EMPTY_KIND_SCHEDULE;
import static com.todo.ui.base.EmptyState.EMPTY_MUSIC;
import static com.todo.ui.base.EmptyState.EMPTY_RECORDER;
import static com.todo.ui.base.EmptyState.EMPTY_WANCHENG_SCHEDULE;
import static com.todo.ui.base.EmptyState.NORMAL;
import static com.todo.ui.base.EmptyState.PROGRESS;


/**
 * 页面描述：空状态
 * <p>
 * Created by ditclear on 2017/2/24.
 */
@IntDef({NORMAL, PROGRESS, EMPTY_DATA, EMPTY_MUSIC, EMPTY_RECORDER, EMPTY_KIND_SCHEDULE,
        EMPTY_DAIBAN_SCHEDULE, EMPTY_GUOQI_SCHEDULE, EMPTY_WANCHENG_SCHEDULE})
@Retention(RetentionPolicy.SOURCE)
public @interface EmptyState {

    int NORMAL = -1;  //正常
    int PROGRESS = -2;//显示进度条

    int EMPTY_DATA = 11111; //行程为空
    int EMPTY_MUSIC = 22222; //音乐为空
    int EMPTY_RECORDER = 33333; //录音为空
    int EMPTY_KIND_SCHEDULE = 44444; //行程为空
    int EMPTY_DAIBAN_SCHEDULE = 55555; //待办行程为空
    int EMPTY_GUOQI_SCHEDULE = 66666; //过期行程为空
    int EMPTY_WANCHENG_SCHEDULE = 77777; //已完成行程为空

}
