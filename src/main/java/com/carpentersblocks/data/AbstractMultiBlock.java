package com.carpentersblocks.data;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.FeatureRegistry;

public abstract class AbstractMultiBlock {

    /**
     * Gathers adjacent blocks based on specific criteria, enabling
     * a group of blocks to act as a single entity when interacting
     * with only a single block.
     *
     * @param TE the {@link TEBase}
     * @param block the {@link Block} to match against
     * @return a {@link Set} of parts
     * @see {@link #getMatchingDataPattern}
     */
    public final Set<TEBase> getBlocks(TEBase TE, Block block)
    {
        Set<TEBase> set = new HashSet<TEBase>();
        int matchData = getMatchingDataPattern(TE);
        ForgeDirection[] dirs = getLocateDirs(TE);
        addAndLocateBlocks(TE, block, matchData, dirs, set);
        return set;
    }

    /**
     * Adds {@link TEBase} object to set, and continues locating additional
     * blocks that are part of structure.
     *
     * @param TE the {@link TEBase}
     * @param block the {@link Block} type to locate
     * @param matchData the {@link #getMatchingDataPattern data mask} to match against
     * @param dirs the {@link #getLocateDirs directions} to search
     * @param set the {@link Set} of blocks
     */
    private void addAndLocateBlocks(TEBase TE, Block block, int matchData, ForgeDirection[] dirs, Set<TEBase> set)
    {
        if (set.size() > FeatureRegistry.multiBlockSizeLimit) {
            return;
        }

        if (set.contains(TE)) {
            return;
        }

        // Add piece to final set
        set.add(TE);

        // Locate additional blocks
        for (ForgeDirection dir : dirs) {
            TEBase TE_adj = BlockProperties.getTileEntity(block, TE.getWorldObj(), TE.xCoord - dir.offsetX, TE.yCoord - dir.offsetY, TE.zCoord - dir.offsetZ);
            if (TE_adj != null && (TE_adj.getData() & matchData) == matchData) {
                addAndLocateBlocks(TE_adj, block, matchData, dirs, set);
            }
        }
    }

    /**
     * Used to match against adjacent block data when determining
     * whether a connection can be made.
     * <p>
     * Supplies an integer mask to logical AND with candidate
     * block data.
     *
     * @return an integer
     */
    abstract public int getMatchingDataPattern(TEBase TE);

    /**
     * Grabs an array of valid {@link ForgeDirection ForgeDirections}
     * used when locating additional pieces for block in a
     * two-dimensional space.
     *
     * @param TE the {@link TEBase}
     * @return an array of supported {@link ForgeDirection ForgeDirections}
     */
    abstract public ForgeDirection[] getLocateDirs(TEBase TE);

}
