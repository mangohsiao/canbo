package com.emos.canbo.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.emos.canbo.MTag;
import com.emos.canbo.tools.MD5Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class UpdateComponent {

	public final static int MSG_CHECK_SUCCESS = 100;
	public final static int MSG_NO_NEED_UPDATE = 101;
	public final static int MSG_NEED_TO_UPDATE = 102;
	public final static int MSG_CONNECT_ERR = 104;
	public final static int MSG_RESPONSE_ERR = 105;
	public final static int MSG_CHECK_ERR = 106;
	public final static int MSG_UPDATE_PROGRESS = 107;
	public final static int MSG_DOWN_FINISH = 108;
	public final static int MSG_DOWN_ERR = 109;
	public final static int MSG_DOWN_CANCEL = 110;
	public final static int MSG_INSTALL = 111;
	public final static int MSG_ALREADY_DOWN = 112;

	public final static String INDEX_VER_CODE = "ver_code";
	public final static String INDEX_VER_NAME = "ver_name";
	public final static String INDEX_UPDATE_DESC = "update_desc";
	public final static String INDEX_FILE_ID = "file_id";
	public final static String INDEX_FILE_PATH = "file_path";
	public final static String INDEX_FILE_SUM = "file_sum";

	public final static String HOST = "125.216.243.235";
	public final static String APK_PATH = "/storage/sdcard0/CanboDownloads/";
	public final static String APK_FILE_NAME = "apkCacheFile.apk";

	Context context = null;

	int this_ver_code = -1;
	String this_ver_name = null;
	VersionInfo ver_info = null;

	ProgressDialog pgDialog = null;

	boolean isAlert = true;

	MDownProgressDialog downProgressDialog = null;
	boolean flag_DownInterrupt = false;
	int down_progress = 0;

	public UpdateComponent(Context ctx) {
		this.context = ctx;
	}

	public boolean check_update() {
		/* get this version */
		this_ver_code = get_this_ver_code(context);
		this_ver_name = get_this_ver_name(context);

		/* check version from internet */
		check_net_version();

		return false;
	}

	public void check_update_background() {
		isAlert = false;
		check_update();
	}

	public void check_update_with_alert() {
		isAlert = true;
		check_update();
	}

	private void check_net_version() {
		/* http thread */
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("pkg_name", context
						.getPackageName()));
				StringBuffer url = new StringBuffer();
				url.append("http://");
				url.append(ConstValue.UPDATE_HOST);
				url.append(":");
				url.append(ConstValue.UPDATE_PORT);
				url.append(ConstValue.UPDATE_URL);
				JSONObject check_res = null;
				try {
					check_res = MyHttpUtil.getJson(url.toString(), params);
					Log.v(MTag.mango, "JSON: " + check_res.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
					handler.sendEmptyMessage(MSG_CONNECT_ERR);
					return;
				}

				if (check_res == null) {
					handler.sendEmptyMessage(MSG_RESPONSE_ERR);
					return;
				}

				int res = -1;
				// get RES
				try {
					res = check_res.getInt("RES");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// switch RES
				switch (res) {
				case 0: // CHECK_SUCCESS
					ver_info = new VersionInfo();
					try {
						ver_info.ver_code = check_res.getInt(INDEX_VER_CODE);
						ver_info.ver_name = check_res.getString(INDEX_VER_NAME);
						ver_info.update_desc = check_res
								.getString(INDEX_UPDATE_DESC);
						ver_info.file_id = check_res.getInt(INDEX_FILE_ID);
						// ver_info.file_path = "";
						ver_info.file_sum = check_res.getString(INDEX_FILE_SUM);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (this_ver_code < ver_info.ver_code) {
						/* need to UPDATE */
						handler.sendEmptyMessage(MSG_NEED_TO_UPDATE);
					} else {
						/* NO need, show VERSION */
						handler.sendEmptyMessage(MSG_NO_NEED_UPDATE);
					}
					// handler.sendEmptyMessage(MSG_CHECK_SUCCESS);
					break;

				case 101: // PACKAGE_NAME_NULL
					handler.sendEmptyMessage(MSG_CHECK_ERR);
					break;

				case 102: // PACKAGE_NOT_EXIST
					handler.sendEmptyMessage(MSG_CHECK_ERR);
					break;

				case 103: // FILE_NOT_EXIST
					handler.sendEmptyMessage(MSG_CHECK_ERR);
					break;

				case 104: // DB_CONNECT_FAILED
					handler.sendEmptyMessage(MSG_CHECK_ERR);
					break;

				default:
					break;
				}

				/*
				 * ver_info = new VersionInfo(); try { parsing the json result
				 * ver_info.ver_code = check_res.getInt(INDEX_VER_CODE);
				 * ver_info.ver_name = check_res.getString(INDEX_VER_NAME);
				 * ver_info.file_id = check_res.getInt(INDEX_FILE_ID);
				 * ver_info.file_path = check_res.getString(INDEX_FILE_PATH); }
				 * catch (JSONException e) { e.printStackTrace(); return; }
				 * 
				 * need to update or NOT??? if(this_ver_code <
				 * ver_info.ver_code){ //need to update
				 * handler.sendEmptyMessage(MSG_TO_UPDATE); }else{ //no need to
				 * update handler.sendEmptyMessage(MSG_NO_NEED_UPDATE); }
				 */
			}
		};
		Thread thread = new Thread(runnable);

		// processing waiting dialog
		if (isAlert) {
			pgDialog = new ProgressDialog(context);
			pgDialog.setCancelable(false);
			pgDialog.setMessage("检查更新，请等待");
			pgDialog.show();
		}
		thread.start();
	}

	private int get_this_ver_code(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	private String get_this_ver_name(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// dismiss the waiting dialog.
			if (pgDialog != null) {
				pgDialog.dismiss();
			}

			// handle msg
			switch (msg.what) {
			case MSG_CHECK_SUCCESS:

				break;

			case MSG_NO_NEED_UPDATE:
				if (!isAlert)
					return;
				StringBuffer show_msg = new StringBuffer();
				show_msg.append("已是最新");
				show_msg.append("\n版本：");
				show_msg.append(ver_info.ver_name);
//				show_msg.append("\n发布日期：2013年11月08日");

				/* show the info of version */
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("软件更新");
				builder.setMessage(show_msg);
				builder.setPositiveButton("确认", new MDismissListener());
				builder.show();
				break;

			case MSG_NEED_TO_UPDATE:
				/* update version? */
				StringBuffer show_update = new StringBuffer();
				show_update.append("当前使用版本：\n");
				show_update.append(this_ver_name);
				show_update.append("\n新版本：\n");
				show_update.append(ver_info.ver_name);
				show_update.append("\n发布日期：\n");
//				show_update.append("2013-09-09\n");
				show_update.append(ver_info.update_desc);
				AlertDialog.Builder dialog_update = new AlertDialog.Builder(
						context);
				dialog_update.setTitle("发现新版本！").setMessage(show_update)
						.setPositiveButton("立即更新", new MUpdateListener())
						.setNegativeButton("取消", new MDismissListener())
						.create();
				dialog_update.show();
				break;

			case MSG_CONNECT_ERR:
				if (!isAlert)
					return;
				/* connect error */
				StringBuffer show_err = new StringBuffer();
				show_err.append("访问网络错误，未能连接至：\n");
				show_err.append(ConstValue.UPDATE_HOST);
				AlertDialog dialog_err = new AlertDialog.Builder(context)
						.setTitle("软件更新").setMessage(show_err)
						.setPositiveButton("确认", new MDismissListener())
						.create();
				dialog_err.show();
				break;

			case MSG_CHECK_ERR:
				break;
			case MSG_RESPONSE_ERR:
				break;
			case MSG_UPDATE_PROGRESS:
				if (downProgressDialog == null) {
					downProgressDialog = new MDownProgressDialog(context);
				}
				downProgressDialog.setProgress(down_progress);
				if (!downProgressDialog.isShowing()) {
					downProgressDialog.show();
				}
				break;
			case MSG_DOWN_FINISH:
			case MSG_ALREADY_DOWN:
				Log.v("mango", "MSG_DOWN_FINISH");
				if (downProgressDialog != null) {
					downProgressDialog.dismiss();
				}
//				Toast.makeText(context, "MSG_DOWN_FINISH", Toast.LENGTH_LONG)
//						.show();
				AlertDialog dialog_install = new AlertDialog.Builder(context)
				.setTitle("软件更新").setMessage("下载完成，是否安装？")
				.setPositiveButton("确认", new MInstallListener())
				.setNegativeButton("取消", new MDismissListener())
				.create();
				dialog_install.show();
				break;
			case MSG_DOWN_ERR:
				if (downProgressDialog != null) {
					downProgressDialog.dismiss();
				}
				Toast.makeText(context, "MSG_DOWN_ERR", Toast.LENGTH_LONG)
						.show();
				break;
			case MSG_DOWN_CANCEL:
				if (downProgressDialog != null) {
					downProgressDialog.dismiss();
				}
				// TODO delete the file .
				Toast.makeText(context, "已取消", Toast.LENGTH_LONG).show();
				break;
			case MSG_INSTALL:
				break;
			default:
				break;
			}

		}// end of handlerMessage

	};// end of handler.

	private class MDismissListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}

	}

	private class MInstallListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			/* INSTALL APKFILE */
//			installApk(new File(context.getFilesDir() + "/" + APK_FILE_NAME));
			installApk(new File(APK_PATH + APK_FILE_NAME));
		}
		
	}
	
	private class MUpdateListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			if (ver_info == null) {
				return;
			}
			Toast.makeText(context, "更新。。。。", Toast.LENGTH_LONG).show();
			// TODO download file and update.
			String downUrlStr = "http://" + ConstValue.UPDATE_HOST + ":"
					+ ConstValue.UPDATE_PORT + ConstValue.DOWN_URL
					+ "?file_id=" + ver_info.file_id;
			Log.v("mango", downUrlStr);
			MDownRunnable runner = new MDownRunnable(downUrlStr);
			Thread thread = new Thread(runner);
			thread.start();
		}

	}

	private class MDownRunnable implements Runnable {
		String urlStr = null;

		public MDownRunnable(String urlStr) {
			super();
			this.urlStr = urlStr;
		}

		@Override
		public void run() {
			if (urlStr == null)
				return;
			try {
				URL url = new URL(urlStr);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setConnectTimeout(8000);
				con.connect();
				int length = con.getContentLength();
				InputStream is = con.getInputStream();

				String path = context.getFilesDir() + "/";
				File folder = new File(APK_PATH);
				if(!folder.exists()){
					folder.mkdir();
				}
				File apkTemp = new File(APK_PATH + APK_FILE_NAME);
				if (apkTemp.exists()) {
					// caculate MD5.
					String md5Str = MD5Util.getFileMD5(apkTemp);
					if (md5Str.toUpperCase(Locale.US).equals(
							ver_info.file_sum.toUpperCase(Locale.US))) {
						handler.sendEmptyMessage(MSG_ALREADY_DOWN);
						return;
					}
					// if not the newest , then delete
					apkTemp.delete();
				}
				FileOutputStream fos = new FileOutputStream(apkTemp);

				// downloading
				int count = 0;
				byte[] buf = new byte[4096];
				int read;
				down_progress = 0;
				flag_DownInterrupt = false;
				while (!flag_DownInterrupt && (read = is.read(buf)) > 0) {
					count += read;
					down_progress = (int) (((float) count / length) * 100);
					// write file
					fos.write(buf, 0, read);
					// update progress , send message.
					handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);

					if (count == length) {
						// download OK.
						//check MD5
						String checkSumStr = MD5Util.getFileMD5(apkTemp);
						Log.v("mango", "sum: " + checkSumStr);
						Log.v("mango", "ver.sum: " + ver_info.file_sum);
						handler.sendEmptyMessage(MSG_DOWN_FINISH);
					}
				}
				if (flag_DownInterrupt) {
					handler.sendEmptyMessage(MSG_DOWN_CANCEL);
				}
				Log.v("mango", "Thread finish " + count);
				is.close();
				fos.close();
			} catch (MalformedURLException e) {
				handler.sendEmptyMessage(MSG_DOWN_ERR);
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendEmptyMessage(MSG_DOWN_ERR);
				e.printStackTrace();
			}
		}
	}

	private class MDownProgressDialog extends ProgressDialog {

		public MDownProgressDialog(Context context) {
			super(context);
			this.setProgressStyle(STYLE_HORIZONTAL);
			this.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消",
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							flag_DownInterrupt = true;
						}
					});
		}

	}

	private void installApk(File apkFile) {
		if (!apkFile.exists()) {
			return;
		}
		Log.v("mango", "file : " + apkFile.toString());
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}
}
