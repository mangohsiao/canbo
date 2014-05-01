package com.emos.canbo.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.emos.canbo.MyApp;
import com.emos.canbo.user.UserCommon;
import com.emos.utils.RequestByHttpPost;

import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoginRunnable implements Runnable{

	Handler handler;
	String username;
	String password;
	
	public LoginRunnable(Handler handler, String username, String password) {
		this.handler = handler;
		this.username = username;
		this.password = password;
	}

	@Override
	public void run() {

		/* to do something */
		//init params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair p1 = new BasicNameValuePair("username", username);
		NameValuePair p2 = new BasicNameValuePair("password", password);
		params.add(p1);
		params.add(p2);
//		System.out.println(params.toString());
		
		
		//HTTP post get result
		String res = null;
		try {
			res = RequestByHttpPost.doPost(params, UserCommon.LOGIN_URL);
		} catch (Exception e1) {
			e1.printStackTrace();
			sendStatus(LoginStatus.LOGIN_ERR_NETWORK_REQUEST);
			return;
		}
		
		if(res.equals(RequestByHttpPost.TIME_OUT)){
			//res == null
			sendStatus(LoginStatus.LOGIN_ERR_HTTP_TIMEOUT);
			return;
		}
		
		//parse the result
		int status = LoginStatus.LOGIN_ERR_J_PARSING_ERR;
		String ip = null;
		String port = null;
		try {
			JSONObject resJson = new JSONObject(res);
			Log.v("suibian", "jsonObject.");
			status = resJson.getInt("status");
			if(status == 100){
				ip = resJson.getString("ip");
//				port = resJson.getString("port");
				sendStatusIpPort(status,ip,port);
				return;
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
			sendStatus(LoginStatus.LOGIN_ERR_J_PARSING_ERR);
			return;
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		sendStatus(status);
		return;
		
//		handler.sendEmptyMessage(status);
	}

	private void sendStatus(int status) {
		sendStatusIpPort(status, null, null);
	}
	
	private void sendStatusIpPort(int status, String ip, String port){
		Message msg = new Message();
		msg.what = LoginActivity.LOGIN_CHECK;
		Bundle bd = new Bundle();
		bd.putInt("status", status);
		if(ip != null){
			bd.putString("ip", ip);
		}
		if(port != null){
			bd.putString("port", port);
		}
		msg.setData(bd);
		handler.sendMessage(msg);
	}

}
