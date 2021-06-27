package com.carpentersblocks.util.states.factory;

import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.nbt.CbTileEntity;

public class StateFactory {
	
	public static AbstractState getState(CbTileEntity cbTileEntity) {
		if (CbBlocks.blockPressurePlate.equals(cbTileEntity.getBlockState().getBlock())) {
			return new PressurePlateState(cbTileEntity);
		} else if (CbBlocks.blockFlowerPot.equals(cbTileEntity.getBlockState().getBlock())) {
			return new FlowerPotState(cbTileEntity);
		}
		return null;
	}
	
}