package com.emos.canbo.monitor;

import com.emos.canbo.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.LinearLayout;

public class DialogPresetManage extends Dialog {

	/**
	 */
	int cam_id;
	/**
	 */
	private Context mContext = null;
	/**
	 */
	IPresetListener presetListener = null;
	
	/**
	 */
	LinearLayout linearlayout_preset_add = null;
	/**
	 */
	LinearLayout linearlayout_preset_delete = null;
	
	public DialogPresetManage(Context context, int cam_id, IPresetListener presetListener) {
		super(context);
		this.mContext = context;
		this.cam_id = cam_id;
		this.presetListener = presetListener;
		setContentView(R.layout.dialog_cam_preset_manage);
		
		initial_view();
	}

	private void initial_view() {
		// TODO Auto-generated method stub
		//find view
		linearlayout_preset_add = (LinearLayout)findViewById(R.id.linearlayout_preset_add);
		linearlayout_preset_delete = (LinearLayout)findViewById(R.id.linearlayout_preset_delete);
		
		//set listener
		linearlayout_preset_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogPresetAdd mPresetAdd = new DialogPresetAdd(mContext, cam_id);
				mPresetAdd.setPresetListener(presetListener);
				mPresetAdd.show();
				DialogPresetManage.this.dismiss();
			}
		});
		
		linearlayout_preset_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PresetDeleteActivity.class);
				Bundle bd = new Bundle();
				bd.putInt("cam_id", cam_id);
				intent.putExtras(bd);
				mContext.startActivity(intent);
				DialogPresetManage.this.dismiss();
			}
		});
	}

}
