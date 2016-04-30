package com.next.dynamo.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;

public class ITDynamoServiceImplTest extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Before
	public void init() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Test
	public void createDomainAndRetrieveItById() throws DynamoException{
		Domain domain = new Domain();
		domain.setName("www.mydomain.com");
		Domain savedDomain = dynamoService.saveDomain(domain);
		Domain fetchedDomain = dynamoService.getDomainById(savedDomain.getId());
		assertEqualDomain(domain, fetchedDomain);
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
	}

}
