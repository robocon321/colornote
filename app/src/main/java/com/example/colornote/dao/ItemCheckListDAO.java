package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.model.ItemCheckList;

import java.util.ArrayList;
import java.util.List;

public class ItemCheckListDAO extends AbstractDAO{
    private static ItemCheckListDAO instance = new ItemCheckListDAO();
    public static ItemCheckListDAO getInstance(){
        return instance;
    }
    private ItemCheckListDAO(){}
    public long insert(ItemCheckList itemCheckList){
        ContentValues values = new ContentValues();
        values.put("content", itemCheckList.getContent());
        values.put("parentId", itemCheckList.getParentId());
        values.put("modifiedDate", itemCheckList.getModifiedDate().getTime());
        values.put("status", itemCheckList.getStatus());
        return database.getSqLiteDatabase().insert("ItemCheckList", null, values);
    }

    public int update(ItemCheckList itemCheckList){
        ContentValues values = new ContentValues();
        values.put("content", itemCheckList.getContent());
        values.put("parentId", itemCheckList.getParentId());
        values.put("modifiedDate", itemCheckList.getModifiedDate().getTime());
        values.put("status", itemCheckList.getStatus());
        return database.getSqLiteDatabase().update("ItemCheckList", values, "id = ?", new String[]{itemCheckList.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.getSqLiteDatabase().update("ItemCheckList", values, "id = ?", new String[]{id+""});
    }

    public List<ItemCheckList> getByStatus(int status){
        List<ItemCheckList> list = new ArrayList<>();
        String query = queryAll() + "WHERE status = "+status;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
        RowMapper<ItemCheckList> mapper = new ItemCheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public void deleteByIdCheckList(int id) {
        String sql = "DELETE FROM ItemCheckList WHERE parentId = " + id;
        database.getSqLiteDatabase().execSQL(sql);
    }

    public List<ItemCheckList> getByParentId(int parentId) {
        List<ItemCheckList> list = new ArrayList<>();
        String query = queryAll() + " WHERE parentId = " + parentId;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
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
