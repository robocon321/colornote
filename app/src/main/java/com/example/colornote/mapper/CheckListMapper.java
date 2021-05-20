package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.CheckList;

import java.sql.Date;

public class CheckListMapper implements RowMapper<CheckList>{
    @Override
    public CheckList mappRow(Cursor cursor) {
        CheckList checkList = new CheckList();
        checkList.setId(cursor.getInt(0));
        checkList.setTitle(cursor.getString(1));
        checkList.setColorId(cursor.getInt(2));
        checkList.setReminder(new Date(cursor.getLong(3)));
        checkList.setModifiedDate(new Date(cursor.getLong(4)));
        checkList.setStatus(cursor.getInt(5));
        return checkList;
    }
}
