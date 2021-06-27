package com.carpentersblocks.util.states.factory;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.carpentersblocks.util.metadata.PressurePlateMetadata;

public class PressurePlateState extends AbstractState {
	
	public PressurePlateState(CbTileEntity cbTileEntity) {
		super(cbTileEntity);
	}

	@Override
	protected void rotate() {
		switch (new PressurePlateMetadata(_cbTileEntity).getFacing()) {
			case DOWN:
				this.rotate(CbRotation.X0_Y0_Z180);
				break;
			case NORTH:
				this.rotate(CbRotation.X270_Y0_Z0);
				break;
			case SOUTH:
				this.rotate(CbRotation.X90_Y0_Z0);
				break;
			case WEST:
				this.rotate(CbRotation.X0_Y0_Z270);
				break;
			case EAST:
				this.rotate(CbRotation.X0_Y0_Z90);
				break;
			default: {}
		}
	}

}
