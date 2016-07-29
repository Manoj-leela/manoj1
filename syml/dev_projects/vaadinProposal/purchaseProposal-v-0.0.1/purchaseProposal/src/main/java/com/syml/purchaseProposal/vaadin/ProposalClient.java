package com.syml.purchaseProposal.vaadin;

import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.purchaseProposal.couchbase.*;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.openerp.HttpsConnectionCase;
import com.syml.purchaseProposal.openerp.TestDevCRM;
import com.syml.purchaseProposal.util.JsonConvertion;
import com.vaadin.data.fieldgroup.FieldGroup;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import com.vaadin.ui.CssLayout;

import com.vaadin.ui.Form;

import com.vaadin.ui.Grid;

import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;

import com.vaadin.ui.Notification;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ProposalClient extends CssLayout implements View {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalClient.class);
	@SuppressWarnings("deprecation")
	final Form ff = new Form();
	CleintService clientService = null;
	private Navigator navigator;
	public boolean combinedValue = false;
	public boolean singleValue = false;
	Map<String, String> mapofNotesContents = null;

	CouchbaseData data = new CouchbaseData();
	VaadinFormUtil formUtil = null;

	public ProposalClient(String oppid, String ipAddress) throws CouchbaseDaoServiceException, ProposalException {
		logger.debug("inside ProposalClient coming {} ");
		setSizeUndefined();
		UwAppAllAlgo algo = null;

		formUtil = new VaadinFormUtil();
		clientService = new CleintService();
		algo = data.getDataFromCouchbase(oppid);

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		if (marketingNotes.equals(null) || marketingNotes.isEmpty() || marketingNotes.equals("")) {
			navigator = new Navigator(getUI(), new ErrorDisplay("error "));

		}

		mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		// setSizeFull();
		setWidth("85%");
		setStyleName("csslayoutformargin");

		// setSizeFull();
		// setMargin(true);
		// setSpacing(true);

		settopbarLayout();
		SetApplicantNames(algo);

		setLable(algo);
		getFormLayoutforOriginal(algo);
		if(mapofNotesContents.containsKey("HelpingAchieve")){
		setForHelpingLabel();
		}
		labelforOurRecommendation();

		if (getCompanyName(oppid).equals("WFG")) {
			getFormLayoutforRecommendationcombine(algo);
		} else {
			getFormLayoutforRecommendationSingle(algo);
		}

		if (mapofNotesContents.containsKey("DebtRestructure")) {
			setLableForDebt();
		}

		if (mapofNotesContents.containsKey("Options")) {
			setOptiontLabel();
		}
		if (algo.getRecommendations().stream().anyMatch(rec -> rec.getMortgageType().equals("2"))) {
			setLableforVaible();
			settableVarible(algo);

		}
		
		if(algo.getRecommendations().stream().anyMatch(rec->rec.getMortgageType().equals("3"))){
			setLableforFixed();
			settableFixed(algo);
	
		}
		 
		if (algo.getCombinedRecommendation() != null) {
			setLableforCombinedTable();
			settableForCombined(algo);
		}

		setSumitButton(algo, ipAddress);
		setLabelforNotes();

	}

	public void labelforOurRecommendation() {
		addComponent(formUtil.getLabelForOurRecommendation());

	}

	public void settopbarLayout() {
		addComponent(formUtil.getTopBarDisplay());
	}

	public void setLable(UwAppAllAlgo algo) throws ProposalException {
		try {
			addComponent(formUtil.getLabelForInstructionAndOriginal(algo));
		} catch (CouchbaseDaoServiceException e1) {
			logger.error("error occur In Instruction label value ");
			throw new ProposalException("The Instruction should no be null", e1);
		}
	}

	public void SetApplicantNames(UwAppAllAlgo algoNames) {
		addComponent(formUtil.getApplicantNames(algoNames));
	}

	public void setLableforVaible() {
		addComponent(formUtil.getLabelForVariable(mapofNotesContents.get("VariableRecommendations")));
	}

	public void setLableforFixed() {
		addComponent(formUtil.getLabelForFixed(mapofNotesContents.get("FixedRecommendations")));

	}

	public void setLableforCombinedTable() {
		addComponent(formUtil.getLableforCombinedTable(mapofNotesContents.get("CombinedTable")));

	}

	OriginalDetailForm originalform = new OriginalDetailForm();

	public void getFormLayoutforOriginal(UwAppAllAlgo algo) throws ProposalException {

		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);

		originalform.setMargin(true);
		originalform.setSpacing(true);
		originalform.setStyleName("loppp");

		OrignalDetails original = new OrignalDetails();

		try {
			original = JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));

			FieldGroup binder = new FieldGroup();
			binder.setReadOnly(true);
			BeanItem<OrignalDetails> item = new BeanItem<OrignalDetails>(original);

			binder.setItemDataSource(item);
			binder.bindMemberFields(originalform);

			if (originalform.intialHere.isEnabled() == true) {
				originalform.intialHere.addFocusListener(new FocusListener() {

					public void focus(FocusEvent event) {
						setOriginalEnableOnly();
						ff.setItemDataSource(item);
					}
				});
			} else {

			}
			Verticallayut.addComponent(originalform);
			Verticallayut.setComponentAlignment(originalform, Alignment.MIDDLE_CENTER);
			addComponent(Verticallayut);
		} catch (NullPointerException e) {

			logger.error("error while in OriginalDetails form {} " + e.getMessage());
			throw new ProposalException("error in OriginalDetails Select ", e);
		}

	}

	RecommendationDetailForm recommendationform = new RecommendationDetailForm();

	public void getFormLayoutforRecommendationcombine(UwAppAllAlgo algo) throws ProposalException {
		logger.debug("coming in Combine  REcommendation Details{} ");
		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);

		recommendationform.setMargin(true);
		recommendationform.setSpacing(true);
		recommendationform.setStyleName("loppp");
		try {

			RecommendDetails recdetails = new RecommendDetails();

			recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));

			FieldGroup binder = new FieldGroup();
			binder.setReadOnly(true);
			BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
			binder.setItemDataSource(item);
			binder.bindMemberFields(recommendationform);

			if (recommendationform.initealhere.isEnabled() == true) {

				recommendationform.initealhere.addFocusListener(new FocusListener() {

					public void focus(FocusEvent event) {
						setCombineRecommendationEnableOnly();
						ff.setItemDataSource(item);
					}
				});
			} else {

			}

			Verticallayut.addComponent(recommendationform);
			Verticallayut.setComponentAlignment(recommendationform, Alignment.MIDDLE_CENTER);
			addComponent(Verticallayut);
		} catch (NullPointerException e) {
			logger.error("error while getting in recommendationDetails combine" + e.getMessage());
			throw new ProposalException("error while getting in recommendationDetails combine in Client", e);
		}

	}

	RecommendationDetailForSingleForm singleRecommendationFform = new RecommendationDetailForSingleForm();

	public void getFormLayoutforRecommendationSingle(UwAppAllAlgo algo) throws ProposalException {

		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);

		singleRecommendationFform.setMargin(true);
		singleRecommendationFform.setSpacing(true);
		singleRecommendationFform.setStyleName("loppp");
		singleRecommendationFform.setWidth("650px");
		try {

			RecommendDetails recdetails = new RecommendDetails();

			recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));

			FieldGroup binder = new FieldGroup();
			binder.setReadOnly(true);
			BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
			binder.setItemDataSource(item);
			binder.bindMemberFields(singleRecommendationFform);
			if (singleRecommendationFform.initealhere.isEnabled() == true) {

				singleRecommendationFform.initealhere.addFocusListener(new FocusListener() {

					public void focus(FocusEvent event) {
						setSingleRecommendationEnableOnly();
						ff.setItemDataSource(item);
					}
				});
			} else {

			}

			Verticallayut.addComponent(singleRecommendationFform);
			Verticallayut.setComponentAlignment(singleRecommendationFform, Alignment.MIDDLE_CENTER);
			addComponent(Verticallayut);
		} catch (NullPointerException e) {
			logger.error("error while singleRecommendationFform {} " + e.getMessage());
			throw new ProposalException("error while getting single Recommendation", e);

		}

	}

	Grid tableVariable = new Grid("");

	@SuppressWarnings("deprecation")
	public void settableVarible(UwAppAllAlgo algo) throws CouchbaseDaoServiceException {

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<Recommendation> varibaleBean = clientService.getVaribleBean(algo.getRecommendations());
		clientService.setGridConfig(tableVariable, varibaleBean);
		tableVariable.setSelectionMode(SelectionMode.SINGLE);

		if (tableVariable.isEnabled() == true) {
			listenForTableVaribale(tableVariable, varibaleBean);
		}
		tableVariable = clientService.getGridColumnConfig(tableVariable);
		lay.addComponent(tableVariable);
		lay.setComponentAlignment(tableVariable, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	Grid table = new Grid("");

	@SuppressWarnings("deprecation")
	public void settableFixed(UwAppAllAlgo algo) throws CouchbaseDaoServiceException {

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();
		BeanItemContainer<Recommendation> beanFixed = clientService.getFixedBean(algo.getRecommendations());
		clientService.setGridConfig(table, beanFixed);
		table.setSelectionMode(SelectionMode.SINGLE);

		if (table.isEnabled() == true) {
			listenForTableFixed(table, beanFixed);
		}

		table = clientService.getGridColumnConfig(table);

		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	Grid tableCombined = new Grid("");

	public void settableForCombined(UwAppAllAlgo algo) throws CouchbaseDaoServiceException {

		logger.debug("inside combine table");

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();
		BeanItemContainer<CombinedRecommendation> beanCombined = clientService
				.getCombinedBean(algo.getCombinedRecommendation());
		clientService.setCombineGridConfig(tableCombined, beanCombined);
		tableCombined.setSelectionMode(SelectionMode.SINGLE);
		if (tableCombined.isEnabled() == true) {
			listenForTableCombined(tableCombined, beanCombined);
		}
		tableCombined = clientService.getCombineGridColumnConfig(tableCombined);
		lay.addComponent(tableCombined);
		lay.setComponentAlignment(tableCombined, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	/*
	 * public void ComparisonNotes() throws IndexOutOfBoundsException,
	 * ProposalException {
	 * addComponent(formUtil.getComparisonNotes(mapofNotesContents.get(
	 * "CompareDetails").split("\n")));
	 * 
	 * }
	 */
	public void setForHelpingLabel() {
		addComponent(formUtil.getLabelForHelping(mapofNotesContents.get("HelpingAchieve")));
	}

	/*
	 * public void setLableforMakeSense() {
	 * addComponent(formUtil.getLabelForMakeSense(mapofNotesContents.get(
	 * "WhySense").split("\n"))); }
	 */

	public void setLableForDebt() {
		try {
			addComponent(formUtil.getLabelForDebt(mapofNotesContents.get("DebtRestructure").split("\n")));
		} catch (IndexOutOfBoundsException e) {
			logger.error("index out of bound Exception in Debt-restructure");
		}
	}

	

	
	public void setOptiontLabel() {
		addComponent(formUtil.getLabelForOption(mapofNotesContents.get("Options")));
	}

	public void setLabelforNotes() throws ProposalException {
		addComponent(formUtil.getLabelForNotes(mapofNotesContents.get("Notes").split("\n")));
	}

	@SuppressWarnings("deprecation")
	public void setSumitButton(UwAppAllAlgo algo, String ipaddress) {

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Button button = new Button("Submit");
		button.setStyleName("primary");
		button.setWidth("90%");

		layout.addComponent(button);
		layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
		addComponent(layout);

		button.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				getUI().getNavigator().navigateTo("ThankYou");
				checkForSubmit(algo, ipaddress);
				try {
					clientService.callStageLead(String.valueOf(algo.getOpportunityID()));
				} catch (ProposalException e) {
					logger.error("error callStageLead error : ", e);
				}
			}

		});

	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public void checkForSubmit(UwAppAllAlgo algo, String ipaddress) {

		logger.debug("inside check method for submit : ");
		String originalInitialhere = originalform.intialHere.getValue();
		String recommendationformcombine = recommendationform.initealhere.getValue();
		String singlerecomdataion = singleRecommendationFform.initealhere.getValue();

		if (originalInitialhere.isEmpty() == false) {
			logger.debug("inside original Details");
			try {
				clientService.Originalsubmitcall(algo, ipaddress, ff);
			} catch (ProposalException e) {
				logger.error("Originalsubmitcall " + e.getMessage());
			}
		} else if (recommendationformcombine.isEmpty() == false || singlerecomdataion.isEmpty() == false) {
			logger.debug("inside recommendationDetails");
			try {
				clientService.RecommendationDetailSubmitCall(algo, ipaddress, ff);
			} catch (ProposalException e) {
				logger.error("RecommendationDetailSubmitCall " + e.getMessage());
			}
		} else if (singleValue == true) {
			logger.debug("inside recommendation single value");
			try {
				clientService.RecommendationSingleSubmitCall(algo, ipaddress, ff);
			} catch (ProposalException e) {
				logger.error("RecommendationSingleSubmitCall" + e.getMessage());
			}
		} else if (combinedValue == true) {
			logger.debug("inside recommendation Combine value");
			try {
				clientService.RecommendationCombinedSubmitCall(algo, ipaddress, ff);
			} catch (ProposalException e) {
				logger.error("submit for Recommendation incouchbase" + e.getMessage());

			}

		}
	}

	private String getCompanyName(String oppid) throws ProposalException {
		TestDevCRM test = new TestDevCRM();
		String companyName = null;

		try {
			companyName = HttpsConnectionCase.getCompanyName(oppid);
		} catch (OpeneERPApiException | XmlRpcException e) {
			logger.error("xml Rpc Exception (){} : " + e.getMessage());
			throw new ProposalException("Xml Rpc error occured getting Company  Name Client", e);
		}

		/*
		 * try { companyName = test.getCompanyName(oppid);
		 * 
		 * } catch (XmlRpcException e1) {
		 * 
		 * logger.error("xml Rpc Exception while getting company Name (){} : " +
		 * e1.getMessage()); } catch (OpeneERPApiException e1) { logger.error(
		 * "openerp error thrown here {} : " + e1.getMessage()); }
		 */
		return companyName;

	}

	public void setOriginalEnableOnly() {
		recommendationform.initealhere.setEnabled(false);
		singleRecommendationFform.initealhere.setEnabled(false);
		table.setSelectionMode(SelectionMode.NONE);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void setCombineRecommendationEnableOnly() {
		originalform.intialHere.setEnabled(false);
		table.setSelectionMode(SelectionMode.NONE);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void setSingleRecommendationEnableOnly() {
		originalform.intialHere.setEnabled(false);
		recommendationform.initealhere.setEnabled(false);

		tableVariable.setEnabled(false);
		table.setSelectionMode(SelectionMode.NONE);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void setTableVaribaleEnableOnly() {

		originalform.intialHere.setEnabled(false);
		recommendationform.initealhere.setEnabled(false);
		singleRecommendationFform.setEnabled(false);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		table.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void setTableFixedEnableOnly() {
		originalform.intialHere.setEnabled(false);
		recommendationform.initealhere.setEnabled(false);
		singleRecommendationFform.initealhere.setEnabled(false);
		table.setSelectionMode(SelectionMode.NONE);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void setCombineTableEnableOnly() {
		originalform.intialHere.setEnabled(false);
		recommendationform.initealhere.setEnabled(false);
		singleRecommendationFform.initealhere.setEnabled(false);
		table.setEnabled(false);
		tableVariable.setEnabled(false);
		tableVariable.setSelectionMode(SelectionMode.NONE);
		table.setSelectionMode(SelectionMode.NONE);
		tableCombined.setSelectionMode(SelectionMode.NONE);
	}

	public void listenForTableVaribale(Grid tableVarible, BeanItemContainer<Recommendation> varibaleBean) {
		tableVariable.addSelectionListener(selectionEvent -> {
			Object selected = ((SingleSelectionModel) tableVariable.getSelectionModel()).getSelectedRow();
			if (selected != null) {

				Notification.show("Selected " + tableVariable.getContainerDataSource().getItem(selected)
						.getItemProperty("lender").getValue());
				ff.setItemDataSource(varibaleBean.getItem(selected));
				singleValue = true;
				logger.debug("value of boolean for  variable >>>>>> " + singleValue);
				setTableVaribaleEnableOnly();
			} else {
				Notification.show("Nothing selected");
			}
		});
	}

	public void listenForTableFixed(Grid tableVarible, BeanItemContainer<Recommendation> beanFixed) {
		table.addSelectionListener(selectionEvent -> {
			Object selected = ((SingleSelectionModel) table.getSelectionModel()).getSelectedRow();
			if (selected != null) {
				Notification.show("Selected "
						+ table.getContainerDataSource().getItem(selected).getItemProperty("lender").getValue());
				ff.setItemDataSource(beanFixed.getItem(selected));
				singleValue = true;
				setTableFixedEnableOnly();
				table.setStyleName("gridwithpics128px");
				table.setCellStyleGenerator(
						cell -> "initialforApproval".equals(cell.getItem() == selected) ? "imagecol" : null);
				logger.debug("value of boolean for fixed>>>>>> " + singleValue);

			} else {
				Notification.show("Nothing selected");
			}
		});
	}

	public void listenForTableCombined(Grid tableVarible, BeanItemContainer<CombinedRecommendation> beanCombined) {

		tableCombined.addSelectionListener(selectionEvent -> {

			Object selected = ((SingleSelectionModel) tableCombined.getSelectionModel()).getSelectedRow();

			if (selected != null) {
				Notification.show("Selected " + tableCombined.getContainerDataSource().getItem(selected)
						.getItemProperty("baseLender").getValue());
				ff.setItemDataSource(beanCombined.getItem(selected));
				combinedValue = true;
				logger.debug("value of boolean for Combine>>>>>> " + combinedValue);
				setCombineTableEnableOnly();
				logger.info("get the Item source Combine>>>>>> " + ff.getItemDataSource());
				logger.info("tableCombined"
						+ tableCombined.getContainerDataSource().getItem(selected).getItemProperty("baseProductID"));
			} else {
				Notification.show("Nothing selected");
			}
		});

	}
}
