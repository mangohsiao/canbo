/**
 * 
 */
package com.emos.canbo.monitor;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author Administrator
 *
 */
public class AddCamActivity extends Activity implements OnClickListener{

	private final static String TAG = "AddCamActivity"; 

	private final static String sql = "INSERT INTO cam_table(cam_name,cam_ip,cam_port, cam_channel, cam_username, cam_pswd) " +
			"VALUES(?,?,?,?,?,?);";
	
	/**
	 */
	LinearLayout linearlayout_addcam_yes = null;
	/**
	 */
	LinearLayout linearlayout_addcam_cancel = null;	
	
	/**
	 */
	ViewHolder holder;
	/**
	 */
	CameraInfo camInfo;
	
	/**
	 */
	SQLiteDatabase dbWriter = null;
	
	SharedPreferences sharedPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcam);
		sharedPreferences = getSharedPreferences(CanboCommon.GlobalSettingsName, Context.MODE_PRIVATE);
		/* initial view */
		initialView();
		
		setListener();
	}

	private void initialView() {
		holder = new ViewHolder();		
		holder.edtx_addcam_cam_name = (EditText)findViewById(R.id.edtx_addcam_cam_name);
		holder.edtx_addcam_cam_ip = (EditText)findViewById(R.id.edtx_addcam_cam_ip);
		if(((MyApp)getApplicationContext()).getLoginModeGLB()==CanboCommon.LOGIN_MODE_WAN){
			holder.edtx_addcam_cam_ip.setText(sharedPreferences.getString(CanboCommon.SHPF_WAN_CONNECT_IP, CanboCommon.CONTROL_CONNECT_IP));
			holder.edtx_addcam_cam_ip.setEnabled(false);
			holder.edtx_addcam_cam_ip.setFocusable(false);
		}
		holder.edtx_addcam_cam_port = (EditText)findViewById(R.id.edtx_addcam_cam_port);
		holder.edtx_addcam_cam_username = (EditText)findViewById(R.id.edtx_addcam_cam_username);
		holder.edtx_addcam_cam_pswd = (EditText)findViewById(R.id.edtx_addcam_cam_pswd);
		holder.edtx_addcam_cam_channel = (EditText)findViewById(R.id.edtx_addcam_cam_channel);

		linearlayout_addcam_yes = (LinearLayout)findViewById(R.id.linearlayout_addcam_yes);
		linearlayout_addcam_cancel = (LinearLayout)findViewById(R.id.linearlayout_addcam_cancel);
		
		camInfo = new CameraInfo();
		camInfo.cam_channel = 1;
		camInfo.cam_name = "Î´ÃüÃû";
		camInfo.cam_ip = "192.168.1.50";
		camInfo.cam_port = 8000;
		camInfo.cam_username = "admin";
		camInfo.cam_pswd = "88888";
	}
	
	private void setListener() {
		linearlayout_addcam_yes.setOnClickListener(this);
		linearlayout_addcam_cancel.setOnClickListener(this);
	}

	class ViewHolder {
		public EditText edtx_addcam_cam_name;
		public EditText edtx_addcam_cam_ip;
		public EditText edtx_addcam_cam_port;
		public EditText edtx_addcam_cam_username;
		public EditText edtx_addcam_cam_pswd;
		public EditText edtx_addcam_cam_channel;
	}

//	public class CamInfo{
//		public String cam_name = "¼à¿ØÉè±¸";
//		public String cam_ip = "192.168.1.50";
//		public String cam_port = "8000";
//		public String cam_username = "admin";
//		public String cam_pswd = "12345";
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearlayout_addcam_yes:
			if(add_data()){
				AddCamActivity.this.finish();
			}else {
				
			}
			break;

		case R.id.linearlayout_addcam_cancel:
			AddCamActivity.this.finish();
			break;

		default:
			break;
		}
		
	}

	private boolean add_data() {
		// TODO Auto-generated method stub
		CamDb camDb = new CamDb(getApplicationContext());
		dbWriter = camDb.getWritableDatabase();
		if(!holder.edtx_addcam_cam_name.getText().toString().equals("")){
			camInfo.cam_name = holder.edtx_addcam_cam_name.getText().toString();
		}
		if(!holder.edtx_addcam_cam_ip.getText().toString().equals("")){
			camInfo.cam_ip = holder.edtx_addcam_cam_ip.getText().toString();
		}
		if(!holder.edtx_addcam_cam_port.getText().toString().equals("")){
			camInfo.cam_port = Integer.parseInt(holder.edtx_addcam_cam_port.getText().toString());
		}
		if(!holder.edtx_addcam_cam_username.getText().toString().equals("")){
			camInfo.cam_username = holder.edtx_addcam_cam_username.getText().toString();
		}
		if(!holder.edtx_addcam_cam_pswd.getText().toString().equals("")){
			camInfo.cam_pswd = holder.edtx_addcam_cam_pswd.getText().toString();
		}
		if(!holder.edtx_addcam_cam_channel.getText().toString().equals("")){
			camInfo.cam_channel = Integer.parseInt(holder.edtx_addcam_cam_channel.getText().toString());
		}
		
		
		mLog(sql);
		
		try {
			dbWriter.execSQL(sql, 
					new String[]{
						camInfo.cam_name,
						camInfo.cam_ip,
						Integer.toString(camInfo.cam_port),
						Integer.toString(camInfo.cam_channel),
						camInfo.cam_username,
						camInfo.cam_pswd,
					});
		} catch (Exception e) {
			mLog(e.getMessage());
			return false;
		}
		dbWriter.close();
		return true;
	}

	private void mLog(String str) {
		// TODO Auto-generated method stub
		Log.v(TAG, str);
	}
}
