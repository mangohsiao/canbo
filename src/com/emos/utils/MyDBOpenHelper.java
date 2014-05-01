package com.emos.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "settingsdb";
	private final static int DATABASE_VERSION = 2; 
	private final static String TABLE_NAME = "settingtable"; 
	private final static String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + 
			"(_id INTEGER PRIMARY KEY AUTOINCREMENT, first VARCHAR(5))"; 
	
	public MyDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
    	System.out.println("create a database");
        db.execSQL(CREATE_TABLE_SQL);
        String INSERT_SQL = "INSERT INTO " + TABLE_NAME + " VALUES ('1','1');";
        db.execSQL(INSERT_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
