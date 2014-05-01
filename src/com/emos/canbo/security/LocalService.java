package com.emos.canbo.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.DBhelper;
import com.emos.canbo.Device;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;
import com.emos.canbo.database.SmartDbHelper;

import com.emos.canbo.security.SecurityTask2;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service {
	
    private static final String TAG = "SecurityService"; 
	/**
	 */
	LocalBinder mBinder = new LocalService.LocalBinder();
	
	/**
	 */
	String reqUrl;
	
	/**
	 */
	SharedPreferences prefSensorUsed = null;
	/**
	 */
	SharedPreferences globalSettings = null;
	
	/**
	 */
	Timer timer = null;
	
	/**
	 */
	List<Device> devices = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/* update datalist */
		getData();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		prefSensorUsed = getSharedPreferences(SecurityCommon.prefSensorUsedName, Context.MODE_PRIVATE);
		globalSettings = getSharedPreferences(CanboCommon.GlobalSettingsName, MODE_PRIVATE);
		reqUrl = "http://" 
				+ globalSettings.getString(CanboCommon.CONTROL_CONNECT_IP_NAME, CanboCommon.CONTROL_CONNECT_IP)
				+ ":"
				+ globalSettings.getString(CanboCommon.CONTROL_CONNECT_PORT_NAME, CanboCommon.CONTROL_CONNECT_PORT)
				+ globalSettings.getString(CanboCommon.CONTROL_SERVER_PATH_NAME, CanboCommon.CONTROL_SERVER_PATH)
				+ globalSettings.getString(CanboCommon.STATUS_SERVLET_URL_NAME, CanboCommon.STATUS_SERVLET_URL);
		baseNoti("安防系统","已启动", false, false,false);
		
		/* get list data */
		getData();
//		initialData();
		
		if(devices!=null){
			timer = new Timer();
//			SecurityTask task = new SecurityTask(this, devices);
			SecurityTask2 task = new SecurityTask2(this, devices, reqUrl);
//			timer.schedule(task, 0, globalSettings.getInt(CanboCommon.SETTING_SECURITY_REFRESH_TIME, 4000));
			timer.schedule(task, 0, 4000);
		}else {
			Log.v(TAG, "usedDevices == null in LocalServise");
		}
	}

	private void initialData() {
		// TODO Auto-generated method stub
		devices = MyApp.instance.getDeployedList();
	}

	private void getData() {
		if(prefSensorUsed == null){
			return;
		}
		if(devices == null){
			devices = new ArrayList<Device>();
		}else {
			devices.clear();
		}
		/** get device in room **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(this);
		if(db==null){
			Log.i("db", "db is null");
		}else{
			try {
				Cursor c = db.rawQuery("SELECT * FROM Device WHERE `category`==1",null);
				while(c.moveToNext()){
					Device device = new Device();
					device.d_mac = c.getString(c.getColumnIndex("d_mac"));
					device.d_devtype = c.getString(c.getColumnIndex("d_devtype"));
					device.d_name = c.getString(c.getColumnIndex("d_name"));
					device.d_no = c.getInt(c.getColumnIndex("d_no"));
					device.r_id = c.getInt(c.getColumnIndex("r_id"));
					device.d_serialport = c.getInt(c.getColumnIndex("d_serialport"));
					if(true == prefSensorUsed.getBoolean(device.d_mac + device.d_devtype + device.d_no, false)){
						devices.add(device);
					}
				}
				Log.v(TAG, "devices - - - - --  " + devices.size());
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearNoti(R.drawable.ic_launcher);
		if(timer != null){
			timer.cancel();
		}
		System.out.println("service destroyed.......");
	}

	public void startNoti(String title, String msg, String topLineContent){

		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.ic_launcher, topLineContent, System.currentTimeMillis());
		noti.flags = Notification.FLAG_NO_CLEAR;
//		noti.defaults |= Notification.DEFAULT_SOUND;
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		String noti_ring = globalSettings.getString(CanboCommon.SECURITYNOTIRINGNAME, null);
		if(noti_ring != null){
			noti.sound = Uri.parse(noti_ring);
		}
		Intent intent = new Intent(this, SecurityActivity2.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.drawable.ic_launcher, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		noti.setLatestEventInfo(this, title, msg, contentIntent);
		nm.notify(R.drawable.ic_launcher, noti);
	}

	public void baseNoti(String title, String msg, boolean isVibrant, boolean isRing, boolean isLights){

		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.ic_launcher, msg, System.currentTimeMillis());
		noti.flags = Notification.FLAG_NO_CLEAR;
//		noti.defaults |= Notification.DEFAULT_SOUND;
		if(isLights){
			noti.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
		if(isVibrant){
			noti.defaults |= Notification.DEFAULT_VIBRATE;
		}
		if(isRing){
			String noti_ring = globalSettings.getString(CanboCommon.SECURITYNOTIRINGNAME, null);
			if(noti_ring != null){
				noti.sound = Uri.parse(noti_ring);
			}else {
				noti.defaults |= Notification.DEFAULT_SOUND;
			}
		}
		/* here "R.drawable.ic_launcher" as a unique integer value */
		Intent intent = new Intent(this, SecurityActivity2.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.drawable.ic_launcher, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		noti.setLatestEventInfo(this, title, msg, contentIntent);
		nm.notify(R.drawable.ic_launcher, noti);
	}

	private void clearNoti(int id) {
		// TODO Auto-generated method stub
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(id);
	}
	
	public class LocalBinder extends Binder{
		
		public LocalService getService(){
			return LocalService.this;
		}
	}
}
