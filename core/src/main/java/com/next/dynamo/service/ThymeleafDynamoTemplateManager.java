package com.next.dynamo.service;


import com.next.dynamo.service.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class ThymeleafDynamoTemplateManager implements DynamoTemplateManager {

    private TemplateEngine templateEngine;
    private static final String TEMPLATE_NAME = "TEMPLATE_NAME";

    public ThymeleafDynamoTemplateManager() {
        templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(new ThymeleafTemplateResolver("thymeleafTemplateResolver", 1));
    }

    @Override
    public String processTemplate(String templateBody, Map<String, Object> contextParameters) {
        Context context = new Context();
        context.setVariable(TEMPLATE_NAME, templateBody);
        context.setVariables(contextParameters);
        String response = templateEngine.process(TEMPLATE_NAME, context);
        System.out.println("response : " + response);
        return response;
    }

}
