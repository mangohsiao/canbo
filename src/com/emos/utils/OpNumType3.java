package com.emos.utils;

import com.emos.utils.OpTime.ParseException;

public class OpNumType3 {
	/**
	 */
	private int valDefault;
	/**
	 */
	private int valMin;
	/**
	 */
	private int valMax;
	
	public OpNumType3(String op_num) throws ParseException {
		// TODO Auto-generated constructor stub
		parse(op_num);
	}
	
	private void parse(String op_num) throws ParseException {
		String[] array = op_num.split("\\|");
		if(array.length!=3){
			throw new ParseException("Parsing syntax error");
		}
		valDefault = OpParse.hexStrToInt(array[0]);
		valMin = OpParse.hexStrToInt(array[1]);
		valMax = OpParse.hexStrToInt(array[2]);
	}
	
	/**
	 * @return
	 */
	public int getValDefault() {
		return valDefault;
	}
	/**
	 * @return
	 */
	public int getValMin() {
		return valMin;
	}
	/**
	 * @return
	 */
	public int getValMax() {
		return valMax;
	}
	
	public class ParseException extends Exception{
		public ParseException(String msg) {
			super(msg);
		}
	}
}
