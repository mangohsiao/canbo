package com.emos.canbo.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestResult;

import com.emos.canbo.DBhelper;
import com.emos.canbo.Device;
import com.emos.canbo.MainMenuActivity;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;
import com.emos.canbo.database.SmartDbHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SecurityActivity2 extends Activity implements View.OnClickListener{

	///////****** Serviece **********/
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

	///////****** ListView **********/
	/**
	 */
	ListView listview = null;
	/**
	 */
	List<Device> deployedList = null;
	/**
	 */
	List<Device> undeployedList = null;
	/**
	 */
	MyAdapter adapter = null;

	///////****** SharedPreferences **********/
	/**
	 */
	SharedPreferences prefSensorUsed = null;

	/**
	 */
	Button security_btn_deploy = null;
	/**
	 */
	Button security_btn_undeploy = null;
	/**
	 */
	Button security_btn_add_deploy = null;
	/**
	 */
	Button security_btn_remove = null;

	///////****** running values **********/
	/**
	 */
	boolean checkArray[] = null;
	/**
	 */
	int preDevicesCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//get global deployed list
		deployedList = MyApp.instance.getDeployedList();
		undeployedList = MyApp.instance.getUndeployedList();
		adapter = new MyAdapter(this);
		setContentView(R.layout.activity_security2);
		
		prefSensorUsed = getSharedPreferences(SecurityCommon.prefSensorUsedName, MODE_PRIVATE);
		
		servIntent = new Intent(this, LocalService.class);
		
		getData();
		initialView();
		setListener();
	}
	
	private void initialView() {
		security_btn_deploy = (Button)findViewById(R.id.security_btn_deploy);
		security_btn_undeploy = (Button)findViewById(R.id.security_btn_undeploy);
		security_btn_add_deploy = (Button)findViewById(R.id.security_btn_add_deploy);
		security_btn_remove = (Button)findViewById(R.id.security_btn_remove);

		listview = (ListView)findViewById(R.id.security_listview);
		listview.setAdapter(adapter);
	}

	private void setListener() {
		findViewById(R.id.security_line_setting).setOnClickListener(this);
		security_btn_deploy.setOnClickListener(this);
		security_btn_undeploy.setOnClickListener(this);
		security_btn_add_deploy.setOnClickListener(this);
		security_btn_remove.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//clear data and rescan
		getData();
		//notify
		adapter.notifyDataSetChanged();
		if(localServiceIsStart("com.emos.canbo.security.LocalService")){
			bindLocalService();
			//×·¼ÓÉè±¸
			if(preDevicesCount!=deployedList.size()){
				startAndBindService();
			}
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
		if (isBind == true) {
//			System.out.println("mango unbind.......");
			unbindService(serviceCon);
			isBind = false;
		}
	}

	private void getData() {
		if(deployedList.size()!=0||undeployedList.size()!=0){
			deployedList.clear();
			undeployedList.clear();
			checkArray = null;
		}
		
		/** get device in room **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(this);
		if(db==null){
			Log.i("db", "db is null");
		}else{
			try {
				Cursor c = db.rawQuery("SELECT * FROM Sensor_Device ;",null);				
				while(c.moveToNext()){
					Device device = new Device();
					device.d_mac = c.getString(c.getColumnIndex("d_mac"));
					device.d_devtype = c.getString(c.getColumnIndex("d_devtype"));
					device.d_name = c.getString(c.getColumnIndex("d_name"));
					device.d_no = c.getInt(c.getColumnIndex("d_no"));
					device.r_id = c.getInt(c.getColumnIndex("r_id"));
					device.d_serialport = c.getInt(c.getColumnIndex("d_serialport"));
					if(prefSensorUsed.getBoolean(device.d_mac + device.d_devtype + device.d_no, false)){
						//saved
						deployedList.add(device);
					}else {
						//unsaved
						undeployedList.add(device);
					}
				}
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.close();
			}//end of try
			checkArray = new boolean[deployedList.size()];
		}//end of else
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
			return deployedList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return deployedList.get(position);
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
				convertView = inflater.inflate(R.layout.security_list_item2, parent, false);
			}
			TextView security_item_txv_name = (TextView)convertView.findViewById(R.id.security_item_txv_name);
			TextView security_item_txv_desc = (TextView)convertView.findViewById(R.id.security_item_txv_desc);
			CheckBox security_lst_item_chbx = (CheckBox)convertView.findViewById(R.id.security_lst_item_chbx);
			
			Device device = deployedList.get(position);
			security_item_txv_name.setText(device.d_name);
			security_item_txv_desc.setText(Integer.toString(device.r_id));

			security_lst_item_chbx.setTag(position);
			security_lst_item_chbx.setChecked(checkArray[position]);
			security_lst_item_chbx.setOnCheckedChangeListener(checkListener);
			
			return convertView;
		}
		
	}
	
	/**
	 */
	private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			checkArray[(Integer)buttonView.getTag()] = isChecked;
//			for (int i = 0; i < checkArray.length; i++) {
//				Log.v("check", "pos=" + i + " " + checkArray[i]);
//			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.security_btn_deploy:
			startAndBindService();
			break;
		case R.id.security_btn_undeploy:
			if(stopAndUnbindService()){
				Toast.makeText(SecurityActivity2.this, R.string.security_unbind_toast, Toast.LENGTH_SHORT).show();
			}else {
				
			}
			break;
		case R.id.security_btn_add_deploy:
			Intent intent0 = new Intent(SecurityActivity2.this, SecurityAddDeployActivity.class);
			startActivity(intent0);
			break;
		case R.id.security_line_setting:
			Intent intent = new Intent(this, SecuritySettingActivity.class);
			startActivity(intent);
			break;
		case R.id.security_btn_remove:
			boolean noSelect = true;
			for (int i = 0; i < checkArray.length; i++) {
				if(checkArray[i]){
					noSelect = false;
					i = checkArray.length;
				}
			}
			if(noSelect){
				//no selection
				AlertDialog.Builder buidler0 = new Builder(SecurityActivity2.this);
				buidler0.setTitle(R.string.security_alert_title)
				.setMessage(R.string.security_dialog_choose_something)
				.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				break;
			}
			AlertDialog.Builder buidler = new Builder(SecurityActivity2.this);
			buidler.setTitle(R.string.security_alert_title)
				.setMessage(R.string.security_alert_content)
				.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeSelected();
					}
				})
				.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
			break;
		default:
			break;
		}
	}

	private void removeSelected() {
		ListTools.moveListItem(deployedList, checkArray, checkArray.length, undeployedList, prefSensorUsed, false);
		getData();
		adapter.notifyDataSetChanged();
		if(isBind){
			startAndBindService();
		}
	}

	private boolean stopAndUnbindService() {
		if(isBind == true){
			unBindLocalService();
		}
		return stopService(servIntent);
	}

	private void startAndBindService() {
		Log.v("service", "startAndBindService()######################## ");
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
			Intent it = new Intent(SecurityActivity2.this, MainMenuActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
			SecurityActivity2.this.finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
