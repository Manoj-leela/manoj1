package com.syml.proposalRefinance;

import javax.servlet.annotation.WebServlet;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.LoggerFactory;

import com.couchbase.client.deps.com.lmax.disruptor.TimeoutException;
import com.couchbase.client.java.document.json.JsonObject;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;
import com.syml.proposalRefinance.couchbase.dao.service.CouchBaseService;
import com.syml.proposalRefinance.expiryDate.ExpirtyDatePageCR;
import com.syml.proposalRefinance.expiryDate.ExpiryDatePage;
import com.syml.proposalRefinance.expiryDate.ExtendExpiryDate;
import com.syml.proposalRefinance.util.ExpiryDate;
import com.syml.proposalRefinance.vaadin.ComparisonForm;
import com.syml.proposalRefinance.vaadin.ErrorDisplay;
import com.syml.proposalRefinance.vaadin.ProposalBroker;
import com.syml.proposalRefinance.vaadin.ProposalClient;
import com.syml.proposalRefinance.vaadin.ProposalReferal;
import com.syml.proposalRefinance.vaadin.ThankYou;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.client.communication.AtmospherePushConnection;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.communication.PushConnection;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.PushConfiguration;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */

@SuppressWarnings("serial")
@Theme("mytheme")
@Title("Mortgage Proposal")
@Widgetset("war.MyAppWidgetset")

@Push(transport=Transport.LONG_POLLING,value=PushMode.AUTOMATIC)

public class MyUI extends UI {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MyUI.class);
	private Navigator navigator;

	@WebServlet(value = { "/proposalRefinance/*",
			"/VAADIN/*" }, name = "MyUIServlet", asyncSupported = true)
																		 /* initParams
																		  = { @
																		  WebInitParam
																		  (name
																		  =
																		  "org.atmosphere.websocket.maxIdleTime",
																		  value
																		  =
																		  "5000")
																		  }
																		 )*/
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
		
	}

	@Override
	protected void init(VaadinRequest request) {
		/*VerticalLayout layout = new VerticalLayout();
		
		ComparisonForm form = new ComparisonForm();
		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("gridexample");
		setContent(layout);
		layout.addComponent(form);
		*/
		
		
		
		
		
		
		
		
		
	//	com.vaadin.client.communication.AtmospherePushConnection pushConnection =new AtmospherePushConnection();
		
		//pushConnection.
		
		//getPushConfiguration().setParameter("maxReconnectOnClose", "5");
	
				
		final VerticalLayout layout = new VerticalLayout();

		setContent(layout);
		String action = request.getParameter("action");
		String opp_id = request.getParameter("opp_id");
		logger.debug(" inside Init() -->The coming from url action "+action+ " and opp_id "+opp_id);

		if (opp_id.isEmpty() || action.isEmpty()) {
			ErrorDisplay errorDisplay = new ErrorDisplay("The Opportunity id or action is null");
			layout.addComponent(errorDisplay);
			

		}
		
		
		JsonObject json =getJsonById(opp_id);
		if(json==null){
			ShowErrorDisplayPage(layout, "Opps The given opportunity ID is does not exist !");
		}
		
		
		String remoteAddress = request.getRemoteAddr();
		logger.debug("The remoteaddress >>>>>>>>>>>> " + remoteAddress);

		ExpiryDate date = new ExpiryDate();

		if (action.equals("B")) {
			logger.debug("inside broker>>>>>>>>>>>>> {} " + action);

			try {
				if (date.getCurrentDate() == null || date.getExpiryDate(opp_id) == null) {
					ShowErrorDisplayPage(layout, "Expiry Date is coming null from Couchbase");

				} else {

					ShowBrokerPage(navigator, opp_id, remoteAddress, date, layout);
				}

			} catch (CouchbaseDaoServiceException| ProposalException|TimeoutException e) {
				logger.error("getting expiry date is null" + e.getMessage());
				
			} catch (JSONException e) {
				logger.error("getting json Exception in expiry date" + e.getMessage());
			} 

		} else if (action.equals("C")) {

			logger.debug("nside client   {}" + action);
			try {
				if (date.getCurrentDate() == null || date.getExpiryDate(opp_id) == null) {
					ShowErrorDisplayPage(layout, "Expiry Date is coming null from Couchbase");
				} else {
					ShowClientPage(navigator, opp_id, remoteAddress, date);
				}

			} catch (CouchbaseDaoServiceException | ProposalException e) {
				logger.error("getting expiry date is null" + e.getMessage());
			}
		} else if (action.equals("R")) {

			logger.debug("inside the Refferal" + action);

			try {
				if (date.getCurrentDate() == null || date.getExpiryDate(opp_id) == null) {
					ShowErrorDisplayPage(layout, "Expiry Date is coming null from Couchbase");

				} else {
					ShowRefferalPage(navigator, opp_id, remoteAddress, date, layout);
				}

			} catch (CouchbaseDaoServiceException | ProposalException e) {
				logger.error("getting expiry date is null" + e.getMessage());
			}

		}

	}

	private void ShowErrorDisplayPage(VerticalLayout layout, String msg) {

		ErrorDisplay errorDisplay = new ErrorDisplay(msg);
		layout.addComponent(errorDisplay);
	}

	public void ShowBrokerPage(Navigator navigator, String opp_id, String remoteAddress, ExpiryDate date,
			VerticalLayout layout) throws CouchbaseDaoServiceException, JSONException, ProposalException, TimeoutException {

		if (date.CheckDateDifferece(opp_id)) {

			logger.debug("inside the The Broker condition () : ");
			navigator = new Navigator(this, this);
			navigator.addView("", new ExpiryDatePage(opp_id));

			navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

		} else if (date.getCurrentDate().compareTo(date.getExpiryDate(opp_id)) < 0) {

			ProposalBroker broker = new ProposalBroker(opp_id);

			layout.addComponent(broker);
			

		}
	}

	public void ShowClientPage(Navigator navigator, String opp_id, String remoteAddress, ExpiryDate date)
			throws CouchbaseDaoServiceException, ProposalException {
		if (date.CheckDateDifferece(opp_id)) {

			logger.debug("greater than current Date Client Page");
			navigator = new Navigator(this, this);

			navigator.addView("", new ExpirtyDatePageCR(opp_id));
			navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));
			

		} else {
			logger.debug("less  than current Date");

			navigator = new Navigator(this, this);

			navigator.addView("", new ProposalClient(opp_id, remoteAddress));
			navigator.addView("errorDisplay", new ErrorDisplay("error"));
			navigator.addView("ThankYou", new ThankYou());

		}
	}

	public void ShowRefferalPage(Navigator navigator, String opp_id, String remoteAddress, ExpiryDate date,
			VerticalLayout layout) throws CouchbaseDaoServiceException, ProposalException {
		if (date.CheckDateDifferece(opp_id)) {
			logger.debug("inside greater than current date RefferelPage ");

			navigator = new Navigator(this, this);

			navigator.addView("", new ExpirtyDatePageCR(opp_id));
			navigator.addView("extendExpiry", new ExtendExpiryDate(opp_id, remoteAddress));

		} else {
			logger.debug("inside less than current date RefferealPage ");

			ProposalReferal referal = new ProposalReferal(opp_id);
			layout.addComponent(referal);

		}
	}
	private  JsonObject getJsonById(String opp_id){
		CouchBaseService service = new  CouchBaseService();
		
		JsonObject json=null;
		try {
			json=service.getCouhbaseDataByKey("uwapps2couchbase_allProductAlgoJSON_"+opp_id);
		} catch (CouchbaseDaoServiceException|NullPointerException e1) {
			logger.error("The UwApp data should not be null"+e1.getMessage());
		}
		return json;
	}
}
