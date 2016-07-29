package toDoist;

import infrastracture.CouchBaseOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ApplicantDocument;
import dto.ProjectDetails;
import dto.User;

public class DocumentAnalyzerTasks extends Thread {

	ApplicantDocument doclist;
	// static Logger log = LoggerFactory.getLogger(DocumentAnalyzerTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();

	public DocumentAnalyzerTasks(ApplicantDocument doclist) {
		this.doclist = doclist;
	}

	static CouchbaseClient client = null;
	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	static String todoistUser = "Guy";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			createTasksForDocuments(doclist);
		} catch (Exception e) {
			log.error("error while performing createTasksForDocuments" + e.getMessage());
		}
	}


	public static void createNotes(String taskId, String note, String token) {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();
		log.info("inside notes .... \n");

		String myvar = "[{\"type\": \"note_add\", " + "\"temp_id\": \"" + uid
				+ "\"," + " \"uuid\": \"" + uid1 + "\", "
				+ "\"args\": {\"item_id\": " + taskId + ","
				+ " \"content\": \"" + note + "\"}}]";

		try {
			// log.info(URLEncoder.encode(myvar));

			// URL url1 = new URL(
			// "https://todoist.com/API/v6/sync?token=19fb71b3edb93f05952f23e0b99120806ec7d224&commands="
			// + URLEncoder.encode(myvar, "UTF-8"));
			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			// conn1.setRequestProperty("Content-Type", "application/json");

			/*
			 * String
			 * input="{\"token\":\""+token+"\",\"commands\":\""+myvar+"\"}";
			 * log.info(input); OutputStream os = conn1.getOutputStream();
			 * os.write(input.getBytes()); os.flush();
			 */
			if (conn1.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					 log.info(output1);
				}
			}
			conn1.disconnect();

		} catch (MalformedURLException e) {
			log.error("error MalformedURLException "+e.getMessage());
		} catch (IOException e) {

			log.error("error MalformedURLException "+e.getMessage());
		}
	}

	

	public static void taskUpdated(String taskId, String date, String assignId)
			throws JSONException {
		log.info("inside tasks updated .... \n");

		UUID uid = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ",\"responsible_uid\":"
				+ assignId + ", \"date_string\": \"" + date
				+ "\",\"priority\": 4}}]";

		try {
			log.info(date);

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			//log.info(url1);
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		


			if (conn1.getResponseCode() != 200) {
				log.info("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info("task updatedd");
					log.info(output1);

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error  documentanylezertasks "+e.getMessage());

		} catch (IOException e) {

			log.error("error  documentanylezertasks "+e.getMessage());

		}

	}


	static String assignUser = null;

	public static String gettodoistUser(String assginUser) throws JSONException {
	
	/*	try {
			todoistUser = assginUser;
			if (todoistUser.trim().equalsIgnoreCase("Audrey")) {

				todoistUser = "Audrey";
				assignUser = "4287399";
			} else if (todoistUser.trim().equalsIgnoreCase("brenda@visdom.ca")) {

				todoistUser = "brenda@visdom.ca";
				assignUser = "5528089";

			} else {
				assignUser = "4287399";

			}
		} catch (Exception e) {

		}
		try {
		} catch (Exception e) {

		}

		// assignUser="4287399";
*/		assignUser = "5528089";

		return assignUser;

	}

	
	public static String createTasks(String taskName, String projectId,
			String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();
		log.debug("inside taskcreation"+projectId);
		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId + ",\"priority\": 4"
				+ "   }" + "  }" + "]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				
				log.debug("inside Failed"+conn1.getResponseCode());

				log.info("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					// log.info(output1);
					log.debug("inside taskcreation"+output1);

					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					taskId = insideJsondata.get(uid.toString()).toString();

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {
            log.debug("error "+e);
			log.error("error in docuemnt tasks " + e.getMessage());

		} catch (IOException e) {

			log.error("error in docuemnt tasks " + e.getMessage());

		}
		return taskId;
	}

	public static String createTasksWithIntendation(String taskName, String projectId,
			String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId + ",\"indent\":\"2\""
				+ "   }" + "  }" + "]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				log.info("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					// log.info(output1);

					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					taskId = insideJsondata.get(uid.toString()).toString();

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}
		return taskId;
	}
	

	
	public static String createTasksWithIntendationTwo(String taskName,
			String projectId, String token, String indentation, String assignId)
			throws JSONException {
		log.debug("tassk creation ");
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();


		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId
				+ ",\"indent\":\"" + indentation
				+ "\",\"priority\": 4,\"responsible_uid\":" + assignId + ""
				+ "   }" + "  }" + "]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				log.info("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					// log.info(output1);

					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					taskId = insideJsondata.get(uid.toString()).toString();

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}
		return taskId;
	}
	static CouchbaseClient client1 = null;

	public static void createTasksForDocuments(ApplicantDocument docList)
			throws JSONException {
		client1 = new CouchBaseOperation().getConnectionToCouchBase();

		Properties prop = getNotesFromConfig();
			String taskid = "0";
		String opprunityId = "0";
		log.debug("Inside createTasksForDocuments method ");
		try {
			opprunityId = docList.getOpprtunityId();

		} catch (Exception e) {
			log.info("error in getting opporunity ID " + e);
		}
		String documentsHeading="@ Awaiting Documents:";

		ArrayList<ProjectDetails> projectDetailsList = StageMailTasksCreation.getProjectId(opprunityId);
	
		try {
			Thread.sleep(6000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("error   deleting view "+e.getMessage());
		}
		if (projectDetailsList.size() == 0) {
			
			projectDetailsList = StageMailTasksCreation.getProjectId(opprunityId);
		}
		if (projectDetailsList.size() == 0) {
			
			projectDetailsList = StageMailTasksCreation.getProjectId(opprunityId);

			try {
				Thread.sleep(6000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("error  documentanylezertasks "+e.getMessage());
			}
			if (projectDetailsList.size() == 0) {
				
				projectDetailsList = StageMailTasksCreation.getProjectId(opprunityId);
			} else {
				try {
					client1 = new CouchBaseOperation()
							.getConnectionToCouchBase();
					client1.deleteDesignDoc("dev_projectid_" + opprunityId);
					client1.shutdown(9000l, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					log.error("error   deleting view "+e.getMessage());
				}
			}
		} else {
			try {
				client1 = new CouchBaseOperation().getConnectionToCouchBase();
				client1.deleteDesignDoc("dev_projectid_" + opprunityId);
				client1.shutdown(9000l, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				log.error("error  deleting view  "+e.getMessage());
			}
		}
		
		log.debug(" doclist data "+ projectDetailsList);

		/*
		 * ProjectDetails pro=new ProjectDetails();
		 * pro.setProjectId("145803486");
		 * pro.setToken("19fb71b3edb93f05952f23e0b99120806ec7d224");
		 * 
		 * project.add(pro); //=getProjectId(docList.getOpprtunityId());
		 */// log.info("size" + project.size());
		String assignUser = "";
		int numberOfUser = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator1 = projectDetailsList.iterator(); iterator1
				.hasNext();) {
			ProjectDetails projectDetails = (ProjectDetails) iterator1.next();

			++numberOfUser;
			String leadName="@";
			
			leadName= leadName+projectDetails.getLeadName();

			String projectId = projectDetails.getProjectId();
			// token =projectDetails.getToken();
			log.info("projectid   -----  " + projectId);
			log.info("token   -----  " + token);

		assignUser = gettodoistUser(projectDetails.getAssignedUser());

			log.info("assginuser   -----  " + assignUser);

		

			if (numberOfUser < 2) {
				log.info("task creation metho");

				taskid = createTasks(documentsHeading, projectId, token);

				taskUpdated(taskid, "+2" , assignUser);
				
				 createTasksWithIntendationTwo(
						prop.getProperty("creditstagetask1"), projectId,
						token, "2", assignUser);
			
				//taskUpdated(taskId, "Today ", assignUser);

				/*taskId = createTasksWithIntendationTwo(
						prop.getProperty("creditstagetask3"), projectId,
						token, "2", assignUser);*/
				//taskUpdated(taskId, "Today ", assignuser);

		//	taskid = createTasksWithIntendationTwo("Documents Required", projectId, token,"2",assignUser);
				//taskUpdated(taskid, "+3" , assignUser);

			if (!docList.getDocuments().isEmpty()) {
				String taskId="0";
					Set<String> data = docList.getDocuments();

					Iterator<String> iterator = data.iterator();
					while (iterator.hasNext()) {
						String docVal = iterator.next();

						taskId = createTasksWithIntendationTwo(leadName +"_"+docList.getFirstName() + " "
								+ docList.getLastName() + "-" + docVal,
								projectId, token,"2",assignUser);
					//	taskUpdated1(taskId, "+3", assignUser);

						addingNotesContent(taskId, docVal);

						try {
							String[] mortgageVal = docVal.split("\\(");
							switch (mortgageVal[0]) {
							case "Mortgage Statement":
								createNotes(taskId,
										prop.getProperty("mortgage"), token);
								break;

							case "Property Tax Assessment":
								createNotes(taskId,
										prop.getProperty("property"), token);
								break;

							default:
								break;
							}

						} catch (Exception e) {
							log.error("error  documentanylezertasks "+e.getMessage());

						}

					}
					taskId = createTasksWithIntendationTwo(leadName +"_"+
							docList.getFirstName() + " "
									+ docList.getLastName()
									+ "-Confirmation of Taxes Paid", projectId,
							token,"2",assignUser);
					//taskUpdated1(taskId, "+3", assignUser);

					addingNotesContent(taskId, docList.getFirstName() + " "
							+ docList.getLastName()
							+ "-Confirmation of Taxes Paid");

				}
				if (!docList.getCo_documents().isEmpty()) {
					String taskId="0";
					Set<String> data = docList.getCo_documents();

					Iterator<String> iterator = data.iterator();

					while (iterator.hasNext()) {
						String docVal = iterator.next();
						taskId = createTasksWithIntendationTwo(leadName +"_"+docList.co_firstName + " "
								+ docList.co_lastName + "-" + docVal,
								projectId, token,"2",assignUser);
					//	taskUpdated1(taskId, "+3", assignUser);

						addingNotesContent(taskId, docVal);

						try {
							String[] mortgageVal = docVal.split("\\(");
							switch (mortgageVal[0]) {
							case "Mortgage Statement":
								createNotes(taskId,
										prop.getProperty("mortgage"), token);
								break;

							case "Property Tax Assessment":
								createNotes(taskId,
										prop.getProperty("property"), token);
								break;

							default:
								break;
							}

						} catch (Exception e) {
							log.error("error  documentanylezertasks "+e.getMessage());

						}

					}
					taskId = createTasksWithIntendationTwo(leadName +"_"+docList.co_firstName + " "
							+ docList.co_lastName
							+ "-Confirmation of Taxes Paid", projectId, token,"2",assignUser);
				//	taskUpdated1(taskId, "+3", assignUser);

					addingNotesContent(taskId, docList.co_firstName + " "
							+ docList.co_lastName
							+ "-Confirmation of Taxes Paid");

				}
				if (docList.getDownPayments().size() != 0
						&& !docList.getDownPayments().isEmpty()) {
					Set<String> data = docList.getDownPayments();
					String taskId="0";
					Iterator<String> iterator = data.iterator();
					
					while (iterator.hasNext()) {
						String docVal = iterator.next();

						taskId = createTasksWithIntendationTwo(leadName +"_"+docVal, projectId, token,"2",assignUser);
						//taskUpdated1(taskId, "+3", assignUser);

						addingNotesContent(taskId, docVal);

					}
				}

				if (docList.getApplicantCreditDocuments().size() != 0
						&& !docList.getApplicantCreditDocuments().isEmpty()) {
					Set<String> data = docList.getApplicantCreditDocuments();
					String taskId="0";
					Iterator<String> iterator = data.iterator();
				
					while (iterator.hasNext()) {
						String docVal = iterator.next();

						taskId = createTasksWithIntendationTwo(leadName +"_"+docList.firstName + " "
								+ docList.lastName + "_" + docVal, projectId,
								token,"2",assignUser);
					//	taskUpdated1(taskId, "+3", assignUser);

						// addingNotesContent(taskId, docVal);

					}
				}

				if (docList.getCo_applicantCreditDocuments().size() != 0
						&& !docList.getCo_applicantCreditDocuments().isEmpty()) {
					Set<String> data = docList.getCo_applicantCreditDocuments();
					String taskId="0";
					Iterator<String> iterator = data.iterator();
					
					while (iterator.hasNext()) {
						String docVal = iterator.next();

						taskId = createTasksWithIntendationTwo(leadName +"_"+docList.co_firstName + " "
								+ docList.co_lastName + "_" + docVal,
								projectId, token,"2",assignUser);
						//taskUpdated1(taskId, "+3", assignUser);

						// addingNotesContent(taskId, docVal);

					}
				}

				if (docList.getPropertyDocments().size() != 0
						&& !docList.getPropertyDocments().isEmpty()) {
					Set<String> data = docList.getPropertyDocments();
					String taskId="0";
					Iterator<String> iterator = data.iterator();

					while (iterator.hasNext()) {

						String docVal = iterator.next();

						taskId = createTasksWithIntendationTwo(leadName +"_"+docVal, projectId, token,"2",assignUser);
						//taskUpdated1(taskId, "+3", assignUser);

						addingNotesContent(taskId, docVal);

					}
				}
				
				taskid = createTasksWithIntendationTwo("Enter all fields into OpenERP confirmed from documents received ", projectId, token,"2",assignUser);
				//taskUpdated(taskid, "+3" , assignUser);

				taskid = createTasksWithIntendationTwo("Once the documents are received, they are split and saved in document analyzer", projectId, token,"3",assignUser);
			//	taskUpdated(taskid, "+3" , assignUser);

				taskid = createTasksWithIntendationTwo("Move to ALL PRODUCTS Stage", projectId, token,"3",assignUser);
				//taskUpdated(taskid, "+3" , assignUser);
	
				
			}

		}

	}

	
	
	public static void addingNotesContent(String taskId, String documentName) {
		log.info("note content");
		Properties prop = getNotesFromConfig();
		try {
			Calendar year1 = Calendar.getInstance();
			int currentMonth = year1.get(Calendar.MONTH) + 1;

			String currentYearNoa = "";
			String lastYearNoa = "";
			String lastSecondYesrNoa = "";
			if (currentMonth >= 3 && currentMonth < 5) {

				int startYear = year1.get(Calendar.YEAR) - 1;

				currentYearNoa = startYear + " T4";

				int priorYear = startYear - 1;
				lastYearNoa = priorYear + " Notice of Assessment";

				int priorYear2 = startYear - 2;
				lastSecondYesrNoa = priorYear2 + " Notice of Assessment";

			} else if (currentMonth >= 5) {
				int startYear = year1.get(Calendar.YEAR) - 1;

				lastYearNoa = startYear + " Notice of Assessment";
				int priorYear = startYear - 1;
				lastSecondYesrNoa = priorYear + " Notice of Assessment";

			}

			if (documentName.equalsIgnoreCase(currentYearNoa)) {
				createNotes(taskId, prop.getProperty("noa"), token);

			}
			if (documentName.equalsIgnoreCase(lastYearNoa)) {
				createNotes(taskId, prop.getProperty("noa"), token);
			}

			if (documentName.equalsIgnoreCase(lastSecondYesrNoa)) {
				createNotes(taskId, prop.getProperty("noa"), token);
			}

			switch (documentName) {
			case "December 31st Pay stub showing Year to date income":
				createNotes(taskId, prop.getProperty("noaDecember"), token);

				break;

			case "Letter of Employment":
				createNotes(taskId, prop.getProperty("letterofemployeement"),
						token);

				break;

			case "Applicant-Confirmation of Taxes Paid":
				createNotes(taskId, prop.getProperty("tax"), token);

				break;
			case "Co-Applicant-Confirmation of Taxes Paid":
				createNotes(taskId, prop.getProperty("tax"), token);

				break;

			case "Paystub":
				createNotes(taskId, prop.getProperty("paystub"), token);

				break;

			case "Divorce/Separation Agreement":
				createNotes(taskId, prop.getProperty("divorce"), token);

				break;
			case "Child Tax Credit":
				createNotes(taskId, prop.getProperty("childTax"), token);

				break;
			case "90 Days history bank statements":
				createNotes(taskId, prop.getProperty("bank"), token);

				break;
			case "90 Days history Investment":
				createNotes(taskId, prop.getProperty("rrsp"), token);

				break;

			case "Gift Letter":
				createNotes(taskId, prop.getProperty("giftletter"), token);

				break;

			case "Sale MLS Listing":
				createNotes(taskId, prop.getProperty("mls"), token);

				break;
			case "Offer to Sale of Existing":
				createNotes(taskId, prop.getProperty("purchase"), token);

				break;
			default:
				break;
			}

		} catch (Exception e) {
			log.error("error  documentanylezertasks "+e.getMessage());
		}

	}

	public static Properties getNotesFromConfig() {
		Properties prop = new Properties();

		try {

			// getting get notes content
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("taskconfig.properties"));

		} catch (Exception e) {
			log.error("error  documentanylezertasks "+e.getMessage());
		}
		return prop;

	}

	

	
	
	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, IOException, JSONException {

		



String myvar = "{"+
"  \"firstName\": \"Brad \","+
"  \"lastName\": \"Pitt\","+
"  \"emailAddress\": \"guy@visdom.ca\","+
"  \"documents\": ["+
"    \"Copy of Photo ID (Driver's Licence or Passport)\","+
"    \"Current Paystub\","+
"    \"2013 Notice of Assessment\","+
"    \"Letter of Employment\","+
"    \"2014 Notice of Assessment\""+
"  ],"+
"  \"applicantCreditDocuments\": ["+
"    \"Bankruptcy Document\","+
"    \"Discharge Certificate\","+
"    \"Orderly Debt Payment (ODP) Documents\","+
"    \"Confirmation of paid collection with teacher\","+
"    \"Explanation of collection with teacher\","+
"    \"Explanation for late payments\""+
"  ],"+
"  \"co_applicantCreditDocuments\": [],"+
"  \"co_firstName\": null,"+
"  \"co_lastName\": null,"+
"  \"co_emailAddress\": null,"+
"  \"co_documents\": [],"+
"  \"propertyDocments\": ["+
"    \"MLS Listing (246 Snowberry Dr. SW, Calgary)\","+
"    \"Sale MLS Listing\","+
"    \"Offer to Purchase (246 Snowberry Dr. SW, Calgary)\""+
"  ],"+
"  \"downPayments\": [],"+
"  \"lendingGoal\": \"Purchase\","+
"  \"opprtunityId\": \"439\","+
"  \"Type\": \"DocMaster\","+
"  \"Type_DocMaster\": \"DocMaster\","+
"  \"CrmURL\": \"439\","+
"  \"DocMasterUrl\": \"\","+
"  \"masterdoc_opporunityName\": \"Brad  Pitt - 246 Snowberry Dr. SW, Calgary, First, 2015-08-05 20\","+
"  \"Submission_Date_Time1b\": \"2015/12/02 14:21:54\""+
"}";
	

	


		ObjectMapper mapper = new ObjectMapper();
		dto.ApplicantDocument docList = mapper.readValue(myvar,
				dto.ApplicantDocument.class);
		new DocumentAnalyzerTasks(docList).start();
		// .createTasksForDocuments(docList);

	}
}