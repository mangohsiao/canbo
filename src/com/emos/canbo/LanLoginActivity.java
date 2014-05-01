/**
 * 
 */
package com.emos.canbo;

import com.emos.utils.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class LanLoginActivity extends LinkedActivity {

	/**
	 */
	Button btn_lan_login_yes;
	/**
	 */
	EditText edtx_lan_log_ip;

//	//default
//	String conIp = "125.216.243.205";
//	String conPort = "8080";
//	String conUrl = "/huabo/request";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//set View
		setContentView(R.layout.activity_lan_login);
		
		//find view
		btn_lan_login_yes = (Button)findViewById(R.id.btn_lan_login_yes);
		edtx_lan_log_ip = (EditText)findViewById(R.id.edtx_lan_log_ip);
		
		Settings mset = new Settings(LanLoginActivity.this);
		edtx_lan_log_ip.setHint(mset.getOption("con_ip"));
		mset.finish();
		//TODO processing input
		
		//listenerInitial()
		listenerInitial();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	private void listenerInitial() {
		btn_lan_login_yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//set IP from Editext input
				if(edtx_lan_log_ip.getText().toString().equals("")){
					
				}else{
					((MyApp)getApplicationContext()).setConIp(edtx_lan_log_ip.getText().toString());
				}
				
				Bundle bd = new Bundle();	
				Intent intent = new Intent(LanLoginActivity.this, MainMenuActivity.class);
				intent.putExtras(bd);
				startActivity(intent);
			}
		});
	}
}
