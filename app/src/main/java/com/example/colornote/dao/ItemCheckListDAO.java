package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.model.ItemCheckList;

import java.util.ArrayList;
import java.util.List;

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

    public List<ItemCheckList> getByStatus(int status){
        List<ItemCheckList> list = new ArrayList<>();
        String query = queryAll() + "WHERE status = "+status;
        Cursor cursor = database.rawQuery(query, null);
        RowMapper<ItemCheckList> mapper = new ItemCheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM ItemCheckList";
    }
}
