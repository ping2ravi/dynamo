package com.next.dynamo.service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.*;
import com.next.dynamo.util.DynamoAssert;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class GitServiceImpl implements GitService {

	@Autowired
	private DynamoService dynamoService;
	@Autowired
	private DynamoAssert dynamoAssert;

	@Override
	public void refreshDomainFromGit(Long domainId) throws DynamoException {
		Domain domain = dynamoService.getDomainById(domainId);
		DomainTemplate currentActiveDomainTemplate = dynamoService.getActiveDomainTemplateOfDomain(domainId);

		refreshDomainFromGit(domain, currentActiveDomainTemplate);
	}

	@Override
	public void refreshDomainFromGit(Long domainId, Long domainTemplateId) throws DynamoException {
		Domain domain = dynamoService.getDomainById(domainId);
		DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
		refreshDomainFromGit(domain, domainTemplate);

	}

    @Override
    public void refreshFileList(Long domainTemplateId) throws DynamoException {
        DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
        try {

            String gitRepository = domainTemplate.getGitRepository();
            String gitBranch = domainTemplate.getGitBranch();
            File localPath = File.createTempFile("/tmp/TestGitRepository/", "");
            localPath.delete();
            // then clone
            log.info("Cloning from {} : {} to {}", gitRepository, gitBranch, localPath);
            String templatePath = localPath.getAbsolutePath() + "/src/main/resources/templates";
            File htmlFileDirectory = new File(templatePath);
            domainTemplate.setGitFiles(new TreeSet<>());
            try (Git result = Git.cloneRepository().setURI(gitRepository).setDirectory(localPath).setBranch(gitBranch)
                    .call()) {

                // Note: the call() returns an opened repository already which
                // needs to be closed to avoid file handle leaks!
                printFIles(htmlFileDirectory, domainTemplate, localPath.getPath());

                result.close();
            }
            localPath.delete();

        } catch (Exception e) {
            throw new DynamoException(e);
        }
    }

    private void printFIles(File htmlFileDirectory, DomainTemplate domainTemplate, String basePath) {
        File[] files = htmlFileDirectory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File oneFile : files) {
            if (oneFile.isDirectory()) {
                printFIles(oneFile, domainTemplate, basePath);
                continue;
            }
            domainTemplate.getGitFiles().add(oneFile.getPath().replace(basePath, ""));
            System.out.println(oneFile.getPath().replace(basePath + "/", ""));
        }
    }

    private void refreshDomainFromGit(Domain domain, DomainTemplate domainTemplate) throws DynamoException {
        dynamoAssert.notNull(domainTemplate,
				"No Active Domain template found for Domain[" + domain.getName() + ", id: " + domain.getId() + "]");
		try {

			String gitRepository = domainTemplate.getGitRepository();
			String gitBranch = domainTemplate.getGitBranch();
			File localPath = File.createTempFile("/tmp/TestGitRepository/", "");
			localPath.delete();
			// then clone
			log.info("Cloning from {} : {} to {}", gitRepository ,gitBranch,  localPath);
			try (Git result = Git.cloneRepository().setURI(gitRepository).setDirectory(localPath).setBranch(gitBranch)
					.call()) {
				// Note: the call() returns an opened repository already which
				// needs to be closed to avoid file handle leaks!
				System.out.println("Having repository: " + result.getRepository().getDirectory());
				List<PageTemplate> pageTemplates = dynamoService
						.findPageTemplatesByDomainTemplateId(domainTemplate.getId());
				for (PageTemplate onePageTemplate : pageTemplates) {
					reloadPageTemplate(localPath, onePageTemplate);
				}

                List<PartTemplate> partTemplates = dynamoService
                        .findPartTemplateByDomainTemplate(domainTemplate.getId());
                for (PartTemplate onePartTemplate : partTemplates) {
                    reloadPageTemplate(localPath, onePartTemplate);
                }
                result.close();
			}
			localPath.delete();

		} catch (Exception e) {
			throw new DynamoException(e);
		}
		

	}

    private void reloadPageTemplate(File localPath, Template pageTemplate) throws IOException {
        String sourceFilePath = localPath.getPath() + "/" + pageTemplate.getGitFilePath();
		if (pageTemplate.getGitFilePath().startsWith("/")) {
			sourceFilePath = localPath.getPath() + pageTemplate.getGitFilePath();
		}
		File sourceFile = new File(sourceFilePath);
		String htmlContent = Files.toString(sourceFile, Charsets.UTF_8);
		log.info("Content from file {} is {}", sourceFilePath, htmlContent);
		pageTemplate.setHtmlContent(htmlContent);
	}

}
