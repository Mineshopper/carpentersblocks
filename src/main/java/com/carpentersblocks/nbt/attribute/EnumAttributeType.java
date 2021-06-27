package com.carpentersblocks.nbt.attribute;

import java.util.HashMap;
import java.util.Map;

public enum EnumAttributeType {
	
	COVER(0),
	DYE(1),
	OVERLAY(2),
	DESIGN_BASIC(3),
	DESIGN_CHISEL(4),
	ILLUMINATOR(5),
	PLANT(6),
	SOIL(7),
	FERTILIZER(8),
	SAFE_UPGRADE(9),
	SNOW_PILE(10);
	
	private int _value;
    private static Map<Integer, EnumAttributeType> map = new HashMap<Integer, EnumAttributeType>();

    static {
        for (EnumAttributeType enumValue : EnumAttributeType.values()) {
            map.put(enumValue._value, enumValue);
        }
    }

    private EnumAttributeType(final int value) {
    	_value = value;
    }

    public static EnumAttributeType valueOf(int value) {
        return map.get(value);
    }
	
}