package com.next.dynamo.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
	public Page<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		PageRequest page = new PageRequest(pageNumber, pageSize, sort);
		Page<Domain> domains = domainRepository.findAll(page);
		return domains;
	}

	@Override
	public Domain saveDomain(@Valid Domain domain) throws DynamoException {
		domain = domainRepository.save(domain);
		return domain;
	}

	@Override
	public Domain getDomainById(Long domainId) throws DynamoException {
		return domainRepository.findOne(domainId);
	}

}
