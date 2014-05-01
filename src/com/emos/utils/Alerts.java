package com.emos.utils;


import com.emos.canbo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Alerts {
	public static String  prompt(String message, Context ctx, TextView txv){
		
		LayoutInflater li = LayoutInflater.from(ctx);
		View view = li.inflate(R.layout.prompt, null);
		
		TextView txv_prompt2 = (TextView)view.findViewById(R.id.txv_prompt2);
		SeekBar skbar2 = (SeekBar)view.findViewById(R.id.skbar_prompt);
		MyListener2 mlistener = new MyListener2(txv_prompt2);
		skbar2.setOnSeekBarChangeListener(mlistener);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("title");
		builder.setView(view);
		PromptListener pl = new PromptListener(view, txv);
		builder.setPositiveButton("OK", pl);
		builder.setNegativeButton("Cancel", pl);
		
		AlertDialog alertDialog = builder.create();
		alertDialog.show();	
		
		return null;
	}
}