package com.next.dynamo.service.thymeleaf;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

import java.nio.charset.StandardCharsets;

public class ThymeleafTemplateResolver implements ITemplateResolver {
    private String name;
    private Integer order;

    public ThymeleafTemplateResolver(String name, Integer order) {
        this.name = name;
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public TemplateResolution resolveTemplate(TemplateProcessingParameters templateProcessingParameters) {
        IContext context = templateProcessingParameters.getContext();
        InMemoryResourceResolver inMemoryResourceResolver = new InMemoryResourceResolver(templateProcessingParameters.getTemplateName());
        TemplateResolution templateResolution = new TemplateResolution(templateProcessingParameters.getTemplateName(), templateProcessingParameters.getTemplateName(),
                inMemoryResourceResolver, StandardCharsets.UTF_8.displayName(), "HTML5", new ThymeleafTemplateResolutionValidity());
        return templateResolution;
    }

    @Override
    public void initialize() {

    }
}