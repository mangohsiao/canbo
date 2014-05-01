package com.emos.canbo.monitor;

import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Map;

import org.MediaPlayer.PlayM4.Display;
import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.PlayerCallBack;

import com.emos.canbo.OptListActivity;
import com.emos.canbo.user.LoadingDialog;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_ALARMER;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.PTZPresetCmd;
import com.hikvision.netsdk.RealPlayCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MonitorSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	private static String TAG = "MonitorSurfaceView";

	public static final int SUCCESS_LOGIN = 100;
	public static final int ERR_LOGIN_FAILED = 101;

	public static Map<Integer, Integer> CTRL_MAP = new HashMap<Integer, Integer>();
	static{
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_UP, PTZCommand.TILT_UP);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_DOWN, PTZCommand.TILT_DOWN);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_LEFT, PTZCommand.PAN_LEFT);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_RIGHT, PTZCommand.PAN_RIGHT);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_ZOOM_IN, PTZCommand.ZOOM_IN);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_ZOOM_OUT, PTZCommand.ZOOM_OUT);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_FOCUS_NEAR, PTZCommand.FOCUS_NEAR);
		CTRL_MAP.put(MonitorSurfaceView.CTRL_CAM_FOCUS_FAR, PTZCommand.FOCUS_FAR);
	}
	
	public final static int CTRL_CAM_UP = 201;
	public final static int CTRL_CAM_DOWN = 202;
	public final static int CTRL_CAM_LEFT = 203;
	public final static int CTRL_CAM_RIGHT = 204;
	public final static int CTRL_CAM_ZOOM_IN = 205;
	public final static int CTRL_CAM_ZOOM_OUT = 206;
	public final static int CTRL_CAM_FOCUS_NEAR = 207;
	public final static int CTRL_CAM_FOCUS_FAR = 208;

	public final static int CAM_SET_PRESET = PTZPresetCmd.SET_PRESET;
	public final static int CAM_CLR_PRESET = PTZPresetCmd.CLE_PRESET;
	public final static int CAM_GOTO_PRESET = PTZPresetCmd.GOTO_PRESET;
	
	private static int ctrlStatus = 0;
	
	private Context mContext = null;
	private SurfaceHolder holder = null;
    private HCNetSDK hkNetSdk;    //网络库sdk
    private Player myPlayer = null;  //播放库sdk
    private int playPort = -1;   //播放端口   
    public boolean playFlag = false;   //播放标志   
    private int userId = -1;   //登录帐号id
    private int mPlayhandle = -1;
    private MonitorInfo monitorInfo = null;
	
    Surface surface = null;
    
    LoadingDialog mLoadingDialog = null;
    
	public MonitorSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
	}

	public MonitorSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
	}

	public MonitorSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
	}

	private void initSurfaceView(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		holder = this.getHolder();
		holder.addCallback(this);
		myPlayer = Player.getInstance();
		Log.v(this.TAG, "Player SDK = " + Integer.toString(myPlayer.getSdkVersion(),16));
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		/** set videoWindow **/
		surface = holder.getSurface();
        if (null != myPlayer && true == surface.isValid()) {
        	if (false == myPlayer.setVideoWindow(playPort, 0, holder)) {	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}
    	}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
//	    while ((!holder.getSurface().isValid()) || (this.myPlayer == null) || (this.myPlayer.setVideoWindow(this.playPort, 0, null)))
//	      return;
//	    Log.e(this.TAG, "Player releaseVideoWindow failed!");
	}

	/**
	 * @param monitorInfo
	 */
	public void setMonitorInfo(MonitorInfo monitorInfo) {
		this.monitorInfo = monitorInfo;
	}
	
	class PlayThread extends Thread{
		/**
		 */
		private RealPlayCallBack mRealPlayCallBack = new RealPlayCallBack() {
			
			@Override
			public void fRealDataCallBack(int lRealHandle, int dataType, byte[] pBuffer, int pBufSize) {
				// TODO 流播放
				/* 播放端口检测 */
				if(playPort < 0){
					Log.v(TAG, "playPort < 0 err.");
					return;
				}
				
				/* 处理数据 */
				switch (dataType) {
				// 系统头数据 
				case HCNetSDK.NET_DVR_SYSHEAD:
//					Log.v(TAG, "get Port: " + playPort);

					
					/* setStreamOpenMode */
					if(myPlayer.setStreamOpenMode(playPort, Player.STREAM_REALTIME)){
						Log.v(TAG, "setStreamOpenMode(). - pBufSize=" + pBufSize);
//						writeText("setStreamOpenMode(). - pBufSize=" + pBufSize);
					}else{
						//setStreamOpenMode err.
						Log.v(TAG, "setStreamOpenMode err.--------");
						return;
					}
					Log.v(TAG, "openStream - getLastError = " + myPlayer.getLastError(playPort));

					if(!myPlayer.setSecretKey(playPort, 1, "ge_security_3477".getBytes(), 128))
					{
						Log.e(TAG, "setSecretKey failed");
						break;
					}
					
					/* openStream */
					if(myPlayer.openStream(playPort, pBuffer, pBufSize, 2*1024*1024)){
						Log.v(TAG, "openStream().");
//						writeText("openStream().");
					}else{
						//openStream err.
						Log.v(TAG, "openStream err.--------");
						return;
					}
					Log.v(TAG, "openStream - getLastError = " + myPlayer.getLastError(playPort));
					
					/* setDisplayBuf */
//					if(myPlayer.setDisplayBuf(playPort, 15)){
//						Log.v(TAG, "setDisplayBuf = " + myPlayer.getDisplayBuf(playPort));
//					}else {
//						Log.v(TAG, "setDisplayBuf err.--------");
//					}
					
//					Log.v(TAG, "DisplayBuf  = " + myPlayer.getDisplayBuf(playPort));
//					if(myPlayer.setDisplayRegion(playPort, 0, null, null, 1)){
//						Log.v(TAG, "setDisplayRegion().");
//					}else{
//						//openStream err.
//						Log.v(TAG, "setDisplayRegion err.--------");
//						return;
//					}
					
					/* play */
					if(myPlayer.play(playPort, MonitorSurfaceView.this.getHolder())){
						Log.v(TAG, "play().");
					}else{
						//playStream err.
						Log.v(TAG, "playStream err.--------");
						return;
					}
//					Log.v(TAG, "play - getLastError = " + myPlayer.getLastError(playPort));
//					Log.v(TAG, "play hkNetSdk - getLastError = " + hkNetSdk.NET_DVR_GetLastError());
					break;

				// 流数据（包括复合流或音视频分开的视频流数据）
	    		case HCNetSDK.NET_DVR_STD_AUDIODATA:
	    		case HCNetSDK.NET_DVR_STD_VIDEODATA:
				case HCNetSDK.NET_DVR_STREAMDATA:
					
					if(playFlag && myPlayer.inputData(playPort, pBuffer, pBufSize) && playPort!=-1){
//						Log.v(TAG, "in inputData(). - pBufSize=" + pBufSize);
						playFlag = true;		
					}else{
						//流错误
						playFlag = false;
						Log.v(TAG, "inputData err.");
					}
//					Log.v(TAG, "input getLastError = " + myPlayer.getLastError(playPort));
//					Log.v(TAG, "play hkNetSdk - getLastError = " + hkNetSdk.NET_DVR_GetLastError());
					break;

				// 音频数据
				case HCNetSDK.NET_DVR_AUDIOSTREAMDATA:

					Log.v(TAG, "audio .");
					break;
					
				default:
					break;
				}
			}
		};
		
		@Override
		public void run() {
			try {
				/* 初始化播放库 */
				playPort = myPlayer.getPort();
				/* 初始化NetSDK */
				hkNetSdk = new HCNetSDK();
				if(hkNetSdk == null){
					Log.v(TAG, "hkNetSdk == null");
					mHandler.sendEmptyMessage(ERR_LOGIN_FAILED);
					return;
				}
				if(!hkNetSdk.NET_DVR_Init()){
					Log.v(TAG, "NET_DVR_Init() failed.");
					mHandler.sendEmptyMessage(ERR_LOGIN_FAILED);
					return;
				}
				if(!hkNetSdk.NET_DVR_SetExceptionCallBack(mExceptionCallBack)){
					Log.v(TAG, "mExceptionCallBack failed.");
					mHandler.sendEmptyMessage(ERR_LOGIN_FAILED);
					return;
				}
				hkNetSdk.NET_DVR_SetConnectTime(10000);
				hkNetSdk.NET_DVR_SetLogToFile(3, Environment.getExternalStorageDirectory().getPath() + "/sdklog/", true);
				int code = 0;
				//check info
//				mLog(monitorInfo.serverip + "|" + monitorInfo.serverport + "|" + monitorInfo.username + "|" + monitorInfo.userpwd);
				if(monitorInfo.serverip.equals("")||monitorInfo.userpwd.equals("")||monitorInfo.username.equals("")){
					hkNetSdk.NET_DVR_Cleanup();
					throw new Exception("check - error.");
				}
				
				/* 登录，获取userId */
				NET_DVR_DEVICEINFO_V30 devInfo = new NET_DVR_DEVICEINFO_V30();
				userId = hkNetSdk.NET_DVR_Login_V30(
						monitorInfo.serverip,
						monitorInfo.serverport,
						monitorInfo.username,
						monitorInfo.userpwd,
						devInfo);
				if(userId<0){
					//出错
					code = hkNetSdk.NET_DVR_GetLastError();
					hkNetSdk.NET_DVR_Cleanup();
					throw new Exception("Login Failed. code - " + code);
				}else{
					/* 成功 */
					Log.v(TAG, "下面是设备信息************************");
					Log.v(TAG, "通道开始" + devInfo.byStartChan);
					Log.v(TAG, "通道个数=" + devInfo.byChanNum);
					Log.v(TAG, "设备类型=" + devInfo.byDVRType);
					Log.v(TAG, "ip通道个数=" + devInfo.byIPChanNum);
					byte[] sbbyte = devInfo.sSerialNumber;  
		            String sNo = "";
		            for (int i = 0; i < sbbyte.length; i++) {  
		                sNo += String.valueOf(sbbyte[i]);  
		            }
					Log.v(TAG, "设备序列号=" + sNo);
					Log.v(TAG, "userId=" + userId);
					Log.v(TAG, "********************************");
					
					NET_DVR_IPPARACFG_V40 struIPPara = new NET_DVR_IPPARACFG_V40();
					hkNetSdk.NET_DVR_GetDVRConfig(userId, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, struIPPara);
					
					int iFirstChannelNo = -1;// get start channel no
					if(struIPPara.dwAChanNum > 0)
					{
						iFirstChannelNo = 1;
					}
					else
					{
						iFirstChannelNo = struIPPara.dwStartDChan;
					}
					Log.v(TAG, "iFirstChannelNo:" +iFirstChannelNo);

					NET_DVR_CLIENTINFO clientInfo = new NET_DVR_CLIENTINFO();
//			        clientInfo.lChannel =  iFirstChannelNo; 	// start channel no + preview channel
			        clientInfo.lChannel =  monitorInfo.channel; 	// start channel no + preview channel
			        clientInfo.lLinkMode = 1<<31;  			// bit 31 -- 0,main stream;1,sub stream
			        										// bit 0~30 -- link type,0-TCP;1-UDP;2-multicast;3-RTP 
			        clientInfo.sMultiCastIP = null;
			        
//					/* 预览模式 */
//					NET_DVR_CLIENTINFO clientInfo = new NET_DVR_CLIENTINFO();
//					clientInfo.lChannel = monitorInfo.channel;
//					/* 最高位(31)为 0表示主码流，为1 表示子码流。
//					 * 0～30位表示连接
//					 * 方式：0－TCP 方式；1－UDP 方式；2－多播方式；3－RTP 方式
//					 * */ 
//					clientInfo.lLinkMode = 1<<31;
//					clientInfo.sMultiCastIP = null;
					
					playFlag = true;
					mPlayhandle = hkNetSdk.NET_DVR_RealPlay_V30(userId, clientInfo, mRealPlayCallBack, false);
//					Log.v(TAG, "mPlayhandle = " + mPlayhandle);
//					Log.v(TAG, "NETSDK-error - " + hkNetSdk.NET_DVR_GetLastError());
					mHandler.sendEmptyMessage(SUCCESS_LOGIN);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.v(TAG, e.getMessage());
				mHandler.sendEmptyMessage(ERR_LOGIN_FAILED);
				return;
			}
		}
		
	}
	
	PlayThread mPlayThread = null;
	public boolean startPlay() {
		//显示loading 
		if(mLoadingDialog == null){
			mLoadingDialog = new LoadingDialog(mContext,"登录中。。。");
		}
		mLoadingDialog.show();
		mPlayThread = new PlayThread();
		mPlayThread.start();
		return true;
	}	
	
	/* 1-暂停 0-恢复*/
	public void pausePlay() {
		if(myPlayer!=null){
			myPlayer.pause(playPort, playFlag?1:0);
		}
	}
	public void stopPlay() {
		try {

			hkNetSdk.NET_DVR_StopRealPlay(playPort);
			hkNetSdk.NET_DVR_Logout_V30(userId);
			userId = -1;
			hkNetSdk.NET_DVR_Cleanup();
			
			if(myPlayer != null){
				
				myPlayer.stop(playPort);
				myPlayer.closeStream(playPort);
				myPlayer.freePort(playPort);
				
				playPort = -1;
			}
			
			playFlag = false;
			
		} catch (Exception e) {
			Log.v(TAG, e.getMessage().toString());
		}
	}
	
	/**
	 */
	private ExceptionCallBack mExceptionCallBack = new ExceptionCallBack() {
		
		@Override
		public void fExceptionCallBack(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			int code = hkNetSdk.NET_DVR_GetLastError();
			Log.v(TAG, "异常回调函数运行! ExceptionCallBack - " + code);
		}
	};
	
	
	

	/**
	 * @return
	 */
	public HCNetSDK getHkNetSdk() {
		return hkNetSdk;
	}
	
	public boolean cam_contrl(int agrs, int action) {
		mLog("in_cam_contrl() - handle:" + mPlayhandle);
		if(mPlayhandle<0){
			return false;
		}
		if(CTRL_MAP.get(agrs)!=null){
			try {
				if(!hkNetSdk.NET_DVR_PTZControl(mPlayhandle, CTRL_MAP.get(agrs), action)){
					mLog("error: " + agrs + " failed");
					return false;
				}
			} catch (Exception e) {
				mLog(e.getMessage());
			}
		}
		return true;
	}
	
	public boolean cam_preset(int presetCmd, int index) {
		if(mPlayhandle<0){
			return false;
		}
		try {
			if(!hkNetSdk.NET_DVR_PTZPreset(mPlayhandle, presetCmd, index)){
				mLog("error: presetCmd - " + presetCmd + " failed");
				return false;
			}
		} catch (Exception e) {
			mLog(e.getMessage());
		}
		return true;
	}
	
	private void mLog(String msg) {
		Log.v(TAG, msg);
		return;
	}
	
	AlertDialog mAlertDialog = null;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
			
			switch(msg.what){
			case ERR_LOGIN_FAILED:
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				mAlertDialog = builder.setTitle("Tips")
						.setMessage("Login failed.")
						.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mAlertDialog.dismiss();
								((Activity)mContext).finish();
							}
						})
						.create();
				mAlertDialog.show();
				break;
			default:
				break;
			}
		}
		
	};
}
