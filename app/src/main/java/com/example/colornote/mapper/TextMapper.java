package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.Text;

import java.sql.Date;

public class TextMapper implements RowMapper<Text> {

    @Override
    public Text mappRow(Cursor cursor) {
        Text text = new Text();
        text.setId(cursor.getInt(0));
        text.setTitle(cursor.getString(1));
        text.setContent(cursor.getString(2));
        text.setColorId(cursor.getInt(3));
        text.setReminder(new Date(cursor.getLong(4)));
        text.setModifiedDate(new Date(cursor.getLong(5)));
        text.setStatus(cursor.getInt(6));
        return text;
    }
}
