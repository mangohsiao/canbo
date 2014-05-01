package com.emos.canbo.monitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CamDb extends SQLiteOpenHelper{
	
	private final static String DATABASE_NAME = "camDataBase";
	private final static int DATABASE_VERSION = 2; 

	public final static String CAM_TABLE = "cam_table";
	public final static String VISION_NODE_TABLE = "vision_node_table";
	
//	private final static String TABLE_NAME = "cam_table"; 
	private final static String CREATE_cam_table_SQL = 
			"CREATE TABLE " + CAM_TABLE + "(" +
			"cam_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"cam_ip VARCHAR(32), " +
			"cam_port Integer, " +
			"cam_username VARCHAR(64), " +
			"cam_pswd VARCHAR(64), " +
			"cam_channel Integer, " +
			"cam_name VARCHAR(64), " +
			"cam_desc VARCHAR(64));";
	
	private final static String CREATE_vision_node_table_SQL =
			"CREATE TABLE " + VISION_NODE_TABLE + "( " +
			"node_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"cam_id INTEGER CONSTRAINT CAM_ID_FK REFERENCES cam_table(cam_id) ON DELETE CASCADE, " +
			"node_index INTEGER, " +
			"node_name VARCHAR(64), " +
			"node_desc VARCHAR(64));";
	
	public CamDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO create table
    	System.out.println("create a database");
    	try {
            db.execSQL(CREATE_cam_table_SQL);
            db.execSQL(CREATE_vision_node_table_SQL);
		} catch (Exception e) {
	    	System.out.println(e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
