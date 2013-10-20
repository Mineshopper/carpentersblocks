package carpentersblocks.data;

import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Barrier
{

	/**
	 * 16-bit data components:
	 *
	 *	[00000000000]		[0]		[0000]
	 *  Unused				Post	Type
	 */

	/*
	 * Type definitions
	 */
	public final static byte TYPE_VANILLA = 0;
	public final static byte TYPE_VANILLA_X1 = 1;
	public final static byte TYPE_VANILLA_X2 = 2;
	public final static byte TYPE_VANILLA_X3 = 3;
	public final static byte TYPE_PICKET = 4;
	public final static byte TYPE_PLANK_VERTICAL = 5;
	public final static byte TYPE_WALL = 6;

	/*
	 * Barrier post
	 */
	public final static byte NO_POST = 0;
	public final static byte HAS_POST = 1;

	/**
	 * Returns data.
	 */
	public final static int getType(int data)
	{
		return data & 0xf;
	}

	/**
	 * Sets data (vanilla, picket, etc).
	 */
	public final static void setType(TECarpentersBlock TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff0;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns post bit from data.
	 */
	public final static int getPost(int data)
	{
		return data >> 4;
	}

	/**
	 * Sets post bit.
	 */
	public final static void setPost(TECarpentersBlock TE, int post)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= post << 4;

		BlockProperties.setData(TE, temp);
	}

}
