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
    public void setSqLiteDatabase(String path) {
        this.sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void pointToDatabaseMain(Activity activity){
        createDatabase("database.sqlite", activity);
    }

    public String buildPathDatabase(String name, Activity activity){
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
        sqLiteDatabase.execSQL("CREATE TABLE Text(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), content TEXT, color INTEGER, reminderId INTEGER, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE CheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), color INTEGER, reminderId INTEGER, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE ItemCheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, parentId BIGINT, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE Color(id INTEGER PRIMARY KEY AUTOINCREMENT, colorMain VARCHAR(10), colorSub VARCHAR(10), content NVARCHAR(255))");
        sqLiteDatabase.execSQL("CREATE TABLE Reminder(id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, startTime DATETIME, endTime DATETIME, repetition INTEGER, status INT)");

        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'ZDG', 'osier iopser oise rionsdagf oiwe rlkase rf oiw eraskdnf', 3, null, '2007-01-01', 2);");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'YTI', 'laksdfqwierjpsaoi dfj;askdf askdnf;sakd,f qwsf oisdnfsq erijsnef asdfkkj', 3, null, '2021-11-12', 2);");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'mbd','asidfwqe oisđ oids02', 2, null, '2020-12-05', 3)");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'YTU','asdkj ase983w3 lksd', 2, null, '2020-03-15', 3)");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'kd','2938 sdkk w23kd', 7, null, '2020-03-05', 3)");

        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'as983', 2, null, '2021-03-02', 3)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, '98 ksd2983', 3, null, '2021-01-07', 2)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'DKW', 7, null, '2021-08-17', 3)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'TaHahha', 5, null, '2021-02-17', 2)");

        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'lkdsk lksd oi3498 sdkoi lksdoiek oiwe kdoi', 1, '2020-02-02', 3);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, '98dk sod89 klisd98 lkđ kdsoi ', 1, '2020-04-02', 3);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, '83 sdlkf lkds92 lksd lkweoi023 983 lkjsd', 1, '2021-02-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'lksd 8328 isedlk sdlkf oiewr lksd', 1, '2019-11-11', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'LKASDFO8IWE SDLK 9832 LKDS ', 2, '2020-01-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, ' SOID LKWE okier ', 2, '2020-04-02', 3);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'oakszdieow iwe lkd lkske ik ', 2, '2021-10-10', 3);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'mdkco dkhcohan lsk oie lksd oke oi ', 3, '2021-11-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, ' SOID LKWE okier ', 3, '2021-02-01', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'oakszdieow iwe lkd lkske ik ', 3, '2011-11-10', 2);");

        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,null ,null, null);");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#ffe77a' ,'#f7d539', 'yellow'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#ffaf75' ,'#ff7f24', 'orange'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#fc6f6f' ,'#ff2b2b','red'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#94f08d' ,'#48fc3a', 'green'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#97c2f7' ,'#286fc7', 'blue'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#e4a8ff' ,'#e4a8ff', 'purple'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#b5b5b5' ,'#000000', 'black'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#e6e6e6' ,'#948787', 'gray'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#ffffff' ,'#ededed', 'white'); ");
    }

    public void dropTable(String name){
        sqLiteDatabase.execSQL("DROP TABLE "+name);
    }

}
