package com.next.dynamo.service.thymeleaf;

import org.thymeleaf.templateresolver.ITemplateResolutionValidity;

public class ThymeleafTemplateResolutionValidity implements ITemplateResolutionValidity {

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isCacheStillValid() {
        return false;
    }
}