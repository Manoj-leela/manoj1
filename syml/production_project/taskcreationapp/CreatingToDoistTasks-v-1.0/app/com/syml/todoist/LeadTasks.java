package com.syml.todoist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.syml.ReadConfigurationFile;
import com.syml.TodoistConstants;
import com.syml.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.dto.User;

public class LeadTasks {
	// static Logger log = LoggerFactory.getLogger(LeadTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();
	// assistant
	public static void main(String[] args) throws JSONException, IOException, TaskCreationException, CouchbaseDaoServiceException {
		createLeadtasks("626", "test_test", "403-992-8057", "test@icloud.com",
				"test_Zhang", "403-233-0703", "info@calgarybbs.ca");
	
		
	}

	static String token = "";

	static String todoistUser = null;
	
	static String assignUserId = null;
	static String assignUserName="Brenda";
	static String assignUser = null;

	//assign tasks in round robin fashion 
		public static String gettodoistUser() throws IOException, TaskCreationException {
				Properties properties=	ReadConfigurationFile.readConfigProperties();
				if(properties==null)
					throw new TaskCreationException("ERROR  in getting configuration file ");
				assignUser=properties.getProperty(TodoistConstants.TASK_ASSIGN_USER_ID);
				
			return assignUser;

		}
	

	public static String createProject(String projectName, String token)
			throws JSONException {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();
		log.info("project " + projectName);

		String projectId = null;
		log.info("uid " + uid);
		log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"project_add\", " + "\"temp_id\": \""
				+ uid + "\"," + " \"uuid\": \"" + uid1 + "\","
				+ " \"args\": {\"name\": \"" + projectName + "\"}}]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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
					// log.info(output1);
					JSONObject jsondata = new JSONObject(output1);
					JSONObject insideJsondata = (JSONObject) jsondata
							.get("TempIdMapping");
					projectId = insideJsondata.get(uid.toString()).toString();
				}

			}
			// createLabels(projectName, token);
			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}
		return projectId;
	}

	public static void createLeadtasks(String leadid, String leadName,
			String leadPhone, String leadEmial, String leadReferralName,
			String leadReferralPhone, String leadReferralEmail) throws TaskCreationException
			 {
		String leadfullName = leadName;
		try {
			assignUserId = gettodoistUser();
			token=ReadConfigurationFile.getUserToken();
			ArrayList<User> listOfTodoistUser=(ArrayList<User>) ReadConfigurationFile.getTodoistUserDetails();

	

		try {
			String[] str = leadName.split("_");
			leadName = str[0] + " " + str[1];
		} catch (Exception e) {
			log.error("error while processing  createLeadtasks" + e.getMessage());
		}
		String leadHeadingTask = "@ New Lead from (" + leadReferralName + ")";
		String leadHeadingTask2 = "Mortgage Application Deadline";
		String leadTask1 = "Call "
				+ leadName
				+ " at "
				+ leadPhone
				+ " and leadmail Id is "
				+ leadEmial
				+ " referral source mail id is "
				+ leadReferralEmail
				+ ",  to ensure email has been received and it did not go into spam. Welcome to Visdom and assist with the application process if needed. Get an estimated time when they will have the mortgage application completed.";
		//String leadTask1note = "- Set reminder deadline for completed Mortgage Application as discussed with client call. \n- If no email was received confirming email address.  If this email address is correct have client verify the email is not in their spam folder (have them click Not Spam).   \n- If not in spam, get new email address, forward previous sent message with link. \n- Change email address in Business System in Contacts";
		String leadTask1Note1 = "Hello "
				+ leadfullName
				+ ", this is "
				+ assignUserName
				+ " with Visdom Mortgage Solutions. "
				+ leadReferralName
				+ " has referred you and we wanted to call to let you know an online mortgage application has been sent to your email address on file.  If it’s currently not in your inbox, please double check your spam folder as it will depend on your email provider and how they filter emails.  If you have not received the mortgage application, please contact me at (your direct number) so we can offer assistance.  We look forward to getting your mortgage application! Have a great (day/evening)!";
		String leadTask1Note2 = "Hello "
				+ leadfullName
				+ " this is "
				+ assignUserName
				+ " with Visdom Mortgage Solutions. "
				+ leadReferralName
				+ " has referred you and we wanted to call to let you know an online mortgage application has been sent to your email address on file.  If it’s currently not in your inbox, please double check your spam folder as it will depend on your email provider and how they filter emails. -"

				+ "\n\n(Confirm Email Address : In the event they did not get the email, look for the email address it was sent to and confirm with client the email address.  If Address is correct, have them check spam and click Not Spam.  If not in Spam, get another email address from them and forward the sent message with the link to the new email they provided you.  Change Contact’s email address in Business System to new address)";

		String leadtask2 = "Find out from the client as to when they will be able to have the application completed";
		String leadtask3 = "Follow up call & email as Application still hasn’t been received. To ensure the Mortgage Application has been received. Assign due date.";

		String leadtask5 = "Call & Email  "
				+ leadReferralName
				+ " at "
				+ leadReferralPhone
				+ " and emailId  is "
				+ leadReferralEmail
				+ ",  informing we have not received the application yet and ask them to influence client to send application in. Assign Due Date after 3 attempt";
		String leadtask5note = "Step 1 - Follow up in 6 hours \nStep 2 - Follow up in 2 days \n Step 3 - Follow up in 7 days \nStep 4 - 10 day Call referral (if realtor) \nStep 5 - 14 days Move to lost";
		String projectId = "0";
		String taskId = "0";
		String taskIdHeading = "0";
		projectId = createProject(leadfullName + "_" + leadid, token);


		for(User user:listOfTodoistUser){
				shareProject(projectId, user.getEmail(), token);
			}


		log.info("project Id-----------------------" + projectId);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 30);

		taskIdHeading = createTasks(leadHeadingTask, projectId, token,
				assignUserId);

		taskUpdated(taskIdHeading, "-1");
		taskId = createTasksWithIntendationTwo(leadTask1, projectId, token,
				"2", assignUserId);

		createLabel(leadid, token);

		createNotes(taskId, leadTask1Note1, token);
		createNotes(taskId, leadTask1Note2, token);

		taskId = createTasksWithIntendationTwo(leadHeadingTask2, projectId,
				token, "2", assignUserId);

		taskUpdated(taskId, "+3");

		taskId = createTasksWithIntendationTwo(leadtask2, projectId, token,
				"3", assignUserId);

		createTasksWithIntendationTwo(leadtask2, projectId, token, "3",
				assignUserId);

		createTasksWithIntendationTwo(leadtask3, projectId, token, "3",
				assignUserId);


		createLabel(leadid + "_" + leadName, token);

		// createNotes(taskId1, leadtask2note, token);
		String taskId2 = "0";

		taskId2 = createTasksWithIntendationTwo(leadtask5, projectId, token,
				"2", assignUserId);
		createLabel(leadid + "_" + leadReferralName, token);

		createNotes(taskId2, leadtask5note, token);

		} catch (IOException | TaskCreationException e1) {
			throw new TaskCreationException("Error creating lead tasks ",e1 );
		} catch (JSONException e) {
			throw new TaskCreationException("Error creating lead tasks ",e );

		} catch (CouchbaseDaoServiceException e) {
			throw new TaskCreationException("Error creating lead tasks ",e );

		}

	}

	public static String createTasks(String taskName, String projectId,
			String token, String assignId) throws JSONException {
		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId
				+ ",\"priority\": 4,\"responsible_uid\":" + assignId + ""
				+ "   }" + "  }" + "]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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

	public static void taskUpdated(String taskId, String date)
			throws JSONException {

		UUID uid = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ", \"date_string\": \""
				+ date + "\",\"priority\": 4}}]";

		JSONArray jsonArray = new JSONArray();
		JSONObject obj = new JSONObject();
		JSONObject obj2 = new JSONObject();
		obj2.put("id", taskId);
		obj2.put("date_string", date);
		obj.put("type", "item_update");
		obj.put("uuid", uid);
		obj.put("args", obj2);
		jsonArray.put(obj);

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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
					log.info(output1);

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}

	}

	public static void assignUser(String taskId, String assignId, String date)
			throws JSONException {

		UUID uid = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ",\"responsible_uid\":"
				+ assignId + ",\"priority\": 4}}]";

		try {
			// log.info(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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
					log.info(output1);

				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}

	}

	public static void createLabel(String label, String token)
			throws JSONException {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		log.info("uid " + uid);
		log.info("uid 2" + uid1);

		String myvar = "[{\"type\": \"label_add\"," + " \"temp_id\": \"" + uid
				+ "\"," + " \"uuid\": \"" + uid1 + "\", "
				+ "\"args\": {\"name\": \"" + label + "\"}}]";

		try {
			// log.debug(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
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
			// createLabels(projectName, token);
			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}

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
				URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				// log.debug("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info(output1);
				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}

	}

	public static void shareProject(String projectId, String email, String token) {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();
		log.info("email " + email + "" + projectId);

		String myvar = "[{\"type\": \"share_project\"," + " \"temp_id\": \""
				+ uid + "\"," + " \"uuid\": \"" + uid1 + "\","
				+ " \"args\": {\"project_id\": \"" + projectId + "\","
				+ " \"message\": \"\", \"email\": \"" + email + "\"}}]";

		try {
			// log.debug(URLEncoder.encode(myvar));

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

			if (conn1.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			} else {

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						(conn1.getInputStream())));

				// log.debug("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info(output1);
				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in leadtaskcreation " + e.getMessage());

		}

	}

}
