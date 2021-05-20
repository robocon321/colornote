package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.ItemCheckList;

import java.sql.Date;

public class ItemCheckListMapper implements RowMapper<ItemCheckList> {
    @Override
    public ItemCheckList mappRow(Cursor cursor) {
        ItemCheckList item = new ItemCheckList();
        item.setId(cursor.getInt(1));
        item.setContent(cursor.getString(2));
        item.setParentId(cursor.getInt(3));
        item.setModifiedDate(new Date(cursor.getLong(4)));
        item.setStatus(cursor.getInt(5));
        return item;
    }
}
