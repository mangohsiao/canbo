package com.emos.canbo.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestResult;

import com.emos.canbo.DBhelper;
import com.emos.canbo.MainMenuActivity;
import com.emos.canbo.R;
import com.emos.canbo.database.SmartDbHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SecurityActivity extends Activity implements View.OnClickListener{

	/**
	 */
	LocalService mService = null;
	/**
	 */
	boolean isStart = false;
	/**
	 */
	boolean isBind = false;
	/**
	 */
	Intent servIntent = null;
	
	/**
	 */
	ListView listview = null;
	/**
	 */
	List<SensorDevice> data = null;
	/**
	 */
	MyAdapter adapter = null;
	
	/**
	 */
	SharedPreferences prefSensorUsed = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		data = new ArrayList<SensorDevice>();
		adapter = new MyAdapter(this);
		setContentView(R.layout.activity_security);
		
		prefSensorUsed = getSharedPreferences(SecurityCommon.prefSensorUsedName, MODE_PRIVATE);
		
		servIntent = new Intent(this, LocalService.class);

		getData();
		listview = (ListView)findViewById(R.id.security_listview);
		listview.setAdapter(adapter);
		
		setListener();
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		findViewById(R.id.security_btn_1).setOnClickListener(this);
		findViewById(R.id.security_btn_2).setOnClickListener(this);
		findViewById(R.id.security_btn_3).setOnClickListener(this);
		findViewById(R.id.security_btn_4).setOnClickListener(this);
		findViewById(R.id.security_line_setting).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//clear data and rescan
//		data.clear();
//		getData();
		//notify
//		adapter.notifyDataSetChanged();
		if(localServiceIsStart("com.emos.canbo.security.LocalService")){
//			System.out.println("service OKOKOKOKOK~!!!!!!!!!!!!!!!!!.......");
			bindLocalService();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unBindLocalService();
	}

	private void bindLocalService() {
		// TODO Auto-generated method stub
		if(isBind == false){
			bindService(servIntent, serviceCon, 0);
			isBind = true;
		}
	}
	
	private void unBindLocalService() {
		// TODO Auto-generated method stub
		if (isBind == true) {
//			System.out.println("mango unbind.......");
			unbindService(serviceCon);
			isBind = false;
		}
	}

	private void getData() {
		/** get device in room **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(this);
		if(db==null){
			Log.i("db", "db is null");
		}else{
//			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Device WHERE `category`==1",null);

//				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					SensorDevice device = new SensorDevice();
					device.d_mac = c.getString(c.getColumnIndex("d_mac"));
					device.d_devtype = c.getString(c.getColumnIndex("d_devtype"));
					device.d_name = c.getString(c.getColumnIndex("d_name"));
					device.d_no = c.getInt(c.getColumnIndex("d_no"));
					device.r_id = c.getInt(c.getColumnIndex("r_id"));
					device.d_serialport = c.getInt(c.getColumnIndex("d_serialport"));
					data.add(device);
				}
							
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
//			Log.i("db", "size: " + data.size());
		}
	}

	class MyAdapter extends android.widget.BaseAdapter{

		Context mContext = null;
		LayoutInflater inflater = null;
		
		public MyAdapter(Context ctx) {
			mContext = ctx;
			inflater = LayoutInflater.from(mContext);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = inflater.inflate(R.layout.security_list_item, parent, false);
			}
			TextView security_item_txv_name = (TextView)convertView.findViewById(R.id.security_item_txv_name);
			TextView security_item_txv_desc = (TextView)convertView.findViewById(R.id.security_item_txv_desc);
			
			ToggleButton security_item_toggle_btn = (ToggleButton)convertView.findViewById(R.id.security_item_toggle_btn);
			
			SensorDevice device = data.get(position);
			security_item_txv_name.setText(device.d_name);
			security_item_txv_desc.setText(Integer.toString(device.r_id));
			
			security_item_toggle_btn.setTag(device.d_mac + device.d_no);
			security_item_toggle_btn.setOnClickListener(toggleListener);
			if(prefSensorUsed != null){
				security_item_toggle_btn.setChecked(prefSensorUsed.getBoolean(device.d_mac + device.d_no, false));
			}
			
			return convertView;
		}
		
	}
	
	/**
	 */
	View.OnClickListener toggleListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			/* set preference */
			prefSensorUsed
				.edit()
				.putBoolean((String)view.getTag(), ((ToggleButton)view).isChecked())
				.commit();
			/* send start command to service */
			if(isStart){
				startAndBindService();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.security_btn_1:
			startAndBindService();
			break;
		case R.id.security_btn_2:
			stopAndUnbindService();
			break;
		case R.id.security_btn_3:
			
			break;
		case R.id.security_btn_4:
			
			break;
		case R.id.security_line_setting:
			Intent intent = new Intent(this, SecuritySettingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void stopAndUnbindService() {
		if(isBind == true){
			unBindLocalService();
		}
		stopService(servIntent);
	}

	private void startAndBindService() {
		startService(servIntent);
		isStart = true;
		if(isBind == false){
			bindLocalService();
		}
	}

	/**
	 */
	private ServiceConnection serviceCon = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			System.out.println("Service disConnected.......");
			isBind = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			LocalService.LocalBinder binder = (LocalService.LocalBinder)service;
			mService =  binder.getService();
			isBind = true;
			System.out.println("Service Connected.......");
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent it = new Intent(SecurityActivity.this, MainMenuActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
			SecurityActivity.this.finish();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private boolean localServiceIsStart(String serviceClassName){
		String className = "com.emos.canbo.security.LocalService";
		ActivityManager mActivityManagerManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningList = mActivityManagerManager.getRunningServices(40);
		for (int i = 0; i < runningList.size(); i++) {
			if(className.equals(runningList.get(i).service.getClassName()))
				return true;
		}		
		return false;
	}
}
