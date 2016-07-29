package com.syml.purchaseProposal.vaadin;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syml.purchaseProposal.couchbase.ApplicantsNames;
import com.syml.purchaseProposal.couchbase.Comparison;
import com.syml.purchaseProposal.couchbase.MarketingNotes;
import com.syml.purchaseProposal.couchbase.MarketingNotesOperation;
import com.syml.purchaseProposal.couchbase.UwAppAllAlgo;
import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.ExpiryDate;
import com.syml.purchaseProposal.vaadin.ImageService;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class VaadinFormUtil {
	private static final Logger logger = LoggerFactory.getLogger(VaadinFormUtil.class);
	private HorizontalLayout horizontalLayout;
	private VerticalLayout verticalLayout;
	ExpiryDate expirydate = new ExpiryDate();

	public HorizontalLayout getTopBarDisplay() {
		horizontalLayout = new HorizontalLayout();
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

		horizontalLayout.addComponent(imagesrc);

		horizontalLayout.addComponent(label1);
		horizontalLayout.setComponentAlignment(imagesrc, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);

		return horizontalLayout;

	}

	public VerticalLayout getLabelForOurRecommendation() {
		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);

		Label label = new Label("Our Recommendation", ContentMode.HTML);
		label.setStyleName("labelcolor");
		verticalLayout.addComponent(label);
		return verticalLayout;
	}

	public VerticalLayout getLabelForVariable(String variableValue) {
		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label label = new Label("Best Variable Options:");
		Label labelvarible = new Label();

		label.setStyleName("labelcolor");
		labelvarible.setStyleName("bold");
		labelvarible.setValue(variableValue);

		verticalLayout.addComponent(label);
		verticalLayout.addComponent(labelvarible);
		return verticalLayout;
	}

	public VerticalLayout getLabelForFixed(String fixedValue) {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label label = new Label("Best Fixed Options:");
		Label fixedvarible = new Label();
		label.setStyleName("labelcolor");
		fixedvarible.setStyleName("bold");

		fixedvarible.setValue(fixedValue);
		verticalLayout.addComponent(label);
		verticalLayout.addComponent(fixedvarible);

		return verticalLayout;
	}

	public VerticalLayout getLableforCombinedTable(String combineValue) {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label label = new Label("Combined Options");
		Label fixedvarible = new Label();
		label.setStyleName("labelcolor");
		fixedvarible.setStyleName("bold");

		fixedvarible.setValue(combineValue);
		verticalLayout.addComponent(label);
		verticalLayout.addComponent(fixedvarible);
		return verticalLayout;

	}

	public VerticalLayout getLabelForOption(String optionValue) {
		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label labledebt = new Label("Options:");
		labledebt.setStyleName("labelcolor");
		Label optionlabeltext = new Label(optionValue);
		optionlabeltext.setStyleName("labeltext");

		verticalLayout.addComponent(labledebt);
		verticalLayout.addComponent(optionlabeltext);
		return verticalLayout;
	}

	public VerticalLayout getLabelForInstructionAndOriginal(UwAppAllAlgo algo)
			throws CouchbaseDaoServiceException, ProposalException {

		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		Map<String, String> mapofNotesContents = MarketingNotesOperation.getNoteContentByNotes(marketingNotes);

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);

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
		String instvalue = "";
		try {
			String instruct = mapofNotesContents.get("Instructions");
			instvalue = instruct.substring(0, 118);
		} catch (NullPointerException e) {
			logger.error("The Instruction Value not be null>> " + e.getMessage());
			// throw new ProposalException("The Instruction value shuld not be
			// null :");

		}

		labelinstruction.setValue(instvalue + " " + expirydate.getFormatDate(String.valueOf(algo.getOpportunityID())));
		labelgoaltext.setValue(mapofNotesContents.get("Goals"));
		labeloriginaltext.setValue(mapofNotesContents.get("OriginalDesired"));

		verticalLayout.addComponent(label);
		verticalLayout.addComponent(labelinstruction);
		verticalLayout.addComponent(labelgoal);
		verticalLayout.addComponent(labelgoaltext);
		verticalLayout.addComponent(labeloriginl);
		verticalLayout.addComponent(labeloriginaltext);

		return verticalLayout;

	}

	public VerticalLayout getApplicantNames(UwAppAllAlgo algoNames) {
		verticalLayout = new VerticalLayout();

		verticalLayout.setSizeFull();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);

		String firstName = "";
		String lastName = "";
		for (ApplicantsNames names : algoNames.getApplicantsNames()) {
			firstName = names.getFirstName();
			lastName = names.getLastName();

		}

		Label labelfirstName = new Label(firstName + "  " + lastName, ContentMode.HTML);
		Label labelAdress = new Label(algoNames.getAddress(), ContentMode.HTML);
		Label labelCity = new Label(algoNames.getCity(), ContentMode.HTML);
		labelfirstName.setWidth(null);
		labelAdress.setWidth(null);
		labelCity.setWidth(null);

		labelfirstName.setStyleName("labeltext1");
		labelAdress.setStyleName("labeltext1");
		labelCity.setStyleName("labeltext1");

		verticalLayout.addComponent(labelfirstName);
		verticalLayout.addComponent(labelAdress);
		verticalLayout.addComponent(labelCity);

		verticalLayout.setComponentAlignment(labelfirstName, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(labelAdress, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(labelCity, Alignment.MIDDLE_CENTER);

		return verticalLayout;

	}

	public VerticalLayout getComparisonLayout(Comparison comparison) {

		verticalLayout = new VerticalLayout();
		ComparisonForm form = new ComparisonForm();
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

		verticalLayout.addComponent(form);
		verticalLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		return verticalLayout;

	}

	public VerticalLayout getLabelForComparison() {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);

		Label label = new Label("Comparison", ContentMode.HTML);
		label.setStyleName("labelcolor");
		verticalLayout.addComponent(label);
		return verticalLayout;
	}

	public VerticalLayout getLabelForHelping(String helpingValue) {
		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label labelhelp = new Label();
		Label lablehelptext = new Label();

		labelhelp.setStyleName("labelcolor");
		labelhelp.setValue("Helping You Find the Right Mortgage:");
		lablehelptext.setValue(helpingValue);
		lablehelptext.setStyleName("labeltext");

		verticalLayout.addComponent(labelhelp);
		verticalLayout.addComponent(lablehelptext);
		return verticalLayout;
	}

	public VerticalLayout getLabelForHighLight(String[] high) {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label labledebt = new Label("Highlights:");
		labledebt.setStyleName("labelcolor");

		Label labeldebttext = new Label(high[0], ContentMode.HTML);
		Label labeldebttext1 = new Label("<li>" + high[1].replaceFirst("-", "") + "</li>", ContentMode.HTML);
		Label labeldebttext2 = new Label("<li>" + high[2].replaceFirst("-", "") + "</li>", ContentMode.HTML);

		labeldebttext.setStyleName("bold");
		labeldebttext1.setStyleName("bold");
		labeldebttext2.setStyleName("bold");
		verticalLayout.addComponent(labledebt);
		verticalLayout.addComponent(labeldebttext);
		verticalLayout.addComponent(labeldebttext1);
		verticalLayout.addComponent(labeldebttext2);

		return verticalLayout;

	}

	public VerticalLayout getLabelForDebt(String[] debt) {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		Label labledebt = new Label("Debt Re-Structuring");
		labledebt.setStyleName("labelcolor");

		verticalLayout.addComponent(labledebt);

		for (String debtValue : debt) {
			verticalLayout.addComponent(
					new Label("&nbsp;" + "<li><b>" + debtValue.replaceFirst("-", "") + "</b></li>", ContentMode.HTML));

		}

		return verticalLayout;

	}

	public VerticalLayout getLabelForNotes(String[] map) throws ProposalException {
		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);

		Label noteslabel = new Label("Notes:");
		noteslabel.setStyleName("labelcolor");
		verticalLayout.addComponent(noteslabel);
		for (String value : map) {
			verticalLayout.addComponent(
					new Label("&nbsp;" + "<li><b>" + value.replaceFirst("-", "") + "</b></li>", ContentMode.HTML));
		}
		return verticalLayout;

	}

	public VerticalLayout getComparisonNotes(String[] mapComparison) throws ProposalException {
		verticalLayout = new VerticalLayout();

		verticalLayout.setMargin(true);

		Label optionlabeltext = null;
		Label optionlabeltext1 = null;

		try {

			optionlabeltext = new Label("&nbsp;" + "<li>" + mapComparison[0] + "</li>", ContentMode.HTML);
			optionlabeltext1 = new Label("&nbsp;" + "<li>" + mapComparison[1] + "</li>", ContentMode.HTML);
			optionlabeltext.setStyleName("labeltext");
			optionlabeltext1.setStyleName("labeltext");

		} catch (IndexOutOfBoundsException e) {
			logger.error("index bound exception for notes " + e.getMessage());
			throw new ProposalException("Index Bound exception occur in Comparison Notes", e);
		}

		verticalLayout.addComponent(optionlabeltext);

		verticalLayout.addComponent(optionlabeltext1);

		return verticalLayout;

	}

	public VerticalLayout getLabelForMakeSense(String[] sense) {

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);

		Label senselabel = new Label("Why it Makes Sense");

		senselabel.setStyleName("labelcolor");

		Label sensetext = new Label(sense[0], ContentMode.HTML);
		Label sensetext1 = new Label("<li>" + sense[1].replaceFirst("-", "") + "</li>", ContentMode.HTML);
		Label sensetext2 = new Label("<li>" + sense[2].replaceFirst("-", "") + "</li>", ContentMode.HTML);
		sensetext.setStyleName("labeltext");
		sensetext1.setStyleName("labeltext");
		sensetext2.setStyleName("labeltext");

		verticalLayout.addComponent(senselabel);
		verticalLayout.addComponent(sensetext);
		verticalLayout.addComponent(sensetext1);
		verticalLayout.addComponent(sensetext2);

		return verticalLayout;
	}

}
