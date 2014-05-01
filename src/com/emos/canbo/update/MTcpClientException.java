package com.emos.canbo.update;

import java.io.IOException;

public class MTcpClientException extends Exception {

	public static final int FILE_EXIST = 1;
	public static final int FILE_VERIFY_FAILED = 2;
	public static final int SOCKET_INPUT_ERR = 3;
	
	
	
	public int what = 0;

	public MTcpClientException(String msg) {
		super(msg);
	}
	
	public MTcpClientException(String msg, int what) {
		super(msg);
		this.what = what;
	}
	
	public int getWhat() {
		return what;
	}

	public void setWhat(int what) {
		this.what = what;
	}
}
