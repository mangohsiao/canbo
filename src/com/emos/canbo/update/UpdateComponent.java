package com.emos.canbo.update;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.emos.utils.ConstValue;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UpdateComponent {

	public final static int MSG_NO_NEED_UPDATE = 101;
	public final static int MSG_TO_UPDATE = 102;
	public final static int MSG_CONNECT_ERR = 104;
	public final static String INDEX_VER_CODE = "ver_code";
	public final static String INDEX_VER_NAME = "ver_name";
	public final static String INDEX_FILE_ID = "file_id";
	public final static String INDEX_FILE_PATH = "file_path";
	
	public final static String HOST = "125.216.243.235";
	
	/**
	 */
	Context context = null;
	
	/**
	 */
	int this_ver_code = -1;
	/**
	 */
	String this_ver_name = null;
	/**
	 */
	VersionInfo ver_info = null;
	
	/**
	 */
	ProgressDialog pgDialog = null;
	
	public UpdateComponent(Context ctx) {
		// TODO Auto-generated constructor stub
		this.context = ctx;
	}
	
	public boolean check_update() {
		/* get this version */
		this_ver_code = get_this_ver_code(context);
		this_ver_name = get_this_ver_name(context);
		
		/* check version from internet  */		
		check_net_version();
		
		return false;
	}
	
	private void check_net_version() {
		// TODO Auto-generated method stub
		/* http thread */
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("pkg_name", context.getPackageName()));
				StringBuffer url = new StringBuffer();
				url.append("http://");
				url.append(ConstValue.UPDATE_HOST);
				url.append(":");
				url.append(ConstValue.UPDATE_PORT);
				url.append(ConstValue.UPDATE_URL);
				JSONObject check_res = MyHttpUtil.getJson(url.toString(), params);
				
				if(check_res == null){
					System.out.println("check_res == null");
					handler.sendEmptyMessage(MSG_CONNECT_ERR);
					return;
				}
				
				ver_info = new VersionInfo();
				try {
					/* parsing the json result */
					ver_info.ver_code = check_res.getInt(INDEX_VER_CODE);
					ver_info.ver_name = check_res.getString(INDEX_VER_NAME);
					ver_info.file_id = check_res.getInt(INDEX_FILE_ID);
					ver_info.file_path = check_res.getString(INDEX_FILE_PATH);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

				/* need to update or NOT??? */
				boolean need_to_update = false;
				if(this_ver_code < ver_info.ver_code){
					need_to_update = true;
				}
				if(need_to_update){
					//need to update
					handler.sendEmptyMessage(MSG_TO_UPDATE);
				}else{
					//no need to update
					handler.sendEmptyMessage(MSG_NO_NEED_UPDATE);
				}
			}
		};
		Thread thread = new Thread(runnable);
	
		//processing waiting dialog
		pgDialog = new ProgressDialog(context);
		pgDialog.setCancelable(false);
		pgDialog.setMessage("查询中，请等待");
		pgDialog.show();
		thread.start();
	}

	private int get_this_ver_code(Context context){
		int verCode = -1;
        try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return verCode;
	}
	
	private String get_this_ver_name(Context context){
        try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//dismiss the waiting dialog.
			if(pgDialog != null){
				pgDialog.dismiss();
			}
			// TODO handle msg
			switch (msg.what) {
			case MSG_NO_NEED_UPDATE:
				StringBuffer show_msg = new StringBuffer();
				show_msg.append("已是最新");
				show_msg.append("\n版本号：");
				show_msg.append(ver_info.ver_name);
				show_msg.append("\n发布日期：2013年11月08日");
				
				/* show the info of version */
				AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle("软件更新")
					.setMessage(show_msg)
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					})
					.create();
				dialog.show();
				break;

			case MSG_TO_UPDATE:
				/* update version? */
				StringBuffer show_update = new StringBuffer();
				show_update.append("当前使用版本：\n");
				show_update.append(this_ver_name);
				show_update.append("\n新版本号：\n");
				show_update.append(ver_info.ver_name);
				show_update.append("\n发布日期：\n");
				show_update.append("2013-09-09");
				AlertDialog dialog_update = new AlertDialog.Builder(context)
					.setTitle("发现新版本！")
					.setMessage(show_update)
					.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Update processing
							Toast.makeText(context, "更新。。。。", Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					})
					.create();
				dialog_update.show();
				break;
				
			case MSG_CONNECT_ERR:
				/* connect error */
				StringBuffer show_err = new StringBuffer();
				show_err.append("访问网络错误，未能连接至：\n");
				show_err.append(ConstValue.UPDATE_HOST);
				AlertDialog dialog_err = new AlertDialog.Builder(context)
					.setTitle("软件更新")
					.setMessage(show_err)
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					})
					.create();
				dialog_err.show();
				break;

			default:
				break;
			}			
			
		}
		
	};
}
