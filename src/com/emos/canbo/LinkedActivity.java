/**
 * 
 */
package com.emos.canbo;

import com.emos.utils.SysApplication;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Mango.Xiao
 *
 */
public class LinkedActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// add this Activity to List.
		SysApplication.getInstance().addActivity(this);
	}
}
