package com.carpentersblocks.client;

import java.util.Arrays;
import java.util.List;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.client.renderer.bakedblock.AbstractBakedModel;
import com.carpentersblocks.client.renderer.bakedblock.BakedBlock;
import com.carpentersblocks.client.renderer.bakedblock.BakedCollapsibleBlock;
import com.carpentersblocks.client.renderer.bakedblock.BakedPressurePlate;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeInvertedPrism;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeObliqueExterior;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeObliqueInterior;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopePrism;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopePrismWedge;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeWedge;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeWedgeExterior;
import com.carpentersblocks.client.renderer.bakedblock.slope.BakedSlopeWedgeInterior;
import com.carpentersblocks.util.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, value = { Dist.CLIENT }, bus = Bus.MOD)
public class ClientEventSubscriber {
	
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		registerModel(event, CbBlocks.blockBarrier, BakedBlock.class);
		registerModel(event, CbBlocks.blockBed, BakedBlock.class);
		registerModel(event, CbBlocks.blockBlock, BakedBlock.class);
		registerModel(event, CbBlocks.blockButton, BakedBlock.class);
		registerModel(event, CbBlocks.blockCollapsibleBlock, BakedCollapsibleBlock.class);
		registerModel(event, CbBlocks.blockDaylightSensor, BakedBlock.class);
		registerModel(event, CbBlocks.blockDoor, BakedBlock.class);
		registerModel(event, CbBlocks.blockFlowerPot, BakedBlock.class);
		registerModel(event, CbBlocks.blockGarageDoor, BakedBlock.class);
		registerModel(event, CbBlocks.blockGate, BakedBlock.class);
		registerModel(event, CbBlocks.blockHatch, BakedBlock.class);
		registerModel(event, CbBlocks.blockLadder, BakedBlock.class);
		registerModel(event, CbBlocks.blockLever, BakedBlock.class);
		registerModel(event, CbBlocks.blockPressurePlate, BakedPressurePlate.class);
		registerModel(event, CbBlocks.blockSafe, BakedBlock.class);
		registerModel(event, CbBlocks.blockSlopeInvertedPrism, BakedSlopeInvertedPrism.class);
		registerModel(event, CbBlocks.blockSlopeObliqueExterior, BakedSlopeObliqueExterior.class);
		registerModel(event, CbBlocks.blockSlopeObliqueInterior, BakedSlopeObliqueInterior.class);
		registerModel(event, CbBlocks.blockSlopePrism, BakedSlopePrism.class);
		registerModel(event, CbBlocks.blockSlopePrismWedge, BakedSlopePrismWedge.class);
		registerModel(event, CbBlocks.blockSlopeWedge, BakedSlopeWedge.class);
		registerModel(event, CbBlocks.blockSlopeWedgeExterior, BakedSlopeWedgeExterior.class);
		registerModel(event, CbBlocks.blockSlopeWedgeInterior, BakedSlopeWedgeInterior.class);
		registerModel(event, CbBlocks.blockStairs, BakedBlock.class);
		registerModel(event, CbBlocks.blockTorch, BakedBlock.class);
	}
	
	/**
	 * Registers baked model classes.
	 * 
	 * @param event the event
	 * @param block the block
	 * @param bakedModelClass the baked model class
	 */
	private static void registerModel(ModelBakeEvent event, Block block, Class<? extends AbstractBakedModel> bakedModelClass) {
		try {
			for (BlockState blockState : block.getStateDefinition().getPossibleStates()) {
				ModelResourceLocation variant = BlockModelShapes.stateToModelLocation(blockState);
				event.getModelRegistry().put(variant,
						bakedModelClass.getDeclaredConstructor(VertexFormat.class).newInstance(DefaultVertexFormats.BLOCK));
		    }
			ModelResourceLocation inventoryModelResourceLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
    		event.getModelRegistry().put(inventoryModelResourceLocation,
    				bakedModelClass.getDeclaredConstructor(VertexFormat.class).newInstance(DefaultVertexFormats.NEW_ENTITY));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set render types for blocks.
	 * 
	 * @param event the event
	 */
	@SubscribeEvent
	public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
		List<Block> blocks = ReflectionHelper.getStaticValues(Block.class, CbBlocks.class);
		blocks.forEach(block -> RenderTypeLookup.setRenderLayer(block, renderType -> Arrays.asList(new RenderType[] {
				RenderType.solid(),
				RenderType.cutout(),
				RenderType.cutoutMipped(),
				RenderType.translucent() }).contains(renderType)));
	}
	
}
