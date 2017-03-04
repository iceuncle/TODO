package com.todo.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

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


}
