package com.todo.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by tianyang on 2017/2/18.
 */
public class DateFormatUtil {
    public static String string = "yyyy年MM月dd日 HH:mm";
//    public static String string1 = "yyyy-MM-dd HH:mm:ss";

    public static DateTime parse(String a) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(string);
        return DateTime.parse(a, dateTimeFormatter);
    }

    public static DateTime parse(String a, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return DateTime.parse(a, dateTimeFormatter);
    }

    public static String format(DateTime dateTime) {
        return dateTime.toString(string);
    }

    public static String format(DateTime dateTime, String format) {
        return dateTime.toString(format);
    }


    public static DateTime parseDate(String a) {
        String[] strings = a.split("-");
        int year = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]);
        int day = Integer.parseInt(strings[2]);
        DateTime dateTime = new DateTime(year, month, day, 0, 0);
        return dateTime;
    }


}
