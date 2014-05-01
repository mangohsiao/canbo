package com.emos.canbo.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emos.canbo.R;
import com.hikvision.netsdk.PTZPresetCmd;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

public class DialogPresetGoto extends Dialog implements OnClickListener,OnItemClickListener{

	private final static String TAG = "DialogPresetGoto";
	private final static String sql = "SELECT * FROM vision_node_table WHERE cam_id=? ORDER BY node_index;";
	
	/**
	 */
	Context mContext = null;
	/**
	 */
	int cam_id = -1;
	/**
	 */
	int currentPosition = -1;
	/**
	 */
	IPresetListener presetListener = null;
	
	/**
	 */
	List<NodeData> data_list = null;
	/**
	 */
	MyAdapter myAdapter = null;
	
	/**
	 */
	ListView listview_preset_goto = null;
	/**
	 */
	LinearLayout linearlayout_preset_goto_yes = null;
	/**
	 */
	LinearLayout linearlayout_preset_goto_no = null;
	
	public DialogPresetGoto(Context context, int cam_id, IPresetListener presetListener) {
		super(context);
		setContentView(R.layout.dialog_cam_preset_goto);
		
		this.mContext = context;
		this.cam_id = cam_id;
		this.presetListener = presetListener;
		// TODO Auto-generated constructor stub
		
		// view initial
		initial_view();
		
		//data initial
		initial_data();
		
		//test data
//		NodeData test = new NodeData();
//		test.node_id = 3;
//		test.node_name = "mango3";
//		test.node_index = 1;
//		data_list.add(test);
		
		listview_preset_goto.setAdapter(myAdapter);
	}

	private boolean initial_data() {
		// TODO Auto-generated method stub

		data_list = new ArrayList<DialogPresetGoto.NodeData>();
		
		if(cam_id<0){
			return false;
		}
		
		//read from db
		CamDb camDb = new CamDb(mContext);
		SQLiteDatabase reader = camDb.getReadableDatabase();
		mLog("before try.");
		try {
			Cursor c = reader.rawQuery(sql, new String[]{Integer.toString(cam_id)});
			mLog("count:" + c.getCount());
			mLog("before while.");
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
		Log.v(TAG, msg);
	}

	private void initial_view() {
		// TODO Auto-generated method stub
		//find view
		listview_preset_goto = (ListView)findViewById(R.id.listview_preset_goto);
		linearlayout_preset_goto_yes = (LinearLayout)findViewById(R.id.linearlayout_preset_goto_yes);
		linearlayout_preset_goto_no = (LinearLayout)findViewById(R.id.linearlayout_preset_goto_no);
		//set listener
		linearlayout_preset_goto_yes.setOnClickListener(this);
		linearlayout_preset_goto_no.setOnClickListener(this);
		listview_preset_goto.setOnItemClickListener(this);
		//adatper
		myAdapter = new MyAdapter(mContext);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearlayout_preset_goto_yes:
			if(currentPosition != -1){
				presetListener.preset_goto(PTZPresetCmd.GOTO_PRESET, data_list.get(currentPosition).node_index);
			}
			DialogPresetGoto.this.dismiss();
			break;
		case R.id.linearlayout_preset_goto_no:
			DialogPresetGoto.this.dismiss();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		currentPosition = position;
		myAdapter.notifyDataSetChanged();
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
				convertview = mInflater.inflate(R.layout.item_preset_goto, parent, false);
			}
			
			RadioButton radioButton = (RadioButton)convertview.findViewById(R.id.radioButton_preset_goto_item);
			radioButton.setText(data_list.get(position).node_index + " - " + data_list.get(position).node_name);
			
			if(position == currentPosition){
				radioButton.setChecked(true);
			}else {
				radioButton.setChecked(false);
			}
			
			return convertview;
		}
		
	}
	
	class NodeData{
		public String node_name;
		public int node_id;
		public int node_index;
	}
}
