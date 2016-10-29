package com.next.dynamo.service.thymeleaf;


import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InMemoryResourceResolver implements IResourceResolver {

    private String name;

    public InMemoryResourceResolver(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
        String template = (String) templateProcessingParameters.getContext().getVariables().get(resourceName);
        System.out.println("Getting Resource " + resourceName);
        if (template == null) {
            return null;
        }
        InputStream stream = new ByteArrayInputStream(template.getBytes(StandardCharsets.UTF_8));
        return stream;
    }
}