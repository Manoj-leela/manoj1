package com.syml.infrastracture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;
import com.syml.couchbase.dao.service.CouchBaseService;

import controllers.User;

public class CouchBaseOperation {
	
	private static org.slf4j.Logger logger = play.Logger.underlying();
	CouchbaseClient client1 = null;
	CouchBaseService couchBaseService=new CouchBaseService();
	ObjectMapper object = new ObjectMapper();

	

	public void storeDataInCouchBase(String key, JSONObject data) {
		logger.info("(.) inside storeDataInCouchBase method of CouchBaseOperation class");
		try {
			JsonObject json=	mapToJson(data);
			json.put("Submission_Date_Time1b", getSubmissionDate());
			couchBaseService.storeDataToCouchbase(key, json);
		} catch (Exception e) {
			logger.error("error while storing data into couchbase : " +e.getMessage());
		}

	}
	public void updateTaskInCouchBase(String key, JSONObject editData) {

		try {
			logger.info("(.) inside editDataInCouchBase method of CouchBaseOperation class");
					JsonObject json=	mapToJson(editData);
				json.put("Submission_Date_Time1b", getSubmissionDate());
				couchBaseService.storeDataToCouchbase(key, json);
		} catch (Exception e) {
			logger.error("error while editing data into couchbase : "+e.getMessage());
		}
	}
	// --------------------------------------Todoist
	// adding,create,update,delete---> project,task,notes ,lable methods
	// ----------------------------------------------
	public void splitEvents(String eventName, String id, JSONObject eventData,
			String userId) throws JSONException {

		switch (eventName) {
		case "project:added":
			addProjectName(eventData);
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");
			storeDataInCouchBase("tasksproject_" + id, eventData);
			break;

		case "project:updated":
			addProjectName(eventData);
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");
			storeDataInCouchBase("tasksproject_" + id, eventData);
			break;

		case "project:deleted":
			addProjectName(eventData);
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");
			storeDataInCouchBase("tasksproject_" + id, eventData);

			break;

		case "project:archived":
			addProjectName(eventData);
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "ProjectToDo");
			storeDataInCouchBase("tasksproject_" + id, eventData);
			break;

		case "item:added":
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			try {
				JSONObject eventJsondata = eventData.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				logger.error("error while performing event on daocument in couchbase : "+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
			storeDataInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:updated":
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			try {
				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");
				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				logger.error("error while adding content of json"+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:completed":
			eventData.put("Type_TaskCompleted", "Type_TaskCompleted");
			eventData.put("Type", "TaskToDo");
			eventData.put("note1", "");
			try {
				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");
				eventData.put("Type_TaskCompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				logger.error("error while adding content of json"+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			getLabels(eventData);
			updateTaskInCouchBase("tasksitem_" + id, eventData);

			break;

		case "item:uncompleted":
			eventData.put("Type", "TaskToDo");
			eventData.put("Type_TaskUncompleted", "Type_TaskUncompleted");
			eventData.put("note1", "");
			try {
				JSONObject eventJsondata = eventData
						.getJSONObject("event_data");
				int taskId = eventJsondata.getInt("id");

				eventData.put("Type_TaskUncompleted1", taskId);
				eventJsondata.put("responsible_userName", "");
				JSONArray jsonArray=new JSONArray();
				eventData.put("NotesContent",jsonArray);
				eventData.put("event_data",eventJsondata);
			} catch (Exception e) {
				logger.error("error while adding content of json"+e.getMessage());
			}
			eventData = getModefiedUserData(eventData, userId);
			updateTaskInCouchBase("tasksitem_" + id, eventData);
			break;

		case "item:deleted":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "TaskToDo");
			updateTaskInCouchBase("tasksitem_" + id, eventData);
			break;
		case "note:added":
			eventData = getModefiedUserData(eventData, userId);
			try {
				JSONObject obj = (JSONObject) eventData.get("event_data");
				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("Error is getting while Modifying UserData"+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");
			storeDataInCouchBase("tasksnote_" + id, eventData);
			break;

		case "note:updated":
			eventData = getModefiedUserData(eventData, userId);
			try {
				JSONObject obj = (JSONObject) eventData.get("event_data");
				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("error while performing Coucgbaseoperation "+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");

			storeDataInCouchBase("tasksnote_" + id, eventData);

			break;

		case "note:deleted":
			eventData = getModefiedUserData(eventData, userId);
			try {
				JSONObject obj = (JSONObject) eventData.get("event_data");
				getNoteToTasks(obj.get("item_id").toString().trim(),
						obj.get("id").toString().trim(), obj.get("content")
								.toString().trim());
			} catch (Exception e) {
				logger.error("error while performing deleting item of Coucgbaseoperation "+e.getMessage());
			}
			eventData.put("Type", "NoteToDo");

			storeDataInCouchBase("tasksnote_" + id, eventData);

			break;

		case "label:added":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");
			storeDataInCouchBase("taskslabel_" + id, eventData);

			break;

		case "label:updated":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");

			storeDataInCouchBase("taskslabel_" + id, eventData);

			break;

		case "label:deleted":
			eventData = getModefiedUserData(eventData, userId);
			eventData.put("Type", "LabelToDo");

			storeDataInCouchBase("taskslabel_" + id, eventData);

			break;

		default:
			break;
		}

	}

	// add project name to before storing TodoitProject to couchbase
	public void addProjectName(JSONObject eventData) throws JSONException {
		JSONObject insideEventdata = (JSONObject) eventData.get("event_data");
		try {
			if (insideEventdata.getInt("id") != 0) {
				eventData.put("ProjectName_", insideEventdata.get("name")
						.toString());
			}
		} catch (Exception e) {
			logger.error("error while performing addProjectName operation "+e.getMessage());
		}
	}

	// adding username,assigned username, responsible username to
	// tasks,project,lebals,notes
	public JSONObject getModefiedUserData(JSONObject eventData, String userId)
			throws JSONException {

		String userName = getUserName(userId);
		if (userName != null) {

			JSONObject insideEventdata = (JSONObject) eventData
					.get("event_data");

			
			JSONObject jsonProjectdata = null;
			String projectId = "0";
			try {
				if (insideEventdata.getInt("project_id") != 0) {
					eventData.put("ProjectName",
							getProjectName(insideEventdata.getInt("project_id")
									+ ""));

					try {
						projectId = insideEventdata.getInt("project_id") + "";
						
						jsonProjectdata = new JSONObject(couchBaseService.getCouhbaseDataByKey(
								"tasksproject_"
										+ insideEventdata.getInt("project_id"))
								.toString());

					} catch (Exception e) {
						logger.error("error while performing addProjectName operation "+e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}
			try {
				if (insideEventdata.getInt("responsible_uid") != 0) {
					
					insideEventdata.put(
							"responsible_userName",
							getUserName(insideEventdata
									.getInt("responsible_uid") + ""));

					jsonProjectdata.put(
							"assigned_userName",
							getUserName(insideEventdata
									.getInt("responsible_uid") + ""));
				}
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}

			try {
				if (insideEventdata.getInt("assigned_by_uid") != 0) {
					eventData.put(
							"assigned_userName",
							getUserName(insideEventdata
									.getInt("assigned_by_uid") + ""));

				}

			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}
			try {
				storeDataInCouchBase("tasksproject_" + projectId,
						jsonProjectdata);
			} catch (Exception e) {
				logger.error("error while performing addProjectName operation "+e.getMessage());
			}

			insideEventdata.put("username", userName);

			eventData.put("username", userName);
			eventData.put("event_data", insideEventdata);

		}

		logger.debug("-------------------------inside Getmodiefied user data-------------------------------"
						+ eventData.get("event_data"));
		return eventData;

	}

	// adding lables to the Tasks
	public void getLabels(JSONObject eventData) {
		JSONObject jsn = null;
		org.codehaus.jettison.json.JSONArray arrayOflables = null;
		org.codehaus.jettison.json.JSONArray arrayOflables1 = new org.codehaus.jettison.json.JSONArray();
		try {
			jsn = (JSONObject) eventData.get("event_data");
			arrayOflables = (org.codehaus.jettison.json.JSONArray) jsn
					.get("labels");
			logger.debug("-------------inside  get lables-----------"
					+ arrayOflables);

			for (int i = 0; i < arrayOflables.length(); i++) {
				try {

					JSONObject jsn1 = new JSONObject(couchBaseService.getCouhbaseDataByKey(
							"taskslabel_" + arrayOflables.get(i)).toString());
					
					JSONObject json = (JSONObject) jsn1.get("event_data");
					arrayOflables1.put(json.get("name").toString());
				} catch (Exception e) {
					logger.error("error while performing  operation in getLabels"+e.getMessage());
				}
			}
			eventData.put("LabelsName", arrayOflables1);
			client1.shutdown();
		} catch (Exception e) {
			logger.error("error while performing  operation in getLabels"+e.getMessage());
		}
	}

	// adding number of notes and notescontent to Tasks
	public void getNoteToTasks(String taskId, String id, String content) {

		JSONObject jsn = null;
		try {

			logger.debug("tasksitem_" + taskId.trim());

			jsn = new JSONObject(couchBaseService.getCouhbaseDataByKey("tasksitem_" + taskId.trim())
					.toString());
			org.codehaus.jettison.json.JSONArray arrayOfNotes = null;
			org.codehaus.jettison.json.JSONArray arrayOfContents = null;
			try {
				arrayOfNotes = (org.codehaus.jettison.json.JSONArray) jsn
						.get("Notes");
				arrayOfContents = (org.codehaus.jettison.json.JSONArray) jsn
						.get("NotesContent");

			} catch (Exception e) {

				arrayOfNotes = null;
				arrayOfContents = null;
				logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());
			}
			logger.debug("notes-------------- " + arrayOfNotes + " arrayOfNotes "
					+ arrayOfContents + "----------json " + jsn);

			if (arrayOfNotes != null && arrayOfContents != null) {
				arrayOfContents.put(content);
				arrayOfNotes.put(id);

				logger.debug("after adding notesnotes-------------- "
						+ arrayOfNotes + " arrayOfNotes " + arrayOfContents
						+ "----------json " + jsn);

				jsn.put("Notes", arrayOfNotes);
				jsn.put("NotesContent", arrayOfContents);
				String notesdata="";
				try{
				for(int i=0;i<arrayOfContents.length();i++){
					
					notesdata=notesdata+arrayOfContents.get(i).toString()+"\n\n";
				}
				jsn.put("note1", notesdata);

				}catch(Exception e){
		logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());		
				}
			} else {
				arrayOfContents = new JSONArray();
				arrayOfNotes = new JSONArray();
				arrayOfContents.put(content);
				arrayOfNotes.put(id);
				jsn.put("Notes", arrayOfNotes);
				jsn.put("NotesContent", arrayOfContents);
				jsn.put("note1",content );
			}

			storeDataInCouchBase("tasksitem_" + taskId.trim(), jsn);
			// name = (String) jsonEventdata.get("name");
		} catch (Exception e) {
			logger.error("error while performing  operation in getNoteToTasks"+e.getMessage());
		}

	}

	// Get project name using Proejct id from couchbase to Add projectName in
	// Tasks
	public String getProjectName(String projectId) {
		String name = "";

		try {

			logger.debug("tasksproject_" + projectId.trim());

			JSONObject jsn = new JSONObject(couchBaseService.getCouhbaseDataByKey(
					"tasksproject_" + projectId.trim()).toString());

			JSONObject jsonEventdata = (JSONObject) jsn.get("event_data");
			name = (String) jsonEventdata.get("name");
		} catch (Exception e) {
			logger.error("error while performing  operation in getProjectName"+e.getMessage());
		}
		return name;

	}

	// get UserName of Todoist From Coucbase to add in project, tasks,items,
	// labels and notes
	public String getUserName(String userid) {

		String name = null;
		try {

			logger.debug("Taskuser_" + userid.trim());

			JSONObject jsn = new JSONObject(getDocument(
					"TaskUser_" + userid.trim()).toString());
			name = (String) jsn.get("userName");
		} catch (Exception e) {
			logger.error("error while performing  operation in getUserName"+e.getMessage());
		}

		logger.debug("-------------------------inside getusername-------------------------------"
						+ name);
		return name;
	}

	// Fetching the perticuler TaskUser Name based UserId from Couchbase, To add
	// userName to Tasks,project,notes
	public User getPerticulerUser(String userid) {

		User user = null;
		try {

			logger.debug("Taskuser_" + userid.trim());

			ObjectMapper object = new ObjectMapper();
			user = object.readValue(
					(getDocument("TaskUser_" + userid.trim()).toString()),
					User.class);
		} catch (Exception e) {
			logger.error("error while performing  operation in getPerticulerUser"+e.getMessage());
		}

		return user;
	}

	// Fetching UserId of TodOist user Using Token and Storing TaskUser to
	// couchbase
	public boolean addingUser(String token) {
		boolean sucess = false;

		JSONObject jsonObject = null;
		String userId = null;
		String email = null;
		String userName = null;
		try {

			// URL url = new
			// URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
			URL url = new URL("https://todoist.com/API/v6/sync?token="
					+ token.trim()
					+ "&seq_no=0&seq_no_global=0&resource_types=[\"all\"]");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			if (conn.getResponseCode() != 200) {
				logger.debug("Failed : HTTP error code : "
						+ conn.getResponseCode());
				sucess = false;
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				logger.debug("Output from Server .... \n");
				String output;
				while ((output = br.readLine()) != null) {
					// logger.debug(output);
					jsonObject = new JSONObject(output);
					// token=jsonObject.getString("api_token");
					JSONObject userJson = new JSONObject(jsonObject.get("User")
							.toString());
					userId = userJson.get("id").toString();
					email = userJson.get("email").toString();
					userName = userJson.get("full_name").toString();
					sucess = true;

				}

			}

			if (userId != null) {

				JSONObject json1 = new JSONObject();
				json1.put("userId", userId);
				json1.put("userName", userName);
				json1.put("token", token);
				json1.put("email", email);
				storeDataInCouchBase("TaskUser_" + userId, json1);

			}
			conn.disconnect();

		} catch (Exception e) {

			logger.error("error while performing  operation in addingUser"+e.getMessage());

		}
		return sucess;
	}

	// updating Task Users----------------------------
	public boolean updateUser(String token) {
		boolean sucess = false;

		JSONObject jsonObject = null;
		String userId = null;
		String email = null;
		String userName = null;
		try {

			// URL url = new
			// URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
			URL url = new URL("https://todoist.com/API/v6/sync?token="
					+ token.trim()
					+ "&seq_no=0&seq_no_global=0&resource_types=[\"all\"]");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			if (conn.getResponseCode() != 200) {
				logger.debug("Failed : HTTP error code : "
						+ conn.getResponseCode());
				sucess = false;
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				logger.debug("Output from Server .... \n");
				String output;
				while ((output = br.readLine()) != null) {
					// log.debug(output);
					jsonObject = new JSONObject(output);
					// token=jsonObject.getString("api_token");
					JSONObject userJson = new JSONObject(jsonObject.get("User")
							.toString());
					userId = userJson.get("id").toString();
					email = userJson.get("email").toString();
					userName = userJson.get("full_name").toString();
					sucess = true;

				}

			}

			if (userId != null) {

				JSONObject json1 = new JSONObject();
				json1.put("userId", userId);
				json1.put("userName", userName);
				json1.put("token", token);
				json1.put("email", email);
				storeDataInCouchBase("TaskUser_" + userId, json1);

			}
			conn.disconnect();

		} catch (Exception e) {

			logger.error("error while performing  operation in updateUser"+e.getMessage());
		}
		return sucess;

	}

	// get list of Taskusers for couchbase using view and query to display in
	// webpage
	/**
	 * get The list of users  on basis of userId login
	 * @return
	 */
		public List<User> getUsers() {
			logger.debug("(.) inside the getUsers() in CouchBaseOperation Class .");

			ArrayList<User> listOfTodostUser = new ArrayList<User>();
			CouchBaseService couchBaseService = new CouchBaseService();
			Bucket bucket = null;
			try {
				bucket = new CouchbaseUtil().getCouchbaseClusterConnection();
			} catch (CouchbaseServiceException e1) {
			}
			ObjectMapper objectMapper = new ObjectMapper();

			BucketManager bucketManager = bucket.bucketManager();

			com.couchbase.client.java.view.DesignDocument designDoc = bucketManager.getDesignDocument("todoistUsers");
			if (designDoc == null) {

				designDoc = com.couchbase.client.java.view.DesignDocument.create("todoistUsers",
						Arrays.asList(DefaultView.create("todoistUsers",
								"function (doc, meta) { if (doc.userId && doc.userName && doc.token ) { emit(meta.id, null); } }")));

				bucketManager.insertDesignDocument(designDoc);

			}

			List<com.couchbase.client.java.view.ViewRow> list = bucket.query(ViewQuery.from("todoistUsers", "todoistUsers").stale(Stale.FALSE)).allRows();

			User user = null;
			for (Iterator<com.couchbase.client.java.view.ViewRow> iterator = list.iterator(); iterator.hasNext();) {
				com.couchbase.client.java.view.ViewRow viewRow = iterator.next();
				logger.debug("referral Document " + viewRow.id());
				try {

					String userData = couchBaseService.getCouhbaseDataByKey(viewRow.id()).toString();
					user = objectMapper.readValue(userData, User.class);
				} catch (Exception e) {
					Logger.error("error in getting couchbase data ", e);
				}
				if (user != null) {
					listOfTodostUser.add(user);
				}

			}

			return listOfTodostUser;

		}

	// Deleting Task user From couchbase
	public boolean deleteUser(String userid) {

		boolean sucess = false;
		try {
			Bucket bucket = null;
			try {
				bucket = new CouchbaseUtil().getCouchbaseClusterConnection();
			} catch (CouchbaseServiceException e1) {
			}
			bucket.remove(("TaskUser_" + userid.trim())
					.toString());
		} catch (Exception e) {
			logger.error("error while deleting user"+e.getMessage());
		}

		return sucess;
	}

	
	public String encryptPassword(String password) {
		StringBuffer secureString = new StringBuffer();
		try {
			password = "visdom" + password;
			char myKey = (char) 0x15; // binary 10101
			char[] chPass = password.toCharArray();
			for (int i = 0; i < chPass.length; i++) {
				chPass[i] = (char) (chPass[i] ^ myKey); // XOR
				secureString.append(chPass[i]);
			}
		} catch (Exception e) {
			logger.error("error while encryptPassword : " +e.getMessage());
		}

		return secureString.toString();
	}

	public String decryptPassword(String password) {
		StringBuffer secureString = new StringBuffer();
		try {

			char myKey = (char) 0x15; // binary 10101
			char[] chPass = password.toCharArray();
			for (int i = 0; i < chPass.length; i++) {
				chPass[i] = (char) (chPass[i] ^ myKey); // XOR
				secureString.append(chPass[i]);
			}
		} catch (Exception e) {
			logger.error("error while decrypt Password : " +e.getMessage());
		}

		return secureString.toString();
	}

	public JSONObject getDocument(String key) {
		JSONObject documentData = null;
		try {
			documentData = new JSONObject(couchBaseService.getCouhbaseDataByKey(key).toString());
		} catch (Exception e) {
			logger.error("error while getDocument from JSON " +e.getMessage());
		}

		return documentData;

	}

	public void storeAdminDetailsCouhbase(String userName, String password)
			throws JSONException {
		JSONObject json = null;

		try {
			json = new JSONObject(getDocument("DataFromTodoist").toString());
		} catch (Exception e) {
			logger.error("error while performing in storeAdminDetailsCouhbase" +e.getMessage());
		}
		if (json != null) {

		} else {
			json = new JSONObject();
			password = encryptPassword(password);
			json.put("username", userName);
			json.put("password", password);
			storeDataInCouchBase("DataFromTodoist", json);

		}
	}

	public void updateAdminDetailsCouhbase(String userName, String password) {
		JSONObject json = null;

		try {
			json = new JSONObject(getDocument("DataFromTodoist").toString());

		} catch (Exception e) {
			logger.error("error while performing in updateAdminDetailsCouhbase" +e.getMessage());
		}
		if (json != null) {
			try {

				password = encryptPassword(password);
				json.put("username", userName);
				json.put("password", password);
				storeDataInCouchBase("DataFromTodoist", json);
			} catch (Exception e) {
				logger.error("error while performing in updateAdminDetailsCouhbase like username & password" +e.getMessage());
			}
		} else {
			try {
				json = new JSONObject();
				password = encryptPassword(password);
				json.put("username", userName);
				json.put("password", password);
				storeDataInCouchBase("DataFromTodoist", json);
			} catch (Exception e) {
				logger.error("error while performing in updateAdminDetailsCouhbase like username & password" +e.getMessage());
			}
		}
	}

	public boolean getLogin(String userName, String password) {

		JSONObject json = null;
		boolean sucess = false;

		try {
			try {
				json = new JSONObject(couchBaseService.getCouhbaseDataByKey("DataFromTodoist").toString());
				client1.shutdown();
			} catch (Exception e) {
				logger.error("error while login" +e.getMessage());
			}
			if (json != null) {
				String pass = null;
				pass = decryptPassword(json.get("password").toString().trim());
				pass = pass.substring(6, pass.length());
				if (userName.trim().equalsIgnoreCase(
						json.get("username").toString().trim())
						&& password.trim().equalsIgnoreCase(pass)) {
					sucess = true;
				} else {
					sucess = false;
				}

			} else {
				sucess = false;
			}

		} catch (Exception e) {
			logger.error("error while login" +e.getMessage());
		}
		return sucess;
	}
	
	/**
	 * To get Current with correct format 
	 * @return
	 */
	private String getSubmissionDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String currentDateTime = (dateFormat.format(cal.getTime()));
		return currentDateTime;
	}            
	
	private JsonObject mapToJson(JSONObject jsonObject) throws JSONException{
		JsonObject json=JsonObject.create();
		Iterator<?> keysItr = jsonObject.keys();
		while (keysItr.hasNext()) {
			String keyString = (String) keysItr.next();
			Object value = jsonObject.get(keyString);
			json.put(keyString, value);
		}
return json;
	}
}
