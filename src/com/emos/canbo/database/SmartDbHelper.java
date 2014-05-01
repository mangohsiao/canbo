package com.emos.canbo.database;

import com.emos.canbo.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SmartDbHelper {
	final static String fileName = "smart.db";
	
	public SmartDbHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public final static SQLiteDatabase openSQLite(Context context){
		
		SQLiteDatabase mdb = null;
		String path = context.getFilesDir() + "/" + fileName;
		File db_file = new File(path);
		if(false == db_file.exists()){
			System.out.println(path + " not exists.");	
			//copy db file
			InputStream is = context.getResources().openRawResource(R.raw.smart);
			try {
//				FileOutputStream fos = new FileOutputStream(db_file);
				FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				byte[] buffer = new byte[8192]; 
                int count = 0; 
                // 开始复制dictionary.db文件 
                while ((count = is.read(buffer)) > 0) { 
                    fos.write(buffer, 0, count); 
                }
                fos.flush(); 
                fos.close(); 
                is.close(); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
//			if(db_file.delete()){
//				System.out.println(path + " is deleted.");	
//			}
		}
		try {
			mdb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);	
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "open db failed.", Toast.LENGTH_SHORT).show();
		}	
		return mdb;
	}
}
