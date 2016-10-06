package com.next.dynamo.service.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

public interface UiTemplateManager<T> {

	T getCompiledTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void refresh();

    JsonObject getDomainSettings(HttpServletRequest httpServletRequest);

    Integer getCacheTime(HttpServletRequest httpServletRequest);
	    
}
