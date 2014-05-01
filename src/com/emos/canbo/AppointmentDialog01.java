package com.emos.canbo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppointmentDialog01 extends Dialog {

	/**
	 */
	Context ctx;
	
	/**
	 */
	Button appoint_dl_01_btn_cancel;
	/**
	 */
	Button appoint_dl_01_btn_next;
	
	public AppointmentDialog01(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.ctx = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//set contentView
		setContentView(R.layout.layout_appoint_dialog_01);
		
		//find view
		appoint_dl_01_btn_cancel = (Button)findViewById(R.id.appoint_dl_01_btn_cancel);
		appoint_dl_01_btn_next = (Button)findViewById(R.id.appoint_dl_01_btn_next);
		
		//set listener
		appoint_dl_01_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppointmentDialog01.this.dismiss();
			}
		});
		
		appoint_dl_01_btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppointmentDialog02 mAppointmentDialog02 = new AppointmentDialog02(ctx, "‘§‘º - ÷Û∑π");
				mAppointmentDialog02.show();
				AppointmentDialog01.this.dismiss();
			}
		});
	}

}
