package com.emos.canbo;

import java.util.ArrayList;
import java.util.List;

import com.emos.canbo.log.FileLog;
import com.emos.canbo.service.CanboService;
import com.emos.utils.Settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class MyApp extends Application {
	
	public static MyApp instance = null;
	
	private static final String TAG = "MyApp";
	
	private String conIp = CanboCommon.CONTROL_CONNECT_IP;
	private String conPort = CanboCommon.CONTROL_CONNECT_PORT;
	private String conUrl = CanboCommon.CONTROL_CONNECT_URL;
	private String conTotalStr;
	private int loginModeGLB = CanboCommon.LOGIN_MODE_LAN;

	SharedPreferences global_settings = null;
	
//	private String serverIp = CanboCommon.Server_IP;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate");
		instance = this;
		
		global_settings = getSharedPreferences(CanboCommon.GlobalSettingsName, MODE_PRIVATE);
		
		if(global_settings.getBoolean("first_running", true)){
			/* first running */
			System.out.println("app first running");
			
			firstRunningInitial();
			
		}else{
			/* not first running */
			System.out.println("not first running...");
			notFirstRunningInitial();
		}
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onTerminate()");
//		FileLog.close();
		super.onTerminate();
	}

	private void notFirstRunningInitial() {
		global_settings.edit().putString(CanboCommon.CONTROL_CONNECT_PORT_NAME, CanboCommon.CONTROL_CONNECT_PORT).commit();
		getConInfo();
	}

	public void getConInfo() {
		conIp = global_settings.getString(CanboCommon.CONTROL_CONNECT_IP_NAME, CanboCommon.CONTROL_CONNECT_IP);
		conPort = global_settings.getString(CanboCommon.CONTROL_CONNECT_PORT_NAME, CanboCommon.CONTROL_CONNECT_PORT);
		conUrl = global_settings.getString(CanboCommon.CONTROL_CONNECT_URL_NAME, CanboCommon.CONTROL_CONNECT_URL);
//		conPort = CanboCommon.CONTROL_CONNECT_PORT;
//		conUrl = CanboCommon.CONTROL_CONNECT_URL;
	}

	private void firstRunningInitial() {
		Editor editor = global_settings.edit();
		
		/* initial security */
		editor.putInt(CanboCommon.SETTING_SECURITY_REFRESH_TIME, CanboCommon.SECURITY_REFRESH_TIME);
		
		/* initial security */
		editor.putString(CanboCommon.CONTROL_CONNECT_IP_NAME, CanboCommon.CONTROL_CONNECT_IP);
		editor.putString(CanboCommon.CONTROL_CONNECT_PORT_NAME, CanboCommon.CONTROL_CONNECT_PORT);	
		editor.putString(CanboCommon.CONTROL_CONNECT_URL_NAME, CanboCommon.CONTROL_CONNECT_URL);

		editor.putBoolean("first_running", false).commit();	//first initialize finish!
		editor.commit();
	}

	/**
	 * @return
	 */
	public String getConTotalStr() {
		if(loginModeGLB == CanboCommon.LOGIN_MODE_LAN){
			conTotalStr = "http://" + global_settings.getString(CanboCommon.CONTROL_CONNECT_IP_NAME, CanboCommon.CONTROL_CONNECT_IP)
					+ ":" 
					+ conPort 
					+ conUrl;
		}else if(loginModeGLB == CanboCommon.LOGIN_MODE_WAN){
			conTotalStr = "http://" + global_settings.getString(CanboCommon.SHPF_WAN_CONNECT_IP, CanboCommon.CONTROL_CONNECT_IP)
					+ ":" 
					+ conPort 
					+ conUrl;
		}else{
			
		}
		return conTotalStr;
	}
	/**
	 * @return
	 */
	public String getConIp() {
		return conIp;
	}
	/**
	 * @param conIp
	 */
	public void setConIp(String conIp) {
		this.conIp = conIp;
	}
	/**
	 * @return
	 */
	public String getConPort() {
		return conPort;
	}
	/**
	 * @param conPort
	 */
	public void setConPort(String conPort) {
		this.conPort = conPort;
	}
	/**
	 * @return
	 */
	public String getConUrl() {
		return conUrl;
	}
	/**
	 * @param conUrl
	 */
	public void setConUrl(String conUrl) {
		this.conUrl = conUrl;
	}

	////////***************** for security ********************/
	/**
	 */
	private List<Device> deployedList = new ArrayList<Device>();
	/**
	 */
	private List<Device> undeployedList = new ArrayList<Device>();

	public List<Device> getDeployedList() {
		return deployedList;
	}

	public void setDeployedList(List<Device> deployedList) {
		this.deployedList = deployedList;
	}

	public List<Device> getUndeployedList() {
		return undeployedList;
	}

	public void setUndeployedList(List<Device> undeployedList) {
		this.undeployedList = undeployedList;
	}	
	////////***************** for security ********************/

	public int getLoginModeGLB() {
		return loginModeGLB;
	}

	public void setLoginModeGLB(int loginModeGLB) {
		this.loginModeGLB = loginModeGLB;
	}

//	public String getServerIp() {
//		return serverIp;
//	}
	
	
}
