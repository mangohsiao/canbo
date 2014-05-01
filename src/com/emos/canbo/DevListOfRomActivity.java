package com.emos.canbo;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.emos.canbo.R.string;
import com.emos.canbo.database.SmartDbHelper;
import com.emos.utils.OpParse;
import com.emos.utils.Quit;
import com.emos.utils.RequestByHttpPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class DevListOfRomActivity extends LinkedActivity {

	public static final String TAG ="DevListOfRomActivity";
	public static final int ITEM_1 = Menu.FIRST;
	public static final int ITEM_2 = Menu.FIRST + 1;
	
	/**
	 */
	private String connectionUrl = MyApp.instance.getConTotalStr();
	
	/**
	 */
	String r_name;
	/**
	 */
	int r_id;
	/**
	 */
	int house_id;
	
//	TextView txv_devList_top;
	/**
	 */
	ListView dev_listview;
	/**
	 */
	List<Device> dev_list_data;
	/**
	 */
	MyListAdatper mylistadatper;
	/**
	 */
	TextView txv_head_med;
	/**
	 */
	TextView txv_head_small;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		Log.v(TAG, "onCreate............................................");
		setContentView(R.layout.activity_devlist_of_rom);
		
//		txv_devList_top = (TextView)findViewById(R.id.txv_devlist_top);
		dev_listview = (ListView)findViewById(R.id.dev_listview);
		txv_head_med = (TextView)findViewById(R.id.txv_head_med);
		txv_head_small = (TextView)findViewById(R.id.txv_head_small);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		r_id = bundle.getInt("r_id");
		r_name = bundle.getString("r_name");
		house_id = 0;	
		//TODO 
		Log.d("debug", "0000");
//		txv_devList_top.setText(loc_name);
		txv_head_small.setText(r_name);
		txv_head_med.setText("选择设备");

		dataInitial();

		mylistadatper = new MyListAdatper(this);
		dev_listview.setAdapter(mylistadatper);
		dev_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Device mDevice = dev_list_data.get(position);

				Log.v(TAG, "mDevice.itemType == " + mDevice.itemType);
				if(mDevice.itemType == 1){
					Log.v(TAG, "mDevice.itemType == 1");
					return;
				}
				
				Intent intent = new Intent(getApplicationContext(), OptListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("dev_name", mDevice.d_name);
				bundle.putString("op_dev", mDevice.d_devtype);
				bundle.putString("d_mac", mDevice.d_mac);
				bundle.putInt("d_serialport", mDevice.d_serialport);
				bundle.putString("r_name", r_name);
				intent.putExtras(bundle);
				
				startActivity(intent);
			}
		});
		
	}

	private void dataInitial() {
		// TODO Auto-generated method stub
		/** setup the list **/
		dev_list_data = new ArrayList<Device>();
		
		getDevOfRoom();
	}

	private void getDevOfRoom() {
		// TODO Auto-generated method stub

		/** get device in room **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(this);
		if(db==null){
			Log.i("db", "db is null");
		}else{
			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Device WHERE r_id == ? AND `category` == 0",new String[]{Integer.toString(r_id)});

				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					Device mTempDevice = new Device();
					mTempDevice.r_id = r_id;
					mTempDevice.d_mac = c.getString(c.getColumnIndex("d_mac"));
					mTempDevice.d_devtype = c.getString(c.getColumnIndex("d_devtype"));
					mTempDevice.d_name = c.getString(c.getColumnIndex("d_name"));
					mTempDevice.d_serialport = c.getInt(c.getColumnIndex("d_serialport"));
					dev_list_data.add(mTempDevice);
				}
							
				c.close();
			} catch (Exception e) {
				// TODO: handle exception
			} finally{
				db.close();
			}
			Log.i("db", "size: " + dev_list_data.size());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
//		test for menu
//		menu.add(0, ITEM_1, 0, "新加1");
//		menu.add(0, ITEM_2, 0, "新加2");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case ITEM_1:
			actionClickMenuItem1();
			break;
		case ITEM_2:
			actionClickMenuItem2();
			break;
		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	private void actionClickMenuItem2() {
		// TODO Auto-generated method stub
		
	}

	private void actionClickMenuItem1() {
		// TODO Auto-generated method stub
		Quit.quit();
	}

	public void function_back(View view) {
		this.finish();
	}
	
	class MyListAdatper extends BaseAdapter{

		Context mContext;
		LayoutInflater inflater;
		
		public MyListAdatper(Context context) {
			// TODO Auto-generated constructor stub
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dev_list_data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return dev_list_data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			
			/**
			 * 0 - 单独一个
			 * 1 - top View
			 * 2 - bottom View
			 * 3 - mid View
			 *  
			 * **/
			
			if(position==0){
				if(getCount()==1){
					return 0;
				}else{
					return 1;
				}
			}else{
				if(position==(getCount()-1)){
					return 2;
				}else{
					return 3;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView txv;

			if(convertView==null){
//				Log.i("view", "convertView == null");
				//convertView is null, get new one.
				convertView = inflater.inflate(R.layout.list_dev_item, parent, false);
				
			}else{
//				Log.i("view", "convertView not null");
				//not null, use it.
			}
			
			/** set icon **/
			//get ImageView
			ImageView imgv_dev_item = (ImageView)convertView.findViewById(R.id.imgv_dev_item);
			ImageView imgv_jiantou = (ImageView)convertView.findViewById(R.id.imgv_jiantou);
			//get linearlayout
			LinearLayout device_line_btnOnOff = (LinearLayout)convertView.findViewById(R.id.device_line_btnOnOff);
			
			imgv_jiantou.setVisibility(View.VISIBLE);
			device_line_btnOnOff.setVisibility(View.GONE);
			
			//set image source
			String op_dev = dev_list_data.get(position).d_devtype;
			int op_dev_int = OpParse.hexStrToInt(op_dev);
//			Log.v(TAG, "op_dev:" + op_dev_int);
			switch (op_dev_int) {
			case 2: /** 油烟机    2, 0x02 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x02));
				break;
			
			case 3: /** 消毒柜    3, 0x03 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x03));
				break;

			case 5: /** 压力锅    5, 0x05 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x05));
				break;

			case 6: /** 电磁炉    6, 0x06 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x06));
				break;

			case 7: /** 热水器    7, 0x07 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x07));
				break;

			case 8: /** 冰箱    8, 0x08 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x08));
				break;

			case 11: /** 窗帘    11, 0x0B **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x0b));
				break;

			case 20: /** 饮水机    20, 0x14 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x14));
				break;

			case 36: /** 灶具    36, 0x24 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x24));
				break;

				/* 灯需要特殊处理 */
			case 64: /** 灯    64, 0x40 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x40));
				dev_list_data.get(position).itemType = 1;
				imgv_jiantou.setVisibility(View.GONE);
				device_line_btnOnOff.setVisibility(View.VISIBLE);
				//get Button
				Button device_btn_on = (Button)convertView.findViewById(R.id.device_btn_on);
				Button device_btn_off = (Button)convertView.findViewById(R.id.device_btn_off);
				device_btn_off.setTag(new BtnTag(0, position));
				device_btn_on.setTag(new BtnTag(1, position));
				/*set listener??? TODO */
				device_btn_off.setOnClickListener(onOffListener);
				device_btn_on.setOnClickListener(onOffListener);
				break;

			case 128: /** 排气扇    128, 0x80 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0x80));
				break;

			case 160: /** 中央空调    160, 0xA0 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0xa0));
				break;

			case 176: /** 电视    176, 0xB0 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0xb0));
				break;

			case 178: /** DVD    178, 0xB2 **/
				imgv_dev_item.setImageDrawable(getResources().getDrawable(R.drawable.dev_0xb2));
				break;
				
			default:
				break;
			}
			
			
			switch (getItemViewType(position)) {
			case 0:	/** only 1 **/
				convertView.setBackgroundResource(R.drawable.opt_item_button_selector);
				break;
			case 1:	/** Top View **/
				convertView.setBackgroundResource(R.drawable.opt_item_multbg_top_selector);
				break;
			case 2:	/** Bottom View **/
				convertView.setBackgroundResource(R.drawable.opt_item_multbg_bottom_selector);
				break;
			case 3:	/** Mid View **/
				//default is mid view background
				break;
			default:
				break;
			}
			
			
//			if(position==0){
//				if(getCount()==1){
//					convertView.setBackgroundResource(R.drawable.opt_item_button_selector);
//				}else{
//					convertView.setBackgroundResource(R.drawable.opt_item_multbg_top_selector);
//				}
//			}else if(position==(getCount()-1)){
//				convertView.setBackgroundResource(R.drawable.opt_item_multbg_bottom_selector);
//			}
			
			txv = (TextView)convertView.findViewById(R.id.txv_dev_name);
//			imgv = (ImageView)convertView.findViewById(R.id.imgv_dev_item);
			
			txv.setText(dev_list_data.get(position).d_name);
//			imgv.setImageResource(R.drawable.ic_launcher);
			
			return convertView;
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 */
	View.OnClickListener onOffListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BtnTag tag = (BtnTag)v.getTag();
			int op_num = tag.status;
			Device mDevice = dev_list_data.get(tag.position);
			final JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("1", OpParse.hexStrToInt(mDevice.d_mac));
				jsonObj.put("2", OpParse.hexStrToInt(mDevice.d_devtype));
				jsonObj.put("3", 47);
				jsonObj.put("4", op_num);
				jsonObj.put("5", mDevice.d_serialport);
				
				new Thread(){

					@Override
					public void run() {
						try {
							RequestByHttpPost.doPostJson(jsonObj, connectionUrl);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}.start();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	class BtnTag{
		public int status;
		public int position;
		public BtnTag(int status,int pos) {
			this.status = status;
			this.position = pos;
		}
	}
}
