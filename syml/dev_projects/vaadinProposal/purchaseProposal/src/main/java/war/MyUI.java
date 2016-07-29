package war;


import javax.servlet.annotation.WebServlet;



import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;

import war.expiryDate.ExpirtyDatePageCR;
import war.expiryDate.ExpiryDatePage;
import war.expiryDate.ExtendExpiryDate;
import war.util.ExpiryDate;
import war.vaadin.ProposalBroker;
import war.vaadin.ProposalClient;
import war.vaadin.ProposalReferal;
import war.vaadin.ThankYou;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@SuppressWarnings("serial")
@Theme("mytheme")
@Title("Mortgage Proposal")
@Widgetset("war.MyAppWidgetset")

public class MyUI extends UI {
	private Navigator navigator;

	@WebServlet(value = { "/purchaseProposal/*", "/VAADIN/*" }, name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		/*
		GridLayout gl = new GridLayout(3,12);
	 gl.addStyleName("gridexample");
	    
	  gl.setMargin(true);
	   gl.setSpacing(true);
	    gl.setSizeFull();
	    gl.setColumnExpandRatio(1, 1);
	    
	    gl.addComponent(new Label("Property Value:"), 0, 0);
	    gl.addComponent(new TextField(), 2,0);
	    gl.addComponent(new Label("Amortization:"), 0, 1);
	    gl.addComponent(new TextField(), 2, 1);
	    gl.addComponent(new Label("Downpayment / Equity:"), 0, 2);
	    gl.addComponent(new TextField(), 2, 2);
	    gl.addComponent(new Label("Mortgage Type:"), 0, 3);
	    gl.addComponent(new TextField(), 2, 3);
	    gl.addComponent(new Label("Property Value:"), 0, 4);
	    gl.addComponent(new TextField(), 2,4);
	    	   gl.addComponent(new Label("Amortization:"), 0, 5);
    gl.addComponent(new TextField(), 2, 5);
	    gl.addComponent(new Label("Downpayment00000000000 / Equity:"), 0, 6);
	    gl.addComponent(new TextField(), 2, 6);
	    gl.addComponent(new Label("Mortgage Type:"), 0, 7);
	    gl.addComponent(new TextField(), 2, 7);
	    gl.addComponent(new Label("Amortization:"), 0, 8);
	    gl.addComponent(new TextField(), 2, 8);
	    gl.addComponent(new Label("Downpayment00000000000 / Equity:"), 0, 9);
	    gl.addComponent(new TextField(), 2, 9);
	    gl.addComponent(new Label("Mortgage Type:"), 0, 10);
	    gl.addComponent(new TextField(), 2, 10);
	    gl.addComponent(new Label("Mortgage Type:"), 0, 11);
	    gl.addComponent(new TextField(), 2, 11);

	    Label vSep = new Label();
	    vSep.addStyleName("vertical-separator");
	    vSep.setHeight("100%");
	    vSep.setWidth("1px");
	    gl.addComponent(vSep, 1, 0,1,11);

	    
	    
	  

	   
	  
	   
	  
		
		
		setContent(gl);
		*/
		

		final VerticalLayout layout = new VerticalLayout();
		
		

		setContent(layout);
		String action = request.getParameter("action");
		String opp_id = request.getParameter("opp_id");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + opp_id);

		String remoteAddress = request.getRemoteAddr();
		System.out.println(">>>>>>>>>>>>>>>remoteaddress:{}: " + remoteAddress);
		ExpiryDate date = new ExpiryDate();
		date.getExpiryDate(opp_id);

		if (action.equals("B")) {
			System.out.println("inside broker>>>>>>>>>>>>> {} ");
			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				System.out.println("inside less greateher thang B  {} ");
				navigator = new Navigator(this, this);
				navigator.addView("", new ExpiryDatePage(opp_id));
				
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {

				System.out.println("inside less less  thang B  {} ");
				System.out.println("borker : ");
				ProposalBroker broker = new ProposalBroker(opp_id);
				
				layout.addComponent(broker);
				//layout.setComponentAlignment(broker, Alignment.MIDDLE_CENTER);

			}

		} else if (action.equals("C")) {
			System.out.println("inside client   {} ");

			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				System.out.println("inside less greateher thang C  {} ");
				navigator = new Navigator(this, this);
				
				navigator.addView("", new ExpirtyDatePageCR(opp_id));
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {

				System.out.println("client : ");
				navigator = new Navigator(this, this);

				navigator.addView("", new ProposalClient(opp_id, remoteAddress));
				navigator.addView("ThankYou", new ThankYou());

			}
		} else if (action.equals("R")) {

			System.out.println("inside Referal   {} ");

			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				System.out.println("inside less greateher thang R  {} ");
				navigator = new Navigator(this, this);

				navigator.addView("", new ExpirtyDatePageCR(opp_id));
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {

				System.out.println("referal : ");
				ProposalReferal referal = new ProposalReferal(opp_id);
				layout.addComponent(referal);

			}

		}

}

}
