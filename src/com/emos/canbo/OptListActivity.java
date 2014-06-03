package com.emos.canbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.emos.utils.*;
import com.emos.canbo.DevListOfRomActivity.MyListAdatper;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class OptListActivity extends LinkedActivity {

	final static String tag = "OptListActivity";
	final static int itemTypeCount = 6;	/**
	 * item的种类数
	 */
	String connectionUrl = "";
	
	/**
	 * 发送指令需要用到,传入的
	 */
	String d_mac;	//设备mac地址
	int r_id;	//room的id
	int d_serialport;	//device串口号
	
	String r_name;	//房间名字
	String op_devtype;	//dev类型代码
	
	List<Map<String,String>> opt_data_list;	//用于存储显示数据
	ListView lstv_optlist;
//	TextView txv_optlist;
	MyOptListAdatper myOptListAdatper;
	//head
	TextView txv_head_med;
	TextView txv_head_small;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// something onCreate 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optlist);
		
		//initial
		connectionUrl = ((MyApp)getApplicationContext()).getConTotalStr();
		Log.v(tag, "connectionUrl: " + connectionUrl);
		
		//get data.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		d_mac = bundle.getString("d_mac");
		d_serialport = bundle.getInt("d_serialport");
		if((op_devtype = bundle.getString("op_devtype"))!=null){
			//Log.d(tag, "get op_devtype.");
			dataInitial();
		}
		r_name = bundle.getString("r_name")==null?"未知房间":bundle.getString("r_name");
//		loc_name += " > ";
		
		//find VIEW
		lstv_optlist = (ListView)findViewById(R.id.lstv_optlist);
//		txv_optlist = (TextView)findViewById(R.id.txv_optlist);
		
		//head
		txv_head_med = (TextView)findViewById(R.id.txv_head_med);
		txv_head_small = (TextView)findViewById(R.id.txv_head_small);
		
		txv_head_small.setText(r_name);
		txv_head_med.setText(bundle.getString("dev_name")==null?"未知设备":bundle.getString("dev_name"));
		
//		txv_optlist.setText(loc_name + (bundle.getString("dev_name")==null?"未知设备":bundle.getString("dev_name")));
		
		myOptListAdatper = new MyOptListAdatper(getApplicationContext());
		lstv_optlist.setAdapter(myOptListAdatper);
		
		//set listener
		lstv_optlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// No need to do for the Item clicking
				
			}
		});
	}	
	
	private void dataInitial() {
		/** setup the list **/
		opt_data_list = new ArrayList<Map<String,String>>();
		
		getOptOfDevices();
		
		/** 处理数据包括子UI **/
		//TODO 
	}
	
	private void getOptOfDevices() {
		
		/** get location in house **/
		SQLiteDatabase db = SmartDbHelper.openSQLite(this);
		if(db==null){
			Log.i("db", "db is null");
		}else{
			Log.i("db", "db not null");
			try {
				Cursor c = db.rawQuery("SELECT * FROM Operation WHERE op_devtype=? AND op_parent='0' ORDER BY op_id;",new String[]{op_devtype});

				Log.i("db", "db query done");
				
				while(c.moveToNext()){
					Map<String,String> map = new HashMap<String, String>();
					map.put("op_id", c.getString(c.getColumnIndex("op_id")) );
					map.put("op_devtype", op_devtype);
					map.put("op_code", c.getString(c.getColumnIndex("op_code")));
					map.put("op_num", c.getString(c.getColumnIndex("op_num")));
					map.put("op_type", Integer.toString( c.getInt(c.getColumnIndex("op_type")) ));
					map.put("op_desc", c.getString(c.getColumnIndex("op_desc")));	
					opt_data_list.add(map);
				}
				
				c.close();
			} catch (Exception e) {
				// 
				e.printStackTrace();
			} finally{
				db.close();
			}
		}
	}

	class MyOptListAdatper extends android.widget.BaseAdapter{
		
		Context mContext;
		LayoutInflater inflater;
		
		public MyOptListAdatper(Context ctx) {
			// initial Adapter & get inflater
			mContext = ctx;
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return opt_data_list.size();
		}

		@Override
		public Object getItem(int position) {
			return opt_data_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getItemViewType(int position) {
			//
			/** 根据opt_type判断item类型 **/
			return Integer.parseInt(opt_data_list.get(position).get("op_type"))-1;
		}

		@Override
		public int getViewTypeCount() {
			/** 返回item类型count **/
			return itemTypeCount;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//
/** 旧版本 **/
//			TextView txv_opt_name;
//			TextView txv_opt_dev_code;
//			TextView txv_opt_code;
//			TextView txv_opt_num;
//			if(convertView==null){
//				convertView = inflater.inflate(R.layout.opt_item_two_lines, parent, false);				
//			}else{
//				Log.d("opt", "convertview not null");
//			}
//			txv_opt_name = (TextView)convertView.findViewById(R.id.txv_opt_name);
//			txv_opt_dev_code = (TextView)convertView.findViewById(R.id.txv_opt_dev_code);
//			txv_opt_code = (TextView)convertView.findViewById(R.id.txv_opt_code);
//			txv_opt_num = (TextView)convertView.findViewById(R.id.txv_opt_num);
//			Map<String, String> tempMap = opt_data_list.get(position);
//			txv_opt_name.setText(tempMap.get("op_desc"));
//			txv_opt_dev_code.setText(tempMap.get("op_devtype"));
//			txv_opt_code.setText(tempMap.get("op_code"));
//			txv_opt_num.setText(tempMap.get("op_num"));
/** 旧版本 **/
			int viewType = getItemViewType(position);
			
			if(convertView==null){
				Log.v(tag, "into cnvt view  == null:" + viewType);
				
				switch (viewType) {
				case -1:
					Log.v(tag, "op_type -1");
					convertView = inflater.inflate(R.layout.opt_type_show, parent, false);		
					break;
				case 0:	/** op_type=1 **/
					convertView = inflater.inflate(R.layout.opt_type_0, parent, false);						
					break;
				case 1:	/** op_type=2 **/
					convertView = inflater.inflate(R.layout.opt_type_1, parent, false);		
					break;
				case 2:	/** op_type=3 **/
					convertView = inflater.inflate(R.layout.opt_type_2, parent, false);		
					break;
				case 3:	/** op_type=4 **/
					convertView = inflater.inflate(R.layout.opt_type_3, parent, false);		
					break;
				case 4:	/** op_type=5 **/
					convertView = inflater.inflate(R.layout.opt_type_4, parent, false);		
					break;
				case 5:	/** op_type=6 **/
					convertView = inflater.inflate(R.layout.opt_type_5, parent, false);		
					break;
				default:
//					Log.v(tag, "viewType == " + viewType);
					convertView = inflater.inflate(R.layout.opt_type_0, parent, false);	
					break;
				}		
			}else{
//				Log.d(tag, "convertview not null");
			}
			
			Map<String, String> tempMap = opt_data_list.get(position);

//			TextView txv = (TextView)convertView.findViewById(R.id.txv_opt_final);
//			TextView txv1 = (TextView)convertView.findViewById(R.id.textView1);
//			txv1.setText(tempMap.get("op_desc"));

			final String op_desc = tempMap.get("op_desc");
			final String op_id = tempMap.get("op_id");
			final String op_code = tempMap.get("op_code");
			final List<String> op_code_List = OpParse.toList(op_code);	/** 解析op_code,存到list **/
			final String op_num = tempMap.get("op_num");
			final List<String> op_num_List = OpParse.toList(op_num);	/** 解析op_num,存到list **/
			
//			txv.setText("");
//			txv.append("op_devtype: " + op_devtype1 + "\n");
//			txv.append("d_mac: " + d_mac + "\n");
//			txv.append("d_serialport: " + d_serialport + "\n");
//			txv.append("op_desc: " + tempMap.get("op_desc") + "\n");
//			txv.append("op_code: " + op_code + "\n");
//			txv.append("op_num: " + op_num + "\n");
//			txv.append("op_type: " + tempMap.get("op_type") + "\n");
//			txv.append("viewType: " + viewType);

			switch (viewType) {
			case 0:	/** viewType 0 **/
				Button op_0_btn = (Button)convertView.findViewById(R.id.op_0_btn);
				op_0_btn.setText(tempMap.get("op_desc"));
				//set listener
				op_0_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// build json and send
						final JSONObject jobj0 = new JSONObject();
						try {
//							jobj0.put("1", d_mac);
//							jobj0.put("2", op_devtype);
							jobj0.put("1", OpParse.hexStrToInt(d_mac));
							jobj0.put("2", OpParse.hexStrToInt(op_devtype));
							jobj0.put("3", OpParse.hexStrToInt(op_code));
							jobj0.put("4", OpParse.hexStrToInt(op_num));
//							jobj0.put("3", op_code);
//							jobj0.put("4", op_num);
							jobj0.put("5", d_serialport);
						} catch (JSONException e1) {
							e1.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
//						send message.
						new Thread(){

							@Override
							public void run() {
								try {
//									RequestByHttpPost.doPostStr(cmd, connectionUrl);
									RequestByHttpPost.doPostJson(jobj0, connectionUrl);
//									RequestByHttpPost.doPostJsonWithToast(
//											OptListActivity.this,
//											jobj0, 
//											connectionUrl);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				});
				break;
				
			case 1:	/** op_type_1.xml, op_type='2' **/
				//light on/off
//				TextView op_1_txv1 = (TextView)convertView.findViewById(R.id.op_1_txv1);
//				op_1_txv1.setText(tempMap.get("op_desc"));
//				ImageView op_1_imgv = (ImageView)convertView.findViewById(R.id.op_1_imgv);
				ToggleButton op_1_btn = (ToggleButton)convertView.findViewById(R.id.op_1_btn);
				
				op_1_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(((ToggleButton)arg0).isChecked()){
							final JSONObject jobj = new JSONObject();
							try {
//								jobj.put("1", d_mac);
//								jobj.put("2", op_devtype);
								jobj.put("1", OpParse.hexStrToInt(d_mac));
								jobj.put("2", OpParse.hexStrToInt(op_devtype));
								jobj.put("3", OpParse.hexStrToInt(op_code_List.get(0)));
								jobj.put("4", OpParse.hexStrToInt(op_num_List.get(0)));
								jobj.put("5", d_serialport);
							} catch (JSONException e1) {
								e1.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							final String cmd = "{\"1\":" 
//									+ d_mac + ",\"2\":" 
//									+ op_devtype + ",\"3\":" 
//									+ "0x2F" + ",\"4\":" 
//									+ "0x01" + ",\"5\":" 
//									+ d_serialport + "}";
							new Thread(){

								@Override
								public void run() {
									try {
//										RequestByHttpPost.doPostStr(cmd, connectionUrl);
										RequestByHttpPost.doPostJson(jobj, connectionUrl);
//										RequestByHttpPost.doPostJsonWithToast(
//												OptListActivity.this, 
//												jobj, 
//												connectionUrl);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}.start();
						}else{
							final JSONObject jobj = new JSONObject();
							try {
//								jobj.put("1", d_mac);
//								jobj.put("2", op_devtype);
								jobj.put("1", OpParse.hexStrToInt(d_mac));
								jobj.put("2", OpParse.hexStrToInt(op_devtype));
								jobj.put("3", OpParse.hexStrToInt(op_code_List.get(0)));
								jobj.put("4", OpParse.hexStrToInt(op_num_List.get(1)));
								jobj.put("5", d_serialport);
							} catch (JSONException e1) {
								// 
								e1.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							final String cmd = "{\"1\":" 
//									+ d_mac + ",\"2\":" 
//									+ op_devtype + ",\"3\":" 
//									+ "0x2F" + ",\"4\":" 
//									+ "0x00" + ",\"5\":" 
//									+ d_serialport + "}";
							new Thread(){
								@Override
								public void run() {
									try {
//										RequestByHttpPost.doPostStr(cmd, connectionUrl);
										RequestByHttpPost.doPostJson(jobj, connectionUrl);
									} catch (Exception e) {
										// 
										e.printStackTrace();
									}
								}
							}.start(); /** end of new Thread **/
						}
					}/** end of method onClick **/
				});
				break;
				
			case 2:	/** op_type_2.xml, op_type='3' Time seek **/
				if(op_devtype.equals("0x07")){	//TODO 热水器还没好。
					break;
				}
				Button op_2_btn = (Button)convertView.findViewById(R.id.op_2_btn);
				op_2_btn.setText(op_desc);
				op_2_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("d_mac", d_mac);
						dataMap.put("op_devtype", op_devtype);
						dataMap.put("op_code", op_code);
						dataMap.put("op_num", op_num);
						dataMap.put("op_id", op_id);
						dataMap.put("d_serialport", Integer.toString(d_serialport));
						dataMap.put("connectionUrl", connectionUrl);
						SeekDialog mSeekDialog = new SeekDialog(OptListActivity.this, dataMap);
						mSeekDialog.setTitle(op_desc);
						mSeekDialog.show();
					} /** onClick() **/
					
				}); /** op_2_btn.setOnClickListener() **/		
				break;
			case 3:	/** op_type_3.xml, op_type='4' **/
				Button op_3_btn = (Button)convertView.findViewById(R.id.op_3_btn);
				op_3_btn.setText(tempMap.get("op_desc"));
				//analysis op_num
				final List<Map<String, String>> list = MyQuery.getSubUIMap(OptListActivity.this, op_devtype, tempMap.get("op_id"));				
				op_3_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// build and show Dialog
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("d_mac", d_mac);
						dataMap.put("op_devtype", op_devtype);
						dataMap.put("op_code", op_code);
						dataMap.put("d_serialport", Integer.toString(d_serialport));
						dataMap.put("connectionUrl", connectionUrl);
						RadioDialog mRadioDialog = new RadioDialog(OptListActivity.this, dataMap);
						mRadioDialog.setRadioList(list);
						mRadioDialog.setTitle(op_desc);
						mRadioDialog.show();
					}
				});
				break;	
			case 4:	/** op_type_4.xml, op_type='5' **/
				Button op_4_btn = (Button)convertView.findViewById(R.id.op_4_btn);
				op_4_btn.setText(tempMap.get("op_desc"));	
				op_4_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// build and show Dialog
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("d_mac", d_mac);
						dataMap.put("op_devtype", op_devtype);
						dataMap.put("op_code", op_code);
						dataMap.put("op_num", op_num);
						dataMap.put("d_serialport", Integer.toString(d_serialport));
						dataMap.put("connectionUrl", connectionUrl);
						RangeDialog mRangeDialog = new RangeDialog(OptListActivity.this, dataMap);
						mRangeDialog.setTitle(op_desc);
						mRangeDialog.show();
					}
				});
				break;
			case 5:	/** op_type_5.xml, op_type='6', appointment ? **/
				LinearLayout op_5_linear = (LinearLayout)convertView.findViewById(R.id.op_5_linear);
				op_5_linear.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AppointmentDialog01 mAppointmentDialog01 = new AppointmentDialog01(OptListActivity.this);
						mAppointmentDialog01.setTitle("预约功能");
						mAppointmentDialog01.show();
					}
				});
				break;
			default:
				Log.v(tag, "viewType == " + viewType);
			}
			
			return convertView;
		}
	}
	
	public void function_back(View view) {
		this.finish();
	}
}
