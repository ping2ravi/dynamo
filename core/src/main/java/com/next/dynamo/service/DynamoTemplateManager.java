package com.next.dynamo.service;

import java.util.Map;

public interface DynamoTemplateManager {

    String processTemplate(String templateBody, Map<String, Object> context);
}
