package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.Text;

import java.sql.Date;

public class TextMapper implements RowMapper<Text> {

    @Override
    public Text mappRow(Cursor cursor) {
        Text text = new Text();
        text.setId(cursor.getInt(1));
        text.setTitle(cursor.getString(2));
        text.setContent(cursor.getString(3));
        text.setColorId(cursor.getInt(4));
        text.setReminder(new Date(cursor.getLong(5)));
        text.setModifiedDate(new Date(cursor.getLong(6)));
        text.setStatus(cursor.getInt(7));
        return text;
    }
}
