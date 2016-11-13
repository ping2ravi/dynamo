package com.next.dynamo.service;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.repository.DomainRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

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
        domain.setName("Some Domain Name");
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
