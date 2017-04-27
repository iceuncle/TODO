package com.todo.utils;

import android.util.Log;

import com.todo.data.database.Schedule;
import com.todo.data.database.WeekSchedule;

import org.joda.time.DateTime;
import org.joda.time.Days;
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
                Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
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
            Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
            s.setStartTime(DateFormatUtil.format(date));
            return s;
        }
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
                Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
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


    /**
     * 判断Schedule是否在该月中
     *
     * @param monthList
     * @param schedule
     * @return
     */
    public static boolean isInMonth(List<DateTime> monthList, Schedule schedule) {
        DateTime dateTime = DateFormatUtil.parse(schedule.getStartTime());
        DateTime date = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
        for (DateTime d : monthList) {
            if (d.equals(date)) return true;
        }
        return false;
    }


    /**
     * 根据类型返回Schedule在该月中重复的天数
     *
     * @param monnthList
     * @param type       1 每天  2 每周  3 自定义每周
     * @return
     */
    public static List<DateTime> getDaysInMonth(List<DateTime> monnthList, Schedule schedule, int type) {
        List<DateTime> dateTimeList = new ArrayList<>();
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());

        if (type == 1) {
            for (DateTime dateTime : monnthList) {
                if (DateManageUtil.BigerThanStart2(startTime, dateTime))
                    dateTimeList.add(dateTime);
            }
        } else if (type == 2) {
            int num = startTime.getDayOfWeek();
            for (DateTime dateTime : monnthList) {
                if (DateManageUtil.BigerThanStart2(startTime, dateTime) && dateTime.getDayOfWeek() == num)
                    dateTimeList.add(dateTime);
            }
        } else if (type == 3) {
            List<Integer> integerList = parse(schedule.getCycleTime());
            for (int i : integerList) {
                for (DateTime dateTime : monnthList) {
                    if (DateManageUtil.BigerThanStart2(startTime, dateTime) && dateTime.getDayOfWeek() == i)
                        dateTimeList.add(dateTime);
                }
            }
        }
        return dateTimeList;
    }


    /**
     * 获取该日的Schedule
     *
     * @param schedule
     * @param type
     * @param dayTime
     * @return
     */
    public static Schedule getSheduleInDay(Schedule schedule, DateTime dayTime, int type) {
        DateTime time = DateFormatUtil.parse(schedule.getStartTime());
        String str = DateFormatUtil.format(time, "yyyy年MM月dd日");
        DateTime startTime = DateFormatUtil.parse(str, "yyyy年MM月dd日");

        int days = Days.daysBetween(startTime, dayTime).getDays();
        if (type == 0) {
            if (days == 0) {
                return GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
            }
        } else if (type == 1) {
            if (days >= 0) {
                Log.d("days", "days type1   :" + days);
                String dateTimeStr = DateFormatUtil.format(time, "HH:mm");
                String s = DateFormatUtil.format(dayTime, "yyyy年MM月dd日 " + dateTimeStr);
                Schedule schedule1 = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
                schedule1.setStartTime(s);
                return schedule1;
            }
        } else if (type == 2) {
            if (days >= 0 && dayTime.getDayOfWeek() == startTime.getDayOfWeek()) {
                String dateTimeStr = DateFormatUtil.format(time, "HH:mm");
                String s = DateFormatUtil.format(dayTime, "yyyy年MM月dd日 " + dateTimeStr);
                Schedule schedule1 = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
                schedule1.setStartTime(s);
                return schedule1;
            }
        } else if (type == 3) {
            List<Integer> integerList = parse(schedule.getCycleTime());
            for (int i : integerList) {
                if (days >= 0 && dayTime.getDayOfWeek() == i) {
                    String dateTimeStr = DateFormatUtil.format(time, "HH:mm");
                    String s = DateFormatUtil.format(dayTime, "yyyy年MM月dd日 " + dateTimeStr);
                    Schedule schedule1 = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
                    schedule1.setStartTime(s);
                    return schedule1;
                }
            }
        }
        return null;
    }


    /**
     * 判断是否在本周内
     */
    public static boolean isInThisWeek(Schedule schedule) {
        DateTime dateTime = DateFormatUtil.parse(schedule.getStartTime());
        return DateManageUtil.isInThisWeek(dateTime);
    }


    /**
     * type==1 每天型闹钟 获取本周schedule
     */
    public static List<Schedule> getThisWeek(Schedule schedule) {
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        List<DateTime> dateTimeList = DateManageUtil.getThisWeek(startTime);
        List<Schedule> scheduleList = new ArrayList<>();
        for (DateTime date : dateTimeList) {
            if (DateManageUtil.BigerThanStart(startTime, date)) {
                Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
                s.setStartTime(DateFormatUtil.format(date));
                scheduleList.add(s);
            }
        }
        return scheduleList;
    }

    /**
     * type==2  每周单天型闹钟 获取本周内该周schedule;
     */
    public static Schedule getScheduleOfThisWeek(Schedule schedule) {
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        DateTime date = DateManageUtil.getDayofThisWeek(startTime);
//        LogUtil.d(startTime.toString() + "   " + date.toString());
        if (DateManageUtil.BigerThanStart(startTime, date)) {
            Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
            s.setStartTime(DateFormatUtil.format(date));
            return s;
        }
        return null;
    }


    /**
     * type==3 每周多天型闹钟 获取本周内ScheduleList
     */
    public static List<Schedule> getSchedulesOfWeek(Schedule schedule) {
        List<Schedule> scheduleList = new ArrayList<>();
        List<DateTime> dateTimeList = new ArrayList<>();
        DateTime startTime = DateFormatUtil.parse(schedule.getStartTime());
        List<Integer> integerList = parse(schedule.getCycleTime());
        for (int i : integerList) {
            dateTimeList.add(DateManageUtil.getDayofThisWeek(startTime, i));
        }

        for (DateTime dateTime : dateTimeList) {
            if (DateManageUtil.BigerThanStart(startTime, dateTime)) {
                Schedule s = GsonUtil.parse(GsonUtil.toJson(schedule), Schedule.class);
                s.setStartTime(DateFormatUtil.format(dateTime));
                scheduleList.add(s);
            }
        }
        return scheduleList;
    }


}
