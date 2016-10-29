package com.next.dynamo.service.thymeleaf;

import org.thymeleaf.templateresolver.ITemplateResolutionValidity;

public class ThymeleafTemplateResolutionValidity implements ITemplateResolutionValidity {

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public boolean isCacheStillValid() {
        return true;
    }
}