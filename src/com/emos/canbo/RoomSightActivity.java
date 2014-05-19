package com.emos.canbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.emos.canbo.database.SmartDbHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class RoomSightActivity extends ExActivity {

	/**
	 */
	int house_id = 0;
	/**
	 */
	List<Room> rooms_list;
	/**
	 */
	GribAdapter gribAdapter;
	/**
	 */
	GridView gridView;
	/**
	 */
	TextView txv_head_med;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		data_initial();
		setContentView(R.layout.activity_loc_sight);

		txv_head_med = (TextView)findViewById(R.id.txv_head_med);
		txv_head_med.setText("选择房间");
		
		gridView = (GridView)findViewById(R.id.gridView1);
		gribAdapter = new GribAdapter(this);
		gridView.setAdapter(gribAdapter);
		
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Room room = rooms_list.get(position);
				
				Intent intent = new Intent(RoomSightActivity.this, DevListOfRomActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("r_id", room.r_id);
				bundle.putString("r_name", room.r_name);
				intent.putExtras(bundle);
				
				startActivity(intent);
//				String name = tempMap.get("loc_name");
//				String id = tempMap.get("loc_id");
//				Log.i("location: ", "name="+ name + " id=" + id);
			}
		});
	}
	
	/** 需要异步!!!! **/
	void data_initial(){
		/** setup the list **/
		rooms_list = new ArrayList<Room>();
		
		/** get rooms in house **/
		getRoomOfHouse();
	}
	
	private void getRoomOfHouse() {
		// TODO Auto-generated method stub
		/** get location in house **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(RoomSightActivity.this);
		if(db==null){
			Log.i("db", "db is null");
//			Toast.makeText(RoomSightActivity.this, "打开数据库失败", Toast.LENGTH_SHORT).show();
		}else{
			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Room WHERE h_id == ?",new String[]{Integer.toString(house_id)});

				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					Room tempRoom = new Room();
					tempRoom.r_id = c.getInt(c.getColumnIndex("r_id"));
					tempRoom.r_name = c.getString(c.getColumnIndex("r_name"));
					rooms_list.add(tempRoom);
				}
							
				c.close();
			} catch (Exception e) {
				// TODO: handle exception
				
			} finally{
				db.close();
			}
		}
	}

	class GribAdapter extends BaseAdapter{

		Context mContext;
		LayoutInflater inflater;
		
		public GribAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mContext = context;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rooms_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return rooms_list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView txv;
			ImageView imgv;
			
			if(convertView==null){
				Log.i("view", "convertView == null");
				//convertView is null, get new one.
				convertView = inflater.inflate(R.layout.grid_item_house, parent, false);
			}else{
				Log.i("view", "convertView not null");
			}
			
			txv = (TextView)convertView.findViewById(R.id.house_item_name);
			imgv = (ImageView)convertView.findViewById(R.id.img_house_item);
			
			txv.setText(rooms_list.get(position).r_name);
			
			int r_id = rooms_list.get(position).r_id;
			
			switch (r_id) {
			case 1:	/** 主卧 **/
				imgv.setImageResource(R.drawable.room_main);
				break;
			case 2:	/** 客厅 **/
				imgv.setImageResource(R.drawable.room_living);
				break;
			case 3:	/** 书房 **/
				imgv.setImageResource(R.drawable.room_book);
				break;
			case 4:	/** 厨房 **/
				imgv.setImageResource(R.drawable.room_kitchen);
				break;
			case 5:	/** 次卧 **/
				imgv.setImageResource(R.drawable.room_bed);
				break;
			case 6:	/** 洗手间 **/
				imgv.setImageResource(R.drawable.room_washing);
				break;
			case 7:	/** 楼梯 **/
				imgv.setImageResource(R.drawable.room_step);
				break;
			default:
				imgv.setImageResource(R.drawable.ic_launcher);
				break;
			}
			
//			if(rooms_list.get(position).get("r_id").equals("主卧")){
//				Log.d("xiao", "主卧ing");
//				imgv.setImageResource(R.drawable.bed_deep);
//			}else{
//				Log.d("xiao", "not主卧");
//				imgv.setImageResource(R.drawable.ic_launcher);
//			}
			
			return convertView;
		}
		
	}
	
	
	public void function_back(View view) {
		this.finish();
	}
	
	class Room{
		public int r_id;
		public String r_name;
	}
}
