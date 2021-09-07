package com.example.colornote.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

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
        values.put("isComplete", checkList.isCompleted());
        values.put("color", checkList.getColorId());
        values.put("reminderId", checkList.getReminderId());
        values.put("modifiedDate", checkList.getModifiedDate().getTime());
        values.put("status",checkList.getStatus());
        return database.getSqLiteDatabase().insert("CheckList", null, values);
    }

    public int update(CheckList checkList){
        ContentValues values = new ContentValues();
        values.put("title", checkList.getTitle());
        values.put("isComplete", checkList.isCompleted());
        values.put("color", checkList.getColorId());
        values.put("reminderId", checkList.getReminderId());
        values.put("modifiedDate", checkList.getModifiedDate().getTime());
        values.put("status",checkList.getStatus());
        return database.getSqLiteDatabase().update("CheckList", values, "id = ?", new String[]{checkList.getId()+""});
    }

    public int changeStatus(long id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        return database.getSqLiteDatabase().update("CheckList", values, "id = ?", new String[]{id+""});
    }

    public int changeCompleted(long id, boolean isComplete){
        ContentValues values = new ContentValues();
        values.put("isComplete", isComplete ? 1 : 0);
        return database.getSqLiteDatabase().update("CheckList", values, "id = ?", new String[]{id+""});
    }

    public List<CheckList> getByStatus(int status){
        List<CheckList> list = new ArrayList<>();
        String query = queryAll() + " WHERE status = "+status;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(query, null);
        RowMapper<CheckList> mapper = new CheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public List<CheckList> getNoteCheckList() {
        List<CheckList> list = new ArrayList<>();
        String sql = queryAll() + " WHERE reminderId == 0" ;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(sql, null);
        RowMapper<CheckList> mapper= new CheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    public List<CheckList> getCalendarCheckList() {
        List<CheckList> list = new ArrayList<>();
        String sql = queryAll() + " WHERE reminderId <> 0" ;
        Cursor cursor = database.getSqLiteDatabase().rawQuery(sql, null);
        RowMapper<CheckList> mapper= new CheckListMapper();
        while(cursor.moveToNext()){
            list.add(mapper.mappRow(cursor));
        }
        return list;
    }

    @Override
    public String queryAll() {
        return "SELECT * FROM CheckList";
    }

    public void delete(int id){
        String sql = "DELETE FROM CheckList WHERE id = "+id;
        database.getSqLiteDatabase().execSQL(sql);
    }

    @Override
    public String queryWithKey() {
        return "SELECT * FROM CHECKLIST WHERE title LIKE ? or modifiedDate like ? OR id IN (SELECT parentId FROM ItemChecklist WHERE content like ?)";
    }

}
