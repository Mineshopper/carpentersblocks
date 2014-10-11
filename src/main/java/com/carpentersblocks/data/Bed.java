package com.carpentersblocks.data;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.registry.BlockRegistry;

public class Bed {

    /**
     * 16-bit data components:
     *
     * [0]     [00]       [00000000]  [0]         [0000]
     * isHead  Direction  Unused      isOccupied  Type
     */

    public final static byte TYPE_NORMAL   = 0;

    /**
     * Returns type.
     */
    public final static int getType(TEBase TE)
    {
        return TE.getData() & 0xf;
    }

    /**
     * Sets type.
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = TE.getData() & 0xfff0;
        temp |= type;

        TE.setData(temp);
    }

    /**
     * Returns whether bed is occupied.
     */
    public static boolean isOccupied(TEBase TE)
    {
        int temp = TE.getData() & 0x10;

        return temp != 0;
    }

    /**
     * Sets occupation.
     */
    public static void setOccupied(TEBase TE, boolean isOccupied)
    {
        int temp = TE.getData() & 0xffef;

        if (isOccupied) {
            temp |= 1 << 4;
        }

        TE.setData(temp);
    }

    /**
     * Returns TE for opposite piece.
     * Will return null if opposite piece doesn't exist (when creating or destroying block, for instance).
     */
    public static TEBase getOppositeTE(TEBase TE)
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

        World world = TE.getWorldObj();

        if (world.getBlock(x, TE.yCoord, z).equals(BlockRegistry.blockCarpentersBed)) {
            return (TEBase) world.getTileEntity(x, TE.yCoord, z);
        } else {
            return null;
        }
    }

    /**
     * Returns whether block is head of bed.
     */
    public static boolean isHeadOfBed(TEBase TE)
    {
        int temp = TE.getData() & 0x8000;

        return temp != 0;
    }

    /**
     * Sets block as head of bed.
     */
    public static void setHeadOfBed(TEBase TE)
    {
        int temp = TE.getData() & 0x7fff;
        temp |= 1 << 15;

        TE.setData(temp);
    }

    /**
     * Returns direction of bed piece.
     */
    public static ForgeDirection getDirection(TEBase TE)
    {
        int rot = (TE.getData() & 0x6000) >> 13;

        return EntityLivingUtil.getRotationFacing(rot).getOpposite();
    }

    /**
     * Sets direction of bed piece.
     * Stored as player facing from 0 to 3.
     */
    public static void setDirection(TEBase TE, int facing)
    {
        int temp = TE.getData() & 0x9fff;
        temp |= facing << 13;

        TE.setData(temp);
    }

}
