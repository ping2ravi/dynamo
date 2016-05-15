package com.next.dynamo.ui.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import com.next.dynamo.persistance.Domain;
import com.next.dynamo.service.DynamoService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class DomainPage extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final DynamoService dynamoService;

	private final DomainEditor editor;

	private final Grid grid;

	private final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public DomainPage(DynamoService dynamoService, DomainEditor editor) {
		this.dynamoService = dynamoService;
		this.editor = editor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New Domain", FontAwesome.PLUS);
	}

	public void init() {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		actions.setResponsive(true);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		mainLayout.setResponsive(true);
		this.addComponent(mainLayout);
		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "name", "setting");

		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listDomains(e.getText()));

		// Connect selected Domain to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.editDomain((Domain) grid.getSelectedRow());
			}
		});

		addNewBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);

		// Instantiate and edit new Domain the new button is clicked
		addNewBtn.addClickListener(e -> {
			Domain d = new Domain();
			d.setName("www.mydomain.com");
			d.setSetting("Test Setting");
			editor.editDomain(d);
		});

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listDomains(filter.getValue());
		});

		// Initialize listing
		listDomains(null);
	}

	// tag::listDomains[]
	private void listDomains(String text) {
		try {
			if (StringUtils.isEmpty(text)) {
				Page<Domain> domains = dynamoService.getDomains(0, 100);
				grid.setContainerDataSource(new BeanItemContainer<Domain>(Domain.class, domains.getContent()));
			} else {
				// grid.setContainerDataSource(new
				// BeanItemContainer(Domain.class,
				// repo.findByLastNameStartsWithIgnoreCase(text)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
