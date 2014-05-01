package com.emos.canbo.security;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SecuritySettingActivity extends Activity implements android.view.View.OnClickListener{

	/**
	 */
	SharedPreferences shRef = null;
	
	/**
	 */
	TextView securitysetting_txv_refresh_time = null;
	/**
	 */
	TextView security_ringtone = null;
	/**
	 */
	Button button_back = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_setting);

		button_back = (Button)findViewById(R.id.button_back);
		security_ringtone = (TextView)findViewById(R.id.security_ringtone);
		((Button)findViewById(R.id.button_lingsheng)).setOnClickListener(this);

		//TODO changeName
		shRef = getSharedPreferences(CanboCommon.GlobalSettingsName, MODE_PRIVATE);
		
		/* set listener */
		button_back.setOnClickListener(this);
		
		
		securitysetting_txv_refresh_time = (TextView)findViewById(R.id.securitysetting_txv_refresh_time);
		
		readSetting();
		
	}

	private void readSetting() {
		if(shRef==null){
			return;
		}
		securitysetting_txv_refresh_time.setText("" + shRef.getInt(CanboCommon.SETTING_SECURITY_REFRESH_TIME, -1));
		String myRingStr = shRef.getString(CanboCommon.SECURITYNOTIRINGNAME, null);
		if(myRingStr!=null){
			Uri ringRri = Uri.parse(myRingStr);
			security_ringtone.setText(RingtoneManager.getRingtone(this, ringRri).getTitle(this).toString());
		}else{
			security_ringtone.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_lingsheng:
			Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
	        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
	        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,"…Ë÷√Õ®÷™¡Â…˘");
	        String myRingStr = shRef.getString(CanboCommon.SECURITYNOTIRINGNAME, null);
	        if(null != myRingStr){
		        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(myRingStr));
	        }
	        startActivityForResult(intent,0);
			break;
		case R.id.button_back:
			SecuritySettingActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 0){
			return;
		}
		Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		if(pickedUri == null){
			Log.v("", "pickedUri == null");
			return;
		}
		String title = RingtoneManager.getRingtone(this, pickedUri).getTitle(this);
//		security_ringtone.setText(pickedUri.toString());
		security_ringtone.setText(title.toString());
		shRef.edit().putString(CanboCommon.SECURITYNOTIRINGNAME, pickedUri.toString()).commit();		
	}
}
