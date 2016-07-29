package com.syml.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)

public class ProjectDetails {
	
	@Override
	public String toString() {
		return "ProjectDetails [leadName=" + leadName + ", projectId="
				+ projectId + ", assignedUser=" + assignedUser + ", token="
				+ token + "]";
	}
	private String leadName;
	public String getLeadName() {
		return leadName;
	}
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	private String projectId;
	private String assignedUser;
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	/**
	 * 
	 */
	private String token;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
