package com.carpentersblocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import com.carpentersblocks.util.registry.BlockRegistry;

public class MigrationHelper {

    boolean containsCache = false;
    boolean containsFlowerPotCache = false;

    private final String TAG_COVER            = "cover";
    private final String TAG_DYE              = "dye";
    private final String TAG_OVERLAY          = "overlay";
    private final String TAG_ITEMSTACKS       = "itemstacks";
    private final String TAG_METADATA         = "metadata";
    private final String TAG_SOIL             = "soil";
    private final String TAG_PLANT            = "plant";
    private final String TAG_PLANT_ITEMSTACKS = "plantitemstacks";

    private short[] cachedCover  = new short[7];
    private byte[] cachedColor   = new byte[7];
    private byte[] cachedOverlay = new byte[7];
    private short cachedData;

    private short cachedPlant;
    private short cachedSoil;

    /**
     * Store old NBT values for conversion later.
     */
    public void cacheNBT(NBTTagCompound nbt)
    {
        for (int count = 0; count < 7; ++count) {
            cachedCover[count] = nbt.getShort("cover_" + count);
        }

        cachedColor = nbt.getByteArray("color");
        cachedOverlay = nbt.getByteArray("overlay");
        cachedData = nbt.getShort("data");
        containsCache = true;
    }

    public void cacheFlowerPotNBT(NBTTagCompound nbt)
    {
        cachedPlant = nbt.getShort("plant");
        cachedSoil = nbt.getShort("soil");
        containsFlowerPotCache = true;
    }

    /**
     * Write new values to NBT using cached values.
     */
    public void writeToNBT(TEBase TE, NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();

        TE.cover = getCoversAsItemStacks();
        TE.dye = getColorAsItemStacks();
        TE.overlay = getOverlaysAsItemStacks();

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

        short data = filterData(TE, cachedData);
        TE.metadata = data;
        nbt.setShort(TAG_METADATA, data);

        nbt.setTag(TAG_ITEMSTACKS, list);
    }

    /**
     * Prepares the tile entity for migration to MC 1.7.2.
     */
    public void writeFlowerPotToNBT(TECarpentersFlowerPot TE, NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();

        if (hasBlock(cachedSoil)) {
            TE.soil = new ItemStack(getBlockId(cachedSoil), 1, getBlockMetadata(cachedSoil));
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_SOIL, (byte) 0);
            TE.soil.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }
        if (hasBlock(cachedPlant)) {
            TE.plant = new ItemStack(getBlockId(cachedPlant), 1, getBlockMetadata(cachedPlant));
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_PLANT, (byte) 0);
            TE.plant.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }

        nbt.setTag(TAG_PLANT_ITEMSTACKS, list);
    }

    private ItemStack[] getOverlaysAsItemStacks()
    {
        ItemStack[] overlayStack = {
                null,
                new ItemStack(Item.seeds),
                new ItemStack(Item.snowball),
                new ItemStack(Item.silk),
                new ItemStack(Block.vine),
                new ItemStack(Item.wheat),
                new ItemStack(Block.mushroomBrown)
        };

        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasData(cachedOverlay[idx])) {
                itemStack[idx] = overlayStack[cachedOverlay[idx]];
            }
        }
        return itemStack;
    }

    private ItemStack[] getCoversAsItemStacks()
    {
        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasBlock(cachedCover[idx])) {
                itemStack[idx] = new ItemStack(getBlockId(cachedCover[idx]), 1, getBlockMetadata(cachedCover[idx]));
            }
        }
        return itemStack;
    }

    private ItemStack[] getColorAsItemStacks()
    {
        ItemStack[] itemStack = new ItemStack[7];
        for (int idx = 0; idx < 7; ++idx) {
            if (hasData(cachedColor[idx])) {
                itemStack[idx] = new ItemStack(Item.dyePowder, 1, 15 - cachedColor[idx]);
            }
        }
        return itemStack;
    }

    /**
     * Filters old data and returns the result.
     */
    private short filterData(TEBase TE, short data)
    {
        Block block = TE.getBlockType();
        if (block.equals(BlockRegistry.blockCarpentersSlope) || block.equals(BlockRegistry.blockCarpentersStairs)) {
            byte[] oldIdToNewId = {
                    0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
                    11, 18, 14, 17, 13, 19, 15, 16, 12, 27, 23,
                    24, 20, 26, 22, 25, 21, 34, 30, 33, 29, 35,
                    31, 32, 28, 43, 39, 40, 36, 42, 38, 41, 37,
                    45, 44, 46, 47, 48, 49, 50, 51, 52, 53, 54,
                    55, 56, 57, 58, 59, 60, 61, 62, 63, 64
            };
            return oldIdToNewId[data];
        } else {
            return data;
        }
    }

    /**
     * Returns block ID.
     */
    private int getBlockId(Short data)
    {
        return data & 0xfff;
    }

    /**
     * Returns block metadata.
     */
    private int getBlockMetadata(Short data)
    {
        return (data & 0xf000) >>> 12;
    }

    /**
     * Returns true if data represents a valid block.
     */
    private boolean hasBlock(Short data)
    {
        int blockID = getBlockId(data);
        return blockID > 0 &&
               Block.blocksList[blockID] != null;
    }

    /**
     * Returns true if data contains useful information.
     */
    private boolean hasData(byte data)
    {
        return data > 0;
    }

}
