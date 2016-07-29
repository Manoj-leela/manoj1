package com.syml.proposalRefinance.vaadin;



import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class RecommendationDetailForm  extends GridLayout{
	
	
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
	public TextField mortgageAmount1 =new TextField();
	
	public TextField amortization = new TextField();
	public TextField amortization1 = new TextField();
	
	public TextField mortgageType = new TextField();
	public TextField mortgageType1 = new TextField();
	
	public TextField mortgageTerm = new TextField();
	public TextField mortgageTerm1 = new  TextField();
	
	public TextField paymentAmount = new TextField();
	public TextField paymentAmount1 = new TextField();
	
	public TextField interestRate = new TextField();
	public TextField interestRate1 = new TextField();
	
	
	public TextField lender = new TextField();
	public TextField totalPayment = new TextField();
	public TextField totalMortgageAmount = new TextField();
	
	public TextField initealhere =new TextField();
	
	
	RecommendationDetailForm(){
		super(4,13);
		MarginInfo info = new MarginInfo(true);
		info.setMargins(false,false, false, false);
		setMargin(info);
		
		/*setWidth("800px");
		setHeight("300px");*/
		setWidth("70%");
	/*	setColumnExpandRatio(0, 0.50f);
		setColumnExpandRatio(1, 0.50f);
		setColumnExpandRatio(2, 0.50f);
		setColumnExpandRatio(3, 0.50f);*/
		
		propertvaluelabel.setWidth("200px");
		
		propertvaluelabel.setStyleName("bold");
		downpaymentEquitylabel.setStyleName("bold");
		mortgageamountlabel.setStyleName("bold");
		amortizationlable.setStyleName("bold");
		mortgagetypelable.setStyleName("bold");
		mortgagetermlabel.setStyleName("bold");
		
		paymentamountlabel.setStyleName("bold");
		interestlabel.setStyleName("bold");
		lenderlabel.setStyleName("bold");
		totalmortgageamountlabel.setStyleName("bold");
		totalpaymentlabel.setStyleName("bold");
		initiallabel.setStyleName("bold");
		
		
		propertyValue.setStyleName("borderless");
		downpaymentEquity.setStyleName("borderless");
		mortgageAmount.setStyleName("borderless");
		mortgageAmount1.setStyleName("borderless");
		amortization.setStyleName("borderless");
		
		amortization1.setStyleName("borderless");
		mortgageType.setStyleName("borderless");
		
		mortgageType1.setStyleName("borderless");
		mortgageTerm.setStyleName("borderless");
		
		mortgageTerm1.setStyleName("borderless");
		paymentAmount.setStyleName("borderless");
		
		paymentAmount1.setStyleName("borderless");
		interestRate.setStyleName("borderless");
		
		interestRate1.setStyleName("borderless");
		lender.setStyleName("borderless");
		totalMortgageAmount.setStyleName("borderless");
		totalPayment.setStyleName("borderless");
		
		
		mortgageAmount.setWidth("");
		
		
		addComponent(propertvaluelabel,0,0);
		addComponent(propertyValue,1,0,3,0);
		
		addComponent(downpaymentEquitylabel,0,1);
		addComponent(downpaymentEquity,1,1,3,1);
		
		addComponent(mortgageamountlabel,0,2,0,2);
		addComponent(mortgageAmount,1,2);
		addComponent(mortgageAmount1,3,2);
		
		addComponent(amortizationlable,0,3);
		
		
		
		addComponent(amortization,1,3);
		addComponent(amortization1,3,3);
		
		addComponent(mortgagetypelable,0,4);
		addComponent(mortgageType,1,4);
		addComponent(mortgageType1,3,4);
		
		addComponent(mortgagetermlabel,0,5);
		addComponent(mortgageTerm,1,5);
		addComponent(mortgageTerm1,3,5);
		
		
		addComponent(paymentamountlabel,0,6);
		addComponent(paymentAmount,1,6);
		addComponent(paymentAmount1,3,6);
		
		addComponent(interestlabel,0,7);
		addComponent(interestRate,1,7);
		addComponent(interestRate1,3,7);
		
		addComponent(lenderlabel,0,8);
		
		lender.setReadOnly(true);
		
		addComponent(lender,1,8,3,8);
		addComponent(totalmortgageamountlabel,0,9);
		
		
	
		addComponent(totalMortgageAmount,1,9,3,9);
		
		
		addComponent(totalpaymentlabel,0,10);
		addComponent(totalPayment,1,10,3,10);
		
		addComponent(initiallabel,0,11);
		addComponent(initealhere,1,11,3,11);
	
	
		 Label vSep = new Label();
		    vSep.setStyleName("vertical-separator");
		    vSep.setHeight("100%");
		    vSep.setWidth("1px");
		   addComponent(vSep, 2,2,2,7);
		   setComponentAlignment(propertvaluelabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(propertyValue, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(downpaymentEquitylabel, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(downpaymentEquity, Alignment.MIDDLE_CENTER);
		   
		   setComponentAlignment(mortgageamountlabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(mortgagetypelable, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(mortgagetermlabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(paymentamountlabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(interestlabel, Alignment.MIDDLE_LEFT);
		   
		   setComponentAlignment(lenderlabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(lender, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(totalmortgageamountlabel, Alignment.MIDDLE_LEFT);
		   
		   setComponentAlignment(totalMortgageAmount, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(totalpaymentlabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(totalPayment, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(initiallabel, Alignment.MIDDLE_LEFT);
		   setComponentAlignment(initealhere, Alignment.MIDDLE_CENTER);
		   setComponentAlignment(vSep, Alignment.MIDDLE_LEFT);
		    
				   
		   
		   
		
		
		
	}
}
