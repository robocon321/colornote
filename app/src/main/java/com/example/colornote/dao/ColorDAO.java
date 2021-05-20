package com.example.colornote.dao;

import android.content.ContentValues;

import com.example.colornote.model.Color;

public class ColorDAO extends AbstractDAO{

    public long insert(Color color){
        ContentValues values = new ContentValues();
        values.put("color", color.getColorMain());
        values.put("color", color.getColorSub());
        values.put("content", color.getContent());
        return database.insert("Color", null, values);
    }

    public int update(Color color){
        ContentValues values = new ContentValues();
        values.put("color", color.getColorMain());
        values.put("color", color.getColorSub());
        values.put("content", color.getContent());
        return database.update("Color", values, "id = ?", new String[]{color.getId()+""});
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Color";
    }
}
