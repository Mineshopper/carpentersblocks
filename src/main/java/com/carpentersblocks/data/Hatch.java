package com.carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.carpentersblocks.tileentity.TEBase;

public class Hatch {

    /**
     * 16-bit data components:
     *
     * [00000000]  [0]    [00]  [0]    [0]       [000]
     * Unused      Rigid  Dir   State  Position  Type
     */

    public final static byte TYPE_HIDDEN        = 0;
    public final static byte TYPE_WINDOW        = 1;
    public final static byte TYPE_SCREEN        = 2;
    public final static byte TYPE_FRENCH_WINDOW = 3;
    public final static byte TYPE_PANEL         = 4;

    public final static byte STATE_CLOSED = 0;
    public final static byte STATE_OPEN   = 1;

    public final static byte POSITION_LOW  = 0;
    public final static byte POSITION_HIGH = 1;

    public final static byte DIR_Z_NEG = 0;
    public final static byte DIR_Z_POS = 1;
    public final static byte DIR_X_NEG = 2;
    public final static byte DIR_X_POS = 3;

    public final static byte HINGED_NONRIGID = 0;
    public final static byte HINGED_RIGID    = 1;

    /**
     * Returns type.
     */
    public static int getType(TEBase TE)
    {
        return TE.getData() & 0x7;
    }

    /**
     * Sets type.
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = (TE.getData() & ~0x7) | type;
        TE.setData(temp);
    }

    /**
     * Returns position (high or low).
     */
    public static int getPos(TEBase TE)
    {
        return (TE.getData() & 0x8) >> 3;
    }

    /**
     * Sets position (high or low).
     */
    public static void setPos(TEBase TE, int position)
    {
        int temp = (TE.getData() & ~0x8) | (position << 3);
        TE.setData(temp);
    }

    /**
     * Returns state (open or closed).
     */
    public static int getState(TEBase TE)
    {
        return (TE.getData() & 0x10) >> 4;
    }

    /**
     * Sets state (open or closed).
     */
    public static void setState(TEBase TE, int state)
    {
        int temp = (TE.getData() & ~0x10) | (state << 4);
        World world = TE.getWorldObj();

        if (!world.isRemote) {
            world.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);
        }

        TE.setData(temp);
    }

    /**
     * Returns direction.
     */
    public static int getDir(TEBase TE)
    {
        return (TE.getData() & 0x60) >> 5;
    }

    /**
     * Sets direction.
     */
    public static void setDir(TEBase TE, int dir)
    {
        int temp = (TE.getData() & ~0x60) | (dir << 5);
        TE.setData(temp);
    }

    /**
     * Returns hatch rigidity (requires redstone for activation).
     */
    public static int getRigidity(TEBase TE)
    {
        return (TE.getData() & 0x80) >> 7;
    }

    /**
     * Sets hatch rigidity (requires redstone for activation).
     */
    public static void setRigidity(TEBase TE, int rigid)
    {
        int temp = (TE.getData() & ~0x80) | (rigid << 7);
        TE.setData(temp);
    }

}
