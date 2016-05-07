package com.next.dynamo.util;

import com.next.dynamo.exception.DynamoException;

public class DynamoAssert {

	public void notNull(Object object, String errorMessage) throws DynamoException{
		if(object == null){
			throw new DynamoException(errorMessage);
		}
	}
}
