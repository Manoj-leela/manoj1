package com.syml.proposalRefinance.couchbase;

public class CurrentSituation {

	private String currentMortage;
	private String currentExtraCash;
	private String currentInterestToendOldTerm;
	private String currentPayoutPenalty;
	private String currentClosingCost;
	private String currentTotalCost;
	private String currentCashInHand;

	private String currentInterestRate;

	public String getCurrentInterestRate() {
		return currentInterestRate;
	}

	public void setCurrentInterestRate(String currentInterestRate) {
		this.currentInterestRate = currentInterestRate;
	}

	public String getCurrentMortage() {
		return currentMortage;
	}

	public void setCurrentMortage(String currentMortage) {
		this.currentMortage = currentMortage;
	}

	public String getCurrentExtraCash() {
		return currentExtraCash;
	}

	public void setCurrentExtraCash(String currentExtraCash) {
		this.currentExtraCash = currentExtraCash;
	}

	public String getCurrentInterestToendOldTerm() {
		return currentInterestToendOldTerm;
	}

	public void setCurrentInterestToendOldTerm(String currentInterestToendOldTerm) {
		this.currentInterestToendOldTerm = currentInterestToendOldTerm;
	}

	public String getCurrentPayoutPenalty() {
		return currentPayoutPenalty;
	}

	public void setCurrentPayoutPenalty(String currentPayoutPenalty) {
		this.currentPayoutPenalty = currentPayoutPenalty;
	}

	public String getCurrentClosingCost() {
		return currentClosingCost;
	}

	public void setCurrentClosingCost(String currentClosingCost) {
		this.currentClosingCost = currentClosingCost;
	}

	public String getCurrentTotalCost() {
		return currentTotalCost;
	}

	public void setCurrentTotalCost(String currentTotalCost) {
		this.currentTotalCost = currentTotalCost;
	}

	public String getCurrentCashInHand() {
		return currentCashInHand;
	}

	public void setCurrentCashInHand(String currentCashInHand) {
		this.currentCashInHand = currentCashInHand;
	}

}
