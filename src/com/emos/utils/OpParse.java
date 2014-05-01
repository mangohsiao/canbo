package com.emos.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emos.utils.OpNumType3.ParseException;

import android.util.Log;

public class OpParse {
	final static String TAG = "OpParse";
	
//	public static Map<Integer, String> toMap(String op) {
//		Map<Integer, String> map = new HashMap<Integer, String>();
//		String[] array = new String[10];
//		array = op.split("\\|");
//		return null;
//	}
	
	public static List<String> toList(String op) {
		if(op==null){
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] array = new String[10];
		if(array.length<1){
			return null;
		}
		array = op.split("\\|");
//		System.out.println("array.length:" + array.length);
		for (String string : array) {
			list.add(string);
		}
//		System.out.println("list.size:" + list.size());
		return list;
	}
	
	public static int hexStrToInt(String hexStr) {
		if(hexStr.split("0x").length>0){
			return Integer.parseInt(hexStr.split("0x")[1], 16);
		}else{
			return -1;
		}
	}
	
	public static Map<String, Integer> defaultMinMaxToMap(String op_num) {
		if(op_num==null){
			return null;
		}
		String[] array = op_num.split("\\|");
		if(array.length!=3){
			return null;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("valDefault", OpParse.hexStrToInt(array[0]));
		map.put("valMin", OpParse.hexStrToInt(array[1]));
		map.put("valMax", OpParse.hexStrToInt(array[2]));
		return map;
	}

	public static Map<String, Integer> defaultMinMaxStepToMap(String op_num) {
		if(op_num==null){
			return null;
		}
		Log.v(TAG, "op_num: ----" + op_num);
		String[] array = op_num.split("\\|");
		if(array.length!=8){
			return null;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("valDefault", OpParse.hexStrToInt(array[0]));
		map.put("valMin", OpParse.hexStrToInt(array[1]));
		map.put("valMax", OpParse.hexStrToInt(array[2]));
		map.put("valStep", Integer.parseInt(array[6]));
		return map;
	}
	
	public static String getUnitFromOpnum(String op_num) {
		if(op_num==null){
			return null;
		}
		String[] array = op_num.split("\\|");
		if(array.length!=8){
			return null;
		}
		return array[7];
	}
}
