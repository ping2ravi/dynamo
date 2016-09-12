package com.next.dynamo.service.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.next.dynamo.exception.DynamoException;

public interface PluginManager {
	void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ModelAndView modelAndView, boolean addData, boolean applyGenericPlugins) throws DynamoException;

	void refresh();

}
