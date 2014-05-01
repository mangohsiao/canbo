package com.emos.canbo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Xiao.Min 2013-05-03
 *
 */
public class _DeviceOptViewActivity extends Activity {

	/**
	 */
	ListView listView;
	/**
	 */
	MyAdapter listAdapter;
	/**
	 */
	ArrayList<String> listString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dev_opt_list);
		
		listView = (ListView)findViewById(R.id.dev_opt_listView);
		
		//initial the string array
		listString = new ArrayList<String>();
		

		listString.add("灯");
		listString.add("电视机");
		listString.add("灯2");
		listString.add("贫道");
		listString.add("开关");
		
		Log.v("filter", "for finished");
		
		listAdapter = new MyAdapter(this);
		listView.setAdapter(listAdapter);

		Log.v("filter", "setAdatper finished");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	class MyAdapter extends BaseAdapter{

		Context mContext;
		
		LayoutInflater inflater;

		
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		final int TYPE_3 = 2;
		
		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listString.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listString.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			viewHolder1 holder1 = null;
			viewHolder2 holder2 = null;
			
			int type = getItemViewType(position);

			if(convertView==null){
				/** no convertView, new **/
				switch(type){
				case TYPE_1:
					convertView = inflater.inflate(R.layout.item_light, parent, false);
					holder1 = new viewHolder1();
					holder1.txv_light = (TextView)convertView.findViewById(R.id.txv_lighte);
					holder1.toggleButton = (ToggleButton)convertView.findViewById(R.id.toggleButton1);
					Log.v("convertView", "NULL   Type1");
					convertView.setTag(holder1);
					Log.v("convertView", "setTag holder1");
					break;
				case TYPE_2:
					convertView = inflater.inflate(R.layout.item_channel, parent, false);
					holder2 = new viewHolder2();
					holder2.txv_channel = (TextView)convertView.findViewById(R.id.txv_channel);
					Log.v("convertView", "NULL   Type2");
					convertView.setTag(holder2);
					Log.v("convertView", "setTag holder2");
					break;
				default:
					break;
				}
			}else{
				Log.e("tag", "convertView not null.");
				/** has convertView, changeView **/
				switch(type){
				case TYPE_1:
					holder1 = (viewHolder1)convertView.getTag();
					Log.v("convertView", "gettag() Type1");
					break;
				case TYPE_2:
					holder2 = (viewHolder2)convertView.getTag();
					Log.v("convertView", "gettag() Type2");
					break;
				default:
					break;
				}
			}

			Log.e("convertView", "second");
			switch(type){
			case TYPE_1:
				holder1.txv_light.setText(listString.get(position));
				if(position%4 == 0){
					holder1.toggleButton.setChecked(true);
				}else{
					holder1.toggleButton.setChecked(false);						
				}
				Log.e("convertView", "set value type1");
				break;
			case TYPE_2:
				holder2.txv_channel.setText(listString.get(position));
				Log.e("convertView", "gettag() Type2");
				break;
			default:
				break;
			}
			
			return convertView;
			
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
//			return super.getItemViewType(position);
			int p = position%2;
			if(p==0){
				return TYPE_1;
			}else{
				return TYPE_2;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		
	}
	
	class viewHolder1{
		TextView txv_light;
		ToggleButton toggleButton;
	}
	class viewHolder2{
		TextView txv_channel;
	}
}
