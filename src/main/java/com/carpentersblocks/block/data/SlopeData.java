package com.carpentersblocks.block.data;

import com.carpentersblocks.tileentity.CbTileEntity;

public class SlopeData extends RotatableData {
	
	private static final int TYPE_BITMASK = 0x7c0; // 32 types supported right now
	private static final int TYPE_BITSHIFT = 6;
	
	public enum Type {
		WEDGE,
		WEDGE_INTERIOR,
		WEDGE_EXTERIOR,
		OBLIQUE_INTERIOR,
		OBLIQUE_EXTERIOR,
		PRISM_WEDGE,
		PRISM,
		PRISM_1P,
		PRISM_2P,
		PRISM_3P,
		PRISM_4P,
		INVERT_PRISM,
		INVERT_PRISM_1P,
		INVERT_PRISM_2P,
		INVERT_PRISM_3P,
		INVERT_PRISM_4P;
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
		return Type.values()[0];
	}
	
	/**
	 * Cycles to next slope type.
	 * 
	 * @param cbTileEntity the tile entity
	 */
	public static void setNextType(CbTileEntity cbTileEntity) {
		Type type = getType(cbTileEntity.getCbMetadata());
		int newType = type.ordinal() + 1;
		if (newType > Type.values().length) {
			newType = 0;
		}
		int temp = cbTileEntity.getCbMetadata();
		temp &= ~TYPE_BITMASK;
		temp |= newType << TYPE_BITSHIFT;
		cbTileEntity.setCbMetadata(temp);
	}
	
}