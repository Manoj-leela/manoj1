package com.bizsyml.utill;
import java.util.HashMap;

/*
 *  class that holds JSON data returned from RingOut REST API
 */
public class RingStatusObject {
	
	private String uri;
	private int id;
	private HashMap<String, String> status = new HashMap<String, String>();
	
	RingStatusObject(String uri,int id, HashMap<String,String> status){
		this.uri = uri;
		this.status = status;
		this.id = id;
	}

	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<String, String> getStatus() {
		return status;
	}

	public void setStatus(HashMap<String, String> status) {
		this.status = status;
	}

	
}
