package com.emos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class DialogWaiting extends ProgressDialog {

	/**
	 */
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};
	
	public DialogWaiting(Context context) {
		super(context);
	}

	/**
	 * @return
	 */
	protected Handler getHandler() {
		return handler;
	}
	
	
}
