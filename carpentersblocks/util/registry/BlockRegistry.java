package carpentersblocks.util.registry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersBarrier;
import carpentersblocks.block.BlockCarpentersBed;
import carpentersblocks.block.BlockCarpentersBlock;
import carpentersblocks.block.BlockCarpentersButton;
import carpentersblocks.block.BlockCarpentersCollapsibleBlock;
import carpentersblocks.block.BlockCarpentersDaylightSensor;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.block.BlockCarpentersFlowerPot;
import carpentersblocks.block.BlockCarpentersGate;
import carpentersblocks.block.BlockCarpentersHatch;
import carpentersblocks.block.BlockCarpentersLadder;
import carpentersblocks.block.BlockCarpentersLever;
import carpentersblocks.block.BlockCarpentersPressurePlate;
import carpentersblocks.block.BlockCarpentersSafe;
import carpentersblocks.block.BlockCarpentersSlope;
import carpentersblocks.block.BlockCarpentersStairs;
import carpentersblocks.block.BlockCarpentersTorch;
import carpentersblocks.block.ItemBlockCarpentersSlope;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegistry {
    
    /* Blocks. */
    
    public static Block blockCarpentersBarrier;
    public static Block blockCarpentersBed;
    public static Block blockCarpentersBlock;
    public static Block blockCarpentersButton;
    public static Block blockCarpentersCollapsibleBlock;
    public static Block blockCarpentersDaylightSensor;
    public static Block blockCarpentersDoor;
    public static Block blockCarpentersFlowerPot;
    public static Block blockCarpentersGate;
    public static Block blockCarpentersHatch;
    public static Block blockCarpentersLadder;
    public static Block blockCarpentersLever;
    public static Block blockCarpentersPressurePlate;
    public static Block blockCarpentersSafe;
    public static Block blockCarpentersSlope;
    public static Block blockCarpentersStairs;
    public static Block blockCarpentersTorch;
    
    /* Block render IDs. */
    
    public static int carpentersBarrierRenderID;
    public static int carpentersBedRenderID;
    public static int carpentersBlockRenderID;
    public static int carpentersButtonRenderID;
    public static int carpentersCollapsibleBlockRenderID;
    public static int carpentersDaylightSensorRenderID;
    public static int carpentersDoorRenderID;
    public static int carpentersFlowerPotRenderID;
    public static int carpentersGateRenderID;
    public static int carpentersHatchRenderID;
    public static int carpentersLadderRenderID;
    public static int carpentersLeverRenderID;
    public static int carpentersPressurePlateRenderID;
    public static int carpentersSafeRenderID;
    public static int carpentersSlopeRenderID;
    public static int carpentersStairsRenderID;
    public static int carpentersTorchRenderID;
    
    /* Block states. */
    
    public static boolean enableBarrier          = true;
    public static boolean enableBed              = true;
    public static boolean enableBlock            = true;
    public static boolean enableButton           = true;
    public static boolean enableCollapsibleBlock = true;
    public static boolean enableDaylightSensor   = true;
    public static boolean enableDoor             = true;
    public static boolean enableFlowerPot        = true;
    public static boolean enableGate             = true;
    public static boolean enableHatch            = true;
    public static boolean enableLadder           = true;
    public static boolean enableLever            = true;
    public static boolean enablePressurePlate    = true;
    public static boolean enableSafe             = true;
    public static boolean enableSlope            = true;
    public static boolean enableStairs           = true;
    public static boolean enableTorch            = true;
    
    /* Block recipe quantities. */
    
    public static int recipeQuantityBarrier          = 4;
    public static int recipeQuantityBed              = 1;
    public static int recipeQuantityBlock            = 5;
    public static int recipeQuantityButton           = 1;
    public static int recipeQuantityCollapsibleBlock = 9;
    public static int recipeQuantityDaylightSensor   = 1;
    public static int recipeQuantityDoor             = 1;
    public static int recipeQuantityFlowerPot        = 1;
    public static int recipeQuantityGate             = 1;
    public static int recipeQuantityHatch            = 1;
    public static int recipeQuantityLadder           = 4;
    public static int recipeQuantityLever            = 1;
    public static int recipeQuantityPressurePlate    = 1;
    public static int recipeQuantitySafe             = 1;
    public static int recipeQuantitySlope            = 6;
    public static int recipeQuantityStairs           = 4;
    public static int recipeQuantityTorch            = 4;
    
    /**
     * Registers block IDs.
     */
    public static void initBlocks(FMLPreInitializationEvent event, Configuration config)
    {
        enableBarrier          = config.get("control",           "Enable Barrier",          enableBarrier).getBoolean(enableBarrier);
        enableBed              = config.get("control",               "Enable Bed",              enableBed).getBoolean(enableBed);
        enableBlock            = config.get("control",        "Enable Block/Slab",            enableBlock).getBoolean(enableBlock);
        enableButton           = config.get("control",            "Enable Button",           enableButton).getBoolean(enableButton);
        enableCollapsibleBlock = config.get("control", "Enable Collapsible Block", enableCollapsibleBlock).getBoolean(enableCollapsibleBlock);
        enableDaylightSensor   = config.get("control",   "Enable Daylight Sensor",   enableDaylightSensor).getBoolean(enableDaylightSensor);
        enableDoor             = config.get("control",              "Enable Door",             enableDoor).getBoolean(enableDoor);
        enableFlowerPot        = config.get("control",        "Enable Flower Pot",        enableFlowerPot).getBoolean(enableFlowerPot);
        enableGate             = config.get("control",              "Enable Gate",             enableGate).getBoolean(enableGate);
        enableHatch            = config.get("control",             "Enable Hatch",            enableHatch).getBoolean(enableHatch);
        enableLadder           = config.get("control",            "Enable Ladder",           enableLadder).getBoolean(enableLadder);
        enableLever            = config.get("control",             "Enable Lever",            enableLever).getBoolean(enableLever);
        enablePressurePlate    = config.get("control",    "Enable Pressure Plate",    enablePressurePlate).getBoolean(enablePressurePlate);
        enableSafe             = config.get("control",              "Enable Safe",             enableSafe).getBoolean(enableSafe);
        enableSlope            = config.get("control",             "Enable Slope",            enableSlope).getBoolean(enableSlope);
        enableStairs           = config.get("control",            "Enable Stairs",           enableStairs).getBoolean(enableStairs);
        enableTorch            = config.get("control",             "Enable Torch",            enableTorch).getBoolean(enableTorch);
        
        recipeQuantityBarrier          = config.get("recipe quantities",           "Barrier",          recipeQuantityBarrier).getInt(recipeQuantityBarrier);
        recipeQuantityBed              = config.get("recipe quantities",               "Bed",              recipeQuantityBed).getInt(recipeQuantityBed);
        recipeQuantityBlock            = config.get("recipe quantities",             "Block",            recipeQuantityBlock).getInt(recipeQuantityBlock);
        recipeQuantityButton           = config.get("recipe quantities",            "Button",           recipeQuantityButton).getInt(recipeQuantityButton);
        recipeQuantityCollapsibleBlock = config.get("recipe quantities", "Collapsible Block", recipeQuantityCollapsibleBlock).getInt(recipeQuantityCollapsibleBlock);
        recipeQuantityDaylightSensor   = config.get("recipe quantities",   "Daylight Sensor",   recipeQuantityDaylightSensor).getInt(recipeQuantityDaylightSensor);
        recipeQuantityDoor             = config.get("recipe quantities",              "Door",             recipeQuantityDoor).getInt(recipeQuantityDoor);
        recipeQuantityFlowerPot        = config.get("recipe quantities",        "Flower Pot",        recipeQuantityFlowerPot).getInt(recipeQuantityFlowerPot);
        recipeQuantityGate             = config.get("recipe quantities",              "Gate",             recipeQuantityGate).getInt(recipeQuantityGate);
        recipeQuantityHatch            = config.get("recipe quantities",             "Hatch",            recipeQuantityHatch).getInt(recipeQuantityHatch);
        recipeQuantityLadder           = config.get("recipe quantities",            "Ladder",           recipeQuantityLadder).getInt(recipeQuantityLadder);
        recipeQuantityLever            = config.get("recipe quantities",             "Lever",            recipeQuantityLever).getInt(recipeQuantityLever);
        recipeQuantityPressurePlate    = config.get("recipe quantities",    "Pressure Plate",    recipeQuantityPressurePlate).getInt(recipeQuantityPressurePlate);
        recipeQuantitySafe             = config.get("recipe quantities",              "Safe",             recipeQuantitySafe).getInt(recipeQuantitySafe);
        recipeQuantitySlope            = config.get("recipe quantities",             "Slope",            recipeQuantitySlope).getInt(recipeQuantitySlope);
        recipeQuantityStairs           = config.get("recipe quantities",            "Stairs",           recipeQuantityStairs).getInt(recipeQuantityStairs);
        recipeQuantityTorch            = config.get("recipe quantities",             "Torch",            recipeQuantityTorch).getInt(recipeQuantityTorch);
    }
    
    /**
     * Create and register blocks.
     */
    public static void registerBlocks()
    {
        
        /* Register the CarpentersBlock first since it's used in recipes. */
        
        if (enableBlock) {
            
            /* Create block. */
            
            blockCarpentersBlock = new BlockCarpentersBlock(Material.wood)
                .setBlockName("blockCarpentersBlock")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
                        
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersBlock, "blockCarpentersBlock");
            Blocks.fire.setFireInfo(blockCarpentersBlock, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBlock, recipeQuantityBlock), "XXX", "XYX", "XXX", 'X', "stickWood", 'Y', "plankWood"));
        
        }
        
        if (enableBarrier) {
            
            /* Create block. */
            
            blockCarpentersBarrier = new BlockCarpentersBarrier(Material.wood)
                .setBlockName("blockCarpentersBarrier")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
           
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersBarrier, "blockCarpentersBarrier");
            Blocks.fire.setFireInfo(blockCarpentersBarrier, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBarrier, recipeQuantityBarrier), " Y ", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        
        }
        
        if (enableBed) {
            
            /* Create block. */
            
            blockCarpentersBed = new BlockCarpentersBed(Material.wood)
                .setBlockName("blockCarpentersBed")
                .setHardness(0.4F)
                .setStepSound(BlockProperties.stepSound);          
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersBed, "blockCarpentersBed"); // This must be set to apply burn properties
            Blocks.fire.setFireInfo(blockCarpentersBed, 5, 20);
            
        }
        
        if (enableButton) {
            
            /* Create block. */
            
            blockCarpentersButton = new BlockCarpentersButton(Material.circuits)
                .setBlockName("blockCarpentersButton")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersButton, "blockCarpentersButton");
            Blocks.fire.setFireInfo(blockCarpentersButton, 5, 20);
                        
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersButton, recipeQuantityButton), new Object[] { "X", 'X', blockCarpentersBlock });
        
        }
        
        if (enableCollapsibleBlock) {
            
            /* Create block. */
            
            blockCarpentersCollapsibleBlock = new BlockCarpentersCollapsibleBlock(Material.wood)
                .setBlockName("blockCarpentersCollapsibleBlock")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersCollapsibleBlock, "blockCarpentersCollapsibleBlock");
            Blocks.fire.setFireInfo(blockCarpentersCollapsibleBlock, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersCollapsibleBlock, recipeQuantityCollapsibleBlock), new Object[] { "XXX", "XXX", "XXX", 'X', blockCarpentersBlock });
        
        }
        
        if (enableDaylightSensor) {
            
            /* Create block. */
            
            blockCarpentersDaylightSensor = new BlockCarpentersDaylightSensor(Material.wood)
                .setBlockName("blockCarpentersDaylightSensor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersDaylightSensor, "blockCarpentersDaylightSensor");
            Blocks.fire.setFireInfo(blockCarpentersDaylightSensor, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersDaylightSensor, recipeQuantityDaylightSensor), new Object[] { "WWW", "XYX", "ZZZ", 'W', Blocks.glass, 'X', Items.redstone, 'Y', new ItemStack(Items.dye, 1, 4), 'Z', blockCarpentersBlock });
        
        }
        
        if (enableDoor) {
            
            /* Create block. */
            
            blockCarpentersDoor = new BlockCarpentersDoor(Material.wood)
                .setBlockName("blockCarpentersDoor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersDoor, "blockCarpentersDoor"); // This must be set to apply burn properties
            Blocks.fire.setFireInfo(blockCarpentersDoor, 5, 20);
        
        }
        
        if (enableFlowerPot) {
            
            /* Create block. */
            
            blockCarpentersFlowerPot = new BlockCarpentersFlowerPot(Material.circuits)
                .setBlockName("blockCarpentersFlowerPot")
                .setHardness(0.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersFlowerPot, "blockCarpentersFlowerPot");
            Blocks.fire.setFireInfo(blockCarpentersFlowerPot, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersFlowerPot, recipeQuantityFlowerPot), new Object[] { "X X", " X ", 'X', blockCarpentersBlock });
        
        }
        
        if (enableGate) {
            
            /* Create block. */
            
            blockCarpentersGate = new BlockCarpentersGate(Material.wood)
                .setBlockName("blockCarpentersGate")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersGate, "blockCarpentersGate");
            Blocks.fire.setFireInfo(blockCarpentersGate, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersGate, recipeQuantityGate), "XYX", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        
        }
        
        if (enableHatch) {
            
            /* Create block. */
            
            blockCarpentersHatch = new BlockCarpentersHatch(Material.wood)
                .setBlockName("blockCarpentersHatch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersHatch, "blockCarpentersHatch");
            Blocks.fire.setFireInfo(blockCarpentersHatch, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersHatch, recipeQuantityHatch), new Object[] { "XXX", "XXX", 'X', blockCarpentersBlock });
        
        }
        
        if (enableLadder) {
            
            /* Create block. */
            
            blockCarpentersLadder = new BlockCarpentersLadder(Material.wood)
                .setBlockName("blockCarpentersLadder")
                .setHardness(0.2F)
                .setStepSound(Blocks.ladder.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);                
           
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersLadder, "blockCarpentersLadder");
            Blocks.fire.setFireInfo(blockCarpentersLadder, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersLadder, recipeQuantityLadder), new Object[] { "X X", "XXX", "X X", 'X', blockCarpentersBlock });
        
        }
        
        if (enableLever) {
            
            /* Create block. */
            
            blockCarpentersLever = new BlockCarpentersLever(Material.circuits)
                .setBlockName("blockCarpentersLever")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersLever, "blockCarpentersLever");
            Blocks.fire.setFireInfo(blockCarpentersLever, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersLever, recipeQuantityLever), "X", "Y", 'X', "stickWood", 'Y', blockCarpentersBlock));
        
        }
        
        if (enablePressurePlate) {
            
            /* Create block. */
            
            blockCarpentersPressurePlate = new BlockCarpentersPressurePlate(Material.wood)
                .setBlockName("blockCarpentersPressurePlate")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersPressurePlate, "blockCarpentersPressurePlate");
            Blocks.fire.setFireInfo(blockCarpentersPressurePlate, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersPressurePlate, recipeQuantityPressurePlate), new Object[] { "XX", 'X', blockCarpentersBlock });
        
        }
        
        if (enableSafe) {
            
            /* Create block. */
            
            blockCarpentersSafe = new BlockCarpentersSafe(Material.wood)
                .setBlockName("blockCarpentersSafe")
                .setHardness(2.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
           
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersSafe, "blockCarpentersSafe");
            Blocks.fire.setFireInfo(blockCarpentersSafe, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersSafe, recipeQuantitySafe), new Object[] { "XXX", "XYX", "XZX", 'X', blockCarpentersBlock, 'Y', Blocks.iron_block, 'Z', Items.redstone });
        
        }
        
        if (enableSlope) {
            
            /* Create block. */
            
            blockCarpentersSlope = new BlockCarpentersSlope(Material.wood)
                .setBlockName("blockCarpentersSlope")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersSlope, ItemBlockCarpentersSlope.class, "blockCarpentersSlope");
            Blocks.fire.setFireInfo(blockCarpentersSlope, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersSlope, recipeQuantitySlope), "  X", " XY", "XYY", 'X', "stickWood", 'Y', blockCarpentersBlock));
        
        }
        
        if (enableStairs) {
            
            /* Create block. */
            
            blockCarpentersStairs = new BlockCarpentersStairs(Material.wood)
                .setBlockName("blockCarpentersStairs")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersStairs, "blockCarpentersStairs");
            Blocks.fire.setFireInfo(blockCarpentersStairs, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersStairs, recipeQuantityStairs), new Object[] { "  X", " XX", "XXX", 'X', blockCarpentersBlock });
        
        }
                
        if (enableTorch) {
            
            /* Create block. */
            
            blockCarpentersTorch = new BlockCarpentersTorch(Material.circuits)
                .setBlockName("blockCarpentersTorch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            
            /* Register block. */
            
            GameRegistry.registerBlock(blockCarpentersTorch, "blockCarpentersTorch");
            Blocks.fire.setFireInfo(blockCarpentersTorch, 5, 20);
            
            /* Add recipe(s). */
            
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 0), 'Y', blockCarpentersBlock });
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', blockCarpentersBlock });
        
        }
    }
    
}
