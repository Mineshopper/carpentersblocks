package carpentersblocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.registry.BlockRegistry;

public class MigrationHelper {

    private static final String TAG_COVER      = "cover";
    private static final String TAG_DYE        = "dye";
    private static final String TAG_OVERLAY    = "overlay";
    private static final String TAG_ITEMSTACKS = "itemstacks";
    private static final String TAG_METADATA   = "metadata";

    /**
     * Prepares the tile entity for migration to MC 1.7.2.
     */
    public static void writeToNBT(TEBase TE, NBTTagCompound nbt)
    {
        NBTTagList itemstack_list = new NBTTagList();

        TE.newCover = getCoversAsItemStacks(TE);
        TE.newDye = getColorAsItemStacks(TE);
        TE.newOverlay = getOverlaysAsItemStacks(TE);

        for (byte side = 0; side < 7; ++side)
        {
            if (TE.newCover[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_COVER, side);
                TE.newCover[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (TE.newDye[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_DYE, side);
                TE.newDye[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (TE.newOverlay[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_OVERLAY, side);
                TE.newOverlay[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
        }

        nbt.setTag(TAG_ITEMSTACKS, itemstack_list);
        nbt.setShort(TAG_METADATA, filterData(TE));
    }

    private static ItemStack[] getOverlaysAsItemStacks(TEBase TE)
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasOverlay(TE, idx)) {
    			itemStack[idx] = OverlayHandler.getItemStack(TE.overlay[idx]);
    		}
    	}
    	return itemStack;
    }

    private static ItemStack[] getCoversAsItemStacks(TEBase TE)
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasCover(TE, idx)) {
    			itemStack[idx] = new ItemStack(BlockProperties.getCoverID(TE, idx), 1, BlockProperties.getCoverMetadata(TE, idx));
    		}
    	}
    	return itemStack;
    }

    private static ItemStack[] getColorAsItemStacks(TEBase TE)
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasDyeColor(TE, idx)) {
    			itemStack[idx] = new ItemStack(Item.dyePowder, 1, 15 - TE.color[idx]);
    		}
    	}
    	return itemStack;
    }

    /**
     * Filters old data and returns the result.
     */
    private static short filterData(TEBase TE)
    {
    	Block block = TE.getBlockType();
    	short data = TE.data;

    	if (block.equals(BlockRegistry.blockCarpentersSlope)) {

    		byte[] oldIdToNewId = {
                    0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
                    11, 18, 14, 17, 13, 19, 15, 16, 12, 27, 23,
                    24, 20, 26, 22, 25, 21, 34, 30, 33, 29, 35,
                    31, 32, 28, 43, 39, 40, 36, 42, 38, 41, 37,
                    45, 44, 46, 47, 48, 49, 50, 51, 52, 53, 54,
                    55, 56, 57, 58, 59, 60, 61, 62, 63, 64
            };

            data = oldIdToNewId[data];

    	} else if (block.equals(BlockRegistry.blockCarpentersStairs)) {

            byte[] oldIdToNewId = {
                    0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
                    11, 18, 14, 17, 13, 19, 15, 16, 12, 27, 23,
                    24, 20, 26, 22, 25, 21, 34, 30, 33, 29, 35,
                    31, 32, 28, 43, 39, 40, 36, 42, 38, 41, 37,
                    45, 44, 46, 47, 48, 49, 50, 51, 52, 53, 54,
                    55, 56, 57, 58, 59, 60, 61, 62, 63, 64
            };

            data = oldIdToNewId[data];

    	}

    	return data;
    }

}
