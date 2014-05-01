package com.emos.canbo.update;

import junit.runner.Version;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class VersionUpdateActivity extends Activity{

	/**
	 */
	Handler handler = null;
	
	/**
	 */
	String new_ver_name = "";
	/**
	 */
	int new_version = -1;
	
	/**
	 */
	ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		pd = new ProgressDialog(this);
		
		ver_check();
		//check version
		
		/* is the newest */
		
		/* update to newest  */
		
		
	}
	
	private void ver_check() {
		pd.show();
		new MyThread().run();
	}
	
	class MyHandler extends Handler{
		public final static int GET_VER_DONE = 101;
		
		Context ctx;
		
		public MyHandler(Context context) {
			// TODO Auto-generated constructor stub
			this.ctx = context;
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			Bundle mBundle = msg.getData();
			switch (mBundle.getInt("msg_type")) {
			case GET_VER_DONE:
				pd.cancel();
				new AlertDialog.Builder(VersionUpdateActivity.this)
				.setTitle("返回结果").setMessage("获取版本成功 - " + mBundle.getInt("ver_code")).show();
				break;

			default:
				break;
			}
		}
		
	}
	
	class MyThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				sleep(3000);
				Message msg = handler.obtainMessage();
				Bundle bd = new Bundle();
				bd.putInt("msg_type", MyHandler.GET_VER_DONE);
				bd.putInt("ver_code", 123);
				msg.setData(bd);
				handler.sendMessage(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
