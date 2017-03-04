package com.todo.data.database;

import org.litepal.crud.DataSupport;

/**
 * Created by tianyang on 2017/2/26.
 */
public class WeekSchedule extends DataSupport {

    private int id;

    private int scheduleId;

    private boolean isRemind;

    private boolean isFinished;

    private String title;

    private String startTime;

    private int type;

    private String cycleTime;

    private String biaoqian;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBiaoqian() {
        return biaoqian;
    }

    public void setBiaoqian(String biaoqian) {
        this.biaoqian = biaoqian;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }


    public void setSchedule(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.isRemind = schedule.isRemind();
        this.title = schedule.getTitle();
        this.startTime = schedule.getStartTime();
        this.type = schedule.getType();
        this.cycleTime = schedule.getCycleTime();
        this.biaoqian = schedule.getBiaoqian();
    }

}
