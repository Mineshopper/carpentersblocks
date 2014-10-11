package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;

public class Ladder implements ISided {

    /**
     * 16-bit data components:
     *
     * [000000000] [0000] [000]
     * Unused      Type   Dir
     */

    public static final byte DIR_ON_X  = 0;
    public static final byte DIR_ON_Z  = 1;

    public static final byte TYPE_DEFAULT = 0;
    public static final byte TYPE_RAIL    = 1;
    public static final byte TYPE_POLE    = 2;

    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        return ForgeDirection.getOrientation(TE.getData() & 0x7);
    }

    @Override
    public void setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = (TE.getData() & ~0x7) | dir.ordinal();
        TE.setData(temp);
    }

    /**
     * Returns true if ladder is not connected to side of a block.
     */
    public boolean isFreestanding(TEBase TE)
    {
        return getDirection(TE).ordinal() < 2;
    }

    public int getType(TEBase TE)
    {
        return (TE.getData() & 0x78) >>> 3;
    }

    public void setType(TEBase TE, int type)
    {
        int temp = (TE.getData() & ~0x78) | (type << 3);
        TE.setData(temp);
    }

}
