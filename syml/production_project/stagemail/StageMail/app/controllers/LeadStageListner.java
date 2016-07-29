package controllers;

import infrastracture.CouchBaseOperation;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendwithus.SendWithUsExample;

public class LeadStageListner {
 	static Logger log = LoggerFactory.getLogger(LeadStageListner.class);

	private Connection conn;
	CouchBaseOperation couchbase = new CouchBaseOperation();
	CouchbaseClient client = null;

	String lead;

	public LeadStageListner(String lead) {
		super();
		this.lead = lead;
	}

	public static void main(String[] args) throws JsonProcessingException {
		mailToStageProposal(17, "reato", "4371", "test",
				"venkateshm383@gmail.com", "tests", "realtorName",
				"venkatesh.m@bizruntime.com","string");
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused", "deprecation" })
	public void run() {

		try {
			Properties prop = new Properties();
			ArrayList<URI> nodes = new ArrayList<URI>();
			try {

				// getting connection parameter
				prop.load(LeadStageListner.class.getClassLoader()
						.getResourceAsStream("config.properties"));

			} catch (Exception e) {

			}

			String url = prop.getProperty("postgresURL");
			String userName = prop.getProperty("postgresUserName");
			String userPassword = prop.getProperty("postgresPassword");

			try {
				Class.forName("org.postgresql.Driver");

				// Create two distinct connections, one for the notifier
				// and another for the listener to show the communication
				// works across connections although this example would
				// work fine with just one connection.
				conn = DriverManager.getConnection(url, userName, userPassword);
			} catch (Exception e) {
				try {
					url = prop.getProperty("postgresURL1");
					userName = prop.getProperty("postgresUserName1");
					userPassword = prop.getProperty("postgresPassword1");

					conn = DriverManager.getConnection(url, userName,
							userPassword);
					// TODO: handle exception
				} catch (Exception e11) {
					e11.printStackTrace();
				}
			}

			JSONObject json = null;
			try {
				log.debug("Inside TRY ---------- ");

				json = new JSONObject(lead);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int stage_id = 0;
			int referralFee = 0;
			int realatorfee = 0;
			String lenderName = "";
			try {
				stage_id = (int) json.get("stage_id");
				log.debug("StageID is" + stage_id);

			} catch (Exception e) {
				e.printStackTrace();

			}
			String Id = "0";
			Id = json.get("id").toString();

			JSONObject oldData = null;
			JSONObject jsonTableData = new JSONObject();
			JSONObject referralData = null;
			JSONObject realtorData = null;
			String referralId = null;
			String realtorId = null;
			String closingDate = null;
			try {
				referralId = json.get("referred_source").toString();
			} catch (Exception e) {

			}
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			try {
				
				Calendar cal=Calendar.getInstance();
		
				closingDate = dateFormat.format(new Date(json.get("expected_closing_date").toString()));;
			} catch (Exception e) {

			}
			try {
				realtorId = json.get("realtor").toString();
			} catch (Exception e) {

			}

			try {
				Statement stmt11 = conn.createStatement();
				ResultSet rs11 = stmt11
						.executeQuery("select row_to_json(hr_applicant) as hr_applicant from hr_applicant where id ="
								+ json.get("referred_source"));
				// log.debug("********hr_applicant_backup_tbl Data IS*****"+rs11.getString("hr_applicant_backup_tbl").toString());
				while (rs11.next()) {
					log.debug("*******hr_applicant**************");
					log.debug(rs11.getString("hr_applicant"));
					referralData = new JSONObject(
							rs11.getString("hr_applicant"));

				}
				rs11.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Statement stmtRealtor = conn.createStatement();

				ResultSet rsRealtor = stmtRealtor
						.executeQuery("select row_to_json(hr_applicant) as hr_applicant from hr_applicant where id ="
								+ json.get("realtor"));
				// log.debug("********hr_applicant_backup_tbl Data IS*****"+rs11.getString("hr_applicant_backup_tbl").toString());
				while (rsRealtor.next()) {
					
					log.debug(rsRealtor.getString("hr_applicant"));
					realtorData = new JSONObject(
							rsRealtor.getString("hr_applicant"));

				}
				stmtRealtor.close();
				rsRealtor.close();
			} catch (Exception e) {

			}
			JSONObject recomendationJsonData = null;

			ArrayList list = new ArrayList();
			try {
				Statement stmt12 = conn.createStatement();

				ResultSet rs12 = stmt12
						.executeQuery("select row_to_json(opp_recommendations) as opp_recommendations from opp_recommendations where opp_id ="
								+ Id);

				while (rs12.next()) {
				
					log.debug(rs12.getString("opp_recommendations"));
					recomendationJsonData = new JSONObject(
							rs12.getString("opp_recommendations"));
					list.add(recomendationJsonData);

				}

				try {
					rs12.close();
				} catch (Exception e) {

				}
			} catch (Exception e) {

			}

			String referralName = "";
			String role = "";
			String referralEmail = "";
			if (referralData != null) {
				referralName = couchbase.getReferralname(referralData.get(
						"partner_id").toString());
				role = referralData.get("role").toString();
				log.debug("Role Is " + role);
				referralEmail = referralData.get("email_from").toString();
				log.debug("Email  Is " + referralEmail);

			}

			String realtorName = "";
			String rolerealotor = "";
			String realtorEmail = "";
			if (realtorData != null) {

				realtorName = couchbase.getReferralname(realtorData.get(
						"partner_id").toString());
				rolerealotor = realtorData.get("role").toString();
				log.debug("Realtor Role Is " + rolerealotor);
				realtorEmail = realtorData.get("email_from").toString();
				log.debug("Email  Is " + realtorEmail);

			}

			jsonTableData.put("id", Id + "");
			jsonTableData.put("stage_Id", stage_id);
			// to get lender name ---------------------------------
			String productNameSeletected = "";
			String recomendedProduct = "";
	
			try {

				client = couchbase.getConnectionToCouchBase();
				try {

					oldData = new JSONObject(client.get(
							"leadsstageTasksdone" + Id.trim()).toString());
					log.debug("Old Data IS" + oldData);
					couchbase.closeCouchBaseConnection();

				} catch (Exception e) {
					e.printStackTrace();
				}

				String taskdone = null;

				//checking for old opprunity data id data not null means opprunity data exisist
				if (oldData != null && oldData.length() != 0) {
				
					try {
					play.Logger.info("old stage change data ------------------------>"
								+oldData);
						taskdone = oldData.get("task" + stage_id).toString();
						
					} catch (Exception e) {
						play.Logger.error("Error in getting oldstage done "+e);
					}
					
				}else {
					String productNameSeletectedfirst=null;
				try {
					productNameSeletectedfirst =json.get("selected_product").toString();
			
				} catch (Exception e) {
					play.Logger.error("Error in getting selected Product "+e);
				}
				play.Logger.debug("fitstproduct selected ------>"+productNameSeletectedfirst);
			try{
				if(productNameSeletectedfirst==null){
					productNameSeletectedfirst="";
				}
			}catch(Exception e){
				e.printStackTrace();
			}
				oldData = new JSONObject();
				oldData.put("task" + stage_id, stage_id);
				oldData.put("priviusproductSelected", productNameSeletectedfirst);
				oldData.put("OpprtunityId",Id);
				oldData=getStageIdDone(stage_id, oldData);
			

				couchbase.storeDataInCouchBase(
						"leadsstageTasksdone" + Id.trim(), oldData);
			}	
					
					String stageNAme = getstageId(stage_id);
					if (taskdone != null) {

						// privous selected product
						String priviousSelectedProduct = null;

						// if selected product
						productNameSeletected = null;
						recomendedProduct = "";

						// selected product from triger
						try {
							productNameSeletected = json.get("selected_product")
											.toString();
						
						} catch (Exception e) {
							log.debug("error in getting selected product "+e);
						}		

						if(productNameSeletected==null){
							productNameSeletected="";
						}

						
						log.debug("productNameSeletected------>"+productNameSeletected);


						// getting privious selected product from couchbase
						try {
							priviousSelectedProduct = oldData.get(
									"priviusproductSelected").toString();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(priviousSelectedProduct==null){
							priviousSelectedProduct="";
						}
						log.debug("priviousSelectedProduct------>"+priviousSelectedProduct);

						if (priviousSelectedProduct != null) {

							if (!priviousSelectedProduct
									.equalsIgnoreCase(productNameSeletected)) {

								// list is the recomended products from openerp
							
								productNameSeletected = "";
								 recomendedProduct = "";
								 JSONObject object	=null;
								try {

									
									
												JSONObject	 json1	=couchbase.getCouchBaseDataProposal("SelectRec_"+Id);

												JSONArray jsondata=json1.getJSONArray("Recommendations");
												object=jsondata.getJSONObject(0);
								} catch (Exception e) {
play.Logger.error("errro in getting sselected product from couchbase with OpporunityID = "+Id,e);								}
								
								if(object!=null){
									play.Logger.info("------------------calling selectedProduct mailg---------------------------------------");
									mailToProposalConfirm(Id, object,role,referralName,referralEmail,rolerealotor,realtorName,realtorEmail);
									oldData.put("priviusproductSelected",
											productNameSeletected);

									couchbase.updateDataInCouchBase(
											"leadsstageTasksdone" + Id, oldData);
		
						}	
									
								
								
								
							} else {
								oldData.put("priviusproductSelected",
										productNameSeletected);

								couchbase.updateDataInCouchBase(
										"leadsstageTasksdone" + Id, oldData);
							}

						}

					}

					if (taskdone == null) {
						jsonTableData.put("lender_name", lenderName);

						oldData.put("task" + stage_id, stage_id);
						oldData.put("OpprtunityId",Id);
						oldData=getStageIdDone(stage_id, oldData);

						couchbase.updateDataInCouchBase("leadsstageTasksdone"
								+ Id, oldData);
						RestCallClass.restCallStageChange(jsonTableData
								.toString());

						log.debug("Stage Of Current" + stageNAme);

						if (stageNAme.equalsIgnoreCase("Proposal")) {
							play.Logger.info("**********--Inside Proposal stage----------------and calling mail method-********");
							mailToStageProposal(stage_id, role, Id,
									referralName, referralEmail, rolerealotor,
									realtorName, realtorEmail,referralId);

						} else if (stageNAme.equalsIgnoreCase("Commitment")) {
							play.Logger.info("***********Inside Stage Stage Commitment********");

							mailToStageCommitment(role, referralData, Id,
									referralName, referralEmail, rolerealotor,
									realtorName, realtorEmail);
							
						} else if (stageNAme.equalsIgnoreCase("Compensation")) {
							play.Logger.info("***********Inside Stage Compensation********");
							try {
								referralFee = Integer.parseInt(json.get(
										"referral_fee").toString());

							} catch (Exception e) {

							}
							mailToStgaeCompensation(Id, referralEmail,
									referralName, recomendationJsonData,
									referralFee, lenderName, closingDate);

						} else if (stageNAme.equalsIgnoreCase("Paid")) {
							play.Logger.info("*********** Inside Stage Paid********");
							try {
								referralFee = Integer.parseInt(json.get(
										"referral_fee").toString());

							} catch (Exception e) {

							}
							mailStageToPaid(Id, referralName, referralEmail,
									referralFee, referralId);

						}
					}
					
					
					//new opprtunity data storing to couhcbase 
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			// wait a while before checking again for old
			// notifications
			// Thread.sleep(500);
		} catch (Exception sqle) {

			try {
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			sqle.printStackTrace();
		}

	}

	public static void mailToStageProposal(int stage_id, String role,
			String opprunity_id, String referralName, String referralEmail,
			String realtorRole, String realtorName, String realtorEmail,String referralid)
			throws JsonProcessingException {
		log.debug("inside proposal mail method --------------------");
		// String stageNAme = getstageId(stage_id);
		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();

		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;

		for (@SuppressWarnings("rawtypes")
		Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			log.debug("Iniside App For Data");
			Applicants applicants2 = (Applicants) iterator.next();
			log.debug("Applicant data firstName is"
					+ applicants2.getApplicantFirstName());
			log.debug("Applicant data LastName is"
					+ applicants2.getApplicantlastName());
			log.debug("Applicant data Email is"
					+ applicants2.getApplicantEmail());

			++applicantsize;
			if (applicantsize == 1) {
				log.debug("Inside If app1");
				app1 = applicants2;

			} else if (applicantsize == 2) {
				log.debug("Inside If app2");

				app2 = applicants2;
			} else if (applicantsize == 3) {
				log.debug("Inside If app3");

				app3 = applicants2;
			}

		}
		String trackingUrl = "https://videos.visdom.ca/clientA?LeadID="
				+ opprunity_id + "&ContactID=" + app1.getApplicantId() + "&referralid="+referralid;

		String applicantNames = "";
		log.debug("aplicant size " + applicantsize);
		SendWithUsExample sendEmail = new SendWithUsExample();

		try {
			if (applicantsize == 1) {
				applicantNames = app1.getApplicantFirstName();
				sendEmail.getsendWithUsApplicant_CoApplicant(opprunity_id,
						applicantNames, app1.getApplicantEmail(), null,
						trackingUrl);
				log.debug("First Name   "
						+ app1.getApplicantFirstName() + "LastNAme   "
						+ app1.getApplicantlastName());

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();

				sendEmail.getsendWithUsApplicant_CoApplicant(opprunity_id,
						applicantNames, app1.getApplicantEmail(),
						app2.getApplicantEmail(), trackingUrl);
			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app3.getApplicantFirstName() + "";

				sendEmail.getsendWithUsApplicant_CoApplicant(opprunity_id,
						applicantNames, app1.getApplicantEmail(),
						app2.getApplicantEmail(), trackingUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out
				.println("---------------------after applicant Name split-----------------------------===========>");

		log.debug("inside commitement" + role + "referralEmail"
				+ referralEmail);
		try {
			if (realtorEmail == null) {
				realtorEmail = "";
			}

			if (referralEmail == null) {
				referralEmail = "";
			}
			if (referralEmail.trim().equalsIgnoreCase(realtorEmail.trim())) {
				sendEmail.getsendWithUsRefferal(applicantNames, referralEmail,
						referralName);

			} else {
				try {
					sendEmail.getsendWithUsRefferal(applicantNames,
							referralEmail, referralName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendEmail.getsendWithUsRefferal(applicantNames, realtorEmail,
						realtorName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("Email Sended");

	}



	@SuppressWarnings("rawtypes")
	public static void mailToProposalConfirm(String opprunity_id,
			
	JSONObject recomendationJsonData, String role1, String referralName,
			String referralEmail, String realtorRole, String realtorName,
			String realtorEmail) throws NumberFormatException,
			JsonProcessingException, JSONException {
		System.out.println("----------------------inside mailToProposal-------------------");
		log.debug("Inside MailTOProduct");
		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
		String productType=null;
		
		
		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;
		for (Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			Applicants applicants2 = (Applicants) iterator.next();
			++applicantsize;
			if (applicantsize == 1) {
				log.debug("Inside If Applicants 1");

				app1 = applicants2;

			} else if (applicantsize == 2) {
				log.debug("Inside If Applicant 2");
				app2 = applicants2;
			} else if (applicantsize == 3) {
				log.debug("Inside Applicant 3");
				app3 = applicants2;
			}

		}
		log.debug("aplicant size " + applicantsize);

		String applicantNames = "";

		SendWithUsExample sendEmail = new SendWithUsExample();

		try {
			if (applicantsize == 1) {
				applicantNames = app1.getApplicantFirstName();

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();
				log.debug("First Name" + app3.getApplicantFirstName()
						+ "LastNAme" + app3.getApplicantlastName());

			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app3.getApplicantFirstName() + "";
				log.debug("First Name" + app2.getApplicantFirstName()
						+ "LastNAme" + app2.getApplicantlastName());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String lenderName = null;
		play.Logger.info("-------------------product tyepe---------------------------");
		try{
			productType=recomendationJsonData.get("productType").toString();
			play.Logger.info("-------------------product type of ---------------------------"+productType);
			lenderName=recomendationJsonData.get("selectedProductLender").toString();
			play.Logger.info("-------------------lender name---------------------------"+lenderName);
			}catch(Exception e){
				play.Logger.error("Error in get data for selected Product json",e);
			}
		if(productType.equalsIgnoreCase("Combine")){
			
			play.Logger.info("--------------------inside combine -------------------------------------");
			mailtoProposalCombine(opprunity_id,recomendationJsonData,role1,referralName,referralEmail,realtorRole,realtorName,realtorEmail);
		}else{
		
			play.Logger.info("--------------------inside Single -------------------------------------");

		
		// Send Mail TO Refferal

		// send Mail TO Applicant, Product Selection

		SendWithUsExample sendEmail2ProductSelection = new SendWithUsExample();

		// Get Product Name
		String productName = null;
		String mortgage_Type = "";
		String mortgageType = null;
		String term = null;
		float amortization1 = 0;
		float interestRate1 = 0;
		float expectedPayment1 = 0;
			
		try {
			try {
				
				lenderName=recomendationJsonData
						.get("selectedProductLender").toString();
			} catch (Exception e) {
				play.Logger.error("error in getting selectedProductLender data from json ",e);
			}
		
			try{
			productName=recomendationJsonData.get("selectedProductName").toString();
			mortgage_Type = (String) recomendationJsonData.get("selectedProductType");


			term = (String) recomendationJsonData.get("selectedProductTerm");
			}catch(Exception e){
				play.Logger.error("error in getting selectedProductTerm or, selectedProductName or   selectedProductType data from json ",e);

			}
		
		
			try{
				amortization1 =Float.parseFloat( recomendationJsonData.get("maximumAmortization").toString());
			}catch(Exception e){
				play.Logger.error("error in getting maximumAmortization data from json ",e);
			}
		
			try{
			interestRate1 = Float.parseFloat(recomendationJsonData.get("selectedProductRate").toString());
		
			}catch(Exception e){
				play.Logger.error("error in getting selectedProductRate data from json ",e);
			}
			
			expectedPayment1 = Float.parseFloat(recomendationJsonData
					.get("selectedProductPayment").toString());
		
		} catch (Exception e) {
			play.Logger.error("error in getting selectedProductPayment data from json ",e);
		}
		
		if (app2 == null) {

			sendEmail2ProductSelection.getsendWithUsProductSelection(
					applicantNames, lenderName, productName, mortgage_Type,
					term, amortization1 + "", interestRate1 + "",
					expectedPayment1 + "", app1.getApplicantEmail(), null);
		} else {
			sendEmail2ProductSelection.getsendWithUsProductSelection(
					applicantNames, lenderName, productName, mortgageType,
					term, amortization1 + "", interestRate1 + "",
					expectedPayment1 + "", app1.getApplicantEmail(),
					app2.getApplicantEmail());
		}
		
		
		
		}
		
		
		try {
			if (realtorEmail == null) {
				realtorEmail = "";
			}

			if (referralEmail == null) {
				referralEmail = "";
			}
			if (referralEmail.trim().equalsIgnoreCase(realtorEmail.trim())) {
				sendEmail.getsendWithUsReferalProposal(applicantNames,
						referralName, referralEmail, "has", lenderName);
			} else {
				try {
					sendEmail.getsendWithUsReferalProposal(applicantNames,
							referralName, referralEmail, "has", lenderName);
				} catch (Exception e) {

				}
				sendEmail.getsendWithUsReferalProposal(applicantNames,
						realtorName, realtorEmail, "has", lenderName);

			}

		} catch (Exception e) {
			play.Logger.error("error in getting applicant Details  ");
		}

		// Send Mail TO Refferal

		play.Logger.info("single product selected mail sent sucessfully ");

	
	
	}
	public static void mailtoProposalCombine(String opprunity_id,JSONObject recomendationJsonData, String role1, String referralName,

			
					String referralEmail, String realtorRole, String realtorName,
					String realtorEmail) throws JsonProcessingException, JSONException{
		
		play.Logger.info("--------------------------mailtoProposalCombine---------------------------------------------");
		
		Recommendation rec=null;
		try {
			
			rec = new ObjectMapper().readValue(recomendationJsonData.toString(), Recommendation.class);
			play.Logger.info("---------------------------------rec -------------------------"+rec.toString());
		
		}catch(Exception e){
			play.Logger.error("error in mapping selected recomendationJsonData to Recommendation pojo ",e);
		}
		
		
		

		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
		

		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;
		for (Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			Applicants applicants2 = (Applicants) iterator.next();
			++applicantsize;
			if (applicantsize == 1) {
				log.debug("Inside If Applicants 1");

				app1 = applicants2;

			} else if (applicantsize == 2) {
				log.debug("Inside If Applicant 2");
				app2 = applicants2;
			} else if (applicantsize == 3) {
				log.debug("Inside Applicant 3");
				app3 = applicants2;
			}

		}
		log.debug("aplicant size " + applicantsize);

		String applicantNames = "";


		try {
			if (applicantsize == 1) {
				applicantNames = app1.getApplicantFirstName();

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();
				log.debug("First Name" + app3.getApplicantFirstName()
						+ "LastNAme" + app3.getApplicantlastName());

			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app3.getApplicantFirstName() + "";
				log.debug("First Name" + app2.getApplicantFirstName()
						+ "LastNAme" + app2.getApplicantlastName());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// send Mail TO Applicant, Product Selection

		SendWithUsExample sendEmail2ProductSelection = new SendWithUsExample();

			
		if (app2 == null) {
			play.Logger.info("------------------------------getsendWithUsProductSelectionForCombine---for null---------------------------------------");
			sendEmail2ProductSelection.getsendWithUsProductSelectionForCombine(rec,applicantNames, app1.getApplicantEmail(), null);
			} else {
			play.Logger.info("------------------------------getsendWithUsProductSelectionForCombine---for not null---------------------------------------");
			sendEmail2ProductSelection.getsendWithUsProductSelectionForCombine(rec,applicantNames, app1.getApplicantEmail(), app2.getApplicantEmail());
		
		}
		
			play.Logger.info("combine product selected mail sent sucessfully ");

	
	}

	public static void mailToStageCommitment(String role,
			JSONObject referralData, String opprunity_id,
			String referralSourceName, String referralEmail,
			String realtorRole, String realtorName, String realtorEmail) {

		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			Applicants applicants2 = (Applicants) iterator.next();
			++applicantsize;
			if (applicantsize == 1) {

				app1 = applicants2;

			} else if (applicantsize == 2) {
				app2 = applicants2;
			} else if (applicantsize == 3) {
				app3 = applicants2;
			}

		}
		String applicantNames = "";
		log.debug("aplicant size " + applicantsize);
		try {
			if (applicantsize == 1) {
				applicantNames = app1.getApplicantFirstName();

				log.debug("First Name   "
						+ app1.getApplicantFirstName() + "LastNAme   "
						+ app1.getApplicantlastName());

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();

			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app3.getApplicantFirstName() + "";
				log.debug("First Name" + app3.getApplicantFirstName()
						+ "LastNAme" + app2.getApplicantlastName());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("after applicant Name split");
		/*
		 * Applicants applicants = couchBaseOperation
		 * .getApplicantDetials(opprunity_id);
		 */
		log.debug("inside commitement" + role + "referralEmail"
				+ referralEmail);

		SendWithUsExample sendTOReferral_Source = new SendWithUsExample();

		try {
			if (realtorEmail == null) {
				realtorEmail = "";
			}

			if (referralEmail == null) {
				referralEmail = "";
			}
			if (referralEmail.trim().equalsIgnoreCase(realtorEmail.trim())) {
				sendTOReferral_Source.getsendWithUsClient_RefferalSource(
						applicantNames, referralSourceName, referralEmail);
			} else {
				try {
					sendTOReferral_Source.getsendWithUsClient_RefferalSource(
							applicantNames, referralSourceName, referralEmail);
				} catch (Exception e) {
					e.printStackTrace();
				}

				sendTOReferral_Source.getsendWithUsClient_RefferalSource(
						applicantNames, realtorName, realtorEmail);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("Email Sended");

	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public static void mailToStgaeCompensation(String opprunity_id,
			String referralemail, String referralName, JSONObject referralData,
			int referralFee, String lenderName, String date)
			throws JsonProcessingException {

		log.debug("inside compnesationn-------------------"
				+ referralemail + "   ");
		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
		/*
		 * Applicants applicants = couchBaseOperation
		 * .getApplicantDetials(opprunity_id);
		 */
		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;
		for (Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			Applicants applicants2 = (Applicants) iterator.next();
			++applicantsize;
			if (applicantsize == 1) {

				app1 = applicants2;

			} else if (applicantsize == 2) {
				app2 = applicants2;
			} else if (applicantsize == 3) {
				app3 = applicants2;
			}

		}
		String applicantNames = "";
		log.debug("aplicant size " + applicantsize);
		SendWithUsExample sendTOReferral_Source = new SendWithUsExample();

		try {
			if (applicantsize == 1) {
				applicantNames = app1.getApplicantFirstName();
				
				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;
				sendTOReferral_Source.getSendWithCompenstionFileComplete(
						applicantNames, app1.getApplicantEmail(), null,
						lenderName, date,surveyLink1);

				log.debug("First Name   "
						+ app1.getApplicantFirstName() + "LastNAme   "
						+ app1.getApplicantlastName());

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();
				String surveyLink3 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;
				sendTOReferral_Source.getSendWithCompenstionFileComplete(
						app1.getApplicantFirstName(), app1.getApplicantEmail(),
						null, lenderName, date,surveyLink3);

				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app2.getApplicantId() + "&op=" + opprunity_id;
				
				sendTOReferral_Source.getSendWithCompenstionFileComplete(
						app2.getApplicantFirstName(), app2.getApplicantEmail(),
						null, lenderName, date,surveyLink1);
			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app2.getApplicantFirstName() + "";

				String surveyLink3 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;
				sendTOReferral_Source.getSendWithCompenstionFileComplete(
						app1.getApplicantFirstName(), app1.getApplicantEmail(),
						null, lenderName, date,surveyLink3);

				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app2.getApplicantId() + "&op=" + opprunity_id;
				
				sendTOReferral_Source.getSendWithCompenstionFileComplete(
						app2.getApplicantFirstName(), app2.getApplicantEmail(),
						null, lenderName, date,surveyLink1);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// send TO Referral Source
		log.debug("inside compnesationn-------------------"
				+ referralemail + "   ");

		// Name: Referral - Final Congratulations

		sendTOReferral_Source.getsendWithUsReferral_Final_Congratulations(
				applicantNames, referralName, referralemail, referralFee + "");
		log.debug("Email Sended");

	}

	@SuppressWarnings("rawtypes")
	public static void mailStageToPaid(String opprunity_id,
			String referralName, String referallMail, int referralFee,
			String referralId) throws JsonProcessingException {
		CouchBaseOperation couchBaseOperation = new CouchBaseOperation();
		String surveyLink = "https://surveys.visdom.ca/referral?app="
				+ referralId + "&op=" + opprunity_id;

		log.debug("inside paid-----------------------");
		/*
		 * Applicants applicants = couchBaseOperation
		 * .getApplicantDetials(opprunity_id);
		 */
		ArrayList<Applicants> applicantslist1 = couchBaseOperation
				.getApplicants(opprunity_id);

		Applicants app1 = null;
		Applicants app2 = null;
		Applicants app3 = null;
		int applicantsize = 0;
		for (Iterator iterator = applicantslist1.iterator(); iterator.hasNext();) {
			Applicants applicants2 = (Applicants) iterator.next();
			++applicantsize;
			if (applicantsize == 1) {

				app1 = applicants2;

			} else if (applicantsize == 2) {
				app2 = applicants2;
			} else if (applicantsize == 3) {
				app3 = applicants2;
			}

		}
		String applicantNames = "";
		SendWithUsExample sendTOReferral_Source = new SendWithUsExample();

		try {
			if (applicantsize == 1) {
				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;
				applicantNames = app1.getApplicantFirstName();
				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app1.getApplicantFirstName(), surveyLink1,
								app1.getApplicantEmail());

				log.debug("First Name   "
						+ app1.getApplicantFirstName() + "LastNAme   "
						+ app1.getApplicantlastName());

			}

			if (applicantsize == 2) {
				applicantNames = app1.getApplicantFirstName();

				applicantNames += " and " + app2.getApplicantFirstName();

				String surveyLink3 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;

				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app1.getApplicantFirstName(), surveyLink3,
								app1.getApplicantEmail());

				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app2.getApplicantId() + "&op=" + opprunity_id;

				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app2.getApplicantFirstName(), surveyLink1,
								app2.getApplicantEmail());

			}
			if (applicantsize == 3) {
				applicantNames = app1.getApplicantFirstName() + " ";
				applicantNames += app2.getApplicantFirstName();
				applicantNames += " and " + app2.getApplicantFirstName() + "";

				String surveyLink4 = "https://surveys.visdom.ca/client?app="
						+ app1.getApplicantId() + "&op=" + opprunity_id;

				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app1.getApplicantFirstName(), surveyLink4,
								app1.getApplicantEmail());

				String surveyLink1 = "https://surveys.visdom.ca/client?app="
						+ app2.getApplicantId() + "&op=" + opprunity_id;

				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app2.getApplicantFirstName(), surveyLink1,
								app2.getApplicantEmail());

				String surveyLink2 = "https://surveys.visdom.ca/client?app="
						+ app3.getApplicantId() + "&op=" + opprunity_id;

				sendTOReferral_Source
						.getsendWithUsApplicant_Client_FinalCongratulations(
								app3.getApplicantFirstName(), surveyLink2,
								app3.getApplicantEmail());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sendTOReferral_Source.getsendWithUsReferral_Referral_Paid(
				applicantNames, referralName, referallMail, referralFee + "",
				surveyLink);
		log.debug("Email Sended");

	}

	
	public static String getLender(int id) {
		String lenderName = null;
		switch (id) {
		case 12:
			lenderName = "Lendwise";
			break;
		case 13:
			lenderName = "MCAP";
			break;
		case 14:
			lenderName = "Merix";
			break;
		case 18:
			lenderName = "TD";
			break;
		case 11:
			lenderName = "Home Trust";
			break;
		case 17:
			lenderName = "Scotia";
			break;

		case 10:
			lenderName = "First National";
			break;
		case 15:
			lenderName = "Optimum";
			break;
		case 545:
			lenderName = "Alberta Treasury Branch";
			break;
			
		case 463:
			lenderName = "Canadiana";
			break;
		case 16:
			lenderName = "RMG";
			break;
		default:
			lenderName = "Gathering Info";

			break;
		}
		return lenderName;

	}
	public static String getMortgage_Type(int id) {
		String mortgage_Type = null;
		switch (id) {
		case 1:
			mortgage_Type = "LOC";
			break;
		case 2:
			mortgage_Type = "Variable";
			break;
		case 3:
			mortgage_Type = "Fixed";
			break;
		case 4:
			mortgage_Type = "Cashback";
			break;
		case 5:
			mortgage_Type = "Combined";
			break;
		case 6:
			mortgage_Type = "Other";
			break;

		}

		return mortgage_Type;

	}

	public static String getTerm(int id) {
		String term = null;
		switch (id) {
		case 1:
			term = "6 Months";
			break;
		case 2:
			term = "1 Year";
			break;
		case 3:
			term = "2 Year";
			break;
		case 4:
			term = "3 Year";
			break;
		case 5:
			term = "4 Year";
			break;
		case 6:
			term = "5 Year";
			break;

		case 7:
			term = "6 Year";
			break;

		case 8:
			term = "7 Year";
			break;

		case 9:
			term = "8 Year";
			break;

		case 10:
			term = "9 Year";
			break;

		case 11:
			term = "10 Year";
			break;

		case 12:
			term = "Other";
			break;

		}

		return term;

	}

	@SuppressWarnings("unused")
	public static String getlenderName(String id) {

		Connection c = null;
		Statement stmt1 = null;
		try {
			Properties prop = new Properties();
			ArrayList<URI> nodes = new ArrayList<URI>();
			try {

				// getting connection parameter
				prop.load(LeadStageListner.class.getClassLoader()
						.getResourceAsStream("config.properties"));

			} catch (Exception e) {

			}
			Class.forName("org.postgresql.Driver");

			String url = prop.getProperty("postgresURL");
			String userName = prop.getProperty("postgresUserName");
			String userPassword = prop.getProperty("postgresPassword");

			try {
				c = DriverManager.getConnection(url, userName, userPassword);

			} catch (Exception e) {
				url = prop.getProperty("postgresURL1");
				userName = prop.getProperty("postgresUserName1");
				userPassword = prop.getProperty("postgresPassword1");

				c = DriverManager.getConnection(url, userName, userPassword);
			}
			c.setAutoCommit(false);
			log.debug("Opened database successfully");

			stmt1 = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt1
					.executeQuery("select name  from res.partner where id="
							+ id + ";");

			int i = 1;

			while (rs.next()) {
				System.out
						.println("*******Res.partner TABLE Data**************");

				rs.getString("name");

			}
			c.close();
		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;
	}


	public static String getstageId(int stageId) {
		String stageName = null;
		switch (stageId) {
		case 9:
			stageName = "Gathering Info";
			break;

		case 6:
			stageName = "Pending Application";

			break;
		case 7:
			stageName = "Won";

			break;
		case 8:
			stageName = "Expired";

			break;
		case 10:
			stageName = "Partial App ";

			break;

		case 15:
			stageName = "Completed App";

			break;

		case 16:
			stageName = "Credit";

			break;
		case 22:
			stageName = "Lender Submission";

			break;

		case 23:
			stageName = "Commitment";

			break;
		case 24:
			stageName = "Compensation";

			break;
		case 20:
			stageName = "Proposal";

			break;
		case 25:
			stageName = "Paid";

			break;

		case 18:

			stageName = "Awaiting Docs";
			break;

		case 19:

			stageName = "All Product";
			break;
		case 21:

			stageName = "Post Selection";
			break;

		default:
			stageName = "Pending Application";

			break;
		}
		return stageName;

	}
	
	public JSONObject getStageIdDone(int stage_id,JSONObject object) throws JSONException{
		
		switch (stage_id) {
		case 15:
			object.put("task"+11, 11);
			break;
		case 16:
			object.put("task"+11, 11);
			object.put("task"+15, 15);

			break;
		case 22:
			object.put("task"+11, 11);
			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			object.put("task"+20, 20);

			object.put("task"+21, 21);


			break;

		case 23:
			object.put("task"+11, 11);
			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			object.put("task"+20, 20);

			object.put("task"+21, 21);
			object.put("task"+22, 22);


			break;
		case 24:
			object.put("task"+11, 11);
			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			object.put("task"+20, 20);

			object.put("task"+21, 21);
			object.put("task"+22, 22);
			object.put("task"+23, 23);

			break;
		case 20:
			object.put("task"+11, 11);
			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			
			break;
		case 25:
			object.put("task"+11, 11);

			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			object.put("task"+20, 20);

			object.put("task"+21, 21);
			object.put("task"+22, 22);
			object.put("task"+23, 23);
			object.put("task"+24, 24);
			break;

		case 18:

			object.put("task"+11, 11);

			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			
		
			break;

		case 19:

			object.put("task"+11, 11);

			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			
			break;
		case 21:

			object.put("task"+11, 11);

			object.put("task"+15, 15);
			object.put("task"+16, 16);
			object.put("task"+17, 17);
			object.put("task"+18, 18);
			object.put("task"+19, 19);
			object.put("task"+20, 20);

			
			break;

		default:
			break;
		}
		return object;
	}

}
