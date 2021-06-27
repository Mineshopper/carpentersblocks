package com.carpentersblocks.item;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.block.AbstractCoverableBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemCarpentersChisel extends Item implements ICarpentersChisel {

    public ItemCarpentersChisel(Properties properties) {
		super(properties);
	}

	@Override
    public void onChiselUse(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.getItemInHand(hand).hurtAndBreak(1, playerEntity, (a) -> {
        	a.broadcastBreakEvent(playerEntity.swingingArm);
        });
    }

    @Override
    public boolean canUseChisel(World world, PlayerEntity playerEntity, Hand hand) {
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
    	return blockState.getBlock() instanceof AbstractCoverableBlock;
    }

}