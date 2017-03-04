package com.todo.utils;

import android.util.Log;

import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;

import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/2/25.
 */
public class SchedulesUtil {
    /**
     * 判断是否在7天内
     */
    public static boolean isInWeek(Schedule schedule) {
        DateTime dateTime = DateFormatUtil.parse(schedule.getStartTime());
        return DateManageUtil.isInWeek(dateTime);
    }

    /**
     * type==1 每天型闹钟 获取7天schedule
     */
    public static List<Schedule> getWeek(Schedule schedule) {
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        List<DateTime> dateTimeList = DateManageUtil.getWeek(startTime);
        List<Schedule> scheduleList = new ArrayList<>();
        for (DateTime date : dateTimeList) {
            if (DateManageUtil.BigerThanStart(startTime, date)) {
                Schedule s = schedule.clone();
                s.setStartTime(DateFormatUtil.format(date));
                scheduleList.add(s);
            }
        }
        return scheduleList;
    }

    /**
     * type==2  每周单天型闹钟 获取7天内该周schedule;
     */
    public static Schedule getDayOfWeek(Schedule schedule) {
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        DateTime date = DateManageUtil.getDayofWeek(startTime);
        LogUtil.d(startTime.toString() + "   " + date.toString());
        if (DateManageUtil.BigerThanStart(startTime, date)) {
            Schedule s = schedule.clone();
            s.setStartTime(DateFormatUtil.format(date));
            LogUtil.d("bigger   ");
            return s;
        }
        LogUtil.d("smaller...   ");
        return null;
    }


    /**
     * type==3 每周多天型闹钟 获取7天内ScheduleList
     */
    public static List<Schedule> getDaysOfWeek(Schedule schedule) {
        List<Schedule> scheduleList = new ArrayList<>();
        List<DateTime> dateTimeList = new ArrayList<>();
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        List<Integer> integerList = parse(schedule.getCycleTime());
        for (int i : integerList) {
            dateTimeList.add(DateManageUtil.getDayofWeek(startTime, i));
        }

        for (DateTime dateTime : dateTimeList) {
            if (DateManageUtil.BigerThanStart(startTime, dateTime)) {
                Schedule s = schedule.clone();
                s.setStartTime(DateFormatUtil.format(dateTime));
                scheduleList.add(s);
            }
        }
        return scheduleList;
    }


    /**
     * 删除三日之前的所有WeekSchedule表数据
     */
    public static void deleteThreeDayBeforeDatas() {
        List<WeekSchedule> weekScheduleList = DataSupport.findAll(WeekSchedule.class);
        DateTime dateTime = DateTime.now().minusDays(3);
        int time = Integer.parseInt(DateFormatUtil.format(dateTime, "yyyyMMdd"));
        for (WeekSchedule weekSchedule : weekScheduleList) {
            DateTime date = DateFormatUtil.parse(weekSchedule.getStartTime());
            if (time > Integer.parseInt(DateFormatUtil.format(date, "yyyyMMdd")))
                DataSupport.delete(WeekSchedule.class, weekSchedule.getId());
        }
    }

    /**
     * 解析字符串，返回周几列表 例
     * 每周（周一，周二，周三）
     * 返回{1,2,3}
     */
    private static List<Integer> parse(String str) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case '一':
                    list.add(1);
                    break;
                case '二':
                    list.add(2);
                    break;
                case '三':
                    list.add(3);
                    break;
                case '四':
                    list.add(4);
                    break;
                case '五':
                    list.add(5);
                    break;
                case '六':
                    list.add(6);
                    break;
                case '日':
                    list.add(7);
                    break;
                default:
            }
        }
        return list;
    }


}
