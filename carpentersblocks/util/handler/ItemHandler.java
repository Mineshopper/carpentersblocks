package carpentersblocks.util.handler;

import carpentersblocks.block.BlockCarpentersBed;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.item.ItemCarpentersBed;
import carpentersblocks.item.ItemCarpentersChisel;
import carpentersblocks.item.ItemCarpentersDoor;
import carpentersblocks.item.ItemCarpentersHammer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemHandler
{

    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;

    public static int itemCarpentersHammerID;
    public static int itemCarpentersChiselID;
    public static int itemCarpentersDoorID;
    public static int itemCarpentersBedID;
    
    public static boolean enableChisel = true;
    public static boolean itemCarpentersToolsDamageable = true;
    
    /**
     * Registers item IDs.
     */
    public static void initItems(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
    	
        int baseItemID = 5401;
        
        enableChisel = config.get("tools", "Enable Chisel", enableChisel).getBoolean(enableChisel);
        itemCarpentersToolsDamageable = config.get("tools", "Tools Damageable", itemCarpentersToolsDamageable).getBoolean(itemCarpentersToolsDamageable);
                
        itemCarpentersHammerID = config.getItem("Hammer", baseItemID++).getInt(baseItemID);
        itemCarpentersChiselID = config.getItem("Chisel", baseItemID++).getInt(baseItemID);
        itemCarpentersDoorID = config.getItem("Door", baseItemID++).getInt(baseItemID);
        itemCarpentersBedID = config.getItem("Bed", baseItemID++).getInt(baseItemID);
        
        config.save();
    }
    
    /**
     * Registers items.
     */
    public static void registerItems()
    {
    	itemCarpentersHammer = new ItemCarpentersHammer(itemCarpentersHammerID - 256);
		GameRegistry.registerItem(itemCarpentersHammer, "itemCarpentersHammer");
    	GameRegistry.addRecipe(new ItemStack(itemCarpentersHammer, 1), new Object[] { "XX ", " YX", " Y ", 'X', Item.ingotIron, 'Y', BlockHandler.blockCarpentersBlock });
    
    	if (enableChisel) {
        	itemCarpentersChisel = new ItemCarpentersChisel(itemCarpentersChiselID - 256);
    		GameRegistry.registerItem(itemCarpentersChisel, "itemCarpentersChisel");
        	GameRegistry.addRecipe(new ItemStack(itemCarpentersChisel, 1), new Object[] { "X", "Y", 'X', Item.ingotIron, 'Y', BlockHandler.blockCarpentersBlock });
    	}
    	
    	if (BlockHandler.enableDoor) {
        	itemCarpentersDoor = new ItemCarpentersDoor(itemCarpentersDoorID - 256);
    		GameRegistry.registerItem(itemCarpentersDoor, "itemCarpentersDoor");
    		GameRegistry.addRecipe(new ItemStack(itemCarpentersDoor, BlockHandler.recipeQuantityDoor), new Object[] {"XX", "XX", "XX", 'X', BlockHandler.blockCarpentersBlock});
    	}
    	
    	if (BlockHandler.enableBed) {
        	itemCarpentersBed = new ItemCarpentersBed(itemCarpentersBedID - 256);
        	GameRegistry.registerItem(itemCarpentersBed, "itemCarpentersBed");
    		GameRegistry.addRecipe(new ItemStack(itemCarpentersBed, BlockHandler.recipeQuantityBed), new Object[] {"XXX", "YYY", 'X', Block.cloth, 'Y', BlockHandler.blockCarpentersBlock});
    	}
    }
	
}
