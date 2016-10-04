package com.next.dynamo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import com.next.dynamo.persistance.StaticDataPlugin;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;

public interface DynamoService {

	Page<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException;
	
	Domain saveDomain(Domain domain) throws DynamoException;
	
	Domain getDomainById(Long domainId) throws DynamoException;
	
	DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws DynamoException;
	
	DomainTemplate getDomainTemplateById(Long domainTemplateId) throws DynamoException;
	
	DomainTemplate getActiveDomainTemplateOfDomain(Long domainId) throws DynamoException;
	
	List<DomainTemplate> getDomainTemplatesOfDomain(Long domainId) throws DynamoException;
	
	CustomDataPlugin saveCustomDataPlugin(CustomDataPlugin customDataPlugin) throws DynamoException;

	CustomDataPlugin getCustomDataPluginById(Long customDataPluginId) throws DynamoException;
	
	StaticDataPlugin saveStaticDataPlugin(StaticDataPlugin staticDataPlugin) throws DynamoException;

	StaticDataPlugin getStaticDataPluginById(Long staticDataPluginId) throws DynamoException;
	
	UrlMapping saveUrlMapping(UrlMapping urlMapping) throws DynamoException;

	UrlMapping getUrlMappingById(Long urlMappingId) throws DynamoException;
	
	List<UrlMapping> getUrlMappingByDomainId(Long domainId) throws DynamoException;
	
	List<UrlMapping> getAllUrlMappings() throws DynamoException;
	
	UrlMappingPlugin saveUrlMappingPlugin(UrlMappingPlugin urlMappingPlugin) throws DynamoException;

	List<UrlMappingPlugin> findUrlMappingPluginByUrlMapping(Long urlMappingId) throws DynamoException;

	PageTemplate savePageTemplate(PageTemplate pageTemplate) throws DynamoException;

	PageTemplate getPageTemplateById(Long pageTemplateId) throws DynamoException;
	
	List<PageTemplate> getPageTemplatesByDomainTemplateId(Long domainTemplateId) throws DynamoException;
}
