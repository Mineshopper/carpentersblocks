package carpentersblocks.data;

import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Torch
{
	
	/**
	 * 16-bit data components:
	 *
	 *	[0000000000]	[0]		[00]	[000]
	 *  Unused			Ready	State	Facing
	 */

	public enum State {
		LIT,
		SMOLDERING,
		UNLIT
	}

	/**
	 * Returns facing.
	 */
	public final static ForgeDirection getFacing(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		
		return ForgeDirection.getOrientation(data & 0x7);
	}

	/**
	 * Sets facing.
	 */
	public final static void setFacing(TECarpentersBlock TE, int side)
	{
		int temp = BlockProperties.getData(TE) & 0xfff8;
		temp |= side;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns state.
	 */
	public final static State getState(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		
		int temp = data & 0x18;
		int val = temp >> 3;
		
		return val == State.LIT.ordinal() ? State.LIT : val == State.SMOLDERING.ordinal() ? State.SMOLDERING : State.UNLIT;
	}

	/**
	 * Sets state.
	 */
	public final static void setState(TECarpentersBlock TE, State state)
	{
		int temp = BlockProperties.getData(TE) & 0xffe7;
		temp |= state.ordinal() << 3;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns whether block is capable of handling logic functions.
	 * This is implemented because for buttons and levers the SERVER
	 * lags behind the client and will cause the block to pop of walls
	 * before it has a chance to set the correct facing.
	 */
	public final static boolean isReady(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);

		return (data & 0x20) > 0;
	}

	/**
	 * Sets block as ready.
	 */
	public final static void setReady(TECarpentersBlock TE)
	{
		int temp = BlockProperties.getData(TE) & 0xffdf;
		temp |= 1 << 5;

		BlockProperties.setData(TE, temp);
	}

}
