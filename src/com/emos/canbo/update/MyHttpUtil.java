package com.emos.canbo.update;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class MyHttpUtil {
	public static JSONObject getJson(String url, List<BasicNameValuePair> params) throws Exception {
		
		/* build param */
		String param = URLEncodedUtils.format(params, "UTF-8");
		
		HttpGet getMethodUri = new HttpGet(url + "?" + param);
		
		HttpClient client = new DefaultHttpClient();
		
		//set timeout?????
//		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		
		try {
			HttpResponse response = client.execute(getMethodUri);
			
			JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return json;
		}catch (JSONException je){
			je.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return null;
	}
}
