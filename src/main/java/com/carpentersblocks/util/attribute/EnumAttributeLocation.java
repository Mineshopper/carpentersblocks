package com.carpentersblocks.util.attribute;

import java.util.HashMap;
import java.util.Map;

public enum EnumAttributeLocation {

    DOWN(0),
    UP(1),
    NORTH(2),
    SOUTH(3),
    WEST(4),
    EAST(5),
	HOST(6);
	
	private int _value;
    private static Map<Integer, EnumAttributeLocation> map = new HashMap<Integer, EnumAttributeLocation>();

    static {
        for (EnumAttributeLocation enumValue : EnumAttributeLocation.values()) {
            map.put(enumValue._value, enumValue);
        }
    }

    private EnumAttributeLocation(final int value) {
    	_value = value;
    }

    public static EnumAttributeLocation valueOf(int value) {
        return map.get(value);
    }
	
}
