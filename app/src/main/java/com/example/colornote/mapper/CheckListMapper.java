package com.example.colornote.mapper;

import android.database.Cursor;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                if(cursor.getString(3) != null){
                   checkList.setReminder(sdf.parse(cursor.getString(3)));
                }
                checkList.setModifiedDate(sdf.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        checkList.setStatus(cursor.getInt(5));
        return checkList;
    }
}
