package war.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.renderers.ButtonRenderer;

import war.couchbase.CombinedRecommendation;
import war.couchbase.Comparison;
import war.couchbase.CouchbaseData;
import war.couchbase.MarketingNotes;
import war.couchbase.MarketingNotesOperation;
import war.couchbase.OrignalDetails;
import war.couchbase.RecommendDetails;
import war.couchbase.Recommendation;
import war.couchbase.UwAppAllAlgo;
import war.syml.HttpsConnectionCase;
import war.syml.TestDevCRM;
import war.util.Consideration;
import war.util.ExpiryDate;
import war.util.JsonConvertion;



@SuppressWarnings("serial")
public class ProposalReferal extends CssLayout {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProposalReferal.class);
	TestDevCRM test = new TestDevCRM();
	 Map<String,String> mapofNotesContents=null;
	 ExpiryDate expirydate =new ExpiryDate();
	 String expirydateinString="";

	public ProposalReferal(String opp) {
		setSizeUndefined();
		
		CouchbaseData data = new CouchbaseData();
		
		
		try{
		UwAppAllAlgo algo =  data.getDataFromCouchbase(opp);
		Set<MarketingNotes> marketingNotes = algo.getMarketingNotes();
		mapofNotesContents= MarketingNotesOperation.getNoteContentByNotes(marketingNotes);
		}catch(Exception e){
			logger.debug("errro in MapNotes Contents {} "+e.getMessage());
		}
		
		setSizeFull();
		setWidth("85%");

		setStyleName("csslayoutformargin");
		
		
		settopbarLayout();
		 
		 
		 
		//settable("4406");
		
		setLable();
		
		getFormLayoutforOriginal(opp);
		setForHelpingLabel();
		labelforOurRecommendation();
		if(getCompanyName(opp).equals("WFG")){
			getFormLayoutforRecommendationcombine(opp);
			}else{
				getFormLayoutforRecommendationSingle(opp);
			}
		//getFormLayoutforRecommendation(opp);
		//labelforComparison();
		//setComparison();
		setLableforMakeSense();
		setLableForDebt();
		setHighLightLabel();
		setOptiontLabel();
		setLableforVaible();
		settableVarible(opp);
		setLableforFixed();
		settableFixed(opp);
		setLableforCombinedTable();
		settableForCombined(opp);
		setLabelforNotes();
		
		
		
	}
	public void labelforOurRecommendation(){
		VerticalLayout forlable = new VerticalLayout();
		forlable.setMargin(true);
		forlable.setSpacing(true);

		Label label = new Label("Our Recommendation", ContentMode.HTML);
		label.setStyleName("labelcolor");
		forlable.addComponent(label);
	}
	public void settopbarLayout(){

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
	
	public void setLableforFixed(){
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
	public void  setLableforCombinedTable(){
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
public void settableForCombined(String opportunity) {
		
		logger.debug("inside combine table");

		double defaultRowsCount = 3.0d;

		VerticalLayout lay = new VerticalLayout();
		lay.setMargin(true);
		lay.setSizeFull();

		BeanItemContainer<CombinedRecommendation> stars = new BeanItemContainer<CombinedRecommendation>(CombinedRecommendation.class);

		CouchbaseData data = new CouchbaseData();

		UwAppAllAlgo algo = data.getDataFromCouchbase(opportunity);

		Set<CombinedRecommendation> rec = algo.getCombinedRecommendation();
		for (CombinedRecommendation variable : rec) {
			stars.addBean(variable);
		}
		stars.addItem(rec);
		// label.setValue(algo.getAlgo());

		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(stars);

		
			

		

		Grid table = new Grid("", gpc);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);

		table.setStyleName("");
		Container.Indexed indexed = table.getContainerDataSource();
		System.out.println("indexed  ::" + indexed);

		int size = table.getContainerDataSource().size();
		double rows = (size > 0) ? size : defaultRowsCount; // Cannot set height
															// to zero rows. So
															// if no data, set
															// height to some
															// arbitrary number
															// of (empty) rows.
		if (rows == 2.0d) {
			rows = 3.0d; // Workaround for weird bug where a value of "2 rows" (
							// 2.0d - 2.7d ) causes a huge slowdown and major
							// CPU load, and page never finishes updating.
		}
		table.setHeightByRows(rows);

		
		
		table.setEditorEnabled(false);
		

			
			
		// table.setSelectable(true);
		// table.setColumnWidth(new Object[]{"lender"},10);
		table.setColumns("baseLender", "baseProduct", "additionalProduct", "baseTerm", "additionalTerm", "baseAmortization",
				"additionalAmortization","baseMortgageAmount","additionalMortgageAmount","basePayment","additionalPayment","baseInterestRate","additionalInterestRate",
				"totalPayment");
		
		HeaderRow extraHeader = table.prependHeaderRow();
		HeaderCell productjoin = extraHeader.join("baseProduct", "additionalProduct");
		productjoin.setText("Product");
		table.getColumn("baseLender").setHeaderCaption("Lender");
		HeaderCell termjoined= extraHeader.join("baseTerm","additionalTerm");
		termjoined.setText("Term");
		
		
		HeaderCell amortizationjoined= extraHeader.join("baseAmortization","additionalAmortization");
		amortizationjoined.setText("Amortization");
		
		
		HeaderCell mortageamountjoined= extraHeader.join("baseMortgageAmount","additionalMortgageAmount");
		mortageamountjoined.setText("Amount");
		
		HeaderCell paymentjoin= extraHeader.join("basePayment","additionalPayment");
		paymentjoin.setText("MonthlyPayment");
		
		

		HeaderCell interestJoin= extraHeader.join("baseInterestRate","additionalInterestRate");
		interestJoin.setText("Interest Rate");
		

		table.getColumn("totalPayment").setHeaderCaption("Total Mortgage");

		lay.addComponent(table);
		lay.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		addComponent(lay);

	}
	
	public  void setLable(){
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
		String instruct =mapofNotesContents.get("Instructions");
		String instvalue =instruct.substring(0, 118);
		
		logger.debug("substring aa >>> "+instvalue+" "+expirydateinString);
		
		

		labelinstruction.setValue(instvalue+" "+expirydateinString);
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
	public void setLableforVaible(){
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
	
	public void getFormLayoutforOriginal(String opportunity){
		
		VerticalLayout Verticallayut= new VerticalLayout();
		Verticallayut.setMargin(true);
		OriginalDetailForm form = new OriginalDetailForm();
		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("loppp");
		OrignalDetails original =new OrignalDetails();
		
		
		original =JsonConvertion.fromJsonforOriginalDetails(mapofNotesContents.get("OriginalDetails"));
		
		FieldGroup binder = new FieldGroup();
		BeanItem<OrignalDetails> item = new BeanItem<OrignalDetails>(original);
		binder.setItemDataSource(item);
		binder.bindMemberFields(form);
		
		
		
		
		
		Verticallayut.addComponent(form);
		Verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);
		
		
		}
	RecommendationDetailForm recommendationform = new RecommendationDetailForm();
	public void getFormLayoutforRecommendationcombine(String opportunity){
		System.out.println("coming in Combine  REcommendation Details{} ");
		VerticalLayout Verticallayut= new VerticalLayout();
		Verticallayut.setMargin(true);
		
		recommendationform.setMargin(true);
		recommendationform.setSpacing(true);
		recommendationform.setStyleName("loppp");
		try{
		
		RecommendDetails recdetails =new RecommendDetails();
		
		recdetails =JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		
		FieldGroup binder = new FieldGroup();
		binder.setReadOnly(true);
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		binder.setItemDataSource(item);
		binder.bindMemberFields(recommendationform);
		
		
		/*if(recommendationform.initealhere.isReadOnly()==false){
			recommendationforgetFormLayoutforRecommendationm.initealhere.addFocusListener(new FocusListener() {
	 
            public void focus(FocusEvent event) {
            	originalform.intialHere.setReadOnly(true);
            	 table.setSelectionMode(SelectionMode.NONE);
            	 ff.setItemDataSource(item);	 
            }
		});
		}else{
			
			return;
		}*/
		
		
		Verticallayut.addComponent(recommendationform);
		Verticallayut.setComponentAlignment(recommendationform, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);
		}catch(NullPointerException e){}
		
		}
	RecommendationDetailForSingleForm singleRecommendationFform  =  new RecommendationDetailForSingleForm();
	public void getFormLayoutforRecommendationSingle(String opportunity){
	System.out.println("coming in Single  REcommendation Details{} ");
	VerticalLayout Verticallayut= new VerticalLayout();
	Verticallayut.setMargin(true);
	
		singleRecommendationFform.setMargin(true);
		singleRecommendationFform.setSpacing(true);
		singleRecommendationFform.setStyleName("loppp");
	try{
	
	RecommendDetails recdetails =new RecommendDetails();
	
	recdetails =JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
	
	FieldGroup binder = new FieldGroup();
	binder.setReadOnly(true);
	BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
	binder.setItemDataSource(item);
	binder.bindMemberFields(singleRecommendationFform);
	
	
	/*if(recommendationform.initealhere.isReadOnly()==false){
		recommendationform.initealhere.addFocusListener(new FocusListener() {
 
        public void focus(FocusEvent event) {
        	originalform.intialHere.setReadOnly(true);
        	 table.setSelectionMode(SelectionMode.NONE);
        	 ff.setItemDataSource(item);	 
        }
	});
	}else{
		
		return;
	}*/
	
	
	Verticallayut.addComponent(singleRecommendationFform);
	Verticallayut.setComponentAlignment(singleRecommendationFform, Alignment.MIDDLE_CENTER);
	addComponent(Verticallayut);
	}catch(NullPointerException e){
		logger.error("error while singleRecommendationFform {} "+e.getMessage());
		
	}
	
	}
	public void getFormLayoutforRecommendation(String opportunity){
		
		VerticalLayout Verticallayut= new VerticalLayout();
		Verticallayut.setMargin(true);
		RecommendationDetailForm form = new RecommendationDetailForm();
		form.setMargin(true);
		form.setSpacing(true);
		form.setStyleName("loppp");
		
		RecommendDetails recdetails =new RecommendDetails();
		recdetails =JsonConvertion.fromJsonforRecommendDetails(mapofNotesContents.get("RecommendDetails"));
		
		
		
		FieldGroup binder = new FieldGroup();
		BeanItem<RecommendDetails> item = new BeanItem<RecommendDetails>(recdetails);
		binder.setItemDataSource(item);
		binder.bindMemberFields(form);
		
		
		
		
		
		Verticallayut.addComponent(form);
		Verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);
		
		
		}
	
	
	public void settableVarible(String opportunity) {
		
		double defaultRowsCount = 3.0d;
	HorizontalLayout lay = new HorizontalLayout();
		lay.setMargin(true);
		
		lay.setHeight("50%");
		lay.setWidth("100%");
		BeanItemContainer<Recommendation> stars =
			    new BeanItemContainer<Recommendation>(Recommendation.class);
		
		CouchbaseData data = new CouchbaseData();
		
		UwAppAllAlgo algo =  data.getDataFromCouchbase(opportunity);
	
		Set<Recommendation> rec = algo.getRecommendations();
		for(Recommendation variable: rec){
			if(variable.getMortgageType().equals("2")){
			stars.addBean(variable);
			}
			
		}
		stars.addItem(rec);
		//label.setValue(algo.getAlgo());
		
		Grid table = new Grid("",stars);
		table.setWidth("100%");
		table.setHeightMode(HeightMode.ROW);
		
		table.setStyleName("");
		int size = table.getContainerDataSource().size();
        double rows = ( size > 0 ) ? size :defaultRowsCount ; // Cannot set height to zero rows. So if no data, set height to some arbitrary number of (empty) rows.
        if ( rows == 2.0d ) {
            rows = 3.0d; // Workaround for weird bug where a value of "2 rows" ( 2.0d - 2.7d ) causes a huge slowdown and major CPU load, and page never finishes updating.
        }
        table.setHeightByRows(rows);
       
	
		table.setImmediate(true);
		
		table.setColumns("lender", "product","term","maximumAmortization","payment","interestRate","initialforApproval");
		
		
		table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
		table.getColumn("product").setHeaderCaption("Product Name");
		table.getColumn("initialforApproval").setHeaderCaption("Initial");
	
		
		//table.setColumnHeaders("Lender","Product Name","Term","Amortization","Monthly Payment","Interest Rate","Initial For Approval");
		lay.addComponent(table);
		lay.setComponentAlignment(table,Alignment.MIDDLE_CENTER);
		
		addComponent(lay);
	
		}
	public void settableFixed(String opportunity){
		
		double defaultRowsCount = 3.0d;
		
		
		VerticalLayout lay = new VerticalLayout();
			lay.setMargin(true);
			
		
			BeanItemContainer<Recommendation> stars =
				    new BeanItemContainer<Recommendation>(Recommendation.class);
			
			CouchbaseData data = new CouchbaseData();
			
			UwAppAllAlgo algo =  data.getDataFromCouchbase(opportunity);
		
			Set<Recommendation> rec = algo.getRecommendations();
			for(Recommendation variable: rec){
				if(variable.getMortgageType().equals("3")){
				stars.addBean(variable);
				}
				
			}
			stars.addItem(rec);
			//label.setValue(algo.getAlgo());
			
			Grid table = new Grid("",stars);
			table.setWidth("100%");
			table.setHeightMode(HeightMode.ROW);
			
			table.setStyleName("");
			
			int size = table.getContainerDataSource().size();
	        double rows = ( size > 0 ) ? size :defaultRowsCount ; // Cannot set height to zero rows. So if no data, set height to some arbitrary number of (empty) rows.
	        if ( rows == 2.0d ) {
	            rows = 3.0d; // Workaround for weird bug where a value of "2 rows" ( 2.0d - 2.7d ) causes a huge slowdown and major CPU load, and page never finishes updating.
	        }
	        table.setHeightByRows(rows);
			
			//table.setSelectable(true);
			//table.setColumnWidth(new Object[]{"lender"},10);
			table.setColumns("lender", "product","term","maximumAmortization","payment","interestRate","initialforApproval");

			
			table.getColumn("maximumAmortization").setHeaderCaption("Amortization");
			table.getColumn("product").setHeaderCaption("Product Name");
			table.getColumn("initialforApproval").setHeaderCaption("Initial");
			
			//table.setColumnHeaders("Lender","Product Name","Term","Amortization","Monthly Payment","Interest Rate","Initial For Approval");
			
			lay.addComponent(table);
			addComponent(lay);
		
			
		
		
	}
	public void setForHelpingLabel(){
		
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
	
	
	public void setTable(){
		
	}
	public void setComparison(){
		VerticalLayout Verticallayut = new VerticalLayout();
		ComparisonForm form = new ComparisonForm();
		
		
			Comparison comparison =	JsonConvertion.getComparison(mapofNotesContents.get("Comparision"));
				logger.debug("compariosn >>>>>"+comparison);
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
		http://stackoverflow.com/questions/32967850/vaadin-grid-layout-borders
		binder.setItemDataSource(item);
		logger.debug("comparins item "+item);
		binder.bindMemberFields(form);
		
		

		Verticallayut.addComponent(form);
		Verticallayut.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		addComponent(Verticallayut);


	/*logger.debug("compariosn >>>>>"+comparison.getCurrentSituation().getCashInHand());
	logger.debug("compariosn >>>>>"+comparison.getNewSituation().getCashInHand());
	*/

		
		
	}
	public void setLableforMakeSense(){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		String sense[] = mapofNotesContents.get("WhySense").split("\n");
		Label senselabel = new Label("Why it Makes Sense");
		
		
		senselabel.setStyleName("labelcolor");

		
		Label sensetext = new Label(sense[0],ContentMode.HTML);
		Label sensetext1 = new Label("<li>"+sense[1]+"</li>",ContentMode.HTML);
		Label sensetext2 = new Label("<li>"+sense[2]+"</li>",ContentMode.HTML);
		sensetext.setStyleName("labeltext");
		sensetext1.setStyleName("labeltext");
		sensetext2.setStyleName("labeltext");

		layout.addComponent(senselabel);
		layout.addComponent(sensetext);
		layout.addComponent(sensetext1);
		layout.addComponent(sensetext2);
		addComponent(layout);
	}
	
	public void setLableForDebt(){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Debt Re-Structuring");
		
		String debt[] =mapofNotesContents.get("DebtRestructure").split("\n");
		labledebt.setStyleName("labelcolor");
		Label labeldebttext = new Label(debt[0],ContentMode.HTML);
		Label labeldebttext1 = new Label("<li>"+debt[1]+"</li>",ContentMode.HTML);
				
		layout.addComponent(labledebt);
		labeldebttext.setStyleName("labeltext");
		labeldebttext1.setStyleName("labeltext");

		layout.addComponent(labeldebttext);
		layout.addComponent(labeldebttext1);
		addComponent(layout);
	}
	public void labelforComparison(){
		VerticalLayout forlable = new VerticalLayout();
		forlable.setMargin(true);
		forlable.setSpacing(true);

		Label label = new Label("Comparison", ContentMode.HTML);
		label.setStyleName("labelcolor");
		forlable.addComponent(label);
		addComponent(forlable);
	}
	public void setHighLightLabel(){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Highlights:");
		labledebt.setStyleName("labelcolor");
		String high[] =mapofNotesContents.get("Highlights").split("\n");
		
		
				
		Label labeldebttext = new Label(high[0],ContentMode.HTML);
		Label labeldebttext1 = new Label("<li>"+high[1]+"</li>",ContentMode.HTML);
		Label labeldebttext2= new Label("<li>"+high[2]+"</li>",ContentMode.HTML);
		
		labeldebttext.setStyleName("bold");
		labeldebttext1.setStyleName("bold");
		labeldebttext2.setStyleName("bold");
		layout.addComponent(labledebt);
		layout.addComponent(labeldebttext);
		layout.addComponent(labeldebttext1);
		layout.addComponent(labeldebttext2);
		
		addComponent(layout);
	}
	
	public void setOptiontLabel(){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label labledebt = new Label("Options:");
		labledebt.setStyleName("labelcolor");
		Label optionlabeltext= new Label(mapofNotesContents.get("Options"),ContentMode.HTML);
		optionlabeltext.setStyleName("bold");
				

		layout.addComponent(labledebt);
		layout.addComponent(optionlabeltext);
		addComponent(layout);
	}
	
	public void setLabelforNotes(){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		Label noteslabel = new Label("Notes:");
		
	String map[]	= mapofNotesContents.get("Notes").split("\n");

	


		Label optionlabeltext = new Label("&nbsp;"+"<li>"+map[0]+"</li>", ContentMode.HTML);
		Label optionlabeltext1 = new Label("&nbsp;"+"<li>"+map[1]+"</li>", ContentMode.HTML);
		optionlabeltext.setStyleName("labeltext");
		optionlabeltext1.setStyleName("labeltext");
		noteslabel.setStyleName("labelcolor");
		layout.addComponent(noteslabel);
		layout.addComponent(optionlabeltext);
		layout.addComponent(optionlabeltext1);
		addComponent(layout);

	}
	private String getCompanyName(String oppid){
		TestDevCRM test = new TestDevCRM();
		
		String companyName=null;
		try {
			companyName=	HttpsConnectionCase.getCompanyName(oppid);
		} catch (Exception e) {
			logger.error("xml Rpc Exception (){} : " + e.getMessage());
			
		}
		
		
		/*
		try {
			companyName  = test.getCompanyName(oppid);
			
		} catch (XmlRpcException e1) {

			logger.error("xml Rpc Exception (){} : "+e1.getMessage()); 
		} catch (OpeneERPApiException e1) {
			logger.error("openerp error thrown here {} : "+e1.getMessage());
		}*/
		return  companyName;
		
	}
	
}
