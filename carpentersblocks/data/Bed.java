package carpentersblocks.data;

import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class Bed
{
	
	/**
	 * 16-bit data components:
	 *
	 *	[0]		[00]		[00000000]	[0]			[0000]
	 *  isHead	Direction	Design		isOccupied	Type
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
	public final static void setType(TEBase TE, int type)
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
	public final static void setDesign(TEBase TE, int design)
	{
		int temp = BlockProperties.getData(TE) & 0xe01f;
		temp |= design << 5;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns whether bed is occupied.
	 */
	public final static boolean isOccupied(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x10;

		return temp != 0;
	}

	/**
	 * Sets occupation.
	 */
	public final static void setOccupied(TEBase TE, boolean isOccupied)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;

		if (isOccupied)
			temp |= (1 << 4);

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns TE for opposite piece.
	 * Will return null if opposite piece doesn't exist (when creating or destroying block, for instance).
	 */
	public final static TEBase getOppositeTE(final TEBase TE)
	{
		ForgeDirection dir = getDirection(TE);
		int x = TE.xCoord;
		int z = TE.zCoord;

		if (isHeadOfBed(TE)) {
			x = TE.xCoord + dir.offsetX;
			z = TE.zCoord + dir.offsetZ;
		} else {
			x = TE.xCoord - dir.offsetX;
			z = TE.zCoord - dir.offsetZ;
		}

		if (TE.worldObj.getBlockId(x, TE.yCoord, z) == BlockRegistry.blockCarpentersBedID)
			return (TEBase) TE.worldObj.getBlockTileEntity(x, TE.yCoord, z);
		else
			return null;
	}
	
    /**
     * Returns whether block is head of bed.
     */
    public final static boolean isHeadOfBed(TEBase TE)
    {
		int temp = BlockProperties.getData(TE) & 0x8000;

		return temp != 0;
    }
    
    /**
     * Sets block as head of bed.
     */
    public final static void setHeadOfBed(TEBase TE)
    {
    	int temp = BlockProperties.getData(TE) & 0x7fff;
    	temp |= 1 << 15;
    	
		BlockProperties.setData(TE, temp);
    }
    
    /**
     * Returns direction of bed piece.
     */
    public final static ForgeDirection getDirection(TEBase TE)
    {
    	int facing = BlockProperties.getData(TE) & 0x6000;
    	
    	return BlockProperties.getDirectionFromFacing(facing >> 13);
    }
    
    /**
     * Sets direction of bed piece.
     * Stored as player facing from 0 to 3.
     */
    public final static void setDirection(TEBase TE, int facing)
    {
		int temp = BlockProperties.getData(TE) & 0x9fff;
		temp |= facing << 13;

		BlockProperties.setData(TE, temp);
    }

}
