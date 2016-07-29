package com.syml.purchaseProposal.vaadin;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ErrorDisplay extends VerticalLayout implements  View{
	
	public ErrorDisplay(String errorDisplay) {
		VerticalLayout layout = new VerticalLayout();
		Label  label = new Label(errorDisplay,com.vaadin.shared.ui.label.ContentMode.HTML);
		label.setStyleName("labeltext1");
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		addComponent(layout);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}
	

}
