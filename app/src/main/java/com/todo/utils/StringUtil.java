package com.todo.utils;

import com.todo.R;

import org.joda.time.DateTime;

/**
 * Created by tianyang on 2017/4/13.
 */

public class StringUtil {
    public static boolean IsEmpty(String str) {
        return str == null || str.equals("") || str.length() == 0;
    }

    //格式化时长
    public static String durationFormat(long time) {
        long secondsTotal = time / 1000;
        long minute = secondsTotal / 60;
        long seconds = secondsTotal % 60;
        return minute + "分" + seconds + "秒";
    }

    //获取标签图片
    public static int getIcon(String biaoqian) {
        if (biaoqian.equals("工作")) {
            return R.mipmap.work;
        } else if (biaoqian.equals("学习")) {
            return R.mipmap.study;
        } else if (biaoqian.equals("生活")) {
            return R.mipmap.life;
        } else
            return R.mipmap.other;
    }


    //获取今日时间字符串
    public static String getTodayTimeStr(String time) {
        return "今日" + time.substring(time.length() - 5, time.length());
    }

    //获取本周几时间字符串
    public static String getWeekTimeStr(String time) {
        DateTime startTime = DateFormatUtil.parse(time);
        int num = startTime.getDayOfWeek();
        String string;
        switch (num) {
            case 1:
                string = "周一" + time.substring(time.length() - 5, time.length());
                break;
            case 2:
                string = "周二" + time.substring(time.length() - 5, time.length());
                break;
            case 3:
                string = "周三" + time.substring(time.length() - 5, time.length());
                break;
            case 4:
                string = "周四" + time.substring(time.length() - 5, time.length());
                break;
            case 5:
                string = "周五" + time.substring(time.length() - 5, time.length());
                break;
            case 6:
                string = "周六" + time.substring(time.length() - 5, time.length());
                break;
            default:
                string = "周日" + time.substring(time.length() - 5, time.length());
                break;
        }
        return string;
    }

}
