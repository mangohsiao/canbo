package com.emos.utils;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class RequestByHttpPost {

//	Context ctx_post;
 public static String TIME_OUT = "������ʱ";
 
 public static String doPost(List<NameValuePair> params,String url) throws Exception{
	 String result = null;
      // �½�HttpPost����
	 URI uri = new URI(url);
      HttpPost httpPost = new HttpPost();
      httpPost.setURI(uri);
      // �����ַ���
      HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      // ���ò���ʵ��
      httpPost.setEntity(entity);
      // ��ȡHttpClient����
      HttpClient httpClient = new DefaultHttpClient();
      //���ӳ�ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
      //����ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
      try {
       // ��ȡHttpResponseʵ��
//          Log.v("mango", url);
          HttpResponse httpResp = httpClient.execute(httpPost);
//          Log.v("mango", "action2");
          // �ж��ǹ�����ɹ�
          if (httpResp.getStatusLine().getStatusCode() == 200) {
           // ��ȡ���ص�����
           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
           Log.v("mango", "HttpPost��ʽ����ɹ��������������£�");
           Log.v("mango", result);
          } else {
        	  Log.v("mango", "HttpPost��ʽ����ʧ��");
          }
      } catch (ConnectTimeoutException e){
    	  e.printStackTrace();
			Log.v("mango", "failed");
			result = TIME_OUT;
      }
     
      return result;
 }
 
 
 public static String doPostJson(JSONObject jsobj,String url) throws Exception{
	 String result = null;
      // �½�HttpPost����
	 URI uri = new URI(url);
      HttpPost httpPost = new HttpPost();
      httpPost.setURI(uri);
      
      //new entity
      StringEntity se = new StringEntity(jsobj.toString());
      
      // ���ò���ʵ��
      httpPost.setEntity(se);
      // ��ȡHttpClient����
      HttpClient httpClient = new DefaultHttpClient();
      //���ӳ�ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
      //����ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
      try {
       // ��ȡHttpResponseʵ��
          Log.v("mango", "url: " + url);
          Log.v("mango", "str: " + jsobj.toString());
          HttpResponse httpResp = httpClient.execute(httpPost);
          // �ж��ǹ�����ɹ�
          if (httpResp.getStatusLine().getStatusCode() == 200) {
           // ��ȡ���ص�����
           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
           Log.v("mango", "HttpPost��ʽ����ɹ��������������£�");
           Log.v("mango", result);
          } else {
        	  Log.v("mango", "HttpPost��ʽ����ʧ��");
          }
      } catch (ConnectTimeoutException e){
    	  e.printStackTrace();
			Log.v("mango", "http post failed");
			result = TIME_OUT;
      }
     
      return result;
 }
 
 public static String doPostStr(String str,String url) throws Exception{
	 String result = null;
      // �½�HttpPost����
	 URI uri = new URI(url);
      HttpPost httpPost = new HttpPost();
      httpPost.setURI(uri);
      
      //new entity
      StringEntity se = new StringEntity(str);
      
      // ���ò���ʵ��
      httpPost.setEntity(se);
      // ��ȡHttpClient����
      HttpClient httpClient = new DefaultHttpClient();
      //���ӳ�ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
      //����ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
      try {
       // ��ȡHttpResponseʵ��
          Log.v("mango", "url: " + url);
          Log.v("mango", "str: " + str);
          HttpResponse httpResp = httpClient.execute(httpPost);
          Log.v("mango", "action2");
          // �ж��ǹ�����ɹ�
          if (httpResp.getStatusLine().getStatusCode() == 200) {
           // ��ȡ���ص�����
           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
           Log.v("mango", "HttpPost��ʽ����ɹ��������������£�");
           Log.v("mango", result);
          } else {
        	  Log.v("mango", "HttpPost��ʽ����ʧ��");
          }
      } catch (ConnectTimeoutException e){
    	  e.printStackTrace();
			Log.v("mango", "failed");
			result = TIME_OUT;
      }
     
      return result;
 }
 
 public static String doPostJsonWithToast(Context inContext, JSONObject jsobj,String url) throws Exception{	 
	 String result = null;
     // �½�HttpPost����
	 URI uri = new URI(url);
     HttpPost httpPost = new HttpPost();
     httpPost.setURI(uri);
     
     //new entity
     StringEntity se = new StringEntity(jsobj.toString());
     
     // ���ò���ʵ��
     httpPost.setEntity(se);
     // ��ȡHttpClient����
     HttpClient httpClient = new DefaultHttpClient();
     //���ӳ�ʱ
     httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
     //����ʱ
     httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
     try {
      // ��ȡHttpResponseʵ��
         Log.v("mango", "url: " + url);
         Log.v("mango", "str: " + jsobj.toString());
         HttpResponse httpResp = httpClient.execute(httpPost);
         // �ж��ǹ�����ɹ�
         if (httpResp.getStatusLine().getStatusCode() == 200) {
			// ��ȡ���ص�����
			result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Toast.makeText(inContext, "���ͳɹ�", Toast.LENGTH_SHORT).show();
			Log.v("mango", "HttpPost��ʽ����ɹ��������������£�");
			Log.v("mango", result);
			}else{
				Log.v("mango", "HttpPost��ʽ����ʧ��");
				Toast.makeText(
       	  			inContext, 
       	  			"HttpPostErr: " + httpResp.getStatusLine().getStatusCode(), 
       	  			Toast.LENGTH_SHORT).show();
				}
         } catch (ConnectTimeoutException e){
        	 e.printStackTrace();
        	 Log.v("mango", "http post failed");
        	 result = TIME_OUT;
        	 Toast.makeText(inContext, "HTTP post ConnectTimeoutException.", Toast.LENGTH_SHORT).show();
         }catch (Exception e) {
        	 Toast.makeText(inContext, "HTTP post Exception.", Toast.LENGTH_SHORT).show();
		}
     return result;
 }
 
}

