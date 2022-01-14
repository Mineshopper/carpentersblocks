package com.carpentersblocks.item;

import com.carpentersblocks.api.ICarpentersHammer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemCarpentersHammer extends Item implements ICarpentersHammer {
    
    public ItemCarpentersHammer(Properties properties) {
		super(properties);
	}

	@Override
    public void onHammerUse(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.getItemInHand(hand).hurtAndBreak(1, playerEntity, (a) -> {
        	a.broadcastBreakEvent(hand);
        });
    }
	
    @Override
    public boolean canUseHammer(World world, PlayerEntity playerEntity, Hand hand) {
        return true;
    }
    
}