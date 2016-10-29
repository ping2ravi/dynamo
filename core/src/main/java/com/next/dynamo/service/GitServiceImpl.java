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
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class GitServiceImpl implements GitService {

	@Autowired
	private DynamoService dynamoService;
	@Autowired
	private DynamoAssert dynamoAssert;
    @Autowired
    private DynamoTemplateManager dynamoTemplateManager;

	@Override
	public void refreshDomainFromGit(Long domainId) throws DynamoException {
		Domain domain = dynamoService.getDomainById(domainId);
		DomainTemplate currentActiveDomainTemplate = dynamoService.getActiveDomainTemplateOfDomain(domainId);

		refreshDomainFromGit(currentActiveDomainTemplate);
	}

	@Override
	public void refreshDomainFromGit(Long domainId, Long domainTemplateId) throws DynamoException {
		DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
		refreshDomainFromGit(domainTemplate);

	}

    @Override
    public void refreshFileList(Long domainTemplateId) throws DynamoException {
        DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
        try {
			File mainBasePath = new File("/tmp/TestGitRepository/");
			mainBasePath.delete();

            String gitRepository = domainTemplate.getGitRepository();
            String gitBranch = domainTemplate.getGitBranch();
			File localPath = File.createTempFile("/tmp/TestGitRepository/github/", "");
			localPath.delete();
            // then clone
            log.info("Cloning from {} : {} to {}", gitRepository, gitBranch, localPath);
            domainTemplate.setGitFiles(new TreeSet<>());
            try (Git result = Git.cloneRepository().setURI(gitRepository).setDirectory(localPath).setBranch(gitBranch)
                    .call()) {

                // Note: the call() returns an opened repository already which
                // needs to be closed to avoid file handle leaks!
				scanHtmlFiles(localPath, domainTemplate);

                result.close();
            }
            localPath.delete();

        } catch (Exception e) {
            throw new DynamoException(e);
        }
    }

	@Override
	public void refreshDomainTemplateFromGit(Long domainTemplateId) throws DynamoException {
		DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
		refreshDomainFromGit(domainTemplate);
	}

	@Override
	public List<String> getDomaintemplateGitFiles(Long domainTemplateId) throws DynamoException {
		DomainTemplate domainTemplate = dynamoService.getDomainTemplateById(domainTemplateId);
		return Collections.unmodifiableList(new ArrayList(domainTemplate.getGitFiles()));
	}

	private void scanHtmlFiles(File localPath, DomainTemplate domainTemplate) {
		String templatePath = localPath.getAbsolutePath() + "/src/main/resources/templates";
		File htmlFileDirectory = new File(templatePath);
		scanFiles(htmlFileDirectory, domainTemplate, localPath.getPath());
	}

	private void scanFiles(File htmlFileDirectory, DomainTemplate domainTemplate, String basePath) {
		File[] files = htmlFileDirectory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File oneFile : files) {
            if (oneFile.isDirectory()) {
				scanFiles(oneFile, domainTemplate, basePath);
				continue;
            }
            domainTemplate.getGitFiles().add(oneFile.getPath().replace(basePath, ""));
            System.out.println(oneFile.getPath().replace(basePath + "/", ""));
        }
    }

	private void refreshDomainFromGit(DomainTemplate domainTemplate) throws DynamoException {
		dynamoAssert.notNull(domainTemplate,
				"No Active Domain template found for Domain[" + domainTemplate.getDomain().getName() + ", id: " + domainTemplate.getDomain().getId() + "]");
		try {
			File mainBasePath = new File("/tmp/TestGitRepository/");
			mainBasePath.delete();

			String gitRepository = domainTemplate.getGitRepository();
			String gitBranch = domainTemplate.getGitBranch();
			File localPath = File.createTempFile("/tmp/TestGitRepository/github/", "");
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
			scanHtmlFiles(localPath, domainTemplate);
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


        Map<String, Object> contextParams = new HashMap<>();

        contextParams.put("s3_static_content_dir", "https://s3.ap-south-1.amazonaws.com/siorg/website");
        contextParams.put("s3_dynamic_css_dir", "https://s3.ap-south-1.amazonaws.com/siorg/website/custom/css");
        htmlContent = dynamoTemplateManager.processTemplate(htmlContent, contextParams);
        pageTemplate.setHtmlContent(htmlContent);
	}

}
