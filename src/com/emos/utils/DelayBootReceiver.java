package com.emos.utils;
/** auto boot broadcast receiver **/
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DelayBootReceiver extends BroadcastReceiver {
	static final String action_boot="android.intent.action.BOOT_COMPLETED"; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		  Intent ootStartIntent=new Intent(context,com.emos.canbo.Splash.class); 
          ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
          context.startActivity(ootStartIntent); 
	}

}
