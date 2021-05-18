package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.Color;

public class ColorMapper implements RowMapper<Color>{
    @Override
    public Color mappRow(Cursor cursor) {
        Color color = new Color();
        color.setId(cursor.getInt(1));
        color.setColor(cursor.getString(2));
        color.setContent(cursor.getString(3));
        return color;
    }
}
