package com.syml.submitreferral.impl;

import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.syml.referralsource.impl.ReferralSourcePageServiceException;

import controllers.Lead;

public class LeadTaskCreationRestcall extends Thread {

	Lead lead;

	public LeadTaskCreationRestcall(Lead lead) {
		this.lead = lead;
	}

	@Override
	public void run() {
		String jsonString = pojoTojson(lead);
		try {
			restCallLeadTasks(jsonString);
		} catch (ReferralSourcePageServiceException e) {

			Logger.error("Error in taskcreation of Referral ", e);
		}
	}

	public void restCallLeadTasks(String jsonString)
			throws ReferralSourcePageServiceException {
		Logger.debug("(.) inside  restCallLeadTasks method with lea details : "
				+ jsonString);

		Client client = Client.create();

		WebResource webResource = client
				.resource(SubmitReferralConstants.TASK_CREATION_APP_URL);

		ClientResponse response = webResource.type("application/json").post(
				ClientResponse.class, jsonString);

		if (response.getStatus() != 200) {
			throw new ReferralSourcePageServiceException(
					"Failed To task create for Referral : HTTP error code : "
							+ response.getStatus());
		}
		Logger.info("Output from Server .... \n");

		String output = response.getEntity(String.class);
		Logger.info(output);

	}

	public String pojoTojson(Lead lead) {

		JSONObject jsonTableData = new JSONObject();
		try {
			jsonTableData.put("leadid", lead.getId());
			jsonTableData.put("leadName", lead.getName());
			jsonTableData.put("leadPhone", lead.getMobile());
			jsonTableData.put("leadEmail", lead.getEmail_from());

			jsonTableData.put(
					"leadReferralName",
					lead.getReferral_Source_First_Name() + " "
							+ lead.getReferral_Source_Last_Name());
			jsonTableData.put(
					"leadReferralPhone",
					lead.getReferral_Source_Phone() == null ? "" : lead
							.getReferral_Source_Phone());
			jsonTableData.put("leadReferralEmail",
					lead.getReferral_Source_Email());

		} catch (org.codehaus.jettison.json.JSONException e1) {
			Logger.error("Error in parsing lead pojo to Json " + e1);
		}

		return jsonTableData.toString();
	}

}