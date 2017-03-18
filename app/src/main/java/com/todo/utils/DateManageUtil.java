package com.todo.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/2/25.
 */
public class DateManageUtil {
    /**
     * 判断是否在7天内
     */
    public static boolean isInWeek(DateTime dateTime) {
        List<DateTime> list = new ArrayList<>();
        DateTime now = DateTime.now();
        list.add(now);
        for (int i = 1; i < 4; i++) {
            list.add(now.plusDays(i));
            list.add(now.minusDays(i));
        }
        String dateTimeStr = DateFormatUtil.format(dateTime, "yyyy年MM月dd日");
        List<String> stringList = new ArrayList<>();
        for (DateTime date : list) {
            stringList.add(DateFormatUtil.format(date, "yyyy年MM月dd日"));
        }
        for (String s : stringList) {
            if (s.equals(dateTimeStr))
                return true;
        }
        return false;
    }

    /**
     * 修改datetime为7天
     */
    public static List<DateTime> getWeek(DateTime dateTime) {
        List<DateTime> list = new ArrayList<>();
        DateTime now = DateTime.now();
        list.add(now);
        for (int i = 1; i < 4; i++) {
            list.add(now.plusDays(i));
            list.add(now.minusDays(i));
        }
        String dateTimeStr = DateFormatUtil.format(dateTime, "HH:mm");
        List<String> stringList = new ArrayList<>();
        List<DateTime> dateList = new ArrayList<>();
        for (DateTime date : list) {
            String s = DateFormatUtil.format(date, "yyyy年MM月dd日 " + dateTimeStr);
            stringList.add(s);
            dateList.add(DateFormatUtil.parse(s));
//            System.out.println(s);
        }
        return dateList;
    }

    /**
     * 返回7天内周几的日期
     */
    public static DateTime getDayofWeek(DateTime startTime) {
        int num = startTime.getDayOfWeek();
        String dateTimeStr = DateFormatUtil.format(startTime, "HH:mm");
        List<DateTime> list = new ArrayList<>();
        DateTime now = DateTime.now();
        list.add(now);
        for (int i = 1; i < 4; i++) {
            list.add(now.plusDays(i));
            list.add(now.minusDays(i));
        }
        for (DateTime date : list) {
            if (date.getDayOfWeek() == num) {
                String s = DateFormatUtil.format(date, "yyyy年MM月dd日 " + dateTimeStr);
                return DateFormatUtil.parse(s);
            }
        }
        return null;
    }


    public static DateTime getDayofWeek(DateTime startTime, int num) {
        String dateTimeStr = DateFormatUtil.format(startTime, "HH:mm");
        List<DateTime> list = new ArrayList<>();
        DateTime now = DateTime.now();
        list.add(now);
        for (int i = 1; i < 4; i++) {
            list.add(now.plusDays(i));
            list.add(now.minusDays(i));
        }
        for (DateTime date : list) {
            if (date.getDayOfWeek() == num) {
                String s = DateFormatUtil.format(date, "yyyy年MM月dd日 " + dateTimeStr);
                return DateFormatUtil.parse(s);
            }
        }
        return null;
    }


    /**
     * 判断是否比startTime大 minutes
     *
     * @param dateTime
     * @return
     */
    public static boolean BigerThanStart(DateTime startTime, DateTime dateTime) {
        int minutes = Minutes.minutesBetween(startTime, dateTime).getMinutes();
        return minutes >= 0;
    }

    /**
     * 判断是否比startTime大 days
     *
     * @param startTime
     * @param dateTime
     * @return
     */
    public static boolean BigerThanStart2(DateTime startTime, DateTime dateTime) {
        int days = Days.daysBetween(startTime, dateTime).getDays();
        return days >= 0;
    }


    public static List<DateTime> getMonthDays(int year, int month) {
        List<DateTime> list = new ArrayList<>();
        DateTime dateTime = new DateTime(year, month, 1, 0, 0);
        int count = dateTime.dayOfMonth().getMaximumValue();
        for (int i = 1; i <= count; i++) {
            list.add(new DateTime(year, month, i, 0, 0));
        }
        return list;
    }


    /**
     * 判断daytime是否在dateTimeList中
     */
    public static boolean isInDateTimeList(DateTime dayTime, List<DateTime> dateTimeList) {
        String dateTimeStr = DateFormatUtil.format(dayTime, "yyyy年MM月dd日");
        List<String> stringList = new ArrayList<>();
        for (DateTime date : dateTimeList) {
            stringList.add(DateFormatUtil.format(date, "yyyy年MM月dd日"));
        }
        for (String s : stringList) {
            if (s.equals(dateTimeStr))
                return true;
        }
        return false;
    }


}
