package com.carpentersblocks.data;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.BlockRegistry;

public class GarageDoor implements ISided {

    /**
     * 16-bit data components:
     *
     * [000000] [0]  [0]   [0]   [000] [0000]
     * Unused   Host Rigid State Dir   Type
     */

    public static final int  TYPE_DEFAULT   = 0;
    public static final int  TYPE_GLASS_TOP = 1;
    public static final int  TYPE_GLASS     = 2;
    public static final int  TYPE_SIDING    = 3;
    public static final int  TYPE_HIDDEN    = 4;

    public final static int  STATE_CLOSED   = 0;
    public final static int  STATE_OPEN     = 1;

    public final static byte DOOR_NONRIGID = 0;
    public final static byte DOOR_RIGID    = 1;

    /**
     * Returns type.
     */
    public int getType(TEBase TE)
    {
        return TE.getData() & 0xf;
    }

    /**
     * Sets type.
     */
    public void setType(TEBase TE, int type)
    {
        int temp = (TE.getData() & ~0xf) | type;
        TE.setData(temp);
    }

    /**
     * Returns direction.
     */
    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        int side = (TE.getData() & 0x70) >> 4;
        return ForgeDirection.getOrientation(side);
    }

    /**
     * Sets direction.
     */
    @Override
    public void setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = (TE.getData() & ~0x70) | (dir.ordinal() << 4);
        TE.setData(temp);
    }

    /**
     * Returns state (open or closed).
     */
    public int getState(TEBase TE)
    {
        return (TE.getData() & 0x80) >> 7;
    }

    /**
     * Sets state (open or closed).
     */
    public void setState(TEBase TE, int state, boolean playSound)
    {
        int temp = (TE.getData() & ~0x80) | (state << 7);

        World world = TE.getWorldObj();
        if (!world.isRemote && playSound) {
            world.playAuxSFXAtEntity((EntityPlayer)null, 1003, TE.xCoord, TE.yCoord, TE.zCoord, 0);
        }

        TE.setData(temp);
    }

    /**
     * Whether garage door is rigid (requires redstone).
     */
    public boolean isRigid(TEBase TE)
    {
        return getRigidity(TE) == DOOR_RIGID;
    }

    /**
     * Returns rigidity (requiring redstone).
     */
    public int getRigidity(TEBase TE)
    {
        return (TE.getData() & 0x100) >> 8;
    }

    /**
     * Sets rigidity (requiring redstone).
     */
    public void setRigidity(TEBase TE, int rigidity)
    {
        int temp = (TE.getData() & ~0x100) | (rigidity << 8);
        TE.setData(temp);
    }

    /**
     * Sets host door (the topmost).
     */
    public void setHost(TEBase TE)
    {
        int temp = TE.getData() | 0x200;
        TE.setData(temp);
    }

    /**
     * Returns true if door is host (the topmost).
     */
    public boolean isHost(TEBase TE)
    {
        return (TE.getData() & 0x200) > 0;
    }

    /**
     * Recursively adds door pieces for current vertical stack and
     * also neighboring stacks in the given direction.
     *
     * @param TE a reference {@link TEBase}
     * @param list an {@link ArrayList} of current door pieces
     * @param dir the {@link ForgeDirection} used when locating neighbor pieces
     * @param addStack <code>true</code> if current stack for reference {@link TEBase} should be added,
     *                 or <code>false</code> if first stack should not be added to list
     */
    private void addConnectingDoors(TEBase TE, ArrayList<TEBase> list, ForgeDirection dir, boolean addStack)
    {
        World world = TE.getWorldObj();
        boolean neighborFound = false;
        ForgeDirection facing = getDirection(TE);

        int x = TE.xCoord;
        int z = TE.zCoord;
        int y = getBottommost(world, x, TE.yCoord, z).yCoord;
        do {
            TEBase temp = BlockProperties.getTileEntity(BlockRegistry.blockCarpentersGarageDoor, world, x, y, z);
            if (temp != null) {

                // Add door piece to list
                if (addStack) {
                    list.add(temp);
                }

                // Check if adjacent piece exists, recursively add adjacent pieces
                if (!neighborFound) {
                    TEBase TE_adj = BlockProperties.getTileEntity(BlockRegistry.blockCarpentersGarageDoor, world, x + dir.offsetX, y, z + dir.offsetZ);
                    if (TE_adj != null && getDirection(TE_adj).equals(facing)) {
                        neighborFound = true;
                        // Call function again
                        addConnectingDoors(TE_adj, list, dir, true);
                    }
                }

            }
        } while (world.getBlock(x, ++y, z).equals(BlockRegistry.blockCarpentersGarageDoor));
    }

    /**
     * Grabs all doors that face the same direction and
     * that are adjacent to any connecting piece of structure.
     *
     * @param TE the {@link TEBase}
     * @return an {@link ArrayList} of {@link TEBase} objects
     */
    public ArrayList<TEBase> getConnectingDoors(TEBase TE)
    {
        ForgeDirection dir = getDirection(TE).getRotation(ForgeDirection.UP);
        ArrayList<TEBase> list = new ArrayList<TEBase>();
        addConnectingDoors(TE, list, dir, true);
        addConnectingDoors(TE, list, dir.getOpposite(), false);

        System.out.println("DEBUG: Found " + list.size() + " doors.");

        return list;
    }

    /**
     * Whether garage door is open.
     *
     * @param TE the {@link TEBase}
     * @return <code>true</code> if garage door is open
     */
    public boolean isOpen(TEBase TE)
    {
        return getState(TE) == STATE_OPEN;
    }

    /**
     * Weather panel is the bottommost.
     *
     * @param  TE the {@link TEBase}
     * @return true if panel is the bottommost
     */
    public boolean isBottommost(TEBase TE)
    {
        return !TE.getWorldObj().getBlock(TE.xCoord, TE.yCoord - 1, TE.zCoord).equals(BlockRegistry.blockCarpentersGarageDoor);
    }

    /**
     * Gets the topmost garage door tile entity.
     *
     * @param  TE the {@link TEBase}
     * @return the {@link TEBase}
     */
    public TEBase getTopmost(World world, int x, int y, int z)
    {
        do {
            ++y;
        } while (world.getBlock(x, y, z).equals(BlockRegistry.blockCarpentersGarageDoor));

        return (TEBase) world.getTileEntity(x, y - 1, z);
    }

    /**
     * Gets the bottommost garage door tile entity.
     *
     * @param  TE the {@link TEBase}
     * @return the {@link TEBase}
     */
    public TEBase getBottommost(World world, int x, int y, int z)
    {
        do {
            --y;
        } while (world.getBlock(x, y, z).equals(BlockRegistry.blockCarpentersGarageDoor));

        return (TEBase) world.getTileEntity(x, y + 1, z);
    }

    /**
     * Whether block is visible.
     * <p>
     * If a block is open and not in the topmost position,
     * it cannot be selected or collided with.
     *
     * @param  TE the {@link TEBase}
     * @return true if visible
     */
    public boolean isVisible(TEBase TE)
    {
        if (isOpen(TE)) {
            return isHost(TE);
        } else {
            return true;
        }
    }

}
