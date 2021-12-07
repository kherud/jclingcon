package org.potassco.clingcon.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Value types that can be returned by a theory.
 */
public enum ValueType {
	INT(0),
	DOUBLE(1),
	SYMBOL(2);

    private static final Map<Integer, ValueType> mapping = new HashMap<>();

	static {
	    for (ValueType solveEventType : ValueType.values()) {
	    	mapping.put(
	          solveEventType.getValue(),
	          solveEventType
	        );
	    }
	}

	public static ValueType fromValue(int code) {
		return mapping.get(code);
	}

    private final int code;

    ValueType(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }

}
