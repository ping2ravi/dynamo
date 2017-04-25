package com.next.dynamo.ui;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SpringUI(path="/")
@Theme("mytheme")
public class VaadinNaviagationUI extends UI {

	private static final long serialVersionUID = 1L;
	@Autowired
	private SpringViewProvider viewProvider;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new MainLayout(viewProvider));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VaadinNaviagationUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
	
}
