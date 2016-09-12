package com.next.dynamo.service.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.DataPlugin;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;
import com.next.dynamo.persistance.repository.CustomDataPluginRepository;
import com.next.dynamo.persistance.repository.DataPluginRepository;
import com.next.dynamo.persistance.repository.DomainRepository;
import com.next.dynamo.persistance.repository.DomainTemplateRepository;
import com.next.dynamo.persistance.repository.UrlMappingPluginRepository;
import com.next.dynamo.persistance.repository.UrlMappingRepository;

@Service
@Transactional(rollbackFor={Throwable.class})
public class DataPluginServiceImpl implements DataPluginService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private UrlMappingPluginRepository urlMappingPluginRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainTemplateRepository domainTemplateRepository;
    //@Autowired
    //private DomainPageTemplateRepository domainPageTemplateRepository;
    //@Autowired
    //private DomainTemplateFileRepository domainTemplateFileRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DataPluginRepository dataPluginRepository;
    @Autowired
    private CustomDataPluginRepository customDataPluginRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public List<UrlMapping> getAllUrlMappings() throws DynamoException {
        return urlMappingRepository.findAll();
    }

    @Override
    public Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws DynamoException {
        return null;
    }
/*
    @Override
    public List<DomainTemplate> getAllDomainTemplates(Long locationId) throws DynamoException {
        if (locationId == null) {
            return domainTemplateRepository.getGlobalDomainTemplates();
        }
        return domainTemplateRepository.getLocationDomainTemplates(locationId);
    }

    @Override
    public DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws DynamoException {
        entityManager.merge(domainTemplate);
        Domain domain = domainRepository.findOne(domainTemplate.getDomainId());
        domainTemplate.setDomain(domain);
        domainTemplate = domainTemplateRepository.save(domainTemplate);
        return domainTemplate;
    }

    @Override
    public DomainPageTemplate saveDomainPageTemplate(DomainPageTemplate domainPageTemplate) throws DynamoException {
        domainPageTemplate = entityManager.merge(domainPageTemplate);
        UrlMapping urlMapping = entityManager.merge(domainPageTemplate.getUrlMapping());
        domainPageTemplate.setUrlMapping(urlMapping);
        DomainTemplate domainTemplate = domainTemplateRepository.findOne(domainPageTemplate.getDomainTemplateId());
        domainPageTemplate.setDomainTemplate(domainTemplate);

        domainPageTemplate = domainPageTemplateRepository.save(domainPageTemplate);
        return domainPageTemplate;
    }

    @Override
    public DomainTemplateFile saveDomainTemplateFile(Long domainTemplateId, String filePathAndName, long fileSize) throws DynamoException {
    	System.out.println("Getting Domain Template for id : "+ domainTemplateId);
        DomainTemplate domainTemplate = domainTemplateRepository.findOne(domainTemplateId);
    	System.out.println("Domain Template : "+ domainTemplate);

        filePathAndName = filePathAndName.toLowerCase();
    	System.out.println("Getting Domain Template file for filePathAndName : "+ filePathAndName);
        DomainTemplateFile domainTemplateFile = domainTemplateFileRepository.getDomainTemplateFileByFileName(filePathAndName);
    	System.out.println("Domain Template File : "+ domainTemplateFile);

        if (domainTemplateFile == null) {
            domainTemplateFile = new DomainTemplateFile();
            domainTemplateFile.setFileName(filePathAndName);
        }
        domainTemplateFile.setSize(fileSize);
        domainTemplateFile.setDomainTemplate(domainTemplate);
    	System.out.println("Saving Domain Template file for filePathAndName : "+ domainTemplateFile);

        domainTemplateFile = domainTemplateFileRepository.save(domainTemplateFile);
        return domainTemplateFile;
    }
    @Override
    public List<DataPlugin> getDataPluginsByUrlMappingId(Long urlMappingId) throws DynamoException {
        return dataPluginRepository.getDataPluginOfUrlMapping(urlMappingId);
    }
    @Override
    public List<DataPlugin> getAllDataPlugins() throws DynamoException {
        return dataPluginRepository.findAll();
    }
*/
    @Override
    public UrlMapping saveUrlMapping(UrlMapping urlMapping) throws DynamoException {
        return urlMappingRepository.save(urlMapping);
    }

    

    

    @Override
    public void addDataPluginForUrlMapping(Long urlMappingId, List<DataPlugin> dataPlugins) throws DynamoException {
        UrlMapping urlMapping = urlMappingRepository.findOne(urlMappingId);
        List<UrlMappingPlugin> urlMappingPlugins = urlMappingPluginRepository.findUrlMappingPluginsByUrlMappingId(urlMapping.getId());
        if (urlMappingPlugins == null) {
        	urlMappingPlugins = new ArrayList<UrlMappingPlugin>();
        }

        boolean existing;
        for (DataPlugin oneDataPlugin : dataPlugins) {
            existing = false;
            for (UrlMappingPlugin oneUrlMappingPlugin : urlMappingPlugins) {
                if (oneUrlMappingPlugin.getDataPlugin().getId().equals(oneDataPlugin.getId())) {
                    existing = true;
                }
            }
            if (existing) {
                continue;
            }
            oneDataPlugin = entityManager.merge(oneDataPlugin);
            UrlMappingPlugin oneUrlMappingPlugin = new UrlMappingPlugin();
            oneUrlMappingPlugin.setUrlMapping(urlMapping);
            oneUrlMappingPlugin.setDataPlugin(oneDataPlugin);
            oneUrlMappingPlugin = urlMappingPluginRepository.save(oneUrlMappingPlugin);
        }

        for (UrlMappingPlugin oneUrlMappingPlugin : urlMappingPlugins) {
            existing = false;
            for (DataPlugin oneDataPlugin : dataPlugins) {
                if (oneUrlMappingPlugin.getDataPlugin().getId().equals(oneDataPlugin.getId())) {
                    existing = true;
                }
            }
            if (existing) {
                continue;
            }
            urlMappingPluginRepository.delete(oneUrlMappingPlugin);
        }

    }

    @Override
    public void createAllCustomDataPlugins(List<String> classNames) throws DynamoException {
        List<CustomDataPlugin> allCustomDataPlugins = customDataPluginRepository.findAll();
        Set<String> allExistingPluginClassNames = new HashSet<String>();
        for (CustomDataPlugin oneCustomDataPlugin : allCustomDataPlugins) {
            allExistingPluginClassNames.add(oneCustomDataPlugin.getFullClassName());
        }
        for(String oneClass : classNames){
            if (allExistingPluginClassNames.contains(oneClass)) {
                continue;
            }
            CustomDataPlugin customDataPlugin = new CustomDataPlugin();
            customDataPlugin.setFullClassName(oneClass);
            customDataPlugin.setDisabled(false);
            customDataPlugin.setPluginName(getClassName(oneClass));
            customDataPlugin = customDataPluginRepository.save(customDataPlugin);
        }
        
    }

    private String getClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
    @Override
    public void updateDbWithAllPlugins() throws DynamoException {
        Map<String, WebDataPlugin> allWebDataPlugins = applicationContext.getBeansOfType(WebDataPlugin.class);
        List<String> allPluginImplementations = new ArrayList<String>();
        for (Entry<String, WebDataPlugin> oneEntry : allWebDataPlugins.entrySet()) {
            if (oneEntry.getValue() instanceof WebStaticDataPlugin) {
                continue;
            }
            allPluginImplementations.add(oneEntry.getValue().getClass().getName());
            System.out.println(oneEntry.getValue().getClass().getName());
        }
        createAllCustomDataPlugins(allPluginImplementations);
    }
/*
    @Override
    public List<DataPlugin> getAllGlobalDataPlugins() throws DynamoException {
        return dataPluginRepository.getAllGlobalDataPlugins();
    }

    @Override
    public List<DataPlugin> getAllNonGlobalDataPlugins() throws DynamoException {
        return dataPluginRepository.getAllNonGlobalDataPlugins();
    }

	@Override
	public DomainPageTemplate getDomainPageTemplateByUrlAndDomainTemplate(String url, Long domainTemplateId) throws DynamoException {
		return domainPageTemplateRepository.getDomainPageTemplateByUrlAndDomainTemplate(url, domainTemplateId);
	}
	*/
}
