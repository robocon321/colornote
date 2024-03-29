package com.example.colornote.database;

import android.app.Activity;
import android.content.Context;
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

    public String buildPathDatabase(String name, Context context){
        String path;
        if(Build.VERSION.SDK_INT >= 17)
            path = context.getApplicationInfo().dataDir+"/databases/";
        else
            path = "/data/data"+context.getPackageName()+"/databases/";
        path += "/"+name;
        return path;
    }

    public void createDatabase(String name, Context context){
        String path = buildPathDatabase(name, context);
        File file = context.getDatabasePath(path);
        if(!file.exists()){
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            createTable();
        }else{
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private void createTable(){
        sqLiteDatabase.execSQL("CREATE TABLE Text(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), content TEXT, isComplete BOOLEAN, color INTEGER, reminderId INTEGER, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE CheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(255), isComplete BOOLEAN, color INTEGER, reminderId INTEGER, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE ItemCheckList(id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, isComplete BOOLEAN, parentId BIGINT, modifiedDate DATETIME, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE Color(id INTEGER PRIMARY KEY AUTOINCREMENT, colorMain VARCHAR(10), colorSub VARCHAR(10), content NVARCHAR(255))");
        sqLiteDatabase.execSQL("CREATE TABLE Reminder(id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, startTime DATETIME, endTime DATETIME, repetition INTEGER, status INT)");

        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'yellow2', 'osier iopser oise rionsdagf oiwe rlkase rf oiw eraskdnf', 1, 2, 0, '2007-01-01', 2);");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'orange3', 'laksdfqwierjpsaoi dfj;askdf askdnf;sakd,f qwsf oisdnfsq erijsnef asdfkkj', 0, 3, 0, '2021-11-12', 2);");
//        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'tuyen','asidfwqe oisđ oids02', 1, 2, 0, '2020-12-05', 2)");
//        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'YTU','asdkj ase983w3 lksd', 0, 2, 0, '2020-03-15', 1)");
//        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, 'tuyen1','2938 sdkk w23kd', 0, 7, 0, '2020-03-05', 0)");

        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, '4', 'osier iopser oise rionsdagf oiwe rlkase rf oiw eraskdnf', 1, 4, 1, '1632502800000', 2);");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, '5', 'laksdfqwierjpsaoi dfj;askdf askdnf;sakd,f qwsf oisdnfsq erijsnef asdfkkj', 0, 5, 0, '1632243600000', 2);");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, '6','asidfwqe oisđ oids02', 1, 6, 0, '1632934800000', 2)");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, '7','asdkj ase983w3 lksd', 0, 7, 0, '2020-03-15', 2)");
        sqLiteDatabase.execSQL("INSERT INTO TEXT VALUES(null, '8','2938 sdkk w23kd', 0, 8, 0, '2020-03-05', 2)");

        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, '9', 1, 9, 0, '163276200000', 2)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, '10', 0, 10, 0, '1635613200000', 2)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'hecklist13', 1, 7, 0, '2021-08-17', 1)");
        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'hecklist14', 1, 5, 0, '2021-02-17', 0)");
//        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'checklist5', 0, 2, 0, '2021-03-02', 1)");
//        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'hecklist16', 0, 3, 0, '2021-01-07', 2)");
//        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'hecklist17', 1, 7, 0, '2021-08-17', 2)");
//        sqLiteDatabase.execSQL("INSERT INTO CHECKLIST VALUES(null, 'hecklist18', 0, 5, 0, '2021-02-17', 2)");

        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'lkdsk lksd oi3498 sdkoi lksdoiek oiwe kdoi', 0, 1, '2020-02-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, '98dk sod89 klisd98 lkđ kdsoi ', 1, 1, '2020-04-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, '83 sdlkf lkds92 lksd lkweoi023 983 lkjsd', 1, 1, '2021-02-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'lksd 8328 isedlk sdlkf oiewr lksd', 0, 1, '2019-11-11', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'LKASDFO8IWE SDLK 9832 LKDS ', 0, 2, '2020-01-02', 2);");
        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, ' SOID LKWE okier ', 1, 2, '2020-04-02', 2);");
//        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'oakszdieow iwe lkd lkske ik ', 1, 2, '2021-10-10', 2);");
//        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'mdkco dkhcohan lsk oie lksd oke oi ', 0, 3, '2021-11-02', 2);");
//        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, ' SOID LKWE okier ', 0, 3, '2021-02-01', 2);");
//        sqLiteDatabase.execSQL("INSERT INTO ItemCheckList VALUES(null, 'oakszdieow iwe lkd lkske ik ', 0, 3, '2011-11-10', 2);");

        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,null ,null, null);");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#fff2b2' ,'#ffe14c', 'yellow'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#FFEAD7' ,'#ff7f24', 'orange'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#f7cad0' ,'#fc6f6f','red'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#b7efc5' ,'#6ede8a', 'green'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#caf0f8' ,'#48cae4', 'blue'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#dec9e9' ,'#c19ee0', 'purple'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#adb5bd' ,'#212529', 'black'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#dee2e6' ,'#6c757d', 'gray'); ");
        sqLiteDatabase.execSQL("INSERT INTO Color(id,colorMain,colorSub,content) VALUES (null,'#ffffff' ,'#ffffff', 'white'); ");
    }

    public void dropTable(String name){
        sqLiteDatabase.execSQL("DROP TABLE "+name);
    }

}
