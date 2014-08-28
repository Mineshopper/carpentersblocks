package com.carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.Bed;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersBed extends ItemBlock {

    public ItemCarpentersBed(int itemID)
    {
        super(itemID);
        setMaxStackSize(64);
        setCreativeTab(CarpentersBlocks.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "bed");
    }

    @Override
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side == 1) {

            ++y;

            int facing = BlockProperties.getOppositeFacing(entityPlayer);
            ForgeDirection dir = BlockProperties.getDirectionFromFacing(facing);

            int x_offset = x - dir.offsetX;
            int z_offset = z - dir.offsetZ;

            if (
                    entityPlayer.canPlayerEdit(x, y, z, side, itemStack)                                                &&
                    entityPlayer.canPlayerEdit(x_offset, y, z_offset, side, itemStack)                                  &&
                    world.isAirBlock(x, y, z)                                                                           &&
                    world.isAirBlock(x_offset, y, z_offset)                                                             &&
                    world.doesBlockHaveSolidTopSurface(x, y - 1, z)                                                     &&
                    world.doesBlockHaveSolidTopSurface(x_offset, y - 1, z_offset)                                       &&
                    placeBlock(world, BlockRegistry.blockCarpentersBed, entityPlayer, itemStack, x, y, z)               &&
                    placeBlock(world, BlockRegistry.blockCarpentersBed, entityPlayer, itemStack, x_offset, y, z_offset)
                    )
            {

                /* Foot of bed. */

                TEBase TE_foot = (TEBase) world.getBlockTileEntity(x, y, z);
                Bed.setDirection(TE_foot, facing);

                /* Head of bed. */

                TEBase TE_head = (TEBase) world.getBlockTileEntity(x_offset, y, z_offset);
                Bed.setHeadOfBed(TE_head);
                Bed.setDirection(TE_head, facing);

                BlockProperties.playBlockSound(world, new ItemStack(Block.planks), x, y, z, false);

                if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
                }

                return true;
            }

        }

        return false;
    }

}
