package com.next.dynamo.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import com.next.dynamo.persistance.UrlMapping;

public class ITDynamoServiceImplTest_PageTemplate extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Test
	public void testCreateAPageTemplateWithvalidData() throws DynamoException{
		DomainTemplate domainTemplate = createValidDomainTemplateInDatabase(dynamoService);
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService);
		PageTemplate pageTemplate = createPageTemplate("Some git Paht", "Html Content", domainTemplate, urlMapping);
		
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		assertEqualPageTemplate(pageTemplate, dbPageTemplate);
	}
		
	
	@Test
	public void testCreateAPageTemplateWithvalidData_PathStartsWithSlash() throws DynamoException{
		DomainTemplate domainTemplate = createValidDomainTemplateInDatabase(dynamoService);
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService);
		PageTemplate pageTemplate = createPageTemplate("/home/page.html", "Html Content", domainTemplate, urlMapping);
		
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		assertEqualPageTemplate(pageTemplate, dbPageTemplate);
	}
	

}
