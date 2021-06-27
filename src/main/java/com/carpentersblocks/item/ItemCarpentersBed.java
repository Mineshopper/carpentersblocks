package com.carpentersblocks.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ItemCarpentersBed extends Item {

	public ItemCarpentersBed(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		// TODO: Place bed block
		return super.useOn(context);
	}

}
