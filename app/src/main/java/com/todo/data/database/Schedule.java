package com.todo.data.database;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by tianyang on 2017/2/15.
 */
public class Schedule {
    private int id;

    private boolean isRemind;

    private String title;

    private DateTime startTime;

    private String endTime;

    private int type;

    private int whichDay;

    private int biaoqian;
}
