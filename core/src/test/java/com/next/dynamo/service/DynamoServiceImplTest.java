package com.next.dynamo.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.repository.DomainRepository;

public class DynamoServiceImplTest {

	@InjectMocks
	private DynamoServiceImpl dynamoService;
	
	@Mock
	private DomainRepository domainRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createDomainAndRetrieveItById() throws DynamoException{
		Domain domain = new Domain();
		dynamoService.saveDomain(domain);
		Mockito.verify(domainRepository).save(domain);

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
