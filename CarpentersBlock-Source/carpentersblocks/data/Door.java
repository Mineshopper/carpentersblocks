package carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Door
{

	/**
	 * 16-bit data components:
	 *
	 *	[0000000]	[0]		[0]		[0]		[00]	 [0]	[000]
	 *  Unused		Rigid	Piece	State	Facing	 Hinge	Type
	 */

	/*
	 * Type definitions for door.
	 */
	public final static byte TYPE_GLASS_TOP = 0;
	public final static byte TYPE_GLASS_TALL = 1;
	public final static byte TYPE_PANELS = 2;
	public final static byte TYPE_SCREEN_TALL = 3;
	public final static byte TYPE_FRENCH_GLASS = 4;
	public final static byte TYPE_HIDDEN = 5;

	/*
	 * Facing of door.
	 */
	public final static byte FACING_XP = 0;
	public final static byte FACING_ZP = 1;
	public final static byte FACING_XN = 2;
	public final static byte FACING_ZN = 3;

	/*
	 * Hinge side.
	 */
	public final static byte HINGE_LEFT = 0;
	public final static byte HINGE_RIGHT= 1;

	/*
	 * Open/closed state.
	 */
	public final static byte STATE_CLOSED = 0;
	public final static byte STATE_OPEN = 1;

	/*
	 * Door piece.
	 */
	public final static byte PIECE_BOTTOM = 0;
	public final static byte PIECE_TOP = 1;

	/*
	 * Door rigidity.
	 */
	public final static byte HINGED_NONRIGID = 0;
	public final static byte HINGED_RIGID = 1;

	/**
	 * Returns type.
	 */
	public final static int getType(int data)
	{
		return data & 0x7;
	}

	/**
	 * Sets type.
	 */
	public final static void setType(TECarpentersBlock TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff8;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns hinge side (relative to facing).
	 */
	public final static int getHinge(int data)
	{
		int temp = data & 0x8;
		return temp >> 3;
	}

	/**
	 * Sets hinge side (relative to facing).
	 */
	public final static void setHingeSide(TECarpentersBlock TE, int hingeSide)
	{
		int temp = BlockProperties.getData(TE) & 0xfff7;
		temp |= hingeSide << 3;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns facing (faces opening direction).
	 */
	public final static int getFacing(int data)
	{
		int temp = data & 0x30;
		return temp >> 4;
	}

	/**
	 * Sets facing (faces opening direction).
	 */
	public final static void setFacing(TECarpentersBlock TE, int facing)
	{
		int temp = BlockProperties.getData(TE) & 0xffcf;
		temp |= facing << 4;

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
	public final static void setState(TECarpentersBlock TE, int state, boolean playSound)
	{
		int temp = BlockProperties.getData(TE) & 0xffbf;
		temp |= state << 6;

		if (!TE.worldObj.isRemote && playSound)
			TE.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns door piece (top or bottom).
	 */
	public final static int getPiece(int data)
	{
		int temp = data & 0x80;
		return temp >> 7;
	}

	/**
	 * Sets door piece (top or bottom).
	 */
	public final static void setPiece(TECarpentersBlock TE, int piece)
	{
		int temp = BlockProperties.getData(TE) & 0xff7f;
		temp |= piece << 7;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns door rigidity (requires redstone for activation).
	 */
	public final static int getRigidity(int data)
	{
		int temp = data & 0x100;
		return temp >> 8;
	}

	/**
	 * Sets door rigidity (requires redstone for activation).
	 */
	public final static void setRigidity(TECarpentersBlock TE, int rigid)
	{
		int temp = BlockProperties.getData(TE) & 0xfeff;
		temp |= rigid << 8;

		BlockProperties.setData(TE, temp);
	}

}
