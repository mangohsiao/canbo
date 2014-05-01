package com.emos.canbo;

public class CanboCommon {
	public static final String GlobalSettingsName = "global_settings";
	
	/* Security */
//	public static final String SecuritySettingsName = "security_settings";
	public static final String SECURITYNOTIRINGNAME= "security_noti_ring";
	public static final String SETTING_SECURITY_REFRESH_TIME= "security_refresh_time";
	public static final int SECURITY_REFRESH_TIME= 5000;
	
	/* connection initial constant */
	//settings name
	public static final String CONTROL_CONNECT_IP_NAME = "control_connect_ip";
	public static final String CONTROL_CONNECT_PORT_NAME = "control_connect_port";
	public static final String CONTROL_SERVER_PATH_NAME = "control_server_path";
	public static final String CONTROL_CONNECT_URL_NAME = "control_connect_url";
	
	//settings value
	public static final String CONTROL_CONNECT_IP = "125.216.243.235";
	public static final String CONTROL_CONNECT_PORT = "8189";
	public static final String CONTROL_SERVER_PATH = "/huabo";
	public static final String CONTROL_CONNECT_URL = CONTROL_SERVER_PATH + "/request";
	//status url
	
	public static final String STATUS_SERVLET_URL_NAME = "status_servlet_url";
	public static final String STATUS_SERVLET_URL = "/picker";
	public static final String STATUS_WHOLE_URL = "http://" 
												+ CONTROL_CONNECT_IP 
												+ ":" + CONTROL_CONNECT_PORT 
												+ "/huabo/request";

	/* login */
//	public static final String Server_IP = "125.216.243.235";
	public static final int LOGIN_MODE_WAN = 1;
	public static final int LOGIN_MODE_LAN = 0;
	public static final String SHPF_WAN_CONNECT_IP = "WAN_CONNECT_IP";
	public static final String SHPF_WAN_CONNECT_PORT = "WAN_CONNECT_PORT";
	
	/* DB Sync */
	public static final String DB_SYNC_SERVER_URL = "/dbcheck";
}
