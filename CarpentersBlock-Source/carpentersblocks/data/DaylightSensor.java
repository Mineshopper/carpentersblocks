package carpentersblocks.data;

import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class DaylightSensor
{

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
	public final static int getType(int data)
	{
		return data & 0xf;
	}

	/**
	 * Sets type (light value).
	 */
	public final static void setType(TECarpentersBlock TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff0;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns polarity.
	 */
	public final static int getPolarity(int data)
	{
		int temp = data & 0x10;
		return temp >> 4;
	}

	/**
	 * Sets polarity.
	 */
	public final static void setPolarity(TECarpentersBlock TE, int state)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= state << 4;

		BlockProperties.setData(TE, temp);
	}

}
