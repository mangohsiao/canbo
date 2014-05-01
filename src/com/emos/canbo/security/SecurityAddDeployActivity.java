package com.emos.canbo.security;

import java.util.List;

import com.emos.canbo.Device;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.emos.canbo.common.BaseActivity;

public class SecurityAddDeployActivity extends BaseActivity implements OnClickListener{

	//////////* view */
	/**
	 */
	Button securityadd_btn_cancel = null;
	/**
	 */
	Button securityadd_btn_ok = null;
	/**
	 */
	ListView securityadd_listview = null;
	
	//////////* data */
	/**
	 */
	List<Device> undeployedList = null;
	/**
	 */
	List<Device> deployedList = null;
	/**
	 */
	SecurityAddAdapter adapter = null;
	/**
	 */
	boolean checkArray[] = null;

	/**
	 */
	SharedPreferences prefSensorUsed = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_add);
		prefSensorUsed = getSharedPreferences(SecurityCommon.prefSensorUsedName, MODE_PRIVATE);
		initialData();
		initialView();
		
	}

	private void initialData() {
		undeployedList = MyApp.instance.getUndeployedList();
		deployedList = MyApp.instance.getDeployedList();
		checkArray = new boolean[undeployedList.size()];
		for (int i = 0; i < checkArray.length; i++) {
			System.out.print(i + (checkArray[i]==false?"false":"true"));
		}
		System.out.println("checkArray size = " + checkArray.length + "  undeployedList size = " + undeployedList.size());
	}

	private void initialView() {
		securityadd_btn_cancel = (Button)findViewById(R.id.securityadd_btn_cancel);
		securityadd_btn_ok = (Button)findViewById(R.id.securityadd_btn_ok);
		securityadd_btn_cancel.setOnClickListener(SecurityAddDeployActivity.this);
		securityadd_btn_ok.setOnClickListener(SecurityAddDeployActivity.this);
		
		securityadd_listview = (ListView)findViewById(R.id.securityadd_listview);
		adapter = new SecurityAddAdapter(SecurityAddDeployActivity.this);
		securityadd_listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	class SecurityAddAdapter extends BaseAdapter{

		Context context = null;
		LayoutInflater inflater = null;
		
		public SecurityAddAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return undeployedList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return undeployedList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.securityadd_list_item, parent, false);
			}

			TextView securityadd_item_txv_name = (TextView)convertView.findViewById(R.id.securityadd_item_txv_name);
			TextView securityadd_item_txv_desc = (TextView)convertView.findViewById(R.id.securityadd_item_txv_desc);
			CheckBox securityadd_item_checkBox = (CheckBox)convertView.findViewById(R.id.securityadd_item_checkBox);
			securityadd_item_checkBox.setTag(position);
			securityadd_item_checkBox.setChecked(checkArray[position]);
			securityadd_item_checkBox.setOnCheckedChangeListener(checkBoxListener);
			
			Device device = undeployedList.get(position);
			securityadd_item_txv_name.setText(device.d_name);
			securityadd_item_txv_desc.setText(Integer.toString(device.r_id));
			
			return convertView;
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.securityadd_btn_cancel:
			SecurityAddDeployActivity.this.finish();
			break;
		case R.id.securityadd_btn_ok:
			//process data
			ListTools.moveListItem(undeployedList, checkArray, checkArray.length, deployedList, prefSensorUsed, true);
			SecurityAddDeployActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 */
	private OnCheckedChangeListener checkBoxListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			checkArray[((Integer)buttonView.getTag())] = isChecked;
//			Log.v("canbo check" ,"checked position =  " + ((Integer)buttonView.getTag()));
		}
	}; 
}
