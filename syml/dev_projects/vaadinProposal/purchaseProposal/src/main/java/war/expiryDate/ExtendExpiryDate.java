package war.expiryDate;

import java.util.Date;



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

import war.util.ExpiryDate;
import war.vaadin.ImageService;
import war.vaadin.ProposalClient;

import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ExtendExpiryDate extends CssLayout implements View   {
	
	 Date  date12=null;
	 public ExtendExpiryDate(String opp_id,String remoteAddress) {
		
		 System.out.println("insdfas---------------"+opp_id);
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
					System.out.println("ffffffffffff"+date12);
					
				}
			});
	 }
	
	 
	 
	
		
		
		 
		 
	

	
	 public void settopbarLayout(){
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			
			horizontalLayout.setSizeFull();
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
			

			Label label1 = new Label("<span style='color:#0070C0;'>R</span>efinance"+" "+"<span style='color:#0070C0;'>P</span>roposal",ContentMode.HTML);
			label1.setStyleName("h1");
			label1.setResponsive(true);
			label1.setWidth("500px");
			
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
	            	
	            	
	            	System.out.println("submit"+date12);
	            	System.out.println("at botton click"+opp_id);
	            	
	            	ExpiryDate.setExpiryDate(date12,opp_id);
	            	
	            	//getUI().getSession().setAttribute("opp_id", opp_id);
	            	ProposalClient proposalClient =new ProposalClient(opp_id,remoteAddress);
	            	//System.out.println("sfasfas");
	            	
	            	getUI().getPage().setLocation("http://localhost:8080/proposalRefinance/?action=B&&opp_id="+opp_id);	
	            	//getUI().getPage().setLocation("https://dev-proposalv.visdom.ca/proposalRefinance-4.0.0/VAADIN?action=B&&opp_id="+opp_id);	
	               
	            }

				
	        });
		  button.setStyleName("primary");
			button.setWidth("10%");
		
		layout.addComponent(button); 
		layout.setComponentAlignment(button,Alignment.MIDDLE_CENTER); 
		 addComponent(layout);
	}
	
	
	}
