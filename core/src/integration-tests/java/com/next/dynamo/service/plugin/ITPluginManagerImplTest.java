package com.next.dynamo.service.plugin;

import com.google.gson.JsonObject;
import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.StaticDataPlugin;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;
import com.next.dynamo.service.BaseServiceItest;
import com.next.dynamo.service.DynamoService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class ITPluginManagerImplTest extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Autowired
	private PluginManager pluginManager;
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url pattern which doesnt have any
	 * path parameters
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
		final String url = "/index.html";
		final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(urlMapping.getUrlPattern());
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNotNull(context.get(staticDataPluginName));
	
	}
	/**
	 * Have two Data Plugins defined and then trying to apply it on the alias which doesnt have any
	 * path parameters
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_whenApplyPluginsOnoneOfAlias() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/index.templates";
        final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(alias);
		pluginManager.refresh();
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNotNull(context.get(staticDataPluginName));
	
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url which do not exists in our system
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_OnInvalidUrl() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI("/some/Invalid/Url");
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNull(context.get(customDataPluginName));
		Assert.assertNull(context.get(staticDataPluginName));
	
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url pattern with /api in front
	 *  which doesnt have any path parameters
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_withApiOnUrlPattern() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/index.templates";
        final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI("/api"+url);
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNotNull(context.get(staticDataPluginName));
	
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url alias with /api in front
	 *  which doesnt have any path parameters
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_withApiOnUrlAlias() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/index.templates";
        final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI("/api"+alias);
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNotNull(context.get(staticDataPluginName));
	
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url pattern which doesnt have any
	 * path parameters and one Plugin is disbaled
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_forDisabledPlugin() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/index.templates";
        final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), true, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(urlMapping.getUrlPattern());
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNull(context.get(staticDataPluginName));
	
	}
	
	/**
	 * Create Custome Plugin with unknown class and try to apply it.
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_forUnknownPlugin() throws DynamoException{
		final String customDataPluginName = "PluginOne";
        final String url = "/index.templates";
        final String alias = "/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.DoNotExists", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(urlMapping.getUrlPattern());
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNull(context.get(customDataPluginName));
	
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url pattern which have
	 * path parameters, url invoked is main url
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_withPathParams() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/{userId}/index.templates";
        final String alias = "/{userId}/home";
        final String invokedUrl = "/100/index.templates";
        UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(invokedUrl);
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNotNull(context.get(customDataPluginName));
		Assert.assertNotNull(context.get(staticDataPluginName));
		Assert.assertNotNull(httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM));
		Map<String, String> pathParameters = (Map<String, String>)httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
		Assert.assertEquals("100", pathParameters.get("userId"));
		Assert.assertNotNull(httpServletRequest.getAttribute(HttpParameters.URL_MAPPING));
	}
	
	/**
	 * Have two Data Plugins defined and then trying to apply it on the url pattern which have
	 * path parameters, url invoked is one of alias
	 * 
	 */
	@Test
	public void test_applyAllPluginsForUrl_withPathParamsAliasUrl() throws DynamoException{
		final String customDataPluginName = "PluginOne";
		final String staticDataPluginName = "SomeDataPlugin";
        final String url = "/{userId}/index.templates";
        final String alias = "/{userId}/home";
		final String invokedUrl = "/100/home";
		UrlMapping urlMapping = createValidUrlMappingInDatabase(dynamoService, url, alias);

		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.dynamo.service.plugin.impl.PluginOne", false, customDataPluginName);
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Some", "Data");
		StaticDataPlugin staticDataPlugin = createStaticDataPlugin(jsonObject.toString(), false, staticDataPluginName);
		staticDataPlugin = dynamoService.saveStaticDataPlugin(staticDataPlugin);
		
		UrlMappingPlugin urlMappingPlugin1 = createUrlMappingPlugin(customDataPlugin, "", urlMapping);
		urlMappingPlugin1 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin1);
		
		UrlMappingPlugin urlMappingPlugin2 = createUrlMappingPlugin(staticDataPlugin, "", urlMapping);
		urlMappingPlugin2 = dynamoService.saveUrlMappingPlugin(urlMappingPlugin2);
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ModelAndView modelAndView = new ModelAndView();
		JsonObject context = new JsonObject();
		modelAndView.getModel().put("context", context);
		boolean addData = true;
		boolean applyGenericPlugins = false;
		httpServletRequest.setRequestURI(invokedUrl);
		pluginManager.refresh();//Refreshing the latest data from DB as we are not dirties the context after each method to keep it faster
		pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, addData, applyGenericPlugins);
	
		Assert.assertNull(context.get(customDataPluginName));
		Assert.assertNull(context.get(staticDataPluginName));
	
	}
}
