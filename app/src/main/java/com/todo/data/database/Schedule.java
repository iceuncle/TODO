package com.todo.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by tianyang on 2017/2/15.
 */
public class Schedule extends DataSupport implements Parcelable {
    private int id;

    /**
     * 是否开启闹钟提示
     */
    private boolean isRemind;

    /**
     * 是否完成
     */
    private boolean isFinished;

    /**
     * 标题文字
     */
    private String title;

    /**
     * 提醒时间
     */
    private String startTime;

    /**
     * type = 0 表示一次性的闹钟
     * type = 1 表示每天提醒的闹钟
     * type = 2 表示按周每周提醒的闹钟
     */
    private int type;



    /**
     * 循环类型文字
     */
    private String cycleTime;

    /**
     * 标签文字
     */
    private String biaoqian;

    public Schedule() {
    }

    protected Schedule(Parcel in) {
        id = in.readInt();
        isRemind = in.readByte() != 0;
        isFinished = in.readByte() != 0;
        title = in.readString();
        startTime = in.readString();
        type = in.readInt();
        cycleTime = in.readString();
        biaoqian = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeByte((byte) (isRemind ? 1 : 0));
        parcel.writeByte((byte) (isFinished ? 1 : 0));
        parcel.writeString(title);
        parcel.writeString(startTime);
        parcel.writeInt(type);
        parcel.writeString(cycleTime);
        parcel.writeString(biaoqian);
    }
}
