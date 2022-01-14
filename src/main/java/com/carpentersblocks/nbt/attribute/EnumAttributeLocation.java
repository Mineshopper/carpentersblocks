package com.carpentersblocks.nbt.attribute;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

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
    
    public boolean isHost() {
    	return HOST.equals(this);
    }
    
    public boolean isSide() {
    	return !isHost();
    }
    
    public boolean isCardinalSide() {
    	return this.ordinal() > 1 && this.ordinal() < 6;
    }
    
    public BlockPos offset(BlockPos blockPos) {
    	if (HOST.equals(this)) {
    		return blockPos;
    	} else {
    		return blockPos.offset(
    				Direction.from2DDataValue(this.ordinal()).getStepX(),
    				Direction.from2DDataValue(this.ordinal()).getStepY(),
    				Direction.from2DDataValue(this.ordinal()).getStepZ());
    	}
    }
	
}
