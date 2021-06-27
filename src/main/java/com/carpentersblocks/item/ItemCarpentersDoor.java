package com.carpentersblocks.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ItemCarpentersDoor extends Item {

	public ItemCarpentersDoor(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		// TODO: place door
		return super.useOn(context);
	}

}
