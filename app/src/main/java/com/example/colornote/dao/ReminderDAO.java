package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.ReminderMapper;
import com.example.colornote.model.Reminder;

public class ReminderDAO extends AbstractDAO {
    private static ReminderDAO instance = new ReminderDAO();

    public static ReminderDAO getInstance() {
        return instance;
    }

    private ReminderDAO() {
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Reminder";
    }


    @Override
    public String queryWithKey() {
        return "Select * from Reminder Where title LIKE ? OR content LIKE ? or modifiedDate like ?";
    }

    public Reminder getReminderById(int id) {
        String sql = queryAll() + " WHERE id = " + id;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(sql, null);

        cursor.moveToNext();
        return new ReminderMapper().mappRow(cursor);
    }

    public long insert(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put("type", reminder.getType());
        values.put("startTime", reminder.getStartDate() == null ? null : reminder.getStartDate().getTime());
        values.put("endTime", reminder.getEndDate() == null ? null : reminder.getEndDate().getTime());
        values.put("repetition", reminder.getRepetition());
        values.put("status", reminder.getStatus());
        return database.getSqLiteDatabase().insert("Reminder", null, values);
    }

    public int update(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put("type", reminder.getType());
        values.put("startTime", reminder.getStartDate() == null ? null : reminder.getStartDate().getTime());
        values.put("endTime", reminder.getEndDate() == null ? null : reminder.getEndDate().getTime());
        values.put("repetition", reminder.getRepetition());
        values.put("status", reminder.getStatus());
        return database.getSqLiteDatabase().update("Reminder", values, "id = ?", new String[]{reminder.getId() + ""});
    }

    public void delete(int id) {
        String sql = "DELETE FROM Reminder WHERE id = " + id;
        database.getSqLiteDatabase().execSQL(sql);
    }
}
