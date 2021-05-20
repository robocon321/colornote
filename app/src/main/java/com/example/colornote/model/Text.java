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
    public boolean isComplete() {
        return status == Constant.COMPLETE;
    }

}
