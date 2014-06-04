package com.emos.canbo.update;

import java.io.File;

import com.emos.canbo.R;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class DownloadComponent {
	private static String mango = "mango";

	private static String folderName = "CanboDownload";
	private static String fileName = "apkCachedFile.apk";

	String apkUrl = "http://202.104.31.132:8888/update/getfile?file_id=108";
	Context ctx;

	private DownloadHandler handler;
	private DownloadManager downloadManager = null;

	public DownloadComponent(Context ctx) {
		super();
		this.ctx = ctx;
		handler = new DownloadHandler();
		mDownloadBrocastReceiver = new DownloadBrocastReceiver();
		ctx.registerReceiver(mDownloadBrocastReceiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public void down() {
		downloadManager = (DownloadManager) ctx
				.getSystemService(Context.DOWNLOAD_SERVICE);
		checkFolder(Environment.getExternalStorageDirectory() + folderName);
		checkFileExist();
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(apkUrl));
		request.setDestinationInExternalPublicDir(folderName, fileName);
		request.setTitle("canbo download");
		request.setDescription("canbo download desc");
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		long downId = downloadManager.enqueue(request);
		Log.v(mango, "downID = " + downId);
	}

	private void checkFileExist() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + folderName + File.separator + fileName);
		if(file.exists()){
			file.delete();
		}
	}

	private boolean checkFolder(String folderName) {
		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder
				.mkdirs();
	}

	public void testBroadcast() {
		mDownloadBrocastReceiver.onReceive(ctx, null);
	}

	private DownloadBrocastReceiver mDownloadBrocastReceiver;

	class DownloadBrocastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(mango, "broadcast");
			Toast.makeText(ctx, "下载完成", Toast.LENGTH_LONG).show();

			long completeDownloadId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(completeDownloadId);
			Cursor c = null;
			String downFileName = null;
			try {
				if (downloadManager != null) {
					c = downloadManager.query(query);
					if (c != null && c.moveToFirst()) {
						downFileName = c
								.getString(c
										.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
						// String uri =
						// c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
						if (downFileName == null) {
							Toast.makeText(context, "找不到文件", Toast.LENGTH_LONG)
									.show();
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			File apkFile = new File(downFileName);

			Intent i = new Intent(Intent.ACTION_VIEW);
			Log.v(mango, apkFile.toString());
			i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
					"application/vnd.android.package-archive");
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			// PendingIntent
			PendingIntent pIntent = PendingIntent.getActivity(context,
					R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification n = new NotificationCompat.Builder(ctx)
					.setContentTitle("程序更新").setContentText("下载完成")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(pIntent).setAutoCancel(true).build();
			NotificationManager nm = (NotificationManager) ctx
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(R.drawable.ic_launcher, n);

			ctx.unregisterReceiver(mDownloadBrocastReceiver);
		}

	}

	@SuppressLint("HandlerLeak")
	class DownloadHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	
}
