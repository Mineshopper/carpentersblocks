package com.carpentersblocks.util.states.factory;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.registry.BlockRegistry;

public class StateFactory {
	
	public static AbstractState getState(CbTileEntity cbTileEntity) {
		if (BlockRegistry.blockCarpentersPressurePlate.equals(cbTileEntity.getBlockType())) {
			return new PressurePlateState(cbTileEntity);
		}
		return null;
	}
	
}