/**
 * 
 */
package com.emos.canbo.monitor;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class CamEditActivity extends Activity implements OnClickListener{

	private final static String delete_sql = "DELETE FROM cam_table WHERE cam_id=?;";
	private final static String update_sql = "UPDATE cam_table SET cam_name=?,cam_ip=?,cam_port=?,cam_channel=?,cam_username=?,cam_pswd=? WHERE cam_id=?;";
	
	/**
	 */
	CameraInfo camInfo = null;
	/**
	 */
	ViewHolder holder = null;

	/**
	 */
	CamDb camDb = null;
	
	/**
	 */
	MyHandler mhandler = null;
	
	SharedPreferences sharedPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cam_edit);
		sharedPreferences = getSharedPreferences(CanboCommon.GlobalSettingsName, Context.MODE_PRIVATE);
		camInfo = new CameraInfo();
		camDb = new CamDb(this);	//database
		mhandler = new MyHandler();
		
		//get intent & bundle
		getInfo();
		
		//objects initial
		initial();
		
		
		set_listener();
	}

	private void set_listener() {
		holder.line_save.setOnClickListener(this);
		holder.line_delete.setOnClickListener(this);
		holder.btn_cancel.setOnClickListener(this);
	}

	private void initial() {
		holder = new ViewHolder();
		holder.btn_cancel = (Button)findViewById(R.id.cam_edit_btn_cancel);
		holder.line_delete = (LinearLayout)findViewById(R.id.cam_edit_line_delete);
		holder.line_save = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_save);
		
		holder.line_name = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_name);
		holder.line_ip = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_ip);
		holder.line_port = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_port);
		holder.line_channel = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_channel);
		holder.line_username = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_username);
		holder.line_pswd = (LinearLayout)findViewById(R.id.cam_edit_linearlayout_cam_pswd);

		holder.edtx_name = (EditText)findViewById(R.id.cam_edit_edtx_cam_name);
		holder.edtx_ip = (EditText)findViewById(R.id.cam_edit_edtx_cam_ip);
		if(((MyApp)getApplicationContext()).getLoginModeGLB()==CanboCommon.LOGIN_MODE_WAN){
			holder.edtx_ip.setText(sharedPreferences.getString(CanboCommon.SHPF_WAN_CONNECT_IP, CanboCommon.CONTROL_CONNECT_IP));
			holder.edtx_ip.setEnabled(false);
			holder.edtx_ip.setFocusable(false);
		}
		holder.edtx_port = (EditText)findViewById(R.id.cam_edit_edtx_cam_port);
		holder.edtx_channel = (EditText)findViewById(R.id.cam_edit_edtx_cam_channel);
		holder.edtx_username = (EditText)findViewById(R.id.cam_edit_edtx_cam_username);
		holder.txv_pswd = (TextView)findViewById(R.id.cam_edit_txv_cam_pswd);
		
		//filling text
		holder.edtx_name.setText(camInfo.cam_name);
		holder.edtx_ip.setText(camInfo.cam_ip);
		holder.edtx_port.setText(Integer.toString(camInfo.cam_port));
		holder.edtx_channel.setText(Integer.toString(camInfo.cam_channel));
		holder.edtx_username.setText(camInfo.cam_username);
		//密码直接修改
//		holder.txv_pswd.setText(camInfo.cam_pswd);
	}

	private void getInfo() {
		Intent it = getIntent();
		Bundle bundle = it.getExtras();
		camInfo.cam_id = bundle.getInt("cam_id");
		camInfo.cam_ip = bundle.getString("cam_ip");
		camInfo.cam_port = bundle.getInt("cam_port");
		camInfo.cam_name = bundle.getString("cam_name");
		camInfo.cam_username = bundle.getString("cam_username");
		camInfo.cam_pswd = bundle.getString("cam_pswd");
		Log.v("test", "pswd:::: " + camInfo.cam_pswd);
		camInfo.cam_channel = bundle.getInt("cam_channel");
	}
	
	class ViewHolder{
		
		public Button btn_cancel;
		public LinearLayout line_delete;		
		public LinearLayout line_save;

		public LinearLayout line_name;
		public LinearLayout line_ip;
		public LinearLayout line_port;
		public LinearLayout line_channel;
		public LinearLayout line_username;
		public LinearLayout line_pswd;
		
		public EditText edtx_name;
		public EditText edtx_ip;
		public EditText edtx_port;
		public EditText edtx_channel;
		public EditText edtx_username;
		public TextView txv_pswd;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cam_edit_line_delete:
			/* delete */
//			delete_cam();
//			this.finish();
			
			DialogMsgBack dialogMsgBack = new DialogMsgBack(this, mhandler, MyHandler.TYPE_DELETE);
			dialogMsgBack.setTitle("确认删除监控点 " + camInfo.cam_name + "?");
			dialogMsgBack.show();
			
			break;
			
		case R.id.cam_edit_linearlayout_save:
			/* save */
//			Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
			if(save_edit()){
				CamEditActivity.this.finish();
			}
			break;
			
		case R.id.cam_edit_btn_cancel:
			CamEditActivity.this.finish();
			break;

		default:
			break;
		}
		
	}

	private boolean save_edit() {
		// save data

		/* name */
		if(holder.edtx_name.getText().toString().equals("")){
			camInfo.cam_name = "未命名";
		}else {
			camInfo.cam_name = holder.edtx_name.getText().toString();
		}
		/* ip */
		if(holder.edtx_ip.getText().toString().equals("")){
			showToast("IP地址为空，请填写");
			return false;
		}else {
			camInfo.cam_ip = holder.edtx_ip.getText().toString();
		}
		/* port */
		if(holder.edtx_port.getText().toString().equals("")){
			showToast("端口（Port）为空，请填写");
			return false;
		}else {
			camInfo.cam_port = Integer.parseInt(holder.edtx_port.getText().toString());
		}
		/* channel */
		if(holder.edtx_channel.getText().toString().equals("")){
			showToast("通道（Channel）为空，请填写");
			return false;
		}else {
			camInfo.cam_channel = Integer.parseInt(holder.edtx_channel.getText().toString());
		}
		/* username */
		if(holder.edtx_username.getText().toString().equals("")){
			showToast("用户名（Username）为空，请填写");
			return false;
		}else {
			camInfo.cam_username = holder.edtx_username.getText().toString();
		}
		/* pswd */
		
		/* write to db */
		SQLiteDatabase writer = camDb.getWritableDatabase();
		try {
			writer.execSQL(update_sql, 
					new String[]{
					camInfo.cam_name,
					camInfo.cam_ip,
					Integer.toString(camInfo.cam_port),
					Integer.toString(camInfo.cam_channel),
					camInfo.cam_username,
					camInfo.cam_pswd,
					Integer.toString(camInfo.cam_id)
					}
			);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		writer.close();
		
		return true;
	}

	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private void delete_cam() {
		SQLiteDatabase write = camDb.getWritableDatabase();
		try {
			write.execSQL(delete_sql,new String[]{Integer.toString(camInfo.cam_id)});
		} catch (Exception e) {
			e.printStackTrace();
		}
		write.close();
	}
	
	class MyHandler extends Handler{

		public final static int TYPE_DELETE = 101;
		
		@Override
		public void handleMessage(Message msg) {
			Log.v("handler", "msg received.");
			Bundle bd = msg.getData();
			switch (bd.getInt("MSG_TYPE")) {
			case TYPE_DELETE:
//				Log.v("handler", "msg handling. TYPE_DELETE");
				delete_cam();
				CamEditActivity.this.finish();
				break;

			default:
				break;
			}
		}
		
	}
	
}
