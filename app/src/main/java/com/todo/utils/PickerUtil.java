package com.todo.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by tianyang on 2017/3/5.
 */
public class PickerUtil {

    //设置右上角圆点标志
    public static void setDPDecor(HashSet<DateTime> dateTimeList, DatePicker picker) {
        List<String> tmpTR = new ArrayList<>();
        for (DateTime dateTime : dateTimeList) {
            String year = dateTime.getYear() + "";
            String month = dateTime.getMonthOfYear() + "";
            String day = dateTime.getDayOfMonth() + "";
            tmpTR.add(year + "-" + month + "-" + day);
        }
        DPCManager.getInstance().setDecorTR(tmpTR);

        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint, String data) {
                super.drawDecorTL(canvas, rect, paint, data);
            }

            @Override
            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint, String data) {
                super.drawDecorTR(canvas, rect, paint, data);
                paint.setColor(Color.parseColor("#45C01A"));
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 4, paint);
            }
        });

    }

}
