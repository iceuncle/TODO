package com.todo.vendor.comparator;


import com.todo.data.database.WeekSchedule;

public class SortModel extends WeekSchedule{

    private String sortLetters;


    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

}
