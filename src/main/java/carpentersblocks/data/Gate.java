package carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Gate {

    /**
     * 16-bit data components:
     *
     * [000000000]  [0]    [0]     [0]      [0000]
     * Unused       State  Facing  OpenDir  Type
     */

    public final static byte TYPE_VANILLA        = Barrier.TYPE_VANILLA;
    public final static byte TYPE_VANILLA_X1     = Barrier.TYPE_VANILLA_X1;
    public final static byte TYPE_VANILLA_X2     = Barrier.TYPE_VANILLA_X2;
    public final static byte TYPE_VANILLA_X3     = Barrier.TYPE_VANILLA_X3;
    public final static byte TYPE_PICKET         = Barrier.TYPE_PICKET;
    public final static byte TYPE_SHADOWBOX = Barrier.TYPE_SHADOWBOX;
    public final static byte TYPE_WALL           = Barrier.TYPE_WALL;

    public final static byte FACING_ON_X = 0;
    public final static byte FACING_ON_Z = 1;

    public final static byte STATE_CLOSED = 0;
    public final static byte STATE_OPEN   = 1;

    public final static byte DIR_POS = 0;
    public final static byte DIR_NEG = 1;

    /**
     * Returns type (vanilla, picket, etc).
     */
    public static int getType(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0xf;
    }

    /**
     * Sets type (vanilla, picket, etc).
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff0;
        temp |= type;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns facing (path on x, or path on z).
     */
    public static int getFacing(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x20;
        return temp >> 5;
    }

    /**
     * Sets facing (path on x, or path on z).
     */
    public static void setFacing(TEBase TE, int facing)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffdf;
        temp |= facing << 5;

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
     * Returns opening direction (positive or negative on axis opposite of facing).
     */
    public static int getDirOpen(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x10;
        return temp >> 4;
    }

    /**
     * Sets opening direction (positive or negative on axis opposite of facing).
     */
    public static void setDirOpen(TEBase TE, int dirOpen)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffef;
        temp |= dirOpen << 4;

        BlockProperties.setMetadata(TE, temp);
    }

}
