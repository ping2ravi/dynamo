package com.next.dynamo.service.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.next.dynamo.service.plugin.AbstractDataPlugin;
import com.next.dynamo.service.plugin.WebDataPlugin;

@Component
public class PluginOne extends AbstractDataPlugin{

	public PluginOne(){
		super("PluginOne");
	}
	@Override
	public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ModelAndView mv) {
		 JsonObject context = (JsonObject) mv.getModel().get("context");
         context.add(getName(), new JsonPrimitive("Value1"));
	}

	@Override
	public void setSettings(String settings) {
		// TODO Auto-generated method stub
		
	}

}
