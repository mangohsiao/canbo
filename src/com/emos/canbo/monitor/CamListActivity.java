/**
 * 
 */
package com.emos.canbo.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emos.canbo.CanboCommon;
import com.emos.canbo.MyApp;
import com.emos.canbo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * @author Administrator
 *
 */
public class CamListActivity extends Activity implements OnItemClickListener{

	private final static String TAG = "CamListActivity";
	
	/**
	 */
	private Button btn_cam_list_add = null;
	
	/**
	 */
	private ListView listview_cam_list = null;
	/**
	 */
	private MyCamListAdapter myCamListAdapter = null;
	/**
	 */
	private List<CameraInfo> data_list = null;
	/**
	 */
	private int current_position = -1;
	
	int login_mode = 0;
	
	/**
	 */
	CamDb camDb = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cam_list);
		
		login_mode = ((MyApp)getApplicationContext()).getLoginModeGLB();
		
		/* initial */
		initial();

		/* filling the data_list */
		data_filling();
		
		listview_cam_list.setAdapter(myCamListAdapter);
		listview_cam_list.setOnItemClickListener(this);
		
		btn_cam_list_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.emos.canbo.monitor.AddCamActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initial() {
		
		/* find listview  */
		listview_cam_list = (ListView)findViewById(R.id.listview_cam_list);
		btn_cam_list_add = (Button)findViewById(R.id.btn_cam_list_add);
		
		/* datalist construction */
		data_list = new ArrayList<CameraInfo>();

		/* myCamListAdapter construction */
		myCamListAdapter = new MyCamListAdapter(this);
		
		/* get DB */
		camDb = new CamDb(getApplicationContext());
		
		/* test data */
//		Map<String, String> ip01 = new HashMap<String, String>();
//		ip01.put("ip", "125.216.243.231");
//		data_list.add(ip01);		
//		Map<String, String> ip02 = new HashMap<String, String>();
//		ip02.put("ip", "125.216.243.105");
//		data_list.add(ip02);
		
	}

	private void data_filling() {
		// TODO read data
		
//		String insert01 = "INSERT INTO cam_table(cam_name,cam_ip,cam_port) VALUES('mangoCam01','125.216.111.111','8000');";		
//		SQLiteDatabase writer =  camDb.getWritableDatabase();
//		try {
//			writer.execSQL(insert01);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		read_data_list();
	}
	
	class MyCamListAdapter extends BaseAdapter{

		Context mContext;
		LayoutInflater mInflater;
		
		public MyCamListAdapter(Context ctx) {
			mContext = ctx;
			mInflater = LayoutInflater.from(mContext);
		}
		
		@Override
		public int getCount() {
			return data_list.size();
		}

		@Override
		public Object getItem(int position) {
			// 
			return data_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//
			final int pos = position;
			ViewHolder viewHolder = null;
			if(convertView == null){
				/* inflate the view */
				convertView = mInflater.inflate(R.layout.list_cam_item, parent, false);
			}else {
				
			}
			
			/* find widget */
			//TODO
			TextView txv_cam_item_name = (TextView)convertView.findViewById(R.id.txv_cam_item_name);
			TextView txv_cam_item_ip = (TextView)convertView.findViewById(R.id.txv_cam_item_ip);
			txv_cam_item_name.setText(data_list.get(position).cam_name);
			if(login_mode == CanboCommon.LOGIN_MODE_LAN){
				txv_cam_item_ip.setText(data_list.get(position).cam_ip);
			}else{
				txv_cam_item_ip.setText("ÍâÍøµÇÂ½");
			}
			LinearLayout linearlayout_cam_item = (LinearLayout)convertView.findViewById(R.id.linearlayout_cam_item);
			LinearLayout linearlayout_cam_edit = (LinearLayout)convertView.findViewById(R.id.linearlayout_cam_edit);
			LinearLayout linearlayout_cam_preview = (LinearLayout)convertView.findViewById(R.id.linearlayout_cam_preview);
			LinearLayout cam_list_down_line_icon = (LinearLayout)convertView.findViewById(R.id.cam_list_down_line_icon);
			
			if(position == current_position){
				cam_list_down_line_icon.setBackgroundResource(R.drawable.cam_list_item_icon_down);
				linearlayout_cam_item.setVisibility(View.VISIBLE);
				linearlayout_cam_edit.setClickable(true);
				linearlayout_cam_preview.setClickable(true);
			}else{
				cam_list_down_line_icon.setBackgroundResource(R.drawable.null_selector);
				linearlayout_cam_item.setVisibility(View.GONE);
				linearlayout_cam_edit.setClickable(false);
				linearlayout_cam_preview.setClickable(false);
			}
			
			linearlayout_cam_edit.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
//					new AlertDialog.Builder(mContext).setTitle(data_list.get(pos).cam_ip + " - " + data_list.get(pos).cam_port).show();
					//Intent Action
					Intent intent = new Intent(mContext, CamEditActivity.class);
					//bundle
					CameraInfo tempData = data_list.get(pos);
					Bundle bundle = new Bundle();
					bundle.putInt("cam_id", tempData.cam_id);
					bundle.putString("cam_ip", tempData.cam_ip);
					bundle.putInt("cam_port", tempData.cam_port);
					bundle.putString("cam_username", tempData.cam_username);
					bundle.putString("cam_pswd", tempData.cam_pswd);
					bundle.putInt("cam_channel", tempData.cam_channel);
					bundle.putString("cam_name", tempData.cam_name);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
			linearlayout_cam_preview.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					
					/* test print out  */
//					new AlertDialog.Builder(mContext).setTitle(data_list.get(pos).get("cam_ip") + " - " + data_list.get(pos).get("cam_port"))
//					.setMessage("").show();
					
					//Intent Action
					CameraInfo tempData = data_list.get(pos);
					Bundle bundle = new Bundle();
					bundle.putInt("cam_id", tempData.cam_id);
					bundle.putString("cam_ip", tempData.cam_ip);
					bundle.putInt("cam_port", tempData.cam_port);
					bundle.putString("cam_username", tempData.cam_username);
					bundle.putString("cam_pswd", tempData.cam_pswd);
					bundle.putInt("cam_channel", tempData.cam_channel);
					bundle.putString("cam_name", tempData.cam_name);
					Intent intent = new Intent(mContext, com.emos.canbo.monitor.MonitorActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
						
			return convertView;
		}

		class ViewHolder{
			//view member
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		current_position = position;
		myCamListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		update_data_list();
		myCamListAdapter.notifyDataSetChanged();
	}

	private void update_data_list() {
		// TODO Auto-generated method stub
		data_list.clear();
		read_data_list();
	}

	private void read_data_list() {
		// TODO Auto-generated method stub
		String select01 = "SELECT * FROM  cam_table  ORDER BY cam_id;";
		SQLiteDatabase reader = camDb.getReadableDatabase();
		Cursor c = reader.rawQuery(select01, null);
		while(c.moveToNext()){
			CameraInfo temp = new CameraInfo();
			temp.cam_id = c.getInt(c.getColumnIndex("cam_id"));
			if(login_mode == CanboCommon.LOGIN_MODE_LAN){
				temp.cam_ip = c.getString(c.getColumnIndex("cam_ip"));
			}else{
				temp.cam_ip = ((MyApp)getApplicationContext()).getConIp();
			}
			temp.cam_port = c.getInt(c.getColumnIndex("cam_port"));
			temp.cam_name = c.getString(c.getColumnIndex("cam_name"));
			temp.cam_username = c.getString(c.getColumnIndex("cam_username"));
			temp.cam_pswd = c.getString(c.getColumnIndex("cam_pswd"));
			temp.cam_channel = c.getInt(c.getColumnIndex("cam_channel"));
			data_list.add(temp);
		}
		reader.close();
	}

	private void mLog(String msg) {
		// TODO Auto-generated method stub
		Log.v(TAG, msg);
	}

}
