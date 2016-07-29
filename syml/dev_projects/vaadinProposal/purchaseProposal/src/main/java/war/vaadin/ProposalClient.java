package war.vaadin;

import java.io.IOException;
import java.sql.SQLException;

import java.util.Map;
import java.util.Set;

import javax.management.modelmbean.XMLParseException;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;

import war.stagelead.RestCall;
import com.debortoliwines.openerp.api.OpeneERPApiException;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;

import com.vaadin.ui.CssLayout;

import com.vaadin.ui.Form;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import war.couchbase.*;
import war.couchbase.dao.service.CouchBaseService;
import war.stagelead.StageLead;
import war.syml.HttpsConnectionCase;
import war.syml.TestDevCRM;

import war.util.ExpiryDate;
import war.util.JsonConvertion;

@SuppressWarnings("serial")
public class ProposalClient extends CssLayout implements View {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalClient.class);
	final Form ff = new Form();

	private Navigator navigator;
	public boolean combinedValue = false;
	public boolean singleValue = false;
	Map<String, String> mapofNotesContents = null;
	ExpiryDate expirydate = new ExpiryDate();
	String expirydateinString = "";
	CouchbaseData data = new CouchbaseData();

	public ProposalClient(String oppid, String ipAddress) {
		setSizeUndefined();
		UwAppAllAlgo algo = null;

		try {

			algo = data.getDataFromCouchbase(oppid);
			Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
			mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		} catch (NullPointerException e) {
			logger.debug("errro in MapNotes Contents {} " + e.getMessage());
		}
		// setSizeFull();
		setWidth("85%");
		setStyleName("csslayoutformargin");

		// setSizeFull();
		// setMargin(true);
		// setSpacing(true);
		logger.debug("inside ProposalClient coming {} ");
		settopbarLayout();
		SetApplicantNames(algo);

		setLable();
		getFormLayoutforOriginal(oppid);
		setForHelpingLabel();
		labelforOurRecommendation();
		if (getCompanyName(oppid).equals("WFG")) {
			getFormLayoutforRecommendationcombine(oppid);
		} else {
			getFormLayoutforRecommendationSingle(oppid);
		}
		//labelforComparison();
		//setComparison();
		setLableforMakeSense();
		setLableForDebt();
		setHighLightLabel();
		setOptiontLabel();
		setLableforVaible();
		settableVarible(oppid);

		setLableforFixed();
		settableFixed(oppid);
		setLableforCombinedTable();
		settableForCombined(oppid);

		setSumitButton(algo, ipAddress);
		setLabelforNotes();

	}

	public void labelforOurRecommendation() {
		VerticalLayout forlable = new VerticalLayout();
		forlable.setMargin(true);
		forlable.setSpacing(true);

		Label label = new Label("Our Recommendation", ContentMode.HTML);
		label.setStyleName("labelcolor");
		forlable.addComponent(label);
		addComponent(forlable);
	}

	public void settopbarLayout() {

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		Label label1 = new Label(
				"<span style='color:#0070C0;font-size: 50px; font-family:eurofurenceregular;'>P</span>"
						+ "<span style='font-size: 50px; font-family:eurofurenceregular;'>urchase</span>" + " "
						+ "<span style='color:#0070C0;font-size: 50px; font-family:eurofurenceregular;'>P</span>"
						+ "<span style='font-size: 50px; font-family:eurofurenceregular;'>roposal</span>",
				ContentMode.HTML);

		label1.setStyleName("bold");

		Image imagesrc = new Image(null, ImageService.getImageFile());

		imagesrc.setWidth("300px");
		imagesrc.setWidth("300px");

		horizontalLayout.addComponent(imagesrc);

		horizontalLayout.addComponent(label1);
		horizontalLayout.setComponentAlignment(imagesrc, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);

		addComponent(horizontalLayout);

	}

	public void setLable() {
		logger.debug("inside setLable()");

		VerticalLayout forlable = new VerticalLayout();
		forlable.setMargin(true);
		forlable.setSpacing(true);

		Label label = new Label("Instructions", ContentMode.HTML);
		Label labelgoal = new Label("Goals:", ContentMode.HTML);
		Label labeloriginl = new Label("Original Desired:", ContentMode.HTML);
		label.setStyleName("labelcolor");
		labelgoal.setStyleName("labelcolor");
		labeloriginl.setStyleName("labelcolor");

		Label labelinstruction = new Label();
		Label labelgoaltext = new Label();
		Label labeloriginaltext = new Label();

		labelinstruction.setStyleName("labeltext");
		labelgoaltext.setStyleName("labeltext");
		labeloriginaltext.setStyleName("labeltext");
		String instruct = mapofNotesContents.get("Instructions");
		String instvalue = instruct.substring(0, 118);

		logger.debug("substring aa >>> " + instvalue + " " + expirydateinString);

		labelinstruction.setValue(instvalue + " " + expirydateinString);
		labelgoaltext.setValue(mapofNotesContents.get("Goals"));
		labeloriginaltext.setValue(mapofNotesContents.get("OriginalDesired"));

		forlable.addComponent(label);
		forlable.addComponent(labelinstruction);
		forlable.addComponent(labelgoal);
		forlable.addComponent(labelgoaltext);
		forlable.addComponent(labeloriginl);
		forlable.addComponent(labeloriginaltext);

		addComponent(forlable);

	}

	public void SetApplicantNames(UwAppAllAlgo algoNames) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);

		String firstName = "";
		String lastName = "";
		for (ApplicantsNames names : algoNames.getApplicantsNames()) {
			firstName = names.getFirstName();
			lastName = names.getLastName();

		}

		Label labelfirstName = new Label(firstName + "" + lastName, ContentMode.HTML);
		Label labelAdress = new Label(algoNames.getAddress(), ContentMode.HTML);
		labelfirstName.setWidth(null);
		labelAdress.setWidth(null);
		labelfirstName.setStyleName("labeltext1");
		labelAdress.setStyleName("labeltext1");
		layout.addComponent(labelfirstName);
		layout.addComponent(labelAdress);

		layout.setComponentAlignment(labelfirstName, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(labelAdress, Alignment.MIDDLE_CENTER);
		addComponent(layout);

	}

	public void setLableforVaible() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("Best Variable Options:");
		Label labelvarible = new Label();

		label.setStyleName("labelcolor");
		labelvarible.setStyleName("bold");
		labelvarible.setValue(mapofNotesContents.get("VariableRecommendations"));

		layout.addComponent(label);
		layout.addComponent(labelvarible);
		addComponent(layout);
	}

	public void setLableforFixed() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("Best Fixed Options:");
		Label fixedvarible = new Label();
		label.setStyleName("labelcolor");
		fixedvarible.setStyleName("bold");

		fixedvarible.setValue(mapofNotesContents.get("FixedRecommendations"));
		layout.addComponent(label);
		layout.addComponent(fixedvarible);
		addComponent(layout);
	}

	public void setLableforCombinedTable() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("Combined Options");
		Label fixedvarible = new Label();
		label.setStyleName("labelcolor");
		fixedvarible.setStyleName("bold");

		fixedvarible.setValue(mapofNotesContents.get("CombinedTable"));
		layout.addComponent(label);
		layout.addComponent(fixedvarible);
		addComponent(layout);
	}

	OriginalDetailForm originalform = new OriginalDetailForm();

	public void getFormLayoutforOriginal(String opportunity) {

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

						recommendationform.initealhere.setEnabled(false);
						singleRecommendationFform.initealhere.setEnabled(false);
						table.setSelectionMode(SelectionMode.NONE);
						tableVariable.setSelectionMode(SelectionMode.NONE);
						tableCombined.setSelectionMode(SelectionMode.NONE);
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
		}

	}

	RecommendationDetailForm recommendationform = new RecommendationDetailForm();

	public void getFormLayoutforRecommendationcombine(String opportunity) {
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
						originalform.intialHere.setEnabled(false);
						table.setSelectionMode(SelectionMode.NONE);
						tableVariable.setSelectionMode(SelectionMode.NONE);
						tableCombined.setSelectionMode(SelectionMode.NONE);
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
		}

	}

	RecommendationDetailForSingleForm singleRecommendationFform = new RecommendationDetailForSingleForm();

	public void getFormLayoutforRecommendationSingle(String opportunity) {

		VerticalLayout Verticallayut = new VerticalLayout();
		Verticallayut.setMargin(true);

		singleRecommendationFform.setMargin(true);
		singleRecommendationFform.setSpacing(true);
		singleRecommendationFform.setStyleName("loppp");
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
						originalform.intialHere.setEnabled(false);
						recommendationform.initealhere.setEnabled(false);
						// originalform.intialHere.setReadOnly(true);

						table.setSelectionMode(SelectionMode.NONE);
						tableVariable.setSelectionMode(SelectionMode.NONE);
						tableCombined.setSelectionMode(SelectionMode.NONE);
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

		}

	}

	Grid tableVariable = new Grid("");

	@SuppressWarnings("deprecation")
	public void settableVarible(String opportunity) {
		double defaultRowsCount = 3.0d;
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<Recommendation> stars = new BeanItemContainer<Recommendation>(Recommendation.class);

		CouchbaseData data = new CouchbaseData();

		UwAppAllAlgo algo = data.getDataFromCouchbase(opportunity);

		Set<Recommendation> rec = algo.getRecommendations();
		for (Recommendation variable : rec) {
			if (variable.getMortgageType().equals("2")) {
				stars.addBean(variable);
			}

		}
		stars.addItem(rec);

		tableVariable.setContainerDataSource(stars);
		tableVariable.setWidth("100%");
		tableVariable.setHeightMode(HeightMode.ROW);

		tableVariable.setStyleName("");
		int size = tableVariable.getContainerDataSource().size();
		double rows = (size > 0) ? size : defaultRowsCount;
		if (rows == 2.0d) {
			rows = 3.0d;
		}
		tableVariable.setHeightByRows(rows);
		tableVariable.setSelectionMode(SelectionMode.SINGLE);
		final CheckBox checkBox = new CheckBox("Multi Select");
		checkBox.setValue(false);
		checkBox.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

			}

		});

		if (tableVariable.isEnabled() == true) {
			tableVariable.addSelectionListener(selectionEvent -> { // Java 8
				// Get selection from the selection model
				Object selected = ((SingleSelectionModel) tableVariable.getSelectionModel()).getSelectedRow();

				if (selected != null) {
					// selectionEvent.getSource().toString();
					Notification.show("Selected "
							+ tableVariable.getContainerDataSource().getItem(selected).getItemProperty("productID"));
					ff.setItemDataSource(stars.getItem(selected));

					singleValue = true;
					logger.debug("value of boolean for  variable >>>>>> " + singleValue);

					originalform.intialHere.setEnabled(false);
					recommendationform.initealhere.setEnabled(false);
					// originalform.intialHere.setEnabled(false);
					singleRecommendationFform.setEnabled(false);
					logger.debug("ItemDataSoruce{}" + ff.getItemDataSource());
					logger.info("taleVaraible"
							+ tableVariable.getContainerDataSource().getItem(selected).getItemProperty("productID"));

					tableVariable.setSelectionMode(SelectionMode.NONE);
					// table.setSelectionMode(SelectionMode.NONE);
				} else {
					Notification.show("Nothing selected");
				}
			});
		}

		tableVariable.setColumns("lender", "product", "term", "maximumAmortization", "payment", "interestRate",
				"initialforApproval");

		tableVariable.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		tableVariable.getColumn("product").setHeaderCaption("Product Name");
		tableVariable.getColumn("initialforApproval").setHeaderCaption("Initial");

		lay.addComponent(tableVariable);
		lay.setComponentAlignment(tableVariable, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	Grid table = new Grid("");

	@SuppressWarnings("deprecation")
	public void settableFixed(String opportunity) {

		double defaultRowsCount = 3.0d;
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<Recommendation> stars = new BeanItemContainer<Recommendation>(Recommendation.class);

		CouchbaseData data = new CouchbaseData();

		UwAppAllAlgo algo = data.getDataFromCouchbase(opportunity);

		Set<Recommendation> rec = algo.getRecommendations();
		for (Recommendation variable : rec) {
			if (variable.getMortgageType().equals("") || variable.getMortgageType().isEmpty()) {
				throw new NullPointerException("error in variable Recommendation{} ");
			} else if (variable.getMortgageType().equals("3")) {

				stars.addBean(variable);
			}

		}
		stars.addItem(rec);

		table.setContainerDataSource(stars);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);

		table.setStyleName("");
		int size = table.getContainerDataSource().size();
		double rows = (size > 0) ? size : defaultRowsCount;
		if (rows == 2.0d) {
			rows = 3.0d;
		}
		table.setHeightByRows(rows);
		table.setSelectionMode(SelectionMode.SINGLE);

		if (table.isEnabled() == true) {

			table.addSelectionListener(selectionEvent -> { // Java 8
				Object selected = ((SingleSelectionModel) table.getSelectionModel()).getSelectedRow();
				if (selected != null) {

					/*
					 * Notification.show("Selected " +
					 * table.getContainerDataSource().getItem(selected).
					 * getItemProperty("productID"));
					 */

					ff.setItemDataSource(stars.getItem(selected));

					singleValue = true;

					table.setStyleName("gridwithpics128px");
					table.setCellStyleGenerator(cell ->

					"initialforApproval".equals(cell.getItem() == selected) ? "imagecol" : null);

					logger.debug("value of boolean for fixed>>>>>> " + combinedValue);

					// ff.setItemDataSource(table.getContainerDataSource().getItem(selected));
					originalform.intialHere.setEnabled(false);
					recommendationform.initealhere.setEnabled(false);
					singleRecommendationFform.initealhere.setEnabled(false);

					logger.info("Item source " + ff.getItemDataSource());

					System.out.println("tableFixed"
							+ table.getContainerDataSource().getItem(selected).getItemProperty("productID"));
					table.setSelectionMode(SelectionMode.NONE);
					tableVariable.setSelectionMode(SelectionMode.NONE);
					tableCombined.setSelectionMode(SelectionMode.NONE);
				} else {
					Notification.show("Nothing selected");
				}
			});

		}

		table.setColumns("lender", "product", "term", "maximumAmortization", "payment", "interestRate",
				"initialforApproval");

		table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		table.getColumn("product").setHeaderCaption("Product Name");
		table.getColumn("initialforApproval").setHeaderCaption("Initial");

		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	Grid tableCombined = new Grid("");

	public void settableForCombined(String opportunity) {

		logger.debug("inside combine table");

		double defaultRowsCount = 3.0d;
		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<CombinedRecommendation> stars = new BeanItemContainer<CombinedRecommendation>(
				CombinedRecommendation.class);

		CouchbaseData data = new CouchbaseData();

		UwAppAllAlgo algo = data.getDataFromCouchbase(opportunity);

		Set<CombinedRecommendation> rec = algo.getCombinedRecommendation();
		for (CombinedRecommendation variable : rec) {
			stars.addBean(variable);
		}
		stars.addItem(rec);

		tableCombined.setContainerDataSource(stars);
		tableCombined.setWidth("100%");
		tableCombined.setHeightMode(HeightMode.ROW);

		tableCombined.setStyleName("");
		int size = tableCombined.getContainerDataSource().size();
		double rows = (size > 0) ? size : defaultRowsCount;
		if (rows == 2.0d) {
			rows = 3.0d;
		}
		tableCombined.setHeightByRows(rows);
		tableCombined.setSelectionMode(SelectionMode.SINGLE);
		if (tableCombined.isEnabled() == true) {
			tableCombined.addSelectionListener(selectionEvent -> { // Java 8

				Object selected = ((SingleSelectionModel) tableCombined.getSelectionModel()).getSelectedRow();

				if (selected != null) {

					Notification.show("Selected " + tableCombined.getContainerDataSource().getItem(selected)
							.getItemProperty("baseProductID"));
					ff.setItemDataSource(stars.getItem(selected));

					combinedValue = true;
					logger.debug("value of boolean for Combine>>>>>> " + combinedValue);

					originalform.intialHere.setEnabled(false);
					recommendationform.initealhere.setEnabled(false);
					singleRecommendationFform.initealhere.setEnabled(false);
					table.setEnabled(false);
					tableVariable.setEnabled(false);
					tableVariable.setSelectionMode(SelectionMode.NONE);
					table.setSelectionMode(SelectionMode.NONE);
					logger.info("get the Item source Combine>>>>>> " + ff.getItemDataSource());
					logger.info("tableCombined"
							+ tableCombined.getContainerDataSource().getItem(selected).getItemProperty("productID"));

					tableCombined.setSelectionMode(SelectionMode.NONE);

				} else {
					Notification.show("Nothing selected");
				}
			});

		}

		tableCombined.setColumns("baseLender", "baseProduct", "additionalProduct", "baseTerm", "additionalTerm",
				"baseAmortization", "additionalAmortization", "baseMortgageAmount", "additionalMortgageAmount",
				"basePayment", "additionalPayment", "baseInterestRate", "additionalInterestRate", "totalPayment");

		HeaderRow extraHeader = tableCombined.prependHeaderRow();
		HeaderCell productjoin = extraHeader.join("baseProduct", "additionalProduct");
		productjoin.setText("Product");
		tableCombined.getColumn("baseLender").setHeaderCaption("Lender");
		HeaderCell termjoined = extraHeader.join("baseTerm", "additionalTerm");
		termjoined.setText("Term");

		HeaderCell amortizationjoined = extraHeader.join("baseAmortization", "additionalAmortization");
		amortizationjoined.setText("Amortization");

		HeaderCell mortageamountjoined = extraHeader.join("baseMortgageAmount", "additionalMortgageAmount");
		mortageamountjoined.setText("Amount");

		HeaderCell paymentjoin = extraHeader.join("basePayment", "additionalPayment");
		paymentjoin.setText("MonthlyPayment");

		HeaderCell interestJoin = extraHeader.join("baseInterestRate", "additionalInterestRate");
		interestJoin.setText("Interest Rate");

		tableCombined.getColumn("totalPayment").setHeaderCaption("Total Mortgage");

		lay.addComponent(tableCombined);
		lay.setComponentAlignment(tableCombined, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}

	public void setForHelpingLabel() {

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labelhelp = new Label();
		Label lablehelptext = new Label();

		labelhelp.setStyleName("labelcolor");
		labelhelp.setValue("Helping You Find the Right Mortgage:");
		lablehelptext.setValue(mapofNotesContents.get("HelpingAchieve"));
		lablehelptext.setStyleName("bold");

		layout.addComponent(labelhelp);
		layout.addComponent(lablehelptext);
		addComponent(layout);

	}

	public void setTable() {

	}

	public void setComparison() {
		VerticalLayout Verticallayut = new VerticalLayout();
		ComparisonForm form = new ComparisonForm();

		Comparison comparison = JsonConvertion.getComparison(mapofNotesContents.get("Comparision"));
		logger.debug("compariosn >>>>>" + comparison);
		FieldGroup binder = new FieldGroup();

		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("gridexample");

		binder.setReadOnly(true);
		BeanItem<Comparison> item = new BeanItem<Comparison>(comparison);
		item.addNestedProperty("currentSituation.currentInterestRate");
		item.addNestedProperty("newSituation.newInterestRate");

		item.addNestedProperty("currentSituation.currentMortage");
		item.addNestedProperty("newSituation.newMortgage");

		item.addNestedProperty("currentSituation.currentExtraCash");
		item.addNestedProperty("newSituation.newExtraCash");

		item.addNestedProperty("currentSituation.currentInterestToendOldTerm");
		item.addNestedProperty("newSituation.newInterestToendOldTerm");

		item.addNestedProperty("currentSituation.currentPayoutPenalty");
		item.addNestedProperty("newSituation.newPayoutPenalty");

		item.addNestedProperty("currentSituation.currentClosingCost");
		item.addNestedProperty("newSituation.newClosingCost");

		item.addNestedProperty("currentSituation.currentTotalCost");
		item.addNestedProperty("newSituation.newTotalCost");

		item.addNestedProperty("currentSituation.currentCashInHand");
		item.addNestedProperty("newSituation.newCashInHand");

		binder.setItemDataSource(item);
		logger.debug("comparins item " + item);
		binder.bindMemberFields(form);

		Verticallayut.addComponent(form);
		Verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);

	}

	public void setLableforMakeSense() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		String sense[] = mapofNotesContents.get("WhySense").split("\n");
		Label senselabel = new Label("Why it Makes Sense");

		senselabel.setStyleName("labelcolor");

		Label sensetext = new Label(sense[0], ContentMode.HTML);
		Label sensetext1 = new Label("<li>" + sense[1] + "</li>", ContentMode.HTML);
		Label sensetext2 = new Label("<li>" + sense[2] + "</li>", ContentMode.HTML);
		sensetext.setStyleName("labeltext");
		sensetext1.setStyleName("labeltext");
		sensetext2.setStyleName("labeltext");

		layout.addComponent(senselabel);
		layout.addComponent(sensetext);
		layout.addComponent(sensetext1);
		layout.addComponent(sensetext2);
		addComponent(layout);
	}

	public void setLableForDebt() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Debt Re-Structuring");

		String debt[] = mapofNotesContents.get("DebtRestructure").split("\n");
		labledebt.setStyleName("labelcolor");
		Label labeldebttext = new Label(debt[0], ContentMode.HTML);
		Label labeldebttext1 = new Label("<li>" + debt[1] + "</li>", ContentMode.HTML);

		layout.addComponent(labledebt);
		labeldebttext.setStyleName("labeltext");
		labeldebttext1.setStyleName("labeltext");

		layout.addComponent(labeldebttext);
		layout.addComponent(labeldebttext1);
		addComponent(layout);
	}

	public void labelforComparison() {
		VerticalLayout forlable = new VerticalLayout();
		forlable.setMargin(true);
		forlable.setSpacing(true);

		Label label = new Label("Comparison", ContentMode.HTML);
		label.setStyleName("labelcolor");
		forlable.addComponent(label);
		addComponent(forlable);
	}

	public void setHighLightLabel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Highlights:");
		labledebt.setStyleName("labelcolor");
		String high[] = mapofNotesContents.get("Highlights").split("\n");

		Label labeldebttext = new Label(high[0], ContentMode.HTML);
		Label labeldebttext1 = new Label("<li>" + high[1] + "</li>", ContentMode.HTML);
		Label labeldebttext2 = new Label("<li>" + high[2] + "</li>", ContentMode.HTML);

		labeldebttext.setStyleName("bold");
		labeldebttext1.setStyleName("bold");
		labeldebttext2.setStyleName("bold");
		layout.addComponent(labledebt);
		layout.addComponent(labeldebttext);
		layout.addComponent(labeldebttext1);
		layout.addComponent(labeldebttext2);

		addComponent(layout);
	}

	public void setOptiontLabel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Options:");
		labledebt.setStyleName("labelcolor");
		Label optionlabeltext = new Label(mapofNotesContents.get("Options"), ContentMode.HTML);
		optionlabeltext.setStyleName("bold");

		layout.addComponent(labledebt);
		layout.addComponent(optionlabeltext);
		addComponent(layout);
	}

	public void setLabelforNotes() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		Label noteslabel = new Label("Notes:");

		String map[] = mapofNotesContents.get("Notes").split("\n");

		Label optionlabeltext = new Label("&nbsp;" + "<li>" + map[0] + "</li>", ContentMode.HTML);
		Label optionlabeltext1 = new Label("&nbsp;" + "<li>" + map[1] + "</li>", ContentMode.HTML);
		optionlabeltext.setStyleName("labeltext");
		optionlabeltext1.setStyleName("labeltext");
		noteslabel.setStyleName("labelcolor");
		layout.addComponent(noteslabel);
		layout.addComponent(optionlabeltext);
		layout.addComponent(optionlabeltext1);
		addComponent(layout);

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

				String originalInitialhere = originalform.intialHere.getValue();
				String recommendationformcombine = recommendationform.initealhere.getValue();
				String singlerecomdataion = singleRecommendationFform.initealhere.getValue();

				logger.debug("originalInitialhere value " + originalInitialhere);
				logger.debug("recommendationformcombine value" + recommendationformcombine);

				if (originalInitialhere.isEmpty() == false) {
					logger.debug("inside original Details");
					Originalsubmitcall(algo,ipaddress);
				} else if (recommendationformcombine.isEmpty() == false || singlerecomdataion.isEmpty() == false) {
					logger.debug("inside recommendationDetails");
					RecommendationDetailSubmitCall(algo, ipaddress);
				}
				else if (singleValue == true) {
					logger.debug("inside recommendation single value");
					RecommendationSingleSubmitCall(algo,ipaddress);
				} else if (combinedValue == true) {
					logger.debug("inside recommendation Combine value");
					RecommendationCombinedSubmitCall(algo,ipaddress);
					
				}
				callStageLead(String.valueOf(algo.getOpportunityID()));
			}

		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private String getCompanyName(String oppid) {
		TestDevCRM test = new TestDevCRM();
		String companyName = null;

		/*
		 try { companyName= HttpsConnectionCase.getCompanyName(oppid); } catch
		 (Exception e) { logger.error("xml Rpc Exception (){} : " +
		 e.getMessage());
		  
		  // TODO Auto-generated catch block
		 
		 }*/
		 

		try {
			companyName = test.getCompanyName(oppid);

		} catch (XmlRpcException e1) {

			logger.error("xml Rpc Exception while getting company Name (){} : " + e1.getMessage());
		} catch (OpeneERPApiException e1) {
			logger.error("openerp error thrown here {} : " + e1.getMessage());
		}

		return companyName;

	}

	private void callStageLead(String oppId) {
		StageLead lead = new StageLead();
		String crmdata = "";
		try {
			crmdata = lead.getcrmLeadFrompostgress(oppId);
			RestCallForStageLead(crmdata);
		} catch (SQLException e) {
			logger.error("sql exception " + e.getMessage());

		} catch (IOException e) {
			logger.error("IO exception " + e.getMessage());

		}
	}

	private void updateProduct(String oppid, String productid) throws OpeneERPApiException {
		try {
			//TestDevCRM.updateProduct(oppid, productid);

			 HttpsConnectionCase.updateProduct(oppid,productid);

		} catch (XmlRpcException e) {
			logger.error("error in  Updating  Product" + e.getMessage());
		}

	}

	private void RestCallForStageLead(String crmdata) {
		logger.debug("inside restcall method() ");
		RestCall restcall = null;
		//restcall = new RestCall(crmdata);
		//restcall.restcallTostagMail(crmdata);
	}
	private void Originalsubmitcall(UwAppAllAlgo algo,String ipaddress) {
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		logger.debug("inside original Details");
		try {
			//logger.debug(">>>>>>>>>>>>>>>>>>>>" + originalInitialhere + "<<<<<<<<<<<<<<<");

			BeanItem<OrignalDetails> recBean = (BeanItem<OrignalDetails>) ff.getItemDataSource();
			logger.debug("OriginaltDetails {} " + recBean.getBean());
			OrignalDetails ori = recBean.getBean();

			updateProduct(String.valueOf(algo.getOpportunityID()), ori.getProductID());

			marketingOperation.updateOriginalDetailsInOpenErp(algo);
			marketingOperation.storeSelectOriginalDetails(algo, ori, ipaddress);

		} catch (NullPointerException | OpeneERPApiException e) {
			logger.error("error while submitting the originalDetails {}", e.getMessage());
		}
		
	}
	public void RecommendationDetailSubmitCall(UwAppAllAlgo algo,String ipaddress){
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		try {
			logger.debug("inside RecommendDetailas Details");

			BeanItem<RecommendDetails> recBean = (BeanItem<RecommendDetails>) ff.getItemDataSource();
			System.out.println("RecommendDetaisl {} " + recBean.getBean());

			RecommendDetails recommedetails = recBean.getBean();
			updateProduct(String.valueOf(algo.getOpportunityID()), recommedetails.getProductID());

			marketingOperation.updateRecomendationDetailsInOpenErp(algo);
			marketingOperation.storeSelectRecommnedation(algo, recommedetails, ipaddress);

		} catch (NullPointerException | OpeneERPApiException e) {
			logger.error("error while storing in couchbase:" + e.getMessage());
		}
	
	}
	public void RecommendationSingleSubmitCall(UwAppAllAlgo algo,String ipaddress){
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();

		logger.debug("coming the recommendattion {} ");

		BeanItem<Recommendation> recBean = (BeanItem<Recommendation>) ff.getItemDataSource();
		Recommendation rec = recBean.getBean();

		logger.debug("while on submit get theproduct " + rec.getProductID());
		try {
			updateProduct(String.valueOf(algo.getOpportunityID()), rec.getProductID());
		} catch (OpeneERPApiException e) {
			logger.error("openerp error while update product" + e.getMessage());
		}
		marketingOperation.updateRecomendationDetailsInOpenErp(algo);
		marketingOperation.storeSelectRecommnedationForSingle(algo, rec, ipaddress);
}
	public void RecommendationCombinedSubmitCall(UwAppAllAlgo algo,String ipaddress){
		MarketingNotesOperation marketingOperation = new MarketingNotesOperation();
		try {
			logger.debug("coming the CombinedValue as true {} ");

			BeanItem<CombinedRecommendation> recBean = (BeanItem<CombinedRecommendation>) ff
					.getItemDataSource();
			CombinedRecommendation rec = recBean.getBean();

			logger.debug("while on submit get theproduct " + rec.getBaseProductID());
			updateProduct(String.valueOf(algo.getOpportunityID()), rec.getBaseProductID());

			marketingOperation.updateRecomendationDetailsInOpenErp(algo);
			marketingOperation.storeSelectRecommnedationForCombined(algo, rec, ipaddress);
		} catch (OpeneERPApiException e) {
			logger.error("error openerp getting updateProduct"+e.getMessage());
		}
	}

}
