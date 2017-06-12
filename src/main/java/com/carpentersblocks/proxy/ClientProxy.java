package com.carpentersblocks.proxy;

import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.renderer.ModelLoader;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event, Configuration config) {
    	super.preInit(event, config);
        ModelLoaderRegistry.registerLoader(new ModelLoader());
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new SpriteRegistry());
        CarpentersBlocksCachedResources.INSTANCE.init();
        BlockRegistry.registerRenderers();
    	//Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(CbBlockColor.INSTANCE, BlockRegistry.blockCarpentersBlock);
    	
        // TODO: Check if these are needed
        //if (FMLClientHandler.instance().hasOptifine()) {
        //    OptifineHandler.init();
        //}

        //ShadersHandler.init();

        // Register entity renderers
        //RenderingRegistry.registerEntityRenderingHandler(EntityCarpentersTile.class, new RenderCarpentersTile());
    }
	
}
