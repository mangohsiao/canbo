package com.emos.utils;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MyListener2 implements OnSeekBarChangeListener {

	/**
	 */
	TextView txv = null;
	
	public MyListener2(TextView inTextView) {
		// TODO Auto-generated constructor stub
		txv = inTextView;
	}
	
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		txv.setText( Integer.toString(arg1) );
		Log.d("prompt", Integer.toString(arg1));
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

}
