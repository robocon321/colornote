package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.CheckList;

import java.sql.Date;

public class CheckListMapper implements RowMapper<CheckList> {
    @Override
    public CheckList mappRow(Cursor cursor) {
        CheckList checkList = new CheckList();
        checkList.setId(cursor.getInt(1));
        checkList.setTitle(cursor.getString(2));
        checkList.setColorId(cursor.getInt(3));
        checkList.setReminder(new Date(cursor.getLong(4)));
        checkList.setModifiedDate(new Date(cursor.getLong(5)));
        checkList.setStatus(cursor.getInt(6));
        return checkList;
    }
}
