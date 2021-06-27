package com.carpentersblocks.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

public class ItemCarpentersApplicator extends Item {

    public ItemCarpentersApplicator(Properties properties) {
		super(properties);
	}

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
    	return false;
    }

}