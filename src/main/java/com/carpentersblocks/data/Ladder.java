package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class Ladder implements ISided {

    /**
     * 16-bit data components:
     *
     * [0000000000000] [000]
     * Type            Dir
     */

    public static final byte DIR_ON_X  = 0;
    public static final byte DIR_ON_Z  = 1;

    public static final byte TYPE_DEFAULT = 0;
    public static final byte TYPE_RAIL    = 1;
    public static final byte TYPE_POLE    = 2;

    @Override
    public void setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = (BlockProperties.getMetadata(TE) & 0xfff8) | dir.ordinal();
        BlockProperties.setMetadata(TE, temp);
    }

    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        return ForgeDirection.getOrientation(BlockProperties.getMetadata(TE) & 0x7);
    }

    /**
     * Returns true if ladder is not connected to side of a block.
     */
    public boolean isFreestanding(TEBase TE)
    {
        return getDirection(TE).ordinal() < 2;
    }

    public static int getType(TEBase TE)
    {
        return (BlockProperties.getMetadata(TE) & 0xfff8) >>> 3;
    }

    public void setType(TEBase TE, int type)
    {
        int temp = (BlockProperties.getMetadata(TE) & 0x7) | (type << 3);
        BlockProperties.setMetadata(TE, temp);
    }

}
