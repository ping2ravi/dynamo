package com.next.dynamo.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

@SpringComponent
@UIScope
public class AdminMenu extends HorizontalLayout{

	private static final long serialVersionUID = 1L;
	private MenuBar menuBar;
	
	public void init(MenuBar.Command command){
		menuBar = new MenuBar();
		MenuItem child = menuBar.addItem("Domain", null);
		child.addItem("Domain", command);
		child.addItem("Domain Template", command);
		
		child = menuBar.addItem("Page", null);
		child.addItem("Page Template", command);
		child.addItem("Part Template", command);
		child.addItem("Url Mapping", command);
		
		child = menuBar.addItem("Plugin", null);
		child.addItem("Static Data Plugin", command);
		child.addItem("Custom Data Plugin", command);

		super.addComponent(menuBar);
	}

}
