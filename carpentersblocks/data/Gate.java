package carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Gate
{

	/**
	 * 16-bit data components:
	 *
	 *	[000000000]		[0]		[0]		[0]			[0000]
	 *  Unused			State	Facing	OpenDir		Type
	 */

	/*
	 * Type definitions for gates (they match barrier types).
	 */
	public final static byte TYPE_VANILLA = Barrier.TYPE_VANILLA;
	public final static byte TYPE_VANILLA_X1 = Barrier.TYPE_VANILLA_X1;
	public final static byte TYPE_VANILLA_X2 = Barrier.TYPE_VANILLA_X2;
	public final static byte TYPE_VANILLA_X3 = Barrier.TYPE_VANILLA_X3;
	public final static byte TYPE_PICKET = Barrier.TYPE_PICKET;
	public final static byte TYPE_PLANK_VERTICAL = Barrier.TYPE_PLANK_VERTICAL;
	public final static byte TYPE_WALL = Barrier.TYPE_WALL;

	/*
	 * Facing of gate.
	 */
	public final static byte FACING_ON_X = 0;
	public final static byte FACING_ON_Z = 1;

	/*
	 * Open/closed state.
	 */
	public final static byte STATE_CLOSED = 0;
	public final static byte STATE_OPEN = 1;

	/*
	 * Opening direction on axis opposite of facing.
	 */
	public final static byte DIR_POS = 0;
	public final static byte DIR_NEG = 1;

	/**
	 * Returns type (vanilla, picket, etc).
	 */
	public final static int getType(int data)
	{
		return data & 0xf;
	}

	/**
	 * Sets type (vanilla, picket, etc).
	 */
	public final static void setType(TEBase TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff0;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns facing (path on x, or path on z).
	 */
	public final static int getFacing(int data)
	{
		int temp = data & 0x20;
		return temp >> 5;
	}

	/**
	 * Sets facing (path on x, or path on z).
	 */
	public final static void setFacing(TEBase TE, int facing)
	{
		int temp = BlockProperties.getData(TE) & 0xffdf;
		temp |= facing << 5;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns open/closed state.
	 */
	public final static int getState(int data)
	{
		int temp = data & 0x40;
		return temp >> 6;
	}

	/**
	 * Sets state (open or closed).
	 */
	public final static void setState(TEBase TE, int state, boolean playSound)
	{
		int temp = BlockProperties.getData(TE) & 0xffbf;
		temp |= state << 6;

		if (!TE.worldObj.isRemote && playSound)
			TE.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns opening direction (positive or negative on axis opposite of facing).
	 */
	public final static int getDirOpen(int data)
	{
		int temp = data & 0x10;
		return temp >> 4;
	}

	/**
	 * Sets opening direction (positive or negative on axis opposite of facing).
	 */
	public final static void setDirOpen(TEBase TE, int dirOpen)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= dirOpen << 4;

		BlockProperties.setData(TE, temp);
	}

}
