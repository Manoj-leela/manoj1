package com.syml.referralsource.impl;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import controllers.Referral_Source;

public class ReferralTaskCreationRestCall extends Thread {

	Referral_Source referral_Source;

	public ReferralTaskCreationRestCall(Referral_Source referral_Source) {
		this.referral_Source=referral_Source;
	}
	
	@Override
	public void run() {
		try {
			restCallReferralTaskCreation(referral_Source);
		} catch (ReferralSourcePageServiceException e) {
			Logger.error("Error in task creation of lead ",e);
		}
	}
	/**
	 * To make Rest call to task creation app to create referral tasks (about new referral Registred inivte 
	 * them )
	 * @param referral_Source
	 * @throws ReferralSourcePageServiceException 
	 */
	public void restCallReferralTaskCreation(Referral_Source referral_Source) throws ReferralSourcePageServiceException {
		Logger.info("insdie rest call of the referral page");
		
		JSONObject jsonTableData = new JSONObject();
		
			try {
				jsonTableData.put("name", referral_Source.getName());
		
			jsonTableData.put("phonenumber",
					referral_Source.getPartner_mobile());
			jsonTableData.put("email", referral_Source.getEmail_from());

			jsonTableData.put("referrdBy", referral_Source.getReferrer());

		

		

			Client client = Client.create();

			WebResource webResource = client
					.resource(ReferralConstants.TASK_CREATION_APP_URL);

			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, jsonTableData.toString());

			if (response.getStatus() != 200) {
				throw new ReferralSourcePageServiceException("error in creating tasks for referral Details  "
						+ jsonTableData +", Status "+response.getStatus());
			}

		 response.getEntity(String.class);
			Logger.info("Request of referral tasks send to  Taskcreation app  sucessfully  done.... \n");

			} catch (JSONException e) {
			throw new ReferralSourcePageServiceException("error in creating tasks for referral Details  "
					+ jsonTableData);
			}
			

	
	}

}