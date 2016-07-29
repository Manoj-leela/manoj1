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

import dto.ProjectDetails;
import dto.User;

public class test extends Thread {
	// static Logger log = LoggerFactory.getLogger(ReferralTasks.class);
	private static org.slf4j.Logger log = play.Logger.underlying();

	// assistant

	static String token = "19fb71b3edb93f05952f23e0b99120806ec7d224";
	// dale user
	static String daletoken = "181e3a07f6893de7946eaba76982bc0522791bf1";

	static String todoistUser = "Guy";

	String referralName;
	String phoneNumber;

	public test(String referralName, String phoneNumber) {
		this.referralName = referralName;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			createReferralproject(referralName, phoneNumber);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("error in referraltaskcreation " + e.getMessage());
		}
	}

	public static void main(String[] args) throws JSONException {
		createReferralproject("Vijay","9786767777");
		log.info("getTodoist User" + gettodoistUser());
		// ArrayList list= getProjectId();
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
			String phoneNumber) throws JSONException {
		String projectId = null;
String		referraltaskHEading="@Referral Source Sign up";
		String referraltask1 = "Call  "
				+ referralname
				+ " at "
				+ phoneNumber
				+ " to welcome to Visdom and offer assistance with mobile link setup.";
		String referralNote1 = "- Use Call Script (calling referral sources)";
		String referraltask2 = "Follow up call to "
				+ referralname
				+ " at "
				+ phoneNumber
				+ " from conversation or message from previous week. See if further assistance is required.";
		String referralNote2 = "- Use Call Script (calling referral sources)";
		String taskid = "0";

		ArrayList<ProjectDetails> projectList = null;
		getProjectId();
		projectList = view();
		try {
			Thread.sleep(6000);
		} catch (Exception e) {
			log.error("error occurs while creating refferel project " + e.getMessage());
		}
		getProjectId();
		projectList = view();

		if (projectList.size() == 0) {
			getProjectId();
			projectList = view();
			try {
				Thread.sleep(6000);
			} catch (Exception e) {
				log.error("error occurs while creating refferel project " + e.getMessage());
			}
			getProjectId();
			projectList = view();
		}
		// User user2=new CouchBaseOperation().getTodoistUserEmail(todoistUser);

		if (projectList.isEmpty()) {
			assignUser = gettodoistUser();

			projectId = createProject("Referral Sources", token);
		//	taskid = createTasks(referraltaskHEading, projectId, token,assignUser);

			taskid = createTasks(referraltask1, projectId, token,assignUser);
			taskUpdated(taskid, "today");
			createNotes(taskid, referralNote1, token);
			String taskId1 = "0";
			taskId1 = createTasks(referraltask2, projectId, token,assignUser);
			taskUpdated(taskId1, "+14");

			createNotes(taskId1, referralNote2, token);

			ArrayList<User> list = (ArrayList<User>) new CouchBaseOperation()
					.getUsers();
			for (@SuppressWarnings("rawtypes")
			Iterator iterator = list.iterator(); iterator.hasNext();) {
				User user2 = (User) iterator.next();
				if (!user2.getUserName().trim().equalsIgnoreCase("assistant")) {
					shareProject(projectId, user2.getEmail(), token);
				}
				// taskUpdated2(taskid, user2.getUserId());

			}

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
				token = projectDetails.getToken();
				log.info(projectId + "--------------------------------");

				if (projectId != null && projectId != "0") {
					// User user=new
					// CouchBaseOperation().getTodoistUserEmail(todoistUser);
					assignUser = gettodoistUser();

				//	taskid = createTasks(referraltaskHEading, projectId, token,assignUser);
					taskid = createTasks(referraltask1, projectId, token,assignUser);
					taskUpdated(taskid, "-1");
					createNotes(taskid, referralNote1, token);
					String taskId1 = "0";
					taskId1 = createTasks(referraltask2, projectId, token,assignUser);

					taskUpdated(taskId1, "+14");
					createNotes(taskId1, referralNote2, token);
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

			// 7. close the connection with couchbase

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error in referraltaskcreation " + e.getMessage());
		}

		return list;

	}

	public static ArrayList<ProjectDetails> view() {
		ArrayList<ProjectDetails> list1 = new ArrayList<ProjectDetails>();
		String projectId = "0";

		try {

			// get the view
			View view = client1.getView("dev_referralTask", "referralTask");

			// create Query.
			Query query = new Query();
			query.setIncludeDocs(true).setLimit(1);

			// get ViewResponse
			ViewResponse viewRes = client1.query(view, query);

			// Iterate over the ViewResponse
			for (ViewRow row : viewRes) {

				JSONObject jsonData = new JSONObject(row.getDocument()
						.toString());
				log.info("old data " + jsonData);
				JSONObject josnInsidedata = (JSONObject) jsonData
						.get("event_data");
				projectId = josnInsidedata.get("id").toString();
				try {
					User user = new CouchBaseOperation()
							.getTodoistUserEmail(jsonData.get("username")
									.toString());
					token = user.getToken();
				} catch (Exception e) {
					log.error("error while processing view " + e.getMessage());
				}
				ProjectDetails project = new ProjectDetails();
				project.setProjectId(projectId);
				project.setToken(token);
				list1.add(project);
				log.info("project id" + projectId);

			}
			try {
				client1.shutdown(9000l, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				log.error("error while shutdown " + e.getMessage());
			}

			log.info("data exisst " + dataExsist);
		} catch (Exception e) {
			log.error("error while data exists " + e.getMessage());
		}
		return list1;
	}

	private static DesignDocument getDesignDocument(String name) {
		try {
			return client1.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}

}