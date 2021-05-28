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
        return database.insert("Color", null, values);
    }

    public int update(Color color){
        ContentValues values = new ContentValues();
        values.put("colorMain", color.getColorMain());
        values.put("colorSub", color.getColorSub());
        values.put("content", color.getContent());
        return database.update("Color", values, "id = ?", new String[]{color.getId()+""});
    }

    public int countTask(int id){
        String query = "SELECT COUNT(*) c FROM (SELECT id FROM Text WHERE color = "+id+" UNION ALL SELECT id FROM CheckList  WHERE color = "+id+")";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM Color";
    }
}
