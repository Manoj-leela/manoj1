package com.syml.proposalRefinance.expiryDate;

import com.vaadin.navigator.Navigator;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class MyClickListener implements ClickListener {
	private static final long serialVersionUID = 1L;
	private String page;
	private Navigator navigator;
	
	public MyClickListener(String uri, Navigator nav) {
		this.page = uri;
		this.navigator = nav;
	}

	public MyClickListener(String uri, ExpiryDatePage expiry) {
		this.page = uri;
		//this.navigator = expiry.getNavigator();
		
	}
	public void buttonClick(ClickEvent event) {
		//navigator.navigateTo(page);
		navigator.navigateTo(page);		
	}

}
