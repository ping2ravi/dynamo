package com.next.dynamo.ui.test;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AboutView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "about";
    
    public AboutView() {
		this.addComponent(new Label("About view Ravi"));
	}

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
