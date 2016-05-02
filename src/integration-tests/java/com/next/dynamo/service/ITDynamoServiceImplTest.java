package com.next.dynamo.service;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;

import com.google.common.collect.Sets;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;

public class ITDynamoServiceImplTest extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Before
	public void init() {
	}
	
	protected Domain createDomain(String name, boolean active, String setting, Domain extendDomain, String...aliases){
		Domain domain = new Domain();
		domain.setName(name);
		domain.setActive(active);
		Set<String> aliasSet = Sets.newHashSet(aliases);
		domain.setAliases(aliasSet);
		domain.setSetting(setting);
		domain.setExtendedDomain(extendDomain);
		return domain;
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
	protected void assertEqualDomain(Domain exepected, Domain actual){
		if(exepected == null && actual == null){
			return;
		}
		assertEquals(exepected.getId(), actual.getId());
		assertEquals(exepected.getAliases(), actual.getAliases());
		assertEqualDomain(exepected.getExtendedDomain(), actual.getExtendedDomain());
		assertEquals(exepected.getName(), actual.getName());
		assertEquals(exepected.getSetting(), actual.getSetting());
		assertEquals(exepected.isActive(), actual.isActive());
	}

}
