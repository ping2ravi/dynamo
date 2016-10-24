package com.next.dynamo.service;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ITGitServiceImplTest extends BaseServiceItest{

	@Autowired
	private GitService gitService;
	@Autowired
	private DynamoService dynamoService;
	
	@Test
	public void test_refreshDomainFromGit_ByDomainId_ActiveDomainTemplateExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://github.com/ping2ravi/dynamo-ui-test.git", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		PageTemplate pageTemplate = createPageTemplate("/first/design1.html", "", domainTemplate, null);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		gitService.refreshDomainFromGit(domain.getId());
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		Assert.assertNotEquals("", dbPageTemplate.getHtmlContent());
	}
	@Test
	public void test_refreshDomainFromGit_ByDomainId_ActiveDomainTemplateExists_pathDoesntStartWithSlash() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://github.com/ping2ravi/dynamo-ui-test.git", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		PageTemplate pageTemplate = createPageTemplate("first/design1.html", "", domainTemplate, null);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		gitService.refreshDomainFromGit(domain.getId());
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		Assert.assertNotEquals("", dbPageTemplate.getHtmlContent());
	}


	@Test(expected=DynamoException.class)
	public void test_refreshDomainFromGit_ByDomainId_ActiveDomainTemplateDoNotExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		gitService.refreshDomainFromGit(domain.getId());
	}

	@Test
	public void test_refreshDomainFromGit_ByDomainIdAndTemplateId_ActiveDomainTemplateExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://github.com/ping2ravi/dynamo-ui-test.git", true, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		PageTemplate pageTemplate = createPageTemplate("/first/design1.html", "", domainTemplate, null);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		gitService.refreshDomainFromGit(domain.getId(), domainTemplate.getId());
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		Assert.assertNotEquals("", dbPageTemplate.getHtmlContent());
	}
	
	@Test
	public void test_refreshDomainFromGit_ByDomainIdAndTemplateId_ActiveDomainTemplateDoNotExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		//Create as Non Active Domain Template
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://github.com/ping2ravi/dynamo-ui-test.git", false, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		PageTemplate pageTemplate = createPageTemplate("/first/design1.html", "", domainTemplate, null);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		gitService.refreshDomainFromGit(domain.getId(), domainTemplate.getId());
		
		PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
		Assert.assertNotEquals("", dbPageTemplate.getHtmlContent());
	}
	@Test(expected=DynamoException.class)
	public void test_refreshDomainFromGit_ByDomainIdAndTemplateId_DomainTemplateDoNotExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		Long nonExistantDomainTemplateId = 100L;
		
		gitService.refreshDomainFromGit(domain.getId(), nonExistantDomainTemplateId);
		
	}
	
	@Test(expected=DynamoException.class)
	public void test_refreshDomainFromGit_WrongGitRepository() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		
		Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
		domain = dynamoService.saveDomain(domain);
		//Create as Non Active Domain Template
		DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://githusfsdfsb.comsdfsamo-ui-test.git", false, domain);
		domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);
		
		PageTemplate pageTemplate = createPageTemplate("/first/design1.html", "", domainTemplate, null);
		pageTemplate = dynamoService.savePageTemplate(pageTemplate);
		
		gitService.refreshDomainFromGit(domain.getId(), domainTemplate.getId());
	}

    @Test
    public void test_refreshFileList_ByDomainIdAndTemplateId_ActiveDomainTemplateExists() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException {

        Domain domain = createDomain("www.test.com", true, "{Some:Settings}", null);
        domain = dynamoService.saveDomain(domain);

        DomainTemplate domainTemplate = createDomainTemplate("My Template", "master", "https://github.com/ModernAristotle/si-ui.git", true, domain);
        domainTemplate = dynamoService.saveDomainTemplate(domainTemplate);

        //PageTemplate pageTemplate = createPageTemplate("/first/design1.html", "", domainTemplate, null);
        //pageTemplate = dynamoService.savePageTemplate(pageTemplate);

        gitService.refreshFileList(domainTemplate.getId());

        //PageTemplate dbPageTemplate = dynamoService.getPageTemplateById(pageTemplate.getId());
        //Assert.assertNotEquals("", dbPageTemplate.getHtmlContent());
    }

}
