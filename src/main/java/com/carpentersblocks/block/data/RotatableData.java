package com.carpentersblocks.block.data;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.util.Direction.Axis;

public abstract class RotatableData {
	
	private static final int ROT_BITMASK = 0x3f;
	
	public static void rotate(CbTileEntity cbTileEntity, Axis axis) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		CbRotation rotation = CbRotation.get(cbMetadata).getNext(axis);
		setRotation(cbTileEntity, rotation);
	}
	
	public static void resetRotation(CbTileEntity cbTileEntity) {
		int data = CbRotation.X0_Y0_Z0.asInt();
		cbTileEntity.setCbMetadata(data);
	}
	
	public static CbRotation getRotation(CbTileEntity cbTileEntity) {
		return CbRotation.get(cbTileEntity.getCbMetadata());
	}
	
	public static void setRotation(CbTileEntity cbTileEntity, CbRotation rotation) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		cbMetadata &= ~ROT_BITMASK;
		cbMetadata |= rotation.asInt();
        cbTileEntity.setCbMetadata(cbMetadata);
	}
	
}
