/**
 * 
 */
package com.emos.canbo;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.emos.canbo.log.FileLog;
import com.emos.canbo.monitor.MonitorActivity;
import com.emos.canbo.security.SecurityActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class MainMenuActivity extends ExActivity implements View.OnClickListener{

	/**
	 */
	Button btn_main_menu_rom;
	/**
	 */
	Button btn_main_menu_dev;
	/**
	 */
	Button btn_main_menu_mode;
	/**
	 */
	Button btn_main_menu_security;
	/**
	 */
	Button btn_main_menu_monitor;
	
	/* 
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		FileLog.log("hello");
		FileLog.log("im kelly");
		
		//set View
		setContentView(R.layout.activity_main_menu);
		try {
			getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
		}catch (NoSuchFieldException e) {
			// Ignore since this field won't exist in most versions of Android
		}catch (IllegalAccessException e) {
			Log.v("Main", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);
		}
		
		//find view
		btn_main_menu_rom = (Button)findViewById(R.id.btn_main_menu_rom);
		btn_main_menu_dev = (Button)findViewById(R.id.btn_main_menu_dev);
		btn_main_menu_mode = (Button)findViewById(R.id.btn_main_menu_mode);
		btn_main_menu_security = (Button)findViewById(R.id.btn_main_menu_security);
		btn_main_menu_monitor = (Button)findViewById(R.id.btn_main_menu_monitor);
		
		//listenerInitial()
		listenerInitial();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
////		return super.onCreateOptionsMenu(menu);
//		return true;
//	}
//
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = getMenuInflater();
//		menu.clear();
//		inflater.inflate(R.menu.mainmenu, menu);
////		return super.onPrepareOptionsMenu(menu);
//		return true;
//	}

	private void listenerInitial() {
		btn_main_menu_rom.setOnClickListener(this);
		btn_main_menu_dev.setOnClickListener(this);
		btn_main_menu_mode.setOnClickListener(this);
		btn_main_menu_security.setOnClickListener(this);
		btn_main_menu_monitor.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_main_menu_rom:
			intent = new Intent(MainMenuActivity.this, RoomSightActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_main_menu_dev:
			
			break;
		case R.id.btn_main_menu_mode:
			
			break;
		case R.id.btn_main_menu_security:
			intent = new Intent(MainMenuActivity.this, com.emos.canbo.security.SecurityActivity2.class);
			startActivity(intent);
			break;
		case R.id.btn_main_menu_monitor:
			intent = new Intent(MainMenuActivity.this,com.emos.canbo.monitor.CamListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
