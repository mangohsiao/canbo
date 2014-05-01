package com.test;

import com.emos.canbo.R;
import com.emos.canbo.network.NetCheckComponent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TestActivity extends Activity implements OnClickListener{

	/**
	 */
	NetCheckComponent check = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		check = new NetCheckComponent(this);
		
		findViewById(R.id.button_test).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_test:
			check.checkNet();
			break;

		default:
			break;
		}
	}
	
}
