package com.emos.utils;

import org.json.JSONObject;

public class OpProcessor {
	public static String send(String ip, String port, String path, JSONObject command) throws Exception {
		
		String url = "http://" + ip + ":" + port + path;		
		return RequestByHttpPost.doPostJson(command, url);
	}
}
