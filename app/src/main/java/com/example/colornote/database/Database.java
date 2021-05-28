package com.example.colornote.database;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.File;

public class Database {
    private SQLiteDatabase sqLiteDatabase;
    private static Database database = new Database();
    public static Database getInstance(){
        return database;
    }
    private Database(){}

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    private String buildPathDatabase(String name, Activity activity){
        String path;
        if(Build.VERSION.SDK_INT >= 17)
            path = activity.getApplicationInfo().dataDir+"/databases/";
        else
            path = "/data/data"+activity.getPackageName()+"/databases/";
        path += "/"+name;
        return path;
    }

    public void createDatabase(String name, Activity activity){
        String path = buildPathDatabase(name, activity);
        File file = activity.getDatabasePath(path);
        if(!file.exists()){
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            createTable();
        }else{
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private void createTable(){
        sqLiteDatabase.execSQL("CREATE TABLE Text(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), content TEXT, color INTEGER, reminder DATETIME, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE CheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), color INTEGER, reminder DATETIME, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE ItemCheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, parentId BIGINT, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE Color(id INTEGER PRIMARY KEY AUTOINCREMENT, colorMain VARCHAR(10), colorSub VARCHAR(10), content NVARCHAR(255))");
    }

    public void dropTable(String name){
        sqLiteDatabase.execSQL("DROP TABLE "+name);
    }

}
