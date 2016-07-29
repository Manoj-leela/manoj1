package controllers;

import java.util.List;

public class TotalAssets {
	private List<AssetsParam> vehicle;
	private List<AssetsParam> bankAccount;
	private List<AssetsParam> rrsp;
	private List<AssetsParam> investments;
	private List<AssetsParam> others;
	
	//No Argument Constructor
	public TotalAssets() {
		// TODO Auto-generated constructor stub
	}

	//Argumented Constructor
	
	public TotalAssets(List<AssetsParam> vehicle,
			List<AssetsParam> bankAccount, List<AssetsParam> rrsp,
			List<AssetsParam> investments, List<AssetsParam> others) {
		super();
		this.vehicle = vehicle;
		this.bankAccount = bankAccount;
		this.rrsp = rrsp;
		this.investments = investments;
		this.others = others;
	}
	
	//Getters and Setters
	public List<AssetsParam> getVehicle() {
		return vehicle;
	}

	public void setVehicle(List<AssetsParam> vehicle) {
		this.vehicle = vehicle;
	}

	public List<AssetsParam> getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(List<AssetsParam> bankAccount) {
		this.bankAccount = bankAccount;
	}

	public List<AssetsParam> getRrsp() {
		return rrsp;
	}

	public void setRrsp(List<AssetsParam> rrsp) {
		this.rrsp = rrsp;
	}

	public List<AssetsParam> getInvestments() {
		return investments;
	}

	public void setInvestments(List<AssetsParam> investments) {
		this.investments = investments;
	}

	public List<AssetsParam> getOthers() {
		return others;
	}

	public void setOthers(List<AssetsParam> others) {
		this.others = others;
	}
	
}
