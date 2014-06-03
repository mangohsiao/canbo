package com.emos.canbo.monitor;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emos.canbo.*;
import com.emos.canbo.monitor.MonitorInfo;
import com.hikvision.netsdk.HCNetSDK;

public class MonitorActivity extends ExActivity implements View.OnTouchListener,View.OnClickListener,View.OnLongClickListener{

	private final static String TAG = "MonitorActivity";

	private boolean isFullScreen = false;
	private boolean isShowTools = false;
	
	CameraInfo camInfo = null;
	
	LinearLayout linearlayout_bottom_direction = null;
	LinearLayout linearlayout_bottom_zoom = null;
	LinearLayout linearlayout_bottom_focus = null;
	LinearLayout linearlayout_bottom_vision_node = null;
	
	RadioGroup radioGroup_action_change = null;
	
	TextView txv_head_med = null;
	
	Button btn_cam_up = null;
	Button btn_cam_down = null;
	Button btn_cam_left = null;
	Button btn_cam_right = null;
	Button btn_cam_zoom_in = null;
	Button btn_cam_zoom_out = null;
	Button btn_cam_focus_near = null;
	Button btn_cam_focus_far = null;

	Button btn_cam_vision_node_choose = null;
	Button btn_cam_vision_node_manage = null;	

	Button monitor_btn_full_screen = null;
	LinearLayout monitor_line_bottom = null;
	RelativeLayout monitor_line_head_cell = null;
	LinearLayout monitor_line_holder = null;
	RelativeLayout monitor_rltv_middle = null;
	RelativeLayout monitor_rltv_exit_fullscreen = null;
	Button monitor_btn_exit_fullscreen = null;
	
	MonitorSurfaceView monitorSurfaceView = null;
	HCNetSDK hcNetSDK = null;
	MonitorInfo monitorInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		
		initial_view();
		
		//初始化
		hcNetSDK = monitorSurfaceView.getHkNetSdk();	//get net SDK
		
		camInfo = new CameraInfo();
		monitorInfo = new MonitorInfo();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle.isEmpty()){
			mLog("bundle empty.");
			this.finish();
			
			AlertDialog.Builder builder = new Builder(getApplicationContext());
			builder.setTitle("bundle empty error.");
			builder.setMessage("启动活动错误，bundle为空");
			builder.show();
//			monitorInfo.serverip = "125.216.243.231";
//	//		monitorInfo.serverip = "125.216.243.105";
//			monitorInfo.serverport = 8000;
//			monitorInfo.username = "admin";
//			monitorInfo.userpwd = "12345";
//			monitorInfo.describe = "emosCam";
//			monitorInfo.channel = 1;
		}else{
			
			camInfo.cam_id = bundle.getInt("cam_id");
			camInfo.cam_ip = bundle.getString("cam_ip");
			camInfo.cam_port = bundle.getInt("cam_port");
			camInfo.cam_name = bundle.getString("cam_name");
			camInfo.cam_username = bundle.getString("cam_username");
			camInfo.cam_pswd = bundle.getString("cam_pswd");
			camInfo.cam_channel = bundle.getInt("cam_channel");
			
			mLog("cam_id = " + camInfo.cam_id);
			monitorInfo.serverip = bundle.getString("cam_ip");
			monitorInfo.serverport = bundle.getInt("cam_port");
			monitorInfo.username = bundle.getString("cam_username");
			monitorInfo.userpwd = bundle.getString("cam_pswd");
			monitorInfo.channel = bundle.getInt("cam_channel");
			
			/* TODO channel info */
			monitorInfo.cam_name = bundle.getString("cam_name");
			monitorInfo.describe = "emosCam";
			
			txv_head_med.setText(camInfo.cam_name);
			mLog("set text : cam_name = " + camInfo.cam_name);
		}
		
		monitorSurfaceView.setMonitorInfo(monitorInfo);
		monitorSurfaceView.setOnLongClickListener(this);
		
//		if(monitorSurfaceView.startPlay() == false){
//			new AlertDialog.Builder(getApplicationContext()).setTitle("登陆失败！").show();
//		}else {
//			
//		}

	}

	private void initial_view() {

		/* find view  */
		txv_head_med = (TextView)findViewById(R.id.txv_head_med);
		
		btn_cam_up = (Button)findViewById(R.id.btn_cam_up);
		btn_cam_down = (Button)findViewById(R.id.btn_cam_down);
		btn_cam_left = (Button)findViewById(R.id.btn_cam_left);
		btn_cam_right = (Button)findViewById(R.id.btn_cam_right);
		btn_cam_zoom_in = (Button)findViewById(R.id.btn_cam_zoom_in);
		btn_cam_zoom_out = (Button)findViewById(R.id.btn_cam_zoom_out);
		btn_cam_focus_near = (Button)findViewById(R.id.btn_cam_focus_near);
		btn_cam_focus_far = (Button)findViewById(R.id.btn_cam_focus_far);

		btn_cam_vision_node_choose = (Button)findViewById(R.id.btn_cam_vision_node_choose);
		btn_cam_vision_node_manage = (Button)findViewById(R.id.btn_cam_vision_node_manage);
		
		monitor_btn_full_screen = (Button)findViewById(R.id.monitor_btn_full_screen);
		monitor_line_bottom = (LinearLayout)findViewById(R.id.monitor_line_bottom);
		monitor_line_head_cell = (RelativeLayout)findViewById(R.id.monitor_line_head_cell);
		monitor_line_holder = (LinearLayout)findViewById(R.id.monitor_line_holder);
		monitor_rltv_middle = (RelativeLayout)findViewById(R.id.monitor_rltv_middle);
		
		radioGroup_action_change = (RadioGroup)findViewById(R.id.radioGroup_action_change);
		
		linearlayout_bottom_direction = (LinearLayout)findViewById(R.id.linearlayout_bottom_direction);
		linearlayout_bottom_zoom = (LinearLayout)findViewById(R.id.linearlayout_bottom_zoom);
		linearlayout_bottom_focus = (LinearLayout)findViewById(R.id.linearlayout_bottom_focus);
		linearlayout_bottom_vision_node = (LinearLayout)findViewById(R.id.linearlayout_bottom_vision_node);
		monitor_rltv_exit_fullscreen = (RelativeLayout)findViewById(R.id.monitor_rltv_exit_fullscreen);
		monitor_btn_exit_fullscreen = (Button)findViewById(R.id.monitor_btn_exit_fullscreen);

		monitorSurfaceView = (MonitorSurfaceView)findViewById(R.id.sfv_monitor);
		monitorSurfaceView.setClickable(false);
		
		/* set listener */
		btn_cam_up.setOnTouchListener(this);
		btn_cam_down.setOnTouchListener(this);
		btn_cam_left.setOnTouchListener(this);
		btn_cam_right.setOnTouchListener(this);
		btn_cam_zoom_in.setOnTouchListener(this);
		btn_cam_zoom_out.setOnTouchListener(this);
		btn_cam_focus_near.setOnTouchListener(this);
		btn_cam_focus_far.setOnTouchListener(this);
		
		btn_cam_vision_node_choose.setOnClickListener(this);
		btn_cam_vision_node_manage.setOnClickListener(this);
		
		monitor_btn_full_screen.setOnClickListener(this);
		monitor_line_holder.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		monitor_line_holder.setOnClickListener(this);
		monitor_rltv_middle.setOnClickListener(this);
		monitorSurfaceView.setOnClickListener(this);
		monitor_btn_exit_fullscreen.setOnClickListener(this);
		
		radioGroup_action_change.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				/* clean the bottom tools */
				linearlayout_bottom_direction.setVisibility(View.GONE);
				linearlayout_bottom_zoom.setVisibility(View.GONE);
				linearlayout_bottom_focus.setVisibility(View.GONE);
				linearlayout_bottom_vision_node.setVisibility(View.GONE);
				
				switch (group.getCheckedRadioButtonId()) {
				case R.id.radio_direction:
					linearlayout_bottom_direction.setVisibility(View.VISIBLE);
					break;
				case R.id.radio_zoom:
					linearlayout_bottom_zoom.setVisibility(View.VISIBLE);
					break;
				case R.id.radio_focus:
					linearlayout_bottom_focus.setVisibility(View.VISIBLE);
					break;
				case R.id.radio_vision_node:
					linearlayout_bottom_vision_node.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		monitorSurfaceView.stopPlay();
	}
	
	public void function_back(View view) {
		this.finish();
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId()){
//		case R.id.btn_cam_up:
//			break;
//		case R.id.btn_cam_down:
//			break;
//		case R.id.btn_cam_left:
//			/* cam left operation */
//			try {
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_LEFT,0);
//			} catch (Exception e) {
//				// TODO: handle exception
//				mLog(e.getMessage());
//			}
//			break;
//		case R.id.btn_cam_right:
//			break;
//		default:
//			break;
//		}
//	}
	
	private void mLog(String msg) {
		Log.v(TAG, msg);
		return;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = 1;
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			action = 0;
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			action = 1;
		}
		
		switch (v.getId()) {
		case R.id.btn_cam_up:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_UP,action);
			break;
		case R.id.btn_cam_down:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_DOWN,action);
			break;
		case R.id.btn_cam_left:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_LEFT,action);
			break;
		case R.id.btn_cam_right:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_RIGHT,action);
			break;
		case R.id.btn_cam_zoom_in:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_ZOOM_IN,action);
			break;
		case R.id.btn_cam_zoom_out:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_ZOOM_OUT,action);
			break;
		case R.id.btn_cam_focus_near:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_FOCUS_NEAR,action);
			break;
		case R.id.btn_cam_focus_far:
			monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_FOCUS_FAR,action);
			break;
		default:
			break;
		}
		
//		switch(v.getId()){
//		case R.id.btn_cam_up:
//			/* cam UP operation */
//			if(event.getAction() == MotionEvent.ACTION_DOWN){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_UP,0);
//			}
//			if(event.getAction() == MotionEvent.ACTION_UP){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_UP,1);
//			}
//			break;
//		case R.id.btn_cam_down:
//			/* cam DOWN operation */
//			if(event.getAction() == MotionEvent.ACTION_DOWN){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_DOWN,0);
//			}
//			if(event.getAction() == MotionEvent.ACTION_UP){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_DOWN,1);
//			}
//			break;
//		case R.id.btn_cam_left:
//			/* cam left operation */
//			if(event.getAction() == MotionEvent.ACTION_DOWN){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_LEFT,0);
//			}
//			if(event.getAction() == MotionEvent.ACTION_UP){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_LEFT,1);
//			}
//			break;
//		case R.id.btn_cam_right:
//			/* cam RIGHT operation */
//			if(event.getAction() == MotionEvent.ACTION_DOWN){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_RIGHT,0);
//			}
//			if(event.getAction() == MotionEvent.ACTION_UP){
//				monitorSurfaceView.cam_contrl(MonitorSurfaceView.CTRL_CAM_RIGHT,1);
//			}
//			break;
//		default:
//			break;
//		}
		
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cam_vision_node_choose:
//			if(monitorSurfaceView.cam_preset(MonitorSurfaceView.CAM_SET_PRESET, 1)){
//				mLog("set preset 1 OK");
//			}else {
//				mLog("set preset 1 failed");
//			}
			DialogPresetGoto mPresetGoto = new DialogPresetGoto(MonitorActivity.this, camInfo.cam_id, presetListener);
			mPresetGoto.setTitle("前往预置点");
			mPresetGoto.show();
			break;
			
		case R.id.btn_cam_vision_node_manage:
//			if(monitorSurfaceView.cam_preset(MonitorSurfaceView.CAM_GOTO_PRESET, 1)){
//				mLog("go to preset 1 OK");
//			}else {
//				mLog("goto preset 1 failed");
//			}
			DialogPresetManage presetManageDialog = new DialogPresetManage(this,camInfo.cam_id,presetListener);
			presetManageDialog.setTitle("管理预置点");
			presetManageDialog.show();
			break;

		case R.id.monitor_btn_full_screen:
			full_screen();
			break;
		case R.id.monitor_rltv_middle:
			Log.v(TAG, "R.id.monitor_rltv_middle");
		case R.id.sfv_monitor:
			Log.v(TAG, "R.id.sfv_monitor");
		case R.id.monitor_line_holder:
			Log.v(TAG, "R.id.monitor_line_holder");
			if(isFullScreen){
				if(isShowTools){
					hideTools();
					isShowTools = false;
				}else{
					showTools();
					isShowTools = true;
				}
			}else {
				Log.v(TAG, "R.id.monitor_line_holder - no fullscreen");
			}
			break;
		case R.id.monitor_btn_exit_fullscreen:
			if(isFullScreen){
				cal_surface_size();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		cal_surface_size();
		
        
		if(monitorSurfaceView.startPlay() == false){
			
//			AlertDialog.Builder alertDialog =  new AlertDialog.Builder(MonitorActivity.this);
//			alertDialog.setTitle("登陆失败！");
//			alertDialog.setNegativeButton("确定", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					MonitorActivity.this.finish();
//				}
//			});
//			alertDialog.setMessage("IP: " + camInfo.cam_ip +
//					"\n端口: " + camInfo.cam_port +
//					"\n通道: " + camInfo.cam_channel +
//					"\n用户名: " + camInfo.cam_username);
//			alertDialog.show();
//			MonitorActivity.this.finish();
		}else {
			
		}
	}

	private void full_screen(){

        android.view.Display display = this.getWindowManager().getDefaultDisplay();
        int max_width  = display.getWidth();
        int max_height = display.getHeight();

        int surface_h = (max_width*3/4)<max_height?(max_width*3/4):max_height;
        int surface_w = surface_h*4/3;
        
        LinearLayout.LayoutParams layoutParams = 
				new LinearLayout.LayoutParams(surface_w,surface_h);
		layoutParams.setMargins(0, 0, 0, 0);
		monitorSurfaceView.setLayoutParams(layoutParams);
		
		monitor_line_holder.setBackgroundResource(R.color.pure_black);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(monitor_rltv_middle.getLayoutParams());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ABOVE, RelativeLayout.NO_ID);
		monitor_rltv_middle.setLayoutParams(params);
//		monitor_line_head_cell.bringToFront();
		monitor_rltv_exit_fullscreen.bringToFront();
		monitor_line_bottom.bringToFront();
		
//		monitor_line_bottom.setVisibility(View.GONE);
		monitor_line_head_cell.setVisibility(View.GONE);
//		monitor_rltv_middle.bringToFront();
		hideTools();
		isFullScreen = true;
		isShowTools = false;
	}
	
	private void cal_surface_size() {
		//adapt the screen size.
        android.view.Display display = this.getWindowManager().getDefaultDisplay();
        int  width = display.getWidth();
        int height = display.getHeight();
        
        int max_height = height-220;
        int max_width = width;
        
        int surface_h = (max_width*3/4)<max_height?(max_width*3/4):max_height;
        int surface_w = surface_h*4/3;
        
		int margin = 4;
        LinearLayout.LayoutParams layoutParams = 
				new LinearLayout.LayoutParams(surface_w-(margin*2), 
						surface_h-(margin*2));
		layoutParams.setMargins(margin, margin, margin, margin);
		monitorSurfaceView.setLayoutParams(layoutParams);

		if(isFullScreen){
			exitFullScreen();
		}
	}

	private void exitFullScreen() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(monitor_rltv_middle.getLayoutParams());
		params.addRule(RelativeLayout.ABOVE, R.id.monitor_line_bottom);
		params.addRule(RelativeLayout.BELOW, R.id.monitor_line_head_cell);
		monitor_rltv_middle.setLayoutParams(params);		
		monitor_line_holder.setBackgroundResource(R.color.white);
		monitor_line_bottom.setVisibility(View.VISIBLE);
		monitor_line_head_cell.setVisibility(View.VISIBLE);
		monitor_rltv_exit_fullscreen.setVisibility(View.GONE);
		isFullScreen = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		monitorSurfaceView.stopPlay();
	}
	
	/**
	 */
	private IPresetListener presetListener = new IPresetListener() {
		
		@Override
		public boolean preset_goto(int presetCmd, int index) {
			//
			mLog("presetCmd=" + presetCmd + " index=" + index);
			return monitorSurfaceView.cam_preset(presetCmd, index);
		}
	};

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sfv_monitor:
			//是否全屏，按返回键退出
			/* full screen dialog */
			break;

		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && isFullScreen){
			cal_surface_size();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showTools() {
		Log.v(TAG, "R.id.monitor_line_holder - show Tools");
		monitor_line_bottom.setVisibility(View.VISIBLE);
		monitor_rltv_exit_fullscreen.setVisibility(View.VISIBLE);
//		monitor_line_head_cell.setVisibility(View.VISIBLE);
//		monitor_line_bottom.bringToFront();
//		monitor_line_head_cell.bringToFront();
		return;
	}

	private void hideTools() {
		Log.v(TAG, "R.id.monitor_line_holder - hide Tools");
		monitor_line_bottom.setVisibility(View.GONE);
		monitor_rltv_exit_fullscreen.setVisibility(View.GONE);
//		monitor_line_head_cell.setVisibility(View.GONE);
//		monitor_rltv_middle.bringToFront();
		return;
	}
}
