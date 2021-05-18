package com.example.colornote.dao;

import android.content.ContentValues;

import com.example.colornote.model.ItemCheckList;
import com.example.colornote.model.Text;

public class ItemCheckListDAO extends AbstractDAO{
    public long insert(ItemCheckList itemCheckList){
        ContentValues values = new ContentValues();
        values.put("content", itemCheckList.getContent());
        values.put("parentId", itemCheckList.getParentId());
        values.put("modifiedDate", itemCheckList.getModifiedDate().getTime());
        values.put("status", itemCheckList.getStatus());
        return database.insert("ItemCheckList", null, values);
    }

    public int update(ItemCheckList itemCheckList){
        ContentValues values = new ContentValues();
        values.put("content", itemCheckList.getContent());
        values.put("parentId", itemCheckList.getParentId());
        values.put("modifiedDate", itemCheckList.getModifiedDate().getTime());
        values.put("status", itemCheckList.getStatus());
        return database.update("ItemCheckList", values, "id = ?", new String[]{itemCheckList.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.update("ItemCheckList", values, "id = ?", new String[]{id+""});
    }
}
