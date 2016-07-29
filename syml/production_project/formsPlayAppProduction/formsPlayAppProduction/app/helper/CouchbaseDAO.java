package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import openerp.CreateApplicant;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import couchbase.CouchBaseOperation;

public class CouchbaseDAO {

	
	String applicantID ;

	
	public CouchbaseDAO(String applicantID) {
		this.applicantID=applicantID;
		// TODO Auto-generated constructor stub
	}

	CouchBaseOperation couchbaseObject = null;
	String applicantIDCB = "Applicant_" + applicantID;
	JSONObject jsonObject = null;
	List<String> dataList=null;
	public List<String> getPage4Data() {
		Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
		dataList=new ArrayList<String>();
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			dataList.add(jsonObject.getString("Applicant-mortgageinmind1"));
			dataList.add(jsonObject.getString("Applicant-currentMortgageTerm"));
			dataList.add(jsonObject.getString("Applicant-lookingfor11"));
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return dataList;
	}
	
	public List<String> getPage5aData() {
		Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
		dataList=new ArrayList<String>();
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			dataList.add(jsonObject.getString("Applicant-incomedown1"));
			dataList.add(jsonObject.getString("Applicant-largerfamily1"));
			dataList.add(jsonObject.getString("Applicant-buyingnewvechile1"));
			dataList.add(jsonObject.getString("Applicant-Planninglifestyle1"));
			dataList.add(jsonObject.getString("Applicant-financialrisk1"));
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return dataList;
	}
	
	public List<String> getPage5bData() {
		Logger.info("Inside readApplicantRecord method of PorpertiesDBOperation");
		dataList=new ArrayList<String>();
		try{
			couchbaseObject =new CouchBaseOperation();
			jsonObject = couchbaseObject.getCouchBaseData(applicantIDCB);
			dataList.add(jsonObject.getString("Applicant-thinkproperty1"));
			dataList.add(jsonObject.getString("Applicant-imaginesamejob1"));
			dataList.add(jsonObject.getString("Applicant-incomeraise1"));
			dataList.add(jsonObject.getString("Applicant-rentalproperty1"));
		}catch(JSONException | NullPointerException excp){
			Logger.debug("Exception when reading record from couchbase.",excp);
		}
		return dataList;
	}
	
	
}
