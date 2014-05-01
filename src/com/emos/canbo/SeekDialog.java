package com.emos.canbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.emos.utils.MyQuery;
import com.emos.utils.OpParse;
import com.emos.utils.RequestByHttpPost;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekDialog extends Dialog {

	/**
	 */
	Context ctxSeekDialog;
	
	/**
	 */
	String op_dev;
	/**
	 */
	String d_mac;
	/**
	 */
	String op_id;
	/**
	 */
	String d_serialport;
	/**
	 */
	String connectionUrl;
	/**
	 */
	List<Map<Integer, Integer>> cmdList;
	/**
	 */
	JSONObject jsobj;
	
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
	String dfMinMax;
	/**
	 */
	Map<String, Integer> seekRange;
	
	/**
	 */
	Button seekdialog_btn_cancel;
	/**
	 */
	Button seekdialog_btn_ok;
	/**
	 */
	SeekBar seekdialog_seekbar;
	/**
	 */
	TextView seekdialog_seek_value_txv;
	
	/** dataMap ´«½øÀ´d_mac,op_dev,op_code **/
	public SeekDialog(Context context, Map<String, String> dataMap) {
		super(context);
		this.ctxSeekDialog = context;
		
		jsobj = new JSONObject();
		
		// TODO initial the d_mac,op_dev,
		op_dev = dataMap.get("op_dev");
		d_mac = dataMap.get("d_mac");
		op_id = dataMap.get("op_id");
		d_serialport = dataMap.get("d_serialport");
		connectionUrl = dataMap.get("connectionUrl");
				
		try {
			// cmd 1
//			String op_code_1 = MyQuery.getType2OpCodeMap(op_dev);
			String op_code_1 = "0x3F";			
			String op_num_1 = "0x01";
			jsobj.put("1", OpParse.hexStrToInt(d_mac));
			jsobj.put("2", OpParse.hexStrToInt(op_dev));
			jsobj.put("3", OpParse.hexStrToInt(op_code_1));
			jsobj.put("4", OpParse.hexStrToInt(op_num_1));
			jsobj.put("5", Integer.parseInt(d_serialport));
			// cmd 2
			jsobj.put("6", OpParse.hexStrToInt(d_mac));
			jsobj.put("7", OpParse.hexStrToInt(op_dev));
			jsobj.put("8", OpParse.hexStrToInt(dataMap.get("op_code")));
			jsobj.put("9", OpParse.hexStrToInt(dataMap.get("op_num")));
			jsobj.put("10", Integer.parseInt(d_serialport));
			// cmd 3
			Map<String, String> cmd3Map = MyQuery.getType2SubOpMap(ctxSeekDialog, op_dev, op_id);
			dfMinMax = cmd3Map.get("op_num");
			seekRange = OpParse.defaultMinMaxToMap(dfMinMax);
			jsobj.put("11", OpParse.hexStrToInt(d_mac));
			jsobj.put("12", OpParse.hexStrToInt(op_dev));
			jsobj.put("13", OpParse.hexStrToInt(cmd3Map.get("op_code")));
//			jsobj.put("14", null);	/** 14 is to add **/
			jsobj.put("15", Integer.parseInt(d_serialport));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		valDefault = seekRange.get("valDefault");
		valMin = seekRange.get("valMin");
		valMax = seekRange.get("valMax");
//		Map<Integer, Integer> cmd1 = new HashMap<Integer, Integer>();
//		String op_code_1 = MyQuery.getType2OpCodeMap(op_dev);
//		String op_num_1 = "0x01";
//		cmd1.put(1, OpParse.hexStrToInt(d_mac));
//		cmd1.put(2, OpParse.hexStrToInt(op_dev));
//		cmd1.put(3, OpParse.hexStrToInt(op_code_1));
//		cmd1.put(4, OpParse.hexStrToInt(op_num_1));
//		cmd1.put(5, Integer.parseInt(d_serialport));
//		cmdList.add(cmd1);
//		
//		// cmd 2
//		Map<Integer, Integer> cmd2 = new HashMap<Integer, Integer>();
//		cmd2.put(1, OpParse.hexStrToInt(d_mac));
//		cmd2.put(2, OpParse.hexStrToInt(op_dev));
//		cmd2.put(3, OpParse.hexStrToInt(dataMap.get("op_code")));
//		cmd2.put(4, OpParse.hexStrToInt(dataMap.get("op_num")));
//		cmd2.put(5, Integer.parseInt(d_serialport));
//		cmdList.add(cmd2);
//		
//		// cmd 3
//		Map<Integer, Integer> cmd3 = new HashMap<Integer, Integer>();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//set view
		setContentView(R.layout.layout_seek_dialog);
		
		//find view Button
		seekdialog_btn_cancel = (Button)findViewById(R.id.seekdialog_btn_cancel);
		seekdialog_btn_ok = (Button)findViewById(R.id.seekdialog_btn_ok);
		seekdialog_seekbar = (SeekBar)findViewById(R.id.seekdialog_seekbar);
		seekdialog_seek_value_txv = (TextView)findViewById(R.id.seekdialog_seek_value_txv);
		
		//set seekbar range
		seekdialog_seekbar.setMax(valMax-valMin);
		seekdialog_seekbar.setProgress(valDefault-valMin);
		seekdialog_seek_value_txv.setText(Integer.toString(valDefault));
		seekdialog_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				seekdialog_seek_value_txv.setText(Integer.toString(progress + valMin));
			}
		});
		//set seekbar default
		
		//set listener
		seekdialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SeekDialog.this.dismiss();
			}
		});
		
		seekdialog_btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/** put op_num_3 in jsobj **/
				try {
					jsobj.put("14", seekdialog_seekbar.getProgress() + valMin);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//send msg
				new Thread(){

					@Override
					public void run() {
						try {
							RequestByHttpPost.doPostJson(jsobj, connectionUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
				//dismiss the Dialog.
				SeekDialog.this.dismiss();
			}
		});
	}
	
	

}
