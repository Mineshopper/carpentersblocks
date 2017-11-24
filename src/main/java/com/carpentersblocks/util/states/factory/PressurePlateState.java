package com.carpentersblocks.util.states.factory;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.block.PressurePlateUtil;
import com.carpentersblocks.util.states.StateUtil;

import net.minecraft.util.EnumFacing;

public class PressurePlateState extends AbstractState {
	
	public PressurePlateState(CbTileEntity cbTileEntity) {
		super(cbTileEntity);
	}

	@Override
	protected void rotate() {
		StateUtil util = new StateUtil();
		PressurePlateUtil ppUtil = new PressurePlateUtil(_cbTileEntity);
		switch (ppUtil.getFacing()) {
			case DOWN:
				util.rotate(this, EnumFacing.NORTH, RADIAN);
				break;
			case NORTH:
				util.rotate(this, EnumFacing.WEST, QUARTER_RADIAN);
				break;
			case SOUTH:
				util.rotate(this, EnumFacing.EAST, QUARTER_RADIAN);
				break;
			case WEST:
				util.rotate(this, EnumFacing.NORTH, QUARTER_RADIAN);
				break;
			case EAST:
				util.rotate(this, EnumFacing.SOUTH, QUARTER_RADIAN);
				break;
			default: {}
		}
	}

}
