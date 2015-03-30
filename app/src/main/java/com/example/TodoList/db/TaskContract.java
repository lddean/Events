package com.example.TodoList.db;

import android.provider.BaseColumns;

public class TaskContract {
	public static final String DB_NAME = "com.example.TodoList.db.tasks";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "tasks";


	public class Columns {
		public static final String TASK = "task";
		public static final String _ID = BaseColumns._ID;
        public static final String Repeat = "None";
        public static final String Year = "Year";
        public static final String Month  = "Month";
        public static final String Day = "Day";
        public static final String Task_date = "Date";
	}
}
