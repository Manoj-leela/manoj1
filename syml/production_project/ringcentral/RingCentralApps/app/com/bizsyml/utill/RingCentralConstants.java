package com.bizsyml.utill;

public class RingCentralConstants {

    public static final String REST_SERVER_URL ="https://platform.ringcentral.com";
	public static final String GET_TOKEN_URL =REST_SERVER_URL + "/restapi/oauth/token" ;
	public static final String RINGOUT_URL  =REST_SERVER_URL + "/restapi/v1.0/account/~/extension/~/ringout";
	public static final String CALL_LOG_URL  =REST_SERVER_URL + "/restapi/v1.0/account/~/extension/~/call-log";
	public static final String SMS_URL  =REST_SERVER_URL + "restapi/v1.0/account/~/extension/~/company-pager";
	public static final String APP_KEY ="5C15396Df3e3d4897A2419e4343cf4363B3Febf780F90e01226d87A2419F4DD6";
	public static final String APP_SECRET = "2005E1EF9c8dBB7C7CE88526d9106A0391E4c0ff0099DA99C21Bd384cea64E5A";
	public static final String SANDBOX_LOGIN = "18556992400";
	public static final String SANDBOX_PASSWORD = "Syml@123";
	
	public static final String GET_TOKEN_PAYLOAD = "grant_type=password&username="+SANDBOX_LOGIN+"&extension=&password="+SANDBOX_PASSWORD ;
	
}
