package com.emos.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Settings {
	private final static String DATABASE_NAME = "settingsdb";
	private final static int DATABASE_VERSION = 2; 
	private final static String TABLE_NAME = "settingtable";
	/**
	 */
	SQLiteDatabase writeDb = null;
	/**
	 */
	SQLiteDatabase readDb = null;

	public Settings(Context ctx) {
		MyDBOpenHelper mdbhelper = new MyDBOpenHelper(ctx);
		writeDb = mdbhelper.getWritableDatabase();
		readDb = mdbhelper.getReadableDatabase();
	}
	
	//����������
	public int addOption(String optionName, int length) {
		
		String TEST_SQL = "SELECT * FROM " + TABLE_NAME + ";";
		Cursor cs = readDb.rawQuery(TEST_SQL, null);
		if(cs.getColumnIndex(optionName)>=0){
			return 1;
		}
		String ALTER_SQL = 
				"ALTER TABLE " + TABLE_NAME + 
				" ADD COLUMN " + optionName + 
				" VARCHAR(" + Integer.toString(length) + ") NULL;";
		try {
			writeDb.execSQL(ALTER_SQL);			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	//��ȡ����
	public String getOption(String optionName) {
		String SELECT_SQL = 
				"SELECT " + optionName + 
				" FROM " + TABLE_NAME + 
				" WHERE _id='1';";
		Cursor c = readDb.rawQuery(SELECT_SQL, null);
		if(c.moveToFirst()){
			return c.getString(c.getColumnIndex(optionName));
		}
		return null;
	}
	
	//��������
	public int setOption(String optionName, String value) {
		String UPDATE_SQL = 
				"UPDATE " + TABLE_NAME + 
				" SET " + optionName + "='" + value + 
				"' WHERE _id='1';";
		try {
			writeDb.execSQL(UPDATE_SQL);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	//ɾ������
	public void finish() {
		readDb.close();
		writeDb.close();
	}
}
