/**
 * 
 */
package com.emos.canbo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.emos.utils.SysApplication;

/**
 * @author Administrator
 *
 */
public class ExActivity extends LinkedActivity {

	final static int MENU_ITEM_SETTINGS = 19;
	final static int MENU_ITEM_ABOUT = 20;
	final static int MENU_ITEM_EXIT = 21;

	/* 
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ITEM_SETTINGS, MENU_ITEM_SETTINGS, "设置");
		menu.add(0, MENU_ITEM_ABOUT, MENU_ITEM_ABOUT, "关于");
		menu.add(0, MENU_ITEM_EXIT, MENU_ITEM_EXIT, "退出");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		// SETTINGS
		case MENU_ITEM_SETTINGS:
			Intent intent = new Intent(getApplicationContext(), SettingActivity2.class);
			startActivity(intent);
			break;
			
		// about
		case MENU_ITEM_ABOUT:
//			Toast.makeText(getApplicationContext(), "华南理工大学", Toast.LENGTH_LONG).show();
			AboutDialog mAboutDialog = new AboutDialog(ExActivity.this);
			mAboutDialog.setTitle("About");
			mAboutDialog.show();
			break;
			
		// exit			
		case MENU_ITEM_EXIT:
			Dialog exitAlert = new AlertDialog.Builder(this).
			setTitle("提示").
			setMessage("确定退出程序?").
			setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					SysApplication.getInstance().exit();
					
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create();
			exitAlert.show();
			break;
			
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
