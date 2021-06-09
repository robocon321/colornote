package com.example.colornote.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Comparator;

public abstract class Task implements Serializable {
    protected int id;
    protected String title;
    protected int colorId;
    protected int reminderId;
    protected Date modifiedDate;
    protected int status;

    public abstract String showContent();
    public abstract boolean isComplete();

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

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

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Comparator<Task> compareByModifiedTime = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.modifiedDate.compareTo(t2.modifiedDate);
        }
    };

    public static Comparator<Task> compareByColor = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.colorId - t2.colorId;
        }
    };

    public static Comparator<Task> compareByTitle = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.title.compareTo(t2.title);
        }
    };

    public static Comparator<Task> compareByReminderTime = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getReminderId()>t2.getReminderId() ? 1: -1;
        }
    };
}
