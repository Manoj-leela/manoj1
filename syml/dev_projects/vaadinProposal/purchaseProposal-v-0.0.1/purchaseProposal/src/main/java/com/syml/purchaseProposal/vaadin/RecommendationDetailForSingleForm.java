package com.syml.purchaseProposal.vaadin;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class RecommendationDetailForSingleForm extends GridLayout {
	
	
	Label propertvaluelabel= new Label("Property Value:");
	Label downpaymentEquitylabel= new Label("Downpayment / Equity:");
	Label mortgageamountlabel= new Label("Mortgage Amount:");
	
	
	Label amortizationlable = new Label("Amortization:");
	Label mortgagetypelable = new Label("Mortgage Type:");
	Label mortgagetermlabel = new Label("Mortgage Term:");
	
	Label paymentamountlabel= new Label("Payment Amount:");
	Label interestlabel = new Label("Interest Rate:");
	Label lenderlabel = new Label("Lender:");
	
	Label totalmortgageamountlabel = new Label("Total Mortgage Amount:");
	Label totalpaymentlabel = new Label("Total Payment:");
	
	Label initiallabel = new Label("Initial Here:");
	
	
	
	
	public TextField propertyValue = new TextField();
	public TextField downpaymentEquity = new TextField();
	public TextField mortgageAmount = new TextField();
	public TextField amortization = new TextField();
	public TextField mortgageType = new TextField();
	public TextField mortgageTerm = new TextField();
	public TextField paymentAmount = new TextField();
	public TextField interestRate = new TextField();
	public TextField lender = new TextField();
	public TextField totalMortgage = new TextField();
	public TextField initealhere =new TextField();
	
	
	RecommendationDetailForSingleForm(){
		super(3,12);
		//setCaption("Our Recommendation:");
		//setStyleName("h2");
		//setWidth(400, Sizeable.UNITS_PIXELS);
		setWidth(700,Sizeable.UNITS_PIXELS);
		//setMargin(true);
		//setSpacing(true);
		
		totalMortgage.setNullRepresentation("");
		propertvaluelabel.setStyleName("bold");
		downpaymentEquitylabel.setStyleName("bold");
		mortgageamountlabel.setStyleName("bold");
		amortizationlable.setStyleName("bold");
		mortgagetypelable.setStyleName("bold");
		mortgagetermlabel.setStyleName("bold");
		paymentamountlabel.setStyleName("bold");
		interestlabel.setStyleName("bold");
		lenderlabel.setStyleName("bold");
		initiallabel.setStyleName("bold");
		totalmortgageamountlabel.setStyleName("bold");
		propertyValue.setStyleName("borderless");
		downpaymentEquity.setStyleName("borderless");
		mortgageAmount.setStyleName("borderless");
		amortization.setStyleName("borderless");
		mortgageType.setStyleName("borderless");
		mortgageTerm.setStyleName("borderless");
		
		paymentAmount.setStyleName("borderless");
		interestRate.setStyleName("borderless");
		lender.setStyleName("borderless");
		totalMortgage.setStyleName("borderless");
		
		totalmortgageamountlabel.setWidth("200px");
		
		//initealhere.setWidth("100px");
		
		
		addComponent(propertvaluelabel,0,0);
		addComponent(propertyValue,2,0);
		
		addComponent(downpaymentEquitylabel,0,1);
		addComponent(downpaymentEquity,2,1);
		
		addComponent(mortgageamountlabel,0,2);
		addComponent(mortgageAmount,2,2);
		
		addComponent(amortizationlable,0,3);
		addComponent(amortization,2,3);
		
		addComponent(mortgagetypelable,0,4);
		addComponent(mortgageType,2,4);
		
		addComponent(mortgagetermlabel,0,5);
		addComponent(mortgageTerm,2,5);
		
		addComponent(paymentamountlabel,0,6);
		addComponent(paymentAmount,2,6);
		
		addComponent(interestlabel,0,7);
		addComponent(interestRate,2,7);
		
		addComponent(lenderlabel,0,8);
		addComponent(lender,2,8);
		
		addComponent(totalmortgageamountlabel,0,9);
		addComponent(totalMortgage,2,9);
		
		
		initealhere.setWidth("80px");
		
		//addComponent(initiallabel,0,10);
		//addComponent(initealhere,2,10);
		 
		  Label vSep = new Label();
		    vSep.setStyleName("vertical-separator");
		    vSep.setHeight("100%");
		    vSep.setWidth("1px");
		   addComponent(vSep, 1, 0,1,10);
		
		
		
		//initiallabel.setWidth("200px");
		
		
		
		

		
		
	
		
		
		
	
		
		
		
		
	
		
		
		
		
		
		

		
		
		
		
	
		
		

		
		

		
		
		
		

		
		
	}
}
