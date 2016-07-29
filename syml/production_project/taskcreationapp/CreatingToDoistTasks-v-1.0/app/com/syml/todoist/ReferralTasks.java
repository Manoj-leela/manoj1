package com.syml.todoist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewRow;
import com.syml.ReadConfigurationFile;
import com.syml.TodoistConstants;
import com.syml.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;
import com.syml.dto.User;

public class ReferralTasks extends Thread {
	// static Logger log = LoggerFactory.getLogger(ReferralTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();
	static String token = "";
	String referralName;
	String phoneNumber;
	String referralEmailId;
	String referredBy;
	public ReferralTasks(){
		
	}
	public ReferralTasks(String referralName, String phoneNumber,	String referralEmailId,String referredBy) {
		this.referralName = referralName;
		this.phoneNumber = phoneNumber;
		this.referralEmailId=referralEmailId;
		this.referredBy=referredBy;
	}
	@Override
	public void run() {
			try {
				createReferralproject(referralName, phoneNumber,referralEmailId,referredBy);
			} catch (TaskCreationException e) {
				Logger.error("Error in Referral task Creation for given Referral details :referralEmailId = "+referralEmailId ,e);
			}
	
	}

	static String assignUser = null;
//assign tasks in round robin fashion 
	public static String gettodoistUser() throws IOException, TaskCreationException {
			Properties properties=	ReadConfigurationFile.readConfigProperties();
			if(properties==null)
				throw new TaskCreationException("ERROR  in getting configuration file ");
			assignUser=properties.getProperty(TodoistConstants.TASK_ASSIGN_USER_ID);
		return assignUser;

	}
	
	
/**
 *  To create Referral Todoist Project , if already referralProject Exist and found projectID , use project referral tasks
 *  else we create new todoist project and referral tasks 
 * @param referralname
 * @param phoneNumber
 * @param email
 * @param referedBy
 * @throws TaskCreationException
 */
	public static void createReferralproject(String referralname,
			String phoneNumber,String email,String referedBy) throws  TaskCreationException {
	String projectId;
	try {
		projectId = getReferralTodoistProjectId();
	

		assignUser = gettodoistUser();
		token=ReadConfigurationFile.getUserToken();
		ArrayList<User> listOfTodoistUser=(ArrayList<User>) ReadConfigurationFile.getTodoistUserDetails();
		//String		referraltaskHEading="@Referral Source Sign up";
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

	if (projectId.equalsIgnoreCase("0")) {

			projectId = createProject("Referral Sources", token);

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
	
			for(User user:listOfTodoistUser){
					shareProject(projectId, user.getEmail(), token);
				}
			log.info("assign Id" + assignUser);

			assignUser(taskid, assignUser);

			assignUser(taskId1, assignUser);

		}else {
				if (projectId != null && projectId != "0") {
					assignUser = gettodoistUser();
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
	} catch (CouchbaseDaoServiceException e) {
		throw new TaskCreationException("Error when reterving Desgin Document for query ",e);
	}catch (IOException e) {
	
		throw new TaskCreationException("error  while reading configuration File  ",e);
	} catch (JSONException e) {
		throw new TaskCreationException("error  while  creating todoist tasks   ",e);

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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL  + token
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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL  + token
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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL  + token
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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL  + token
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

			URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL  + token
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
				URL url1 = new URL(TodoistConstants.TODOIST_APPLICATION_URL + token
					+ "&commands=" + URLEncoder.encode(myvar, "UTF-8"));
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setDoOutput(true);
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		

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


	



	
	public static String getReferralTodoistProjectId() throws CouchbaseDaoServiceException{
		Bucket bucket = new CouchbaseUtil().getCouchbaseClusterConnection();

		
		// Get bucket manager
		BucketManager bucketManager = bucket.bucketManager();

		com.couchbase.client.java.view.DesignDocument designDoc = bucketManager.getDesignDocument("referralTask");
	if(designDoc ==null){
		// Initialize design document
		 designDoc = 	com.couchbase.client.java.view.DesignDocument.create(
			"referralTask",
			Arrays.asList(
				DefaultView.create("referralTask",
					"function (doc, meta) { if (doc.ProjectName_==\"Referral Sources\") { emit(meta.id, null); } }")
					)
		);
			// Insert design document into the bucket
			bucketManager.insertDesignDocument(designDoc);
		
	}
		
	List<ViewRow> list= bucket.query(ViewQuery.from("referralTask", "referralTask").stale(Stale.FALSE)).allRows();

	String projectName="";
	String project[]=null;
	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		ViewRow viewRow = (ViewRow) iterator.next();
		log.error("referral Document "+viewRow.id());
		projectName=viewRow.id();
	}
	
	String projectId="0";
	if(!projectName.isEmpty()){
		project=projectName.split("_");
		projectId=project[1];
	}
		
		return projectId;
	
	}
	public static void main(String[] args) throws TaskCreationException {
		createReferralproject("test", "324432", "test@gmail.com", "test");
	}
	
}
