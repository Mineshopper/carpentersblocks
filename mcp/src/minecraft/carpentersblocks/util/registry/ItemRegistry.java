package carpentersblocks.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import carpentersblocks.item.ItemCarpentersBed;
import carpentersblocks.item.ItemCarpentersChisel;
import carpentersblocks.item.ItemCarpentersDoor;
import carpentersblocks.item.ItemCarpentersHammer;
import carpentersblocks.item.ItemCarpentersTile;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegistry {

    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;
    public static Item itemCarpentersTile;

    public static int itemCarpentersHammerID;
    public static int itemCarpentersChiselID;
    public static int itemCarpentersDoorID;
    public static int itemCarpentersBedID;
    public static int itemCarpentersTileID;

    public static boolean enableHammer                  = true;
    public static boolean enableChisel                  = true;
    public static int     itemCarpentersToolsUses       = 400;
    public static boolean itemCarpentersToolsDamageable = true;

    public static double itemHammerDamageChanceFromSlopes      = 0.75D;
    public static double itemHammerDamageChanceFromStairs      = 1.0D;
    public static double itemHammerDamageChanceFromCollapsible = 0.2D;

    private static int recipeQuantityTile = 12;

    /**
     * Registers item IDs.
     */
    public static void preInit(FMLPreInitializationEvent event, Configuration config)
    {
        int baseItemID = 5401;

        enableHammer                  = config.get("items",            "Enable Hammer",                  enableHammer).getBoolean(enableHammer);
        enableChisel                  = config.get("items",            "Enable Chisel",                  enableChisel).getBoolean(enableChisel);
        itemCarpentersToolsUses       = config.get("items",        "Vanilla Tool Uses",       itemCarpentersToolsUses).getInt(itemCarpentersToolsUses);
        itemCarpentersToolsDamageable = config.get("items", "Vanilla Tools Damageable", itemCarpentersToolsDamageable).getBoolean(itemCarpentersToolsDamageable);

        itemCarpentersHammerID        = config.getItem("Hammer", baseItemID++).getInt(baseItemID);
        itemCarpentersChiselID        = config.getItem("Chisel", baseItemID++).getInt(baseItemID);
        itemCarpentersDoorID          = config.getItem(  "Door", baseItemID++).getInt(baseItemID);
        itemCarpentersBedID           = config.getItem(   "Bed", baseItemID++).getInt(baseItemID);
        itemCarpentersTileID          = config.getItem(  "Tile", baseItemID++).getInt(baseItemID);

        itemHammerDamageChanceFromSlopes      = config.get("items",      "itemHammerDamageChanceFromSlopes",      itemHammerDamageChanceFromSlopes).getDouble(     itemHammerDamageChanceFromSlopes);
        itemHammerDamageChanceFromStairs      = config.get("items",      "itemHammerDamageChanceFromStairs",      itemHammerDamageChanceFromStairs).getDouble(     itemHammerDamageChanceFromStairs);
        itemHammerDamageChanceFromCollapsible = config.get("items", "itemHammerDamageChanceFromCollapsible", itemHammerDamageChanceFromCollapsible).getDouble(itemHammerDamageChanceFromCollapsible);

        registerItems();
    }

    public static void init(FMLInitializationEvent event)
    {
        registerRecipes();
    }

    private static void registerItems()
    {
        if (enableHammer) {
            itemCarpentersHammer = new ItemCarpentersHammer(itemCarpentersHammerID - 256).setUnlocalizedName("itemCarpentersHammer");
            GameRegistry.registerItem(itemCarpentersHammer, "itemCarpentersHammer");
        }
        if (enableChisel) {
            itemCarpentersChisel = new ItemCarpentersChisel(itemCarpentersChiselID - 256).setUnlocalizedName("itemCarpentersChisel");
            GameRegistry.registerItem(itemCarpentersChisel, "itemCarpentersChisel");
        }
        if (BlockRegistry.enableDoor) {
            itemCarpentersDoor = new ItemCarpentersDoor(itemCarpentersDoorID - 256).setUnlocalizedName("itemCarpentersDoor");
            GameRegistry.registerItem(itemCarpentersDoor, "itemCarpentersDoor");
        }
        if (BlockRegistry.enableBed) {
            itemCarpentersBed = new ItemCarpentersBed(itemCarpentersBedID - 256).setUnlocalizedName("itemCarpentersBed");
            GameRegistry.registerItem(itemCarpentersBed, "itemCarpentersBed");
        }
        if (FeatureRegistry.enableTile) {
            itemCarpentersTile = new ItemCarpentersTile(itemCarpentersTileID - 256).setUnlocalizedName("itemCarpentersTile");
            GameRegistry.registerItem(itemCarpentersTile, "itemCarpentersTile");
        }
    }

    private static void registerRecipes()
    {
        if (enableHammer) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersHammer, 1), new Object[] { "XX ", " YX", " Y ", 'X', Item.ingotIron, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (enableChisel) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersChisel, 1), new Object[] { "X", "Y", 'X', Item.ingotIron, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (BlockRegistry.enableDoor) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersDoor, BlockRegistry.recipeQuantityDoor), new Object[] { "XX", "XX", "XX", 'X', BlockRegistry.blockCarpentersBlock });
        }
        if (BlockRegistry.enableBed) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersBed, BlockRegistry.recipeQuantityBed), new Object[] { "XXX", "YYY", 'X', Block.cloth, 'Y', BlockRegistry.blockCarpentersBlock });
        }
        if (FeatureRegistry.enableTile) {
            GameRegistry.addRecipe(new ItemStack(itemCarpentersTile, recipeQuantityTile), new Object[] { "XXX", "YYY", 'X', Block.hardenedClay, 'Y', BlockRegistry.blockCarpentersBlock });
        }
    }

}
