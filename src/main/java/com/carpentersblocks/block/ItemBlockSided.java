package com.carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockSided extends ItemBlock {

    public ItemBlockSided(Block block)
    {
        super(block);
    }

    @Override
    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        /*
         * For BlockSided blocks, we need to create them using the 0 flag to
         * ensure that block updates don't happen too early.  This is most
         * noticeable with the Lever and Button, where they will pop off walls
         * immediately upon placement if a neighbor is updated before the server
         * has had a chance to initialize the block tile entity with a side.
         */

        if (!world.setBlock(x, y, z, field_150939_a, metadata, 2)) {
            return false;
        }

        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }

}
