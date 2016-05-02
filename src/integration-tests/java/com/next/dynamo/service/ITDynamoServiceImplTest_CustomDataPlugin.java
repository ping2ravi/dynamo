package com.next.dynamo.service;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;

public class ITDynamoServiceImplTest_CustomDataPlugin extends BaseServiceItest{

	@Autowired
	private DynamoService dynamoService;
	
	@Test
	public void createACustomDataPluginWithValidData() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.test.plugin.TestPlugin", false, "TestPlugin");
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		CustomDataPlugin dbCustomDataPlugin = dynamoService.getCustomDataPluginById(customDataPlugin.getId());
		assertEqualCustomDataPlugin(customDataPlugin, dbCustomDataPlugin);
	}
	
	@Test
	public void createACustomDataPluginWithValidDataWhenOtherPluginExistsWithSameClassName() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.test.plugin.TestPlugin", false, "TestPlugin");
		customDataPlugin = dynamoService.saveCustomDataPlugin(customDataPlugin);
		
		CustomDataPlugin customDataPluginOther = createCustomDataPlugin("com.next.test.plugin.TestPlugin", false, "TestPlugin");
		customDataPluginOther = dynamoService.saveCustomDataPlugin(customDataPluginOther);

		assertEqualCustomDataPlugin(customDataPlugin, customDataPluginOther);

	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createACustomDataPluginWhenPluginNameIsNull() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.test.plugin.TestPlugin", false, null);
		dynamoService.saveCustomDataPlugin(customDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createACustomDataPluginWhenPluginNameIsEmptyString() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin("com.next.test.plugin.TestPlugin", false, "");
		dynamoService.saveCustomDataPlugin(customDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createACustomDataPluginWhenFullClassNameIsNull() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin(null, false, "TestPlugin");
		dynamoService.saveCustomDataPlugin(customDataPlugin);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void createACustomDataPluginWhenFullClassNameIsEmptyString() throws DynamoException{
		CustomDataPlugin customDataPlugin = createCustomDataPlugin("", false, "TestPlugin");
		dynamoService.saveCustomDataPlugin(customDataPlugin);
	}

}
