package com.syml.purchaseProposal.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;
import com.syml.purchaseProposal.couchbase.*;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.service.CouchBaseService;

public class ExpiryDate {

	static Logger logger = LoggerFactory.getLogger(ExpiryDate.class);
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

	public Date getExpiryDate(String exp) throws CouchbaseDaoServiceException {

		CouchbaseData couchdata = new CouchbaseData();
		JsonObject expiry = null;
		try {
			expiry = couchdata.getDataCouchbase(exp);
		} catch (CouchbaseDaoServiceException e1) {
			logger.error("expiry date :" + e1.getMessage());
			throw new CouchbaseDaoServiceException("Expiry date getting null from coucbase", e1);
		}
		Date expirayTime = null;
		String expirayTime1 = "";
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		try {

			expirayTime1 = expiry.get("ExpiryDateTime").toString();

		} catch (NullPointerException e) {
			logger.error("NUll pointer:" + e.getMessage());
		}
		try {
			expirayTime = df.parse(expirayTime1);

		} catch (ParseException e) {
			logger.error("Parse Exception: " + e.getMessage());
		}

		return expirayTime;

	}

	public boolean CheckDateDifferece(String opp_id) throws CouchbaseDaoServiceException {

		if (getCurrentDate().compareTo(getExpiryDate(opp_id)) >= 0) {
			return true;
		}

		return false;

	}

	public String getStringExpiryDate(String opp) throws CouchbaseDaoServiceException {

		CouchbaseData couchdata = new CouchbaseData();
		JsonObject expiry = null;
		try {
			expiry = couchdata.getDataCouchbase(opp);
		} catch (CouchbaseDaoServiceException e1) {
			logger.error("CouchbaseDaoServiceException:" + e1.getMessage());
			throw new CouchbaseDaoServiceException("Error While Expiry date is null", e1);
		}

		String expirayTime = "";

		try {

			expirayTime = expiry.get("ExpiryDateTime").toString();

		} catch (NullPointerException e) {
			logger.error("NUll pointer:" + e.getMessage());
		}

		return expirayTime;

	}

	public Date getCurrentDate() {

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String current_date = df.format(today);
		Date date = null;
		try {
			date = df.parse(current_date);
		} catch (ParseException e) {

			logger.error("ParseException in current Date :" + e.getMessage());
		}
		
		return date;
	}

	public static void main(String... A) throws CouchbaseDaoServiceException {
		ExpiryDate EX = new ExpiryDate();
		Date value =EX.getExpiryDate("4406");
	logger.debug("va lue date : "+value);
		//EX.CheckDateDifferece("3584");
		//logger.debug("coming as " + EX.CheckDateDifferece("3584"));
		// System.out.println(EX.getCurrentDate());
	}

	public static void setExpiryDate(Date date, String oppertunityId) throws CouchbaseDaoServiceException {

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(date);
		CouchbaseData cbdata = new CouchbaseData();

		JsonObject expiryDate = null;
		try {
			expiryDate = cbdata.getDataCouchbase(oppertunityId);
			expiryDate.put("ExpiryDateTime", reportDate);

			cbdata.UpdateDataCouchbase(oppertunityId, expiryDate.toString());
		} catch (CouchbaseDaoServiceException e1) {

			logger.error("CouchbaseDaoServiceException" + e1.getMessage());
			throw new CouchbaseDaoServiceException("error in update Expiry Date in Couchbase ",e1);
		}

	}
	
	public  String  getFormatDate(String oppid) throws CouchbaseDaoServiceException{
		
		String formatDate=	df.format(getExpiryDate(oppid));
			return formatDate;
		
	}
}