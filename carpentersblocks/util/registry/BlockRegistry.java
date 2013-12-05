package carpentersblocks.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import carpentersblocks.block.BlockCarpentersBarrier;
import carpentersblocks.block.BlockCarpentersBed;
import carpentersblocks.block.BlockCarpentersBlock;
import carpentersblocks.block.BlockCarpentersButton;
import carpentersblocks.block.BlockCarpentersCollapsibleBlock;
import carpentersblocks.block.BlockCarpentersDaylightSensor;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.block.BlockCarpentersGate;
import carpentersblocks.block.BlockCarpentersHatch;
import carpentersblocks.block.BlockCarpentersLadder;
import carpentersblocks.block.BlockCarpentersLever;
import carpentersblocks.block.BlockCarpentersPressurePlate;
import carpentersblocks.block.BlockCarpentersSlope;
import carpentersblocks.block.BlockCarpentersStairs;
import carpentersblocks.block.BlockCarpentersTorch;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegistry
{
	
	// References
    public static Block	blockCarpentersSlope;
    public static Block	blockCarpentersStairs;
    public static Block	blockCarpentersBarrier;
    public static Block	blockCarpentersGate;
    public static Block	blockCarpentersBlock;
    public static Block	blockCarpentersButton;
    public static Block	blockCarpentersLever;
    public static Block	blockCarpentersPressurePlate;
    public static Block	blockCarpentersDaylightSensor;
    public static Block	blockCarpentersHatch;
    public static Block	blockCarpentersDoor;
    public static Block	blockCarpentersBed;
    public static Block	blockCarpentersLadder;
    public static Block blockCarpentersCollapsibleBlock;
    public static Block blockCarpentersTorch;
    
    // Render IDs
    public static int carpentersSlopeRenderID;
    public static int carpentersStairsRenderID;
    public static int carpentersBarrierRenderID;
    public static int carpentersGateRenderID;
    public static int carpentersBlockRenderID;
    public static int carpentersButtonRenderID;
    public static int carpentersLeverRenderID;
    public static int carpentersPressurePlateRenderID;
    public static int carpentersDaylightSensorRenderID;
    public static int carpentersHatchRenderID;
    public static int carpentersDoorRenderID;
    public static int carpentersBedRenderID;
    public static int carpentersLadderRenderID;
    public static int carpentersCollapsibleBlockRenderID;
    public static int carpentersTorchRenderID;
    
    // Block IDs
    public static int blockCarpentersSlopeID;
    public static int blockCarpentersStairsID;
    public static int blockCarpentersBarrierID;
    public static int blockCarpentersGateID;
    public static int blockCarpentersBlockID;
    public static int blockCarpentersButtonID;
    public static int blockCarpentersLeverID;
    public static int blockCarpentersPressurePlateID;
    public static int blockCarpentersDaylightSensorID;
    public static int blockCarpentersHatchID;
    public static int blockCarpentersDoorID;
    public static int blockCarpentersBedID;
    public static int blockCarpentersLadderID;
    public static int blockCarpentersCollapsibleBlockID;
    public static int blockCarpentersTorchID;
    
    // Blocks enabled state
    public static boolean enableSlope = true;
    public static boolean enableStairs = true;
    public static boolean enableBarrier = true;
    public static boolean enableGate = true;
    public static boolean enableBlock = true;
    public static boolean enableButton = true;
    public static boolean enableLever = true;
    public static boolean enablePressurePlate = true;
    public static boolean enableDaylightSensor = true;
    public static boolean enableHatch = true;
    public static boolean enableDoor = true;
    public static boolean enableBed = true;
    public static boolean enableLadder = true;
    public static boolean enableCollapsibleBlock = true;
    public static boolean enableTorch = true;
    
    // Recipe quantities
    public static int recipeQuantitySlope = 4;
    public static int recipeQuantityStairs = 4;
    public static int recipeQuantityBarrier = 2;
    public static int recipeQuantityGate = 1;
    public static int recipeQuantityBlock = 5;
    public static int recipeQuantityButton = 1;
    public static int recipeQuantityLever = 1;
    public static int recipeQuantityPressurePlate = 1;
    public static int recipeQuantityDaylightSensor = 1;
    public static int recipeQuantityHatch = 1;
    public static int recipeQuantityDoor = 1;
    public static int recipeQuantityBed = 1;
    public static int recipeQuantityLadder = 4;
    public static int recipeQuantityCollapsibleBlock = 9;
    public static int recipeQuantityTorch = 4;
    
    /**
     * Registers block IDs.
     */
    public static void initBlocks(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
    	
        int baseBlockID = 2401;

        enableSlope = config.get("control", "Enable Slope", enableSlope).getBoolean(enableSlope);
        enableStairs = config.get("control", "Enable Stairs", enableStairs).getBoolean(enableStairs);
        enableBarrier = config.get("control", "Enable Barrier", enableBarrier).getBoolean(enableBarrier);
        enableGate = config.get("control", "Enable Gate", enableGate).getBoolean(enableGate);
        enableBlock = config.get("control", "Enable Block/Slab", enableBlock).getBoolean(enableBlock);
        enableButton = config.get("control", "Enable Button", enableButton).getBoolean(enableButton);
        enableLever = config.get("control", "Enable Lever", enableLever).getBoolean(enableLever);
        enablePressurePlate = config.get("control", "Enable Pressure Plate", enablePressurePlate).getBoolean(enablePressurePlate);
        enableDaylightSensor = config.get("control", "Enable Daylight Sensor", enableDaylightSensor).getBoolean(enableDaylightSensor);
        enableHatch = config.get("control", "Enable Hatch", enableHatch).getBoolean(enableHatch);
        enableDoor = config.get("control", "Enable Door", enableDoor).getBoolean(enableDoor);
        enableBed = config.get("control", "Enable Bed", enableBed).getBoolean(enableBed);
        enableLadder = config.get("control", "Enable Ladder", enableLadder).getBoolean(enableLadder);
        enableCollapsibleBlock = config.get("control", "Enable Collapsible Block", enableCollapsibleBlock).getBoolean(enableCollapsibleBlock);
        enableTorch = config.get("control", "Enable Torch", enableTorch).getBoolean(enableTorch);
        
	    blockCarpentersSlopeID = config.getBlock("Slope", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersStairsID = config.getBlock("Stairs", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersBarrierID = config.getBlock("Barrier", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersGateID = config.getBlock("Gate", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersBlockID = config.getBlock("Block", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersButtonID = config.getBlock("Button", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersLeverID = config.getBlock("Lever", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersPressurePlateID = config.getBlock("PressurePlate", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersDaylightSensorID = config.getBlock("DaylightSensor", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersHatchID = config.getBlock("Hatch", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersDoorID = config.getBlock("Door", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersBedID = config.getBlock("Bed", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersLadderID = config.getBlock("Ladder", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersCollapsibleBlockID = config.getBlock("Collapsible Block", baseBlockID++).getInt(baseBlockID);
	    blockCarpentersTorchID = config.getBlock("Torch", baseBlockID++).getInt(baseBlockID);
	    
	    recipeQuantitySlope = config.get("recipe quantities", "Slope", recipeQuantitySlope).getInt(recipeQuantitySlope);
        recipeQuantityStairs = config.get("recipe quantities", "Stairs", recipeQuantityStairs).getInt(recipeQuantityStairs);
        recipeQuantityBarrier = config.get("recipe quantities", "Barrier", recipeQuantityBarrier).getInt(recipeQuantityBarrier);
        recipeQuantityGate = config.get("recipe quantities", "Gate", recipeQuantityGate).getInt(recipeQuantityGate);
        recipeQuantityBlock = config.get("recipe quantities", "Block", recipeQuantityBlock).getInt(recipeQuantityBlock);
        recipeQuantityButton = config.get("recipe quantities", "Button", recipeQuantityButton).getInt(recipeQuantityButton);
        recipeQuantityLever = config.get("recipe quantities", "Lever", recipeQuantityLever).getInt(recipeQuantityLever);
        recipeQuantityPressurePlate = config.get("recipe quantities", "Pressure Plate", recipeQuantityPressurePlate).getInt(recipeQuantityPressurePlate);
        recipeQuantityDaylightSensor = config.get("recipe quantities", "Daylight Sensor", recipeQuantityDaylightSensor).getInt(recipeQuantityDaylightSensor);
        recipeQuantityHatch = config.get("recipe quantities", "Hatch", recipeQuantityHatch).getInt(recipeQuantityHatch);
        recipeQuantityDoor = config.get("recipe quantities", "Door", recipeQuantityDoor).getInt(recipeQuantityDoor);
        recipeQuantityBed = config.get("recipe quantities", "Bed", recipeQuantityBed).getInt(recipeQuantityBed);
        recipeQuantityLadder = config.get("recipe quantities", "Ladder", recipeQuantityLadder).getInt(recipeQuantityLadder);
        recipeQuantityCollapsibleBlock = config.get("recipe quantities", "Collapsible Block", recipeQuantityCollapsibleBlock).getInt(recipeQuantityCollapsibleBlock);
        recipeQuantityTorch = config.get("recipe quantities", "Torch", recipeQuantityTorch).getInt(recipeQuantityTorch);
        
        config.save();
    }
    
    /**
     * Create and register blocks.
     */
    public static void registerBlocks()
    {
    	if (enableBarrier) {
    		blockCarpentersBarrier = (new BlockCarpentersBarrier(blockCarpentersBarrierID));
    		GameRegistry.registerBlock(blockCarpentersBarrier, "blockCarpentersBarrier");
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBarrier, recipeQuantityBarrier), "X X", "XXX", 'X', "stickWood"));
    	}
    	
    	if (enableBed) {
        	blockCarpentersBed = (new BlockCarpentersBed(blockCarpentersBedID));
        }
    	
    	if (enableBlock) {
    		blockCarpentersBlock = (new BlockCarpentersBlock(blockCarpentersBlockID));
    		GameRegistry.registerBlock(blockCarpentersBlock, "blockCarpentersBlock");
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBlock, recipeQuantityBlock), "XXX", "XYX", "XXX", 'X', "stickWood", 'Y', "plankWood"));
    	}
    	
    	if (enableButton) {
    		blockCarpentersButton = (new BlockCarpentersButton(blockCarpentersButtonID));
    		GameRegistry.registerBlock(blockCarpentersButton, "blockCarpentersButton");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersButton, recipeQuantityButton), new Object[] {"X", 'X', blockCarpentersBlock});
    	}
    	
    	if (enableDaylightSensor) {
    		blockCarpentersDaylightSensor = (new BlockCarpentersDaylightSensor(blockCarpentersDaylightSensorID));
    		GameRegistry.registerBlock(blockCarpentersDaylightSensor, "blockCarpentersDaylightSensor");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersDaylightSensor, recipeQuantityDaylightSensor), new Object[] {"XXX", "YYY", "ZZZ", 'X', Block.glass, 'Y', Item.netherQuartz, 'Z', blockCarpentersBlock});
    	}
    	
    	if (enableDoor) {
    		blockCarpentersDoor = (new BlockCarpentersDoor(blockCarpentersDoorID));
        }
    	
    	if (enableGate) {
    		blockCarpentersGate = (new BlockCarpentersGate(blockCarpentersGateID));
    		GameRegistry.registerBlock(blockCarpentersGate, "blockCarpentersGate");
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersGate, recipeQuantityGate), "XYX", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
    	}
    	
    	if (enableHatch) {
    		blockCarpentersHatch = (new BlockCarpentersHatch(blockCarpentersHatchID));
    		GameRegistry.registerBlock(blockCarpentersHatch, "blockCarpentersHatch");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersHatch, recipeQuantityHatch), new Object[] {"XXX", "XXX", 'X', blockCarpentersBlock});
    	}
    	
    	if (enableLadder) {
    		blockCarpentersLadder = (new BlockCarpentersLadder(blockCarpentersLadderID));
    		GameRegistry.registerBlock(blockCarpentersLadder, "blockCarpentersLadder");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersLadder, recipeQuantityLadder), new Object[] {"X X", "XXX", "X X", 'X', blockCarpentersBlock});
    	}

    	if (enableLever) {
    		blockCarpentersLever = (new BlockCarpentersLever(blockCarpentersLeverID));
    		GameRegistry.registerBlock(blockCarpentersLever, "blockCarpentersLever");
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersLever, recipeQuantityLever), "X", "Y", 'X', "stickWood", 'Y', blockCarpentersBlock));
    	}

    	if (enablePressurePlate) {
    		blockCarpentersPressurePlate = (new BlockCarpentersPressurePlate(blockCarpentersPressurePlateID));
    		GameRegistry.registerBlock(blockCarpentersPressurePlate, "blockCarpentersPressurePlate");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersPressurePlate, recipeQuantityPressurePlate), new Object[] {"XX", 'X', blockCarpentersBlock});
    	}
    	
    	if (enableSlope) {
    		blockCarpentersSlope = (new BlockCarpentersSlope(blockCarpentersSlopeID));
    		GameRegistry.registerBlock(blockCarpentersSlope, "blockCarpentersSlope");
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersSlope, recipeQuantitySlope), "  X", " XY", "XYY", 'X', "stickWood", 'Y', "plankWood"));
    	}

    	if (enableStairs) {
    		blockCarpentersStairs = (new BlockCarpentersStairs(blockCarpentersStairsID));
    		GameRegistry.registerBlock(blockCarpentersStairs, "blockCarpentersStairs");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersStairs, recipeQuantityStairs), new Object[] {"  X", " XX", "XXX", 'X', blockCarpentersBlock});
    	}
    	
    	if (enableCollapsibleBlock) {
    		blockCarpentersCollapsibleBlock = (new BlockCarpentersCollapsibleBlock(blockCarpentersCollapsibleBlockID));
    		GameRegistry.registerBlock(blockCarpentersCollapsibleBlock, "blockCarpentersCollapsibleBlock");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersCollapsibleBlock, recipeQuantityCollapsibleBlock), new Object[] {"XXX", "XXX", "XXX", 'X', blockCarpentersBlock});
    	}
    	
    	if (enableTorch) {
    		blockCarpentersTorch = (new BlockCarpentersTorch(blockCarpentersTorchID));
    		GameRegistry.registerBlock(blockCarpentersTorch, "blockCarpentersTorch");
    		GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] {"X", "Y", 'X', Item.coal, 'Y', blockCarpentersBlock});
    	}
    }
	
}
