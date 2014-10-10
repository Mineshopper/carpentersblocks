package com.carpentersblocks.data;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class Torch implements ISided {

    /**
     * 16-bit data components:
     *
     * [0000000] [0000] [00]  [000]
     * Unused    Type   State Dir
     */

    public enum State
    {
        LIT,
        SMOLDERING,
        UNLIT
    }

    public final static byte TYPE_VANILLA = 0;
    public final static byte TYPE_LANTERN = 1;

    /**
     * Returns direction.
     */
    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        return ForgeDirection.getOrientation(BlockProperties.getMetadata(TE) & 0x7);
    }

    /**
     * Sets direction.
     */
    @Override
    public void setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff8;
        temp |= dir.ordinal();

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Gets type.
     */
    public int getType(TEBase TE)
    {
        return (BlockProperties.getMetadata(TE) & 0x1e0) >> 5;
    }

    /**
     * Sets type.
     */
    public void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getMetadata(TE) & ~0x1e0;
        temp |= (type & 0xf) << 5;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns state.
     */
    public State getState(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x18;
        int val = temp >> 3;

        return val == State.LIT.ordinal() ? State.LIT : val == State.SMOLDERING.ordinal() ? State.SMOLDERING : State.UNLIT;
    }

    /**
     * Sets state.
     */
    public void setState(TEBase TE, State state)
    {
        if (state.ordinal() > getState(TE).ordinal()) {
            double[] headCoords = getHeadCoordinates(TE);
            World world = TE.getWorldObj();
            world.playSoundEffect(headCoords[0], headCoords[1], headCoords[2], "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }

        int temp = BlockProperties.getMetadata(TE) & 0xffe7;
        temp |= state.ordinal() << 3;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns location where particles and sounds originate.
     */
    public double[] getHeadCoordinates(TEBase TE)
    {
        double[] coords;
        double xOffset = TE.xCoord + 0.5F;
        double yOffset = TE.yCoord + 0.7F;
        double zOffset = TE.zCoord + 0.5F;

        if (getType(TE) == TYPE_VANILLA) {
            double offset1 = 0.2199999988079071D;
            double offset2 = 0.27000001072883606D;
            ForgeDirection side = getDirection(TE);
            switch (side) {
                case NORTH:
                    coords = new double[] { xOffset, yOffset + offset1, zOffset + offset2 };
                    break;
                case SOUTH:
                    coords = new double[] { xOffset, yOffset + offset1, zOffset - offset2 };
                    break;
                case WEST:
                    coords = new double[] { xOffset + offset2, yOffset + offset1, zOffset };
                    break;
                case EAST:
                    coords = new double[] { xOffset - offset2, yOffset + offset1, zOffset };
                    break;
                default:
                    coords = new double[] { xOffset, yOffset, zOffset }; // Default UP
                    break;
            }
        } else {
            coords = new double[] { xOffset, TE.yCoord + 0.5625F, zOffset };
        }

        return coords;
    }

}
