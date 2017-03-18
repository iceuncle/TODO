package com.todo.data.database;

import org.litepal.crud.DataSupport;

/**
 * Created by tianyang on 2017/3/17.
 */
public class Alarm extends DataSupport {
    private int id;
    private String name;
    private Schedule schedule;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
