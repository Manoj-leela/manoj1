package com.syml.referral;

import infrastracture.couchbase.CouchBaseOperation;
import infrastracture.odoo.OdooException;
import infrastracture.odoo.OdooHelpler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.DesignDocument;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewRow;
import com.syml.Constants;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;
import com.syml.couchbase.dao.service.CouchBaseService;

import controllers.Client;

public class ReferralSurvey {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();

	CouchBaseOperation couchBaseOperation=new CouchBaseOperation();
	OdooHelpler odooHelpler = new OdooHelpler();
	CouchBaseService couchBaseService = new CouchBaseService();
	CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
	
	/**
	 * To store Referral Suvery details into couchbase 
	 * @param jsonObject
	 * @throws RefferalSurveyException
	 */
	public void storeReferralSurvey(JsonObject jsonObject) throws RefferalSurveyException  {

		ArrayList<String> list = null;
		
		logger.debug("(.) inside storeReferralSurvey method ------------------------>");

		String applicantName = null;
		String referralName = null;
		String refrralEmail = null;
			try {
				list = (ArrayList<String>) odooHelpler.getReferralnameByLeadID(jsonObject.get(
						"opprtunityId").toString());
				if(list!=null && list.size()==2){
				referralName = list.get(1).toString();
				refrralEmail = list.get(0).toString();
				}
			} catch (OdooException e) {
				throw new RefferalSurveyException("The list of Refferal name not found",e);
			}

			try {
				applicantName = odooHelpler.getApplicantnamesByleadId(jsonObject.get(
						"opprtunityId").toString());
			} catch (OdooException e1) {
				throw new RefferalSurveyException("The applicant name not found",e1);
			}
			jsonObject.put("ClientName", applicantName);
			jsonObject.put("ReferralName", referralName);
			jsonObject.put("ReferralEmail", refrralEmail);

			try {
				couchBaseService.storeDataToCouchbase(Constants.SURVEY_COUCHBASE_KEY + referralName + "_"
						+ jsonObject.get("opprtunityId").toString(), jsonObject);
			} catch (CouchbaseServiceException e) {
				throw new RefferalSurveyException("error in storing referral survey details into couchbase ",e);
			}catch (Exception e) {
				throw new RefferalSurveyException("error in storing referral survey details into couchbase",e);
			}
			
		
	}

	/**
	 * To get Referral survey details from couchbase query by opportunityId
	 * @param opprtunityId
	 * @return
	 * @throws RefferalSurveyException
	 */
	public ArrayList<Client> getReferralSurveyFromCouchbase(String opprtunityId) throws RefferalSurveyException   {
		Bucket bucket;
		ArrayList<Client> list = new ArrayList<Client>();
		List<ViewRow> rowlist = null;
		try {
			DesignDocument designDocument = getReferralDesignDocument(Constants.REFERRAL_DESGIN_DOC, opprtunityId);
			logger.debug("Desgin DOcument Name :  "+designDocument);
			bucket = couchbaseUtil.getCouchbaseClusterConnection();
			rowlist = bucket.query(ViewQuery.from(designDocument.name(), Constants.REFERRAL_DESGIN_DOC).stale(Stale.FALSE))
					.allRows();
		} catch (CouchbaseServiceException e) {
			throw new RefferalSurveyException("Error in getting referral survey details ",e);
		}catch (Exception e) {
			throw new RefferalSurveyException("Error in getting referral survey details ",e);
		}
		
		
		
		list = getViewRowList(rowlist);
		
		logger.debug("list of Client size ->" + list.size());

		return list;
	}
	private ArrayList<Client> getViewRowList(List<ViewRow> rowlist) {
		ArrayList<Client> list = new ArrayList<Client>();
		for (Iterator<ViewRow> iterator = rowlist.iterator(); iterator.hasNext();) {
			Client client = new Client();
			ViewRow viewRow = (ViewRow) iterator.next();
			logger.debug("document" + viewRow.document());
			JsonDocument jsonDocument = viewRow.document();
			logger.debug("name get from docuement opportunityid -->"
					+ jsonDocument.content().get("opprtunityId").toString());
			logger.debug(
					"name get from docuement Client Name-->" + jsonDocument.content().get("ClientName").toString());
			client.setOpprtunityId(jsonDocument.content().get("opprtunityId").toString());
			client.setApplicantname(jsonDocument.content().get("ClientName").toString());
			list.add(client);
		}
		return list;
	}

	private DesignDocument getReferralDesignDocument(String name, String opportunity)
			throws CouchbaseServiceException {
		logger.debug("(.) inside getReferralDesignDocument ");
		Bucket bucket = null;
		
		try {
			bucket = couchbaseUtil.getCouchbaseClusterConnection();
		} catch (CouchbaseServiceException e) {
			logger.error("Connection Time out Exception has found to couchbase", e.getMessage());
			throw new CouchbaseServiceException("Connection Time out exception has found to couchbase", e);
		}
		
		BucketManager bucketManager = bucket.bucketManager();
		DesignDocument designDoc = bucketManager.getDesignDocument(name + opportunity);

		logger.debug("design doc" + designDoc);

		if (designDoc == null) {

			designDoc = DesignDocument.create(name + opportunity,
					Arrays.asList(DefaultView.create(name, "function (doc,meta) { if(doc.opprtunityId=='"
							+ opportunity + "' && doc.Type=='Referral'){ emit(meta.id,null); }}")));
			bucketManager.insertDesignDocument(designDoc);

		}

		logger.debug("The design doc has created by this name -->" + designDoc.name());

		return designDoc;
	}

}
