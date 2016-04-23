package com.next.dynamo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.repository.DomainRepository;

@Service
@Transactional
public class DynamoServiceImpl implements DynamoService{

	@Autowired
	private DomainRepository domainRepository;
	
	@Override
	public List<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Domain saveDomain(Domain domain) throws DynamoException {
		domain = domainRepository.save(domain);
		return domain;
	}

	@Override
	public Domain getDomainById(Long domainId) throws DynamoException {
		return domainRepository.findOne(domainId);
	}

}
