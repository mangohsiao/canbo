package com.emos.utils;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


class MyListener implements android.view.View.OnDragListener{

	/**
	 */
	TextView txv = null;
	
	public MyListener(TextView inTextView) {
		// TODO Auto-generated constructor stub
		txv = inTextView;
	}
	
	@Override
	public boolean onDrag(View arg0, DragEvent arg1) {
		// TODO Auto-generated method stub
		int i = ((SeekBar)arg0).getProgress();
		txv.setText( Integer.toString(i) );
		Log.d("prompt", Integer.toString(i));
		return true;
	}
	
}