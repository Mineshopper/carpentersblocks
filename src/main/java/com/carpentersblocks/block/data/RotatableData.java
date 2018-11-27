package com.carpentersblocks.block.data;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.RotationUtil.Rotation;

import net.minecraft.util.EnumFacing.Axis;

public abstract class RotatableData {
	
	private static final int ROT_BITMASK = 0x3f;
	
	public static void rotate(CbTileEntity cbTileEntity, Axis axis) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		Rotation rotation = Rotation.get(cbMetadata).getNext(axis);
		setRotation(cbTileEntity, rotation);
	}
	
	public static void resetRotation(CbTileEntity cbTileEntity) {
		int data = Rotation.X0_Y0_Z0.asInt();
		cbTileEntity.setCbMetadata(data);
	}
	
	public static Rotation getRotation(CbTileEntity cbTileEntity) {
		return Rotation.get(cbTileEntity.getCbMetadata());
	}
	
	public static void setRotation(CbTileEntity cbTileEntity, Rotation rotation) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		cbMetadata &= ~ROT_BITMASK;
		cbMetadata |= rotation.asInt();
        cbTileEntity.setCbMetadata(cbMetadata);
	}
	
}
