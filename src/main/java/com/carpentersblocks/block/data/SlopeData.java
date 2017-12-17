package com.carpentersblocks.block.data;

import com.carpentersblocks.tileentity.CbTileEntity;

public class SlopeData extends RotatableData {
	
	private static final int TYPE_BITMASK = 0x7c0;
	private static final int TYPE_BITSHIFT = 6;
	private static final int TYPE_WEDGE = 0;
	private static final int TYPE_OBL_INT = 1;
	private static final int TYPE_OBL_EXT = 2;
	private static final int TYPE_PRISM = 3;
	private static final int TYPE_PRISM_1P = 4;
	private static final int TYPE_PRISM_2P = 5;
	private static final int TYPE_PRISM_3P = 6;
	private static final int TYPE_PRISM_4P = 7;
	private static final int TYPE_PRISM_WEDGE = 8;
	
	public enum Type {
		
		WEDGE,
		OBLIQUE_INTERIOR;
		
	}
	
	/**
	 * Gets slope type.
	 * 
	 * @param cbMetadata the metadata
	 * @return a slope type
	 */
	public static Type getType(int cbMetadata) {
		int temp = (cbMetadata & TYPE_BITMASK) >> TYPE_BITSHIFT;
		for (Type type : Type.values()) {
			if (type.ordinal() == temp) {
				return type;
			}
		}
		return Type.WEDGE;
	}
	
	/**
	 * Cycles to next slope type.
	 * 
	 * @param cbTileEntity the tile entity
	 */
	public static void setNextType(CbTileEntity cbTileEntity) {
		Type type = getType(cbTileEntity.getData());
		int newType = type.ordinal() + 1;
		if (newType > Type.values().length) {
			newType = 0;
		}
		int temp = cbTileEntity.getData();
		temp &= ~TYPE_BITMASK;
		temp |= newType << TYPE_BITSHIFT;
		cbTileEntity.setData(temp);
	}
	
}