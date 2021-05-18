package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.colornote.database.Database;
import com.example.colornote.mapper.RowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractDAO {
    protected SQLiteDatabase database = Database.getInstance().getSqLiteDatabase();

    public <T> List<T> get(String sql, RowMapper<T> mapper, long id){
        List<T> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, null);
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }
}
