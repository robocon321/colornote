package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.colornote.database.Database;
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Text;

import java.util.ArrayList;
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

    public List<Text> getTextEnable(){
        List<Text> list = new ArrayList<>();
        String sql = queryAll() + " WHERE status = 2 OR status = 3";
        Cursor cursor = database.getSqLiteDatabase().rawQuery(sql, null);
        RowMapper<Text> mapper= new TextMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
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

    @Override
    public String queryWithKey() {
        return "Select distinct * from Text Where title LIKE ? OR content LIKE ? or modifiedDate like ?";
    }
}
