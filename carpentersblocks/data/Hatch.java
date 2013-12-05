package carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Hatch
{

	/**
	 * 16-bit data components:
	 *
	 *	[00000000]	[0]		[00]	[0]		[0]			[000]
	 *  Unused		Rigid	Dir		State	Position	Type
	 */

	/*
	 * Type definitions.
	 */
	public final static byte TYPE_HIDDEN = 0;
	public final static byte TYPE_WINDOW = 1;
	public final static byte TYPE_SCREEN = 2;
	public final static byte TYPE_FRENCH_WINDOW = 3;
	public final static byte TYPE_PANEL = 4;

	/*
	 * Open/closed state.
	 */
	public final static byte STATE_CLOSED = 0;
	public final static byte STATE_OPEN = 1;

	/*
	 * High or low position.
	 */
	public final static byte POSITION_LOW = 0;
	public final static byte POSITION_HIGH = 1;

	/*
	 * Opening direction.
	 */
	public final static byte DIR_Z_NEG = 0;
	public final static byte DIR_Z_POS = 1;
	public final static byte DIR_X_NEG = 2;
	public final static byte DIR_X_POS = 3;

	/*
	 * Hatch rigidity.
	 */
	public final static byte HINGED_NONRIGID = 0;
	public final static byte HINGED_RIGID = 1;

	/**
	 * Returns type.
	 */
	public final static int getType(TEBase TE)
	{
		return BlockProperties.getData(TE) & 0x7;
	}

	/**
	 * Sets type.
	 */
	public final static void setType(TEBase TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff8;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns position (high or low).
	 */
	public final static int getPos(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x8;
		return temp >> 3;
	}

	/**
	 * Sets position (high or low).
	 */
	public final static void setPos(TEBase TE, int position)
	{
		int temp = BlockProperties.getData(TE) & 0xfff7;
		temp |= position << 3;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns state (open or closed).
	 */
	public final static int getState(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x10;
		return temp >> 4;
	}

	/**
	 * Sets state (open or closed).
	 */
	public final static void setState(TEBase TE, int state)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= state << 4;

		BlockProperties.setData(TE, temp);

		if (!TE.worldObj.isRemote)
			TE.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);
	}

	/**
	 * Returns direction.
	 */
	public final static int getDir(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x60;
		return temp >> 5;
	}

	/**
	 * Sets direction.
	 */
	public final static void setDir(TEBase TE, int dir)
	{
		int temp = BlockProperties.getData(TE) & 0xff9f;
		temp |= dir << 5;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns hatch rigidity (requires redstone for activation).
	 */
	public final static int getRigidity(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x80;
		return temp >> 7;
	}

	/**
	 * Sets hatch rigidity (requires redstone for activation).
	 */
	public final static void setRigidity(TEBase TE, int rigid)
	{
		int temp = BlockProperties.getData(TE) & 0xff7f;
		temp |= rigid << 7;

		BlockProperties.setData(TE, temp);
	}

}
