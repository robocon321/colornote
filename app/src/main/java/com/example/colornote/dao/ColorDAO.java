package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.colornote.model.Color;

public class ColorDAO extends AbstractDAO{
    private static ColorDAO instance = new ColorDAO();
    public static ColorDAO getInstance(){
        return instance;
    }
    private ColorDAO(){}


    public long insert(Color color){
        ContentValues values = new ContentValues();
        values.put("colorMain", color.getColorMain());
        values.put("colorSub", color.getColorSub());
        values.put("content", color.getContent());
        return database.getSqLiteDatabase().insert("Color", null, values);
    }

    public int update(Color color){
        ContentValues values = new ContentValues();
        values.put("colorMain", color.getColorMain());
        values.put("colorSub", color.getColorSub());
        values.put("content", color.getContent());
        return database.getSqLiteDatabase().update("Color", values, "id = ?", new String[]{color.getId()+""});
    }

    public int countTask(int id){
        String query = "SELECT COUNT(*) c FROM (SELECT id FROM Text WHERE color = "+id+" UNION ALL SELECT id FROM CheckList  WHERE color = "+id+")";
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Color";
    }

    @Override
    public String queryWithKey() {
        return "Select * from Color Where title LIKE ? OR content LIKE ? or modifiedDate like ?";
    }
    public Color getColorMainSub(int id){
        Color color = new Color();
        return color;
    }
}
