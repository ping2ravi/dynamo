package com.next.dynamo.ui.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.service.DynamoService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToCollectionConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class DomainEditor extends FormLayout {

	private static final long serialVersionUID = 1L;

	private final DynamoService dynamoService;

	private Domain domain;

	/* Fields to edit properties in Domain entity */
	TextField name = new TextField("Name");
	TextField setting = new TextField("Setting");
	CheckBox active = new CheckBox("Active", true);
	TextField aliases = new TextField("Aliases");
	ComboBox extendedDomain = new ComboBox("Extend Domain");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	@Autowired
	public DomainEditor(DynamoService dynamoService) {
		this.dynamoService = dynamoService;

		StringToCollectionConverter aliasConvertor = new StringToCollectionConverter(","){
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
		    public String convertToPresentation(Collection value,
		            Class<? extends String> targetType, Locale locale)
		            throws Converter.ConversionException {
				String str = super.convertToPresentation(value, targetType, locale);
				if(str == null){
					str = "";
				}
				return str;
			}
		};
		aliases.setConverter(aliasConvertor);
		addComponents(name, setting, active, aliases, extendedDomain, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			try{
				dynamoService.saveDomain(domain);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			});
		//delete.addClickListener(e -> repository.delete(domain));
		cancel.addClickListener(e -> {
			DomainEditor.this.setVisible(false);	
		});
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editDomain(Domain c) {

		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			try {
				domain = dynamoService.getDomainById(c.getId());
			} catch (DynamoException e) {
				e.printStackTrace();
			}
		}
		else {
			domain = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(domain, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
		populateExtendedDomainComboxBox(domain);

	}
	private void populateExtendedDomainComboxBox(Domain domain){
		try{
			Page<Domain> domains = dynamoService.getDomains(0, 1000);
			BeanItemContainer<Domain> container = new BeanItemContainer<Domain>(Domain.class);
			List<Domain> domainList = new ArrayList<>();
			domainList.addAll(domains.getContent());
			domainList.remove(domain);
			container.addAll(domainList);
			extendedDomain.setContainerDataSource(container);
			extendedDomain.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			extendedDomain.setItemCaptionPropertyId("name");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
		cancel.addClickListener(e -> h.onChange());
	}

}
