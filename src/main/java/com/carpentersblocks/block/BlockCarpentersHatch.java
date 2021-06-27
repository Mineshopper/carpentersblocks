package com.carpentersblocks.block;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.states.StateConstants;
import com.carpentersblocks.util.states.StateMap;

public class BlockCarpentersHatch extends AbstractCoverableBlock implements IStateImplementor {

	private static StateMap _stateMap;
	
	public BlockCarpentersHatch(Properties properties, StateMap stateMap) {
		super(properties);
		_stateMap = stateMap;
	}

	@Override
	public String getStateDescriptor(CbTileEntity cbTileEntity) {
		return StateConstants.DEFAULT_STATE;
	}

	@Override
	public StateMap getStateMap() {
		return _stateMap;
	}

}
