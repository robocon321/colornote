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
        text.setColorId(cursor.getInt(3));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(cursor.getString(4) != null){
                text.setReminder(sdf.parse(cursor.getString(4)));
            }
            text.setModifiedDate(sdf.parse(cursor.getString(5)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        text.setStatus(cursor.getInt(6));
        return text;
    }
}
