package com.next.dynamo.service.plugin;

import java.util.List;
import java.util.Map;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.DataPlugin;
import com.next.dynamo.persistance.UrlMapping;


public interface DataPluginService {

    List<UrlMapping> getAllUrlMappings() throws DynamoException;

    UrlMapping saveUrlMapping(UrlMapping urlMapping) throws DynamoException;

    void addDataPluginForUrlMapping(Long urlMappingId, List<DataPlugin> dataPlugins) throws DynamoException;

    Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws DynamoException;

    
    /*
    List<DataPlugin> getAllDataPlugins() throws DynamoException;

    List<DataPlugin> getAllGlobalDataPlugins() throws DynamoException;
    
    List<DataPlugin> getAllNonGlobalDataPlugins() throws DynamoException;

    
    List<DataPlugin> getDataPluginsByUrlMappingId(Long urlMappingId) throws DynamoException;
    List<DomainTemplate> getAllDomainTemplates(Long locationId) throws DynamoException;

    DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws DynamoException;

    DomainPageTemplate saveDomainPageTemplate(DomainPageTemplate domainPageTemplate) throws DynamoException;
    
    DomainPageTemplate getDomainPageTemplateByUrlAndDomainTemplate(String url, Long domainTemplateId) throws DynamoException;


    DomainTemplateFile saveDomainTemplateFile(Long domainTemplateId, String filePathAndName, long fileSize) throws DynamoException;
	*/
    void createAllCustomDataPlugins(List<String> classNames) throws DynamoException;
    
    void updateDbWithAllPlugins() throws DynamoException;
}
