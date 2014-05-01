package com.emos.canbo.update;

import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.net.SocketException;

import org.json.JSONObject;

import com.emos.canbo.tools.MD5Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MDBSyncClient implements Runnable{

	final static String fileName = "smart.png";
	
	Context ctx = null;
	
	Handler handler = null;
	MTcpClient mClient = null;
	File db_file = null;
	
	public MDBSyncClient(Context ctx, Handler handler) {
		super();
		this.ctx = ctx;
		this.handler = handler;
	}

	private static final String ip = "125.216.243.235";
	private static final int port = 8002;
	
	@Override
	public void run() {
		mClient = new MTcpClient();
		mClient.setProgressListener(new MTcpClient.IProgressListener() {
			
			@Override
			public void report(int progress) {
//				System.out.println("report: " + progress);
				Message msg = handler.obtainMessage();
				msg.what = MDBSyncMessage.WHAT_PROGRESS;
				Bundle bd = new Bundle();
				bd.putInt("progress", progress);
				msg.setData(bd);
				handler.sendMessage(msg);
			}
		});
		mClient.setStartDownloadListener(new MTcpClient.IStartDownloadListener() {
			
			@Override
			public void startDownload() {
				handler.sendEmptyMessage(MDBSyncMessage.WHAT_START_DOWNLOAD);
			}
		});
		
		try {
			String path = ctx.getFilesDir() + "/" + fileName;
			db_file = new File(path);
			String nowMD5 = MD5Util.getFileMD5(db_file);
			if(mClient == null){
				mClient = new MTcpClient();
			}
			
			mClient.open(ip, port);
			mClient.connect();
			JSONObject reqJson = new JSONObject();
			reqJson.put("TYPE", MDBSyncMessage.REQ_TYPE_CHECK);
			reqJson.put("nowMD5", nowMD5);
//			System.out.println(reqJson.toString());
			
			String res;
			res = mClient.requestByText(reqJson.toString());
			mClient.close();
			//processing result
			ParsingRes(res);
			
		} catch (MTcpClientException e) {
			switch(e.what){
			case MTcpClientException.FILE_EXIST:
				System.out.println("MTcpClientException.FILE_EXIST");
				break;
			case MTcpClientException.FILE_VERIFY_FAILED:
				System.out.println("MTcpClientException.FILE_VERIFY_FAILED");
				break;
			case MTcpClientException.SOCKET_INPUT_ERR:
				System.out.println("MTcpClientException.SOCKET_INPUT_ERR");
				break;
			}
		} catch (SocketException e) {
			Log.v("mango", e.getMessage());
			handler.sendEmptyMessage(MDBSyncMessage.WHAT_NETWORK_ERR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				mClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void ParsingRes(String res) {
		System.out.println(res);
		try {
			JSONObject resJson = new JSONObject(res);
			int RES = resJson.getInt("RES");
			switch (RES) {
			//no need to update
			case 202:
				handler.sendEmptyMessage(4417);
				break;
			case 200:
				System.out.println("need to update.");

				if(db_file == null){
					break;
				}
				String sum = resJson.getString("MD5");
				int fileSize = resJson.getInt("SIZE");

				JSONObject reqJson = new JSONObject();
				mClient.connect();
				reqJson.put("TYPE", MDBSyncMessage.REQ_TYPE_GET_FILE);
//				String path = ctx.getFilesDir() + "/" + fileName;
//				File file = new File(path);
				mClient.requestByStreamWithCheck(reqJson.toString(), db_file, fileSize, sum);
				mClient.close();
				break;
				
			case 333:
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
