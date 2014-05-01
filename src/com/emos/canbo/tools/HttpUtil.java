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
	      // �½�HttpPost����
		 URI uri = new URI(url);
	      HttpPost httpPost = new HttpPost();
	      httpPost.setURI(uri);
	      // �����ַ���
	      if(params != null){
	    	  HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		      httpPost.setEntity(entity);
	      }
	      // ���ò���ʵ��
	      // ��ȡHttpClient����
	      HttpClient httpClient = new DefaultHttpClient();
	      //���ӳ�ʱ
	      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
	      //����ʱ
	      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
	      try {
	       // ��ȡHttpResponseʵ��
	          HttpResponse httpResp = httpClient.execute(httpPost);
	          // �ж��ǹ�����ɹ�
	          if (httpResp.getStatusLine().getStatusCode() == 200) {
	           // ��ȡ���ص�����
	           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
	          } else {
	        	  System.out.println("HttpUtil.java: http ����ʧ��");
	          }
	      } catch (ConnectTimeoutException e){
	    	  e.printStackTrace();
	      }
	     
	      return result;
	 }
}
