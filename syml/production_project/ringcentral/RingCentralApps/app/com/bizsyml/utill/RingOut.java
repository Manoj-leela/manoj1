package com.bizsyml.utill;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/*
 *  Class that calls RingOut REST API.
 */

public class RingOut {

	public static RingStatusObject MakeCall(String accessToken,String Caller , String Callee) throws Exception {
		
		try{
			
			TrustCertsUtil.disableSslVerification();
			URL ringOutURL = new URL(RingCentralConstants.RINGOUT_URL);
			HttpURLConnection conn = (HttpURLConnection) ringOutURL.openConnection();
			String bearerAuth = "bearer   " + accessToken;
			//System.out.println(bearerAuth );
			conn.setRequestProperty ("Authorization", bearerAuth);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);			
			RingDataObject obj = new RingDataObject(Caller,Callee);
			Gson gson = new Gson();
			String postData = gson.toJson(obj);
			//System.out.println(postData);	
					
			conn.getOutputStream().write(postData.getBytes( Charset.forName( "UTF-8" )));
			conn.getOutputStream().flush();
			
			
			
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
	
	public static void main(String[] args) throws Exception {

		
		String accessToken="U0pDMTFQMDFQQVMwMXxBQUNtTHJmeDlHLW1WamVxZ2xZRnNHbE5Ib1BIcmdoRVRfaGJxLWZaejdyRWp1ZHltN2N6OGlyWEVPVzlqMEtnd3owOEVhRHNxRWdJVUhPV3FzbEwwTUtuQ0gxR1h3cUFBVDF2cjZSYUdsbW93bVlMbWtKYURQeFJ83oNzpGrR6F79lD7oaxyuEaPthZg";
		String Caller="";
		String Callee="";

		new RingOut().MakeCall(accessToken, Caller, Callee);
		
	}

}
