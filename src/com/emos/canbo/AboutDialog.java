package com.emos.canbo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutDialog extends Dialog {

	/**
	 */
	Context ctx;
	/**
	 */
	Button about_btn_cancel;
	
	public AboutDialog(Context context) {
		super(context);
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//set contentView
		setContentView(R.layout.layout_about_dialog);
		
		//find View
		about_btn_cancel = (Button)findViewById(R.id.about_btn_cancel);
		
		//set listener
		about_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutDialog.this.dismiss();
			}
		});
	}

}
