package com.syml.client;

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

public class ClientSurvey {

	private static org.slf4j.Logger logger = play.Logger.underlying();

	CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
	CouchBaseService couchBaseService = new CouchBaseService();
	CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
	OdooHelpler odooHelpler = new OdooHelpler();

	/**
	 * To store client survey details into couchbase 
	 * @param jsonObject
	 * @throws ClientSurveyException
	 */
	public void storeClientSurvey(JsonObject jsonObject) throws ClientSurveyException {
		logger.debug("(.) inside storeClientSurvey method ---------------------->");

		ArrayList<Client> list = null;

		ArrayList<String> applicantList = null;

		String applicantName = null;
		String applicantEmail = null;
			list = getListOfClientSurvey(jsonObject.get("opprtunityId").toString());
		try {
			applicantList = (ArrayList<String>) odooHelpler
					.getApplicantnames(jsonObject.getString("applicantId").toString());
			if(!applicantList.isEmpty()){
			applicantName = applicantList.get(0).toString();
			applicantEmail = applicantList.get(1).toString();
			}
			
		} catch (OdooException oe) {
			logger.error("The openerp operation  has failed , applicant list has not found");
			throw new ClientSurveyException("error in storeing clinet Details ", oe);
		}
	

		if (list.size() != 0) {

			for (Iterator<Client> iterator = list.iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				if (!client.getApplicantId().equalsIgnoreCase(jsonObject.getString("applicantId").toString())) {

					jsonObject.put("alreadyPaid", client.getApplicantname());
					jsonObject.put("Type", "Client");

					jsonObject.put("ClientName", applicantName);
					jsonObject.put("ClientEmail", applicantEmail);
					try {
						couchBaseService.storeDataToCouchbase(
								Constants.SURVEY_COUCHBASE_KEY + applicantName + "_" + jsonObject.get("opprtunityId").toString(), jsonObject);
						logger.debug("client survay details stored sucessFully ");

					} catch (CouchbaseServiceException e) {
						throw new ClientSurveyException("Error in storing clint details  ",e);
					}
				}
			}
		} else {

			jsonObject.put("AlreadyPaid", applicantName);
			jsonObject.put("Type", "Client");

			jsonObject.put("ClientName", applicantName);
			jsonObject.put("ClientEmail", applicantEmail);
			try {
				couchBaseService.storeDataToCouchbase(
						Constants.SURVEY_COUCHBASE_KEY + applicantName + "_" + jsonObject.get("opprtunityId").toString(), jsonObject);
		logger.debug("client survay details stored sucessFully ");
			} catch (CouchbaseServiceException e) {
			throw new ClientSurveyException("Error in storing client survey  Data ",e);
			}catch (Exception e) {
				throw new ClientSurveyException("Error in storing client survey  Data",e);
			}
			
		}

	}

	/**
	 * To get Client details from couchbase Through Couchbase query
	 * @param opprtunityId
	 * @return
	 * @throws ClientSurveyException
	 */
	public ArrayList<Client> getListOfClientSurvey(String opprtunityId) throws ClientSurveyException {
		logger.debug("(.) inside getListOfClientSurvey");
		Bucket bucket;
		ArrayList<Client> list = new ArrayList<Client>();
		List<ViewRow> rowlist = null;
		try {
			DesignDocument designDocument = getClientDesignDocument(Constants.CLIENT_DESGIN_DOC, opprtunityId);
			logger.debug("created desgin document name "+designDocument);
			bucket = couchbaseUtil.getCouchbaseClusterConnection();

			rowlist = bucket.query(ViewQuery.from(designDocument.name(),Constants.CLIENT_DESGIN_DOC).stale(Stale.FALSE)).allRows();
		} catch (CouchbaseServiceException e1) {
			logger.error("The client survey document list not found" + e1.getMessage());
			throw new ClientSurveyException("The client survey document list not found", e1);
		}catch (Exception e) {
			throw new ClientSurveyException("The client survey document list not found", e);
		}

		list = getViewRowList(rowlist);

		logger.debug("list of Client size ->" + list.size());
		return list;
	}

	/**
	 * to get list of Clients From viewRow
	 * @param rowlist
	 * @return
	 */
	private ArrayList<Client> getViewRowList(List<ViewRow> rowlist) {
		ArrayList<Client> list = new ArrayList<Client>();
		for (Iterator<ViewRow> iterator = rowlist.iterator(); iterator.hasNext();) {
			try{
			Client client = new Client();
			ViewRow viewRow = (ViewRow) iterator.next();
			logger.debug("document" + viewRow.document());
			JsonDocument jsonDocument = viewRow.document();
			logger.debug("name get from docuement opportunityid -->"
					+ jsonDocument.content().get("opprtunityId").toString());
			logger.debug(
					"name get from docuement Client Name-->" + jsonDocument.content().get("ClientName").toString());
			client.setApplicantId(jsonDocument.content().get("applicantId").toString());
			client.setOpprtunityId(jsonDocument.content().get("opprtunityId").toString());
			client.setApplicantname(jsonDocument.content().get("ClientName").toString());
			list.add(client);
			}catch(Exception e){
				logger.error("Error client details not for given Opportunity ID ",e);
			}
		}
		return list;
	}

	/**
	 * Toget DesginDocument by check whether desginDOcument exist if exist return design doc else create
	 * @param name
	 * @param opportunity
	 * @return
	 * @throws CouchbaseServiceException
	 */
	private DesignDocument getClientDesignDocument(String name, String opportunity)
			throws CouchbaseServiceException {
		logger.debug("(.) inside getClientDesignDocument ");
		Bucket bucket = null;
		DesignDocument designDoc=null;
		try {
			bucket = couchbaseUtil.getCouchbaseClusterConnection();
	
		BucketManager bucketManager = bucket.bucketManager();
		 designDoc = bucketManager.getDesignDocument(name+opportunity);

		logger.debug("design doc" + designDoc);

		if (designDoc == null) {

			designDoc = DesignDocument.create(name + opportunity,
					Arrays.asList(DefaultView.create("client", "function (doc,meta) { if(doc.opprtunityId=='"
							+ opportunity + "' && doc.Type=='Client'){ emit(meta.id,null); }}")));
			bucketManager.insertDesignDocument(designDoc);

		}

		logger.debug("The design doc has created by this name for client  -->" + designDoc.name());
		} catch (CouchbaseServiceException e) {
			throw new CouchbaseServiceException("Error while creating desginDOcument", e);
		}catch (Exception e) {
			throw new CouchbaseServiceException("Error while creating desginDOcument", e);

		}
		return designDoc;
	}
}
