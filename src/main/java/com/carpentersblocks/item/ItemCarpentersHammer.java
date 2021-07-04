package com.carpentersblocks.item;

import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.block.AbstractCoverableBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
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
    
    @Override
    public ActionResultType useOn(ItemUseContext p_195939_1_) {
    	// TODO Auto-generated method stub
    	return super.useOn(p_195939_1_);
    }
    
    @Override
    public boolean isCorrectToolForDrops(BlockState p_150897_1_) {
    	// TODO Auto-generated method stub
    	return super.isCorrectToolForDrops(p_150897_1_);
    }
    
    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
    	// TODO Auto-generated method stub
    	return super.canHarvestBlock(stack, state);
    }
    
    @Override
    public boolean canAttackBlock(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity) {
    	return !(blockState.getBlock() instanceof AbstractCoverableBlock);
    }
    
    @Override
    public boolean mineBlock(ItemStack p_179218_1_, World p_179218_2_, BlockState p_179218_3_, BlockPos p_179218_4_,
    		LivingEntity p_179218_5_) {
    	return super.mineBlock(p_179218_1_, p_179218_2_, p_179218_3_, p_179218_4_, p_179218_5_);
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
    	return super.onBlockStartBreak(itemstack, pos, player);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    	// TODO Auto-generated method stub
    	return super.onLeftClickEntity(stack, player, entity);
    }
    
    

}