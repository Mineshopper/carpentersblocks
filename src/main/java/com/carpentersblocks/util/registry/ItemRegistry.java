package com.carpentersblocks.util.registry;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.item.ItemCarpentersChisel;
import com.carpentersblocks.item.ItemCarpentersHammer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemRegistry {

    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;
    public static Item itemCarpentersTile;
    
    public static void preInit(FMLPreInitializationEvent event) {
    	if (ConfigRegistry.enableHammer) {
			itemCarpentersHammer = new ItemCarpentersHammer()
				.setRegistryName("carpenters_hammer")
				.setUnlocalizedName("itemCarpentersHammer")
				.setCreativeTab(CarpentersBlocks.CREATIVE_TAB)
				.setMaxStackSize(1);
			if (ConfigRegistry.itemCarpentersToolsDamageable) {
				itemCarpentersHammer.setMaxDamage(ConfigRegistry.itemCarpentersToolsUses);
			}
		}
		if (ConfigRegistry.enableChisel) {
			itemCarpentersChisel = new ItemCarpentersChisel()
				.setRegistryName("carpenters_chisel")
				.setUnlocalizedName("itemCarpentersChisel")
				.setCreativeTab(CarpentersBlocks.CREATIVE_TAB)
				.setMaxStackSize(1);
			if (ConfigRegistry.itemCarpentersToolsDamageable) {
				itemCarpentersChisel.setMaxDamage(ConfigRegistry.itemCarpentersToolsUses);
			}
		}
		// TODO: Register other items
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	if (ConfigRegistry.enableHammer) {
    		event.getRegistry().register(this.itemCarpentersHammer);
    	}
    	if (ConfigRegistry.enableChisel) {
    		event.getRegistry().register(this.itemCarpentersChisel);
    	}
    	// TODO: Register other items
    }
    
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        if (ConfigRegistry.enableHammer) {
        	ModelLoader.setCustomModelResourceLocation(itemCarpentersHammer, 0, new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":" + "carpenters_hammer"));
        }
        if (ConfigRegistry.enableChisel) {
            ModelLoader.setCustomModelResourceLocation(itemCarpentersChisel, 0, new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":" + "carpenters_chisel"));
        }
        /*
        if (enableTile) {
            itemCarpentersTile = new ItemCarpentersTile().setUnlocalizedName("itemCarpentersTile");
            GameRegistry.registerItem(itemCarpentersTile, "itemCarpentersTile");
        }
        if (BlockRegistry.enableDoor) {
            itemCarpentersDoor = new ItemCarpentersDoor().setUnlocalizedName("itemCarpentersDoor");
            GameRegistry.registerItem(itemCarpentersDoor, "itemCarpentersDoor");
        }
        if (BlockRegistry.enableBed) {
            itemCarpentersBed = new ItemCarpentersBed().setUnlocalizedName("itemCarpentersBed");
            GameRegistry.registerItem(itemCarpentersBed, "itemCarpentersBed");
        }*/
    }
    
}
