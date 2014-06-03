package com.emos.canbo.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import org.json.JSONObject;

import android.util.Log;

public class HttpPostUtils {

	public static final int TIMEOUT = 8000; // time out milsec

	public static String doPostJson(JSONObject jsobj, String urlStr) throws SocketException, IOException {
		// 新建HttpPost对象
			System.out.println("url: " + urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection mConnection = (HttpURLConnection)url.openConnection();
			mConnection.setConnectTimeout(TIMEOUT);
			mConnection.setDoOutput(true);
			mConnection.setDoInput(true);
			mConnection.setRequestMethod("POST");
			mConnection.setUseCaches(false);
			
			OutputStream reqOS = mConnection.getOutputStream();
			reqOS.write(jsobj.toString().getBytes());
			reqOS.flush();
			reqOS.close();
			
			int code = mConnection.getResponseCode();
//			System.out.println("code = " + code);
			String sCurrentLine = "";
			String sTotalStr = "";
			if(code == 200){
				InputStream is = mConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				while((sCurrentLine = reader.readLine()) != null){
					if(sCurrentLine.length() > 0)
						sTotalStr += sCurrentLine;
				}
				is.close();
			}else{
				Log.v("mango", "return code = " + code);
				throw new IOException("HTTP post Failed.");
			}
			System.out.println("response = " + sTotalStr);
			
			return sTotalStr;
	}
}
