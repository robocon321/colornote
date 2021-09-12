package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.example.colornote.database.Database;
import com.example.colornote.mapper.RowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDAO {
    protected Database database = Database.getInstance();

    public abstract String queryAll();

    public abstract String queryWithKey();

    public <T> List<T> getAll(RowMapper<T> mapper) {
        List<T> list = new ArrayList<>();
        Cursor cursor = database.getSqLiteDatabase().rawQuery(queryAll(), null);
        while (cursor.moveToNext()) {
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public <T> T get(RowMapper<T> mapper, int id) {
        List<T> list = new ArrayList<>();
        String query = queryAll() + " WHERE id =" + id;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(mapper.mappRow(cursor));
        }
        return list.isEmpty() ? null : list.get(0);
    }

    public int count() {
        String query = "SELECT COUNT(*) C FROM (" + queryAll() + ")";
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public <T> List<T> getWithKey(RowMapper<T> mapper, String s) {
        List<T> list = new ArrayList<>();
        Cursor cursor = database.getSqLiteDatabase().rawQuery(queryWithKey(), new String[]{"%" + s + "%", "%" + s + "%", "%" + s + "%"});
        while (cursor.moveToNext()) {
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public <T> List<T> getWithModifiedDate(RowMapper<T> mapper, String date) {
        List<T> list = new ArrayList<>();
        String sql = queryAll() +" where modifiedDate = '" + date + "'";
        Cursor cursor = database.getSqLiteDatabase().rawQuery(sql,null);
        while (cursor.moveToNext()) {
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }
}
