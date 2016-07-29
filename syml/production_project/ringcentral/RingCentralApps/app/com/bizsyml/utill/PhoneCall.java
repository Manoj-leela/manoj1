package com.bizsyml.utill;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


import com.bizsyml.infrastructure.CouchBaseOperation;
import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;

/*
 *  This class has methods to check status of the phone call and to get duration of last call made
 */

public class PhoneCall {
	
	public static RingStatusObject checkStatus(RingStatusObject ringstatusd) throws Exception{
		JSONObject object=null;

		if(ringstatusd != null){
			
			try{
				
				TrustCertsUtil.disableSslVerification();
				String  accessToken= Token.getInstance().getAccess_token();
				URL ringOutURL = new URL(ringstatusd.getUri());
				HttpURLConnection conn = (HttpURLConnection) ringOutURL.openConnection();
				String bearerAuth = "bearer   " + accessToken;
				//System.out.println(bearerAuth );
				conn.setRequestProperty ("Authorization", bearerAuth);
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);			
											
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				StringBuilder sb= new StringBuilder();
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					sb.append(output);
					
				}
				
	            RingStatusObject ringstatus = new Gson().fromJson(sb.toString(), RingStatusObject.class);
	            
				System.out.println(ringstatus.getId());
				System.out.println(ringstatus.getUri());
				System.out.println(ringstatus.getStatus().get("callStatus"));
				System.out.println(ringstatus.getStatus().get("callerStatus"));
				System.out.println(ringstatus.getStatus().get("calleeStatus"));	
				
				return ringstatus;
				
				
			}
			catch(Exception e){
				throw e;
			}
			
			
			
		}
			
		return null;

}
	
	public static Double getDuration() throws Exception{
		JSONObject jsonObject=null;
		CouchBaseOperation couchbase = new CouchBaseOperation();
		CouchbaseClient client = null;


		try{
			
			TrustCertsUtil.disableSslVerification();
			//String  accessToken= Token.getInstance().getAccess_token();
			URL ringOutURL = new URL(RingCentralConstants.CALL_LOG_URL);
			
			
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
			
			
			
			
			
			
			}catch (Exception e) {
e.printStackTrace();			}
			
			
			
			
			
			
			
			
			
			
			
			
			HttpURLConnection conn = (HttpURLConnection) ringOutURL.openConnection();
			String bearerAuth = "bearer   " + accessToken;
			System.out.println(bearerAuth );
			conn.setRequestProperty ("Authorization", bearerAuth);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);			
										
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb= new StringBuilder();
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				
				sb.append(output);

				
			}
			System.out.println("OutSide While");
			jsonObject=new JSONObject(sb.toString());
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
			System.out.println("Json Data Is"+jsonObject);
			
			String startTime = JsonPath.read(sb.toString(), "$.records[0].startTime");
			/*String [] time=startTime.split("T");
			System.out.println("time "+time[0]+" "+time[1].substring(0,time[1].length()-5));
			
try{
	String date=time[0]+" "+time[1].substring(0,time[1].length()-5);
				String dateFormat1=dateformat.format(startTime);
				System.out.println("date "+dateFormat1);
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}*/
			
			System.out.println("Json Path is"+JsonPath.read(sb.toString(),"$.records[0].startTime"));
			String tophone=JsonPath.read(sb.toString(),"$.records[0].to.phoneNumber");
			String fromname=JsonPath.read(sb.toString(),"$.records[0].from.name");
			
			
		String extensionNumber=JsonPath.read(sb.toString(),"$.records[0].from.extensionNumber");
			String toname=JsonPath.read(sb.toString(),"$.records[0].to.name");
			int  duration=JsonPath.read(sb.toString(), ("$.records[0].duration"));
             System.out.println("Duratin "+duration); 
			System.out.println("NAme is"+fromname );
			
			System.out.println("Phone is"+tophone);
			
           CallLogObject callLogStatus = new Gson().fromJson(sb.toString(), CallLogObject.class);
            
			List<Map> callLogList = (List<Map>) callLogStatus.getRecords();
			System.out.println("*************Call Log LIST IS*************"+callLogList);
			LinkedTreeMap<Object,Object> a = (LinkedTreeMap<Object,Object>) callLogList.get(0);
			
         JSONArray jr = new JSONArray();
         
         JSONObject obj = new JSONObject();
         obj.put("TOName", toname);
         obj.put("FromName", fromname);
         obj.put("Number", tophone);
         obj.put("Duration", duration);
         




         
         jr.put(obj);
			System.out.println("*************Call Log LIST Details IS*************"+a);

		  	couchbase.storeDataInCouchBase("callLog_"+fromname+"_"+toname+"_"+tophone+"_"+extensionNumber, obj);

			/*try{
				jsonObject=new JSONObject(client.get("lastAccessTimeStamp").toString());
		  	}catch(Exception e){
		  		
		  	}
		  	if(jsonObject!=null){
			  	jsonObject.put("UserLogs_"+emailId, format.format(calendar.getTime()));
			  	couchbase.updateDataInCouchBase("lastAccessTimeStamp", jsonObject);

		  	}else{
		  		jsonObject=new JSONObject();
			  	jsonObject.put("UserLogs_"+emailId, format.format(calendar.getTime()));

			  	couchbase.storeDataInCouchBase("callLog_"+phone+"_"+name+"_"+cur, jsonObject);

		  	}
		*/
			
			
			
			
			
			return ((Double) a.get("duration"));
			
			
			
			
			//return callLogStatus;
			
			
		}
		catch(Exception e){
			throw e;
		}
		
	}
	public static void main(String[] args) throws Exception {
		PhoneCall.getDuration();
	}
}