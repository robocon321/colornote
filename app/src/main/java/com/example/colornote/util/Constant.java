package com.example.colornote.util;

public class Constant {
    // Status constant
    public final static int DELETE = 0;
    public final static int RECYCLE_BIN = 1;
    public final static int NON_COMPLETE = 2;
    public final static int COMPLETE = 3;

    // task type
    public final static int ALL_TASK = 0;
    public final static int TEXT_TASK = 0;
    public final static int CHECKLIST_TASK = 0;

    // backup table type
    public final static int ALL_TABLE = 0;
    public final static int NOTES_TABLE = 1;
    public final static int CALENDAR_TABLE = 2;

    // backup status
    public final static int ALL_STATUS = 0;
    public final static int NORMAL_STATUS = 1;
    public final static int ARCHIEVED_STATUS = 2;

    // Settings VIEW
    public final static int LIST = 0;
    public final static int  DETAILS = 1;
    public final static int GRID = 2;
    public final static int LARGE_GRID = 3;

    // Color default
    public final static String MAIN_COLOR = "#FFD54F";
    public final static String SUB_COLOR = "#FFC107";

    // to Milliseconds

    public final static long YEAR = 31556952000L;
    public final static long MONTH = 2629800000L;
    public final static long WEEK = 604800000L;
    public final static long DAY = 86400000L;
    public final static long HOUR = 3600000L;
    public final static long MINUTE = 60000L;
    public final static long SECOND = 1000L;
}
