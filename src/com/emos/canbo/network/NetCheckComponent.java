package com.emos.canbo.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * @author xiao
 *
 */
public class NetCheckComponent {
	private static final String TAG = "NetCheckComponent";
	
	/**
	 */
	Context context = null;
	
	public NetCheckComponent(Context ctx) {
		// TODO Auto-generated constructor stub
		context = ctx;
	}
	
	/**
	 * �������״̬,���û�������
	 */
	public void checkNet() {
		ConnectivityManager conMng = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMng.getActiveNetworkInfo();
		if(netInfo==null){
			//û������,��ʾ�Ƿ��������
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("û�п��õ�����").setMessage("�Ƿ�������������?");
			
			builder.setPositiveButton("��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = null;  
                    
                    try {
                        intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                    } catch (Exception e) {  
                        Log.w(TAG, "open network settings failed, please check...");  
                        e.printStackTrace();  
                    }  
				}
			})
			.setNegativeButton("��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					((Activity)context).finish();
				}
			})
			.show();
			
		}else if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
			//ʹ��wifi����
			Toast.makeText(context, "wifi����", Toast.LENGTH_SHORT).show();
		}else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
			//ʹ���ƶ�����
			Toast.makeText(context, "mobile����", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(context, "����", Toast.LENGTH_SHORT).show();
		}
	}
	
}
