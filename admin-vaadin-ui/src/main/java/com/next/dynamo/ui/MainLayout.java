package com.next.dynamo.ui;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.ui.domain.DomainPage;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.GenericFontIcon;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

public class MainLayout extends MainLayoutDesign implements ViewDisplay {

    private static final String STYLE_SELECTED = "selected";
    private Navigator navigator;
    
    @Autowired
    private DomainPage domainPage;

    public MainLayout(SpringViewProvider viewProvider) {
        this.navigator = new Navigator(UI.getCurrent(), (ViewDisplay) this);
        navigator.addProvider(viewProvider);
        domainPage = (DomainPage)viewProvider.getView("domain");
        NativeButton nativeButton = new NativeButton();
        nativeButton.setIcon(new GenericFontIcon(FontAwesome.GOOGLE_PLUS.getFontFamily(), FontAwesome.GOOGLE_PLUS.getCodepoint()));
        nativeButton.setStyleName("menu-button");
        nativeButton.setCaption("Manual");
        nativeButton.setWidth("100%");
        side_bar.addComponent(nativeButton);
        addNavigatorView(domainPage.getNaviagationName(), menuButton1);
        
        //addNavigatorView(OrderView.VIEW_NAME, OrderView.class, menuButton2);
        //addNavigatorView(AboutView.VIEW_NAME, AboutView.class, menuButton3);
        //addNavigatorView("Test", OrderView.class, menuButton4);
        
        if (navigator.getState().isEmpty()) {
            navigator.navigateTo("domain");
        } else {
            navigator.navigateTo(navigator.getState());
        }
    }

    private void doNavigate(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void addNavigatorView(String viewName, Button menuButton) {
        menuButton.addClickListener(event -> doNavigate(viewName));
        //menuButton.setData(viewClass.getName());
    }

    private void adjustStyleByData(Component component, Object data) {
        if (component instanceof Button) {
            if (data != null && data.equals(((Button) component).getData())) {
                component.addStyleName(STYLE_SELECTED);
            } else {
                component.removeStyleName(STYLE_SELECTED);
            }
        }
    }

    @Override
    public void showView(View view) {
        if (view instanceof Component) {
            scroll_panel.setContent((Component) view);
            Iterator<Component> it = side_bar.iterator();
            while (it.hasNext()) {
                adjustStyleByData(it.next(), view.getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("View is not a Component");
        }
    }
}
