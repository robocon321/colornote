package com.example.colornote.model;

import java.io.Serializable;
import java.util.Date;

public class BackupInfo implements Serializable {
    private String password;
    private Date date;
    private boolean type;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BackupInfo{" +
                "password='" + password + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
