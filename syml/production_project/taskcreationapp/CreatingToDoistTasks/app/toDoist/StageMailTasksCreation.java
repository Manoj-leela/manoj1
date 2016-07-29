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

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;

import dto.ProjectDetails;
import dto.User;

public class StageMailTasksCreation extends Thread {
	
	private static org.slf4j.Logger log = play.Logger.underlying();

	// static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	static String todoistUser = "Guy";
	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";

	public static void main(String args[]) throws JSONException {
		creatStageMailTasks("627", 16, "lender");
		// taskUpdated("4646896", "+28");
		// test();
	}

	String opportunityId;
	String lenderName;
	int stage_id;
	public StageMailTasksCreation(){
		
	}
	public StageMailTasksCreation(String opportunityId, int stage_id,
			String lenderName) {
		this.opportunityId = opportunityId;
		this.lenderName = lenderName;
		this.stage_id = stage_id;

		try {
			creatStageMailTasks(opportunityId, stage_id, lenderName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("error in stagemailtaskcreation app " + e.getMessage());
		}
	}

	public static String createTasks(String taskName, String projectId,
			String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();


		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId
				+ ",\"priority\": 4" + "   }" + "  }" + "]";

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

	public static String createTasksWithIntendation(String taskName,
			String projectId, String token) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();


		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId
				+ ",\"indent\":\"2\"" + "   }" + "  }" + "]";

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

	

	public static void createNotes(String taskId, String note, String token) {
		log.info(taskId + "niote" + note);
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

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

				// log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info(output1);
				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in stagemailtaskcreation app " + e.getMessage());

		} catch (IOException e) {

			log.error("error in stagemailtaskcreation app " + e.getMessage());

		}

	}

	public static void taskUpdated(String taskId, String date, String assignId)
			throws JSONException {

		UUID uid = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ", \"date_string\": \""
				+ date + "\",\"responsible_uid\":" + assignId
				+ ",\"priority\": 4}}]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));

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
					log.info(output1);

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in stagemailtaskcreation app " + e.getMessage());

		} catch (IOException e) {

			log.error("error in stagemailtaskcreation app " + e.getMessage());

		}

	}

	

	static String assignUser = null;

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

	public static void creatStageMailTasks(String opprunityId, int stage,
			String lenderName) throws JSONException {
		String assignuser = "";
		ArrayList<ProjectDetails> projectDetailsList = getProjectId(opprunityId);
		
		
		try {
			Thread.sleep(6000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("error while processing creatStageMailTasks " + e.getMessage());
		}
		if (projectDetailsList.size() == 0) {
		
			projectDetailsList = getProjectId(opprunityId);
		}
		if (projectDetailsList.size() == 0) {
			getProjectId(opprunityId);
			projectDetailsList = getProjectId(opprunityId);

			try {
				Thread.sleep(6000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("error while processing creatStageMailTasks " + e.getMessage());
			}
			if (projectDetailsList.size() == 0) {
				getProjectId(opprunityId);
				projectDetailsList =getProjectId(opprunityId);
			} else {
				
			}
		} else {
			try {
				
			} catch (Exception e) {
				log.error("error while processing creatStageMailTasks " + e.getMessage());
			}
		}

		int numberOfUsers = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = projectDetailsList.iterator(); iterator.hasNext();) {
			ProjectDetails projectDetails = (ProjectDetails) iterator.next();
			++numberOfUsers;

			if (numberOfUsers < 2) {
				String projectId = projectDetails.getProjectId();
				log.info("inside stage mail---------------project id"
						+ projectId);
				// User user=new
				// CouchBaseOperation().getTodoistUserEmail(todoistUser);
				// token=projectDetails.getToken();
				Properties prop = getNotesFromConfig();
				int time = 0;

				try {
					assignuser = gettodoistUser(projectDetails
							.getAssignedUser());
					log.info("task assigned to the user :" + assignuser);
				} catch (Exception e) {
					log.error("error while processing creatStageMailTasks" + e.getMessage());
				}

				SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm a");

				Calendar cal = Calendar.getInstance();
				@SuppressWarnings("unused")
				String stageName = null;
				String taskId = "0";
				String newTime = "";
				switch (stage) {
				case 1:
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

				case 11:
					stageName = "Completed App";

					break;

				case 16:
					stageName = "Credit";
					String CreditHeading = "@Credit Stage ";

					taskId = createTasks(CreditHeading, projectId, token);
					taskUpdated(taskId, "-1", assignuser);

					/*taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask1"), projectId,
							token, "2", assignuser);*/
				//	taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask2"), projectId,
							token, "2", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask21"), projectId,
							token, "3", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask22"), projectId,
							token, "3", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask23"), projectId,
							token, "3", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask24"), projectId,
							token, "3", assignuser);
				//	taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask25"), projectId,
							token, "3", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("creditstagetask3"), projectId,
							token, "2", assignuser);
					//taskUpdated(taskId, "Today ", assignuser);

					break;
				case 22:
					stageName = "@ Lender Submission";
				
					taskId = createTasks(stageName, projectId,
							token);
					taskUpdated(taskId, "+1", assignuser);

					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage1"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage2"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage21"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage3"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage31"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage4"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage41"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onlcicklendersubmissionstage5"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					break;

				case 23:
					
					stageName = "@ Commitment";
						taskId = createTasks(
							stageName,
							projectId, token);
					taskUpdated(taskId, "Today " + newTime, assignuser);

					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage1"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage111"), token);

					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage11"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
			
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage2"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
			
					
					createNotes(taskId, prop.getProperty("onclickcommitmentStage211"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage212"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage213"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage214"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage215"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage216"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage217"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage218"), token);
					createNotes(taskId, prop.getProperty("onclickcommitmentStage219"), token);

					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage3"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
		
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage4"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
		
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage41"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
		
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage42"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage43"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage44"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage5"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage51"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					createNotes(taskId, prop.getProperty("onclickcommitmentStage511"), token);

					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage52"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
					
					createNotes(taskId, prop.getProperty("onclickcommitmentStage521"), token);

					
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage6"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+1", assignuser);
				

					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage61"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+1", assignuser);
				
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage62"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+2", assignuser);
					
					
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage7"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+2", assignuser);
				
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage71"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+2", assignuser);
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage72"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+2", assignuser);
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage8"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+2", assignuser);
				
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage81"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "+2", assignuser);
					taskId = createTasksWithIntendationTwo(prop.getProperty("onclickcommitmentStage9"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "+2", assignuser);
				
					
					
		

					break;
				case 24:
					stageName = "@ Compensation";
					
					taskId = createTasks(stageName, projectId, token);
					taskUpdated(taskId, "Today " , assignuser);
					taskId = createTasksWithIntendationTwo("Confirm Final Solution information"
							,
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "Today " , assignuser);
					taskId = createTasksWithIntendationTwo("Change stage to Paid when email from Lender confirming deal has been paid "
							,
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "Today " , assignuser);
					
					break;
				case 20:
					stageName = "@Proposal:";
					
					taskId =	createTasks(stageName, projectId, token);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproposalstage1"), projectId,
							token,"2",assignuser);

					taskUpdated(taskId, "Today ", assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproposalstage11"), projectId,
							token,"3",assignuser);

					taskUpdated(taskId, "Today " + newTime, assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproposalstage12"), projectId,
							token,"3",assignuser);

					taskUpdated(taskId, "Today " + newTime, assignuser);

					
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproposalstage121"), projectId,
							token,"4",assignuser);

					taskUpdated(taskId, "Today " + newTime, assignuser);

					
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproposalstage2"), projectId,
							token,"2",assignuser);

					taskUpdated(taskId, "Today " + newTime, assignuser);

					break;
				case 25:
					stageName = "Paid";

					break;
				/*
				 * case 19: stageName = "Lost";
				 * 
				 * break;
				 */

				case 18:

				/*	stageName = "Awaiting Docs";
					String awaitingDoc2 = "Click the All Products Stage to Advance the Opportunity";
					String AwaitinHeading = "@Awaiting Docs Stage";
					time = new Integer(prop.getProperty("onawaitingstagetime")
							.trim());
					taskId = createTasks(AwaitinHeading, projectId, assignuser);
					taskUpdated(taskId, "+" + time + "Hours", assignuser);

					taskId = createTasksWithIntendation(
							prop.getProperty("onawaitingocumentsatge"),
							projectId, token);
					taskUpdated(taskId, "+" + time + "Hours", assignuser);
					taskId = createTasksWithIntendation(awaitingDoc2,
							projectId, token);
					taskUpdated(taskId, "+" + time + "Hours", assignuser);

					break;*/
				/*
				 * case 21:
				 * 
				 * stageName = "Task"; break;
				 */
					break;

				case 19:
					String allPoductHeading = "@All Products:";
				
					taskId = createTasks(allPoductHeading,
							projectId, token);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage1"), projectId,
							token,"2",assignuser);

					taskUpdated(taskId, "Today ", assignuser);
					

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage11"), projectId,
							token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);


					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage111"), projectId,
							token,"4",assignuser);
					taskUpdated(taskId, "Today ", assignuser);


				
				
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage12"), projectId,
							token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage13"), projectId,
							token,"3",assignuser);
				
					
					taskUpdated(taskId, "Today ", assignuser);
					
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onproductstage2"), projectId,
							token,"2",assignuser);
				
					taskUpdated(taskId, "Today ", assignuser);

				

					break;
				case 21:
					// String
					// postSelectStage="Notice to underwriter a product has been selected to be sent out to the lender.";
					// String
					// postSelectStage1="Communicate with Underwriter if clients want to payoff any liabilities prior to closing or from proceeds";
						stageName = "@Post Selection:";
				
					taskId = createTasks(stageName, projectId,
							token);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "Today " , assignuser);
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage1"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage11"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage12"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage13"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage14"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage15"),
							projectId, token,"3",assignuser);
					taskUpdated(taskId, "Today ", assignuser);

				
					taskId = createTasksWithIntendationTwo(
							prop.getProperty("onclickpostselectionstage2"),
							projectId, token,"2",assignuser);
					taskUpdated(taskId, "Today " , assignuser);
				
					
					
					
				break;

				default:
					stageName = "Pending Application";

					break;
				}

			}
		}
	}

	public static Properties getNotesFromConfig() {
		Properties prop = new Properties();

		try {

			// getting get notes content
			prop.load(StageMailTasksCreation.class.getClassLoader()
					.getResourceAsStream("stagemailtaskconfig.properties"));

		} catch (Exception e) {
			log.error("error in stagemailtaskcreation app " + e.getMessage());
		}
		return prop;

	}

	static CouchbaseClient client1 = null;

	public static ArrayList<ProjectDetails> getProjectId(String opprunityID) {
		ArrayList<ProjectDetails> list=new ArrayList<ProjectDetails>();
		try {
			client1 = new CouchBaseOperation().getConnectionToCouchBase();
			DesignDocument designdoc = getDesignDocument("dev_projectid_"
					+ opprunityID);
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "projectid_" + opprunityID) {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {
				ViewDesign view = new ViewDesign("projectid_" + opprunityID,
						"function (doc, meta) {\n" + "if(doc.crmId=="
								+ opprunityID + " )\n"
								+ "{emit(meta.id,null)}\n" +

								"}");

				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			list=	getCocuhbViewData("projectid_"
					+ opprunityID, "projectid_" + opprunityID)	;	
			log.debug("list data "+list);
			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in stagemailtaskcreation app " + e.getMessage());
		}
return list;
	}

	

	private static DesignDocument getDesignDocument(String name) {
		try {
			log.info("Design document with " + name + " exist ? "
					+ client1.getDesignDoc(name));
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}public static  ArrayList<ProjectDetails> getCocuhbViewData(String designDooc, String viewName)
			throws JSONException {
		ArrayList<ProjectDetails> list1 = new ArrayList<ProjectDetails>();
	String projectId="";
	Properties properties=CouchBaseOperation.getCouchbasePropertiesFile();
	 String assginUser="";
			String couchbaseHost="";
			int couchbasePort=0;
			String couchbaseUser="";
			String couchbasePassword="";
			JSONObject jsonObject=null;
	        org.codehaus.jettison.json.JSONArray jsonArray=null;
	        JSONObject getProjectId=null;
			if(properties!=null){
				couchbaseHost=properties.getProperty("couchbaseHost");
				couchbasePort=Integer.parseInt( properties.getProperty("couchbaseHostPort"));
				couchbaseUser=properties.getProperty("couchbaseHostUser");
				couchbasePassword=properties.getProperty("couchbaseHostPassword");
			}
		  CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	                new AuthScope(couchbaseHost, couchbasePort),
	                new UsernamePasswordCredentials(couchbaseUser, couchbasePassword));
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setDefaultCredentialsProvider(credsProvider)
	                .build();
	        
	        try {
	            HttpGet httpget = new HttpGet("http://"+couchbaseHost+":"+couchbasePort+"/syml/_design/dev_"+designDooc+"/_view/"+viewName+"?full_set=true&inclusive_end=true&stale=false&connection_timeout=60000&limit=10&skip=0");

	          log.info("Executing request " + httpget.getRequestLine());
	            CloseableHttpResponse response = httpclient.execute(httpget);
	            try {
	              Logger.info("statu of getting desisndoc "+response.getStatusLine());
	               jsonObject=new JSONObject(EntityUtils.toString(response.getEntity()));
	               jsonArray=jsonObject.getJSONArray("rows");
	            
	            	   getProjectId=jsonArray.getJSONObject(0);
	   			
	   				JSONObject jsonData = new JSONObject(client1.get(getProjectId.getString("id").toString()).toString());
	   				try {
						assginUser = jsonData.getString("assigned_userName");
					} catch (Exception e) {
						log.error("error while processing in getDesignDocument " + e.getMessage());
					}
					JSONObject josnInsidedata = (JSONObject) jsonData
							.get("event_data");
					log.info("datat " + josnInsidedata);
					projectId = josnInsidedata.get("id").toString();
					String leadNAme="";
					try{
					String leadNAmeTosplit=josnInsidedata.getString("name");
					
					String [] leadNameArray=leadNAmeTosplit.split("_");
					leadNAme=leadNameArray[0];
					
					}catch(Exception e){
						log.error("error while processing in getDesignDocument " + e.getMessage());
					}

					try{
						assginUser=josnInsidedata.get("responsible_uid").toString();
					}catch(Exception  e){
						log.error("error while processing in getDesignDocument " + e.getMessage());	
					}
					try {
						User user = new CouchBaseOperation()
								.getTodoistUserEmail(jsonData.get("username")
										.toString());
						token = user.getToken();
					} catch (Exception e) {
						log.error("error while processing in getDesignDocument " + e.getMessage());
					}
					ProjectDetails project = new ProjectDetails();
					project.setLeadName(leadNAme);
					project.setProjectId(projectId);
					project.setAssignedUser(assginUser);
					project.setToken(token);
					list1.add(project);
					log.info("datat " + projectId + " token " + token);
	            } finally {
	                response.close();
	            }
	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
	        	log.error("error while processing in getDesignDocument " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("error while processing in getDesignDocument " + e.getMessage());
			} finally {
	            try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("failed to close http client S "+e.getMessage());
				}
	        }
		client1.shutdown();
		return list1;
	}


	
}
