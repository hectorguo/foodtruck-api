package com.amazonaws.lambda.foodtrucks.internal;


import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class InternalCalls {

	/**
	 * This method is calling an internal API which could be changed over time.
	 * It is only for use of this demo and not expected for external use.
	 */
	public static Map<String, Object> toSimpleMapValue(
			Map<String, AttributeValue> values) {
		return InternalUtils.toSimpleMapValue(values);
	}

}
