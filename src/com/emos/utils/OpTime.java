package com.emos.utils;

public class OpTime {
	/**
	 */
	private int minDefault;
	/**
	 */
	private int minMin;
	/**
	 */
	private int minMax;
	/**
	 */
	private int hourDefault;
	/**
	 */
	private int hourMin;
	/**
	 */
	private int hourMax;

	/**
	 */
	private String strMinDefault;
	/**
	 */
	private String strMinMin;
	/**
	 */
	private String strMinMax;
	/**
	 */
	private String strHourDefault;
	/**
	 */
	private String strHourMin;
	/**
	 */
	private String strHourMax;
	
	public OpTime(String opNum) throws ParseException {
		// TODO Auto-generated constructor stub
		parse(opNum);
	}

	private void parse(String opNum) throws ParseException {
		// TODO Auto-generated method stub
		String[] array = opNum.split("\\|");
		if(array.length!=6){
			throw new ParseException("");
		}

		strHourDefault = array[0];
		strHourMin = array[1];
		strHourMax = array[2]; 
		strMinDefault = array[3];
		strMinMin = array[4];
		strMinMax = array[5];
		
		//trans hex
		minDefault = Integer.parseInt(strMinDefault.split("0x")[1], 16);
		minMin = Integer.parseInt(strMinMin.split("0x")[1], 16);
		minMax = Integer.parseInt(strMinMax.split("0x")[1], 16);
		hourDefault = Integer.parseInt(strHourDefault.split("0x")[1], 16);
		hourMin = Integer.parseInt(strHourMin.split("0x")[1], 16);
		hourMax = Integer.parseInt(strHourMax.split("0x")[1], 16);
	}
	
	public class ParseException extends Exception{
		public ParseException(String msg) {
			super(msg);
		}
	}
}
