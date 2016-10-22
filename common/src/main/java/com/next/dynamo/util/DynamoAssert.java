package com.next.dynamo.util;

import com.next.dynamo.exception.DynamoException;

import java.util.Date;

public class DynamoAssert {

    public static void notNull(Object object, String errorMessage) throws DynamoException {
        if(object == null){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Throws IllegalArgumentException if given data is null or empty or contains only spaces
     *
     * @param data
     * @param errorMessage
     */
    public static void assertNotBlank(String data, String errorMessage) {
        if (data == null || data.trim().equals("")) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void assertNotNull(Object data, String errorMessage) {
        if (data == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void assertBefore(Date beforeDate, Date afterDate, String errorMessage) {
        if (beforeDate.after(afterDate)) {
            throw new IllegalArgumentException(errorMessage);
        }
	}
}
