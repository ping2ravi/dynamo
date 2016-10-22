package com.next.dynamo.ui;

import java.awt.ScrollPane;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.ui.domain.DomainPage;
import com.next.dynamo.ui.test.DashboardView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

//@SpringUI(path="/")
@Theme("valo")
public class VaadinUI extends UI implements ViewDisplay{

	@WebServlet(value = "/index.html", asyncSupported = true)
	@VaadinServletConfiguration(widgetset="com.next.dynamo.ui.DynamoWidgetSet", productionMode=true, heartbeatInterval=120, closeIdleSessions=true, ui = VaadinUI.class)
    public static class Servlet extends SpringVaadinServlet {
    }
	
	private static final long serialVersionUID = 1L;
	//@Autowired
	//private DomainPage domainPage;
	@Autowired
	private AdminMenu adminMenu;
	
	private Panel pageContainer;
	
	private Navigator navigator;
	
	@Autowired
    private SpringViewProvider viewProvider;

	public VaadinUI() {
		pageContainer = new Panel();
	}
	MenuBar.Command menuBarCommand = new MenuBar.Command() {
		private static final long serialVersionUID = 1L;

		public void menuSelected(MenuItem selectedItem) {
	    	Notification.show("Menu Selection",
	    			selectedItem.getText() +  " Selected",
	                  Notification.Type.HUMANIZED_MESSAGE);
	    	if("Domain".equalsIgnoreCase(selectedItem.getText())){
	    		navigator.navigateTo("domain");
	    		//pageContainer.removeAllComponents();
	    		//domainPage.init();
	    		//pageContainer.addComponent(domainPage);
	    	}
	    	if("Domain Template".equalsIgnoreCase(selectedItem.getText())){
	    		navigator.navigateTo("domainPage");
	    		//pageContainer.removeAllComponents();
	    		//domainPage.init();
	    		//pageContainer.addComponent(domainPage);
	    	}
	    }  
	};
	@Override
	protected void init(VaadinRequest request) {
		navigator = new Navigator(UI.getCurrent(), (ViewDisplay)this);
        navigator.addProvider(viewProvider);
		adminMenu.init(menuBarCommand);
		VerticalLayout mainLayout = new VerticalLayout(adminMenu, pageContainer);
		setContent(mainLayout);
		if (navigator.getState().isEmpty()) {
            navigator.navigateTo(DashboardView.VIEW_NAME);
        } else {
            navigator.navigateTo(navigator.getState());
        }
	}
	@Override
	public void showView(View view) {
		if (view instanceof Component) {
			pageContainer.setContent((Component) view);
        } else {
            throw new IllegalArgumentException("View is not a Component");
        }
		
	}
	
}
