package com.next.dynamo.service;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;

public class ITDynamoServiceImplTest extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Before
	public void init() {
	}
	
	
	@Test
	public void createDomainAndRetrieveItById() throws DynamoException{
		Domain domain = createDomain("www.mydomain.com", true, "Some Setting", null, "www.myalias.com");
		Domain savedDomain = dynamoService.saveDomain(domain);
		Domain fetchedDomain = dynamoService.getDomainById(savedDomain.getId());
		assertEqualDomain(domain, fetchedDomain);
	}
	@Test
	public void createOneDomainAndRetrieveItByGetAllDomainApi() throws DynamoException{
		Domain domain = createDomain("www.mydomain.com", true, "Some Setting", null, "www.myalias.com");
		Domain savedDomain = dynamoService.saveDomain(domain);
		Page<Domain> fetchedDomain = dynamoService.getDomains(0, 10);
		assertEqualDomain(savedDomain, fetchedDomain.getContent().get(0));
	}
	@Test
	public void createOneDomainAndRetrieveItByGetAllDomainApiButWrongPageNumber() throws DynamoException{
		Domain domain = createDomain("www.mydomain.com", true, "Some Setting", null, "www.myalias.com");
		dynamoService.saveDomain(domain);
		Page<Domain> fetchedDomain = dynamoService.getDomains(1, 10);
		assertEquals(0, fetchedDomain.getContent().size());
	}
	
	@Test
	public void createOneDomainWithExtendedDomainAndRetrieveItById() throws DynamoException{
		Domain extendedDomain = createDomain("www.emydomain.com", true, "Some Extended Setting", null, "www.emyalias.com");
		extendedDomain = dynamoService.saveDomain(extendedDomain);
		Domain domain = createDomain("www.mydomain.com", true, "Some Setting", extendedDomain, "www.myalias.com");
		dynamoService.saveDomain(domain);
		Page<Domain> fetchedDomain = dynamoService.getDomains(1, 10);
		assertEquals(0, fetchedDomain.getContent().size());
	}
	@Test(expected=ConstraintViolationException.class)
	public void createDomainWhenNameIsNull() throws DynamoException{
		Domain domain = createDomain(null, true, "Some Setting", null, "www.myalias.com");
		dynamoService.saveDomain(domain);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createDomainWhenNameIsEmpty() throws DynamoException{
		Domain domain = createDomain("", true, "Some Setting", null, "www.myalias.com");
		dynamoService.saveDomain(domain);
	}
	@Test(expected=ConstraintViolationException.class)
	public void createDomainWhenNameIsStringWithSpacesOnly() throws DynamoException{
		Domain domain = createDomain("   ", true, "Some Setting", null, "www.myalias.com");
		dynamoService.saveDomain(domain);
	}
	

}
