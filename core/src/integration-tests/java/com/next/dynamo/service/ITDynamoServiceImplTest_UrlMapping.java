package com.next.dynamo.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.DataPlugin;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;

@Transactional
public class ITDynamoServiceImplTest_UrlMapping extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	
	@Test
	public void testCreateAUrlMappingWithValidDataFwdUrlIsNull() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping("/home", true, 60, null, true, domain, "index.html");
		urlMapping = dynamoService.saveUrlMapping(urlMapping);
		
		UrlMapping dbUrlMapping = dynamoService.getUrlMappingById(urlMapping.getId());
		assertEqualUrlMapping(urlMapping, dbUrlMapping);
	}
	@Test
	public void testCreateAUrlMappingWithValidDataFwdUrlIsNotNull() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping("/home", true, 60, "/fwdurl", true, domain, "index.html");
		urlMapping = dynamoService.saveUrlMapping(urlMapping);
		
		UrlMapping dbUrlMapping = dynamoService.getUrlMappingById(urlMapping.getId());
		assertEqualUrlMapping(urlMapping, dbUrlMapping);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateAUrlMappingWhenDomainIsNull() throws DynamoException{
		Domain domain = null;
		
		UrlMapping urlMapping = createUrlMapping("/home", true, 60, null, true, domain, "index.html");
		dynamoService.saveUrlMapping(urlMapping);
	}
	@Test(expected=ConstraintViolationException.class)
	public void testCreateAUrlMappingWhenUrlPatternIsNull() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping(null, true, 60, null, true, domain, "index.html");
		dynamoService.saveUrlMapping(urlMapping);
	}
	@Test(expected=ConstraintViolationException.class)
	public void testCreateAUrlMappingWhenUrlPatternIsEmptyString() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping("", true, 60, null, true, domain, "index.html");
		dynamoService.saveUrlMapping(urlMapping);
	}
	@Test
	public void testCreateAUrlMappingAndAddUrlMappingPluginToIt() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping("/home", true, 60, null, true, domain, "index.html");
		urlMapping = dynamoService.saveUrlMapping(urlMapping);
		
		DataPlugin dataPlugin = createCustomDataPlugin("com.next.TesPlugin", false, "TestPlugin");
		final String setting = "Some setting";
		UrlMappingPlugin urlMappingPlugin = new UrlMappingPlugin();
		urlMappingPlugin.setUrlMapping(urlMapping);
		urlMappingPlugin.setDataPlugin(dataPlugin);
		urlMappingPlugin.setSetting(setting);
		urlMappingPlugin = dynamoService.saveUrlMappingPlugin(urlMappingPlugin);
		
		UrlMapping dbUrlMapping = dynamoService.getUrlMappingById(urlMapping.getId());
		assertEqualUrlMapping(urlMapping, dbUrlMapping);
		List<UrlMappingPlugin> dbUrlMappingPlugins = dynamoService.findUrlMappingPluginByUrlMapping(dbUrlMapping.getId());;
		Assert.assertEquals(1, dbUrlMappingPlugins.size());
	}
	@Test
	public void testCreateAUrlMappingAndGetItByDomainId() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping = createUrlMapping("/home", true, 60, null, true, domain, "index.html");
		urlMapping = dynamoService.saveUrlMapping(urlMapping);
		
		List<UrlMapping> dbUrlMappings = dynamoService.getUrlMappingByDomainId(domain.getId());
		assertEqualUrlMapping(urlMapping, dbUrlMappings.get(0));
	}
	@Test
	public void testCreateTwoUrlMappingAndGetItByDomainId() throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		
		UrlMapping urlMapping1 = createUrlMapping("/home", true, 60, null, true, domain, "index.html");
		urlMapping1 = dynamoService.saveUrlMapping(urlMapping1);
		UrlMapping urlMapping2 = createUrlMapping("/zhome", true, 60, null, true, domain, "zindex.html");
		urlMapping2 = dynamoService.saveUrlMapping(urlMapping2);
		
		List<UrlMapping> dbUrlMappings = dynamoService.getUrlMappingByDomainId(domain.getId());
		assertEqualUrlMapping(urlMapping1, dbUrlMappings.get(0));
		assertEqualUrlMapping(urlMapping2, dbUrlMappings.get(1));
	}
	

}
