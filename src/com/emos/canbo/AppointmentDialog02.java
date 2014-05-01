package com.emos.canbo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppointmentDialog02 extends Dialog {

	/**
	 */
	Context ctx;
	
	/**
	 */
	String input;
	
	/**
	 */
	Button appoint_dl_02_btn_cancel;
	/**
	 */
	Button appoint_dl_02_btn_ok;
	
	public AppointmentDialog02(Context context, String input) {
		super(context);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		
		this.input = input;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//set contentView
		setContentView(R.layout.layout_appoint_dialog_02);
		
		//set title
		setTitle(input);
		
		//find view
		appoint_dl_02_btn_cancel = (Button)findViewById(R.id.appoint_dl_02_btn_cancel);
		appoint_dl_02_btn_ok = (Button)findViewById(R.id.appoint_dl_02_btn_ok);
		
		//set listener
		appoint_dl_02_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppointmentDialog02.this.dismiss();
			}
		});
		
		appoint_dl_02_btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppointmentDialog02.this.dismiss();				
			}
		});
	}
}
