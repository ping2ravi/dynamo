package com.next.dynamo.service.ui;

import com.github.jknack.handlebars.Template;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.next.dynamo.persistance.*;
import com.next.dynamo.service.DynamoService;
import com.next.dynamo.service.plugin.HttpParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UiTemplateManagerHandleBarImpl implements UiTemplateManager<Template> {

	private Map<String, Map<Long, Template>> domainUiCompileTemplateMap;

    private Map<String, JsonObject> domainSettingMap;
    private Template errorTemplate;
    private Template exceptionTemplate;
    @Autowired
    private HandleBarManager handleBarManager;

    @Autowired
    private DynamoService uiTemplateService;

    private volatile boolean isInitialised = false;
    
	@Override
	public Template getCompiledTemplate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		boolean requestForDraft = isRequestForDraft(httpServletRequest, httpServletResponse);
        log.info("requestForDraft : {}", requestForDraft);
        if (requestForDraft) {
            refresh();// refresh if draft is requested
        }
        init();
        String domainName = httpServletRequest.getServerName();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
            log.info("No URL Mapping Found");
            return errorTemplate;
        }
        Template compiledPageTemplate = getCompiledPageTemplate(domainName, urlMapping.getId());
        if(compiledPageTemplate == null){
            log.info("No Domain page Template Found");
            return errorTemplate;
        }
//        if (requestForDraft) {
//            try {
//                return handleBarManager.getHandlebars().compile(domainPageTemplate.getHtmlContentDraft());
//            } catch (IOException e) {
//                logger.error("Exception occured : ",e);
//                return exceptionTemplate;
//            }
//        }

        return compiledPageTemplate;
	}
	private Template getCompiledPageTemplate(String domain, Long urlMappingId) {
		Map<Long, Template> pageTemplateMap = domainUiCompileTemplateMap.get(domain.toLowerCase());
//        if (pageTemplateMap == null) {
//            pageTemplateMap = domainUiTemplateMap.get("default");
//        }
        if (pageTemplateMap == null) {
        	log.info("Not found");
            return null;
        }
        return pageTemplateMap.get(urlMappingId);
    }
	private boolean isRequestForDraft(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String draftParamValue = httpServletRequest.getParameter("draft");
        log.info("draftParamValue : {}", draftParamValue);
        if (draftParamValue == null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie oneCookie : cookies) {
                    if (oneCookie.getName().equals("draft")) {
                        draftParamValue = oneCookie.getValue();
                    }
                }
            }
        } else {
            Cookie cookie = new Cookie("draft", draftParamValue);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }
        if (draftParamValue == null || !draftParamValue.equals("1")) {
            return false;
        }
        return true;
    }

	@Override
	public void refresh() {
		log.info("init resetting isInitialised to false : {}", isInitialised);
        isInitialised = false;
        init();
		
	}
	private void init() {
		log.info("init isInitialised = {}", isInitialised);
        if (isInitialised) {
            return;
        }
        synchronized (this) {
        	log.info("Refreshing UI cache");
            if (isInitialised) {
                return;
            }
            try {
                errorTemplate = handleBarManager.getHandlebars().compileInline("No Template Defined");
                exceptionTemplate = handleBarManager.getHandlebars().compileInline("InternalServer Error");

                domainUiCompileTemplateMap = new HashMap<>();
                domainSettingMap = new HashMap<String, JsonObject>();
                List<Domain> domains = uiTemplateService.getDomains(0, 100).getContent();
                PageTemplate detachedPageTemplate;
                JsonObject emptyDomainSettings = new JsonObject();
                JsonParser jsonParser = new JsonParser();
                
                for (Domain oneDomain : domains) {
                    DomainTemplate activeDomainPageTemplates = uiTemplateService.getActiveDomainTemplateOfDomain(oneDomain.getId());
                    if (activeDomainPageTemplates == null) {
                        continue;
                    }
                    
                    addDomainSettings(emptyDomainSettings, jsonParser, oneDomain);
                    
                    
                    List<PartTemplate> subTemplates = uiTemplateService.findPartTemplateByDomainTemplate(activeDomainPageTemplates.getId());
                    List<PageTemplate> pageTemplates = uiTemplateService.findPageTemplatesByDomainTemplateId(activeDomainPageTemplates.getId());
                    Map<Long, Template> pageCompiledTemplates = new HashMap<Long, Template>();
                    Template compiledTemplate;

                    for(PageTemplate onePageTemplate : pageTemplates){
                    	detachedPageTemplate = new PageTemplate();
                        BeanUtils.copyProperties(onePageTemplate, detachedPageTemplate);
                        applySubTemplates(detachedPageTemplate, subTemplates);
                        try{
                            compiledTemplate = handleBarManager.getHandlebars().compileInline(detachedPageTemplate.getHtmlContent());
                        	pageCompiledTemplates.put(onePageTemplate.getUrlMapping().getId(), compiledTemplate);	
                        }catch(Exception ex){
                        	log.error("unabel to compile template for {}", detachedPageTemplate.getUrlMapping().getUrlPattern());
                        	pageCompiledTemplates.put(detachedPageTemplate.getUrlMappingId(), exceptionTemplate);
                        }
                    }
                    
                    domainUiCompileTemplateMap.put(oneDomain.getName().toLowerCase(), pageCompiledTemplates);
//                    if (oneDomain.getLocationId() == null) {
//                        domainUiTemplateMap.put("default", pageTemplates);
//                        domainUiCompileTemplateMap.put("default", pageCompiledTemplates);
//                    }
                    addDomainAliasCompiledTemplates(oneDomain, pageCompiledTemplates);
                }
                isInitialised = true;

            } catch (Exception ex) {
            	log.error("Unable to load Plugins", ex);
            }
            log.info("Refreshed UI cache");
        }


    }

	private void addDomainAliasCompiledTemplates(Domain oneDomain, Map<Long, Template> pageCompiledTemplates) {
		Set<String> aliases = oneDomain.getAliases();
		if (aliases != null) {
		    for (String oneOtherDomainName : aliases) {
		        domainUiCompileTemplateMap.put(oneOtherDomainName.toLowerCase(), pageCompiledTemplates);
		    }
		}
	}

	private void addDomainSettings(JsonObject emptyDomainSettings, JsonParser jsonParser, Domain oneDomain) {
		JsonObject domainSettings;
		if(StringUtils.isEmpty(oneDomain.getSetting())){
			domainSettings = emptyDomainSettings;
		}else{
			try{
				domainSettings = jsonParser.parse(oneDomain.getSetting()).getAsJsonObject();
			}catch(Exception ex){
				log.error("Exception while building setting json object for : {}", oneDomain.getSetting(), ex);
				domainSettings = emptyDomainSettings;
			}
			
		}
		domainSettingMap.put(oneDomain.getName().toLowerCase(), domainSettings);

		Set<String> aliases = oneDomain.getAliases();
		if (aliases != null) {
		    for (String oneOtherDomainName : aliases) {
		        domainSettingMap.put(oneOtherDomainName.toLowerCase(), domainSettings);
		    }
		}
	}

    private void applySubTemplates(PageTemplate onePageTemplate, List<PartTemplate> subTemplates) {
    	String mainTemplateForPage = onePageTemplate.getMainTemplate().getHtmlContent();
        mainTemplateForPage = StringUtils.replace(mainTemplateForPage, "[[BODY]]", onePageTemplate.getHtmlContent());
        onePageTemplate.setHtmlContent(mainTemplateForPage);
        mainTemplateForPage = applySubTemplates(mainTemplateForPage, subTemplates);
        mainTemplateForPage = applySubTemplates(mainTemplateForPage, subTemplates);
        onePageTemplate.setHtmlContent(mainTemplateForPage);

    }

    private String applySubTemplates(String mainTemplateForPage, List<PartTemplate> subTemplates) {
        for (PartTemplate onePartTemplate : subTemplates) {
            String templateKey = "[[" + onePartTemplate.getPartName() + "]]";
            mainTemplateForPage = StringUtils.replace(mainTemplateForPage, templateKey, onePartTemplate.getHtmlContent());
        }
        return mainTemplateForPage;
    }


	@Override
	public JsonObject getDomainSettings(HttpServletRequest httpServletRequest) {
		init();
        String domain = httpServletRequest.getServerName().toLowerCase();
        return domainSettingMap.get(domain);
	}

	@Override
	public Integer getCacheTime(HttpServletRequest httpServletRequest) {
		init();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
            return 300;
        }
        return urlMapping.getHttpCacheTimeSeconds();
	}

    @Override
    public Long getDomainLocation(HttpServletRequest httpServletRequest) {
        init();
        try {
            JsonObject domainSettingMap = getDomainSettings(httpServletRequest);
            JsonElement locationJsonElement = domainSettingMap.get("locationId");
            if (locationJsonElement == null) {
                return null;
            }
            return locationJsonElement.getAsLong();
        } catch (Exception ex) {
            return null;
        }

    }
}
