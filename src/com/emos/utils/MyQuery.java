package com.emos.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.emos.canbo.DBhelper;
import com.emos.canbo.database.SmartDbHelper;

public class MyQuery {
	public static List< Map<String, String>> getSubUIMap(Context context, String op_dev, String op_id) {
		Log.v("MyQuery", "op_dev:" + op_dev + " op_id:" + op_id);
		SQLiteDatabase db = SmartDbHelper.openSQLite(context);
		List< Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(db==null){
			Log.i("db", "db is null");
			return null;
		}else{
			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Operation WHERE op_dev=? AND op_parent=?;",new String[]{op_dev,op_id});

				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					Map<String,String> map = new HashMap<String, String>();
					for(int i=0;i<c.getColumnCount();i++){
						map.put(c.getColumnName(i), c.getString(i));
					}
					list.add(map);
				}							
				c.close();
			} catch (Exception e) {
				// 
				e.printStackTrace();
				return null;
			} finally{
				db.close();
			}
		}
		return list;
		
	}

	public static String getType2OpCodeMap(Context context, String op_dev) {
		Log.v("MyQuery", "getType2OpCodeMap() - op_dev:" + op_dev);
		String op_code = null;
		SQLiteDatabase db = SmartDbHelper.openSQLite(context);
		if(db==null){
			Log.i("db", "db is null");
			return null;
		}else{
			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Operation WHERE op_dev=? AND op_type='2';",new String[]{op_dev});

				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					op_code = c.getString(c.getColumnIndex("op_code"));
				}							
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally{
				db.close();
			}
		}
		return op_code;
	}
	
	public static Map<String, String> getType2SubOpMap(Context context, String op_dev, String op_id) {
		Log.v("MyQuery", "op_dev:" + op_dev + " op_id:" + op_id);
		SQLiteDatabase db = SmartDbHelper.openSQLite(context);
		Map<String,String> map = new HashMap<String, String>();
		if(db==null){
//			Log.i("db", "db is null");
			return null;
		}else{
//			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Operation WHERE op_dev=? AND op_parent=?;",new String[]{op_dev,op_id});
//				Log.i("db", "db query done");
				while(c.moveToNext()){
					for(int i=0;i<c.getColumnCount();i++){
						map.put(c.getColumnName(i), c.getString(i));
					}
				}
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally{
				db.close();
			}
		}
		return map;
	}
}
