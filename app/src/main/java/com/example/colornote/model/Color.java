package com.example.colornote.model;

public class Color{
    private int id;
    private String colorMain;
    private String colorSub;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColorMain() {
        return colorMain;
    }

    public void setColorMain(String colorMain) {
        this.colorMain = colorMain;
    }

    public String getColorSub() {
        return colorSub;
    }

    public void setColorSub(String colorSub) {
        this.colorSub = colorSub;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
