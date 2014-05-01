package com.emos.canbo;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class DBhelper {
	final static String path = "/smart.db";
	
	public DBhelper() {
		// TODO Auto-generated constructor stub
	}
	
	public final static SQLiteDatabase openSQLite(){
		SQLiteDatabase mdb = null;
		String db_path = Environment.getExternalStorageDirectory().getPath() + path;
		File db_file = new File(db_path);
		if (!db_file.exists()){
			Log.i("db", "not exist.");
			return null; 
		}else{
			Log.i("db", "dbfile exist.");
			try{
				mdb = SQLiteDatabase.openOrCreateDatabase(db_file, null);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return mdb;
	}
}
