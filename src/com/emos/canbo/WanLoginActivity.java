/**
 * 
 */
package com.emos.canbo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class WanLoginActivity extends LinkedActivity {

	/**
	 */
	Button btn_wan_login_back;
	/**
	 */
	Button btn_wan_login_yes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//set View
		setContentView(R.layout.activity_wan_login);
		
		//initial
		initial();
		
	}

	private void initial() {
		// TODO Auto-generated method stub
		
		// binding resources
		btn_wan_login_back = (Button)findViewById(R.id.btn_wan_login_back);
		btn_wan_login_yes =  (Button)findViewById(R.id.btn_wan_login_yes);
		
		// binding listener
		btn_wan_login_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WanLoginActivity.this.finish();
			}
		});

		btn_wan_login_yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
}
