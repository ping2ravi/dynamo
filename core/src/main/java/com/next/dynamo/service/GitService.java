package com.next.dynamo.service;

import com.next.dynamo.exception.DynamoException;

public interface GitService {

	void refreshDomainFromGit(Long domainId) throws DynamoException;
	
	void refreshDomainFromGit(Long domainId, Long domainTemplateId) throws DynamoException;

    void refreshFileList(Long domainTemplateId) throws DynamoException;

}
