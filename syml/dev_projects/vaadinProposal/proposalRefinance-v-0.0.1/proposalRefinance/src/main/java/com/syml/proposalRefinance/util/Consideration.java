package com.syml.proposalRefinance.util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Consideration {
	
	public Consideration(String name, String current, String newblanded, String newSolution) {
		super();
		this.name = name;
		this.current = current;
		this.newblanded = newblanded;
		this.newSolution = newSolution;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getNewblanded() {
		return newblanded;
	}
	public void setNewblanded(String newblanded) {
		this.newblanded = newblanded;
	}
	public String getNewSolution() {
		return newSolution;
	}
	public void setNewSolution(String newSolution) {
		this.newSolution = newSolution;
	}
	private String name;
	private String current;
	private String newblanded;
	private String newSolution;
	

}
