package com.syml.purchaseProposal.vaadin;



import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ThankYou extends CssLayout implements View  {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThankYou(){
		settopbarLayout();
		setLableThankyou();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
public void settopbarLayout(){
		
		//setSizeFull();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		
		
		
		
		
		
		
		Label label1 = new Label("<span style='color:#0070C0;font-size: 60px;'>R</span>"+"<font size='7'>efinance</font>"+" "+"<span style='color:#0070C0;font-size: 60px;'>P</span>"+"<font size='7'>roposal</font>",ContentMode.HTML);
		label1.setWidth("500px");
		
		label1.setStyleName("h2");
		
			Image imagesrc= new Image(null,ImageService.getImageFile());
		
			        
		
		
			imagesrc.setWidth("300px");
		 
		 horizontalLayout.addComponent(imagesrc);
		
		 horizontalLayout.addComponent(label1);
		  horizontalLayout.setComponentAlignment(label1, Alignment.MIDDLE_RIGHT);
		 addComponent(horizontalLayout);
		
	}


public void setLableThankyou(){

	
	VerticalLayout forlable = new VerticalLayout();
	forlable.setMargin(true);
	forlable.setSpacing(true);
	
	
	Label label = new Label("Thank You ",ContentMode.HTML);
	
	
	label.setStyleName("labelcolor");
	
	
	
	Label label2=new Label("Thank you for reviewing your proposal and selecting mortgage product.\n"
			+"We have sent you an email confirming the product you have chosen and will shortly be sending your\n"
			+"application to the appropriate lender\n"
	
			+"In the event there is any need for clarification,we will be in touch with you via email");
	label2.setStyleName("bold");
	
	
	
	
	
	

			
	forlable.addComponent(label);
	forlable.addComponent(label2);
	
	
	
	addComponent(forlable);
	

  

}




}
