package carpentersblocks.data;

import net.minecraft.block.material.Material;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Lever
{
	
	/**
	 * 16-bit data components:
	 *
	 *	[00000000000]		[0]			[0]		[000]
	 *  Unused				Polarity	State	Type
	 */
	
	/*
	 * Polarity (inverts default state).
	 */
	public final static byte POLARITY_POSITIVE = 0;
	public final static byte POLARITY_NEGATIVE = 1;

	/*
	 * State (on/off).
	 */
	public final static byte STATE_OFF = 0;
	public final static byte STATE_ON = 1;
	
	/**
	 * Returns type (facing).
	 */
	public final static int getType(int data)
	{
		return data & 0x7;
	}
	
	/**
	 * Sets type (facing).
	 */
	public final static void setType(TECarpentersBlock TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff8;
		temp |= type;
		
		BlockProperties.setData(TE, temp);
	}
	
	/**
	 * Returns state.
	 */
	public final static int getState(int data)
	{
		int temp = data & 0x8;
		return temp >> 3;
	}
	
	/**
	 * Sets state.
	 */
	public final static void setState(TECarpentersBlock TE, int state, boolean playSound)
	{
		int temp = BlockProperties.getData(TE) & 0xfff7;
		temp |= state << 3;
		
		int data = BlockProperties.getData(TE);
		int polarity = getPolarity(data);
		
		if (
				!TE.worldObj.isRemote &&
				BlockProperties.getCoverBlock(TE, 6).blockMaterial != Material.cloth &&
				playSound &&
				getState(data) != state
			)
			TE.worldObj.playSoundEffect(TE.xCoord + 0.5D, TE.yCoord + 0.5D, TE.zCoord + 0.5D, "random.click", 0.3F, getState(data) == STATE_ON ? (polarity == POLARITY_POSITIVE ? 0.5F : 0.6F) : (polarity == POLARITY_NEGATIVE ? 0.5F : 0.6F));
		
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
	public final static void setPolarity(TECarpentersBlock TE, int polarity)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= polarity << 4;
		
		BlockProperties.setData(TE, temp);
	}
	
}
