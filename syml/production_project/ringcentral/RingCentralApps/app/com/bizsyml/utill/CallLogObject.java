package com.bizsyml.utill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  This class holds the JSON Object returned from Call Log API
 */
public class CallLogObject {
	
	private String uri;
	private List<Map> records = new ArrayList();
	private HashMap<Object, Object> status = new HashMap<Object, Object>();
	private HashMap<Object, Object> paging = new HashMap<Object, Object>();
	private HashMap<Object, Object> navigation = new HashMap<Object, Object>();
	
	
	public CallLogObject(String uri, List<Map> records,
			HashMap<Object, Object> status, HashMap<Object, Object> paging,
			HashMap<Object, Object> navigation) {
		super();
		this.uri = uri;
		this.records = records;
		this.status = status;
		this.paging = paging;
		this.navigation = navigation;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public List<Map> getRecords() {
		return records;
	}
	public void setRecords(List<Map> records) {
		this.records = records;
	}
	public HashMap<Object, Object> getStatus() {
		return status;
	}
	public void setStatus(HashMap<Object, Object> status) {
		this.status = status;
	}
	public HashMap<Object, Object> getPaging() {
		return paging;
	}
	public void setPaging(HashMap<Object, Object> paging) {
		this.paging = paging;
	}
	public HashMap<Object, Object> getNavigation() {
		return navigation;
	}
	public void setNavigation(HashMap<Object, Object> navigation) {
		this.navigation = navigation;
	}
	
	
	

}
