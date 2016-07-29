package war;


import javax.servlet.annotation.WebServlet;

import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;


import war.expiryDate.ExpirtyDatePageCR;
import war.expiryDate.ExpiryDatePage;
import war.expiryDate.ExtendExpiryDate;
import war.util.ExpiryDate;
import war.vaadin.ProposalBroker;
import war.vaadin.ProposalClient;

import war.vaadin.ProposalReferal;
import war.vaadin.ThankYou;


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
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MyUI.class);
	private Navigator navigator;

	@WebServlet(value = { "/proposalRefinance/*", "/VAADIN/*" }, name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
	
		
		

		final VerticalLayout layout = new VerticalLayout();
		
		

		setContent(layout);
		String action = request.getParameter("action");
		String opp_id = request.getParameter("opp_id");
		logger.debug("The opp_id >>>>>>>>>>>> "+opp_id);
		

		String remoteAddress = request.getRemoteAddr();
		logger.debug("The remoteaddress >>>>>>>>>>>> "+remoteAddress);
		
		ExpiryDate date = new ExpiryDate();
		date.getExpiryDate(opp_id);
		if(opp_id.isEmpty() || action.isEmpty()){
			throw new NullPointerException("the action or opp_id Either is null");
		}
		

		if (action.equals("B")) {
			logger.debug("inside broker>>>>>>>>>>>>> {} "+action);
			
			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				logger.debug("inside the The Broker condition () : "); 
				navigator = new Navigator(this, this);
				navigator.addView("", new ExpiryDatePage(opp_id));
				
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {
				logger.debug("inside less less  than B date  {}"+action);
					ProposalBroker broker = new ProposalBroker(opp_id);
				
				layout.addComponent(broker);
				//layout.setComponentAlignment(broker, Alignment.MIDDLE_CENTER);

			}

		} else if (action.equals("C")) {
			
			logger.debug("nside client   {}"+action);
			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				logger.debug("greater than current Date");
				navigator = new Navigator(this, this);
				
				navigator.addView("", new ExpirtyDatePageCR(opp_id));
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {
				logger.debug("less  than current Date");
				
				navigator = new Navigator(this, this);

				navigator.addView("", new ProposalClient(opp_id, remoteAddress));
				navigator.addView("ThankYou", new ThankYou());

			}
		} else if (action.equals("R")) {

			logger.debug("inside the Refferal"+action);

			if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) >= 0) {
				logger.debug("inside greater than current date ");
				
				navigator = new Navigator(this, this);

				navigator.addView("", new ExpirtyDatePageCR(opp_id));
				navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

			} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {
				logger.debug("inside less than current date ");
			
				ProposalReferal referal = new ProposalReferal(opp_id);
				layout.addComponent(referal);

			}

		}

}
	
	

}
