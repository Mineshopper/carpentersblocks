package com.carpentersblocks.data;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class Button implements ISided {

    /**
     * 16-bit data components:
     *
     * [000000000] [0]      [0]   [000]
     * Unused      Polarity State Dir
     */

    public static final byte POLARITY_POSITIVE = 0;
    public static final byte POLARITY_NEGATIVE = 1;

    public static final byte STATE_OFF = 0;
    public static final byte STATE_ON  = 1;

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
     * Returns state.
     */
    public int getState(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x8;
        return temp >> 3;
    }

    /**
     * Sets state.
     */
    public void setState(TEBase TE, int state, boolean playSound)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff7;
        temp |= state << 3;

        World world = TE.getWorldObj();

        if (
                !world.isRemote &&
                BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getMaterial() != Material.cloth &&
                playSound &&
                getState(TE) != state
                ) {
            world.playSoundEffect(TE.xCoord + 0.5D, TE.yCoord + 0.5D, TE.zCoord + 0.5D, "random.click", 0.3F, getState(TE) == STATE_ON ? 0.5F : 0.6F);
        }

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns polarity.
     */
    public int getPolarity(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x10;
        return temp >> 4;
    }

    /**
     * Sets polarity.
     */
    public void setPolarity(TEBase TE, int polarity)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffef;
        temp |= polarity << 4;

        BlockProperties.setMetadata(TE, temp);
    }

}
