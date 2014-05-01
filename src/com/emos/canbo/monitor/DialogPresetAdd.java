package com.emos.canbo.monitor;

import com.emos.canbo.R;
import com.hikvision.netsdk.PTZPresetCmd;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogPresetAdd extends Dialog implements android.view.View.OnClickListener{

	private final static String select_sql = "Select node_id FROM vision_node_table WHERE cam_id=? AND node_index=?;";
	private final static String insert_sql = "INSERT INTO vision_node_table(cam_id,node_index,node_name) VALUES(?,?,?);";
	private final static String update_sql = "UPDATE vision_node_table SET node_name=? WHERE cam_id=? AND node_index=?;";
	
	/**
	 */
	private IPresetListener presetListener = null;
	
	/**
	 */
	Context mContext;
	/**
	 */
	int cam_id;
	/**
	 */
	int node_index = -1;
	/**
	 */
	String node_name = "Î´ÃüÃû";
	/**
	 */
	EditText edtx_cam_preset_node_name = null;
	/**
	 */
	EditText edtx_cam_preset_node_index = null;
	/**
	 */
	CheckBox presetadd_chbx_set_now = null;
	
	
	/**
	 */
	LinearLayout linearlayout_preset_add_dialog_yes = null;
	/**
	 */
	LinearLayout linearlayout_preset_add_dialog_no = null;
	
	public DialogPresetAdd(Context context, int cam_id) {
		super(context);
		this.mContext = context;
		this.cam_id = cam_id;
		setContentView(R.layout.dialog_cam_preset_add);

		edtx_cam_preset_node_name = (EditText)findViewById(R.id.edtx_cam_preset_node_name);
		edtx_cam_preset_node_index = (EditText)findViewById(R.id.edtx_cam_preset_node_index);
		linearlayout_preset_add_dialog_yes = (LinearLayout)findViewById(R.id.linearlayout_preset_add_dialog_yes);
		linearlayout_preset_add_dialog_no = (LinearLayout)findViewById(R.id.linearlayout_preset_add_dialog_no);
		presetadd_chbx_set_now = (CheckBox)findViewById(R.id.presetadd_chbx_set_now);
		
		linearlayout_preset_add_dialog_yes.setOnClickListener(this);
		linearlayout_preset_add_dialog_no.setOnClickListener(this);
	}

	/**
	 * @param presetListener
	 */
	public void setPresetListener(IPresetListener presetListener) {
		this.presetListener = presetListener;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearlayout_preset_add_dialog_yes:
			//get info
			try {
				
				if(!edtx_cam_preset_node_name.getText().toString().equals("")){
					node_name = edtx_cam_preset_node_name.getText().toString();
				}
				if(!edtx_cam_preset_node_index.getText().toString().equals("")){
					node_index = Integer.parseInt(edtx_cam_preset_node_index.getText().toString());
				}
				Log.v("DIALOG", "add --- cam_id=" + cam_id + " node_index=" + node_index + " node_name=" + node_name);
				
				CamDb camDb = new CamDb(mContext);
				boolean exist = false;
				//check db
				SQLiteDatabase reader = camDb.getReadableDatabase();
				Cursor c = reader.rawQuery(select_sql, new String[]{Integer.toString(cam_id), Integer.toString(node_index)});
				
				if(c.getCount()>0){
					exist = true;
					Log.v("DIALOG", "exits = true.");
				}else{
					Log.v("DIALOG", "exits = false.");
				}
				//add to db
				SQLiteDatabase writer = camDb.getWritableDatabase();
				if(exist){
					writer.execSQL(
							update_sql, 
							new String[]{node_name, Integer.toString(cam_id), Integer.toString(node_index)});
				}else {
					writer.execSQL(
					insert_sql, 
					new String[]{Integer.toString(cam_id), Integer.toString(node_index), node_name});
//					ContentValues val = new ContentValues();
//					val.put("cam_id", cam_id);
//					val.put("node_index", node_index);
//					val.put("node_name", node_name);
//					if(writer.insert("vision_node_table", null, val)<0){
//						Log.v("DIALOG", "insert failed.");
//					}
				}
				reader.close();
				writer.close();
				
				//callback - set preset node on camera
				if(presetadd_chbx_set_now.isChecked()){
					presetListener.preset_goto(PTZPresetCmd.SET_PRESET, node_index);
				}

			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			
			//dismiss
			DialogPresetAdd.this.dismiss();
			break;
			
		case R.id.linearlayout_preset_add_dialog_no:
			DialogPresetAdd.this.dismiss();
			break;

		default:
			break;
		}
	}

}
