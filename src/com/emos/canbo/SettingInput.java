package com.emos.canbo;

import com.emos.utils.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingInput extends Activity {

	final static int IP = 10;
	final static int PORT = 11;
	final static int URL = 12;	
	
	/**
	 */
	Button setting_input_cancel_btn;
	/**
	 */
	Button setting_input_ok_btn;
	/**
	 */
	EditText setting_input_edtx;
	/**
	 */
	TextView setting_input_title_txv;

	/**
	 */
	SettingInputBtnListener listener = new SettingInputBtnListener();
	
	/**
	 */
	String setting;
	/**
	 */
	int settingId;
	/*
	 * 10 - [ip]
	 * 11 - [port]
	 * 12 - [url]
	 *  
	 * ***/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bd = intent.getExtras();
		if(bd!=null){
			//analysis the intent data.
			/*
			 * [ip]
			 * [port]
			 * [url]
			 * 
			 * ***/
			setting = bd.getString("setting");
			if(setting.equals("ip")){
				settingId = IP;
			}else if(setting.equals("port")){
				settingId = PORT;
			}else if(setting.equals("url")){
				settingId = URL;			
			}
//			}else if(bd.getString("setting").equals("ip")){
//				
//			}
		}else{
			
		}

		setContentView(R.layout.activity_setting_input);

		setting_input_cancel_btn = (Button)findViewById(R.id.setting_input_cancel_btn);
		setting_input_ok_btn = (Button)findViewById(R.id.setting_input_ok_btn);
		setting_input_edtx = (EditText)findViewById(R.id.setting_input_edtx);
		setting_input_title_txv = (TextView)findViewById(R.id.setting_input_title_txv);

		/** set View Layout **/
		switch(settingId){
		case IP:
			setting_input_edtx.setText(
					((MyApp)getApplicationContext()).getConIp()
					);
			break;
		case PORT:
			setting_input_edtx.setText(
					((MyApp)getApplicationContext()).getConPort()
					);
			break;
		case URL:
			setting_input_edtx.setText(
					((MyApp)getApplicationContext()).getConUrl()
					);
			break;
		default:
			break;
		}
		
		setting_input_title_txv.append(setting);
		setting_input_cancel_btn.setOnClickListener(listener);
		setting_input_ok_btn.setOnClickListener(listener);
	}
	
	public class SettingInputBtnListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Settings mset = new Settings(SettingInput.this);
			SharedPreferences sharedPreferences = getSharedPreferences(CanboCommon.GlobalSettingsName, Context.MODE_PRIVATE);
			
			switch(v.getId()){
			case R.id.setting_input_cancel_btn:
				SettingInput.this.finish();
				break;
			case R.id.setting_input_ok_btn:
				String strFromEditext = setting_input_edtx.getText().toString();
				switch (settingId) {
				case IP:
					((MyApp)getApplicationContext()).setConIp(strFromEditext);
					sharedPreferences.edit().putString(CanboCommon.CONTROL_CONNECT_IP_NAME, strFromEditext);
					break;
				case PORT:
					((MyApp)getApplicationContext()).setConPort(strFromEditext);
					sharedPreferences.edit().putString(CanboCommon.CONTROL_CONNECT_PORT_NAME, strFromEditext);
					break;
				case URL:
					((MyApp)getApplicationContext()).setConUrl(strFromEditext);
					break;
				default:
					break;
				}
				//write to db
				mset.setOption("con_" + setting, strFromEditext);
				mset.finish();
				//combine the ip+port+url
				((MyApp)getApplicationContext()).getConTotalStr();
				SettingInput.this.finish();
				break;
			}
		}
		
	}
}
