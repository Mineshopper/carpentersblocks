package carpentersblocks.data;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class DaylightSensor {

	/**
	 * 16-bit data components:
	 *
	 *	[00000000000]		[0]			[0000]
	 *  Unused				Polarity	LightValue
	 */

	/*
	 * Polarity.
	 */
	public final static byte POLARITY_POSITIVE = 0;
	public final static byte POLARITY_NEGATIVE = 1;

	/**
	 * Returns type (light value).
	 */
	public static int getType(TEBase TE)
	{
		return BlockProperties.getData(TE) & 0xf;
	}

	/**
	 * Sets type (light value).
	 */
	public static void setType(TEBase TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff0;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns polarity.
	 */
	public static int getPolarity(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x10;
		return temp >> 4;
	}

	/**
	 * Sets polarity.
	 */
	public static void setPolarity(TEBase TE, int state)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= state << 4;

		BlockProperties.setData(TE, temp);
	}

}
