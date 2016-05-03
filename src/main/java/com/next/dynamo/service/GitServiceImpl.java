package com.next.dynamo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.util.DynamoAssert;

@Service
@Transactional
public class GitServiceImpl implements GitService {

	@Autowired
	private DynamoService dynamoService;

	@Override
	public void refreshDomainFromGit(Long domainId) throws DynamoException {
		try {
			//Domain domain = dynamoService.getDomainById(domainId);
			DomainTemplate currentActiveDomainTemplate = dynamoService.getActiveDomainTemplateOfDomain(domainId);
			//DynamoAssert.notNull(currentActiveDomainTemplate,"No Active Domain template found for Domain[" + domainId + "]");
			Process p = Runtime.getRuntime().exec("git clone https://github.com/ping2ravi/dynamo.git /tmp/.");
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new DynamoException(e);
		}

	}

}
