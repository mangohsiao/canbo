package com.emos.canbo.sync;

public class MDBSyncMessage {
	//local handler message
	public final static int WHAT_PROGRESS = 4410;
	public final static int WHAT_START_DOWNLOAD = 4411;
	public static final int WHAT_NETWORK_ERR = 4412;
	public final static int SYNC_OK = 1100;
	public final static int NO_NEED_SYNC = 1101;
	public final static int DOWNLOAD_FAILED = 1102;
	public final static int CONNECT_FAILED = 1103;
	public final static int PARSING_FAILED = 1104;
	public final static int VERIFY_FAILED = 1105;
	public static final int NETWORK_ERR = 1105;	

	public final static int REQ_TYPE_CHECK = 101;
	public static final int REQ_TYPE_GET_FILE = 911;
}
