package com.emos.canbo.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.MainMenuActivity;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;
import com.emos.canbo.SettingActivity2;
import com.emos.canbo.common.*;
import com.emos.canbo.sync.DBHttpSync;
import com.emos.canbo.sync.MDBSyncClient;
import com.emos.canbo.sync.MDBSyncMessage;

public class LoginActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	
	public static final int LOGIN_CHECK = 701;

	protected static final int MENU_NETWORK_SETTING = Menu.FIRST;

	Button login_btn_login = null;
	Button login_btn_lan = null;	
	
	EditText login_edtx_password = null;
	EditText login_edtx_username = null;
	CheckBox login_chbx_save_user = null;
	CheckBox login_chbx_save_pswd = null;
	
	private LoadingDialog dialog = null;
	private AlertDialog mAlertDialog = null;
	
	private SharedPreferences sharedPreferences = null;
	
	String username;
	String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		sharedPreferences = getSharedPreferences(CanboCommon.GlobalSettingsName, Context.MODE_PRIVATE);
		
		findView();
		setListener();
	}

	private Boolean checkAndGetUser() {
		if("".equals(login_edtx_username.getText().toString())
				||"".equals(login_edtx_password.getText().toString()) ){
			Bundle bd = new Bundle();
			bd.putInt("status", LoginStatus.LOGIN_ERR_INPUT_NULL);
			Message msg = new Message();
			msg.what = LOGIN_CHECK;
			msg.setData(bd);
			handler.sendMessage(msg);
			return false;
		}
		//GET USER
		username = login_edtx_username.getText().toString();
		password = login_edtx_password.getText().toString();
		return true;
	}

	private void setListener() {
		login_btn_login.setOnClickListener(this);
		login_btn_lan.setOnClickListener(this);
		login_chbx_save_user.setOnCheckedChangeListener(this);
		login_chbx_save_pswd.setOnCheckedChangeListener(this);
	}

	private void findView() {
		login_btn_login = (Button)findViewById(R.id.login_btn_login);
		login_btn_lan = (Button)findViewById(R.id.login_btn_lan);		
		login_edtx_username = (EditText)findViewById(R.id.login_edtx_username);
		login_edtx_password = (EditText)findViewById(R.id.login_edtx_password);
		login_chbx_save_user = (CheckBox)findViewById(R.id.login_chbx_save_user);
		login_chbx_save_pswd = (CheckBox)findViewById(R.id.login_chbx_save_pswd);
		
		//setting initial values
		if(sharedPreferences.getBoolean(UserCommon.SHPF_LOGIN_SAVE_USER_CHECKED, false)){
			//read username
			login_edtx_username.setText(sharedPreferences.getString(UserCommon.SHPF_LOGIN_SAVE_USERNAME, ""));
			login_chbx_save_user.setChecked(true);
		}
		if(sharedPreferences.getBoolean(UserCommon.SHPF_LOGIN_SAVE_PSWD_CHECKED, false)){
			login_edtx_password.setText(sharedPreferences.getString(UserCommon.SHPF_LOGIN_SAVE_PASSWORD, ""));
			login_chbx_save_pswd.setChecked(true);
		}
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_NETWORK_SETTING, 0, "网络设置");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_NETWORK_SETTING:
			Intent it = new Intent(this, SettingActivity2.class);
			startActivity(it);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_login:
			if(false == checkAndGetUser()){
				break;
			}
			
			if(dialog==null){
				dialog = new LoadingDialog(this, getResources().getString(R.string.login_doing));
			}

			/* do something login */
			LoginRunnable work = new LoginRunnable(handler, username, password);
			new Thread(work).start();
			
			dialog.show();
			break;

		case R.id.login_btn_lan:
			//set global
			((MyApp)getApplicationContext()).setLoginModeGLB(CanboCommon.LOGIN_MODE_LAN);
//			String url = "http://" + "125.216.243.235" + ":" + "8080" + "/CanboServer" + "/dbcheck";

			/* TODO 检查WIFI连接，仅在WIFI连接下使用内网控制。 */
			
			syncDB();
			break;
		default:
			break;
		}
	}
	private void syncDB() {
		if(dialog==null){
			dialog = new LoadingDialog(this, "正在同步数据");
		}
		dialog.show();
		String ip = ((MyApp)getApplicationContext()).getConIp();
		String port = ((MyApp)getApplicationContext()).getConPort();
		String url = "http://" + ip + ":" + port + "/CanboServer" + CanboCommon.DB_SYNC_SERVER_URL;
		DBHttpSync.syncDB(handler, LoginActivity.this, "smart.db", url);
	}
	private Thread syncThread = null;
	private ProgressDialog mProgressDialog = null;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(dialog!=null && dialog.isShowing()){
				dialog.dismiss();
				dialog = null;
			}
			
			if(msg.what == MDBSyncMessage.WHAT_PROGRESS){
				int progress = msg.getData().getInt("progress");
				if(null != mProgressDialog){
					mProgressDialog.setProgress(progress);
					if(progress == 100){
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
				}
				return;
			}else if(msg.what == MDBSyncMessage.WHAT_START_DOWNLOAD){
				Log.v("mango", "start download");
				mProgressDialog = new ProgressDialog(LoginActivity.this);
				mProgressDialog.setTitle("正在同步数据");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mProgressDialog.cancel();
						//cancel MtcpClient.
						if(syncThread != null){
							try {
								syncThread.stop();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
				mProgressDialog.show();
				return;
			}else if(msg.what == MDBSyncMessage.WHAT_NETWORK_ERR){
				Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_LONG).show();
				return;
			}else if(msg.what == MDBSyncMessage.WHAT_NETWORK_ERR){
				Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_LONG).show();
				return;
/*			}else if(msg.what == DBHttpSync.WHAT_NO_NEED_SYNC){
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("数据同步");
				builder.setMessage("已经是最新数据库");
				builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
				builder.show();
				return;	*/
			}else if(msg.what == DBHttpSync.WHAT_SYNC_OK || msg.what == DBHttpSync.WHAT_NO_NEED_SYNC){
				
				Intent it = new Intent(LoginActivity.this, MainMenuActivity.class);
				startActivity(it);
				LoginActivity.this.finish();
				
			}else if(msg.what == LOGIN_CHECK){
				int status;
				Bundle bd = msg.getData();
				status = bd.getInt("status");
				
				String tips = "";
				switch (status) {
					/* login successfully */
					case 100:
						String ip = bd.getString("ip");
						//set ip && username into SHPF
						Editor editor = sharedPreferences.edit();
						editor.putString(UserCommon.SHPF_LOGIN_SAVE_USERNAME, username);
						editor.putString(UserCommon.SHPF_LOGIN_SAVE_PASSWORD, password);
						editor.putString(CanboCommon.SHPF_WAN_CONNECT_IP, ip);
						editor.commit();
						((MyApp)getApplicationContext()).setLoginModeGLB(CanboCommon.LOGIN_MODE_WAN);
						((MyApp)getApplicationContext()).setConIp(ip);
						
						//check db sync
						syncDB();
						
//						Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
//						startActivity(intent);
//						LoginActivity.this.finish();
						return;
						
					/* user not exist */
					case 101:					
						tips += getResources().getString(R.string.login_user_not_exist);
						break;
						
					/* password wrong */
					case 102:					
						tips += getResources().getString(R.string.login_pswd_wrong);
						break;
						
					/* not running */
					case 103:					
						tips += getResources().getString(R.string.login_host_not_running);
						break;
						
					/* to verify */
					case 104:
						tips += getResources().getString(R.string.login_to_verify);
						break;
						
					/* http time out */
					case 110:
						tips += getResources().getString(R.string.login_http_timeout);
						break;
	
					/* json parsing error */
					case 111:
						tips += getResources().getString(R.string.login_json_parsing_error);
						break;
	
					/* network request error */
					case 112:
						tips += getResources().getString(R.string.login_network_request_error);
						break;
	
					/* network request error */
					case LoginStatus.LOGIN_ERR_INPUT_NULL:
						tips += getResources().getString(R.string.login_input_null);
						break;
						
					/* other errors */
					default:
						break;
				}
				showMyDialog(tips);
			} // end of if(msg.what == LOGIN_CHECK)
		}
		
	};
	
	void showMyDialog(String tips){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		mAlertDialog = builder.setTitle(R.string.tips)
				.setMessage(tips)
				.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mAlertDialog.dismiss();
					}
				})
				.create();
		mAlertDialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.login_chbx_save_user:
			if(isChecked){
				sharedPreferences.edit().putBoolean(UserCommon.SHPF_LOGIN_SAVE_USER_CHECKED, true).commit();
			}else{
				sharedPreferences.edit().putBoolean(UserCommon.SHPF_LOGIN_SAVE_USER_CHECKED, false).commit();
			}
			break;

		case R.id.login_chbx_save_pswd:
			if(isChecked){
				sharedPreferences.edit().putBoolean(UserCommon.SHPF_LOGIN_SAVE_PSWD_CHECKED, true).commit();
			}else{
				sharedPreferences.edit().putBoolean(UserCommon.SHPF_LOGIN_SAVE_PSWD_CHECKED, false).commit();
			}
			break;

		default:
			break;
		}
	}
}
