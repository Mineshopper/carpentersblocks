package carpentersblocks.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Safe
{

    /**
     * 16-bit data components:
     *
     * [0000000000]  [00]        [0]   [0]    [00]
     * Unused        Automation  Lock  State  Facing
     */

    public final static byte STATE_CLOSED = 0;
    public final static byte STATE_OPEN   = 1;

    public final static byte LOCK_SET   = 0;
    public final static byte LOCK_UNSET = 1;

    public final static byte AUTOMATION_DISABLED = 0;
    public final static byte AUTOMATION_SEND     = 1;
    public final static byte AUTOMATION_RECEIVE  = 2;
    public final static byte AUTOMATION_ALL      = 3;

    /**
     * Returns facing.
     */
    public static ForgeDirection getFacing(TEBase TE)
    {
        int facing = BlockProperties.getMetadata(TE) & 0x3;

        return BlockProperties.getDirectionFromFacing(facing);
    }

    /**
     * Sets facing.
     * Stored as player facing from 0 to 3.
     */
    public static void setFacing(TEBase TE, int facing)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfffc;
        temp |= facing;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns state.
     */
    public static int getState(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x4;

        return temp >> 2;
    }

    /**
     * Sets state.
     */
    public static void setState(TEBase TE, int state)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfffb;
        temp |= state << 2;

        World world = TE.getWorldObj();

        if (!world.isRemote) {
            world.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);
        }

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns whether safe is locked.
     */
    public static boolean isLocked(TEBase TE)
    {
        return (BlockProperties.getMetadata(TE) & 0x8) == LOCK_SET;
    }

    /**
     * Sets lock state.
     */
    public static void setLocked(TEBase TE, boolean isLocked)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff7;
        temp |= (isLocked ? LOCK_SET : LOCK_UNSET) << 3;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns automation permissions for safe.
     */
    public static int getAutoPerm(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x30;

        return temp >> 4;
    }

    /**
     * Sets automation permissions for safe.
     */
    public static void setAutoPerm(TEBase TE, int autoPerm)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffcf;
        temp |= autoPerm << 4;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns whether safe is open.
     */
    public static boolean isOpen(TEBase TE)
    {
        return getState(TE) == STATE_OPEN;
    }

    /**
     * Returns whether items can be inserted into safe through automation.
     */
    public static boolean allowsInsertion(TEBase TE)
    {
        int autoPerm = getAutoPerm(TE);

        return autoPerm == AUTOMATION_RECEIVE || autoPerm == AUTOMATION_ALL;
    }

    /**
     * Returns whether items can be extracted from safe through automation.
     */
    public static boolean allowsExtraction(TEBase TE)
    {
        int autoPerm = getAutoPerm(TE);

        return autoPerm == AUTOMATION_SEND || autoPerm == AUTOMATION_ALL;
    }

}
