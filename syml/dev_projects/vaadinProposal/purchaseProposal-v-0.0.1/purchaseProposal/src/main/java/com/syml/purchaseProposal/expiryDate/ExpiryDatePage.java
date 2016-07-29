package com.syml.purchaseProposal.expiryDate;

import com.syml.purchaseProposal.vaadin.ImageService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ExpiryDatePage extends CssLayout implements View {

	protected static final View View = null;

	public ExpiryDatePage(String opp_id) {

		settopbarLayout();
		setLabelExpiry();
		setLabelExpiry1();
		setButtonExpiry(opp_id);

	}

	public void settopbarLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();

		horizontalLayout.setSizeFull();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		Label label1 = new Label(
				"<span style='color:#0070C0;font-size: 60px;'>R</span>" + "<font size='7'>efinance</font>" + " "
						+ "<span style='color:#0070C0;font-size: 60px;'>P</span>" + "<font size='7'>roposal</font>",
				ContentMode.HTML);
		// label1.setStyleName("h1");
		label1.setResponsive(true);
		label1.setWidth("500px");
		Image imagesrc = new Image(null, ImageService.getImageFile());
		

		

		imagesrc.setResponsive(true);
		imagesrc.setWidth("300px");

		horizontalLayout.addComponent(imagesrc);
		horizontalLayout.addComponent(label1);

		horizontalLayout.setComponentAlignment(label1, Alignment.MIDDLE_RIGHT);
		addComponent(horizontalLayout);

	}

	public void setLabelExpiry() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		Label labelexpirry = new Label("<font size='6'>Expiry</font>", ContentMode.HTML);

		labelexpirry.setStyleName("labelcolor");

		layout.addComponent(labelexpirry);
		layout.setComponentAlignment(labelexpirry, Alignment.MIDDLE_LEFT);
		addComponent(layout);
	}

	public void setLabelExpiry1() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label labelexpirry = new Label(
				"<Strong>Due to interest rate changes, this proposal has expired and can no longer be accessed. Please contact your Visdom Mortgage Broker for a refreshed version.<Strong>",
				ContentMode.HTML);

		layout.addComponent(labelexpirry);

		addComponent(layout);
	}

	public void setButtonExpiry(String opp_id) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		Button button = new Button("Extend Expiry", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				getUI().getNavigator().navigateTo("extendExpiry");

			}

		});
		button.setStyleName("primary");
		button.setWidth("25%");

		layout.addComponent(button);
		layout.setComponentAlignment(button, Alignment.BOTTOM_RIGHT);
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		com.vaadin.ui.Notification.show("welcoome to ");

	}

	/*
	 * public void enter(ViewChangeEvent event) { System.out.println("hello");
	 * this.button.addClickListener(new MyClickListener("extendExpiry", this));
	 * }
	 */
	/*
	 * public Navigator getNavigator() {
	 * 
	 * //return this.navigator; }
	 */

}
