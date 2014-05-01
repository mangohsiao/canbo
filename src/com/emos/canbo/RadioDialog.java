package com.emos.canbo;

import java.util.List;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioDialog extends Dialog {
	final static String TAG = "RadioDialog";
	
	/**
	 */
	Context ctx;
	/**
	 */
	RadioGroup rdGroup;
	/**
	 */
	List<Map<String,String>> radioList;
	/**
	 */
	Button rdialog_btn_ok;
	/**
	 */
	Button rdialog_btn_cancel;
	
	/**
	 */
	String d_mac;
	/**
	 */
	String op_dev;
	/**
	 */
	String op_code;
	/**
	 */
	String d_serialport;
	/**
	 */
	String connectionUrl;

//	public RadioDialog(Context context) {
//		super(context);
//		this.ctx = context;
//	}
	
	/** dataMap ´«½øÀ´d_mac,op_dev,op_code **/
	public RadioDialog(Context context, Map<String, String> dataMap) {
		super(context);
		this.ctx = context;
		d_mac = dataMap.get("d_mac");
		op_dev = dataMap.get("op_dev");
//		op_code = dataMap.get("op_code"); /** op_code is NULL in DB **/
		d_serialport = dataMap.get("d_serialport");
		connectionUrl = dataMap.get("connectionUrl");
	}

	public void setRadioList(List<Map<String,String>> inList) {
		this.radioList = inList;
	}
	
	private void setRadioBtn() {
//		Log.v(TAG, "SET RADIO. " + list.size());
		int index = 0;
		for (Map<String, String> map : radioList) {
//			Log.v(TAG, "map size" + map.size());
			RadioButton rdBtn = new RadioButton(ctx);
			rdBtn.setId(index);
//			Log.v(TAG, Integer.toString(rdBtn.getId()));
//			rdBtn.setTag(map);
			String op_desc = map.get("op_desc");
			if(op_desc!=null){
				rdBtn.setText(op_desc);
			}else{
				rdBtn.setText("NULL");
			}
			rdGroup.addView(rdBtn,index++);
		}
//		Log.v(TAG, "SET RADIO. child count: " + rdGroup.getChildCount());
		if(rdGroup.getChildCount()>0){
			((RadioButton)rdGroup.getChildAt(rdGroup.getChildCount()-1)).setChecked(true);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "on RadioDialog Create");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_radio_dialog);
		rdGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		setRadioBtn();

		rdialog_btn_ok = (Button)findViewById(R.id.rdialog_btn_ok);
		rdialog_btn_cancel = (Button)findViewById(R.id.rdialog_btn_cancel);

		rdialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Cancel the dialog.
				RadioDialog.this.dismiss();
			}
		});
		
		rdialog_btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Send cmd
				int checkId = rdGroup.getCheckedRadioButtonId();
				String op_num = radioList.get(checkId).get("op_num");
				op_code = radioList.get(checkId).get("op_code");
				Log.v(TAG, " 1." + d_mac + " 2." + op_dev + " 3." + op_code + " 4." + op_num);
				//build jsobj and send
				final JSONObject jobj0 = new JSONObject();
				try {
					jobj0.put("1", OpParse.hexStrToInt(d_mac));
					jobj0.put("2", OpParse.hexStrToInt(op_dev));
					jobj0.put("3", OpParse.hexStrToInt(op_code));
					jobj0.put("4", OpParse.hexStrToInt(op_num));
					jobj0.put("5", Integer.parseInt(d_serialport));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				send message.
				new Thread(){

					@Override
					public void run() {
						try {
							RequestByHttpPost.doPostJson(jobj0, connectionUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
				//dismiss the Dialog.
				RadioDialog.this.dismiss();
			}/** end of onClick() **/
			
		});/** end of rdialog_btn_ok.setOnClickListener() **/
	}/** onCreate() **/
}
