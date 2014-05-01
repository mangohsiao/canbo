package com.emos.canbo.tools;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	private static HttpClient httpClient = null;
	
	 public static String doPost(List<NameValuePair> params,String url) throws Exception{
		 String result = null;
	      // 新建HttpPost对象
		 URI uri = new URI(url);
	      HttpPost httpPost = new HttpPost();
	      httpPost.setURI(uri);
	      // 设置字符集
	      if(params != null){
	    	  HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		      httpPost.setEntity(entity);
	      }
	      // 设置参数实体
	      // 获取HttpClient对象
	      HttpClient httpClient = new DefaultHttpClient();
	      //连接超时
	      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
	      //请求超时
	      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
	      try {
	       // 获取HttpResponse实例
	          HttpResponse httpResp = httpClient.execute(httpPost);
	          // 判断是够请求成功
	          if (httpResp.getStatusLine().getStatusCode() == 200) {
	           // 获取返回的数据
	           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
	          } else {
	        	  System.out.println("HttpUtil.java: http 请求失败");
	          }
	      } catch (ConnectTimeoutException e){
	    	  e.printStackTrace();
	      }
	     
	      return result;
	 }
}
