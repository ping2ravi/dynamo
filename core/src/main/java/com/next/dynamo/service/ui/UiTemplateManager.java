package com.next.dynamo.service.ui;

import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UiTemplateManager<T> {

	T getCompiledTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void refresh();

    JsonObject getDomainSettings(HttpServletRequest httpServletRequest);

    Integer getCacheTime(HttpServletRequest httpServletRequest);

    Long getDomainLocation(HttpServletRequest httpServletRequest);

}
