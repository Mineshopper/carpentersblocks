package carpentersblocks.util.registry;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWood;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSnow;
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
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegistry {
    
    /* Blocks. */
    
    public static Block blockCarpentersSlope;
    public static Block blockCarpentersStairs;
    public static Block blockCarpentersBarrier;
    public static Block blockCarpentersGate;
    public static Block blockCarpentersBlock;
    public static Block blockCarpentersButton;
    public static Block blockCarpentersLever;
    public static Block blockCarpentersPressurePlate;
    public static Block blockCarpentersDaylightSensor;
    public static Block blockCarpentersHatch;
    public static Block blockCarpentersDoor;
    public static Block blockCarpentersBed;
    public static Block blockCarpentersLadder;
    public static Block blockCarpentersCollapsibleBlock;
    public static Block blockCarpentersTorch;
    public static Block blockCarpentersSafe;
    public static Block blockCarpentersFlowerPot;
    
    /* Block render IDs. */
    
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
    public static int carpentersSafeRenderID;
    public static int carpentersFlowerPotRenderID;
    
    /* Block states. */
    
    public static boolean enableSlope            = true;
    public static boolean enableStairs           = true;
    public static boolean enableBarrier          = true;
    public static boolean enableGate             = true;
    public static boolean enableBlock            = true;
    public static boolean enableButton           = true;
    public static boolean enableLever            = true;
    public static boolean enablePressurePlate    = true;
    public static boolean enableDaylightSensor   = true;
    public static boolean enableHatch            = true;
    public static boolean enableDoor             = true;
    public static boolean enableBed              = true;
    public static boolean enableLadder           = true;
    public static boolean enableCollapsibleBlock = true;
    public static boolean enableTorch            = true;
    public static boolean enableSafe             = true;
    public static boolean enableFlowerPot        = true;
    
    /* Block recipe quantities. */
    
    public static int recipeQuantitySlope            = 6;
    public static int recipeQuantityStairs           = 4;
    public static int recipeQuantityBarrier          = 4;
    public static int recipeQuantityGate             = 1;
    public static int recipeQuantityBlock            = 5;
    public static int recipeQuantityButton           = 1;
    public static int recipeQuantityLever            = 1;
    public static int recipeQuantityPressurePlate    = 1;
    public static int recipeQuantityDaylightSensor   = 1;
    public static int recipeQuantityHatch            = 1;
    public static int recipeQuantityDoor             = 1;
    public static int recipeQuantityBed              = 1;
    public static int recipeQuantityLadder           = 4;
    public static int recipeQuantityCollapsibleBlock = 9;
    public static int recipeQuantityTorch            = 4;
    public static int recipeQuantitySafe             = 1;
    public static int recipeQuantityFlowerPot        = 1;
    
    /**
     * Registers block IDs.
     */
    public static void initBlocks(FMLPreInitializationEvent event, Configuration config)
    {
        enableSlope            = config.get("control",             "Enable Slope",            enableSlope).getBoolean(enableSlope);
        enableStairs           = config.get("control",            "Enable Stairs",           enableStairs).getBoolean(enableStairs);
        enableBarrier          = config.get("control",           "Enable Barrier",          enableBarrier).getBoolean(enableBarrier);
        enableGate             = config.get("control",              "Enable Gate",             enableGate).getBoolean(enableGate);
        enableBlock            = config.get("control",        "Enable Block/Slab",            enableBlock).getBoolean(enableBlock);
        enableButton           = config.get("control",            "Enable Button",           enableButton).getBoolean(enableButton);
        enableLever            = config.get("control",             "Enable Lever",            enableLever).getBoolean(enableLever);
        enablePressurePlate    = config.get("control",    "Enable Pressure Plate",    enablePressurePlate).getBoolean(enablePressurePlate);
        enableDaylightSensor   = config.get("control",   "Enable Daylight Sensor",   enableDaylightSensor).getBoolean(enableDaylightSensor);
        enableHatch            = config.get("control",             "Enable Hatch",            enableHatch).getBoolean(enableHatch);
        enableDoor             = config.get("control",              "Enable Door",             enableDoor).getBoolean(enableDoor);
        enableBed              = config.get("control",               "Enable Bed",              enableBed).getBoolean(enableBed);
        enableLadder           = config.get("control",            "Enable Ladder",           enableLadder).getBoolean(enableLadder);
        enableCollapsibleBlock = config.get("control", "Enable Collapsible Block", enableCollapsibleBlock).getBoolean(enableCollapsibleBlock);
        enableTorch            = config.get("control",             "Enable Torch",            enableTorch).getBoolean(enableTorch);
        enableSafe             = config.get("control",              "Enable Safe",             enableSafe).getBoolean(enableSafe);
        enableFlowerPot        = config.get("control",        "Enable Flower Pot",        enableFlowerPot).getBoolean(enableFlowerPot);
        
        recipeQuantitySlope            = config.get("recipe quantities",             "Slope",            recipeQuantitySlope).getInt(recipeQuantitySlope);
        recipeQuantityStairs           = config.get("recipe quantities",            "Stairs",           recipeQuantityStairs).getInt(recipeQuantityStairs);
        recipeQuantityBarrier          = config.get("recipe quantities",           "Barrier",          recipeQuantityBarrier).getInt(recipeQuantityBarrier);
        recipeQuantityGate             = config.get("recipe quantities",              "Gate",             recipeQuantityGate).getInt(recipeQuantityGate);
        recipeQuantityBlock            = config.get("recipe quantities",             "Block",            recipeQuantityBlock).getInt(recipeQuantityBlock);
        recipeQuantityButton           = config.get("recipe quantities",            "Button",           recipeQuantityButton).getInt(recipeQuantityButton);
        recipeQuantityLever            = config.get("recipe quantities",             "Lever",            recipeQuantityLever).getInt(recipeQuantityLever);
        recipeQuantityPressurePlate    = config.get("recipe quantities",    "Pressure Plate",    recipeQuantityPressurePlate).getInt(recipeQuantityPressurePlate);
        recipeQuantityDaylightSensor   = config.get("recipe quantities",   "Daylight Sensor",   recipeQuantityDaylightSensor).getInt(recipeQuantityDaylightSensor);
        recipeQuantityHatch            = config.get("recipe quantities",             "Hatch",            recipeQuantityHatch).getInt(recipeQuantityHatch);
        recipeQuantityDoor             = config.get("recipe quantities",              "Door",             recipeQuantityDoor).getInt(recipeQuantityDoor);
        recipeQuantityBed              = config.get("recipe quantities",               "Bed",              recipeQuantityBed).getInt(recipeQuantityBed);
        recipeQuantityLadder           = config.get("recipe quantities",            "Ladder",           recipeQuantityLadder).getInt(recipeQuantityLadder);
        recipeQuantityCollapsibleBlock = config.get("recipe quantities", "Collapsible Block", recipeQuantityCollapsibleBlock).getInt(recipeQuantityCollapsibleBlock);
        recipeQuantityTorch            = config.get("recipe quantities",             "Torch",            recipeQuantityTorch).getInt(recipeQuantityTorch);
        recipeQuantitySafe             = config.get("recipe quantities",              "Safe",             recipeQuantitySafe).getInt(recipeQuantitySafe);
        recipeQuantityFlowerPot        = config.get("recipe quantities",        "Flower Pot",        recipeQuantityFlowerPot).getInt(recipeQuantityFlowerPot);
    }
    
    /**
     * Create and register blocks.
     */
    public static void registerBlocks()
    {
        if (enableBlock) {
            blockCarpentersBlock = new BlockCarpentersBlock(Material.wood).setBlockName("blockCarpentersBlock").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/quartered_frame");
            GameRegistry.registerBlock(blockCarpentersBlock, "blockCarpentersBlock");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBlock, recipeQuantityBlock), "XXX", "XYX", "XXX", 'X', "stickWood", 'Y', "plankWood"));
        }
        
        if (enableBarrier) {
            blockCarpentersBarrier = new BlockCarpentersBarrier(Material.wood).setBlockName("blockCarpentersBarrier").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersBarrier, "blockCarpentersBarrier");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBarrier, recipeQuantityBarrier), " Y ", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        
        if (enableBed) {
            blockCarpentersBed = new BlockCarpentersBed(Material.wood).setBlockName("blockCarpentersBed").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
        }
        
        if (enableButton) {
            blockCarpentersButton = new BlockCarpentersButton(Material.circuits).setBlockName("blockCarpentersButton").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersButton, "blockCarpentersButton");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersButton, recipeQuantityButton), new Object[] { "X", 'X', blockCarpentersBlock });
        }
        
        if (enableDaylightSensor) {
            blockCarpentersDaylightSensor = new BlockCarpentersDaylightSensor(Material.wood).setBlockName("blockCarpentersDaylightSensor").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersDaylightSensor, "blockCarpentersDaylightSensor");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersDaylightSensor, recipeQuantityDaylightSensor), new Object[] { "WWW", "XYX", "ZZZ", 'W', Blocks.glass, 'X', Items.redstone, 'Y', new ItemStack(Items.dye, 1, 4), 'Z', blockCarpentersBlock });
        }
        
        if (enableDoor) {
            blockCarpentersDoor = new BlockCarpentersDoor(Material.wood).setBlockName("blockCarpentersDoor").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
        }
        
        if (enableGate) {
            blockCarpentersGate = new BlockCarpentersGate(Material.wood).setBlockName("blockCarpentersGate").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersGate, "blockCarpentersGate");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersGate, recipeQuantityGate), "XYX", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        
        if (enableHatch) {
            blockCarpentersHatch = new BlockCarpentersHatch(Material.wood).setBlockName("blockCarpentersHatch").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersHatch, "blockCarpentersHatch");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersHatch, recipeQuantityHatch), new Object[] { "XXX", "XXX", 'X', blockCarpentersBlock });
        }
        
        if (enableLadder) {
            blockCarpentersLadder = new BlockCarpentersLadder(Material.wood).setBlockName("blockCarpentersLadder").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersLadder, "blockCarpentersLadder");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersLadder, recipeQuantityLadder), new Object[] { "X X", "XXX", "X X", 'X', blockCarpentersBlock });
        }
        
        if (enableLever) {
            blockCarpentersLever = new BlockCarpentersLever(Material.circuits).setBlockName("blockCarpentersLever").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersLever, "blockCarpentersLever");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersLever, recipeQuantityLever), "X", "Y", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        
        if (enablePressurePlate) {
            blockCarpentersPressurePlate = new BlockCarpentersPressurePlate(Material.wood).setBlockName("blockCarpentersPressurePlate").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/full_frame");
            GameRegistry.registerBlock(blockCarpentersPressurePlate, "blockCarpentersPressurePlate");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersPressurePlate, recipeQuantityPressurePlate), new Object[] { "XX", 'X', blockCarpentersBlock });
        }
        
        if (enableSlope) {
            blockCarpentersSlope = new BlockCarpentersSlope(Material.wood).setBlockName("blockCarpentersSlope").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/full_frame");
            GameRegistry.registerBlock(blockCarpentersSlope, "blockCarpentersSlope");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersSlope, recipeQuantitySlope), "  X", " XY", "XYY", 'X', "stickWood", 'Y', blockCarpentersBlock));
            
            // Add subblocks
            Object object = (new ItemMultiTexture(blockCarpentersSlope, blockCarpentersSlope, BlockCarpentersSlope.slopeType)).setUnlocalizedName("blockCarpentersSlope");
            Item.itemRegistry.addObject(Block.getIdFromBlock(blockCarpentersSlope), "blockCarpentersSlope", object);            
        }
        
        if (enableStairs) {
            blockCarpentersStairs = new BlockCarpentersStairs(Material.wood).setBlockName("blockCarpentersStairs").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/quartered_frame");
            GameRegistry.registerBlock(blockCarpentersStairs, "blockCarpentersStairs");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersStairs, recipeQuantityStairs), new Object[] { "  X", " XX", "XXX", 'X', blockCarpentersBlock });
        }
        
        if (enableCollapsibleBlock) {
            blockCarpentersCollapsibleBlock = new BlockCarpentersCollapsibleBlock(Material.wood).setBlockName("blockCarpentersCollapsibleBlock").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersCollapsibleBlock, "blockCarpentersCollapsibleBlock");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersCollapsibleBlock, recipeQuantityCollapsibleBlock), new Object[] { "XXX", "XXX", "XXX", 'X', blockCarpentersBlock });
        }
        
        if (enableTorch) {
            blockCarpentersTorch = new BlockCarpentersTorch(Material.circuits).setBlockName("blockCarpentersTorch").setBlockTextureName(CarpentersBlocks.MODID + ":" + "torch/torch_lit");
            GameRegistry.registerBlock(blockCarpentersTorch, "blockCarpentersTorch");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 0), 'Y', blockCarpentersBlock });
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', blockCarpentersBlock });
        }
        
        if (enableSafe) {
            blockCarpentersSafe = new BlockCarpentersSafe(Material.wood).setBlockName("blockCarpentersSafe").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersSafe, "blockCarpentersSafe");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersSafe, recipeQuantitySafe), new Object[] { "XXX", "XYX", "XZX", 'X', blockCarpentersBlock, 'Y', Blocks.iron_block, 'Z', Items.redstone });
        }
        
        if (enableFlowerPot) {
            blockCarpentersFlowerPot = new BlockCarpentersFlowerPot(Material.circuits).setBlockName("blockCarpentersFlowerPot").setBlockTextureName(CarpentersBlocks.MODID + ":" + "general/solid");
            GameRegistry.registerBlock(blockCarpentersFlowerPot, "blockCarpentersFlowerPot");
            GameRegistry.addRecipe(new ItemStack(blockCarpentersFlowerPot, recipeQuantityFlowerPot), new Object[] { "X X", " X ", 'X', blockCarpentersBlock });
        }
    }
    
}
