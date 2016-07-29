package com.syml.proposalRefinance.vaadin;

import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.deps.com.lmax.disruptor.TimeoutException;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.syml.proposalRefinance.couchbase.BrokerService;
import com.syml.proposalRefinance.couchbase.CombinedRecommendation;
import com.syml.proposalRefinance.couchbase.Comparison;
import com.syml.proposalRefinance.couchbase.CouchbaseData;
import com.syml.proposalRefinance.couchbase.MarketingNotes;
import com.syml.proposalRefinance.couchbase.MarketingNotesOperation;
import com.syml.proposalRefinance.couchbase.OrignalDetails;
import com.syml.proposalRefinance.couchbase.RecommendDetails;
import com.syml.proposalRefinance.couchbase.Recommendation;
import com.syml.proposalRefinance.couchbase.UwAppAllAlgo;
import com.syml.proposalRefinance.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.proposalRefinance.couchbase.dao.ProposalException;
import com.syml.proposalRefinance.openerp.HttpsConnectionCase;
import com.syml.proposalRefinance.util.ExpiryDate;
import com.syml.proposalRefinance.util.JsonConvertion;
import com.vaadin.data.Container;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;

import com.vaadin.data.validator.RegexpValidator;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.PushConfiguration;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")

public class ProposalBroker extends CssLayout {
	private static final Logger logger = LoggerFactory.getLogger(ProposalBroker.class);

	Map<String, String> mapofNotesContents = null;
	ExpiryDate expirydate = new ExpiryDate();

	CouchbaseData data = null;
	BrokerService brokerService = null;
	VaadinFormUtil formUtil = null;

	public ProposalBroker(String oppid)
			throws CouchbaseDaoServiceException, TimeoutException, JSONException, ProposalException {
		UwAppAllAlgo algo = null;
		logger.debug("inside ProposalBroker Constructor{}", oppid);
		
		init();
		algo = data.getDataFromCouchbase(oppid);

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		if (marketingNotes == null) {
			ErrorDisplay errordisplay = new ErrorDisplay("MaketingNotes occur nuull");
			addComponent(errordisplay);
		} else {
			mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		}
		DisplayAllComponent(algo);

	}
public void init(){
	data = new CouchbaseData();
	
	formUtil = new VaadinFormUtil();
	brokerService = new BrokerService();
}
	private void DisplayAllComponent(UwAppAllAlgo algo)
			throws ProposalException, CouchbaseDaoServiceException, JSONException {
		setSizeFull();
		setWidth("85%");

		setStyleName("csslayoutformargin");

		settopbarLayout();

		SetApplicantNames(algo);

		setLable(algo);

		getFormLayoutforOriginal(algo);
		setForHelpingLabel();
		labelforOurRecommendation();
		if (getCompanyName(String.valueOf(algo.getOpportunityID())).equals("WFG")) {

			getFormLayoutforRecommendationcombine(algo);
		} else {

			getFormLayoutforRecommendationSingle(algo);
		}

		labelforComparison();
		ComparisonNotes();

		if (mapofNotesContents.get("Comparision") != null) {
			setComparison();
		}

		if (mapofNotesContents.get("WhySense") != null) {
			setLableforMakeSense();
		}
		if (mapofNotesContents.get("DebtRestructure") != null) {
			setLableForDebt();
		}
		if (mapofNotesContents.get("Highlights") != null) {
			setHighLightLabel();
		}
		if (mapofNotesContents.get("Options") != null) {
			setOptiontLabel();
		}

		if (algo.getRecommendations().stream().anyMatch(rec -> rec.getMortgageType().equals("2"))) {
			setLableforVaible();
			settableVarible(algo);
		}

		if (algo.getRecommendations().stream().anyMatch(rec -> rec.getMortgageType().equals("3"))) {
			setLableforFixed();
			settableFixed(algo);
		}
		if (algo.getCombinedRecommendation() != null) {
			setLableforCombinedTable();
			settableForCombined(algo);
		}
		setLabelforNotes();
	}

	public void setComparison() throws ProposalException {
		Comparison comparison = JsonConvertion.getComparison(mapofNotesContents.get("Comparision"));
		addComponent(formUtil.getComparisonLayout(comparison));
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

	public void labelforOurRecommendation() {
		addComponent(formUtil.getLabelForOurRecommendation());
	}

	public void labelforComparison() {

		addComponent(formUtil.getLabelForComparison());
	}

	public void SetApplicantNames(UwAppAllAlgo algoNames) {
		addComponent(formUtil.getApplicantNames(algoNames));
	}

	public void setLableforVaible() {
		addComponent(formUtil.getLabelForVariable(mapofNotesContents.get("VariableRecommendations")));

	}

	OrignalDetails original = null;

	public void getFormLayoutforOriginal(UwAppAllAlgo algo) throws ProposalException {

		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);
		OriginalDetailForm form = new OriginalDetailForm();

		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("loppp");
		original = new OrignalDetails();

		try {
			original = JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));
		} catch (NullPointerException e) {
			logger.error("NUll getting in Original Details " + e.getMessage());
			throw new ProposalException("The OriginalDetails should not be null ", e);
		}

		BeanItem<OrignalDetails> item = new BeanItem<OrignalDetails>(original);
		FieldGroup fieldGroup = new BeanFieldGroup<OrignalDetails>(OrignalDetails.class);
		fieldGroup.setReadOnly(true);

		fieldGroup.setItemDataSource(item);
		fieldGroup.bindMemberFields(form);

		Button updateButton = new Button("Save", new ClickListener() {

			public void buttonClick(ClickEvent event) {
				// event.getConnector().getUI().setPollInterval(1000000000);

				form.amortization.addValidator(new RegexpValidator("^[0-9]+$", "Amortization should be in Number"));
				form.interestRate
						.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Interest  should be in Number"));
				brokerService.setOriginalDetailsStyleNamewithBorder(form, "borderless");

				fieldGroup.setReadOnly(true);
				try {

					fieldGroup.commit();

					logger.debug("while stroing the coucbase"
							+ ((BeanItem<OrignalDetails>) fieldGroup.getItemDataSource()).getBean());

					@SuppressWarnings("unchecked") //

					OrignalDetails details = ((BeanItem<OrignalDetails>) fieldGroup.getItemDataSource()).getBean();

					brokerService.saveOriginalDetails(details, algo);

				} catch (CommitException | InvalidValueException e) {
					logger.error("Commit exception while storing couchbase" + e.getMessage());

				} catch (JSONException | ProposalException e) {
					logger.error("error  storing couchbase" + e.getMessage());
				}
				event.getButton().setVisible(false);

			}
		});
		updateButton.setVisible(false);

		Button editButton = new Button("Edit", new ClickListener() {

			public void buttonClick(ClickEvent event) {

			
				logger.debug("push mode is enabled : "
						+ event.getConnector().getUI().getPushConfiguration().getPushMode().isEnabled());
				event.getConnector().getUI().setPollInterval(1000000000);
				// event.getConnector().getUI().setLastHeartbeatTimestamp(1000000);

				fieldGroup.setReadOnly(false);
				updateButton.setVisible(true);
				brokerService.setOriginalDetailsStyleNamewithBorder(form, "border");

			}
		});

		updateButton.setVisible(false);
		form.addComponent(updateButton, 1, 6);
		form.addComponent(editButton, 4, 6);

		Verticallayut.addComponent(form);
		Verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);

	}

	RecommendationDetailForSingleForm singleRecommendationFform = new RecommendationDetailForSingleForm();

	public void getFormLayoutforRecommendationSingle(UwAppAllAlgo algo) throws ProposalException {

		VerticalLayout Verticallayut = new VerticalLayout();

		Verticallayut.setMargin(true);
		Verticallayut.setSpacing(true);

		singleRecommendationFform.setStyleName("loppp");

		RecommendDetails recdetails = null;

		try {

			recdetails = new RecommendDetails();

			recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		} catch (NullPointerException e) {
			logger.error("error while singleRecommendationFform {} " + e.getMessage());
			throw new ProposalException("error getting in RecommendDetails ", e);

		}
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		FieldGroup fieldGroup = new BeanFieldGroup<RecommendDetails>(RecommendDetails.class);
		fieldGroup.setReadOnly(true);

		fieldGroup.setItemDataSource(item);
		fieldGroup.bindMemberFields(singleRecommendationFform);
		Button saveButton = new Button("Save", new ClickListener() {

			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				singleRecommendationFform.amortization
						.addValidator(new RegexpValidator("^[0-9]+$", "Amortization should be in Number"));
				singleRecommendationFform.paymentAmount.addValidator(
						new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Payment Amount  should be in Number"));
				singleRecommendationFform.interestRate
						.addValidator(new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Interest  should be in Number"));
				singleRecommendationFform.mortgageAmount.addValidator(
						new RegexpValidator("[0-9]+(\\.[0-9]{1,2})$", "Mortgage Amount  should be in Number"));

				brokerService.setRecommendationDetailsStyleNamewithBorderSingle(singleRecommendationFform,
						"borderless");
				fieldGroup.setReadOnly(true);
				event.getButton().setVisible(false);
				try {
					fieldGroup.commit();

					logger.debug("while stroing the coucbase"
							+ ((BeanItem<RecommendDetails>) fieldGroup.getItemDataSource()).getBean());
					@SuppressWarnings("unchecked")
					RecommendDetails recdetails = ((BeanItem<RecommendDetails>) fieldGroup.getItemDataSource())
							.getBean();
					recdetails.toString();
					brokerService.saveRecommendDetailsForSingle(recdetails, algo);
					event.getButton().setVisible(false);

				} catch (CommitException e) {
					logger.error("Commit exception while storing couchbase" + e.getMessage());
				} catch (JSONException | ProposalException e) {
					logger.error("error  storing couchbase" + e.getMessage());
				}

			}
		});
		saveButton.setVisible(false);
		Button editButton = new Button("Edit", new ClickListener() {

			public void buttonClick(ClickEvent event) {
				fieldGroup.setReadOnly(false);
				saveButton.setVisible(true);
				brokerService.setRecommendationDetailsStyleNamewithBorderSingle(singleRecommendationFform, "border");
			}
		});
		singleRecommendationFform.addComponent(saveButton, 2, 11);
		singleRecommendationFform.addComponent(editButton, 1, 11);

		Verticallayut.addComponent(singleRecommendationFform);
		Verticallayut.setComponentAlignment(singleRecommendationFform, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);

	}

	RecommendationDetailForm recommendationform = new RecommendationDetailForm();

	public void getFormLayoutforRecommendationcombine(UwAppAllAlgo algo) throws ProposalException {
		logger.debug("coming in Combine  REcommendation Details{} ");
		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);
		Verticallayut.setSpacing(true);

		recommendationform.setMargin(true);
		recommendationform.setSpacing(true);

		recommendationform.setStyleName("loppp");
		RecommendDetails recdetails = null;
		try {

			recdetails = new RecommendDetails();

			recdetails = JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		} catch (NullPointerException e) {
			logger.error("gettting null in RecommendDetails" + e.getMessage());
			throw new ProposalException("error occurrimg RecommendDetails for combine ");
		}

		// FieldGroup binder = new FieldGroup();
		/// binder.setReadOnly(true);
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		FieldGroup fieldGroup = new BeanFieldGroup<RecommendDetails>(RecommendDetails.class);
		fieldGroup.setReadOnly(true);
		// fieldGroup.setEnabled(false);
		fieldGroup.setItemDataSource(item);
		fieldGroup.bindMemberFields(recommendationform);

		Button saveButton = new Button("Save", new ClickListener() {

			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				brokerService.setRecommendationDetailsStyleNamewithBorder(recommendationform, "borderless");
				fieldGroup.setReadOnly(true);
				try {
					fieldGroup.commit();

					logger.debug("while stroing the coucbase"
							+ ((BeanItem<RecommendDetails>) fieldGroup.getItemDataSource()).getBean());
					RecommendDetails recdetails = ((BeanItem<RecommendDetails>) fieldGroup.getItemDataSource())
							.getBean();

					brokerService.saveRecommendDetailsForCombine(recdetails, algo);
					event.getButton().setVisible(false);

				} catch (CommitException e) {
					logger.error("Commit exception while storing couchbase" + e.getMessage());
				} catch (JSONException | ProposalException e) {
					logger.error("json Exception occur  storing couchbase" + e.getMessage());
				}
			}
		});
		saveButton.setVisible(false);
		Button editButton = new Button("Edit", new ClickListener() {

			public void buttonClick(ClickEvent event) {

				fieldGroup.setReadOnly(false);
				saveButton.setVisible(true);
				brokerService.setRecommendationDetailsStyleNamewithBorder(recommendationform, "border");

			}
		});

		recommendationform.addComponent(editButton, 3, 12);
		recommendationform.addComponent(saveButton, 2, 12);

		Verticallayut.addComponent(recommendationform);
		Verticallayut.setComponentAlignment(recommendationform, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);

	}

	public void settableVarible(UwAppAllAlgo algo) throws CouchbaseDaoServiceException, JSONException {
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setHeight("50%");
		lay.setWidth("100%");

		BeanItemContainer<Recommendation> recommedationContainer = brokerService
				.getVaribleBean(algo.getRecommendations());
		GeneratedPropertyContainer gpc = brokerService.getButton(recommedationContainer);

		Grid table = new Grid("", gpc);
		brokerService.setGridConfig(table);

		Container.Indexed indexed = table.getContainerDataSource();
		brokerService.removeRecommendationOnClickDelete(indexed, table, algo);

		table.setEditorEnabled(true);
		brokerService.updateRecommendationOnClickUpdate(table, algo);

		brokerService.setGridColumnConfig(table);

		lay.addComponent(table);

		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);

		addComponent(lay);

	}

	public void settableFixed(UwAppAllAlgo algo) throws CouchbaseDaoServiceException, JSONException {

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();
		BeanItemContainer<Recommendation> recommedationContainer = brokerService
				.getFixedBean(algo.getRecommendations());

		GeneratedPropertyContainer gpc = brokerService.getButton(recommedationContainer);
		Grid table = new Grid("", gpc);
		brokerService.setGridConfig(table);

		Container.Indexed indexed = table.getContainerDataSource();
		brokerService.removeRecommendationOnClickDelete(indexed, table, algo);

		table.setEditorEnabled(true);
		brokerService.updateRecommendationOnClickUpdate(table, algo);
		brokerService.setGridColumnConfig(table);

		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	public void settableForCombined(UwAppAllAlgo algo) throws CouchbaseDaoServiceException, JSONException {

		logger.debug("inside combine table");
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<CombinedRecommendation> recommedationContainer = brokerService
				.getCombinedBean(algo.getCombinedRecommendation());

		GeneratedPropertyContainer gpc = brokerService.getButtonForCombine(recommedationContainer);
		Grid table = new Grid("", gpc);
		brokerService.setGridConfig(table);

		Container.Indexed indexed = table.getContainerDataSource();
		brokerService.removeCombineRecommendationOnClickDelete(indexed, table, algo);

		table.setEditorEnabled(true);

		brokerService.setGridColumnConfigForCombine(table);
		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	public void setForHelpingLabel() {
		addComponent(formUtil.getLabelForHelping(mapofNotesContents.get("HelpingAchieve")));
	}

	public void setLableforFixed() {
		addComponent(formUtil.getLabelForFixed(mapofNotesContents.get("FixedRecommendations")));

	}

	public void setLableforCombinedTable() {
		addComponent(formUtil.getLableforCombinedTable(mapofNotesContents.get("CombinedTable")));
	}

	public void setLableforMakeSense() {

		addComponent(formUtil.getLabelForMakeSense(mapofNotesContents.get("WhySense").split("\n")));

	}

	public void setLableForDebt() {
		addComponent(formUtil.getLabelForDebt(mapofNotesContents.get("DebtRestructure").split("\n")));
	}

	public void setHighLightLabel() {

		addComponent(formUtil.getLabelForHighLight(mapofNotesContents.get("Highlights").split("\n")));

	}

	public void setOptiontLabel() {

		addComponent(formUtil.getLabelForOption(mapofNotesContents.get("Options")));

	}

	public void setLabelforNotes() throws ProposalException {
		addComponent(formUtil.getLabelForNotes(mapofNotesContents.get("Notes").split("\n")));

	}

	public void ComparisonNotes() throws ProposalException {
		addComponent(formUtil.getComparisonNotes(mapofNotesContents.get("CompareDetails").split("\n")));
	}

	private String getCompanyName(String oppid)  {
		// TestDevCRM test = new TestDevCRM();
		String companyName = null;

		try {
			companyName = HttpsConnectionCase.getCompanyName(oppid);
		} catch (OpeneERPApiException | XmlRpcException e) {
			logger.error("openerp error thrown here {} : " + e.getMessage());
			//throw new ProposalException("error get Company name in Broker", e);
		}

		/*
		 * try { companyName = test.getCompanyName(oppid);
		 * 
		 * } catch (XmlRpcException e1) {
		 * 
		 * logger.error("xml Rpc Exception (){} : " + e1.getMessage()); } catch
		 * (OpeneERPApiException e1) { logger.error(
		 * "openerp error thrown here {} : " + e1.getMessage()); }
		 */

		return companyName;

	}

}
