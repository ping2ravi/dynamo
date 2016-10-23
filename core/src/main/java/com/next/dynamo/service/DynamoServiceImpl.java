package com.next.dynamo.service;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.*;
import com.next.dynamo.persistance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.next.dynamo.util.DynamoAssert.assertNotBlank;
import static com.next.dynamo.util.DynamoAssert.notNull;

@Service
@Transactional(rollbackFor=Exception.class)
public class DynamoServiceImpl implements DynamoService {

	@Autowired
	private DomainRepository domainRepository;
	@Autowired
	private DomainTemplateRepository domainTemplateRepository;
	@Autowired
	private PageTemplateRepository pageTemplateRepository;
	@Autowired
	private PartTemplateRepository partTemplateRepository;
	@Autowired
	private CustomDataPluginRepository customDataPluginRepository;
	@Autowired
	private StaticDataPluginRepository staticDataPluginRepository;
	@Autowired
	private UrlMappingRepository urlMappingRepository;
	@Autowired
	private UrlMappingPluginRepository urlMappingPluginRepository;

	@Override
	public Page<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		PageRequest page = new PageRequest(pageNumber, pageSize, sort);
		Page<Domain> domains = domainRepository.findAll(page);
		return domains;
	}

	@Override
	public Domain saveDomain(Domain domain) throws DynamoException {
        assertNotBlank(domain.getName(), "Domain Name can not be null or empty");
        if(domain.getExtendedDomain() != null){
			Domain extendedDomain = domainRepository.findOne(domain.getExtendedDomain().getId());
			domain.setExtendedDomain(extendedDomain);
		}
		domain = domainRepository.save(domain);
		return domain;
	}

	@Override
	public Domain getDomainById(Long domainId) throws DynamoException {
		return domainRepository.findOne(domainId);
	}

	@Override
	public DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws DynamoException {
        notNull(domainTemplate.getDomain(), "Domain Template ,ust have valid domain");
        DomainTemplate currentActiveDomainTemplate = domainTemplateRepository.findActiveDomainTemplateByDomainId(domainTemplate.getDomain().getId());
		if(currentActiveDomainTemplate!=null && domainTemplate.isActive() && !currentActiveDomainTemplate.getId().equals(domainTemplate.getId())){
			throw new DynamoException("{active.domain.template.exists.error}");
		}
		domainTemplate = domainTemplateRepository.save(domainTemplate);
		return domainTemplate;
	}

	@Override
	public DomainTemplate getDomainTemplateById(Long domainTemplateId) throws DynamoException {
		return domainTemplateRepository.findOne(domainTemplateId);
	}
	
	@Override
	public DomainTemplate getActiveDomainTemplateOfDomain(Long domainId) throws DynamoException {
		return domainTemplateRepository.findActiveDomainTemplateByDomainId(domainId);
	}
	
	@Override
	public List<DomainTemplate> getDomainTemplatesOfDomain(Long domainId) throws DynamoException{
		return domainTemplateRepository.findDomainTemplateByDomainId(domainId);
	}

    @Override
    public List<DomainTemplate> getAllDomainTemplates() throws DynamoException {
        return domainTemplateRepository.findAll();
    }


    @Override
    public CustomDataPlugin saveCustomDataPlugin(CustomDataPlugin customDataPlugin) throws DynamoException {
		CustomDataPlugin dbCustomDataPlugin = customDataPluginRepository.findByFullClassName(customDataPlugin.getFullClassName());
		if(dbCustomDataPlugin != null){
			dbCustomDataPlugin.setPluginName(customDataPlugin.getPluginName());
			dbCustomDataPlugin.setDisabled(customDataPlugin.isDisabled());
			customDataPlugin = customDataPluginRepository.save(dbCustomDataPlugin);
		}else{
			customDataPlugin = customDataPluginRepository.save(customDataPlugin);
		}
		return customDataPlugin;
	}
	
	@Override
	public CustomDataPlugin getCustomDataPluginById(Long customDataPluginId) throws DynamoException {
		return customDataPluginRepository.findOne(customDataPluginId);
	}

	@Override
	public StaticDataPlugin saveStaticDataPlugin(StaticDataPlugin staticDataPlugin) throws DynamoException {
		staticDataPlugin = staticDataPluginRepository.save(staticDataPlugin);
		return staticDataPlugin;
	}

	@Override
	public StaticDataPlugin getStaticDataPluginById(Long staticDataPluginId) throws DynamoException {
		return staticDataPluginRepository.findOne(staticDataPluginId);
	}

	@Override
	public UrlMapping saveUrlMapping(UrlMapping urlMapping) throws DynamoException {
		urlMapping = urlMappingRepository.save(urlMapping);
		return urlMapping;
	}

	@Override
	public UrlMapping getUrlMappingById(Long urlMappingId) throws DynamoException {
		return urlMappingRepository.findOne(urlMappingId);
	}
	
	@Override
	public List<UrlMapping> getUrlMappingByDomainId(Long domainId) throws DynamoException{
		return urlMappingRepository.findUrlMappingByDomainId(domainId);
	}
	
	@Override
    public List<UrlMapping> getAllUrlMappings() throws DynamoException {
        return urlMappingRepository.findAll();
    }

	@Override
	public UrlMappingPlugin saveUrlMappingPlugin(UrlMappingPlugin urlMappingPlugin) throws DynamoException {
		urlMappingPlugin = urlMappingPluginRepository.save(urlMappingPlugin);
		return urlMappingPlugin;
	}
	
	@Override
	public List<UrlMappingPlugin> findUrlMappingPluginByUrlMapping(Long urlMappingId) throws DynamoException{
		return urlMappingPluginRepository.findUrlMappingPluginsByUrlMappingId(urlMappingId);
	}

	
	@Override
	public PageTemplate savePageTemplate(PageTemplate pageTemplate) throws DynamoException {
		return pageTemplateRepository.save(pageTemplate);
	}

	@Override
	public PageTemplate getPageTemplateById(Long pageTemplateId) throws DynamoException {
		return pageTemplateRepository.findOne(pageTemplateId);
	}

	@Override
    public List<PageTemplate> findPageTemplatesByDomainTemplateId(Long domainTemplateId) throws DynamoException {
        return pageTemplateRepository.findPageTemplatesByDomainTemplateId(domainTemplateId);
	}

	@Override
	public List<PartTemplate> findPartTemplateByDomainTemplate(Long domainTemplateId) throws DynamoException {
		return partTemplateRepository.findPartTemplatesByDomainTemplateId(domainTemplateId);
	}

	@Override
	public PartTemplate savePartTemplate(PartTemplate partTemplate) throws DynamoException {
		return partTemplateRepository.save(partTemplate);
	}

    @Override
    public PartTemplate getPartTemplateById(Long partTemplateId) throws DynamoException {
        return partTemplateRepository.findOne(partTemplateId);
    }

}
