package com.emos.canbo.security;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.Device;
import com.emos.canbo.log.FileLog;
import com.emos.canbo.tools.HttpUtil;
import com.emos.utils.RequestByHttpPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SecurityTask2 extends TimerTask {

	/**
	 */
	Context context = null;
	/**
	 */
	List<Device> data = null;
	/**
	 */
	String reqUrl = null;
	/**
	 */
	SharedPreferences prefSensorUsed = null;
	
	public SecurityTask2(Context ctx, List<Device> dataIn, String url) {
		this.context = ctx;
		this.data = dataIn;
		this.reqUrl = url;
		prefSensorUsed = context.getSharedPreferences(SecurityCommon.prefSensorUsedName, Context.MODE_PRIVATE);
	}
	
	@Override
	public void run() {
		JSONObject reqJson = new JSONObject();
		//TODO do
		try {
			reqJson.put("TYPE", 11);
			JSONArray array = new JSONArray();
			for (Device device : data) {
//				System.out.println(device.d_name + " - " + device.d_mac + " - " + device.d_devtype);
				StringBuffer str = new StringBuffer();
				str.append(device.d_mac.split("0x")[1]);
				str.append(device.d_devtype.split("0x")[1]);
				array.put(str.toString());
			}
			reqJson.put("MAC_DEV", array);
			String res = RequestByHttpPost.doPostJson(reqJson, reqUrl);
			boolean warningIndexArray[] = new boolean[data.size()];
			FileLog.log(res);
			JSONObject resJson = new JSONObject(res);
			System.out.println("data size = " + data.size());
			
//			//遍历json
//			if(resJson.length()!=0){
//				Iterator<String> keys = resJson.keys();
//				JSONObject devJo = null;
//				String key;
//				int status;
//				int index;
//				while(keys.hasNext()){
//					key = keys.next();
//					devJo = resJson.getJSONObject(key);
//					if(devJo.length()==0){
//						continue;
//					}
//					status = devJo.getInt("0");
//					if(status != 0){
//						
//					}
//				}
//			}
			
			if(resJson.length()!=0){
//				for (Device device : data) {
				Device device = null;
				boolean warningSignal = false;
				for (int i=0; i<data.size(); i++) {
					device = data.get(i);
					StringBuffer str = new StringBuffer();
					str.append(device.d_mac.split("0x")[1]);
					str.append(device.d_devtype.split("0x")[1]);
//					System.out.println("MAC_DEV -- " + str.toString());
					FileLog.log("MAC_DEV -- " + str.toString());
					String key = str.toString().toUpperCase();
					
					if(!resJson.has(key)){
						continue;
					}
					JSONObject devJson = resJson.getJSONObject(key);
//					FileLog.log("device: " + device.d_name + " - " + device.d_mac + " - " + device.d_devtype);
//					FileLog.log("devJson: " + devJson.toString());
					if(devJson.length()==0){
						continue;
					}
					int status = devJson.getInt("0");
					if(status!=0){
//						FileLog.log("base NOTI trigger!!! -- " + device.d_name + " - " + device.d_mac + " - " + device.d_devtype);
//						((LocalService)context).baseNoti("安防警报", "设备：" + device.d_name, true, true);
						warningIndexArray[i] = true;
						warningSignal = true;
					}
				}
				if(warningSignal){
					StringBuffer devices = new StringBuffer();
					for (int i = 0; i < warningIndexArray.length; i++) {
						if(!warningIndexArray[i]){
							continue;
						}
						devices.append(data.get(i).d_name);
						devices.append("\n");
					}
					((LocalService)context).baseNoti("安防警报", "设备：" + devices.toString(), true, true, true);
				}
			}else{
				FileLog.log("resJson.length()==0");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
