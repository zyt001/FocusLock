package com.zyt.kineticlock.bean;

public class Task {

    private int id;
    private String title;
    private int lockTime;
    private String TaskMode;
    private int modeNum;
    private int alarmMode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

    public String getTaskMode() {
        return TaskMode;
    }

    public void setTaskMode(String taskMode) {
        this.TaskMode = taskMode;
    }

    public int getModeNum() {
        return modeNum;
    }

    public void setModeNum(int modeNum) {
        this.modeNum = modeNum;
    }

    public int getAlarmMode() {
        return alarmMode;
    }

    public void setAlarmMode(int alarmMode) {
        this.alarmMode = alarmMode;
    }
}
