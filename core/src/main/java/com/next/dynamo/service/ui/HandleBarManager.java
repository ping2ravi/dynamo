package com.next.dynamo.service.ui;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;

@Component
public class HandleBarManager {

    private final Handlebars handlebars;

    public HandleBarManager() {
        handlebars = new Handlebars();
    }

    public Handlebars getHandlebars() {
        return handlebars;
    }

    @PostConstruct
    public void init() {
        // registerTrimStringFunction();
        handlebars.registerHelpers(new HelperSource());
    }
}
