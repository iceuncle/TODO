package com.todo.utils;

/**
 * Created by tianyang on 2017/4/13.
 */

public class StringUtil {
    //格式化时长
    public static String durationFormat(long time) {
        long secondsTotal = time / 1000;
        long minute = secondsTotal / 60;
        long seconds = secondsTotal % 60;
        return minute + "分" + seconds + "秒";
    }

}
