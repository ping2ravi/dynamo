package com.next.dynamo.service;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.StaticDataPlugin;

public class ITDynamoServiceImplTest_StaticDataPlugin extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Test
	public void createAStaticDataPluginWithValidData() throws DynamoException{
		StaticDataPlugin StaticDataPlugin = createStaticDataPlugin("Static Content", false, "TestPlugin");
		StaticDataPlugin = dynamoService.saveStaticDataPlugin(StaticDataPlugin);
		
		StaticDataPlugin dbStaticDataPlugin = dynamoService.getStaticDataPluginById(StaticDataPlugin.getId());
		assertEqualStaticDataPlugin(StaticDataPlugin, dbStaticDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createAStaticDataPluginWhenPluginNameIsNull() throws DynamoException{
		StaticDataPlugin StaticDataPlugin = createStaticDataPlugin("com.next.test.plugin.TestPlugin", false, null);
		dynamoService.saveStaticDataPlugin(StaticDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createAStaticDataPluginWhenPluginNameIsEmptyString() throws DynamoException{
		StaticDataPlugin StaticDataPlugin = createStaticDataPlugin("com.next.test.plugin.TestPlugin", false, "");
		dynamoService.saveStaticDataPlugin(StaticDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createAStaticDataPluginWhenContentIsNull() throws DynamoException{
		StaticDataPlugin StaticDataPlugin = createStaticDataPlugin(null, false, "TestPlugin");
		dynamoService.saveStaticDataPlugin(StaticDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createAStaticDataPluginWhenContentIsEmptyString() throws DynamoException{
		StaticDataPlugin StaticDataPlugin = createStaticDataPlugin("", false, "TestPlugin");
		dynamoService.saveStaticDataPlugin(StaticDataPlugin);
	}

}
