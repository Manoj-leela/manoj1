package com.syml.purchaseProposal.expiryDate;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syml.purchaseProposal.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.purchaseProposal.couchbase.dao.ProposalException;
import com.syml.purchaseProposal.util.ExpiryDate;
import com.syml.purchaseProposal.vaadin.ImageService;
import com.syml.purchaseProposal.vaadin.ProposalBroker;
import com.syml.purchaseProposal.vaadin.ProposalClient;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;


import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ExtendExpiryDate extends CssLayout implements View   {
	private static final Logger logger = LoggerFactory.getLogger(ExtendExpiryDate.class);
	 Date  date12=null;
	 public ExtendExpiryDate(String opp_id,String remoteAddress) {
		
		 logger.debug("insdfas---------------"+opp_id);
		 settopbarLayout();
		 
		 
		
		//ExpiryDate.setExpiryDate(add);
		 setDateField();
		 
		 setButtonExpiry(opp_id,remoteAddress);
		
	}
	 
	 
	 public void setDateField(){
		 VerticalLayout layout = new VerticalLayout();
		 layout.setMargin(true);
		 DateField date = new DateField();
		 date.setDateFormat("MM/dd/yyyy");
		 	layout.addComponent(date);
		 	layout.setComponentAlignment(date, Alignment.MIDDLE_CENTER);
			addComponent(layout);
		

		 date.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					date12	 =(Date) event.getProperty().getValue();
					logger.info("The date propoerty get value"+date12);
					
					
				}
			});
	 }
	
	 
	 
	
		
		
		 
		 
	

	
	 public void settopbarLayout(){
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			
			horizontalLayout.setSizeFull();
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
			

			Label label1 = new Label("<span style='color:#0070C0;'>P</span>urchase"+" "+"<span style='color:#0070C0;'>P</span>roposal",ContentMode.HTML);
			label1.setStyleName("h1");
			label1.setResponsive(true);
			label1.setWidth("500px");
	//		Image imagesrc = new Image(null, ImageService.getImageFile());
			Image imagesrc = new Image(null, ImageService.getImageFile());
			
			// imagesrc=		 ImageService.getImage(ImageService.getFile());
			
			 imagesrc.setResponsive(true);
			 imagesrc.setWidth("300px");
			
			 horizontalLayout.addComponent(imagesrc);
			  horizontalLayout.addComponent(label1);
			  horizontalLayout.setComponentAlignment(imagesrc, Alignment.MIDDLE_CENTER);
			  horizontalLayout.setComponentAlignment(label1, Alignment.MIDDLE_RIGHT);
			 addComponent(horizontalLayout);
			
			
		}
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}
		  
	public void setButtonExpiry(String opp_id,String remoteAddress){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		
		
		
		 Button button = new Button("Save",
	                new Button.ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	            	
	            	logger.info("submit on Expirydate  "+date12);
	            	logger.info("the opportunity id"+opp_id);
	            	
	            	
	            	
	            	try {
						ExpiryDate.setExpiryDate(date12,opp_id);
					} catch (CouchbaseDaoServiceException e1) {
						logger.error("error on SetExpiry Date  : "+e1);
					}
	            	
	            	//getUI().getSession().setAttribute("opp_id", opp_id);
	            	try {
						ProposalClient proposalClient =new ProposalClient(opp_id,remoteAddress);
					} catch (CouchbaseDaoServiceException | ProposalException e) {
						logger.error("error on while getting date from Couchbase"+e.getMessage());
						
					}
	            	
	            	
	            	getUI().getPage().setLocation("http://localhost:8080/purchaseProposal/?action=B&&opp_id="+opp_id);	
	            	//getUI().getPage().setLocation("https://dev-proposalpurchase.visdom.ca/purchaseProposal-4.0.0/VAADIN?action=B&&opp_id="+opp_id);	
	               
	            }

				
	        });
		  button.setStyleName("primary");
			button.setWidth("10%");
		
		layout.addComponent(button); 
		layout.setComponentAlignment(button,Alignment.MIDDLE_CENTER); 
		 addComponent(layout);
	}
	
	
	}
