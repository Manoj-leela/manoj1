package com.syml.proposalRefinance.vaadin;


import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class ComparisonForm extends GridLayout {
	

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@PropertyId("newSituation.newInterestRate")
		TextField newInterestRate= new TextField();
		
		@PropertyId("newSituation.newMortgage")
		TextField newMortgage= new TextField();
		@PropertyId("newSituation.newExtraCash")
		TextField newExtraCash= new TextField();
		
		@PropertyId("newSituation.newInterestToendOldTerm")
		TextField newInterestToendOldTerm= new TextField();
		@PropertyId("newSituation.newPayoutPenalty")
		TextField newPayoutPenalty= new TextField();
		@PropertyId("newSituation.newClosingCost")
		TextField newClosingCost= new TextField();
		@PropertyId("newSituation.newTotalCost")
		TextField newTotalCost= new TextField();
		@PropertyId("newSituation.newCashInHand")
		TextField newCashInHand= new TextField();
	
		
		@PropertyId("currentSituation.currentInterestRate")
		TextField currentInterestRate= new TextField();
		
		@PropertyId("currentSituation.currentMortage")
		TextField currentMortage= new TextField();
		
		
		@PropertyId("currentSituation.currentExtraCash")
		TextField currentExtraCash= new TextField();
		
		
		@PropertyId("currentSituation.currentInterestToendOldTerm")
		TextField currentInterestToendOldTerm= new TextField();
		
		
		@PropertyId("currentSituation.currentPayoutPenalty")
		TextField currentPayoutPenalty= new TextField();
		
		
		@PropertyId("currentSituation.currentClosingCost")
		TextField currentClosingCost= new TextField();
		@PropertyId("currentSituation.currentTotalCost")
		TextField currentTotalCost= new TextField();
		
		@PropertyId("currentSituation.currentCashInHand")
		TextField currentCashInHand= new TextField();
		
		
		Label labelblank= new Label();
		
		Label labelInterestRate= new Label("Interest Rate");
		Label labelMortgage= new Label("Mortgage");
		Label labelExtraCash= new Label("Extra Cash");
		Label labelInterestToend= new Label("Interest to end of Old Term (IEM)");
		Label labelPenaltylayout= new Label("Payout Penalty (approx)");
		Label labelClosingCost= new Label("Closing Costs (approx)");
		Label labelTotalCost= new Label("Total Costs");
		Label labelCashInHand= new Label("Cash In Hand");
		
		Label labelCurrentSolution =new Label("Current (Do Nothing)");
		Label labelNewSolution =new Label("New Solution");
		
		
		
		//furture for Between table
	 
	public ComparisonForm() {
		super(3,9);
		
		
		
		
		//setColumnExpandRatio(1, 1);
		
		labelblank.setStyleName("bold");
		addComponent(labelblank,0,0);
		addComponent(labelCurrentSolution,1,0);
		addComponent(labelNewSolution,2,0);
		labelCurrentSolution.setStyleName("bold");
		labelNewSolution.setStyleName("bold");
		
		
		
		labelInterestRate.setStyleName("bold");
		addComponent(labelInterestRate,0,1);
		addComponent(currentInterestRate,1,1);
		addComponent(newInterestRate,2,1);
		
		currentInterestRate.setStyleName("borderless");
		newInterestRate.setStyleName("borderless");
		
		
		labelInterestRate.setStyleName("bold");
		addComponent(labelMortgage,0,2);
		addComponent(currentMortage,1,2);
		addComponent(newMortgage,2,2);
		currentMortage.setStyleName("borderless");
		newMortgage.setStyleName("borderless");
		
		
		labelExtraCash.setStyleName("bold");
		addComponent(labelExtraCash,0,3);
		addComponent(currentExtraCash,1,3);
		addComponent(newExtraCash,2,3);
		currentExtraCash.setStyleName("borderless");
		newExtraCash.setStyleName("borderless");
		
		
		labelInterestToend.setStyleName("bold");
		addComponent(labelInterestToend,0,4);
		addComponent(currentInterestToendOldTerm,1,4);
		addComponent(newInterestToendOldTerm,2,4);
		currentInterestToendOldTerm.setStyleName("borderless");
		newInterestToendOldTerm.setStyleName("borderless");
	
		labelPenaltylayout.setStyleName("bold");
		
		addComponent(labelPenaltylayout,0,5);
		addComponent(currentPayoutPenalty,1,5);
		addComponent(newPayoutPenalty,2,5);
		currentPayoutPenalty.setStyleName("borderless");
		newPayoutPenalty.setStyleName("borderless");
	
		
		labelClosingCost.setStyleName("bold");
		addComponent(labelClosingCost,0,6);
		addComponent(currentClosingCost,1,6);
		addComponent(newClosingCost,2,6);
		currentClosingCost.setStyleName("borderless");
		newClosingCost.setStyleName("borderless");
	
		
		labelTotalCost.setStyleName("bold");
		addComponent(labelTotalCost,0,7);
		addComponent(currentTotalCost,1,7);
		addComponent(newTotalCost,2,7);
		currentTotalCost.setStyleName("borderless");
		newTotalCost.setStyleName("borderless");
		
		
		labelCashInHand.setStyleName("bold");
		addComponent(labelCashInHand,0,8);
		addComponent(currentCashInHand,1,8);
		addComponent(newCashInHand,2,8);
		currentCashInHand.setStyleName("borderless");
		newCashInHand.setStyleName("borderless");
		
		
		/*addComponent(newClosingCost,1,1);
		addComponent(newCashInHand,1,2);
		addComponent(newInterestToendOldTerm,1,3);*/
		
		
	}
	

}
