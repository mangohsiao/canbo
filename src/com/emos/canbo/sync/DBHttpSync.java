package com.emos.canbo.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Locale;

import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.emos.canbo.tools.MD5Util;
import com.emos.utils.RequestByHttpPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DBHttpSync implements Runnable{
	public static final int TYPE_DOWN = 911;
	public static final int TYPE_CHECK = 101;
	
	/* STATE */
	public final static int WHAT_PROGRESS = 4410;
	public final static int WHAT_START_DOWNLOAD = 4411;
	public static final int WHAT_NETWORK_ERR = 4412;
	public final static int WHAT_END_DOWNLOAD = 4413;
	public static final int WHAT_IO_ERR = 4414;
	
	public final static int WHAT_SYNC_OK = 1100;
	public final static int WHAT_NO_NEED_SYNC = 1101;
	public final static int WHAT_DOWNLOAD_FAILED = 1102;
	public final static int WHAT_CONNECT_FAILED = 1103;
	public final static int WHAT_PARSING_FAILED = 1104;
	public final static int WHAT_VERIFY_FAILED = 1105;

	public String REQ_URL = "http://125.216.243.235:8080/CanboServer/dbcheck";
	private String file_name = "mango.png";
	private File db_file;
	
	Handler handler;
	Context context;
	public DBHttpSync(Handler handler, Context context) {
		super();
		this.handler = handler;
		this.context = context;
	}
	
	@Override
	public void run() {
		/* initial file */
		String path = context.getFilesDir() + "/" + file_name;
		db_file = new File(path);
		String nowMD5 = "";
		if(db_file.exists()){
//			nowMD5 = MD5Util.getFileMD5(db_file);
			/* get last MD5 from SharePerference */
			SharedPreferences shpf = context.getSharedPreferences("SYNC", Context.MODE_PRIVATE);
			nowMD5 = shpf.getString("LAST_MD5", "");			
		}
		/* check db */
		checkDB(nowMD5);
	}
	
	private void checkDB(String nowMD5) {
		// TODO Auto-generated method stub
		JSONObject reqJson = new JSONObject();
		try {
			reqJson.put("TYPE", TYPE_CHECK);
			reqJson.put("nowMD5", nowMD5);
			String resStr = HttpPostUtils.doPostJson(reqJson, REQ_URL);
			JSONObject resJson = new JSONObject(resStr);
			int RES = resJson.getInt("RES");
			switch (RES) {
			case 202:	//newest
				handler.sendEmptyMessage(WHAT_NO_NEED_SYNC);
				break;

			case 200:	//to sync
				String MD5 = resJson.getString("MD5");
				int SIZE = resJson.getInt("SIZE");
				handleDownload(db_file,SIZE,MD5);
				break;

			default:
				break;
			}			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			handler.sendEmptyMessage(WHAT_NETWORK_ERR);
			e.printStackTrace();
		} catch (IOException e) {
			handler.sendEmptyMessage(WHAT_IO_ERR);
			e.printStackTrace();
		}
	}

	private void handleDownload(File db_file2, int sIZE, String mD5) {
		// TODO Auto-generated method stub
		try {
			JSONObject reqJson = new JSONObject();
			reqJson.put("TYPE", 911);			
			URL url = new URL(REQ_URL);
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setConnectTimeout(8000);
			httpConnection.setRequestMethod("POST");
			OutputStream reqOS = httpConnection.getOutputStream();
			reqOS.write(reqJson.toString().getBytes());
			reqOS.flush();
			InputStream in = httpConnection.getInputStream();
			if(db_file2 == null){
				System.out.println("db_file == null");
				in.close();
				return;
			}
			OutputStream out = new FileOutputStream(db_file2);
			handler.sendEmptyMessage(WHAT_START_DOWNLOAD);
			byte[] buf = new byte[4096];
			int readLength, progress = -1, count=0;
			while((readLength = in.read(buf)) != -1){
				out.write(buf, 0, readLength);
				count += readLength;
				progress = (int)((double)count/sIZE *100);
				Bundle bd = new Bundle();
				bd.putInt("progress", progress);
				Message msg = handler.obtainMessage();
				msg.what = WHAT_PROGRESS;
				msg.setData(bd);
				handler.sendMessage(msg);
//				System.out.println("progress: " + progress);
			}
			
			/* check MD5 */
			String fileSum = MD5Util.getFileMD5(db_file2).toUpperCase(Locale.US);
			System.out.println("sum: " + fileSum);
			if(fileSum.equals(mD5.toUpperCase(Locale.US))){
				/* SAVE THE MD5 TO LAST_MD5 */
				SharedPreferences shpf = context.getSharedPreferences("SYNC", Context.MODE_PRIVATE);
				shpf.edit().putString("LAST_MD5", fileSum).commit();
				handler.sendEmptyMessage(WHAT_SYNC_OK);
			}else{
				handler.sendEmptyMessage(WHAT_VERIFY_FAILED);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(WHAT_PARSING_FAILED);
			e.printStackTrace();
		} catch (SocketException e) {
			handler.sendEmptyMessage(WHAT_NETWORK_ERR);
			e.printStackTrace();
		} catch (IOException e) {
			handler.sendEmptyMessage(WHAT_DOWNLOAD_FAILED);
			e.printStackTrace();
		} finally{
			handler.sendEmptyMessage(WHAT_END_DOWNLOAD);
		}
		
	}

	public void setREQ_URL(String rEQ_URL) {
		REQ_URL = rEQ_URL;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public static void syncDB(Handler handler, Context context, String fileName, String url) {
		DBHttpSync work = new DBHttpSync(handler,context);
		work.setFile_name(fileName);
		work.setREQ_URL(url);
		new Thread(work).start();
	}
}
