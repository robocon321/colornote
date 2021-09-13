package com.example.colornote.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    // Status constant
    public static class STATUS {
        public final static int RECYCLE_BIN = 0;
        public final static int ARCHIVE = 1;
        public final static int NORMAL = 2;
    }

    // task type
    public static class TASK_TYPE {
        public final static int ALL_TASK = 0;
        public final static int TEXT_TASK = 1;
        public final static int CHECKLIST_TASK = 2;
    }

    // backup table type
    public static class BACKUP_TYPE {
        public final static int ALL_TABLE = 0;
        public final static int NOTES_TABLE = 1;
        public final static int CALENDAR_TABLE = 2;
    }

    // backup status
    public static class BACKUP_STATUS {
        public final static int ALL_STATUS = 0;
        public final static int NORMAL_STATUS = 1;
        public final static int ARCHIEVED_STATUS = 2;
    }

    // Settings VIEW
    public static class VIEW {
        public final static int LIST = 0;
        public final static int  DETAILS = 1;
        public final static int GRID = 2;
        public final static int LARGE_GRID = 3;
    }

    // Color default
    public final static String MAIN_COLOR = "#FFD54F";
    public final static String SUB_COLOR = "#FFC107";

    // to Milliseconds
    public static class TIME_TO_MILL {
        public final static long YEAR = 31556952000L;
        public final static long MONTH = 2629800000L;
        public final static long WEEK = 604800000L;
        public final static long DAY = 86400000L;
        public final static long HOUR = 3600000L;
        public final static long MINUTE = 60000L;
        public final static long SECOND = 1000L;
    }

    public static class SORT_BY {
        public final static int NO_SORT = 0;
        public final static int MODIFIED_TIME = 1;
        public final static int ALPHABECALLY = 2;
        public final static int COLOR = 3;
        public final static int REMINDER = 4;
    }

//     public static String textSignin = "Not signed in";
    public static int num_click = 0;

}
