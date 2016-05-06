package com.next.dynamo.service;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;

public class ITDynamoServiceImplTest_DomainTemplate extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Before
	public void init() {
	}
	
	@Test
	public void createADomainTemplateWithValidValues() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		DomainTemplate dbDomainTemplate = dynamoService.getDomainTemplateById(domainTemplate.getId());
		
		assertEqualDomainTemplate(domainTemplate, dbDomainTemplate);
	}
	@Test
	public void createADomainTemplateWithValidValuesGetItByGetActiveDomainTemplateOfDomain() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		DomainTemplate dbDomainTemplate = dynamoService.getActiveDomainTemplateOfDomain(domain.getId());
		
		assertEqualDomainTemplate(domainTemplate, dbDomainTemplate);
	}
	
	@Test(expected=DynamoException.class)
	public void createADomainTemplateWithValidValuesWhenOtherActiveDomainTemplateExistsForDomain() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		DomainTemplate domainTemplateOther = createDomainTemplate("My Other Template", "master", "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplateOther);
	}
	@Test
	public void createADomainTemplateWithValidValuesWhenOtherInActiveDomainTemplateExistsForDomain() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", false, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		DomainTemplate domainTemplateOther = createDomainTemplate("My Other Template", "master", "git Repository", true, domain);
		domainTemplateOther = dynamoService.saveDomainTemplate(domainTemplateOther);
		
		DomainTemplate dbDomainTemplate = dynamoService.getDomainTemplateById(domainTemplate.getId());
		assertEqualDomainTemplate(domainTemplate, dbDomainTemplate);
		
		DomainTemplate dbDomainTemplateOther = dynamoService.getDomainTemplateById(domainTemplateOther.getId());
		assertEqualDomainTemplate(domainTemplateOther, dbDomainTemplateOther);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWhenNameIsNull() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate(null, "master", "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWhenNameAsEmptyString() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("", "master", "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWithGitBranchAsNull() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", null, "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWithGitBranchAsEmptyString() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "", "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWithGitRepositoryAsNull() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", null, true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createADomainTemplateWithGitRepositoryAsEmptyString() throws DynamoException{
		Domain domain = createDomain("www.test.com", true, "{Some Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
		
	}
	@Test(expected=DynamoException.class)
	public void createADomainTemplateWhenDomainIsNull() throws DynamoException{
		Domain domain = null;
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "git Repository", true, domain);
		dynamoService.saveDomainTemplate(domainTemplate);
		
	}

}
