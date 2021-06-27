package com.carpentersblocks;

import com.carpentersblocks.client.itemgroup.CarpentersBlocksItemGroup;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.item.CbItems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(value = CarpentersBlocks.MOD_ID)
public class CarpentersBlocks {
	
    public static final String MOD_ID = "carpentersblocks";
    public static final ItemGroup ITEM_GROUP = new CarpentersBlocksItemGroup(
    		MOD_ID,
    		() -> new ItemStack(CbItems.itemHammer));
    
    public CarpentersBlocks() {
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_SPEC);
	}
    
}