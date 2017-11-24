package com.carpentersblocks.block;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.states.StateMap;

public interface IStateImplementor {

	public String getStateDescriptor(CbTileEntity cbTileEntity);
	
	public StateMap getStateMap();
	
}
