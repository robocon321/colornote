package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.Reminder;

import java.util.Date;

public class ReminderMapper implements RowMapper<Reminder> {
    @Override
    public Reminder mappRow(Cursor cursor) {
        Reminder reminder = new Reminder();
        reminder.setId(cursor.getInt(0));
        reminder.setType(cursor.getInt(1));
        if(reminder.getStartDate() != null){
            reminder.setStartDate(new Date(cursor.getLong(2)));
        }
        if(reminder.getEndDate() != null){
            reminder.setEndDate(new Date(cursor.getLong(3)));
        }
        reminder.setRepetition(cursor.getInt(4));
        reminder.setStatus(cursor.getInt(5));
        return null;
    }
}
