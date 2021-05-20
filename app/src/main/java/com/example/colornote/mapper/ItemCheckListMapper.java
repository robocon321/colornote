package com.example.colornote.mapper;

import android.database.Cursor;

import com.example.colornote.model.ItemCheckList;

import java.sql.Date;

public class ItemCheckListMapper implements RowMapper<ItemCheckList> {
    @Override
    public ItemCheckList mappRow(Cursor cursor) {
        ItemCheckList item = new ItemCheckList();
        item.setId(cursor.getInt(0));
        item.setContent(cursor.getString(1));
        item.setParentId(cursor.getInt(2));
        item.setModifiedDate(new Date(cursor.getLong(3)));
        item.setStatus(cursor.getInt(4));
        return item;
    }
}
