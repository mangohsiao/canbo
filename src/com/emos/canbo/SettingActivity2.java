package com.emos.canbo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emos.canbo.common.BaseActivity;
import com.emos.canbo.update.UpdateComponent;

public class SettingActivity2 extends BaseActivity implements OnClickListener{

	EditText settings_edtx_ip = null;
	EditText settings_edtx_lan_ip = null;
	EditText settings_edtx_port = null;
	EditText settings_edtx_service_name = null;

	Button setttings_btn_save = null;
	Button setttings_btn_cancel = null;
	LinearLayout setting_line_update = null;
	
	SharedPreferences sharedPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings2);
		
		sharedPreferences = getSharedPreferences(CanboCommon.GlobalSettingsName, Context.MODE_PRIVATE);
		
		initialView();
	}

	private void initialView() {
		// TODO Auto-generated method stub
		settings_edtx_ip = (EditText)findViewById(R.id.settings_edtx_ip);
		settings_edtx_lan_ip = (EditText)findViewById(R.id.settings_edtx_lan_ip);
		settings_edtx_port = (EditText)findViewById(R.id.settings_edtx_port);
		settings_edtx_service_name = (EditText)findViewById(R.id.settings_edtx_service_name);

		if(((MyApp)getApplicationContext()).getLoginModeGLB()==CanboCommon.LOGIN_MODE_LAN){
			settings_edtx_ip.setText(sharedPreferences.getString(CanboCommon.CONTROL_CONNECT_IP_NAME, CanboCommon.CONTROL_CONNECT_IP));
			settings_edtx_ip.setEnabled(true);
			settings_edtx_ip.setFocusable(true);
		}else if(((MyApp)getApplicationContext()).getLoginModeGLB()==CanboCommon.LOGIN_MODE_WAN){
			settings_edtx_ip.setText(sharedPreferences.getString(CanboCommon.SHPF_WAN_CONNECT_IP, CanboCommon.CONTROL_CONNECT_IP));
			settings_edtx_ip.setEnabled(false);
			settings_edtx_ip.setFocusable(false);
		}
		settings_edtx_port.setText(sharedPreferences.getString(CanboCommon.CONTROL_CONNECT_PORT_NAME, CanboCommon.CONTROL_CONNECT_PORT));
		settings_edtx_service_name.setText(sharedPreferences.getString(CanboCommon.CONTROL_SERVER_PATH_NAME, CanboCommon.CONTROL_SERVER_PATH));

		setttings_btn_save = (Button)findViewById(R.id.setttings_btn_save);
		setttings_btn_cancel = (Button)findViewById(R.id.setttings_btn_cancel);
		setting_line_update = (LinearLayout)findViewById(R.id.setting_line_update);

		setttings_btn_save.setOnClickListener(this);
		setttings_btn_cancel.setOnClickListener(this);
		setting_line_update.setOnClickListener(this);		
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setttings_btn_save:
			Editor editor = sharedPreferences.edit();
			String ip = settings_edtx_ip.getText().toString();
			String port = settings_edtx_port.getText().toString();
			String service = settings_edtx_service_name.getText().toString();
			if(ip.equals("")||port.equals("")||service.equals("")){
				alertInput();
				break;
			}
			editor.putString(CanboCommon.CONTROL_CONNECT_IP_NAME, ip);
			editor.putString(CanboCommon.CONTROL_CONNECT_PORT_NAME, port);
			editor.putString(CanboCommon.CONTROL_SERVER_PATH_NAME, service);
			editor.commit();
			//refresh connection info
			MyApp.instance.getConInfo();
			Toast.makeText(SettingActivity2.this, "修改成功", Toast.LENGTH_SHORT).show();
			SettingActivity2.this.finish();
			break;
		case R.id.setttings_btn_cancel:
			SettingActivity2.this.finish();
			break;

		case R.id.setting_line_update:
			UpdateComponent updateComponent = new UpdateComponent(this);
			updateComponent.check_update_with_alert();
//			updateComponent.check_update_background();
			break;
			
		default:
			break;
		}		
	}

	private void alertInput() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder01 = new Builder(SettingActivity2.this);
		builder01.setTitle("注意")
			.setMessage("输入值非法")
			.setPositiveButton("", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
			
	}

}
