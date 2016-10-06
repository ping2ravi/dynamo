package com.next.dynamo.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.next.dynamo.context.PersistanceServiceContext;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.DataPlugin;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import com.next.dynamo.persistance.PartTemplate;
import com.next.dynamo.persistance.StaticDataPlugin;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes={PersistanceServiceContext.class})
//@EnableJpaRepositories(basePackages = "com.next.dynamo")
@Transactional
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class BaseServiceItest {

	protected Domain createDomain(String name, boolean active, String setting, Domain extendDomain, String... aliases) {
		Domain domain = new Domain();
		domain.setName(name);
		domain.setActive(active);
		Set<String> aliasSet = Sets.newHashSet(aliases);
		domain.setAliases(aliasSet);
		domain.setSetting(setting);
		domain.setExtendedDomain(extendDomain);
		return domain;
	}

	protected void assertEqualDomain(Domain exepected, Domain actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getAliases(), actual.getAliases());
		assertEqualDomain(exepected.getExtendedDomain(), actual.getExtendedDomain());
		assertEquals(exepected.getName(), actual.getName());
		assertEquals(exepected.getSetting(), actual.getSetting());
		assertEquals(exepected.isActive(), actual.isActive());
	}

	protected Domain createValidDomainInDatabase(DynamoService dynamoService) throws DynamoException {
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		return domain;
	}

	protected DomainTemplate createValidDomainTemplateInDatabase(DynamoService dynamoService) throws DynamoException{
		Domain domain = createValidDomainInDatabase(dynamoService);
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		return domainTemplate;
	}

	protected void assertEqualDomainTemplate(DomainTemplate exepected, DomainTemplate actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getGitBranch(), actual.getGitBranch());
		assertEqualDomain(exepected.getDomain(), actual.getDomain());
		assertEquals(exepected.getName(), actual.getName());
		assertEquals(exepected.getGitRepository(), actual.getGitRepository());
		assertEquals(exepected.isActive(), actual.isActive());
	}

	protected DomainTemplate createDomainTemplate(String name, String gitBranch, String gitRepository, boolean active,
			Domain domain) {
		DomainTemplate domainTemplate = new DomainTemplate();
		domainTemplate.setActive(active);
		domainTemplate.setDomain(domain);
		domainTemplate.setGitBranch(gitBranch);
		domainTemplate.setGitRepository(gitRepository);
		domainTemplate.setName(name);
		return domainTemplate;
	}

	protected UrlMapping createUrlMapping(String urlPattern, boolean secured, Integer httpCacheTimeSeconds,
			String forwardUrl, boolean active, Domain domain, String... aliases) {
		UrlMapping urlMapping = new UrlMapping();
		urlMapping.setActive(active);
		urlMapping.setAliases(Sets.newHashSet(aliases));
		urlMapping.setForwardUrl(forwardUrl);
		urlMapping.setHttpCacheTimeSeconds(httpCacheTimeSeconds);
		urlMapping.setSecured(secured);
		urlMapping.setUrlPattern(urlPattern);
		urlMapping.setDomain(domain);
		return urlMapping;
	}

	protected void assertEqualUrlMapping(UrlMapping exepected, UrlMapping actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getAliases(), actual.getAliases());
		assertEqualDomain(exepected.getDomain(), actual.getDomain());
		assertEquals(exepected.getForwardUrl(), actual.getForwardUrl());
		assertEquals(exepected.getHttpCacheTimeSeconds(), actual.getHttpCacheTimeSeconds());
		assertEquals(exepected.isActive(), actual.isActive());
		assertEquals(exepected.isSecured(), actual.isSecured());
	}

	protected UrlMapping createValidUrlMappingInDatabase(DynamoService dynamoService) throws DynamoException {
		return createValidUrlMappingInDatabase(dynamoService, "/home");
	}
	protected UrlMapping createValidUrlMappingInDatabase(DynamoService dynamoService, String url, String...aliases) throws DynamoException {
		Domain domain = createValidDomainInDatabase(dynamoService);

		UrlMapping urlMapping = createUrlMapping(url, true, 60, null, true, domain, aliases);
		urlMapping = dynamoService.saveUrlMapping(urlMapping);
		return urlMapping;
	}
	

	protected UrlMappingPlugin createUrlMappingPlugin(DataPlugin dataPlugin, String setting, UrlMapping urlMapping) {
		UrlMappingPlugin urlMappingPlugin = new UrlMappingPlugin();
		urlMappingPlugin.setDataPlugin(dataPlugin);
		urlMappingPlugin.setSetting(setting);
		urlMappingPlugin.setUrlMapping(urlMapping);
		return urlMappingPlugin;
	}

	protected void assertEqualUrlMappingPlugin(UrlMappingPlugin exepected, UrlMappingPlugin actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getSetting(), actual.getSetting());
	}

	protected CustomDataPlugin createCustomDataPlugin(String fullClassName, boolean disabled, String pluginName) {
		CustomDataPlugin customDataPlugin = new CustomDataPlugin();
		customDataPlugin.setDisabled(disabled);
		customDataPlugin.setFullClassName(fullClassName);
		customDataPlugin.setPluginName(pluginName);
		return customDataPlugin;
	}

	protected void assertEqualCustomDataPlugin(CustomDataPlugin exepected, CustomDataPlugin actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.isDisabled(), actual.isDisabled());
		assertEquals(exepected.getFullClassName(), actual.getFullClassName());
		assertEquals(exepected.getPluginName(), actual.getPluginName());
	}

	protected StaticDataPlugin createStaticDataPlugin(String content, boolean disabled, String pluginName) {
		StaticDataPlugin staticDataPlugin = new StaticDataPlugin();
		staticDataPlugin.setDisabled(disabled);
		staticDataPlugin.setContent(content);
		staticDataPlugin.setPluginName(pluginName);
		return staticDataPlugin;
	}

	protected void assertEqualStaticDataPlugin(StaticDataPlugin exepected, StaticDataPlugin actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.isDisabled(), actual.isDisabled());
		assertEquals(exepected.getContent(), actual.getContent());
		assertEquals(exepected.getPluginName(), actual.getPluginName());
	}
	
	protected PageTemplate createPageTemplate(String gitPath, String htmlContent, DomainTemplate domainTemplate, UrlMapping urlMapping){
		PageTemplate pageTemplate = new PageTemplate();
		pageTemplate.setDomainTemplate(domainTemplate);
		pageTemplate.setGitFilePath(gitPath);
		pageTemplate.setHtmlContent(htmlContent);
		pageTemplate.setUrlMapping(urlMapping);
		return pageTemplate;
	}
	
	protected PageTemplate createPageTemplateFromFile(String gitPath, String filePath, DomainTemplate domainTemplate, UrlMapping urlMapping, PartTemplate mainPartTemplate) throws IOException{
		
		String htmlContent = readFile(filePath);
		PageTemplate pageTemplate = new PageTemplate();
		pageTemplate.setDomainTemplate(domainTemplate);
		pageTemplate.setGitFilePath(gitPath);
		pageTemplate.setHtmlContent(htmlContent);
		pageTemplate.setUrlMapping(urlMapping);
		pageTemplate.setMainTemplate(mainPartTemplate);
		return pageTemplate;
	}
	protected PartTemplate createPartTemplateFromFile(String gitPath, String filePath, String partName, DomainTemplate domainTemplate) throws IOException{
		
		String htmlContent = readFile(filePath);
		PartTemplate partTemplate = new PartTemplate();
		partTemplate.setDomainTemplate(domainTemplate);
		partTemplate.setGitFilePath(gitPath);
		partTemplate.setHtmlContent(htmlContent);
		partTemplate.setPartName(partName);
		return partTemplate;
	}
	
	private String readFile(String filePath) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filePath).getFile());
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
	
	protected void assertEqualPageTemplate(PageTemplate exepected, PageTemplate actual) {
		if (exepected == null && actual == null) {
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getGitFilePath(), actual.getGitFilePath());
		assertEquals(exepected.getHtmlContent(), actual.getHtmlContent());
		assertEqualUrlMapping(exepected.getUrlMapping(), actual.getUrlMapping());
		assertEqualDomainTemplate(exepected.getDomainTemplate(), actual.getDomainTemplate());
	}

}
