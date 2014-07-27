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
import carpentersblocks.renderer.BlockHandlerCarpentersBarrier;
import carpentersblocks.renderer.BlockHandlerCarpentersBed;
import carpentersblocks.renderer.BlockHandlerCarpentersBlock;
import carpentersblocks.renderer.BlockHandlerCarpentersButton;
import carpentersblocks.renderer.BlockHandlerCarpentersCollapsibleBlock;
import carpentersblocks.renderer.BlockHandlerCarpentersDaylightSensor;
import carpentersblocks.renderer.BlockHandlerCarpentersDoor;
import carpentersblocks.renderer.BlockHandlerCarpentersFlowerPot;
import carpentersblocks.renderer.BlockHandlerCarpentersGate;
import carpentersblocks.renderer.BlockHandlerCarpentersHatch;
import carpentersblocks.renderer.BlockHandlerCarpentersLadder;
import carpentersblocks.renderer.BlockHandlerCarpentersLever;
import carpentersblocks.renderer.BlockHandlerCarpentersPressurePlate;
import carpentersblocks.renderer.BlockHandlerCarpentersSafe;
import carpentersblocks.renderer.BlockHandlerCarpentersSlope;
import carpentersblocks.renderer.BlockHandlerCarpentersStairs;
import carpentersblocks.renderer.BlockHandlerCarpentersTorch;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class BlockRegistry {

    /* Blocks references. */

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
    public static int recipeQuantityTorch            = 8;

    /**
     * Run preinitialization routines.
     */
    public static void preInit(FMLPreInitializationEvent event, Configuration config)
    {
        enableBarrier          = config.get("blocks",           "Enable Barrier",          enableBarrier).getBoolean(enableBarrier);
        enableBed              = config.get("blocks",               "Enable Bed",              enableBed).getBoolean(enableBed);
        enableButton           = config.get("blocks",            "Enable Button",           enableButton).getBoolean(enableButton);
        enableCollapsibleBlock = config.get("blocks", "Enable Collapsible Block", enableCollapsibleBlock).getBoolean(enableCollapsibleBlock);
        enableDaylightSensor   = config.get("blocks",   "Enable Daylight Sensor",   enableDaylightSensor).getBoolean(enableDaylightSensor);
        enableDoor             = config.get("blocks",              "Enable Door",             enableDoor).getBoolean(enableDoor);
        enableFlowerPot        = config.get("blocks",        "Enable Flower Pot",        enableFlowerPot).getBoolean(enableFlowerPot);
        enableGate             = config.get("blocks",              "Enable Gate",             enableGate).getBoolean(enableGate);
        enableHatch            = config.get("blocks",             "Enable Hatch",            enableHatch).getBoolean(enableHatch);
        enableLadder           = config.get("blocks",            "Enable Ladder",           enableLadder).getBoolean(enableLadder);
        enableLever            = config.get("blocks",             "Enable Lever",            enableLever).getBoolean(enableLever);
        enablePressurePlate    = config.get("blocks",    "Enable Pressure Plate",    enablePressurePlate).getBoolean(enablePressurePlate);
        enableSafe             = config.get("blocks",              "Enable Safe",             enableSafe).getBoolean(enableSafe);
        enableSlope            = config.get("blocks",             "Enable Slope",            enableSlope).getBoolean(enableSlope);
        enableStairs           = config.get("blocks",            "Enable Stairs",           enableStairs).getBoolean(enableStairs);
        enableTorch            = config.get("blocks",             "Enable Torch",            enableTorch).getBoolean(enableTorch);

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

        registerBlocks();
    }

    /**
     * Sets recipes and assigns block render Ids.
     */
    public static void init(FMLInitializationEvent event)
    {
        if (event.getSide().equals(Side.CLIENT)) {

            /* Carpenter's Block is always enabled because recipes rely on it. */

            carpentersBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(carpentersBlockRenderID, new BlockHandlerCarpentersBlock());

            if (enableBarrier) {
                carpentersBarrierRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersBarrierRenderID, new BlockHandlerCarpentersBarrier());
            }
            if (enableButton) {
                carpentersButtonRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersButtonRenderID, new BlockHandlerCarpentersButton());
            }
            if (enableDaylightSensor) {
                carpentersDaylightSensorRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersDaylightSensorRenderID, new BlockHandlerCarpentersDaylightSensor());
            }
            if (enableGate) {
                carpentersGateRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersGateRenderID, new BlockHandlerCarpentersGate());
            }
            if (enableLever) {
                carpentersLeverRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersLeverRenderID, new BlockHandlerCarpentersLever());
            }
            if (enablePressurePlate) {
                carpentersPressurePlateRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersPressurePlateRenderID, new BlockHandlerCarpentersPressurePlate());
            }
            if (enableSlope) {
                carpentersSlopeRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersSlopeRenderID, new BlockHandlerCarpentersSlope());
            }
            if (enableStairs) {
                carpentersStairsRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersStairsRenderID, new BlockHandlerCarpentersStairs());
            }
            if (enableHatch) {
                carpentersHatchRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersHatchRenderID, new BlockHandlerCarpentersHatch());
            }
            if (enableDoor) {
                carpentersDoorRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersDoorRenderID, new BlockHandlerCarpentersDoor());
            }
            if (enableBed) {
                carpentersBedRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersBedRenderID, new BlockHandlerCarpentersBed());
            }
            if (enableLadder) {
                carpentersLadderRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersLadderRenderID, new BlockHandlerCarpentersLadder());
            }
            if (enableCollapsibleBlock) {
                carpentersCollapsibleBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersCollapsibleBlockRenderID, new BlockHandlerCarpentersCollapsibleBlock());
            }
            if (enableTorch) {
                carpentersTorchRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersTorchRenderID, new BlockHandlerCarpentersTorch());
            }
            if (enableSafe) {
                carpentersSafeRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersSafeRenderID, new BlockHandlerCarpentersSafe());
            }
            if (enableFlowerPot) {
                carpentersFlowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
                RenderingRegistry.registerBlockHandler(carpentersFlowerPotRenderID, new BlockHandlerCarpentersFlowerPot());
            }

        }

        registerRecipes();
    }

    private static void registerBlocks()
    {
        /* Carpenter's Block is always enabled because recipes rely on it. */

        blockCarpentersBlock = new BlockCarpentersBlock(Material.wood)
            .setBlockName("blockCarpentersBlock")
            .setHardness(0.2F)
            .setStepSound(BlockProperties.stepSound)
            .setCreativeTab(CarpentersBlocks.creativeTab);
        GameRegistry.registerBlock(blockCarpentersBlock, "blockCarpentersBlock");
        Blocks.fire.setFireInfo(blockCarpentersBlock, 5, 20);

        if (enableBarrier) {
            blockCarpentersBarrier = new BlockCarpentersBarrier(Material.wood)
                .setBlockName("blockCarpentersBarrier")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersBarrier, "blockCarpentersBarrier");
            Blocks.fire.setFireInfo(blockCarpentersBarrier, 5, 20);
        }

        if (enableBed) {
            blockCarpentersBed = new BlockCarpentersBed(Material.wood)
                .setBlockName("blockCarpentersBed")
                .setHardness(0.4F)
                .setStepSound(BlockProperties.stepSound);

            /*
             * This must be set to assign burn properties to block.
             * A side-effect of this is that mods like NEI will enable
             * users to place the block itself, which will result in a crash.
             */
            GameRegistry.registerBlock(blockCarpentersBed, "blockCarpentersBed");
            Blocks.fire.setFireInfo(blockCarpentersBed, 5, 20);
        }

        if (enableButton) {
            blockCarpentersButton = new BlockCarpentersButton(Material.circuits)
                .setBlockName("blockCarpentersButton")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            GameRegistry.registerBlock(blockCarpentersButton, "blockCarpentersButton");
            Blocks.fire.setFireInfo(blockCarpentersButton, 5, 20);
        }

        if (enableCollapsibleBlock) {
            blockCarpentersCollapsibleBlock = new BlockCarpentersCollapsibleBlock(Material.wood)
                .setBlockName("blockCarpentersCollapsibleBlock")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersCollapsibleBlock, "blockCarpentersCollapsibleBlock");
            Blocks.fire.setFireInfo(blockCarpentersCollapsibleBlock, 5, 20);
        }

        if (enableDaylightSensor) {
            blockCarpentersDaylightSensor = new BlockCarpentersDaylightSensor(Material.wood)
                .setBlockName("blockCarpentersDaylightSensor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersDaylightSensor, "blockCarpentersDaylightSensor");
            Blocks.fire.setFireInfo(blockCarpentersDaylightSensor, 5, 20);
        }

        if (enableDoor) {
            blockCarpentersDoor = new BlockCarpentersDoor(Material.wood)
                .setBlockName("blockCarpentersDoor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound);

            /*
             * This must be set to assign burn properties to block.
             * A side-effect of this is that mods like NEI will enable
             * users to place the block itself, which will result in a crash.
             */
            GameRegistry.registerBlock(blockCarpentersDoor, "blockCarpentersDoor");
            Blocks.fire.setFireInfo(blockCarpentersDoor, 5, 20);
        }

        if (enableFlowerPot) {
            blockCarpentersFlowerPot = new BlockCarpentersFlowerPot(Material.circuits)
                .setBlockName("blockCarpentersFlowerPot")
                .setHardness(0.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersFlowerPot, "blockCarpentersFlowerPot");
            Blocks.fire.setFireInfo(blockCarpentersFlowerPot, 5, 20);
        }

        if (enableGate) {
            blockCarpentersGate = new BlockCarpentersGate(Material.wood)
                .setBlockName("blockCarpentersGate")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersGate, "blockCarpentersGate");
            Blocks.fire.setFireInfo(blockCarpentersGate, 5, 20);
        }

        if (enableHatch) {
            blockCarpentersHatch = new BlockCarpentersHatch(Material.wood)
                .setBlockName("blockCarpentersHatch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersHatch, "blockCarpentersHatch");
            Blocks.fire.setFireInfo(blockCarpentersHatch, 5, 20);
        }

        if (enableLadder) {
            blockCarpentersLadder = new BlockCarpentersLadder(Material.wood)
                .setBlockName("blockCarpentersLadder")
                .setHardness(0.2F)
                .setStepSound(Blocks.ladder.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersLadder, "blockCarpentersLadder");
            Blocks.fire.setFireInfo(blockCarpentersLadder, 5, 20);
        }

        if (enableLever) {
            blockCarpentersLever = new BlockCarpentersLever(Material.circuits)
                .setBlockName("blockCarpentersLever")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersLever, "blockCarpentersLever");
            Blocks.fire.setFireInfo(blockCarpentersLever, 5, 20);
        }

        if (enablePressurePlate) {
            blockCarpentersPressurePlate = new BlockCarpentersPressurePlate(Material.wood)
                .setBlockName("blockCarpentersPressurePlate")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            GameRegistry.registerBlock(blockCarpentersPressurePlate, "blockCarpentersPressurePlate");
            Blocks.fire.setFireInfo(blockCarpentersPressurePlate, 5, 20);
        }

        if (enableSafe) {
            blockCarpentersSafe = new BlockCarpentersSafe(Material.wood)
                .setBlockName("blockCarpentersSafe")
                .setHardness(2.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersSafe, "blockCarpentersSafe");
            Blocks.fire.setFireInfo(blockCarpentersSafe, 5, 20);
        }

        if (enableSlope) {
            blockCarpentersSlope = new BlockCarpentersSlope(Material.wood)
                .setBlockName("blockCarpentersSlope")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersSlope, ItemBlockCarpentersSlope.class, "blockCarpentersSlope");
            Blocks.fire.setFireInfo(blockCarpentersSlope, 5, 20);
        }

        if (enableStairs) {
            blockCarpentersStairs = new BlockCarpentersStairs(Material.wood)
                .setBlockName("blockCarpentersStairs")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersStairs, "blockCarpentersStairs");
            Blocks.fire.setFireInfo(blockCarpentersStairs, 5, 20);
        }

        if (enableTorch) {
            blockCarpentersTorch = new BlockCarpentersTorch(Material.circuits)
                .setBlockName("blockCarpentersTorch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setTickRandomly(true);
            GameRegistry.registerBlock(blockCarpentersTorch, "blockCarpentersTorch");
            Blocks.fire.setFireInfo(blockCarpentersTorch, 5, 20);
        }
    }

    private static void registerRecipes()
    {
        /* Carpenter's Block is always enabled because recipes rely on it. */

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBlock, recipeQuantityBlock), "XXX", "XYX", "XXX", 'X', "stickWood", 'Y', "plankWood"));

        if (enableBarrier) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersBarrier, recipeQuantityBarrier), " Y ", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        if (enableButton) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersButton, recipeQuantityButton), new Object[] { "X", 'X', blockCarpentersBlock });
        }
        if (enableCollapsibleBlock) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersCollapsibleBlock, recipeQuantityCollapsibleBlock), new Object[] { "XXX", "XXX", "XXX", 'X', blockCarpentersBlock });
        }
        if (enableDaylightSensor) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersDaylightSensor, recipeQuantityDaylightSensor), new Object[] { "WWW", "XYX", "ZZZ", 'W', Blocks.glass, 'X', Items.redstone, 'Y', new ItemStack(Items.dye, 1, 4), 'Z', blockCarpentersBlock });
        }
        if (enableFlowerPot) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersFlowerPot, recipeQuantityFlowerPot), new Object[] { "X X", " X ", 'X', blockCarpentersBlock });
        }
        if (enableGate) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersGate, recipeQuantityGate), "XYX", "XYX", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        if (enableHatch) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersHatch, recipeQuantityHatch), new Object[] { "XXX", "XXX", 'X', blockCarpentersBlock });
        }
        if (enableLadder) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersLadder, recipeQuantityLadder), new Object[] { "X X", "XXX", "X X", 'X', blockCarpentersBlock });
        }
        if (enableLever) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersLever, recipeQuantityLever), "X", "Y", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        if (enablePressurePlate) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersPressurePlate, recipeQuantityPressurePlate), new Object[] { "XX", 'X', blockCarpentersBlock });
        }
        if (enableSafe) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersSafe, recipeQuantitySafe), new Object[] { "XXX", "XYX", "XZX", 'X', blockCarpentersBlock, 'Y', Blocks.iron_block, 'Z', Items.redstone });
        }
        if (enableSlope) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCarpentersSlope, recipeQuantitySlope), "  X", " XY", "XYY", 'X', "stickWood", 'Y', blockCarpentersBlock));
        }
        if (enableStairs) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersStairs, recipeQuantityStairs), new Object[] { "  X", " XX", "XXX", 'X', blockCarpentersBlock });
        }
        if (enableTorch) {
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 0), 'Y', blockCarpentersBlock });
            GameRegistry.addRecipe(new ItemStack(blockCarpentersTorch, recipeQuantityTorch), new Object[] { "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', blockCarpentersBlock });
        }
    }

}
