package com.carpentersblocks.client.itemgroup;

import java.util.function.Supplier;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class CarpentersBlocksItemGroup extends ItemGroup {
    
	private final Supplier<ItemStack> iconSupplier;
	
	public CarpentersBlocksItemGroup(String name, final Supplier<ItemStack> iconSupplier) {
		super(name);
		this.iconSupplier = iconSupplier;
	}
	
	@Override
	public ItemStack makeIcon() {
		return iconSupplier.get();
	}

}