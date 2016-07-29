package toDoist;

import infrastracture.CouchBaseOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;

import dto.ProjectDetails;
import dto.User;

public class ReferralTasks extends Thread {
	// static Logger log = LoggerFactory.getLogger(ReferralTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();
	public static List<String> listofEmails=new ArrayList<String>();
	// assistant

	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	// dale user
	static String daletoken = "181e3a07f6893de7946eaba76982bc0522791bf1";

	static String todoistUser = "Guy";

	String referralName;
	String phoneNumber;
	String referralEmailId;
	String referredBy;

	public ReferralTasks(String referralName, String phoneNumber,	String referralEmailId,String referredBy) {
		this.referralName = referralName;
		this.phoneNumber = phoneNumber;
		this.referralEmailId=referralEmailId;
		this.referredBy=referredBy;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			createReferralproject(referralName, phoneNumber,referralEmailId,referredBy);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("error in referraltaskcreation " + e.getMessage());
		}
	}

	public static void main(String[] args) throws JSONException {
	createReferralproject("Vijay","9786767777","","");
		log.info("getTodoist User" + gettodoistUser());
		// ArrayList list= getProjectId();
		log.debug("helloooooooooooooooooooooooo");
	///log.debug(	createTasks("test", "148450637", token, "5528089"));
log.debug(""+gettodoistUser());
		log.debug("size " + list.size());
	}

	static String assignUser = null;
//assign tasks in round robin fashion 
	public static String gettodoistUser() throws JSONException {

		//aduery userid
				//assignUser="4287399";
		//brenda userId
				assignUser="5528089";

/*		CouchBaseOperation couhbase = new CouchBaseOperation();
		try {
			JSONObject json = new JSONObject(couhbase.getCouchBaseData(
					"TaskAssignusers12").toString());
			todoistUser = json.get("user").toString();

			log.info("----------old user task assigned in referr----------->"
					+ todoistUser);
			log.debug("todoist user " + todoistUser);
			if (todoistUser.trim().equalsIgnoreCase("Audrey")) {
				todoistUser = "brenda@visdom.ca";
				assignUser = "5528089";

				json.put("user", "brenda@visdom.ca");
				json.put("id", "5528089");
				json.put("submitime", new Date());

				couhbase.storeDataInCouchBase("TaskAssignusers12", json);
				log.info("----------task  assigned in referr----------->"
						+ todoistUser);

				log.debug("todoist user " + todoistUser);

			} else if (todoistUser.trim().equalsIgnoreCase("brenda@visdom.ca")) {

				todoistUser = "Audrey";
				assignUser = "4287399";
				json.put("user", "Audrey");
				json.put("id", "4287399");
				json.put("submitime", new Date());

				couhbase.storeDataInCouchBase("TaskAssignusers12", json);
				log.debug("todoist user if  else " + todoistUser);
				log.info("----------task  assigned in referr----------->"
						+ todoistUser);

			} else {
				json.put("user", "Audrey");
				todoistUser = "Audrey";
				assignUser = "4287399";
				json.put("id", "4287399");
				json.put("submitime", new Date());

				couhbase.storeDataInCouchBase("TaskAssignusers12", json);

				log.info("todoist user else " + todoistUser);
				log.info("----------task  assigned in referr----------->"
						+ todoistUser);

			}
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("user", "Audrey");
			todoistUser = "Audrey";
			assignUser = "4287399";
			json.put("id", "4287399");
			json.put("submitime", new Date());

			log.info("----------task  assigned in referr----------->"
					+ todoistUser);

			couhbase.storeDataInCouchBase("TaskAssignusers12", json);
			log.error("error in referraltaskcreation " + e);
		}
	*/

		return assignUser;

	}

	public static void createReferralproject(String referralname,
			String phoneNumber,String email,String referedBy) throws JSONException {
		String projectId = null;
		assignUser = gettodoistUser();
		listofEmails.add("audrey@visdom.ca");
		listofEmails.add("brenda@visdom.ca");
		String		referraltaskHEading="@Referral Source Sign up";
		String referraltask1 = "Call  "+ referralname+" at "+ phoneNumber+ " "+ email+" referredBy  "+ referedBy +" to welcome to Visdom and offer assistance with mobile link setup.";
		String referralNote1 = "- Use Script for leaving a voice message:";
		String referralNote11="Hello "+referralname +", this is "+assignUser+" with Visdom Mortgage Solutions. "+referedBy +" has referred you and we wanted to call to thank you for joining our referral team."
				+"If you could kindly contact me at "+phoneNumber+" (your direct number)(say phone number slowly) so we can help  with setting up our referral app on your phone. Again, I may be reached at "+phoneNumber+", and ask for _________.  I look forward speaking with you and thanks in advance for returning my call. Have a great _____ ";
		String referralNote12="Use Script for person to person call:";
		String referralNote121="Hello "+referralname+", this is "+ assignUser+" with Visdom Mortgage Solutions.  "+referedBy+" has referred you and we wanted to call to thank you for joining our referral team.  Is now a good time to set up our referral app on your phone? Great, okay! ";
		String referralNote122="If No:	\n\nWhen would be a good time for us to start making you money and set up the link on your phone?";
		String referralNote123="Do you have an iphone or android? (depending on reply follow the instructions)"
		+ "Android: To make this process quick and simple on your Android phone, please take a moment to do the following steps:\n"
				+ "1) Using your phone, click this weblink to open the Visdom Referrals Page in your phone Browser Visdom Referrals Form \n\n"
				+ "2) Press the Menu button and select Bookmarks.\n\n"
				+ "3) Select the top left thumbnail labeled Add.\n\n"
				+ "4) Name the bookmark Visdom Referrals and click OK to add it.\n\n"
				+ "5) Press and hold on the Visdom Referrals bookmark you just created, and select Add shortcut to Home. This will put the Visdom Referrals shortcut icon on your Home screen to allow one-touch access.\n\n"
				+"Iphone: To make this process quick and simple on your iPhone, please take a moment to do the following steps:\n\n\n"
				+"1) Using your phone, click this weblink to open the Visdom Referrals Page in Safari Visdom Referrals Form .\n\n"
				+ "2) At the bottom of the screen you'll see an icon depicting an arrow that looks like it's trying to get away from a square. Tap this button and you'll have a few options.\n\n"
				+ "3) Tap the 'Add to Home Screen' button.  You'll be asked to choose a name for the homescreen icon.\n\n"
				+ "4) Name the Icon Visdom Referrals.\n\n"
				+ "Did you have any questions? If you do have any questions in the future, please call  me at (direct number). Thank you for your time and we look forward to working with you and your referrals. Have a great (day/night)";
				String referraltask2 = "Follow up call to "+ referralname+ " at "+ phoneNumber+ " from conversation or message from previous week. See if further assistance is required.";
				String referralNote2 = "- Use Call Script (calling referral sources)\n\n";
				String referralNote3="Hello "+referralname+", this is "+assignUser+" with Visdom Mortgage Solutions.  We are following up to see how things are going and if we can offer you any assistance.   If you have any questions, please contact me directly at (your direct number). I look forward speaking with you and thanks in advance for returning my call. Have a great (day/evening!)";

				String referralNote4="Script for person to person call:";
				String referralNote5="Hello "+referralname+", this is "+assignUser+" with Visdom Mortgage Solutions. How are you today? We wanted to call to thank you again for joining our referral team and am wondering if we can offer you any assistance.  We look forward to working with you and your referrals and if you ever need any assistance please contact our broker team anytime. Thank you and have a great (day/night)!";


				String taskid = "0";
		ArrayList<ProjectDetails> projectList =getProjectId();


		try {
			Thread.sleep(6000);
		} catch (Exception e) {
			log.error("error while processing createReferralproject " + e.getMessage());
		}
		projectList = getProjectId();

		if (projectList.size() == 0) {
			projectList = getProjectId();
			try {
				Thread.sleep(6000);
			} catch (Exception e) {
				log.error("error while Thread sleeping " + e.getMessage());
			}
			
			projectList = getProjectId();
		}
		// User user2=new CouchBaseOperation().getTodoistUserEmail(todoistUser);

		if (projectList.isEmpty()) {

			projectId = createProject("Referral Sources", token);
		//	taskid = createTasks(referraltaskHEading, projectId, token,assignUser);

			taskid = createTasks(referraltask1, projectId, token,assignUser);
			taskUpdated(taskid, "-1");
			createNotes(taskid, referralNote1, token);
			createNotes(taskid, referralNote11, token);
			createNotes(taskid, referralNote12, token);
			createNotes(taskid, referralNote121, token);
			createNotes(taskid, referralNote122, token);
			createNotes(taskid, referralNote123, token);

			String taskId1 = "0";
			taskId1 = createTasks(referraltask2, projectId, token,assignUser);
			taskUpdated(taskId1, "+14");

			createNotes(taskId1, referralNote2, token);
			createNotes(taskId1, referralNote3, token);
			createNotes(taskId1, referralNote4, token);
			createNotes(taskId1, referralNote5, token);

			ArrayList<User> list = (ArrayList<User>) new CouchBaseOperation()
					.getUsers();
			
			for(String useremail:listofEmails){
					shareProject(projectId, useremail, token);
				}
				// taskUpdated2(taskid, user2.getUserId());

			

			log.info("assign Id" + assignUser);

			assignUser(taskid, assignUser);

			assignUser(taskId1, assignUser);

			// User user2=new
			// CouchBaseOperation().getTodoistUserEmail(todoistUser);
			// shareProject(projectId, user2.getEmail(), token);

		}

		int numberOfusers = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = projectList.iterator(); iterator.hasNext();) {
			ProjectDetails projectDetails = (ProjectDetails) iterator.next();
			++numberOfusers;

			if (numberOfusers < 2) {
				projectId = projectDetails.getProjectId();
				
				log.info(projectId + "--------------------------------");

				if (projectId != null && projectId != "0") {
					// User user=new
					// CouchBaseOperation().getTodoistUserEmail(todoistUser);
					assignUser = gettodoistUser();

				//	taskid = createTasks(referraltaskHEading, projectId, token,assignUser);

					taskid = createTasks(referraltask1, projectId, token,assignUser);
					taskUpdated(taskid, "-1");
					createNotes(taskid, referralNote1, token);
					createNotes(taskid, referralNote11, token);
					createNotes(taskid, referralNote12, token);
					createNotes(taskid, referralNote121, token);
					createNotes(taskid, referralNote122, token);
					createNotes(taskid, referralNote123, token);

					String taskId1 = "0";
					taskId1 = createTasks(referraltask2, projectId, token,assignUser);
					taskUpdated(taskId1, "+14");

					createNotes(taskId1, referralNote2, token);
					createNotes(taskId1, referralNote3, token);
					createNotes(taskId1, referralNote4, token);
					createNotes(taskId1, referralNote5, token);
					log.info("assign Id" + assignUser);
					assignUser(taskid, assignUser);

					assignUser(taskId1, assignUser);

				}
			}
		}

	}

	public static void assignUser(String taskId, String assignId)
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

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}

	}

	public static String createProject(String projectName, String token)
			throws JSONException {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		String projectId = null;
		log.info("uid " + uid);
		log.info("uid 2" + uid1);

		String myvar = "[" + "  {" + "    \"type\": \"project_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"name\": \""
				+ projectName + "\"" + "    }" + "  }" + "]";

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

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}
		return projectId;
	}

	public static String createTasks(String taskName, String projectId,
			String token,String responsibleuser) throws JSONException {
		log.info("projectId "+projectId +" token "+token );

		String taskId = "0";
		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		// log.info("uid " + uid);
		// log.info("uid 2" + uid1);
		String myvar = "[" + "  {" + "    \"type\": \"item_add\","
				+ "    \"temp_id\": \"" + uid + "\"," + "    \"uuid\": \""
				+ uid1 + "\"," + "    \"args\": {" + "      \"content\": \""
				+ taskName + "\"," + "      \"project_id\": " + projectId + ",\"priority\": 4,\"responsible_uid\":"+ responsibleuser + ""
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

	public static String createTasksWithIntendation(String taskName, String projectId,
			String token) throws JSONException {
		
	log.info("projectId "+projectId +" token "+token );
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
					 log.info(output1);

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

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}

	}

	public static void taskUpdated2(String taskId, String responsibleuser)
			throws JSONException {

		UUID uid = UUID.randomUUID();

		

		String myvar = "[{\"type\": \"item_update\"," + " \"uuid\": \"" + uid
				+ "\", \"args\": {\"id\": " + taskId + ",\"responsible_uid\":"
				+ responsibleuser + ",\"priority\": 4}}]";

	

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

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}

	}

	public static void createNotes(String taskId, String note, String token) {
		log.info(taskId + "niote" + note  +"  taskId "+taskId  +" token "+token);
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

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}

	}

	public static void shareProject(String projectId, String email, String token) {

		UUID uid = UUID.randomUUID();
		UUID uid1 = UUID.randomUUID();

		String myvar = "[{\"type\": \"share_project\"," + " \"temp_id\": \""
				+ uid + "\"," + " \"uuid\": \"" + uid1 + "\", "
				+ "\"args\": {\"project_id\": \"" + projectId
				+ "\", \"message\": \"\", " + "\"email\": \"" + email + "\"}}]";

		try {
			// log.info(URLEncoder.encode(myvar));

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

				// log.info("Output from Server .... \n");
				String output1;
				while ((output1 = br1.readLine()) != null) {
					log.info(output1);
				}

			}

			conn1.disconnect();

		} catch (MalformedURLException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		} catch (IOException e) {

			log.error("error in referraltaskcreation " + e.getMessage());

		}
	}


	static CouchbaseClient client1 = null;
	static int dataExsist = 0;
	static ArrayList<ProjectDetails> list = new ArrayList<ProjectDetails>();

	public static ArrayList<ProjectDetails> getProjectId() {

		try {
			client1 = new CouchBaseOperation().getConnectionToCouchBase();
			DesignDocument designdoc = getDesignDocument("dev_referralTask");
			boolean found = false;

			// 5. get the views and check the specified in code for existence
			for (ViewDesign view : designdoc.getViews()) {
				if (view.getMap() == "referralTask") {
					found = true;
					break;
				}
			}

			// 6. If not found then create view inside document
			if (!found) {
				ViewDesign view = new ViewDesign(
						"referralTask",
						"function (doc, meta) {\n"
								+ "if(doc.ProjectName_==\"Referral Sources\")\n"
								+ "{emit(meta.id,null)}\n" +

								"}");

				designdoc.getViews().add(view);
				client1.createDesignDoc(designdoc);
			}

			
			list=	getCocuhbViewData("referralTask", "referralTask");

			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in referraltaskcreation " + e.getMessage());
		}

		return list;

	}


	private static DesignDocument getDesignDocument(String name) {
		try {
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}
	
	public static  ArrayList<ProjectDetails> getCocuhbViewData(String designDooc, String viewName)
			throws JSONException {
		ArrayList<ProjectDetails> list1 = new ArrayList<ProjectDetails>();
	String projectId="";
	Properties properties=CouchBaseOperation.getCouchbasePropertiesFile();
				client1 = new CouchBaseOperation().getConnectionToCouchBase();

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
					log.info("old data " + jsonData);
					JSONObject josnInsidedata = (JSONObject) jsonData
							.get("event_data");
					projectId = josnInsidedata.get("id").toString();
					try {
						User user = new CouchBaseOperation()
								.getTodoistUserEmail(jsonData.get("username")
										.toString());
						
					} catch (Exception e) {
						log.error("error while processing getCocuhbViewData " + e.getMessage());
					}
					ProjectDetails project = new ProjectDetails();
					project.setProjectId(projectId);
					
					list1.add(project);
					log.info("project id" + projectId);
				 log.debug("",project);
				 return list1;
	            } finally {
	            	try{
	                response.close();
	            	}catch(Exception e){
	            		log.error("error  while processing getCocuhbViewData "+e.getMessage());;
	            	}
	            }
	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				log.error("error in gettin the view query Data "+e.getMessage());
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				log.error("error in gettin the view query Data "+e.getMessage());
			} finally {
	            try {
					httpclient.close();
					client1.shutdown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("failed to close http client S "+e.getMessage());
				}
	        }
	
		return list1;
	}
	
}
