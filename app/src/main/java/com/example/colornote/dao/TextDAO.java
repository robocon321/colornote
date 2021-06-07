package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.colornote.database.Database;
import com.example.colornote.model.Text;

import java.util.Date;
import java.util.List;

public class TextDAO extends AbstractDAO{
    private static TextDAO instance = new TextDAO();
    public static TextDAO getInstance(){
        return instance;
    }
    private TextDAO(){}

    public long insert(Text text){
        ContentValues values = new ContentValues();
        values.put("title", text.getTitle());
        values.put("content", text.getContent());
        values.put("color", text.getColorId());
        values.put("reminderId", text.getReminderId());
        values.put("modifiedDate", text.getModifiedDate().getTime());
        values.put("status",text.getStatus());
        return database.getSqLiteDatabase().insert("Text", null, values);
    }

    public int update(Text text){
        ContentValues values = new ContentValues();
        values.put("title", text.getTitle());
        values.put("content", text.getContent());
        values.put("color", text.getColorId());
        values.put("reminderId", text.getReminderId());
        values.put("modifiedDate", text.getModifiedDate().getTime());
        values.put("status",text.getStatus());
        return database.getSqLiteDatabase().update("Text", values, "id = ?", new String[]{text.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.getSqLiteDatabase().update("Text", values, "id = ?", new String[]{id+""});
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Text";
    }
}
