package com.emos.canbo.security;

import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.tools.HttpUtil;
import com.emos.utils.RequestByHttpPost;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityTask extends TimerTask {

	/**
	 */
	Context context = null;
	/**
	 */
	List<SensorDevice> data = null;
	/**
	 */
	SharedPreferences prefSensorUsed = null;
	
	public SecurityTask(Context ctx, List<SensorDevice> dataIn) {
		this.context = ctx;
		this.data = dataIn;
		prefSensorUsed = context.getSharedPreferences(SecurityCommon.prefSensorUsedName, Context.MODE_PRIVATE);
	}
	
	@Override
	public void run() {
		
		JSONObject reqJson = new JSONObject();
		//TODO do
		try {
			reqJson.put("TYPE", 11);
			JSONArray array = new JSONArray();
			for (SensorDevice device : data) {
//				System.out.println(device.d_name + " - " + device.d_mac + " - " + device.d_devtype);
				StringBuffer str = new StringBuffer();
				str.append(device.d_mac.split("0x")[1]);
				str.append(device.d_devtype.split("0x")[1]);
				array.put(str.toString());
			}
			reqJson.put("MAC_DEV", array);
			String res = RequestByHttpPost.doPostJson(reqJson, CanboCommon.STATUS_WHOLE_URL);
			JSONObject resJson = new JSONObject(res);
			if(resJson.length()!=0){
				for (SensorDevice device : data) {
					StringBuffer str = new StringBuffer();
					str.append(device.d_mac.split("0x")[1]);
					str.append(device.d_devtype.split("0x")[1]);
					JSONObject devJson = resJson.getJSONObject(str.toString());
					if(devJson.length()==0){
						continue;
					}
					int status = devJson.getInt("0");
					if(status!=0){
//						((LocalService)context).baseNoti("安防警报", "设备：" + device.d_name, true, true, true);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
