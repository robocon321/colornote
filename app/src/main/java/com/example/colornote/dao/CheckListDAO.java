package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;

import java.util.ArrayList;
import java.util.List;

public class CheckListDAO extends AbstractDAO{
    private static CheckListDAO instance = new CheckListDAO();
    public static CheckListDAO getInstance(){
        return instance;
    }
    private CheckListDAO(){}
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

    public List<CheckList> getByStatus(int status){
        List<CheckList> list = new ArrayList<>();
        String query = queryAll() + "WHERE status = "+status;
        Cursor cursor = database.rawQuery(query, null);
        RowMapper<CheckList> mapper = new CheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public List<ItemCheckList> getItemCheckList(int parentId){
        List<ItemCheckList> list = new ArrayList<>();
        String query = ItemCheckListDAO.getInstance().queryAll() + " WHERE parentId = "+parentId;
        Cursor cursor = database.rawQuery(query, null);
        RowMapper<ItemCheckList> mapper = new ItemCheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM CheckList";
    }
}
