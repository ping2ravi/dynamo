package com.next.dynamo.service.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonParser;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.DataPlugin;
import com.next.dynamo.persistance.StaticDataPlugin;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;
import com.next.dynamo.service.DynamoService;

@Service
@Transactional
public class PluginManagerImpl implements PluginManager {

    @Autowired
    private BeanFactory springBeanFactory;
    @Autowired
    private DynamoService dynamoService;

    private List<PatternUrlMapping> urlPatterns;
    private List<WebDataPlugin> globalWebDataPlugins;
    private volatile boolean isInitialized = false;

    @Override
    public void refresh() throws DynamoException {
        isInitialized = false;
        init();
    }

    public void init() throws DynamoException {
        if (isInitialized) {
            return;
        }
        synchronized (this) {
            if(isInitialized){
                return;
            }
                JsonParser jsonParser = new JsonParser();

                List<UrlMapping> urlMappings = dynamoService.getAllUrlMappings();
                urlPatterns = new ArrayList<PatternUrlMapping>();
                List<WebDataPlugin> dataPlugins;
                WebDataPlugin oneWebDataPlugin;
                for (UrlMapping oneUrlMapping : urlMappings) {
                    dataPlugins = new ArrayList<WebDataPlugin>();
                    List<UrlMappingPlugin> urlMappingPlugins = dynamoService.findUrlMappingPluginByUrlMapping(oneUrlMapping.getId());
                    for (UrlMappingPlugin oneUrlMappingPlugin : urlMappingPlugins) {
                        if (oneUrlMappingPlugin.getDataPlugin().isDisabled()) {
                            continue;
                        }
                        oneWebDataPlugin = createDataPlugin(oneUrlMappingPlugin.getDataPlugin(), jsonParser);
                        if (oneWebDataPlugin != null) {
                            dataPlugins.add(oneWebDataPlugin);
                            String setting = oneUrlMappingPlugin.getSetting();
                            oneWebDataPlugin.setSettings(setting);
                        }
                        
                    }
                    PatternUrlMapping onePatternUrlMapping = new PatternUrlMapping(oneUrlMapping, dataPlugins);
                    urlPatterns.add(onePatternUrlMapping);
                }
                //loadGlobalDataPlugins(jsonParser);
                isInitialized = true;

        }

    }
/*
    private void loadGlobalDataPlugins(JsonParser jsonParser) throws DynamoException {
        List<DataPlugin> globalDataPlugins = dataPluginService.getAllGlobalDataPlugins();
        globalWebDataPlugins = new ArrayList<WebDataPlugin>(globalDataPlugins.size());
        for(DataPlugin oneDataPlugin : globalDataPlugins){
            WebDataPlugin oneWebDataPlugin = createDataPlugin(oneDataPlugin, jsonParser);
            oneWebDataPlugin.setSettings("{}");// just empty valid Json
            globalWebDataPlugins.add(oneWebDataPlugin);

        }
        System.out.println("Loading Global Data Plugins Done");
    }*/

    private WebDataPlugin createDataPlugin(CustomDataPlugin customDataPlugin, JsonParser jsonParser) {
        Class<?> clz;
        try {
            clz = Class.forName(customDataPlugin.getFullClassName());
            WebDataPlugin dataPlugin = (WebDataPlugin) springBeanFactory.getBean(clz, customDataPlugin.getPluginName());
            return dataPlugin;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WebDataPlugin createDataPlugin(StaticDataPlugin staticDataPlugin, JsonParser jsonParser) {
        WebStaticDataPlugin webStaticDataPlugin = new WebStaticDataPlugin(jsonParser.parse(staticDataPlugin.getContent()).getAsJsonObject(), staticDataPlugin.getPluginName());
        return webStaticDataPlugin;

    }

    private WebDataPlugin createDataPlugin(DataPlugin dataPlugin, JsonParser jsonParser) {
        if (dataPlugin instanceof CustomDataPlugin) {
            return createDataPlugin((CustomDataPlugin) dataPlugin, jsonParser);
        }
        if (dataPlugin instanceof StaticDataPlugin) {
            return createDataPlugin((StaticDataPlugin) dataPlugin, jsonParser);
        }
        throw new RuntimeException("Should never come here");
    }

    @Override
    public void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, boolean addData, boolean applyGenericPlugins) throws DynamoException {
        init();
        String requestedUrl = httpServletRequest.getRequestURI();
        //System.out.println("Handling Url = " + requestedUrl);
        PatternUrlMapping patternUrlMapping = findDataPlugins(httpServletRequest, requestedUrl);
        if(patternUrlMapping == null){
            return;
        }
        /*if (patternUrlMapping.getUrlMapping().isSecured()) {
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            if (user == null) {
                throw new NotLoggedInException("User Not logged In");
            }
        }*/
        if(addData){
            // First apply all Global Data Plugins
            if(applyGenericPlugins){
                for (WebDataPlugin oneWebDataPlugin : globalWebDataPlugins) {
                    oneWebDataPlugin.applyPlugin(httpServletRequest, httpServletResponse, modelAndView);
                }
            }
            List<WebDataPlugin> plugins = patternUrlMapping.getDataPlugins();
            for (WebDataPlugin oneWebDataPlugin : plugins) {
                oneWebDataPlugin.applyPlugin(httpServletRequest, httpServletResponse, modelAndView);
            }

        }
        
    }

    private PatternUrlMapping findDataPlugins(HttpServletRequest httpServletRequest, String url) {
        String apiUrl = url;
        if (url.startsWith("/api")) {
            apiUrl = url.substring(4);
        }
        for (PatternUrlMapping onePatternUrlMapping : urlPatterns) {
            Pattern r = onePatternUrlMapping.getPattern();
            if (r == null) {
                if (url.equalsIgnoreCase(onePatternUrlMapping.getUrlMapping().getUrlPattern()) || apiUrl.equalsIgnoreCase(onePatternUrlMapping.getUrlMapping().getUrlPattern())) {
                    httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, Collections.emptyMap());
                    httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                    return onePatternUrlMapping;
                }
                for (String oneUrl : onePatternUrlMapping.getAliases()) {
                    if (url.equalsIgnoreCase(oneUrl) || apiUrl.equalsIgnoreCase(oneUrl)) {
                        httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, Collections.emptyMap());
                        httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                        return onePatternUrlMapping;
                    }
                }
            } else {
                Matcher m = r.matcher(url);
                if (m.find()) {
                    Map<String, String> pathParameters = new LinkedHashMap<String, String>();
                    int count = 1;
                    for (String oneParam : onePatternUrlMapping.getParameters()) {
                        pathParameters.put(oneParam, m.group(count));
                        count++;
                    }

                    httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, pathParameters);
                    httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                    return onePatternUrlMapping;
                }
            }

        }
        return null;
    }

}
