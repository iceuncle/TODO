package com.todo.utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/2/25.
 */
public class Test {

    static class Bean implements Cloneable {
        public Bean() {
        }

        public Bean(int i, String a) {
            this.i = i;
            this.a = a;
        }

        int i;
        String a;

        public Bean clone() {
            Bean o = null;
            try {
                o = (Bean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }

    }

    public static void main(String[] args) {
//        boolean b;
//        b = DateManageUtil.isInWeek(DateTime.now().plusDays(1));
//        System.out.println(b);
//        b = DateManageUtil.isInWeek(DateTime.now().plusDays(2));
//        System.out.println(b);
//        b = DateManageUtil.isInWeek(DateTime.now().plusDays(3));
//        System.out.println(b);
//        b = DateManageUtil.isInWeek(DateTime.now().plusDays(4));
//        System.out.println(b);
//        b = DateManageUtil.isInWeek(DateTime.now().plusDays(5));
//        System.out.println(b);
//        b = DateManageUtil.isInWeek(DateTime.now().minusDays(5));
//        System.out.println(b);
//        DateManageUtil.getWeek(DateTime.now());
//        DateManageUtil.getDayofWeek(DateTime.now().getDayOfWeek()-3);
//        DateManageUtil.getDayofWeek(DateTime.now().getDayOfWeek()+1);
//        DateManageUtil.getDayofWeek(DateTime.now().getDayOfWeek()-2);
//        DateManageUtil.getDayofWeek(DateTime.now().getDayOfWeek()+2);

//        Bean bean = new Bean(2, "33");
//        Bean bean1;
//        bean1 = bean.clone();
//        bean1.a = "555";
//        System.out.println(bean.a);
//        System.out.println(bean1.a);

//        List<Integer> list = new ArrayList<>();
//        list = SchedulesUtil.parse("每周（周一，周二，周三，周四，周五，周六，周日）");
//        for (int i : list)
//            System.out.println(i);
//        List<Integer> list = new ArrayList<>();
//        List<Integer> list1 = new ArrayList<>();
//        list.add(1);
//        list.add(1);
//        list.add(1);
//        list1.add(2);
//        list1.add(2);
//        list1.add(2);
//        list1.addAll(list);
//        for (int i : list1)
//            System.out.println(i);
//        SchedulesUtil.main();
//        boolean b = DateManageUtil.isFuture(DateTime.now().minusDays(1));
//        System.out.println(b);
//        boolean b1 = DateManageUtil.isFuture(DateTime.now().plusDays(7).plusMinutes(2));
//        System.out.println(b1);
//        boolean b2 = DateManageUtil.isFuture(DateTime.now().plusDays(7));
//        System.out.println(b2);
//        boolean b3 = DateManageUtil.isFuture(DateTime.now().plusDays(4));
//        System.out.println(b3);

//        String year = DateTime.now().getYear() + "";
//        String month = DateTime.now().getMonthOfYear() + "";
//        String day = DateTime.now().getDayOfMonth() + "";
//        System.out.println(year + "-" + month + "-" + day);

//        int i = DateManageUtil.getMonthDays(2017, 2);
//        System.out.println(i);
//        int a = DateManageUtil.getMonthDays(2017, 3);
//        System.out.println(a);

        DateTime dateTime1 = new DateTime(2017, 2, 3, 0, 0);
        DateTime dateTime2 = new DateTime(2017, 2, 3, 0, 0);
        DateTime dateTime3 = new DateTime(2017, 2, 5, 0, 0);
        System.out.println(dateTime1.equals(dateTime2));
        System.out.println(dateTime1.equals(dateTime3));
    }


}
