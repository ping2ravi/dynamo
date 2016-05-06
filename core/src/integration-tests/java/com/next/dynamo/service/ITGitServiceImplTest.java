package com.next.dynamo.service;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;

public class ITGitServiceImplTest extends BaseServiceItest{

	@Autowired
	private GitService gitService;
	
	@Test
	public void test01() throws DynamoException, IOException, InvalidRemoteException, TransportException, GitAPIException{
		/*
		File localPath = File.createTempFile("/tmp/TestGitRepository", "");
        localPath.delete();
        String REMOTE_URL="https://github.com/ping2ravi/dynamo.git";
        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(localPath)
                .call()) {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
        }
		*/
		gitService.refreshDomainFromGit(1L);
	}

}
