package com.emos.canbo.monitor;

import java.util.ArrayList;
import java.util.List;

import com.emos.canbo.R;
import com.emos.canbo.monitor.DialogPresetGoto.NodeData;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PresetDeleteActivity extends Activity {

	private final static String sql = "SELECT * FROM vision_node_table WHERE cam_id=? ORDER BY node_index;";
	
	private final static int MSG_DEL_YES = 101;
	
	/**
	 */
	int cam_id = -1;
	/**
	 */
	ListView listview_preset_del = null;
	/**
	 */
	List<NodeData> data_list = null;
	/**
	 */
	MyAdapter mAdapter = null;
	
	/**
	 */
	CamDb camDb = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//set view
		setContentView(R.layout.activity_preset_delete);
		
		initial();
		
		listview_preset_del = (ListView)findViewById(R.id.listview_preset_del);
		mAdapter = new MyAdapter(PresetDeleteActivity.this);
		listview_preset_del.setAdapter(mAdapter);
		
	}

	private void initial() {
		// TODO Auto-generated method stub
		Bundle bd = getIntent().getExtras();
		cam_id = bd.getInt("cam_id");
		
		camDb = new CamDb(PresetDeleteActivity.this);
		
		data_filling();
	}

	private boolean data_filling() {
		if(data_list == null){
			data_list = new ArrayList<PresetDeleteActivity.NodeData>();
		}else if(data_list.size()>0){
			data_list.clear();
		}
		
		//read from db
		CamDb camDb = new CamDb(this);
		SQLiteDatabase reader = camDb.getReadableDatabase();
//		mLog("before try.");
		try {
			Cursor c = reader.rawQuery(sql, new String[]{Integer.toString(cam_id)});
//			mLog("count:" + c.getCount());
//			mLog("before while.");
			while (c.moveToNext()) {
				NodeData temp = new NodeData();
				temp.node_id = c.getInt(c.getColumnIndex("node_id"));
				temp.node_index = c.getInt(c.getColumnIndex("node_index"));
				temp.node_name = c.getString(c.getColumnIndex("node_name"));
				
//				mLog(" node_id:" + temp.node_id + " node_name:" + temp.node_name + " node_value:" + temp.node_index);
				/* add to list */
				data_list.add(temp);
			}
		} catch (Exception e) {
			mLog(e.getMessage());
			reader.close();
			return false;
		}
		reader.close();
		return true;
	}

	private void mLog(String msg) {
		// TODO Auto-generated method stub
		Log.v(PresetDeleteActivity.class.getSimpleName(), msg);
	}

	class MyAdapter extends BaseAdapter{

		Context mCtx;
		LayoutInflater mInflater;
		
		public MyAdapter(Context ctx) {
			// TODO Auto-generated constructor stub
			mCtx = ctx;
			mInflater = LayoutInflater.from(mCtx);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data_list.size();
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return data_list.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertview == null){
				convertview = mInflater.inflate(R.layout.preset_delete_item, parent, false);
			}
			TextView preset_del_txv_index = (TextView)convertview.findViewById(R.id.preset_del_txv_index);
			TextView preset_del_txv_name = (TextView)convertview.findViewById(R.id.preset_del_txv_name);
			Button preset_del_btn_del = (Button)convertview.findViewById(R.id.preset_del_btn_del);
//			LinearLayout preset_del_line_del = (LinearLayout)convertview.findViewById(R.id.preset_del_line_del);
			
			NodeData current_node = data_list.get(position);
			
			preset_del_txv_index.setText(Integer.toString(current_node.node_index));
			preset_del_txv_name.setText(current_node.node_name);
//			preset_del_line_del.setOnClickListener(new Mylistener(current_node.node_id));
			preset_del_btn_del.setTag(current_node.node_id);
			preset_del_btn_del.setOnClickListener(del_listener);
			
			return convertview;
		}
		
	}
	
	/**
	 */
	private Mylistener del_listener = new Mylistener();
	
	class Mylistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int node_id = (Integer) v.getTag();
			//delete position
//			Toast.makeText(PresetDeleteActivity.this, "删除:" + node_id, Toast.LENGTH_SHORT).show();
			
			/* show delete dialog */
			Bundle bd = new Bundle();
			bd.putInt("node_id", node_id);
			DialogMsgBack dialogback = new DialogMsgBack(PresetDeleteActivity.this, handler, MSG_DEL_YES, bd);
			dialogback.setTitle("确认删除 预置点?");
			dialogback.show();
		}
	}
	
	/**
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int node_id = -1;
			Bundle bd = msg.getData();
			int msg_val = bd.getInt(DialogMsgBack.MSG_NAME);
			node_id = bd.getInt("node_id");
			switch (msg_val) {
			case MSG_DEL_YES:
				if(node_id >= 0){
					int rtvl = delete_node(node_id);
					mLog("delete rows count : " + rtvl);
					/* 改变数据 */
					reload_data();
					mAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	private int delete_node(int node_id) {
		try {
			SQLiteDatabase write = camDb.getWritableDatabase();
			return write.delete(CamDb.VISION_NODE_TABLE, "cam_id=? AND node_id=?", new String[]{Integer.toString(cam_id),Integer.toString(node_id)});
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	
	protected void reload_data() {
		// TODO Auto-generated method stub
		data_filling();
	}

	class NodeData{
		public String node_name;
		public int node_id;
		public int node_index;
	}
}
