package com.example.colornote.dao;

import android.content.ContentValues;

import com.example.colornote.model.CheckList;
import com.example.colornote.model.Text;

public class CheckListDAO extends AbstractDAO{
    public long insert(CheckList checkList){
        ContentValues values = new ContentValues();
        values.put("title", checkList.getTitle());
        values.put("colorId", checkList.getColorId());
        values.put("reminder", checkList.getReminder().getTime());
        values.put("modifiedDate", checkList.getModifiedDate().getTime());
        values.put("status",checkList.getStatus());
        return database.insert("CheckList", null, values);
    }

    public int update(CheckList checkList){
        ContentValues values = new ContentValues();
        values.put("title", checkList.getTitle());
        values.put("colorId", checkList.getColorId());
        values.put("reminder", checkList.getReminder().getTime());
        values.put("modifiedDate", checkList.getModifiedDate().getTime());
        values.put("status",checkList.getStatus());
        return database.update("CheckList", values, "id = ?", new String[]{checkList.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.update("CheckList", values, "id = ?", new String[]{id+""});
    }
}
