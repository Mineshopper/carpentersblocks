package com.carpentersblocks.item;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.block.BlockCoverable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemCarpentersChisel extends Item implements ICarpentersChisel {

    public ItemCarpentersChisel() {}

    @Override
    public void onChiselUse(World world, EntityPlayer entityPlayer) {
        entityPlayer.getHeldItemMainhand().damageItem(1, entityPlayer);
    }

    @Override
    public boolean canUseChisel(World world, EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockState) {
    	return blockState.getBlock() instanceof BlockCoverable;
    }

}