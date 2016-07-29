package infrastracture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import play.Logger;
import com.couchbase.client.CouchbaseClient;
import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;

public class ReferralSurvey {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();

	CouchBaseOperation couchBaseOperation=new CouchBaseOperation();
	CouchbaseClient client1=null;
	
	public void checkReferralSurvey(JSONObject jsonObject) {

		ArrayList list = null;
		client1 = couchBaseOperation.getConnectionToCouchBase();
		logger.debug("inside check referrral survey------------------------>");

		String applicantName = null;
		String referralName = null;
		String refrralEmail = null;

		try {
			// getting list applicants already referrrad

			list = (ArrayList) getReferralnameByLeadID(jsonObject.get(
					"opprtunityId").toString());

			applicantName = getApplicantnamesByleadId(jsonObject.get(
					"opprtunityId").toString());

			try {
				referralName = list.get(1).toString();
				refrralEmail = list.get(0).toString();
			} catch (Exception e) {
				logger.error("Error occured in json info"+e.getMessage());
			}

			jsonObject.put("ClientName", applicantName);
			jsonObject.put("ReferralName", referralName);
			jsonObject.put("ReferralEmail", refrralEmail);

			couchBaseOperation.storeDataInCouchBase("survey_" + referralName + "_"
					+ jsonObject.get("opprtunityId").toString(), jsonObject);
			client1.shutdown();
		} catch (Exception e) {
			 logger.error("Error occured in couchbase connection"+e.getMessage());
		}

	}

	
	//Get applicantName by Lead id --------------------------
	
	public String getApplicantnamesByleadId(String opprunityID) {
		String applicantName = null;

		String contactId = null;

		try {
			Session openERPSession =couchBaseOperation.getOdooConnection();
			logger.debug("Seesion is" + openERPSession);
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

			FilterCollection filter = new FilterCollection();
			filter.add("id", "=", opprunityID);
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "app_rec_ids" });
			Row row1 = row.get(0);
			Object[] object = (Object[]) row1.get("app_rec_ids");
			contactId = object[0].toString();
		} catch (Exception e) {
			 logger.error("Error occured in applicatName id"+e.getMessage());
		}

		try {
			Session openERPSession =couchBaseOperation.getOdooConnection();
			logger.debug("Seesion is" + openERPSession);
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("applicant.record");

			FilterCollection filter = new FilterCollection();
			if (contactId != null) {
				filter.add("id", "=", contactId);
			}
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "applicant_name",
							"applicant_last_name", "emial_personal" });
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				Row row2 = (Row) iterator.next();
				applicantName = row2.get("applicant_name").toString() + "_"
						+ row2.get("applicant_last_name").toString();

			}

		} catch (Exception e) {
			 logger.error("Error occured while odoo connection"+e.getMessage());
		}

		return applicantName;
	}
	
	
	
	//--------------getReferral name And email using leadId--------------from openerp--
	public List getReferralnameByLeadID(String leadId) {
		String referralName = null;
		String contactId = null;

		String referralID = null;
		ArrayList list = new ArrayList();

		RowCollection leadList = null;
		try {

			Session openERPSession =couchBaseOperation.getOdooConnection();
			ObjectAdapter lead = openERPSession.getObjectAdapter("crm.lead");

			FilterCollection filters1 = new FilterCollection();
			filters1.add("id", "=", leadId);

			leadList = lead.searchAndReadObject(filters1, new String[] { "id",
					"referred_source" });
			logger.debug(leadList.size() + "id");

			for (Row row : leadList) {
				if (row.get("referred_source") != null) {
					Object[] object = (Object[]) row.get("referred_source");
					referralID = object[0].toString();
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in referalname "+e.getMessage());
		}

		RowCollection referralList = null;
		try {

			Session openERPSession = couchBaseOperation.getOdooConnection();
			ObjectAdapter referral = openERPSession
					.getObjectAdapter("hr.applicant");

			FilterCollection filters1 = new FilterCollection();
			filters1.add("id", "=", referralID);

			referralList = referral.searchAndReadObject(filters1, new String[] {
					"id", "name", "partner_id", "email_from" });
			logger.debug(referralList.size() + "id");

			for (Row row : referralList) {
				if (row.get("partner_id") != null) {
					Object[] object = (Object[]) row.get("partner_id");
					contactId = object[0].toString();
				}
				list.add(row.get("email_from").toString());
			}
		} catch (Exception e) {
			 logger.error("Error occured in referalList"+e.getMessage());
		}

		try {
			Session openERPSession =couchBaseOperation.getOdooConnection();
			logger.debug("Seesion is" + openERPSession);
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("res.partner");

			FilterCollection filter = new FilterCollection();
			filter.add("id", "=", contactId);
			RowCollection row = opprtunity.searchAndReadObject(filter,
					new String[] { "id", "name" });
			Row row1 = row.get(0);
			referralName = row1.get("name").toString();
			list.add(referralName);
			logger.debug("-----------------referral soursce Name--------------------- "
							+ referralName);
		} catch (Exception e) {
			 logger.error("Error occured in referalName in list"+e.getMessage());
		}

		return list;
	}

}
