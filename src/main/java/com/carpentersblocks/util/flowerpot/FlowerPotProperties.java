package com.carpentersblocks.util.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersFlowerPot;
import com.carpentersblocks.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FlowerPotProperties {

    /**
     * Will return block from ItemStack. This is to be used for plants only.
     */
    public static Block toBlock(ItemStack itemStack)
    {
        Object plant = FlowerPotHandler.itemPlant.get(itemStack.getItem());

        if (plant != null) {
            return (Block) plant;
        } else {
            return BlockProperties.toBlock(itemStack);
        }
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns plant color.
     */
    public static int getPlantColor(TEBase TE)
    {
        ItemStack itemStack = getPlant(TE);
        Block block = toBlock(itemStack);

        BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());

        int color1 = block.getBlockColor();
        int color2 = block.colorMultiplier(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);

        BlockProperties.resetHostMetadata(TE);

        return color1 < color2 ? color1 : color2;
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns whether plant can be colored - leaves, grass, etc.
     */
    public static boolean isPlantColorable(TEBase TE)
    {
        return FlowerPotProperties.getPlantColor(TE) != 16777215;
    }

    /**
     * Returns whether pot has soil.
     */
    public static boolean hasSoil(TEBase TE)
    {
        ItemStack itemStack = ((TECarpentersFlowerPot)TE).soil;

        return itemStack != null && isSoil(itemStack);
    }

    /**
     * Returns whether ItemStack contains soil.
     */
    public static boolean isSoil(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemBlock) {
            Block block = BlockProperties.toBlock(itemStack);
            if (!block.hasTileEntity(itemStack.getItemDamage())) {
                Material material = block.getMaterial();
                return material.equals(Material.grass) || material.equals(Material.ground) || material.equals(Material.sand);
            }
        }

        return false;
    }

    /**
     * Returns soil ItemStack.
     */
    public static ItemStack getSoil(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).soil;
    }

    /**
     * Sets soil block.
     */
    public static boolean setSoil(TEBase TE, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasSoil(TE)) {
            BlockProperties.dropAttribute(TE, getSoil(TE));
        }

        ((TECarpentersFlowerPot)TE).soil = BlockProperties.getReducedStack(itemStack);

        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, BlockProperties.toBlock(itemStack));
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

        return true;
    }

    /**
     * Returns whether pot has plant.
     */
    public static boolean hasPlant(TEBase TE)
    {
        ItemStack itemStack = ((TECarpentersFlowerPot)TE).plant;

        return itemStack != null && isPlant(itemStack);
    }

    /**
     * Returns whether ItemStack contains a plant.
     */
    public static boolean isPlant(ItemStack itemStack)
    {
        Block block = BlockProperties.toBlock(itemStack);

        if (!block.equals(Blocks.air)) {
            if (!block.hasTileEntity(itemStack.getItemDamage())) {
                return block instanceof IPlantable || block instanceof IShearable;
            } else {
                return false;
            }
        } else {
            return FlowerPotHandler.itemPlant.containsKey(itemStack.getItem());
        }
    }

    /**
     * Returns plant block.
     */
    public static ItemStack getPlant(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).plant;
    }

    /**
     * Sets plant block.
     */
    public static boolean setPlant(TEBase TE, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasPlant(TE)) {
            BlockProperties.dropAttribute(TE, getPlant(TE));
        }

        ((TECarpentersFlowerPot)TE).plant = BlockProperties.getReducedStack(itemStack);

        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, BlockProperties.toBlock(itemStack));
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

        return true;
    }

}
