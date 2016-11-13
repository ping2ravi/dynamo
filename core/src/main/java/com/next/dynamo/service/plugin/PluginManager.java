package com.next.dynamo.service.plugin;

import com.next.dynamo.exception.DynamoException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PluginManager {
	void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ModelAndView modelAndView, boolean addData, boolean applyGenericPlugins) throws DynamoException;

	void refresh() throws DynamoException ;

    void updateDbWithAllPlugins() throws DynamoException;

}
