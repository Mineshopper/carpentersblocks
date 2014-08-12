package com.carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlock extends Item {

    /**
     * Places the block in the world and calls post-placement methods.
     */
    protected boolean placeBlock(World world, Block block, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z)
    {
        if (world.setBlock(x, y, z, block, 0, 4)) {

            block.onBlockPlacedBy(world, x, y, z, entityPlayer, itemStack);
            block.onPostBlockPlaced(world, x, y, z, 0);

            return true;

        } else {

            return false;

        }
    }

}
