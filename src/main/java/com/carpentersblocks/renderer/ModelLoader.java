package com.carpentersblocks.renderer;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.renderer.model.ModelBlock;
import com.carpentersblocks.renderer.model.ModelCollapsible;
import com.carpentersblocks.renderer.model.ModelPressurePlate;
import com.carpentersblocks.util.registry.BlockRegistry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader {

    public static final ModelResourceLocation RESOURCE_LOC_BLOCK = new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":block/carpenters_block");
    public static final ModelResourceLocation RESOURCE_LOC_COLLAPSIBLE_BLOCK = new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":block/carpenters_collapsible_block");
	public static final ModelResourceLocation RESOURCE_LOC_PRESSURE_PLATE = new ModelResourceLocation(CarpentersBlocks.MOD_ID + ":block/carpenters_pressure_plate");
    
    public static final ModelBlock MODEL_BLOCK = new ModelBlock();
    public static final ModelCollapsible MODEL_COLLAPSIBLE_BLOCK = new ModelCollapsible();
    public static final ModelPressurePlate MODEL_PRESSURE_PLATE = new ModelPressurePlate();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
    	return modelLocation.getResourceDomain().equals(CarpentersBlocks.MOD_ID) && BlockRegistry.REGISTRY_NAME_BLOCK.equals(modelLocation.getResourcePath()) ||
    		   modelLocation.getResourceDomain().equals(CarpentersBlocks.MOD_ID) && BlockRegistry.REGISTRY_NAME_COLLAPSIBLE_BLOCK.equals(modelLocation.getResourcePath()) ||
    		   modelLocation.getResourceDomain().equals(CarpentersBlocks.MOD_ID) && BlockRegistry.REGISTRY_NAME_PRESSURE_PLATE.equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation resourceLocation) throws Exception {
    	if (isModel(resourceLocation, BlockRegistry.REGISTRY_NAME_COLLAPSIBLE_BLOCK)) {
    		return MODEL_COLLAPSIBLE_BLOCK;
    	} else if (isModel(resourceLocation, BlockRegistry.REGISTRY_NAME_PRESSURE_PLATE)) {
    		return MODEL_PRESSURE_PLATE;
    	}
        return MODEL_BLOCK;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }
    
    private boolean isModel(ResourceLocation resourceLocation, String registryName) {
    	return resourceLocation.getResourceDomain().equals(CarpentersBlocks.MOD_ID) && registryName.equals(resourceLocation.getResourcePath());
    }
    
}