package carpentersblocks.data;

import net.minecraft.block.BlockBed;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;

public class Bed
{

	/**
	 * 16-bit data components:
	 *
	 *	[000]	[00000000]	[0]			[0000]
	 *  Unused	Design		Occupied	Type
	 */

	/*
	 * Type definitions.
	 */
	public final static byte TYPE_NORMAL = 0;

	/**
	 * Returns type.
	 */
	public final static int getType(int data)
	{
		return data & 0xf;
	}

	/**
	 * Sets type.
	 */
	public final static void setType(TECarpentersBlock TE, int type)
	{
		int temp = BlockProperties.getData(TE) & 0xfff0;
		temp |= type;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns design.
	 */
	public final static int getDesign(int data)
	{
		int temp = data & 0x1fe0;
		return temp >> 5;
	}

	/**
	 * Sets design.
	 */
	public final static void setDesign(TECarpentersBlock TE, int design)
	{
		int temp = BlockProperties.getData(TE) & 0xe01f;
		temp |= design << 5;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns whether bed is occupied.
	 */
	public final static boolean isOccupied(TECarpentersBlock TE)
	{
		int temp = BlockProperties.getData(TE) & 0x10;

		return temp != 0;
	}

	/**
	 * Sets occupation.
	 */
	public final static void setOccupied(TECarpentersBlock TE, boolean isOccupied)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;

		if (isOccupied)
			temp |= (1 << 4);

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Converts player facing to ForgeDirection.
	 */
	public final static ForgeDirection getDirection(int facing)
	{
		switch (facing)
		{
		case 0:
			return ForgeDirection.NORTH;
		case 1:
			return ForgeDirection.EAST;
		case 2:
			return ForgeDirection.SOUTH;
		default:
			return ForgeDirection.WEST;
		}
	}

	/**
	 * Returns TE for opposite piece.
	 * Will return null if opposite piece doesn't exist (when creating or destroying block, for instance).
	 */
	public final static TECarpentersBlock getOppositeTE(IBlockAccess blockAccess, int x, int y, int z)
	{
		int metadata = blockAccess.getBlockMetadata(x, y, z);

		ForgeDirection dir = Bed.getDirection(metadata & 3);

		if (BlockBed.isBlockHeadOfBed(metadata)) {
			x += dir.offsetX;
			z += dir.offsetZ;
		} else {
			x -= dir.offsetX;
			z -= dir.offsetZ;
		}

		if (blockAccess.getBlockId(x, y, z) == BlockHandler.blockCarpentersBedID)
			return (TECarpentersBlock) blockAccess.getBlockTileEntity(x, y, z);
		else
			return null;
	}

}
