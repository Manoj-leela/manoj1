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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import play.Logger;
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

import dto.ApplicantDocument;
import dto.ProjectDetails;
import dto.User;

public class CreditInformationTasks {

	ApplicantDocument doclist;
	// static Logger log = LoggerFactory.getLogger(DocumentAnalyzerTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();

	static CouchbaseClient client = null;
	// static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	static String todoistUser = "Guy";
	static String assignUser="5528089";
	


	
	public static String createTasks(String taskName, String projectId,
			String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		log.debug("inside taskss .... \n" + taskName);
		log.debug("inside projectId .... \n" + projectId);
		log.debug("inside token .... \n" + token);

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId + ""
				+ "    }" + "  }" + "]";

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
					log.debug("ouptut " + output1);
					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					taskId = insideJsondata.get(uid.toString()).toString();

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in credit task creation  " + e.getMessage());

		} catch (IOException e) {

			log.error("error in credit task creation  " + e.getMessage());

		}
		return taskId;
	}

	


	public static void taskUpdated(String taskId, String date, String assignId)
			throws JSONException {
		log.info("inside tasks updated .... \n");

		UUID uid = UUID.randomUUID();

		// Logger.debug("uid " + uid);
		// Logger.debug("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ",\"responsible_uid\":"
				+ assignId + ", \"date_string\": \"" + date
				+ "\",\"priority\": 4}}]";

		try {
			log.info(date);

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			// log.info(url1);
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				log.error("Failed : HTTP error code : "
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

			log.error("error in credit task creation  " + e.getMessage());

		} catch (IOException e) {

			log.error("error in credit task creation  " + e.getMessage());

		}

	}

	
	public static String gettodoistUser(String assginUser) throws JSONException {
		/*
		 * try { todoistUser = assginUser; if
		 * (todoistUser.trim().equalsIgnoreCase("Audrey")) {
		 * 
		 * todoistUser = "Audrey"; assignUser = "4287399"; } else if
		 * (todoistUser.trim().equalsIgnoreCase("brenda@visdom.ca")) {
		 * 
		 * todoistUser = "brenda@visdom.ca"; assignUser = "5528089";
		 * 
		 * } else { assignUser = "4287399";
		 * 
		 * } } catch (Exception e) {
		 * 
		 * } try { } catch (Exception e) {
		 * 
		 * }
		 */
		// assignUser="4287399";
		assignUser = "5528089";

		return assignUser;

	}

	public static void createTasksCredit(JSONObject json) throws JSONException {
		String taskid = "0";

		String creditSucesTask = "";
		int numberOfApplicants = 0;
		/*try {

			numberOfApplicants = json.getInt("applicants");
			creditSucesTask = "Credit has been pulled for "
					+ json.getString("applicantName")
					+ ".  Please review their addresses list, "
					+ "incomes list and clean up any duplicated data sent by Equifax.";

		} catch (Exception e) {
log.error("error in gettint applcant or number of applicatnts in credataks method "+e);
		}*/
		String creditFailedtask = "";
		try {
		/*	if (numberOfApplicants == 1) {*/
				creditFailedtask = "We attempted to pull credit for "
						+ json.getString("applicantName")
						+ " however, the credit inquiry failed. Please check the formatting and completeness of the applicant address.  If it is correct,  Please contact "
						+ json.getString("applicantName")
						+ " to verify the accuracy of their Birthhdate, Social Insurance Number and Address."
						+ " Once you are confident of their address formatting, birthday and Social Insurance Number, please change the Opportunity Stage to Partial Application and then  back to Completed App to trigger the retrieval of credit and subsequent task creation.  ";
				/*} else if (numberOfApplicants > 2) {
				creditFailedtask = "We attempted to pull credit for "
						+ json.getString("applicantNamefailed")
						+ " however, the credit inquiry failed. Please check the formatting and completeness of the applicant address.  If it is correct,  Please contact "
						+ json.getString("applicantNamefailed")
						+ " to verify the accuracy of their Birthhdate, Social Insurance Number and Address."
						+ " Once you are confident of their address formatting, birthday and Social Insurance Number, please pull credit directly in the Applicant Record and skip the Credit Stage in OpenERP (if you click credit, it will pull again for both applicants, so this stage can be safely skipped.  There are no additional tasks associated with it.)  ";
			}*/
		} catch (Exception e) {
			// User user=new
			// CouchBaseOperation().getTodoistUserEmail(todoistUser);
			log.error("error while processing in createTasksCredit " + e.getMessage());
		}

		// token=user.getToken();
		String opprunityId = "0";
		try {
			opprunityId = json.get("id").toString();

		} catch (Exception e) {
			log.error("exception in getting opporunity id" + e.getMessage());
		}
		

		ArrayList<ProjectDetails> projectDetailsList = StageMailTasksCreation.getProjectId(opprunityId);

		
		try {
			Thread.sleep(6000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("error in credit task creation  " + e.getMessage());
		}
		if (projectDetailsList.size() == 0) {
			StageMailTasksCreation.getProjectId(opprunityId);
		}
		if (projectDetailsList.size() == 0) {
			StageMailTasksCreation.getProjectId(opprunityId);

			try {
				Thread.sleep(6000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("error in credit task creation  " + e.getMessage());
			}
			if (projectDetailsList.size() == 0) {
				StageMailTasksCreation.getProjectId(opprunityId);
			} else {
				
			}
		} else {
			
		}

		/*
		 * ProjectDetails pro=new ProjectDetails();
		 * pro.setProjectId("145803486");
		 * pro.setToken("19fb71b3edb93f05952f23e0b99120806ec7d224");
		 * 
		 * project.add(pro); //=getProjectId(docList.getOpprtunityId());
		 */// Logger.debug("size" + project.size());
		String assignUser = "";
		int numberOfUser = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator1 = projectDetailsList.iterator(); iterator1.hasNext();) {
			ProjectDetails projectDetails = (ProjectDetails) iterator1.next();

			++numberOfUser;
			String projectId = projectDetails.getProjectId();
			// token =projectDetails.getToken();
			log.info("projectid   -----  " + projectId);
			log.info("token   -----  " + token);
			log.info("assginuser   -----  " + projectDetails.getAssignedUser());

			assignUser = gettodoistUser(projectDetails.getAssignedUser());
			log.info("usser id " + assignUser);
			if (numberOfUser < 2) {

			

					taskid = createTasks(creditFailedtask, projectId.trim(),
							token.trim());

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"HH:mm a");

					
					taskUpdated(taskid, "-1", assignUser); 
					
			}

		}

	}


}
