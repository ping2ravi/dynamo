package com.next.dynamo.service.ui;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import com.github.jknack.handlebars.Template;
import com.google.gson.JsonObject;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import com.next.dynamo.persistance.PartTemplate;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.service.BaseServiceItest;
import com.next.dynamo.service.DynamoService;
import com.next.dynamo.service.plugin.HttpParameters;

public class ITUiTemplateManagerHandleBarImpl extends BaseServiceItest{

	@Autowired
	private UiTemplateManagerHandleBarImpl uiTemplateManagerHandleBarImpl;
	
	@Autowired
	private DynamoService dynamoService;
	
	@Before
	public void init(){
		ReflectionTestUtils.setField(uiTemplateManagerHandleBarImpl, "isInitialised", false);
	}
	
	/**
	 * When domain doesnt exists in database
	 */
	@Test
	public void test_getDomainSettings01(){
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setServerName("www.myserver.com");
		
		JsonObject settingJsonObject = uiTemplateManagerHandleBarImpl.getDomainSettings(httpServletRequest);
		
		Assert.assertNull(settingJsonObject);
	}

	/**
	 * When domain exists in database but settings is null
	 * @throws DynamoException 
	 */
	@Test
	public void test_getDomainSettings02() throws DynamoException{
		final String serverName = "www.myserver.com";
		final String setting = null;

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setServerName(serverName);
		
		JsonObject settingJsonObject = uiTemplateManagerHandleBarImpl.getDomainSettings(httpServletRequest);
		
		Assert.assertNotNull(settingJsonObject);
	}
	
	/**
	 * When domain exists in database but settings is empty
	 * @throws DynamoException 
	 */
	@Test
	public void test_getDomainSettings03() throws DynamoException{
		final String serverName = "www.myserver.com";
		final String setting = "";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setServerName(serverName);
		
		JsonObject settingJsonObject = uiTemplateManagerHandleBarImpl.getDomainSettings(httpServletRequest);
		
		Assert.assertNotNull(settingJsonObject);
	}
	
	/**
	 * When domain exists in database and settings exists too
	 * @throws DynamoException 
	 */
	@Test
	public void test_getDomainSettings04() throws DynamoException{
		final String serverName = "www.myserver.com";
		final String setting = "{'key':'value'}";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setServerName(serverName);
		
		JsonObject settingJsonObject = uiTemplateManagerHandleBarImpl.getDomainSettings(httpServletRequest);
		
		Assert.assertNotNull(settingJsonObject);
		Assert.assertEquals("value", settingJsonObject.get("key").getAsString());
	}
	
	/**
	 * When domain do not exists
	 * @throws DynamoException 
	 * @throws IOException 
	 */
	@Test
	public void test_getCompiledTemplate01() throws DynamoException, IOException{
		final String serverName = "www.myserver.com";

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		httpServletRequest.setServerName(serverName);
		
		Template compiledTemplate = uiTemplateManagerHandleBarImpl.getCompiledTemplate(httpServletRequest, httpServletResponse);
		
		Assert.assertNotNull(compiledTemplate);
		Assert.assertEquals("No Template Defined", compiledTemplate.apply(""));
	}
	
	/**
	 * When domain exists but no domain template exists
	 * @throws DynamoException 
	 * @throws IOException 
	 */
	@Test
	public void test_getCompiledTemplate02() throws DynamoException, IOException{
		final String serverName = "www.myserver.com";
		final String setting = "{'key':'value'}";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		httpServletRequest.setServerName(serverName);
		
		Template compiledTemplate = uiTemplateManagerHandleBarImpl.getCompiledTemplate(httpServletRequest, httpServletResponse);
		
		Assert.assertNotNull(compiledTemplate);
		Assert.assertEquals("No Template Defined", compiledTemplate.apply(""));
	}
	
	/**
	 * When domain exists and domain template exists but not active
	 * @throws DynamoException 
	 */
	@Test
	public void test_getCompiledTemplate03() throws DynamoException, IOException{
		final String serverName = "www.myserver.com";
		final String setting = "{'key':'value'}";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", false, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		httpServletRequest.setServerName(serverName);
		
		Template compiledTemplate = uiTemplateManagerHandleBarImpl.getCompiledTemplate(httpServletRequest, httpServletResponse);
		
		Assert.assertNotNull(compiledTemplate);
		Assert.assertEquals("No Template Defined", compiledTemplate.apply(""));
	}
	
	/**
	 * When domain exists and domain template exists and active but no Page Template
	 * @throws DynamoException 
	 */
	@Test
	public void test_getCompiledTemplate04() throws DynamoException, IOException{
		final String serverName = "www.myserver.com";
		final String setting = "{'key':'value'}";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		httpServletRequest.setServerName(serverName);
		
		Template compiledTemplate = uiTemplateManagerHandleBarImpl.getCompiledTemplate(httpServletRequest, httpServletResponse);
		
		Assert.assertNotNull(compiledTemplate);
		Assert.assertEquals("No Template Defined", compiledTemplate.apply(""));
	}
	
	/**
	 * When domain exists and domain template exists and active and a Page Template
	 * @throws DynamoException 
	 */
	@Test
	public void test_getCompiledTemplate05() throws DynamoException, IOException{
		final String serverName = "www.myserver.com";
		final String setting = "{'key':'value'}";

		Domain domain = createDomain(serverName, true, setting, null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, "/home");

		PartTemplate mainPartTemplate = createPartTemplateFromFile("Some git path", "MainPage.html", "MainPart", domainTemplate);
		mainPartTemplate.setPartType("Main");
		mainPartTemplate = dynamoService.savePartTemplate(mainPartTemplate);
		
		PartTemplate headPartTemplate = createPartTemplateFromFile("Some git path", "Head.html", "HEAD", domainTemplate);
		headPartTemplate.setPartType("NotMain");
		headPartTemplate = dynamoService.savePartTemplate(headPartTemplate);
		
		PageTemplate pageTemplate = createPageTemplateFromFile("Some git path", "Body.html", domainTemplate, urlMapping, mainPartTemplate);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		httpServletRequest.setServerName(serverName);
		httpServletRequest.setRequestURI("/home");
		httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, urlMapping);
		
		Template compiledTemplate = uiTemplateManagerHandleBarImpl.getCompiledTemplate(httpServletRequest, httpServletResponse);
		
		Assert.assertNotNull(compiledTemplate);
		Assert.assertEquals("No Template Defined", compiledTemplate.apply(""));
	}

}
