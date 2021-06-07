package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.ReminderMapper;
import com.example.colornote.model.Reminder;

public class ReminderDAO extends AbstractDAO{
    private static ReminderDAO instance = new ReminderDAO();
    public static ReminderDAO getInstance(){
        return instance;
    }
    private ReminderDAO(){ }
    @Override
    public String queryAll() {
        return "SELECT * FROM Reminder";
    }

    public Reminder getReminderById(int id){
        String sql = queryAll()+"WHERE id = "+id;
        Cursor cursor =  database.getSqLiteDatabase().rawQuery(sql, null);
        cursor.moveToNext();
        return new ReminderMapper().mappRow(cursor);
    }
}
