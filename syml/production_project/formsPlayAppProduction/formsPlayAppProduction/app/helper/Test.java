package helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonArray;


 public class Test {
	
	 
	 public static void main(String[] args) {
		
		 
		 
		 
		 try{
			

			 
		JSONObject jsonOutside=new JSONObject(); 	  	 
	/*	jsonOutside.put("appid", "2_80168_eQ7KwDUNN");
		jsonOutside.put("key","GneeytyRKVlMBTN");
		jsonOutside.put("reqtype","add");*/



String myvar = "["+
"  {"+
"    \"fields\": ["+
"      {"+
"        \"name\": \"First Name\","+
"        \"value\": \"Tim\""+
"      },"+
"      {"+
"        \"name\": \"Last Name\","+
"        \"value\": \"Lincecum\""+
"      },"+
"      {"+
"        \"name\": \"Email\","+
"        \"value\": \"t.lincecum@test.com\""+
"      },"+
"      {"+
"        \"name\": \"Contact Tags\","+
"        \"value\": \"Test\""+
"      },"+
"      {"+
"        \"name\": \"Sequences\","+
"        \"value\": \"*/*10*/*2*/*14*/*\""+
"      }"+
"    ]"+
"  }"+
"]";
	

JSONArray jsonArrayOutSide=new JSONArray();
jsonOutside.put("objectID", "0");

		 jsonOutside.put("firstname", "test");

		 jsonOutside.put("lastname", "m");

		 jsonOutside.put("email", "venkateshm8@gmail.com");
		 jsonOutside.put("mobilenumber", "645-332-3445");
		
		 //occupation fealid or role
		 jsonOutside.put("f1419", 8);
		 jsonOutside.put("company", "Test");
		
		 //referredby
		 jsonOutside.put("f1443", "Test");
		 
		 ///refrrall
		 jsonOutside.put("f1444", 20);
		 jsonOutside.put("officephone", "3223-3232-3231");
		 
		 //adding tag (agreement)
		 jsonOutside.put("contact_cat", 17);

		 //referral agremment
		 jsonOutside.put("f1445", "url");


		 jsonArrayOutSide.put(jsonOutside);
	

		        URL url = new URL("https://api.ontraport.com/1/objects/saveorupdate");
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		     
		        conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Api-Appid", "2_80168_eQ7KwDUNN");
				conn.setRequestProperty("Api-Key", "GneeytyRKVlMBTN");

			/*	conn.setRequestProperty("appid", "2_80168_eQ7KwDUNN");
				conn.setRequestProperty("key", "GneeytyRKVlMBTN");*/


				String input=jsonOutside.toString();
			OutputStream os = conn.getOutputStream();
					os.write(input.getBytes());
					os.flush();
					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
					}else{
			 
					BufferedReader br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
			 
					
					System.out.println("Output from Server .... \n");
		                           String output;
					while ((output = br.readLine()) != null) {
						System.out.println("output  "+output);
				
						
						
						
					}
		                            
					}
		        System.out.println(conn.getResponseCode());
		        conn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
		 
	}

 
 
 }
