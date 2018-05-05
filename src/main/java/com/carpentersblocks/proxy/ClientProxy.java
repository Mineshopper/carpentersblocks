package com.carpentersblocks.proxy;

import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.renderer.ModelLoader;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.ConfigRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    	super.preInit(event);
    	MinecraftForge.EVENT_BUS.register(new SpriteRegistry());
        ModelLoaderRegistry.registerLoader(new ModelLoader());
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        CarpentersBlocksCachedResources.INSTANCE.init();
        //BlockRegistry.registerRenderers();
    	//Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(CbBlockColor.INSTANCE, BlockRegistry.blockCarpentersBlock);

        //ShadersHandler.init();

        // Register entity renderers
        //RenderingRegistry.registerEntityRenderingHandler(EntityCarpentersTile.class, new RenderCarpentersTile());
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
    	if (FMLClientHandler.instance().hasOptifine()) {
    		ModLogger.info("Optifine detected. Disabling custom vertexformat.");
    		ConfigRegistry.enableOptifineCompatibility = true;
    	}
    }
	
}
