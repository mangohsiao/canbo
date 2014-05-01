package com.emos.canbo.monitor;

import com.emos.canbo.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class DialogMsgBack extends Dialog implements android.view.View.OnClickListener{
	
	public final static String MSG_NAME = "MSG_TYPE";
	
	/**
	 */
	Handler mHandler = null;
	/**
	 */
	int val_YES;

	/**
	 */
	Bundle extras = null;
	
	/**
	 */
	LinearLayout dialog_msgback_line_yes = null;
	/**
	 */
	LinearLayout dialog_msgback_line_no = null;
	
	public DialogMsgBack(Context ctx, Handler handler, int val_YES) {
		super(ctx);
		this.mHandler = handler;
		this.val_YES = val_YES;
	}
	
	public DialogMsgBack(Context ctx, Handler handler, int val_YES, Bundle extras) {
		super(ctx);
		this.mHandler = handler;
		this.val_YES = val_YES;
		this.extras = extras;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_msg_back);
		
		dialog_msgback_line_yes = (LinearLayout)findViewById(R.id.dialog_msgback_line_yes);
		dialog_msgback_line_no = (LinearLayout)findViewById(R.id.dialog_msgback_line_no);

		dialog_msgback_line_yes.setOnClickListener(this);
		dialog_msgback_line_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_msgback_line_yes:
			Message msg = mHandler.obtainMessage();
			Bundle bd = new Bundle();
			if(extras!=null){
				bd.putAll(extras);
			}
			bd.putInt(MSG_NAME, val_YES);
			msg.setData(bd);
			mHandler.sendMessage(msg);
			DialogMsgBack.this.dismiss();
			break;

		case R.id.dialog_msgback_line_no:
			DialogMsgBack.this.dismiss();
			break;

		default:
			break;
		}
	}
}
