package com.next.dynamo.service;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

public class ITThymeleafDynamoTemplateManager extends BaseServiceItest {

    @Autowired
    private ThymeleafDynamoTemplateManager thymeleafDynamoTemplateManager;

    @Test
    public void test01() {
        Map contextParams = new HashMap<>();
        contextParams.put("name", "Ravi Sharma");

        final String expectedRsult = "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<p>Ravi Sharma</p></html>";
        String result = thymeleafDynamoTemplateManager.processTemplate("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                "      xmlns:th=\"http://www.thymeleaf.org\">" +
                "<p th:text=\"${name}\">Abcd</p></html>", contextParams);

        Assert.assertThat(result, is(expectedRsult));
    }
}
