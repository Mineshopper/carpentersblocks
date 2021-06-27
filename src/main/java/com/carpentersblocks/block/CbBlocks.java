package com.carpentersblocks.block;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.ModLogger;
import com.carpentersblocks.util.ReflectionHelper;
import com.carpentersblocks.util.states.StateMap;
import com.carpentersblocks.util.states.loader.StateLoader;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class CbBlocks {
	
	public static final String REGISTRY_NAME_BARRIER = "barrier";
	public static final String REGISTRY_NAME_BED = "bed";
	public static final String REGISTRY_NAME_BLOCK = "block";
	public static final String REGISTRY_NAME_BUTTON = "button";
	public static final String REGISTRY_NAME_COLLAPSIBLE_BLOCK = "collapsible_block";
	public static final String REGISTRY_NAME_DAYLIGHT_SENSOR = "daylight_sensor";
	public static final String REGISTRY_NAME_DOOR = "door";
	public static final String REGISTRY_NAME_FLOWER_POT = "flower_pot";
	public static final String REGISTRY_NAME_GARAGE_DOOR = "garage_door";
	public static final String REGISTRY_NAME_GATE = "gate";
	public static final String REGISTRY_NAME_HATCH = "hatch";
	public static final String REGISTRY_NAME_LADDER = "ladder";
	public static final String REGISTRY_NAME_LEVER = "lever";
	public static final String REGISTRY_NAME_PRESSURE_PLATE = "pressure_plate";
	public static final String REGISTRY_NAME_SAFE = "safe";
	public static final String REGISTRY_NAME_SLOPE_WEDGE = "slope_wedge";
    public static final String REGISTRY_NAME_SLOPE_WEDGE_INTERIOR = "slope_wedge_interior";
    public static final String REGISTRY_NAME_SLOPE_WEDGE_EXTERIOR = "slope_wedge_exterior";
    public static final String REGISTRY_NAME_SLOPE_OBLIQUE_INTERIOR = "slope_oblique_interior";
    public static final String REGISTRY_NAME_SLOPE_OBLIQUE_EXTERIOR = "slope_oblique_exterior";
    public static final String REGISTRY_NAME_SLOPE_PRISM_WEDGE = "slope_prism_wedge";
    public static final String REGISTRY_NAME_SLOPE_PRISM = "slope_prism";
    public static final String REGISTRY_NAME_SLOPE_INVERTED_PRISM = "slope_inverted_prism";
	public static final String REGISTRY_NAME_STAIRS = "stairs";
	public static final String REGISTRY_NAME_TORCH = "torch";
	
    public static Block blockBarrier;
    public static Block blockBed;
    public static Block blockBlock;
    public static Block blockButton;
    public static Block blockCollapsibleBlock;
    public static Block blockDaylightSensor;
    public static Block blockDoor;
    public static Block blockFlowerPot;
    public static Block blockGarageDoor;
    public static Block blockGate;
    public static Block blockHatch;
    public static Block blockLadder;
    public static Block blockLever;
    public static Block blockPressurePlate;
    public static Block blockSafe;
    public static Block blockSlopeWedge;
    public static Block blockSlopeWedgeInterior;
    public static Block blockSlopeWedgeExterior;
    public static Block blockSlopeObliqueInterior;
    public static Block blockSlopeObliqueExterior;
    public static Block blockSlopePrismWedge;
    public static Block blockSlopePrism;
    public static Block blockSlopeInvertedPrism;
    public static Block blockStairs;
    public static Block blockTorch;
    	
	@SubscribeEvent
    public static void onRegisterBlockEvent(RegistryEvent.Register<Block> event) {
		// barrier
		blockBarrier = registerBlock(BlockCarpentersBarrier.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_BARRIER,
    			event);
	    
    	// bed
		blockBed = registerBlock(BlockCarpentersBed.class,
				Block.Properties.of(Material.WOOL, MaterialColor.WOOL)
					.strength(0.2F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_BED,
    			event);

	    // block
		blockBlock = registerBlock(BlockCarpentersBlock.class,
				Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_BLOCK,
    			event);
    	
	    // button
		blockButton = registerBlock(BlockCarpentersButton.class,
				Block.Properties.of(Material.DECORATION)
					.noCollission()
					.strength(0.5F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_BUTTON,
    			event);
    	
	    // collapsible block
		blockCollapsibleBlock = registerBlock(BlockCarpentersCollapsibleBlock.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_COLLAPSIBLE_BLOCK,
    			event);
    	
	    // daylight sensor
		blockDaylightSensor = registerBlock(BlockCarpentersDaylightSensor.class,
				Block.Properties.of(Material.WOOD)
					.strength(0.2F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_DAYLIGHT_SENSOR,
    			event);
    	
	    // door
		blockDoor = registerBlock(BlockCarpentersDoor.class,
	    		Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
	    			.strength(3.0F)
	    			.sound(SoundType.WOOD),
				REGISTRY_NAME_DOOR,
    			event);
    	
	    // flower pot
		blockFlowerPot = registerBlock(BlockCarpentersFlowerPot.class,
	    		Block.Properties.of(Material.DECORATION)
	    			.instabreak(),
				REGISTRY_NAME_FLOWER_POT,
    			event);
    	
	    // garage door
		blockGarageDoor = registerBlock(BlockCarpentersGarageDoor.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_GARAGE_DOOR,
    			event);
    	
	    // gate
		blockGate = registerBlock(BlockCarpentersGate.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_GATE,
    			event);
    	
	    // hatch
 		blockHatch = registerBlock(BlockCarpentersHatch.class,
 				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
 					.strength(3.0F)
 					.sound(SoundType.WOOD),
 				REGISTRY_NAME_HATCH,
    			event);
	    
	    // ladder
		blockLadder = registerBlock(BlockCarpentersLadder.class,
				Block.Properties.of(Material.DECORATION)
					.strength(0.4F)
					.sound(SoundType.LADDER),
				REGISTRY_NAME_LADDER,
    			event);
    	
	    // lever
		blockLever = registerBlock(BlockCarpentersLever.class,
				Block.Properties.of(Material.DECORATION)
					.noCollission()
					.strength(0.5F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_LEVER,
    			event);
    	
	    // pressure plate
		blockPressurePlate = registerBlock(BlockCarpentersPressurePlate.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.noCollission()
					.strength(0.5F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_PRESSURE_PLATE,
    			event);
    	
	    // safe
    	blockSafe = registerBlock(BlockCarpentersSafe.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_SAFE,
    			event);
    	
	    // slope
    	Properties properties = Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
    			.strength(2.0F, 3.0F)
    			.sound(SoundType.WOOD);
    	blockSlopeInvertedPrism = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_INVERTED_PRISM, event);
    	blockSlopeObliqueExterior = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_OBLIQUE_EXTERIOR, event);
    	blockSlopeObliqueInterior = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_OBLIQUE_INTERIOR, event);
    	blockSlopePrism = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_PRISM, event);
    	blockSlopePrismWedge = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_PRISM_WEDGE, event);
    	blockSlopeWedge = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_WEDGE, event);
    	blockSlopeWedgeExterior = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_WEDGE_EXTERIOR, event);
    	blockSlopeWedgeInterior = registerBlock(BlockCarpentersSlope.class, properties, REGISTRY_NAME_SLOPE_WEDGE_INTERIOR, event);
    	
	    // stairs
    	blockStairs = registerBlock(BlockCarpentersStairs.class,
				Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
					.strength(2.0F, 3.0F)
					.sound(SoundType.WOOD),
				REGISTRY_NAME_STAIRS,
    			event);
	    
	    // torch
    	blockTorch = registerBlock(BlockCarpentersTorch.class,
    			Block.Properties.of(Material.DECORATION)
    				.noCollission()
    				.instabreak()
    				.lightLevel((state) -> {
    					return 14;
    				})
    				.sound(SoundType.WOOD),
    			REGISTRY_NAME_TORCH,
    			event);
    }
	
	/**
	 * Registers the block and applies common properties.
	 * 
	 * @param blockType the block type
	 * @param properties the block properties
	 * @param registryName the block registry name
	 * @Param event the block registry event
	 * @return a block instance
	 */
	private static Block registerBlock(Class<? extends Block> blockType, Properties properties, String registryName, RegistryEvent.Register<Block> event) {
		Block block = null;
		try {
			if (IStateImplementor.class.isAssignableFrom(blockType)) {
				block = (Block) blockType
						.getDeclaredConstructor(Properties.class, StateMap.class)
						.newInstance(properties, new StateLoader(registryName).getStateMap());
			} else {
				block = (Block) blockType.getDeclaredConstructor(Properties.class).newInstance(properties);
			}
		} catch (Exception ex) {
			ModLogger.log(Level.ERROR, "Failed to register block '{}': {}", blockType, ex.getMessage());
			return null;
		}
		block.setRegistryName(CarpentersBlocks.MOD_ID, registryName);
		event.getRegistry().register(block);
	    ReflectionHelper.invokePrivateMethod(((FireBlock) Blocks.FIRE),
				"setFlammable",
				Pair.of(Block.class, block),
				Pair.of(int.class, 5),
				Pair.of(int.class, 20));
	    return block;
	}
	
}
