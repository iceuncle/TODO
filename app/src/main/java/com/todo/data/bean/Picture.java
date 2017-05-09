package com.todo.data.bean;

import com.todo.data.database.Schedule;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by tianyang on 2017/5/9.
 */

public class Picture extends DataSupport implements Serializable{

    private int imgId;
    private String imgRes;
    private boolean isAdd;
    private Schedule schedule;

    public Picture(int imgId, boolean isAdd) {
        this.imgId = imgId;
        this.isAdd = isAdd;
    }

    public Picture() {

    }


    public Picture(String imgRes, boolean isAdd) {
        this.imgRes = imgRes;
        this.isAdd = isAdd;
    }

    public Object getImgRes() {
        return imgRes;
    }

    public void setImgRes(String imgRes) {
        this.imgRes = imgRes;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
