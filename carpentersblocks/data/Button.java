package carpentersblocks.data;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Button
{

	/**
	 * 16-bit data components:
	 *
	 *	[000000000000]	[0]			[0]		[00]
	 *  Unused			Polarity	State	Facing
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
	 * Returns facing.
	 */
	public final static ForgeDirection getFacing(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		
		return ForgeDirection.getOrientation((data & 0x3) + 2);
	}

	/**
	 * Sets facing.
	 */
	public final static void setFacing(TECarpentersBlock TE, int side)
	{
		int temp = BlockProperties.getData(TE) & 0xfffc;
		temp |= side;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns state.
	 */
	public final static int getState(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		
		int temp = data & 0x4;
		return temp >> 2;
	}

	/**
	 * Sets state.
	 */
	public final static void setState(TECarpentersBlock TE, int state, boolean playSound)
	{
		int temp = BlockProperties.getData(TE) & 0xfffb;
		temp |= state << 2;

		if (
				!TE.worldObj.isRemote &&
				BlockProperties.getCoverBlock(TE, 6).blockMaterial != Material.cloth &&
				playSound &&
				getState(TE) != state
			) {
			TE.worldObj.playSoundEffect(TE.xCoord + 0.5D, TE.yCoord + 0.5D, TE.zCoord + 0.5D, "random.click", 0.3F, getState(TE) == STATE_ON ? 0.5F : 0.6F);
		}
		
		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns polarity.
	 */
	public final static int getPolarity(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		
		int temp = data & 0x8;
		return temp >> 3;
	}

	/**
	 * Sets polarity.
	 */
	public final static void setPolarity(TECarpentersBlock TE, int polarity)
	{
		int temp = BlockProperties.getData(TE) & 0xfff7;
		temp |= polarity << 3;

		BlockProperties.setData(TE, temp);
	}

}
