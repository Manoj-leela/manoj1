package com.syml.proposalRefinance.openerp;


import org.apache.xmlrpc.XmlRpcException;
import org.hamcrest.core.IsInstanceOf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;


public class TestDevCRM {
	
	
	private static final Logger logger = LoggerFactory.getLogger(TestDevCRM.class);
	public  String  getCompanyName(String  opp_id) throws XmlRpcException, OpeneERPApiException{
		logger.debug("inside getCompanyName()");	
		String companyName="";

	
	
		OpenERPSessionUtil util = new OpenERPSessionUtil();
		ExtendedSession openERPSession =   util.initSession();

	

	
	try {
		ObjectAdapter partnerAd = openERPSession.getObjectAdapter("crm.lead");
	    
	    FilterCollection filters = new FilterCollection();
	    filters.add("application_no","=",opp_id);
	    RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"x_company"});
	    
	    Row row = partners.get(0);
	    companyName=row.get("x_company").toString();
	    logger.debug("-----------CompanyName:-----------" +companyName);

	} catch (NullPointerException e) {
		  logger.error("Error while reading data from server:\n\n" + e.getMessage());
	  
	}
	return companyName;
	}
	
	
	public  void  updateforReunderwriteRecommmend(String oppertunutyId,String propertyValue,String downpropertyValue,String amortization) throws XmlRpcException, OpeneERPApiException{
		logger.debug("inside update for reunderwriting {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		
		
		
		
		
		try{
	
			
		
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("application_no", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"property_value","down_payment_amount","desired_amortization"});
	    		Row rowData = row.get(0);
	    		rowData.put("property_value", propertyValue);
	    		rowData.put("down_payment_amount", downpropertyValue);
	    		rowData.put("desired_amortization", amortization);
	    		
	    		logger.debug("property value {}:"+rowData.get("property_value"));
	    		logger.debug("down_payment_amount value {}:"+rowData.get("down_payment_amount"));
	    		logger.debug("desired_amortization value {}:"+rowData.get("desired_amortization"));
	    		
	    		
	    		//rowData.put("company",rowData);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("update successfully :");
	    		
	    	   }catch(Exception e){
	    		   
	    	 logger.error("Error in  get company name"+e.getMessage());
	    	
	     }
		
		
	}
	
	
	
	public static void updateProduct(String oppertunutyId,String selected_productID) throws XmlRpcException, OpeneERPApiException  {
		//Session session = new Session("dev-crm.visdom.ca", 443, "symlsys", "guy@visdom.ca", "VisdomTesting");
		logger.debug("inside update for Sourece update product Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		
		
		try{
			Object selectproduct= selected_productID;
			
		logger.debug(oppertunutyId+"----"+selected_productID);
	//Integer selectproduct = Integer.valueOf(selected_productID);
		//logger.debug("Integer Value of Selected product {}: "+selectproduct);
		
	    		ObjectAdapter product=openERPSession.getObjectAdapter("crm.lead");
	    		
	    		FilterCollection productData=new FilterCollection();
	    		productData.add("id","=",oppertunutyId);
	    		logger.info("productData");

	    		
	    		
	    		RowCollection productDataRow=product.searchAndReadObject(productData, new String[]{"id","name","selected_product"});
	    		logger.debug("productDataRow"+productDataRow.size());

	    		Row rowproduct=productDataRow.get(0);

	    		logger.info("Selected  Product"+rowproduct.get("selected_product"));
	    		
	    		rowproduct.put("selected_product",selectproduct);
	    		product.writeObject(rowproduct, true);
	    		
	    	
	    		logger.info("OpenERP  product  Updated");
	     }catch(XmlRpcException |OpeneERPApiException e){
	    	 logger.error("Error in  Udating  product"+e.getMessage());
	     }
	}
	public static void  updateDeal(String name,String  marketing_field,String oppertunutyId ) throws XmlRpcException, OpeneERPApiException{
		
	logger.info("inside update for Sourece update deal Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();



		try{
			logger.info("Inside UpdateDeal OpenERP  Methods");
		
	    		ObjectAdapter product=openERPSession.getObjectAdapter("deal");
	    		
	    		 Row newPartner = product.getNewRow(new String[]{"name", "marketing_field","opportunity_id"});
	 		    newPartner.put("name", name);
	 		    newPartner.put("marketing_field", marketing_field);
	 		    newPartner.put("opportunity_id", oppertunutyId);

	 		   product.createObject(newPartner);
	 		     newPartner.getID();
	    		logger.debug("OpenERP  product  Updated");
	     }catch(Exception e){
	    	 logger.error("Error in  updateDeal   "+e.getMessage());
	     }
	}
	
	public static void  chnageStageToPostSelction(String oppertunutyId ) throws Exception{
logger.info("inside update for changeStageToPostSelection Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();
try{
			logger.info("Inside Update  chnageStageToPostSelction  Methods");
		
			ObjectAdapter opprtunity = openERPSession.getObjectAdapter("crm.lead");
					

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("id", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"stage_id","id" });
	    		Row rowData = row.get(0);
	    		
	    		rowData.put("stage_id",23);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("Stage  chnages  To  post  Selection");
	    	   }catch(Exception e){
	    	 logger.error("Error in  postSelection   product"+e.getMessage());
	     }
	}
	public int getApplicantID(String oppertunutyId){
		logger.info("inside get Applicant ID  () {} "); 
		int payoffid=0;
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		
		try{
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");
			
			

    		FilterCollection filter = new FilterCollection();
    		RowCollection partners = opprtunity.searchAndReadObject(filter,new String[] {"app_rec_ids"});
    		
    		
    		Row row = partners.get(0);
    		Object[] object =  (Object[]) row.get("app_rec_ids");
    		
    		for(Object ob :object){
    			//logger.info("object [] ((((((((((((0)))))))))))"+ob.toString());
    			 payoffid=Integer.parseInt(ob.toString());
    		}
    		    System.out.println("Applicantid:" + row.get("app_rec_ids"));
    		  
    		
    						/*Row rw = row.get("app_rec_ids");		
    						payoffid=	rw.getID();	*/
			
		}catch(Exception e){
			logger.error(""+e.getMessage());
		}
		
		logger.debug("logger payoffIf "+payoffid);
		return payoffid;
	}
	
	public void updateforpayOff(String seqno,String applicantid,String payoff) throws XmlRpcException, OpeneERPApiException{

		logger.info("inside updateOff for applicant Liabilities {}  : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

	
		
		
		
		
		try{
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("applicant.liabilities");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("seq_no", "=", seqno);
	    		filter.add("applicant_id","=",applicantid);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"pay_off"
	    								
	    								});
	    		Row rowData = row.get(0);
	    		rowData.put("pay_off",payoff);
	    		
	    		logger.debug("get Id if row Inserted IN source  {} "+rowData.getID());
	    		
	    		//rowData.put("company",rowData);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("update successfully :");
	    		
		}catch(Exception e){
	    		   
	    	 logger.error("Error in  get Sourece DownPayment name"+e.getMessage());
	    	
	     }
		
		
	
	}
	
	
	
	
	public static void main(String[] args) throws XmlRpcException, OpeneERPApiException {
		
		TestDevCRM crm = new TestDevCRM();
		/*String name = crm.getCompanyName("3584");*/
		//logger.debug("name is coming here "+name);
		
		try {
			TestDevCRM.updateProduct("3584","1889");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//System.out.println("the main() compant name : "+name);
		//new TestDevCRM().insertData();
	}

}
