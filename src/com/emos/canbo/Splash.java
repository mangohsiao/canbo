package com.emos.canbo;

import com.emos.canbo.user.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class Splash extends Activity {

	/**
	 */
	private final int SPLASH_DISPLAY_TIME = 600;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		//forward to mainActivity
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				Intent mainIntetn = new Intent(Splash.this, RoomSightActivity.class);
//				Intent mainIntetn = new Intent(Splash.this, LanLoginActivity.class);
//				Intent mainIntetn = new Intent(Splash.this, com.emos.canbo.monitor.CamListActivity.class);
//				Intent mainIntetn = new Intent(Splash.this, MainMenuActivity.class);
				Intent mainIntetn = new Intent(Splash.this, LoginActivity.class);
//				Intent mainIntetn = new Intent(Splash.this, ConnectChooseActivity.class);
//				Intent mainIntetn = new Intent(Splash.this, com.test.TestActivity.class);
				Splash.this.startActivity(mainIntetn);
				Splash.this.finish();
				Splash.this.overridePendingTransition(R.anim.enteralpha, R.anim.exitalpha);
			}
		}, SPLASH_DISPLAY_TIME);
	}	
	
}
