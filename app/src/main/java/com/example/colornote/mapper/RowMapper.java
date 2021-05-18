package com.example.colornote.mapper;

import android.database.Cursor;

public interface RowMapper<T> {
    T mappRow(Cursor cursor);
}
