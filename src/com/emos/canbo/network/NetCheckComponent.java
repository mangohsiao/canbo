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
	 * 检查网络状态,让用户打开网络
	 */
	public void checkNet() {
		ConnectivityManager conMng = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMng.getActiveNetworkInfo();
		if(netInfo==null){
			//没有网络,提示是否进行设置
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("没有可用的网络").setMessage("是否对网络进行设置?");
			
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				
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
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					((Activity)context).finish();
				}
			})
			.show();
			
		}else if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
			//使用wifi网络
			Toast.makeText(context, "wifi网络", Toast.LENGTH_SHORT).show();
		}else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
			//使用移动网络
			Toast.makeText(context, "mobile网络", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(context, "其他", Toast.LENGTH_SHORT).show();
		}
	}
	
}
