/**
 * 
 */
package com.emos.canbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class ConnectChooseActivity extends LinkedActivity {

	/**
	 */
	Button btn_connect_choose_lan;
	/**
	 */
	Button btn_connect_choose_wan;
	
	/* 
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//set View
		setContentView(R.layout.activity_connect_choose);
		
		//find view
		btn_connect_choose_lan = (Button)findViewById(R.id.btn_connect_choose_lan);
		btn_connect_choose_wan = (Button)findViewById(R.id.btn_connect_choose_wan);
		
		//listener initial
		initial();
	}

	/* 
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	private void initial(){
		btn_connect_choose_lan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConnectChooseActivity.this, LanLoginActivity.class);
				startActivity(intent);
			}
		});
		
		btn_connect_choose_wan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConnectChooseActivity.this, WanLoginActivity.class);
				startActivity(intent);
			}
		});
	}
}
