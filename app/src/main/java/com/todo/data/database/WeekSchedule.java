package com.todo.data.database;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by tianyang on 2017/2/26.
 */
public class WeekSchedule extends Schedule implements Serializable {
    private int id;
    private int scheduleId;

    public void setSchedule(Schedule schedule) {
        this.scheduleId = schedule.getId();
        setRemind(schedule.isRemind());
        setTitle(schedule.getTitle());
        setStartTime(schedule.getStartTime());
        setType(schedule.getType());
        setCycleTime(schedule.getCycleTime());
        setBiaoqian(schedule.getBiaoqian());
        setDetail(schedule.getDetail());
        setSoundOrVibrator(schedule.getSoundOrVibrator());
    }

    public int getScheduleId() {
        return scheduleId;
    }

    @Override
    public int getId() {
        return id;
    }
}
