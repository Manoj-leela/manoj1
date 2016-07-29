package com.bizsyml.utill;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

/*  This Singleton class generates access token to be used with calling RESTFUL API's.
 * When access_token expires, new access_token is generated using the refresh token obtained along with access token.
 * When Refresh Token also expires, new access_token is generated using grant_type password data.
 * It uses java Timer for scheduling a thread to set the expiry of the access token and Refresh token.
 * 
 */
public class Token
{

    private static String access_token = null;
    private static String refresh_token = null;
    private volatile static Token INSTANCE = null;
    private static boolean isAccessExpired = true;
    private static boolean isRefreshExpired = true;
    private static int accessExpTime = 0;
    private static int refreshExpTime = 0;    
    private Timer refreshTimer = null; 
    private Timer accessTimer = null;

	private Token() {
    	
    }
    
	public static int getAccessExpTime() {
		return accessExpTime;
	}

	public static void setAccessExpTime(int accessExpTime) {
		Token.accessExpTime = accessExpTime;
	}

	public static int getRefreshExpTime() {
		return refreshExpTime;
	}

	public static void setRefreshExpTime(int refreshExpTime) {
		Token.refreshExpTime = refreshExpTime;
	}
	
    public static Token getInstance ()
    {
    	
    	if(INSTANCE == null)
    	{
    	  	  synchronized(Token.class)
    	  	  {
    	  		  if(INSTANCE == null){
    	  			INSTANCE = new Token();
    	  		  }
    	  	  }
    	    } 
    	return INSTANCE;
    }
    
      
    
/*
 * 
 */
	public synchronized String getAccess_token() throws Exception
	{
		if(access_token == null || isAccessExpired || isRefreshExpired )
		{
			access_token = generate();
			isAccessExpired = false ;
			isRefreshExpired = false ;
			scheduleAccessTimer();
			scheduleRefreshTimer();
										
		}		
		
		return access_token;
	}
	
	private static void setAccessExpired(boolean isAccessExpired) {
		Token.isAccessExpired = isAccessExpired;
	}

	private static void setRefreshExpired(boolean isRefreshExpired) {
		Token.isRefreshExpired = isRefreshExpired;
	}
	
	private static String getRefresh_token() {
		return refresh_token;
	}

	private static void setRefresh_token(String refresh_token) {
		Token.refresh_token = refresh_token;
	}
	
   private  String generate() throws Exception{
		
	
	
	if(isRefreshExpired && isAccessExpired) {
		
			
			/*System.out.println("Generating Token using grant_type password");
			TrustCertsUtil.disableSslVerification();
			byte[] postData       = RingCentralConstants.GET_TOKEN_PAYLOAD.getBytes( Charset.forName( "UTF-8" ));
			URL tokenURL = new URL(RingCentralConstants.GET_TOKEN_URL);
			System.out.println("Token URL is"+tokenURL);
			HttpURLConnection conn = (HttpURLConnection) tokenURL.openConnection();
			System.out.println("Connection Is"+conn);
			String appCredentials = RingCentralConstants.APP_KEY +":"+RingCentralConstants.APP_SECRET;
			System.out.println("AppCredential"+appCredentials);
			String basicAuth = "Basic " + Base64Encode.encode(appCredentials);
			System.out.println(basicAuth );
			conn.setRequestProperty ("Authorization", basicAuth);
			conn.setRequestProperty("extension","102");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoOutput(true);
			conn.getOutputStream().write(postData);
			conn.getOutputStream().flush();		
			
	 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb= new StringBuilder();
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				sb.append(output);
				
			}
			
			JsonObject jobj = new Gson().fromJson(sb.toString(), JsonObject.class);
			
			String accessToken = jobj.get("access_token").toString();
			String refreshToken = jobj.get("refresh_token").toString();
			int refreshExpTime = jobj.get("refresh_token_expires_in").getAsInt();
			int accessExpTime = jobj.get("expires_in").getAsInt();
			
			Token.setRefresh_token(refreshToken.substring(1, refreshToken.length()-1));
			Token.setAccessExpTime(accessExpTime);
			Token.setRefreshExpTime(refreshExpTime);
			
			System.out.println("accessToken "+accessToken);
			
			System.out.println("accessToken "+accessToken.substring(1, accessToken.length()-1));*/
			String apikey = "5C15396Df3e3d4897A2419e4343cf4363B3Febf780F90e01226d87A2419F4DD6";
			String apisecret = "2005E1EF9c8dBB7C7CE88526d9106A0391E4c0ff0099DA99C21Bd384cea64E5A";
			String username = "18556992400";
			String extension = "102";
			String password = "Syml@123";

			String grantType = "password";
			String url = "https://platform.ringcentral.com/restapi/oauth/token";
			String accessToken="";
			String keySec= "5C15396Df3e3d4897A2419e4343cf4363B3Febf780F90e01226d87A2419F4DD6"+":"+"2005E1EF9c8dBB7C7CE88526d9106A0391E4c0ff0099DA99C21Bd384cea64E5A";
			byte[] message = keySec.getBytes();
			String encoded = DatatypeConverter.printBase64Binary(message);
			System.out.println("encoded value is " + new String(encoded ));
			HttpsURLConnection httpConn = null;
			BufferedReader in = null;

			try {
				StringBuilder data = new StringBuilder();
				data.append("grant_type=" + URLEncoder.encode(grantType, "UTF-8"));
				data.append("&username=" + URLEncoder.encode(username, "UTF-8"));
				data.append("&extension=" + URLEncoder.encode(extension, "UTF-8"));
				data.append("&password=" + URLEncoder.encode(password, "UTF-8"));
			        

				byte[] byteArray = data.toString().getBytes("UTF-8");
				URL request = new URL(url);
				httpConn = (HttpsURLConnection) request.openConnection();
				httpConn.setRequestMethod("POST");
				httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
				System.out.println("Encoded Value is"+encoded);
				httpConn.setRequestProperty(
						"Authorization","Basic "+encoded);
				httpConn.setDoOutput(true);
				OutputStream postStream = httpConn.getOutputStream();
				postStream.write(byteArray, 0, byteArray.length);
				postStream.close();
				InputStreamReader reader = new InputStreamReader(
						httpConn.getInputStream());
				in = new BufferedReader(reader);
				StringBuffer content = new StringBuffer();
				String line;
				while ((line = in.readLine()) != null) {
					content.append(line + "\n");
				}
				in.close();

				String json = content.toString();
			System.out.println("Json String = " + json);
				Gson gson = new Gson();
				Type mapType = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> ser = gson.fromJson(json, mapType);
				accessToken = ser.get("access_token");

				System.out.println(ser.get("expires_in"));
				String refreshToken= ser.get("refresh_token");
				
			System.out.println("Access Token = " + accessToken);
				System.out.println("Refresh Token = " + refreshToken);
			
			
			return (accessToken.substring(1, accessToken.length()-1));
			
		}
		catch(Exception e){
			e.printStackTrace();	
			throw e ;
		}
		}
	else
	{

		try{
			System.out.println("Generating Token using RefreshToken");
			TrustCertsUtil.disableSslVerification();
			String datatoPost = "grant_type=refresh_token&refresh_token="+Token.getRefresh_token();
			byte[] postData       = datatoPost.getBytes( Charset.forName( "UTF-8" ));
			URL tokenURL = new URL(RingCentralConstants.GET_TOKEN_URL);
			HttpURLConnection conn = (HttpURLConnection) tokenURL.openConnection();
			String appCredentials = RingCentralConstants.APP_KEY + ":" + RingCentralConstants.APP_SECRET;
			String basicAuth = "Basic " + Base64Encode.encode(appCredentials);
			// System.out.println(basicAuth );
			conn.setRequestProperty ("Authorization", basicAuth);
			conn.setRequestProperty("extension","102");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoOutput(true);
			conn.getOutputStream().write(postData);
			conn.getOutputStream().flush();		
			
	 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb= new StringBuilder();
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				sb.append(output);
				
			}
			
			JsonObject jobj = new Gson().fromJson(sb.toString(), JsonObject.class);
			
			String accessToken = jobj.get("access_token").toString();
			String refreshToken = jobj.get("refresh_token").toString();
			int refreshExpTime = jobj.get("refresh_token_expires_in").getAsInt();
			int accessExpTime = jobj.get("expires_in").getAsInt();
			
			Token.setRefresh_token(refreshToken.substring(1, refreshToken.length()-1));
			Token.setAccessExpTime(accessExpTime);
			Token.setRefreshExpTime(refreshExpTime);
			
			
			return (accessToken.substring(1, accessToken.length()-1));
			
		}
		catch(Exception e){
			e.printStackTrace();	
			throw e ;
		}
		
	}
	}
	
   public static void main(String[] args) throws Exception {
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	Thread.sleep(3*60*1000);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	Thread.sleep(3*60*1000);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	Thread.sleep(1*60*1000);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   	System.out.println("Token" + Token.getInstance().getAccess_token() + " isAccessExpired: " + isAccessExpired + " isRefreshExpired: " + isRefreshExpired);
   }
   
   private void scheduleAccessTimer()
   {
	   System.out.println("Token.getAccessExpTime" + Token.getAccessExpTime());
	   if(accessTimer == null){
		   accessTimer  = new Timer(true);
	   }
	   accessTimer.schedule(new NewAccessToken(), Token.getAccessExpTime()*1000);
	   
   }
   
   private void scheduleRefreshTimer()
   {
	   System.out.println("Token.getRefreshExpTime" + Token.getRefreshExpTime());
	   if(refreshTimer == null)
	   {
		   refreshTimer = new Timer(true);
		   refreshTimer.schedule(new NewRefreshToken(),Token.getRefreshExpTime()*1000 );
	   }
	   else
	   {
		   refreshTimer.cancel();
		   refreshTimer = new Timer(true);
		   refreshTimer.schedule(new NewRefreshToken(),Token.getRefreshExpTime()*1000 );
	   }
	   
   }
   
   private class NewAccessToken extends TimerTask{

	@Override
	public void run() {
		System.out.println("Access Token Expired");
		Token.setAccessExpired(true);		
	}
	   
   }
   
   private class NewRefreshToken extends TimerTask{

		@Override
		public void run() {
			System.out.println("Refresh Token Expired");
			Token.setRefreshExpired(true);		
		}
		   
	   }
       
}


