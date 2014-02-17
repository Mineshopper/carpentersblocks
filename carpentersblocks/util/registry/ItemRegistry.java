package carpentersblocks.util.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.item.ItemCarpentersBed;
import carpentersblocks.item.ItemCarpentersChisel;
import carpentersblocks.item.ItemCarpentersDoor;
import carpentersblocks.item.ItemCarpentersHammer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegistry {
    
    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;
    
    public static boolean enableHammer                  = true;
    public static boolean enableChisel                  = true;
    public static int     itemCarpentersToolsUses       = 400;
    public static boolean itemCarpentersToolsDamageable = true;
    
    public static double itemHammerDamageChanceFromSlopes      = 0.75D;
    public static double itemHammerDamageChanceFromStairs      = 1.0D;
    public static double itemHammerDamageChanceFromCollapsible = 0.2D;
    
    /**
     * Registers item IDs.
     */
    public static void initItems(FMLPreInitializationEvent event, Configuration config)
    {
        enableHammer                  = config.get("tools",            "Enable Hammer",                  enableHammer).getBoolean(enableHammer);
        enableChisel                  = config.get("tools",            "Enable Chisel",                  enableChisel).getBoolean(enableChisel);
        itemCarpentersToolsUses       = config.get("tools",       "Vanilla Tools Uses",       itemCarpentersToolsUses).getInt(itemCarpentersToolsUses);
        itemCarpentersToolsDamageable = config.get("tools", "Vanilla Tools Damageable", itemCarpentersToolsDamageable).getBoolean(itemCarpentersToolsDamageable);
        
        itemHammerDamageChanceFromSlopes      = config.get("chance to damage hammer when interacting with block",      "itemHammerDamageChanceFromSlopes",      itemHammerDamageChanceFromSlopes).getDouble(     itemHammerDamageChanceFromSlopes);
        itemHammerDamageChanceFromStairs      = config.get("chance to damage hammer when interacting with block",      "itemHammerDamageChanceFromStairs",      itemHammerDamageChanceFromStairs).getDouble(     itemHammerDamageChanceFromStairs);
        itemHammerDamageChanceFromCollapsible = config.get("chance to damage hammer when interacting with block", "itemHammerDamageChanceFromCollapsible", itemHammerDamageChanceFromCollapsible).getDouble(itemHammerDamageChanceFromCollapsible);
    }
    
    /**
     * Registers items.
     */
    public static void registerItems()
    {
        if (enableHammer) {
            itemCarpentersHammer = new ItemCarpentersHammer().setUnlocalizedName("itemCarpentersHammer");
            GameRegistry.registerItem(itemCarpentersHammer, "itemCarpentersHammer");
            GameRegistry.addRecipe(new ItemStack(itemCarpentersHammer, 1), new Object[] { "XX ", " YX", " Y ", 'X', Items.iron_ingot, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        
        if (enableChisel) {
            itemCarpentersChisel = new ItemCarpentersChisel().setUnlocalizedName("itemCarpentersChisel");
            GameRegistry.registerItem(itemCarpentersChisel, "itemCarpentersChisel");
            GameRegistry.addRecipe(new ItemStack(itemCarpentersChisel, 1), new Object[] { "X", "Y", 'X', Items.iron_ingot, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        
        if (BlockRegistry.enableDoor) {
            itemCarpentersDoor = new ItemCarpentersDoor().setUnlocalizedName("itemCarpentersDoor");
            GameRegistry.registerItem(itemCarpentersDoor, "itemCarpentersDoor");
            GameRegistry.addRecipe(new ItemStack(itemCarpentersDoor, BlockRegistry.recipeQuantityDoor), new Object[] { "XX", "XX", "XX", 'X', BlockRegistry.blockCarpentersBlock });
        }
        
        if (BlockRegistry.enableBed) {
            itemCarpentersBed = new ItemCarpentersBed().setUnlocalizedName("itemCarpentersBed");
            GameRegistry.registerItem(itemCarpentersBed, "itemCarpentersBed");
            GameRegistry.addRecipe(new ItemStack(itemCarpentersBed, BlockRegistry.recipeQuantityBed), new Object[] { "XXX", "YYY", 'X', Blocks.wool, 'Y', BlockRegistry.blockCarpentersBlock });
        }
    }
    
}
