package com.carpentersblocks.util.registry;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.block.BlockCarpentersBlock;
import com.carpentersblocks.block.BlockCarpentersCollapsibleBlock;
import com.carpentersblocks.block.BlockCarpentersPressurePlate;
import com.carpentersblocks.block.BlockCarpentersSlope;
import com.carpentersblocks.util.states.StateMap;
import com.carpentersblocks.util.states.loader.StateLoader;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRegistry {
	
	private static final Material material = Material.WOOD;
	
	public static final String REGISTRY_NAME_BLOCK = "carpenters_block";
	public static final String REGISTRY_NAME_COLLAPSIBLE_BLOCK = "carpenters_collapsible_block";
	public static final String REGISTRY_NAME_PRESSURE_PLATE = "carpenters_pressure_plate";
	public static final String REGISTRY_NAME_SLOPE = "carpenters_slope";
	private static final String VARIANT_INVENTORY = "inventory";
	
    public static Block blockCarpentersBarrier;
    public static Block blockCarpentersBed;
    public static Block blockCarpentersBlock;
    public static Block blockCarpentersButton;
    public static Block blockCarpentersCollapsibleBlock;
    public static Block blockCarpentersDaylightSensor;
    public static Block blockCarpentersDoor;
    public static Block blockCarpentersFlowerPot;
    public static Block blockCarpentersGarageDoor;
    public static Block blockCarpentersGate;
    public static Block blockCarpentersHatch;
    public static Block blockCarpentersLadder;
    public static Block blockCarpentersLever;
    public static Block blockCarpentersPressurePlate;
    public static Block blockCarpentersSafe;
    public static Block blockCarpentersSlope;
    public static Block blockCarpentersStairs;
    public static Block blockCarpentersTorch;
    
    public static void preInit(FMLPreInitializationEvent event) {
    	{ // Carpenters Block always enabled
	    	blockCarpentersBlock = new BlockCarpentersBlock(material)
        		.setUnlocalizedName("blockCarpentersBlock")
            	.setRegistryName(REGISTRY_NAME_BLOCK)
            	.setHardness(0.2F)
                .setCreativeTab(CarpentersBlocks.CREATIVE_TAB);
	        Blocks.FIRE.setFireInfo(blockCarpentersBlock, 5, 20);
    	}
    	/*
		if (ConfigRegistry.enableBarrier) {
    		blockCarpentersBarrier = new BlockCarpentersBarrier(Material.wood)
                .setBlockName("blockCarpentersBarrier")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersBarrier, "blockCarpentersBarrier");
            Blocks.fire.setFireInfo(blockCarpentersBarrier, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableBed) {
    		blockCarpentersBed = new BlockCarpentersBed(Material.wood)
				.setBlockName("blockCarpentersBed")
				.setHardness(0.4F)
				.setStepSound(BlockProperties.stepSound);
			// This must be set to assign burn properties to block.
			// A side-effect of this is that mods like NEI will enable
			// users to place the block itself, which will result in a crash.
            GameRegistry.registerBlock(blockCarpentersBed, "blockCarpentersBed");
            Blocks.fire.setFireInfo(blockCarpentersBed, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableButton) {
    		blockCarpentersButton = new BlockCarpentersButton(Material.circuits)
                .setBlockName("blockCarpentersButton")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersButton, ItemBlockSided.class, "blockCarpentersButton");
            Blocks.fire.setFireInfo(blockCarpentersButton, 5, 20);
    	}
    	*/
    	if (ConfigRegistry.enableCollapsibleBlock) {
    		blockCarpentersCollapsibleBlock = new BlockCarpentersCollapsibleBlock(material)
                .setUnlocalizedName("blockCarpentersCollapsibleBlock")
                .setRegistryName(REGISTRY_NAME_COLLAPSIBLE_BLOCK)
                .setHardness(0.2F)
                .setCreativeTab(CarpentersBlocks.CREATIVE_TAB);
            Blocks.FIRE.setFireInfo(blockCarpentersCollapsibleBlock, 5, 20);
    	}
    	/*
    	if (ConfigRegistry.enableDaylightSensor) {
    		blockCarpentersDaylightSensor = new BlockCarpentersDaylightSensor(Material.wood)
                .setBlockName("blockCarpentersDaylightSensor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersDaylightSensor, ItemBlockSided.class, "blockCarpentersDaylightSensor");
            Blocks.fire.setFireInfo(blockCarpentersDaylightSensor, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableDoor) {
    		blockCarpentersDoor = new BlockCarpentersDoor(Material.wood)
                .setBlockName("blockCarpentersDoor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound);
            // This must be set to assign burn properties to block.
            // A side-effect of this is that mods like NEI will enable
            // users to place the block itself, which will result in a crash.
            GameRegistry.registerBlock(blockCarpentersDoor, "blockCarpentersDoor");
            Blocks.fire.setFireInfo(blockCarpentersDoor, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableFlowerPot) {
    		blockCarpentersFlowerPot = new BlockCarpentersFlowerPot(Material.circuits)
                .setBlockName("blockCarpentersFlowerPot")
                .setHardness(0.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersFlowerPot, "blockCarpentersFlowerPot");
            Blocks.fire.setFireInfo(blockCarpentersFlowerPot, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableGarageDoor) {
    		blockCarpentersGarageDoor = new BlockCarpentersGarageDoor(Material.wood)
                .setBlockName("blockCarpentersGarageDoor")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersGarageDoor, "blockCarpentersGarageDoor");
            Blocks.fire.setFireInfo(blockCarpentersGarageDoor, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableGate) {
    		blockCarpentersGate = new BlockCarpentersGate(Material.wood)
                .setBlockName("blockCarpentersGate")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersGate, "blockCarpentersGate");
            Blocks.fire.setFireInfo(blockCarpentersGate, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableHatch) {
    		blockCarpentersHatch = new BlockCarpentersHatch(Material.wood)
                .setBlockName("blockCarpentersHatch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersHatch, "blockCarpentersHatch");
            Blocks.fire.setFireInfo(blockCarpentersHatch, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableLadder) {
    		blockCarpentersLadder = new BlockCarpentersLadder(Material.wood)
                .setBlockName("blockCarpentersLadder")
                .setHardness(0.2F)
                .setStepSound(Blocks.ladder.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersLadder, "blockCarpentersLadder");
            Blocks.fire.setFireInfo(blockCarpentersLadder, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableLever) {
    		blockCarpentersLever = new BlockCarpentersLever(Material.circuits)
                .setBlockName("blockCarpentersLever")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersLever, ItemBlockSided.class, "blockCarpentersLever");
            Blocks.fire.setFireInfo(blockCarpentersLever, 5, 20);
    	}
    	*/
    	if (ConfigRegistry.enablePressurePlate) {
        	StateLoader stateLoader = new StateLoader(REGISTRY_NAME_PRESSURE_PLATE);
        	StateMap stateMap = stateLoader.getStateMap();
        	blockCarpentersPressurePlate = new BlockCarpentersPressurePlate(Material.CIRCUITS, stateMap)
                .setUnlocalizedName("blockCarpentersPressurePlate")
                .setRegistryName(REGISTRY_NAME_PRESSURE_PLATE)
	            .setHardness(0.2F)
	            .setCreativeTab(CarpentersBlocks.CREATIVE_TAB);
	        Blocks.FIRE.setFireInfo(blockCarpentersPressurePlate, 5, 20);
    	}
    	/*
    	if (ConfigRegistry.enableSafe) {
    		blockCarpentersSafe = new BlockCarpentersSafe(Material.wood)
                .setBlockName("blockCarpentersSafe")
                .setHardness(2.5F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersSafe, "blockCarpentersSafe");
            Blocks.fire.setFireInfo(blockCarpentersSafe, 5, 20);
    	}
    	*/
    	if (ConfigRegistry.enableSlope) {
    		blockCarpentersSlope = new BlockCarpentersSlope(Material.WOOD)
                .setUnlocalizedName("blockCarpentersSlope")
                .setRegistryName(REGISTRY_NAME_SLOPE)
                .setHardness(0.2F)
                .setCreativeTab(CarpentersBlocks.CREATIVE_TAB);
            Blocks.FIRE.setFireInfo(blockCarpentersSlope, 5, 20);
    	}
    	/*
    	if (ConfigRegistry.enableStairs) {
    	  	blockCarpentersStairs = new BlockCarpentersStairs(Material.wood)
                .setBlockName("blockCarpentersStairs")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab);
            GameRegistry.registerBlock(blockCarpentersStairs, "blockCarpentersStairs");
            Blocks.fire.setFireInfo(blockCarpentersStairs, 5, 20);
    	}
    	*/
    	/*
    	if (ConfigRegistry.enableTorch) {
    		blockCarpentersTorch = new BlockCarpentersTorch(Material.circuits)
                .setBlockName("blockCarpentersTorch")
                .setHardness(0.2F)
                .setStepSound(BlockProperties.stepSound)
                .setCreativeTab(CarpentersBlocks.creativeTab)
                .setLightLevel(1.0F);
            if (ConfigRegistry.enableTorchWeatherEffects) {
            	blockCarpentersTorch.setTickRandomly(true);
            }
            GameRegistry.registerBlock(blockCarpentersTorch, ItemBlockSided.class, "blockCarpentersTorch");
            Blocks.fire.setFireInfo(blockCarpentersTorch, 5, 20);
    	}
    	*/
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
    	{ // Carpenters Block always enabled
    		event.getRegistry().register(this.blockCarpentersBlock);
    	}
		if (ConfigRegistry.enableBarrier) {
    		event.getRegistry().register(this.blockCarpentersBarrier);
    	}
    	if (ConfigRegistry.enableBed) {
    		event.getRegistry().register(this.blockCarpentersBed);
    	}
    	if (ConfigRegistry.enableButton) {
    		event.getRegistry().register(this.blockCarpentersButton);
    	}
    	if (ConfigRegistry.enableCollapsibleBlock) {
    		event.getRegistry().register(this.blockCarpentersCollapsibleBlock);
    	}
    	if (ConfigRegistry.enableDaylightSensor) {
    		event.getRegistry().register(this.blockCarpentersDaylightSensor);
    	}
    	if (ConfigRegistry.enableDoor) {
    		event.getRegistry().register(this.blockCarpentersDoor);
    	}
    	if (ConfigRegistry.enableFlowerPot) {
    		event.getRegistry().register(this.blockCarpentersFlowerPot);
    	}
    	if (ConfigRegistry.enableGarageDoor) {
    		event.getRegistry().register(this.blockCarpentersGarageDoor);
    	}
    	if (ConfigRegistry.enableGate) {
    		event.getRegistry().register(this.blockCarpentersGate);
    	}
    	if (ConfigRegistry.enableHatch) {
    		event.getRegistry().register(this.blockCarpentersHatch);
    	}
    	if (ConfigRegistry.enableLadder) {
    		event.getRegistry().register(this.blockCarpentersLadder);
    	}
    	if (ConfigRegistry.enableLever) {
    		event.getRegistry().register(this.blockCarpentersLever);
    	}
    	if (ConfigRegistry.enablePressurePlate) {
    		event.getRegistry().register(this.blockCarpentersPressurePlate);
    	}
    	if (ConfigRegistry.enableSafe) {
    		event.getRegistry().register(this.blockCarpentersSafe);
    	}
    	if (ConfigRegistry.enableSlope) {
    		event.getRegistry().register(this.blockCarpentersSlope);
    	}
    	if (ConfigRegistry.enableStairs) {
    		event.getRegistry().register(this.blockCarpentersStairs);
    	}
    	if (ConfigRegistry.enableTorch) {
    		event.getRegistry().register(this.blockCarpentersTorch);
    	}
    }
    
    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
    	{ // Carpenters Block always enabled
	        event.getRegistry().register(new ItemBlock(this.blockCarpentersBlock).setRegistryName(this.REGISTRY_NAME_BLOCK));
    	}
    	if (ConfigRegistry.enableBarrier) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersBarrier));
    	}
    	if (ConfigRegistry.enableBed) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersBed));
    	}
    	if (ConfigRegistry.enableButton) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersButton));
    	}
    	if (ConfigRegistry.enableCollapsibleBlock) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersCollapsibleBlock).setRegistryName(this.REGISTRY_NAME_COLLAPSIBLE_BLOCK));
    	}
    	if (ConfigRegistry.enableDaylightSensor) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersDaylightSensor));
    	}
    	if (ConfigRegistry.enableDoor) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersDoor));
    	}
    	if (ConfigRegistry.enableFlowerPot) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersFlowerPot));
    	}
    	if (ConfigRegistry.enableGarageDoor) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersGarageDoor));
    	}
    	if (ConfigRegistry.enableGate) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersGate));
    	}
    	if (ConfigRegistry.enableHatch) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersHatch));
    	}
    	if (ConfigRegistry.enableLadder) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersLadder));
    	}
    	if (ConfigRegistry.enableLever) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersLever));
    	}
    	if (ConfigRegistry.enablePressurePlate) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersPressurePlate).setRegistryName(this.REGISTRY_NAME_PRESSURE_PLATE));
    	}
    	if (ConfigRegistry.enableSafe) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersSafe));
    	}
    	if (ConfigRegistry.enableSlope) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersSlope).setRegistryName(this.REGISTRY_NAME_SLOPE));
    	}
    	if (ConfigRegistry.enableStairs) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersStairs));
    	}
    	if (ConfigRegistry.enableTorch) {
    		event.getRegistry().register(new ItemBlock(this.blockCarpentersTorch));
    	}
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerItemBlockRender(Block block, String variant, int metadata) {
		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(block.getRegistryName(), variant);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), metadata, modelResourceLocation);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {
    	registerItemBlockRender(blockCarpentersBlock, VARIANT_INVENTORY, 0);

        /*
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
        if (enableGarageDoor) {
            carpentersGarageDoorRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(carpentersGarageDoorRenderID, new BlockHandlerCarpentersGarageDoor());
        }
        if (enableGate) {
            carpentersGateRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(carpentersGateRenderID, new BlockHandlerCarpentersGate());
        }
        if (enableLever) {
            carpentersLeverRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(carpentersLeverRenderID, new BlockHandlerCarpentersLever());
        }
        */
        if (ConfigRegistry.enablePressurePlate) {
        	registerItemBlockRender(blockCarpentersPressurePlate, VARIANT_INVENTORY, 0);
        	registerItemBlockRender(blockCarpentersPressurePlate, "depressed", 1);
        }
        if (ConfigRegistry.enableSlope) {
        	registerItemBlockRender(blockCarpentersSlope, VARIANT_INVENTORY, 0);
        }
        /*
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
        */
        if (ConfigRegistry.enableCollapsibleBlock) {
        	registerItemBlockRender(blockCarpentersCollapsibleBlock, VARIANT_INVENTORY, 0);
        }
        /*
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
        */
    }

}
