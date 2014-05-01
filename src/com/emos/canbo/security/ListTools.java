package com.emos.canbo.security;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.emos.canbo.Device;

public class ListTools {
	/**
	 * @param fromList  List源
	 * @param checks	checkArray
	 * @param len		checkArray的长度
	 * @param toList	目的List
	 * @param usedPref	设置存储
	 * @param isAdd		是添加还是删除设置
	 * @return
	 */
	public static int moveListItem(List<Device> fromList, boolean checks[], int len, List<Device> toList, SharedPreferences usedPref, boolean isAdd){
		Editor editor = usedPref.edit();
		try {
			Device device = null;
			for (int i = 0; i < len; i++) {
				if(checks[i]){
					device = fromList.get(i);
					toList.add(device);
					if(isAdd){
						editor.putBoolean(device.d_mac+device.d_devtype+device.d_no, true);
					}else {
						editor.remove(device.d_mac+device.d_devtype+device.d_no);
					}
				}
			}
			for (int i = 0, j = 0; i < len; i++, j++) {
				if(checks[i]){
					fromList.remove(j--);
				}
			}
			//commit SharedPreferences
			editor.commit();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
