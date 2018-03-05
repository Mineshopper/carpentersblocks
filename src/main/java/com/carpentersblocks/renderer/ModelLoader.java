package com.carpentersblocks.renderer;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.renderer.model.ModelBlock;
import com.carpentersblocks.renderer.model.ModelCollapsibleBlock;
import com.carpentersblocks.renderer.model.ModelPressurePlate;
import com.carpentersblocks.renderer.model.ModelSlope;
import com.carpentersblocks.util.registry.BlockRegistry;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader {
	
    @Override
    public boolean accepts(ResourceLocation resourceLocation) {
    	IModel model = null;
    	try { model = loadModel(resourceLocation); } catch (Exception ex) {}
    	return CarpentersBlocks.MOD_ID.equalsIgnoreCase(resourceLocation.getResourceDomain())
    			&& model != null;
    }

    @Override
    public IModel loadModel(ResourceLocation resourceLocation) throws Exception {
    	if (resourceLocation.getResourcePath().equals(BlockRegistry.REGISTRY_NAME_BLOCK)) {
    		return new ModelBlock();
    	} else if (resourceLocation.getResourcePath().endsWith(BlockRegistry.REGISTRY_NAME_SLOPE)) {
    		return new ModelSlope();
    	} else if (resourceLocation.getResourcePath().equals(BlockRegistry.REGISTRY_NAME_COLLAPSIBLE_BLOCK)) {
    		return new ModelCollapsibleBlock();
    	} else if (resourceLocation.getResourcePath().equals(BlockRegistry.REGISTRY_NAME_PRESSURE_PLATE)) {
    		return new ModelPressurePlate();
    	} else {
    		return null;
    	}
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }
    
}