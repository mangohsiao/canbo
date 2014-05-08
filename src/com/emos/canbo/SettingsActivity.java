/**
 * 
 */
package com.emos.canbo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.emos.canbo.update.MyHttpUtil;
import com.emos.canbo.update.UpdateComponent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author Administrator
 *
 */
public class SettingsActivity extends Activity implements View.OnClickListener{

	private final static int MSG_DIALOG_CANCEL = 301;
	
	/**
	 */
	LinearLayout setting_ip_adr_linear;
	/**
	 */
	LinearLayout setting_port_linear;
	/**
	 */
	LinearLayout setting_url_linear;
	
	/**
	 */
	LinearLayout setting_line_update;
	
	/**
	 */
	ProgressDialog pgDialog = null;
	
	/**
	 */
	UpdateComponent updateComponent = new UpdateComponent(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setting_ip_adr_linear = (LinearLayout)findViewById(R.id.setting_ip_adr_linear);
		setting_port_linear = (LinearLayout)findViewById(R.id.setting_port_linear);
		setting_url_linear = (LinearLayout)findViewById(R.id.setting_url_linear);
		
		setting_line_update = (LinearLayout)findViewById(R.id.setting_line_update);

		setting_ip_adr_linear.setOnClickListener(this);
		setting_port_linear.setOnClickListener(this);
		setting_url_linear.setOnClickListener(this);
		
		setting_line_update.setOnClickListener(this);
		
		
		
//		setting_ip_adr_linear.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Dialog dl = new Dialog(SettingsActivity.this);
//				dl.setTitle("gogo");
//				dl.show();
//			}
//		});
	}
	


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Bundle bd = new Bundle();
		Intent intent = new Intent(SettingsActivity.this, SettingInput.class);
		switch(view.getId()){
		
		case R.id.setting_ip_adr_linear:
			bd.putString("setting", "ip");
			intent.putExtras(bd);
			startActivity(intent);
			break;
		case R.id.setting_port_linear:
			bd.putString("setting", "port");
			intent.putExtras(bd);
			startActivity(intent);
			break;
		case R.id.setting_url_linear:
			bd.putString("setting", "url");
			intent.putExtras(bd);
			startActivity(intent);
			break;
			
		case R.id.setting_line_update:
			updateComponent.check_update_with_alert();
			break;
		}
	}
}
