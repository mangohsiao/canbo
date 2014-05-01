package com.emos.canbo.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

public class FileLog {
	static private FileLog instance = null;
	public static FileLog getInstance() {
		if(instance == null){
			instance = new FileLog();
			File dir = new File(Environment.getExternalStorageDirectory() + "/canboData/Log");
			if(!dir.exists()){
				if(dir.mkdirs() == false){
					System.out.println("dir.mkdirs() == false");
				}
			}
		}
		return instance;
	}
	
	public FileLog() {
		System.out.println("FileLog() construction¡£");
	}
	
	static private File file = null;
	public static File getFile() {
		getInstance();
		String dateStr = getDate();
		if(file == null){
			file = new File(Environment.getExternalStorageDirectory() + "/canboData/Log/" + dateStr + ".txt");
			if(!file.exists()){
				try {
					if(false == file.createNewFile()){
						System.out.println("false == file.createNewFile()");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("file == null\n" + file.getPath().toString());
		}else if(false == file.getName().equals(dateStr)){
			System.out.println("name == " + file.getName());
			
			file = new File(Environment.getExternalStorageDirectory() + "/canboData/Log/" + dateStr + ".txt");
			if(!file.exists()){
				try {
					if(false == file.createNewFile()){
						System.out.println("false == file.createNewFile() 2");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			
		}
		return file;
	}
	

	private static String getDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}
	
	private static String getTime() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}

	private static BufferedWriter output = null; 
	public static void log(String msg) {
		getInstance();
		try {
			if(output == null){
				output = new BufferedWriter(new FileWriter(getFile(), true));
			}
			output.append(getTime() + " " + msg + "\r\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void close() {
		getInstance();
		try {
			output.close();
			output = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
