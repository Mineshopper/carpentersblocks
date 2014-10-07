package com.carpentersblocks.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class MigrationHelper {

    /**
     * Updates data prior to version 3.3.1 to new format.
     *
     * @param nbt the {@link NBTTagCompound}
     */
    public static void updateMappingsOnRead(TEBase TE, NBTTagCompound nbt)
    {
        String TAG_METADATA      = "metadata";
        String TAG_OWNER         = "owner";
        String TAG_CHISEL_DESIGN = "chiselDesign";
        String TAG_DESIGN        = "design";

        TE.cbMetadata = nbt.getShort(TAG_METADATA);
        TE.cbDesign = nbt.getString(TAG_DESIGN);
        TE.cbOwner = nbt.getString(TAG_OWNER);

        for (int idx = 0; idx < 7; ++idx) {
            TE.cbChiselDesign[idx] = nbt.getString(TAG_CHISEL_DESIGN + "_" + idx);
        }

        /* Update base block data. */

        String TAG_ITEMSTACKS = "itemstacks";
        String TAG_COVER = "cover";
        String TAG_DYE = "dye";
        String TAG_OVERLAY = "overlay";
        String TAG_ILLUMINATOR = "illuminator";

        NBTTagList nbttaglist = nbt.getTagList(TAG_ITEMSTACKS, 10);
        for (int idx = 0; idx < nbttaglist.tagCount(); ++idx)
        {
            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(idx);
            ItemStack tempStack = ItemStack.loadItemStackFromNBT(nbt1);
            tempStack.stackSize = 1; // All ItemStacks pre-3.2.7 DEV R3 stored original stack sizes, reduce them here.
            if (nbt1.hasKey(TAG_COVER)) {
                System.out.println("DEBUG: Converting " + tempStack.getDisplayName() + " to new ID.");
                TE.cbAttrMap.put(TE.ID_COVER[nbt1.getByte(TAG_COVER) & 255], tempStack);
            } else if (nbt1.hasKey(TAG_DYE)) {
                TE.cbAttrMap.put(TE.ID_DYE[nbt1.getByte(TAG_DYE) & 255], tempStack);
            } else if (nbt1.hasKey(TAG_OVERLAY)) {
                TE.cbAttrMap.put(TE.ID_OVERLAY[nbt1.getByte(TAG_OVERLAY) & 255], tempStack);
            } else if (nbt1.hasKey(TAG_ILLUMINATOR)) {
                TE.cbAttrMap.put(TE.ID_ILLUMINATOR, tempStack);
            }
        }

        /* Update flower pot data. */

        String TAG_PLANT_ITEMSTACKS = "pot_property";
        String TAG_SOIL = "soil";
        String TAG_PLANT = "plant";

        NBTTagList flowerPotList = nbt.getTagList(TAG_PLANT_ITEMSTACKS, 10);
        for (int idx = 0; idx < flowerPotList.tagCount(); ++idx) {
            NBTTagCompound nbt1 = flowerPotList.getCompoundTagAt(idx);
            ItemStack tempStack = ItemStack.loadItemStackFromNBT(nbt1);
            tempStack.stackSize = 1; // All ItemStacks pre-3.2.7 DEV R3 stored original stack sizes, reduce them here.
            if (nbt1.hasKey(TAG_SOIL)) {
                TE.cbAttrMap.put(TECarpentersFlowerPot.ID_SOIL, tempStack);
            } else if (nbt1.hasKey(TAG_PLANT)) {
                TE.cbAttrMap.put(TECarpentersFlowerPot.ID_PLANT, tempStack);
            }
        }
    }

}
