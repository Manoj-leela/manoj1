package helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class OntraPortOperation {
	 
		JSONObject jsonOutside=new JSONObject(); 
		JSONArray jsonArrayOutSide=new JSONArray();

	public void createContact(String  firstname,String  lastname,String email,String mobilenumber,int  role,String  company,String  refferedby,String  refferalId,String  officePhone,String  agreementURL,String  address,String  city,String  zip){
		
		 
		 try{
jsonOutside.put("objectID", 0);

		 jsonOutside.put("firstname", firstname);

		 jsonOutside.put("lastname", lastname);

		 jsonOutside.put("email", email);
		 jsonOutside.put("f1446", mobilenumber);
		 jsonOutside.put("sms_number", mobilenumber);

		 //occupation fealid or role
		 jsonOutside.put("f1419",role);
		 jsonOutside.put("company", company);
		 
		 jsonOutside.put("f1422", true);

		 
		 //referredby
		 jsonOutside.put("f1443", refferedby);
		 
		 ///refrrall
		 jsonOutside.put("f1444", refferalId);
		 jsonOutside.put("office_phone", officePhone);
		 
		 //adding tag (agreement)
		 jsonOutside.put("contact_cat", 17);

		 //referral agremment
		 jsonOutside.put("f1445", agreementURL);

          jsonOutside.put("address", address);
          jsonOutside.put("city", city);
          jsonOutside.put("zip", zip);

          
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
	/*public static void main(String[] args) {
		new OntraPortOperation().createContact("sahil", "singh", "sahil@domain.com", "123-223-2133", 6, "biz", "self", "21", "321", "https://dev-doc.visdom.ca/getid?id","sarjapur" , "banglore", "560054");
	}*/
	
}
