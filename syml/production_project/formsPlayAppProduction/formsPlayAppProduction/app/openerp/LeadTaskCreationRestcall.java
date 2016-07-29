package openerp;


import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;




public class LeadTaskCreationRestcall extends Thread {
	
	
	
	
	String documentlIst;
	
	public LeadTaskCreationRestcall(String documentlIst) {
		super();
		this.documentlIst = documentlIst;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		callToTaskCreationApp(documentlIst);
	}

@SuppressWarnings("unused")
public static void main(String[] args) {
	
	JSONObject jsonTableData=new JSONObject();
	String name="devTest";
	String phoneNumber="988-566-655";
	try{	
	
	/*	jsonTableData.put("name", name);
		jsonTableData.put("phonenumber", phoneNumber);*/

		jsonTableData.put("leadid", "111");
		jsonTableData.put("leadName","DevTest");
		jsonTableData.put("leadPhone","555-651-7444");
		jsonTableData.put("leadReferralName","DevTestRefer");
		jsonTableData.put("leadReferralPhone","555-555-334");
		jsonTableData.put("leadReferralEmail","555-555-334");

		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
new	LeadTaskCreationRestcall	(jsonTableData.toString()).start();;
//restCallLeadTasks(jsonTableData.toString());
}


	
	public void callToTaskCreationApp(String tasks){
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource("https://task.visdom.ca/leaddata");


			ClientResponse response = webResource.type("application/json")
			   .post(ClientResponse.class, tasks);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}

			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);

		  } catch (Exception e) {

			e.printStackTrace();

		  }
	}

	



}
