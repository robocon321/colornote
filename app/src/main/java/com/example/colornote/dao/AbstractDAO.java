package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.colornote.database.Database;
import com.example.colornote.mapper.RowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDAO {
    protected SQLiteDatabase database = Database.getInstance().getSqLiteDatabase();

    public abstract String queryAll();
    public <T> List<T> getAll(RowMapper<T> mapper){
        List<T> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(queryAll(), null);
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public <T> T get(RowMapper<T> mapper, int id){
        List<T> list = new ArrayList<>();
        String query = queryAll() + " WHERE id =" + id;
        Cursor cursor = database.rawQuery(query, null);
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list.isEmpty() ? null : list.get(0);
    }
}
