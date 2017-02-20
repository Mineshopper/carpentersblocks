package com.carpentersblocks.util.registry;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.item.ItemCarpentersChisel;
import com.carpentersblocks.item.ItemCarpentersHammer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ItemRegistry {

    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;
    public static Item itemCarpentersTile;

    public static boolean enableHammer                  = true;
    public static boolean enableChisel                  = true;
    public static boolean enableTile                    = true;
    public static int     itemCarpentersToolsUses       = 400;
    public static boolean itemCarpentersToolsDamageable = true;

    public static double itemHammerDamageChanceFromSlopes      = 0.75D;
    public static double itemHammerDamageChanceFromStairs      = 1.0D;
    public static double itemHammerDamageChanceFromCollapsible = 0.2D;

    private static int recipeQuantityTile = 12;

    /**
     * Registers item IDs.
     */
    public static void preInit(FMLPreInitializationEvent event, Configuration config) {
        enableHammer                  = config.get("items",            "Enable Hammer",                  enableHammer).getBoolean(enableHammer);
        enableChisel                  = config.get("items",            "Enable Chisel",                  enableChisel).getBoolean(enableChisel);
        enableTile                    = config.get("items",              "Enable Tile",                    enableTile).getBoolean(enableTile);
        itemCarpentersToolsUses       = config.get("items",        "Vanilla Tool Uses",       itemCarpentersToolsUses).getInt(itemCarpentersToolsUses);
        itemCarpentersToolsDamageable = config.get("items", "Vanilla Tools Damageable", itemCarpentersToolsDamageable).getBoolean(itemCarpentersToolsDamageable);

        recipeQuantityTile = config.get("recipe quantities", "Tile", recipeQuantityTile).getInt(recipeQuantityTile);

        itemHammerDamageChanceFromSlopes      = config.get("items",      "itemHammerDamageChanceFromSlopes",      itemHammerDamageChanceFromSlopes).getDouble(     itemHammerDamageChanceFromSlopes);
        itemHammerDamageChanceFromStairs      = config.get("items",      "itemHammerDamageChanceFromStairs",      itemHammerDamageChanceFromStairs).getDouble(     itemHammerDamageChanceFromStairs);
        itemHammerDamageChanceFromCollapsible = config.get("items", "itemHammerDamageChanceFromCollapsible", itemHammerDamageChanceFromCollapsible).getDouble(itemHammerDamageChanceFromCollapsible);

        // Register items
        
        if (enableHammer) {
            itemCarpentersHammer = new ItemCarpentersHammer()
            		.setRegistryName("carpenters_hammer")
            		.setUnlocalizedName("itemCarpentersHammer")
            		.setCreativeTab(CarpentersBlocks.CREATIVE_TAB)
            		.setMaxStackSize(1);
            GameRegistry.register(itemCarpentersHammer);
            if (itemCarpentersToolsDamageable) {
            	itemCarpentersHammer.setMaxDamage(itemCarpentersToolsUses);
            }
            if (Side.CLIENT.equals(event.getSide())) {
            	ModelLoader.setCustomModelResourceLocation(itemCarpentersHammer, 0, new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":" + "carpenters_hammer"));
            }
        }
        if (enableChisel) {
            itemCarpentersChisel = new ItemCarpentersChisel()
            		.setRegistryName("carpenters_chisel")
            		.setUnlocalizedName("itemCarpentersChisel")
            		.setCreativeTab(CarpentersBlocks.CREATIVE_TAB)
            		.setMaxStackSize(1);
            GameRegistry.register(itemCarpentersChisel);
            if (itemCarpentersToolsDamageable) {
            	itemCarpentersChisel.setMaxDamage(itemCarpentersToolsUses);
            }
            if (Side.CLIENT.equals(event.getSide())) {
            	ModelLoader.setCustomModelResourceLocation(itemCarpentersChisel, 0, new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":" + "carpenters_chisel"));
            }
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

    public static void init(FMLInitializationEvent event) {
/*        if (enableHammer) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersHammer, 1), new Object[] { "XX ", " YX", " Y ", 'X', Items.iron_ingot, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (enableChisel) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersChisel, 1), new Object[] { "X", "Y", 'X', Items.iron_ingot, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (enableTile) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersTile, recipeQuantityTile), new Object[] { "XXX", "YYY", 'X', Blocks.hardened_clay, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (BlockRegistry.enableDoor) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersDoor, BlockRegistry.recipeQuantityDoor), new Object[] { "XX", "XX", "XX", 'X', BlockRegistry.blockCarpentersBlock });
        }
        if (BlockRegistry.enableBed) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersBed, BlockRegistry.recipeQuantityBed), new Object[] { "XXX", "YYY", 'X', Blocks.wool, 'Y', BlockRegistry.blockCarpentersBlock });
        }*/
    }

}
