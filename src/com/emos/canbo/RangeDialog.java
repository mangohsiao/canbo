package com.emos.canbo;

import java.lang.Character.UnicodeBlock;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.emos.utils.OpParse;
import com.emos.utils.RequestByHttpPost;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RangeDialog extends Dialog {
	
	final static String TAG = "RangeDialog";

	/**
	 */
	Context ctx;
	/**
	 */
	JSONObject jsObj;

	/**
	 */
	String d_mac;	
	/**
	 */
	String op_devtype;
	/**
	 */
	String op_code;
	/**
	 */
	String op_num;
	/**
	 */
	String d_serialport;
	/**
	 */
	String connectionUrl;	
	
	/**
	 */
	String op_unit;
	/**
	 */
	int valDefault;
	/**
	 */
	int valMin;
	/**
	 */
	int valMax;
	/**
	 */
	int valStep;

	/**
	 */
	Button rangedialog_btn_cancel;
	/**
	 */
	Button rangedialog_btn_ok;
	/**
	 */
	SeekBar rangedialog_seekbar;
	/**
	 */
	TextView rangedialog_seek_value_txv;
	/**
	 */
	TextView rangedialog_seek_unit_txv;
	
	public RangeDialog(Context context, Map<String, String> dataMap) {
		super(context);
		this.ctx = context;
		
		//initial val
		d_mac = dataMap.get("d_mac");
		op_devtype = dataMap.get("op_devtype");
		op_code = dataMap.get("op_code");
		op_num = dataMap.get("op_num");
		d_serialport = dataMap.get("d_serialport");
		connectionUrl = dataMap.get("connectionUrl");
		
		op_unit = OpParse.getUnitFromOpnum(op_num);
		Map<String, Integer> dmmStepMap = OpParse.defaultMinMaxStepToMap(op_num);
		valDefault = dmmStepMap.get("valDefault");
		valMin = dmmStepMap.get("valMin");
		valMax = dmmStepMap.get("valMax");
		valStep = dmmStepMap.get("valStep");
		
//		Log.v(TAG, " 1." + d_mac + " 2." + op_devtype + " 3." + op_code + " 4." + op_num + " 5." + d_serialport);
		//initial JSONObject
		jsObj = new JSONObject();
		try {
			jsObj.put("1", OpParse.hexStrToInt(d_mac));
			jsObj.put("2", OpParse.hexStrToInt(op_devtype));
			jsObj.put("3", OpParse.hexStrToInt(op_code));
			// 4 will be add onClick();
			jsObj.put("5", Integer.parseInt(d_serialport));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//set view
		setContentView(R.layout.layout_range_dialog);
		//find view
		rangedialog_btn_cancel = (Button)findViewById(R.id.rangedialog_btn_cancel);
		rangedialog_btn_ok = (Button)findViewById(R.id.rangedialog_btn_ok);
		rangedialog_seekbar = (SeekBar)findViewById(R.id.rangedialog_seekbar);
		rangedialog_seek_value_txv = (TextView)findViewById(R.id.rangedialog_seek_value_txv);
		rangedialog_seek_unit_txv = (TextView)findViewById(R.id.rangedialog_seek_unit_txv);
		
		//set seekbar range
		rangedialog_seekbar.setMax((valMax-valMin)/valStep);
		rangedialog_seekbar.setProgress((valDefault-valMin)/valStep);
		rangedialog_seek_value_txv.setText(Integer.toString(valDefault));	//show default
		rangedialog_seek_unit_txv.setText(op_unit);	//set unit
		//set listener
		rangedialog_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean user) {
				// TODO Auto-generated method stub
				rangedialog_seek_value_txv.setText(Integer.toString(progress*valStep + valMin));
			}
		});
		
		rangedialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RangeDialog.this.dismiss();
			}
		});
		
		rangedialog_btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/** put cmd op_num **/
				try {
					jsObj.put("4", rangedialog_seekbar.getProgress()*valStep + valMin);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/** send msg **/
				new Thread(){

					@Override
					public void run() {
						try {
							RequestByHttpPost.doPostJson(jsObj, connectionUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
				/** dismiss **/
				RangeDialog.this.dismiss();
			}
		});
	}

}
