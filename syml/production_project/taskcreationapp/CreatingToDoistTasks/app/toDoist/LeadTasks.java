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
import java.util.List;
import java.util.UUID;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import dto.User;

public class LeadTasks {
	// static Logger log = LoggerFactory.getLogger(LeadTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();
	public static List<String> listofEmails=new ArrayList<String>();
	// assistant
	public static void main(String[] args) throws JSONException {
		createLeadtasks("626", "Jingxing_Lin", "403-992-8057", "linjx2519@icloud.com",
				"Zhigang_Zhang", "403-233-0703", "info@calgarybbs.ca");
		log.info("todoist User " + gettodoistUser());
		log.debug("" + gettodoistUser());
		
	}

	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	// dale user
	static String todoistUser = null;
	static String daletoken = "181e3a07f6893de7946eaba76982bc0522791bf1";

	// darry1 user
	static String darry1token = "9eeb0908e4998327c65852bf31dfd16cf05c6b82";
	static String assignUserId = null;
	static String assignUserName="Brenda";

	// assign tasks in round robin fashion

	public static String gettodoistUser() throws JSONException {
		// aduery userid
		// assignUserId="4287399";
		// brenda userId
		assignUserId = "5528089";
		/*
		 * CouchBaseOperation couhbase = new CouchBaseOperation(); try {
		 * JSONObject json = new JSONObject(
		 * couhbase.getCouchBaseData("TaskassignUserIds1").toString());
		 * todoistUser = json.get("user").toString();
		 * 
		 * log.info("----------old user task assigned in referr----------->"+
		 * todoistUser); log.debug("todoist user "+todoistUser); if
		 * (todoistUser.trim().equalsIgnoreCase("Audrey")) { todoistUser =
		 * "brenda@visdom.ca"; assignUserId="5528089";
		 * 
		 * json.put("user", "brenda@visdom.ca"); json.put("id", "5528089");
		 * json.put("submitime", new Date());
		 * 
		 * couhbase.storeDataInCouchBase("TaskassignUserIds1", json);
		 * log.info("----------task  assigned in referr----------->"
		 * +todoistUser);
		 * 
		 * log.debug("todoist user "+todoistUser);
		 * 
		 * } else if (todoistUser.trim().equalsIgnoreCase("brenda@visdom.ca")) {
		 * 
		 * todoistUser = "Audrey"; assignUserId="4287399"; json.put("user",
		 * "Audrey"); json.put("id", "4287399"); json.put("submitime", new
		 * Date());
		 * 
		 * couhbase.storeDataInCouchBase("TaskassignUserIds1", json);
		 * log.debug("todoist user if  else "+todoistUser);
		 * log.info("----------task  assigned in referr----------->"
		 * +todoistUser);
		 * 
		 * }else{ json.put("user", "Audrey"); todoistUser = "Audrey";
		 * assignUserId="4287399"; json.put("id", "4287399");
		 * json.put("submitime", new Date());
		 * 
		 * couhbase.storeDataInCouchBase("TaskassignUserIds1", json);
		 * 
		 * log.info("todoist user else "+todoistUser);
		 * log.info("----------task  assigned in lead----------->"+todoistUser);
		 * 
		 * } } catch (Exception e) { JSONObject json = new JSONObject();
		 * json.put("user", "Audrey"); todoistUser = "Audrey";
		 * assignUserId="4287399"; json.put("id", "4287399");
		 * json.put("submitime", new Date());
		 * 
		 * log.info("----------task  assigned in lead----------->"+todoistUser);
		 * 
		 * couhbase.storeDataInCouchBase("TaskassignUserIds1", json);
		 * log.error("error in leadtaskcreation "+e); }
		 */

		return assignUserId;

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
			String leadReferralPhone, String leadReferralEmail)
			throws JSONException {
		String leadfullName = leadName;
		assignUserId = gettodoistUser();
		listofEmails.add("audrey@visdom.ca");
		listofEmails.add("brenda@visdom.ca");

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
		String leadTask1note = "- Set reminder deadline for completed Mortgage Application as discussed with client call. \n- If no email was received confirming email address.  If this email address is correct have client verify the email is not in their spam folder (have them click Not Spam).   \n- If not in spam, get new email address, forward previous sent message with link. \n- Change email address in Business System in Contacts";
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
/*
		ArrayList<User> list = (ArrayList<User>) new CouchBaseOperation()
				.getUsers();*/

		for(String email:listofEmails){
				shareProject(projectId, email, token);
			}


		log.info("project Id-----------------------" + projectId);
		SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm a");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 30);
		String newTime = formatDate.format(cal.getTime());

		taskIdHeading = createTasks(leadHeadingTask, projectId, token,
				assignUserId);

		taskUpdated(taskIdHeading, "-1");
		taskId = createTasksWithIntendationTwo(leadTask1, projectId, token,
				"2", assignUserId);

		createLabel(leadid, token);

		createNotes(taskId, leadTask1Note1, token);
		createNotes(taskId, leadTask1Note2, token);

		String taskId1 = "0";
		taskId = createTasksWithIntendationTwo(leadHeadingTask2, projectId,
				token, "2", assignUserId);

		taskUpdated(taskId, "+3");

		taskId = createTasksWithIntendationTwo(leadtask2, projectId, token,
				"3", assignUserId);

		createTasksWithIntendationTwo(leadtask2, projectId, token, "3",
				assignUserId);

		createTasksWithIntendationTwo(leadtask3, projectId, token, "3",
				assignUserId);

		// createTasksWithIntendationTwo(leadtask2note, projectId, token,"4");

		createLabel(leadid + "_" + leadName, token);

		// createNotes(taskId1, leadtask2note, token);
		String taskId2 = "0";

		taskId2 = createTasksWithIntendationTwo(leadtask5, projectId, token,
				"2", assignUserId);
		createLabel(leadid + "_" + leadReferralName, token);

		createNotes(taskId2, leadtask5note, token);

		// gettodoistUser();
		// User user=new CouchBaseOperation().getTodoistUserEmail(todoistUser);

		/*
		 * assignUserId(taskIdHeading, assignUserId, "+1");
		 * 
		 * assignUserId(taskId, assignUserId, "+1"); assignUserId(taskId1, assignUserId,
		 * "+1"); assignUserId(taskId2, assignUserId, "+1");
		 */

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

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			// log.info(url1);
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

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			// log.info(url1);
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
			// log.debug(URLEncoder.encode(myvar));

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
			 * log.debug(input); OutputStream os =
			 * conn1.getOutputStream(); os.write(input.getBytes()); os.flush();
			 */
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

			URL url1 = new URL("https://todoist.com/API/v6/sync?token=" + token
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
