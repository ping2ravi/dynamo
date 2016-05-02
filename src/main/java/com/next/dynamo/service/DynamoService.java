package com.next.dynamo.service;

import org.springframework.data.domain.Page;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;

public interface DynamoService {

	Page<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException;
	
	Domain saveDomain(Domain domain) throws DynamoException;
	
	Domain getDomainById(Long domainId) throws DynamoException;
}
