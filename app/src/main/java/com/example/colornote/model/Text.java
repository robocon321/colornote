package com.example.colornote.model;

import com.example.colornote.util.Constant;

import java.util.Comparator;
import java.util.Date;

public class Text extends Task{
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String showContent() {
        return getContent();
    }

    @Override
    public boolean completeAll() {
        return completed;
    }

    @Override
    public String toString() {
        return "Text{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", colorId=" + colorId +
                ", reminderId=" + reminderId +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
