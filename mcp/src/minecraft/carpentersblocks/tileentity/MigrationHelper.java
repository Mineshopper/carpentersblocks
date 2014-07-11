package carpentersblocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.registry.BlockRegistry;

public class MigrationHelper {

    private static final String TAG_COVER            = "cover";
    private static final String TAG_DYE              = "dye";
    private static final String TAG_OVERLAY          = "overlay";
    private static final String TAG_ITEMSTACKS       = "itemstacks";
    private static final String TAG_METADATA         = "metadata";
    private static final String TAG_SOIL             = "soil";
    private static final String TAG_PLANT            = "plant";
    private static final String TAG_PLANT_ITEMSTACKS = "plantitemstacks";

    /**
     * Prepares the tile entity for migration to MC 1.7.2.
     */
    public static void readFromFlowerPotNBT(TECarpentersFlowerPot TE, NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();

        if (hasSoil(nbt)) {
            TE.soil = getSoil(nbt);
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_SOIL, (byte) 200);
            TE.soil.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }
        if (hasPlant(nbt)) {
            TE.plant = getPlant(nbt);
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_PLANT, (byte) 201);
            TE.plant.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }

        nbt.setTag(TAG_PLANT_ITEMSTACKS, list);
    }

    /**
     * Prepares the tile entity for migration to MC 1.7.2.
     */
    public static void readFromNBT(TEBase TE, NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();

        TE.cover = getCoversAsItemStacks(nbt);
        TE.dye = getColorAsItemStacks(nbt);
        TE.overlay = getOverlaysAsItemStacks(nbt, TE);

        for (byte side = 0; side < 7; ++side)
        {
            if (TE.cover[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_COVER, side);
                TE.cover[side].writeToNBT(nbt1);
                list.appendTag(nbt1);
            }
            if (TE.dye[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_DYE, side);
                TE.dye[side].writeToNBT(nbt1);
                list.appendTag(nbt1);
            }
            if (TE.overlay[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_OVERLAY, side);
                TE.overlay[side].writeToNBT(nbt1);
                list.appendTag(nbt1);
            }
        }

        nbt.setTag(TAG_ITEMSTACKS, list);
        nbt.setShort(TAG_METADATA, filterData(TE, nbt.getShort("data")));
    }

    private static ItemStack[] getOverlaysAsItemStacks(NBTTagCompound nbt, TEBase TE)
    {
        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasOverlay(nbt, idx)) {
                itemStack[idx] = OverlayHandler.getOverlayType(TE.overlay[idx]).getItemStack();
            }
        }
        return itemStack;
    }

    private static ItemStack[] getCoversAsItemStacks(NBTTagCompound nbt)
    {
        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasCover(nbt, idx)) {
                itemStack[idx] = getCover(nbt, idx);
            }
        }
        return itemStack;
    }

    private static ItemStack[] getColorAsItemStacks(NBTTagCompound nbt)
    {
        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasDyeColor(nbt, idx)) {
                itemStack[idx] = new ItemStack(Item.dyePowder, 1, 15 - nbt.getByteArray("color")[idx]);
            }
        }
        return itemStack;
    }

    /**
     * Filters old data and returns the result.
     */
    private static short filterData(TEBase TE, short data)
    {
        Block block = TE.getBlockType();

        byte[] oldIdToNewId = {
                0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
                11, 18, 14, 17, 13, 19, 15, 16, 12, 27, 23,
                24, 20, 26, 22, 25, 21, 34, 30, 33, 29, 35,
                31, 32, 28, 43, 39, 40, 36, 42, 38, 41, 37,
                45, 44, 46, 47, 48, 49, 50, 51, 52, 53, 54,
                55, 56, 57, 58, 59, 60, 61, 62, 63, 64
        };

        if (block.equals(BlockRegistry.blockCarpentersSlope)) {
            data = oldIdToNewId[data];
        } else if (block.equals(BlockRegistry.blockCarpentersStairs)) {
            data = oldIdToNewId[data];
        }

        return data;
    }

    ////// OLD FLOWERPOT CONVERSION CODE

    /**
     * Returns soil block ID.
     */
    private static int getSoilID(NBTTagCompound nbt)
    {
        return nbt.getShort("soil") & 0xfff;
    }

    /**
     * Returns soil block metadata.
     */
    private static int getSoilMetadata(NBTTagCompound nbt)
    {
        return (nbt.getShort("soil") & 0xf000) >>> 12;
    }

    /**
     * Returns plant block ID.
     */
    private static int getPlantID(NBTTagCompound nbt)
    {
        return nbt.getShort("plant") & 0xfff;
    }

    /**
     * Returns plant block metadata.
     */
    private static int getPlantMetadata(NBTTagCompound nbt)
    {
        return (nbt.getShort("plant") & 0xf000) >>> 12;
    }

    /**
     * Returns soil block.
     */
    private static ItemStack getSoil(NBTTagCompound nbt)
    {
        return new ItemStack(getSoilID(nbt), 1, getSoilMetadata(nbt));
    }

    /**
     * Returns plant block.
     */
    private static ItemStack getPlant(NBTTagCompound nbt)
    {
        return new ItemStack(getPlantID(nbt), 1, getPlantMetadata(nbt));
    }

    /**
     * Returns whether pot has soil.
     */
    private static boolean hasSoil(NBTTagCompound nbt)
    {
        int blockID = getSoilID(nbt);
        getSoilMetadata(nbt);

        return blockID > 0 &&
               Block.blocksList[blockID] != null;
    }

    /**
     * Returns whether pot has plant.
     */
    private static boolean hasPlant(NBTTagCompound nbt)
    {
        int blockID = getPlantID(nbt);
        getPlantMetadata(nbt);

        return blockID > 0 &&
               Block.blocksList[blockID] != null;
    }

    ////// OLD CONVERSION CODE

    /**
     * Returns cover block ID.
     */
    private static int getCoverID(NBTTagCompound nbt, int side)
    {
        return nbt.getShort("cover_" + side) & 0xfff;
    }

    /**
     * Returns cover block metadata.
     */
    private static int getCoverMetadata(NBTTagCompound nbt, int side)
    {
        return (nbt.getShort("cover_" + side) & 0xf000) >>> 12;
    }

    /**
     * Returns whether block has a cover.
     * Checks if block ID exists and whether it is a valid cover block.
     */
    private static boolean hasCover(NBTTagCompound nbt, int side)
    {
        int coverID = getCoverID(nbt, side);

        return coverID > 0 && Block.blocksList[coverID] != null;
    }

    /**
     * Returns cover block.
     */
    private static ItemStack getCover(NBTTagCompound nbt, int side)
    {
        if (hasCover(nbt, side)) {
            new ItemStack(getCoverID(nbt, side), 1, getCoverMetadata(nbt, side));
        }

        return null;
    }

    /**
     * Returns whether side has cover.
     */
    private static boolean hasDyeColor(NBTTagCompound nbt, int side)
    {
        return nbt.getByteArray("color")[side] > 0;
    }

    /**
     * Returns whether block has overlay.
     */
    private static boolean hasOverlay(NBTTagCompound nbt, int side)
    {
        return nbt.getByteArray("overlay")[side] > 0;
    }

}
