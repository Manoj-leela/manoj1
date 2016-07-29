package war.vaadin;




import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Alignment;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;


@SuppressWarnings("serial")
public class OriginalDetailForm extends GridLayout  {
	
	
	/**
	 * 
	 */
	
	Label propertylabel = new Label("Property Value:");
	Label amortizationlable = new Label("Amortization:");
	Label downpaymentEquitylabel = new Label("Downpayment / Equity:");
	Label mortgagelable = new Label("Mortgage Type:");
	
	
	
	Label mortgageamountlabel = new Label("Mortgage Amount:");
	Label mortgagetermlabel = new Label("Mortgage Term:");
	Label insuraceamouuntLabel = new Label("Insurance Amount:");
	Label monthlypaymentlabel = new Label("Monthly Payment:");
	
	
	
	Label mortgageinsurancelabel = new Label("Mortgage + Insurance:");
	Label interestlabel = new Label("Interest Rate:");
	Label lenderlabel = new Label("Lender:");
	Label intialherelabel = new Label("Initial Here:");
	

	
	
	public TextField propertyValue = new TextField();
	
	
	public TextField amortization = new TextField();
	public TextField downpaymentEquity = new TextField();
	public TextField mortgageType = new TextField();
	
	
	
	public TextField mortgageAmount = new TextField();
	public TextField mortgageTerm = new TextField();
	public TextField insuranceAmount = new TextField();
	public TextField paymentAmount = new TextField();
	
	public TextField totalMortgage = new TextField();
	public TextField interestRate = new TextField();
	public TextField lender = new TextField();
	public TextField intialHere = new TextField();
	
	FieldGroup binder = new FieldGroup();
	public OriginalDetailForm() {
		super(5, 7);
		
		
		//	setMargin(true);
		//setSpacing(true);
		
		
	
		
		propertylabel.setStyleName("bold");
		downpaymentEquitylabel.setStyleName("bold");
		insuraceamouuntLabel.setStyleName("bold");
		mortgageamountlabel.setStyleName("bold");
		mortgageinsurancelabel.setStyleName("bold");
		lenderlabel.setStyleName("bold");
		
		amortizationlable.setStyleName("bold");
		mortgagelable.setStyleName("bold");
		
		mortgagetermlabel.setStyleName("bold");
		monthlypaymentlabel.setStyleName("bold");
		interestlabel.setStyleName("bold");
		
		intialherelabel.setStyleName("bold");
		intialHere.setWidth("100px");
		
		propertyValue.setStyleName("borderless");
		downpaymentEquity.setStyleName("borderless");
		mortgageAmount.setStyleName("borderless");
		insuranceAmount.setStyleName("borderless");
		totalMortgage.setStyleName("borderless");
		lender.setStyleName("borderless");
		amortization.setStyleName("borderless");
		
		
		mortgageType.setStyleName("borderless");
		mortgageTerm.setStyleName("borderless");
		paymentAmount.setStyleName("borderless");
		interestRate.setStyleName("borderless");
		
		addComponent(propertylabel, 0, 0);
		addComponent(propertyValue, 1, 0);
		
		
	    addComponent(downpaymentEquitylabel, 0, 1);
	    addComponent(downpaymentEquity, 1, 1);
	    addComponent(mortgageamountlabel, 0, 2);
	    addComponent(mortgageAmount, 1, 2);
	    addComponent(insuraceamouuntLabel, 0, 3);
	    addComponent(insuranceAmount, 1, 3);
	    addComponent(mortgageinsurancelabel, 0, 4);
	    addComponent(totalMortgage, 1, 4);
	    addComponent(lenderlabel, 0, 5);
	    addComponent(lender, 1, 5);
	   
	   
	   Label vSep = new Label();
	    vSep.setStyleName("vertical-separator");
	    vSep.setHeight("100%");
	    vSep.setWidth("1px");
	    
	    addComponent(vSep, 2, 0, 2, 5);
	    addComponent(amortizationlable, 3, 0);
	    addComponent(amortization, 4, 0);
	    addComponent(mortgagelable, 3, 1);
	    addComponent(mortgageType, 4, 1);
	    addComponent(mortgagetermlabel, 3, 2);
	    addComponent(mortgageTerm, 4, 2);
	    addComponent(monthlypaymentlabel, 3, 3);
	    addComponent(paymentAmount, 4, 3);
	    addComponent(interestlabel, 3, 4);
	    addComponent(interestRate, 4, 4);
	    addComponent(intialherelabel, 3, 5);
	    addComponent(intialHere, 4, 5);
	  


	    setComponentAlignment(propertylabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(downpaymentEquitylabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(insuraceamouuntLabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(mortgageamountlabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(mortgageinsurancelabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(lenderlabel, Alignment.MIDDLE_LEFT);
	    
	    setComponentAlignment(amortizationlable, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(mortgagelable, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(mortgagetermlabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(monthlypaymentlabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(interestlabel, Alignment.MIDDLE_LEFT);
	    setComponentAlignment(intialherelabel, Alignment.MIDDLE_LEFT);
		
		
	}

	


	
	
	
	
	

}