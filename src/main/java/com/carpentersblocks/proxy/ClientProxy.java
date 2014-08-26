package com.carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.entity.item.EntityCarpentersTile;
import com.carpentersblocks.renderer.entity.RenderCarpentersTile;
import com.carpentersblocks.util.handler.OptifineHandler;
import com.carpentersblocks.util.handler.ShadersHandler;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new IconRegistry());
        CarpentersBlocksCachedResources.INSTANCE.init();

        if (FMLClientHandler.instance().hasOptifine()) {
            OptifineHandler.init();
        }

        ShadersHandler.init();

        /* Register entity renderers */

        RenderingRegistry.registerEntityRenderingHandler(EntityCarpentersTile.class, new RenderCarpentersTile());
    }

}
