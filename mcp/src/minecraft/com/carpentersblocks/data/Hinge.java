package com.carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class Hinge {

    /**
     * 16-bit data components:
     *
     * [0000000]  [0]    [0]    [0]    [00]    [0]    [000]
     * Unused     Rigid  Piece  State  Facing  Hinge  Type
     */

    public final static byte TYPE_GLASS_TOP    = 0;
    public final static byte TYPE_GLASS_TALL   = 1;
    public final static byte TYPE_PANELS       = 2;
    public final static byte TYPE_SCREEN_TALL  = 3;
    public final static byte TYPE_FRENCH_GLASS = 4;
    public final static byte TYPE_HIDDEN       = 5;
    public final static byte TYPE_SHUTTER      = 6;

    public final static byte FACING_XP = 0;
    public final static byte FACING_ZP = 1;
    public final static byte FACING_XN = 2;
    public final static byte FACING_ZN = 3;

    public final static byte HINGE_LEFT  = 0;
    public final static byte HINGE_RIGHT = 1;

    public final static byte STATE_CLOSED = 0;
    public final static byte STATE_OPEN   = 1;

    public final static byte PIECE_BOTTOM = 0;
    public final static byte PIECE_TOP    = 1;

    public final static byte HINGED_NONRIGID = 0;
    public final static byte HINGED_RIGID    = 1;

    /**
     * Returns type.
     */
    public static int getType(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0x7;
    }

    /**
     * Sets type.
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff8;
        temp |= type;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns hinge side (relative to facing).
     */
    public static int getHinge(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x8;
        return temp >> 3;
    }

    /**
     * Sets hinge side (relative to facing).
     */
    public static void setHingeSide(TEBase TE, int hingeSide)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff7;
        temp |= hingeSide << 3;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns facing (faces opening direction).
     */
    public static int getFacing(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x30;
        return temp >> 4;
    }

    /**
     * Sets facing (faces opening direction).
     */
    public static void setFacing(TEBase TE, int facing)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffcf;
        temp |= facing << 4;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns open/closed state.
     */
    public static int getState(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x40;
        return temp >> 6;
    }

    /**
     * Sets state (open or closed).
     */
    public static void setState(TEBase TE, int state, boolean playSound)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffbf;
        temp |= state << 6;

        World world = TE.getWorldObj();

        if (!world.isRemote && playSound) {
            world.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);
        }

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns door piece (top or bottom).
     */
    public static int getPiece(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x80;
        return temp >> 7;
    }

    /**
     * Sets door piece (top or bottom).
     */
    public static void setPiece(TEBase TE, int piece)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xff7f;
        temp |= piece << 7;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns door rigidity (requires redstone for activation).
     */
    public static int getRigidity(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x100;
        return temp >> 8;
    }

    /**
     * Sets door rigidity (requires redstone for activation).
     */
    public static void setRigidity(TEBase TE, int rigid)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfeff;
        temp |= rigid << 8;

        BlockProperties.setMetadata(TE, temp);
    }

}
