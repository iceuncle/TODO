package com.todo.ui.base;

/**
 * 数据为空 Created by dzq on 16/7/12.
 */
public class EmptyException extends Exception {


    @EmptyState
    private int emptyType = EmptyState.NORMAL;

    public EmptyException(@EmptyState int emptyType) {
        super("没有数据...");
        this.emptyType = emptyType;
    }

    public EmptyException() {
        super("没有数据...");
    }

    @EmptyState
    public int getEmptyType() {
        return emptyType;
    }

    public void setEmptyType(@EmptyState int emptyType) {
        this.emptyType = emptyType;
    }
}
