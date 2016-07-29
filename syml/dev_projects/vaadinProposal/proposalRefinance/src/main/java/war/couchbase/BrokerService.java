package war.couchbase;

import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import war.util.JsonConvertion;
import war.vaadin.OriginalDetailForm;
import war.vaadin.RecommendationDetailForSingleForm;
import war.vaadin.RecommendationDetailForm;


public class BrokerService {
	CouchbaseData data =new CouchbaseData();
	private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);
	public void saveOriginalDetails(OrignalDetails details,UwAppAllAlgo algo) throws JSONException{
		logger.debug("<<<<<<<<<<<<<<<<<<inside save OriginalDetails>>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes =algo.getMarketingNotes();
		
		for(MarketingNotes marketingNotes:notes){
			if(marketingNotes.getNoteName().equals("OriginalDetails")){
				logger.debug("marketing notecontent befre:"+marketingNotes.getNoteContent());
				try {
					marketingNotes.setNoteContent(JsonConvertion.getJsonObject(details));
					
				} catch (JSONException e) {
					logger.error("json Exception while mapping"+e.getMessage());
				}
				logger.debug("marketing noteconent after:"+marketingNotes.getNoteContent());
			}
		}
		
		
			
			data.updateUwappCouchbase(algo);
		

		
	}
	
	public void saveRecommendDetailsForCombine(RecommendDetails recDetails ,UwAppAllAlgo algo) throws JSONException{

		logger.debug("<<<<<<<<<<<<<<<<<<inside save RecommendDetails >Combine>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes =algo.getMarketingNotes();
		
		for(MarketingNotes marketingNotes:notes){
			if(marketingNotes.getNoteName().equals("RecommendDetails")){
				logger.debug("marketing notecontent befre:"+marketingNotes.getNoteContent());
				try {
					marketingNotes.setNoteContent(JsonConvertion.getJsonObject(recDetails));
					
				} catch (JSONException e) {
					logger.error("json Exception while mapping"+e.getMessage());
				}
				logger.debug("marketing noteconent after:"+marketingNotes.getNoteContent());
			}
		}
		
		
			
			//data.updateUwappCouchbase(algo);
		

		
	
	}
	public void setOriginalDetailsStyleNamewithBorder(OriginalDetailForm originalStyleform, String stylename) {
		logger.debug("inside OriginalDetailForm style name change"+stylename);
		originalStyleform.amortization.setStyleName(stylename);
		originalStyleform.downpaymentEquity.setStyleName(stylename);
		originalStyleform.insuranceAmount.setStyleName(stylename);
		originalStyleform.interestRate.setStyleName(stylename);
		originalStyleform.lender.setStyleName(stylename);
		originalStyleform.mortgageAmount.setStyleName(stylename);
		originalStyleform.mortgageTerm.setStyleName(stylename);
		originalStyleform.mortgageType.setStyleName(stylename);
		originalStyleform.paymentAmount.setStyleName(stylename);
		originalStyleform.propertyValue.setStyleName(stylename);
		originalStyleform.totalMortgage.setStyleName(stylename);

	}
	public void setRecommendationDetailsStyleNamewithBorder(RecommendationDetailForm recommendationform ,String stylename){
		
		logger.debug("inside RecommendationDetailForm style name change"+stylename);
		
		recommendationform.propertyValue.setStyleName(stylename);
		recommendationform.downpaymentEquity.setStyleName(stylename);
		recommendationform.mortgageAmount.setStyleName(stylename);
		recommendationform.mortgageAmount1.setStyleName(stylename);
		recommendationform.amortization.setStyleName(stylename);
		recommendationform.amortization1.setStyleName(stylename);
		recommendationform.mortgageType.setStyleName(stylename);
		recommendationform.mortgageType1.setStyleName(stylename);
		recommendationform.mortgageTerm.setStyleName(stylename);
		recommendationform.mortgageTerm1.setStyleName(stylename);
		recommendationform.paymentAmount.setStyleName(stylename);
		recommendationform.paymentAmount1.setStyleName(stylename);
		recommendationform.interestRate.setStyleName(stylename);
		recommendationform.interestRate1.setStyleName(stylename);
		recommendationform.lender.setStyleName(stylename);
		recommendationform.totalPayment.setStyleName(stylename);
		recommendationform.totalMortgageAmount.setStyleName(stylename);
}
	public void setRecommendationDetailsStyleNamewithBorderSingle(RecommendationDetailForSingleForm single,String stylename){
		logger.debug("inside RecommendationDetailForm style name change for Single"+stylename);
		single.propertyValue.setStyleName(stylename);
		single.downpaymentEquity.setStyleName(stylename);
		single.mortgageAmount.setStyleName(stylename);
		single.amortization.setStyleName(stylename);
		single.mortgageType.setStyleName(stylename);
		single.mortgageTerm.setStyleName(stylename);
		single.paymentAmount.setStyleName(stylename);
		single.interestRate.setStyleName(stylename);
		single.lender.setStyleName(stylename);
		single.totalMortgage.setStyleName(stylename);
		
}
	public void saveRecommendDetailsForSingle(RecommendDetails recDetails,UwAppAllAlgo algo)throws JSONException{
	
		
		
	
	
	

		logger.debug("<<<<<<<<<<<<<<<<<<inside save RecommendDetails Single>>>>>>>>>>>>>>>");
		Set<MarketingNotes> notes =algo.getMarketingNotes();
		
		for(MarketingNotes marketingNotes:notes){
			if(marketingNotes.getNoteName().equals("RecommendDetails")){
				logger.debug("marketing notecontent befre:"+marketingNotes.getNoteContent());
				
					marketingNotes.setNoteContent(JsonConvertion.getJsonForRecommendDetailsSingle(recDetails).toString());
					
				
				logger.debug("marketing noteconent after:"+marketingNotes.getNoteContent());
			}
		}
		
		
			
			//data.updateUwappCouchbase(algo);
		

		
	
	
	}
	
}
