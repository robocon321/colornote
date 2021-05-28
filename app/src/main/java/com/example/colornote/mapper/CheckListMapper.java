package com.example.colornote.mapper;

import android.database.Cursor;
import android.util.Log;

import com.example.colornote.model.CheckList;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CheckListMapper implements RowMapper<CheckList>{
    @Override
    public CheckList mappRow(Cursor cursor) {
        CheckList checkList = new CheckList();
        checkList.setId(cursor.getInt(0));
        checkList.setTitle(cursor.getString(1));
        checkList.setColorId(cursor.getInt(2));
        if(cursor.getString(3) != null){
           checkList.setReminder(new Date(cursor.getLong(3)));
        }
        checkList.setModifiedDate(new Date(cursor.getLong(4)));
        checkList.setStatus(cursor.getInt(5));
        return checkList;
    }
}
