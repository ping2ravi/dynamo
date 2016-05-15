package com.next.dynamo.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.ui.domain.DomainPage;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path="/index.html")
@Theme("valo")
public class VaadinUI extends UI {

	private static final long serialVersionUID = 1L;
	@Autowired
	private DomainPage domainPage;
	@Autowired
	private AdminMenu adminMenu;
	
	private VerticalLayout pageContainer;

	public VaadinUI() {
		pageContainer = new VerticalLayout();
	}
	MenuBar.Command menuBarCommand = new MenuBar.Command() {
		private static final long serialVersionUID = 1L;

		public void menuSelected(MenuItem selectedItem) {
	    	Notification.show("Menu Selection",
	    			selectedItem.getText() +  " Selected",
	                  Notification.Type.HUMANIZED_MESSAGE);
	    	if("Domain".equalsIgnoreCase(selectedItem.getText())){
	    		pageContainer.removeAllComponents();
	    		domainPage.init();
	    		pageContainer.addComponent(domainPage);
	    	}
	    }  
	};
	@Override
	protected void init(VaadinRequest request) {
		adminMenu.init(menuBarCommand);
		VerticalLayout mainLayout = new VerticalLayout(adminMenu, pageContainer);
		setContent(mainLayout);
		setResponsive(true);
		mainLayout.setResponsive(true);
		adminMenu.setResponsive(true);
		pageContainer.setResponsive(true);

	}
}
