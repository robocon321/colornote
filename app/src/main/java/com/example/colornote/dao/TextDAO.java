package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.colornote.database.Database;
import com.example.colornote.model.Text;

import java.util.Date;
import java.util.List;

public class TextDAO extends AbstractDAO{
    public long insert(Text text){
        ContentValues values = new ContentValues();
        values.put("title", text.getTitle());
        values.put("content", text.getContent());
        values.put("colorId", text.getColorId());
        values.put("reminder", text.getReminder().getTime());
        values.put("modifiedDate", text.getModifiedDate().getTime());
        values.put("status",text.getStatus());
        return database.insert("Text", null, values);
    }

    public int update(Text text){
        ContentValues values = new ContentValues();
        values.put("title", text.getTitle());
        values.put("content", text.getContent());
        values.put("colorId", text.getColorId());
        values.put("reminder", text.getReminder().getTime());
        values.put("modifiedDate", text.getModifiedDate().getTime());
        values.put("status",text.getStatus());
        return database.update("Text", values, "id = ?", new String[]{text.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.update("Text", values, "id = ?", new String[]{id+""});
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Text";
    }
}
