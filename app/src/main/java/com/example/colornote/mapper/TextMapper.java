package com.example.colornote.mapper;

import android.database.Cursor;
import android.util.Log;

import com.example.colornote.model.Text;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TextMapper implements RowMapper<Text> {

    @Override
    public Text mappRow(Cursor cursor) {
        Text text = new Text();
        text.setId(cursor.getInt(0));
        text.setTitle(cursor.getString(1));
        text.setContent(cursor.getString(2));
        text.setCompleted(cursor.getInt(3) == 1);
        text.setColorId(cursor.getInt(4));
        text.setReminderId(cursor.getInt(5));
        text.setModifiedDate(new Date(cursor.getLong(6)));
        text.setStatus(cursor.getInt(7));
        return text;
    }
}
